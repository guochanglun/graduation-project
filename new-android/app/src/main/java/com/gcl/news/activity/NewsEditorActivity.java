package com.gcl.news.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.richeditor.ContentType;
import com.gcl.news.richeditor.InputDialog;
import com.gcl.news.richeditor.RichEditor;
import com.gcl.news.utils.Properties;

public class NewsEditorActivity extends AppCompatActivity {

    private RichEditor richEditor;

    private static final String TAG = "NewsEditorActivity";

    private InputDialog dialog;

    private String articleTitle;
    private String articleAbstract;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_editor);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("文章编辑");

        initDialog();
        richEditor = findViewById(R.id.rich_editor);
    }

    private void initDialog() {
        dialog = new InputDialog(this);
        dialog.setPositiveButton("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (dialog.getType()) {
                    case ARTICLE_TITLE:
                        insertContent(ContentType.ARTICLE_TITLE);
                        break;
                    case ARTICLE_ABSTRACT:
                        insertContent(ContentType.ARTICLE_ABSTRACT);
                        break;
                }
                dialog.dismiss();
                dialog.clearText();
            }
        });
        dialog.setNegativeButton("取消", new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                dialog.clearText();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == Properties.SELECT_IMAGE_CODE && resultCode == Properties.RESULT_CODE_SUCCESS){
            String name = data.getStringExtra("imgName");
            richEditor.insertImage(name);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_action_bar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.news_title: // 填写文章标题
                dialog.show(ContentType.ARTICLE_TITLE, this.articleTitle);
                return true;
            case R.id.news_abstract: // 填写文章摘要
                dialog.show(ContentType.ARTICLE_ABSTRACT, this.articleAbstract);
                return true;
            case R.id.news_help: // 打开帮助页面
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra("title", "帮助文档");
                intent.putExtra("url", Properties.BASE_OTHER_URL+"/help");
                startActivity(intent);
                return true;
            case android.R.id.home: // 返回上一页
                // 如果草稿未保存，则提示保存草稿
                if(richEditor.getModifyState()){
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("草稿未保存")
                            .setMessage("放弃保存草稿吗？")
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }else {
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
            AlertDialog dialog = new AlertDialog.Builder(this)
                    .setTitle("草稿未保存")
                    .setMessage("放弃保存草稿吗？")
                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create();
            dialog.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void insertContent(ContentType type){
        String content = dialog.getContent();
        if(type == ContentType.ARTICLE_TITLE){
            richEditor.setArticleTitle(content);
            this.articleTitle = content;
        }else {
            richEditor.setArticleAbstract(content);
            this.articleAbstract = content;
        }
    }
}
