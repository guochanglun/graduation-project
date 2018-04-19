package com.gcl.news.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.gcl.news.R;
import com.gcl.news.bean.Image;
import com.gcl.news.utils.Properties;
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

/**
 * 选择网络上的图片
 */
public class ImageSelectActivity extends AppCompatActivity {

    private ListView listView;
    private SwipeRefreshLayout refreshLayout;

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    private static final String TAG = "ImageSelectActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_select);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("选择图片");

        listView = findViewById(R.id.img_list);
        refreshLayout = findViewById(R.id.main_srl);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(false);
            }
        });

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
                List<Image> images = new ArrayList<>();
                for (JsonElement img : jsonArray) {
                    Image image = gson.fromJson(img, Image.class);
                    images.add(image);
                }
                final ImageAdapter adapter = new ImageAdapter(ImageSelectActivity.this, images);
                ImageSelectActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setAdapter(adapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Image image = (Image) adapter.getItem(position);
                                Intent intent = new Intent();
                                intent.putExtra("imgName", image.getImgName());
                                setResult(Properties.RESULT_CODE_SUCCESS, intent);
                                finish();
                            }
                        });
                    }
                });
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            setResult(Properties.RESULT_CODE_ERROR);
        }
        return super.onKeyDown(keyCode, event);
    }

    class ImageAdapter extends BaseAdapter{

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
                holder = new ViewHolder();
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
                setResult(Properties.RESULT_CODE_ERROR);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
