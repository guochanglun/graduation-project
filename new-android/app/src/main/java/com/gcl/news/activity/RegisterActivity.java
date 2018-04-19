package com.gcl.news.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.bean.User;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;
import com.gcl.news.view.StateButton;

import org.jsoup.helper.StringUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText username;
    private EditText password;
    private StateButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("用户注册");

        name = findViewById(R.id.name);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button = findViewById(R.id.register);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String names = name.getText().toString();
                String usernames = username.getText().toString();
                String passwords = password.getText().toString();

                if(StringUtil.isBlank(names)
                        || StringUtil.isBlank(usernames)
                        || StringUtil.isBlank(passwords)){
                    Toast.makeText(RegisterActivity.this,
                            "请填写完整信息", Toast.LENGTH_SHORT).show();
                }else {
                    FormBody formBody = new FormBody.Builder()
                            .add("name", names)
                            .add("username", usernames)
                            .add("password", passwords)
                            .build();

                    Request request = new Request.Builder()
                            .url(Properties.BASE_USER_URL + "/register")
                            .post(formBody).build();
                    RuntimeObject.client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "注册失败", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, "注册成功，请登录", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            });
                        }
                    });
                }
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
}
