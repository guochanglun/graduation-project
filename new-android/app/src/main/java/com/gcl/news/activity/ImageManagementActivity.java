package com.gcl.news.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.bean.Image;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ImageManagementActivity extends AppCompatActivity {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    private OkHttpClient client = RuntimeObject.client;
    private Gson gson = RuntimeObject.gson;

    private static final String TAG = "ImageManagementActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_manage);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("图片管理");

        refreshLayout = findViewById(R.id.refresh);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshImage();
                Toast.makeText(ImageManagementActivity.this, "刷新成功", Toast.LENGTH_SHORT).show();
                refreshLayout.setRefreshing(false);
            }
        });

        listView = findViewById(R.id.img_list);
        refreshImage();
    }

    private void refreshImage(){
        final Request request = new Request.Builder().url(Properties.BASE_IMG_URL).get().build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: 获取图片失败");
                setResult(Properties.RESULT_CODE_ERROR);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String json = response.body().string();
                JsonParser parser = new JsonParser();
                JsonArray jsonArray = parser.parse(json).getAsJsonArray();
                final List<Image> images = new ArrayList<>();
                for (JsonElement img : jsonArray) {
                    Image image = gson.fromJson(img, Image.class);
                    images.add(image);
                }
                final ImageAdapter adapter = new ImageAdapter(ImageManagementActivity.this, images);
                ImageManagementActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                                final Image image = images.get(position);
                                AlertDialog dialog = new AlertDialog.Builder(ImageManagementActivity.this)
                                        .setTitle("删除图片")
                                        .setMessage("删除" + image.getImgName() + "?")
                                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.dismiss();
                                            }
                                        })
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                // 删除服务器中的图片
                                                Request delete = new Request.Builder()
                                                        .url(Properties.BASE_IMG_URL + "/" + image.getImgName())
                                                        .delete().build();
                                                RuntimeObject.client.newCall(delete).enqueue(new Callback() {
                                                    @Override
                                                    public void onFailure(Call call, IOException e) {
                                                        ImageManagementActivity.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(
                                                                        ImageManagementActivity.this,
                                                                        "删除失败",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onResponse(Call call, Response response) throws IOException {
                                                        ImageManagementActivity.this.runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                Toast.makeText(
                                                                        ImageManagementActivity.this,
                                                                        "删除成功",
                                                                        Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                                    }
                                                });
                                                images.remove(position);
                                                adapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        })
                                        .create();
                                dialog.show();
                            }
                        });
                    }
                });
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.image_management_action_bar_menu, menu);
        return true;
    }

    class ImageAdapter extends BaseAdapter {

        private List<Image> images;
        private Context context;

        public ImageAdapter(Context context, List<Image> images){
            this.images = images;
            this.context = context;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null)
            {
                holder = new ImageAdapter.ViewHolder();
                convertView = View.inflate(this.context, R.layout.image_item, null);
                holder.img = convertView.findViewById(R.id.img);
                holder.imgDesc = convertView.findViewById(R.id.img_desc);
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            Image image = this.images.get(position);
            Picasso.with(context)
                    .load(Properties.BASE_IMG_URL+"/"+image.getImgName())
                    .placeholder(R.drawable.image)
                    .error(R.drawable.line)
                    .into(holder.img);
            holder.imgDesc.setText(image.getImgDesc());
            holder.id = image.getId();

            return convertView;
        }

        class ViewHolder{
            public Integer id;
            public ImageView img;
            public TextView imgDesc;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.image_upload:
                startActivity(new Intent(this, UploadImageActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
