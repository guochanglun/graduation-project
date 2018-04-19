package com.gcl.news.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.bean.News;
import com.gcl.news.bean.NewsDraft;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UploadActivity extends AppCompatActivity {

    private ListView uploadList;
    private List<News> newsList;
    private NewsAdapter adapter;

    private static final String TAG = "UploadActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        uploadList = findViewById(R.id.upload_list);
        uploadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 启动web浏览器加载页面
                Intent intent = new Intent(getBaseContext(), WebActivity.class);
                News news = newsList.get(position);
                intent.putExtra("title", news.getTitle());
                intent.putExtra("url", Properties.BASE_NEWS_URL + "/" + news.getArticleId());
                startActivity(intent);
            }
        });

        uploadList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                final News news = newsList.get(position);
                AlertDialog dialog = new AlertDialog.Builder(UploadActivity.this)
                        .setTitle("删除文章？")
                        .setMessage(news.getTitle())
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(final DialogInterface dialog, int which) {
                                // 删除文章
                                Request request = new Request.Builder()
                                        .url(Properties.BASE_NEWS_URL + "/" + news.getId())
                                        .delete().build();
                                RuntimeObject.client.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        UploadActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(),
                                                        "删除失败", Toast.LENGTH_SHORT)
                                                        .show();
                                            }
                                        });
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        UploadActivity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getBaseContext(),
                                                        "删除成功", Toast.LENGTH_SHORT)
                                                        .show();
                                                newsList.remove(position);
                                                adapter.notifyDataSetChanged();
                                                dialog.dismiss();
                                            }
                                        });
                                    }
                                });
                            }
                        }).create();
                dialog.show();
                return true;
            }
        });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("文章发布");

        Request request = new Request.Builder().url(Properties.BASE_NEWS_URL).get().build();
        RuntimeObject.client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                UploadActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getBaseContext(), "加载失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                newsList = RuntimeObject.gson.fromJson(response.body().string(),
                        new TypeToken<List<News>>(){}.getType());
                adapter = new NewsAdapter(getBaseContext(), newsList);
                UploadActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        uploadList.setAdapter(adapter);
                    }
                });
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    class NewsAdapter extends BaseAdapter {

        private List<News> newsList;
        private Context context;

        public NewsAdapter(Context context, List<News> newsList){
            this.newsList = newsList;
            this.context = context;
        }

        @Override
        public int getCount() {
            return newsList.size();
        }

        @Override
        public Object getItem(int position) {
            return newsList.get(position);
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
                convertView = View.inflate(this.context, R.layout.news_item, null);
                holder.title = convertView.findViewById(R.id.news_title);
                holder.abstracts = convertView.findViewById(R.id.news_abstract);
                convertView.setTag(holder);
            }else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            News news = newsList.get(position);
            holder.title.setText(news.getTitle());
            holder.abstracts.setText(news.getAbstracts());
            holder.id = news.getId();

            return convertView;
        }

        class ViewHolder{
            public Integer id;
            public TextView title;
            public TextView abstracts;
        }
    }
}
