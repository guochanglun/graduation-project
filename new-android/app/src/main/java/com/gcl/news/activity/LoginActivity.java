package com.gcl.news.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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
import com.google.gson.Gson;

import org.jsoup.helper.StringUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private StateButton button;
    private TextView register;

    private static final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setTitle("用户登录");

        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        button = findViewById(R.id.login);
        register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String usernames = username.getText().toString();
                String passwords = password.getText().toString();

                if(StringUtil.isBlank(usernames) || StringUtil.isBlank(passwords)){
                    Toast.makeText(LoginActivity.this,
                            "请填写完整信息", Toast.LENGTH_SHORT).show();
                }else {
                    FormBody formBody = new FormBody.Builder()
                            .add("username", usernames)
                            .add("password", passwords)
                            .build();

                    Request request = new Request.Builder()
                            .url(Properties.BASE_USER_URL + "/login")
                            .post(formBody).build();
                    RuntimeObject.client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            LoginActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(LoginActivity.this,
                                            "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, final Response response) throws IOException {
                            User user = RuntimeObject.gson.fromJson(response.body().string(), User.class);
                            if(user != null && user.getId() != -1){
                                RuntimeObject.user = user;
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else{
                                LoginActivity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LoginActivity.this,
                                                "用户名或密码错误", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            }
        });
    }
}
