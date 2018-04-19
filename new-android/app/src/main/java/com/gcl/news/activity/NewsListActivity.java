package com.gcl.news.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.bean.NewsDraft;
import com.gcl.news.db.DatabaseHelper;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;
import com.j256.ormlite.dao.Dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.sql.SQLException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class NewsListActivity extends AppCompatActivity {

    private Dao<NewsDraft, Integer> newsDraftsDao;
    private ListView newsList;
    private TextView emptyWarningText;
    private BottomSheetDialog bottomSheetDialog;
    private static final String TAG = "NewsListActivity";

    private List<NewsDraft> newsDraftList;
    private int curSelectNewsId;
    private int curSelectPosition;
    private NewsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_list);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("我的草稿");

        // 设置bottomSheetDialog
        initBootSheetDialog();

        // 从数据库获取保存的草稿
        newsDraftsDao = DatabaseHelper.getHelper(this).getNewsDraftsDao();
        newsList = findViewById(R.id.news_list);
        emptyWarningText = findViewById(R.id.empty_warning);

        try {
            newsDraftList = newsDraftsDao.queryForAll();
            if(newsDraftList.size() != 0){ // 如果有草稿则隐藏提示
                emptyWarningText.setVisibility(View.GONE);
                adapter = new NewsAdapter(this, newsDraftList);
                newsList.setAdapter(adapter);
                newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // 根据template.html生成index.html
                        AssetManager assetManager = getAssets();
                        try {
                            InputStream inputStream = assetManager.open("template.html");
                            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                            BufferedReader reader = new BufferedReader(inputStreamReader);
                            StringBuffer buffer = new StringBuffer();
                            String line;
                            line = reader.readLine();
                            while(line != null){
                                buffer.append(line);
                                line = reader.readLine();
                            }
                            reader.close();
                            String html = buffer.toString();
                            NewsDraft newsDraft = newsDraftList.get(position);
                            html = html.replace("[title]", newsDraft.getTitle())
                                        .replace("[content]", newsDraft.getContent());
                            Intent intent = new Intent(NewsListActivity.this, WebActivity.class);
                            intent.putExtra("title", "草稿");
                            intent.putExtra("data", html);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                newsList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                        curSelectNewsId = newsDraftList.get(position).getId();
                        curSelectPosition = position;
                        bottomSheetDialog.show();
                        return true;
                    }
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void initBootSheetDialog() {
        bottomSheetDialog = new BottomSheetDialog(this);
        Button publishButton = new Button(this);
        publishButton.setBackgroundColor(Color.WHITE);
        publishButton.setText("发布文章");
        publishButton.setTextColor(Color.BLUE);
        publishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 使用网络上传文章
                bottomSheetDialog.cancel();
                NewsDraft draft = newsDraftList.get(curSelectPosition);
                FormBody formBody = new FormBody.Builder()
                        .add("html", draft.getContent())
                        .add("title", draft.getTitle())
                        .add("abstracts", draft.getAbstracts())
                        .build();
                Request request = new Request.Builder().url(Properties.BASE_NEWS_URL).post(formBody).build();
                RuntimeObject.client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        NewsListActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getBaseContext(), "发布失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        if(response.code() == 200){
                            NewsListActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getBaseContext(), "发布成功", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                });
            }
        });

        Button deleteButton = new Button(this);
        deleteButton.setText("删除文章");
        deleteButton.setBackgroundColor(Color.WHITE);
        deleteButton.setTextColor(Color.RED);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    newsDraftsDao.deleteById(curSelectNewsId);
                    newsDraftList.remove(curSelectPosition);
                    adapter.notifyDataSetChanged();
                    bottomSheetDialog.cancel();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        Button cancelButton = new Button(this);
        cancelButton.setText("取消操作");
        cancelButton.setBackgroundColor(Color.parseColor("#eeeeee"));
        cancelButton.setTextColor(Color.parseColor("#666666"));
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomSheetDialog.cancel();
            }
        });

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(publishButton);
        layout.addView(deleteButton);
        layout.addView(cancelButton);
        bottomSheetDialog.setContentView(layout);
    }

    class NewsAdapter extends BaseAdapter {

        private List<NewsDraft> newsList;
        private Context context;

        public NewsAdapter(Context context, List<NewsDraft> newsList){
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
            NewsDraft news = newsList.get(position);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
