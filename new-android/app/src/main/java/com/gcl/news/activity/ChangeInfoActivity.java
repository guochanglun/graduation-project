package com.gcl.news.activity;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class ChangeInfoActivity extends AppCompatActivity {

    private EditText username;
    private EditText pwdOld;
    private EditText pwdNew;
    StateButton change;
    private User user = RuntimeObject.user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("修改信息");

        username = findViewById(R.id.username);
        pwdOld = findViewById(R.id.password_old);
        pwdNew = findViewById(R.id.password_new);
        change = findViewById(R.id.btn_change_info);

        username.setText(user.getUsername());

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StringUtil.isBlank(username.getText().toString())
                        || StringUtil.isBlank(pwdOld.getText().toString())
                        || StringUtil.isBlank(pwdNew.getText().toString())) {
                    Toast.makeText(ChangeInfoActivity.this, "请填写完整信息", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!user.getPassword().equals(pwdOld.getText().toString())) {
                    Toast.makeText(ChangeInfoActivity.this, "旧密码不正确", Toast.LENGTH_SHORT).show();
                    return;
                }

                if(pwdNew.getText().toString().equals(pwdOld.getText().toString())) {
                    Toast.makeText(ChangeInfoActivity.this, "新旧密码不能相同", Toast.LENGTH_SHORT).show();
                    return;
                }

                change.setEnabled(false);

                FormBody formBody = new FormBody.Builder()
                        .add("id", user.getId() + "")
                        .add("name", user.getName())
                        .add("username", username.getText().toString())
                        .add("password", pwdNew.getText().toString()).build();
                Request request = new Request.Builder().url(Properties.BASE_USER_URL+"/update")
                        .post(formBody).build();
                RuntimeObject.client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        ChangeInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeInfoActivity.this, "修改信息失败", Toast.LENGTH_SHORT).show();
                                change.setEnabled(true);
                            }
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        ChangeInfoActivity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ChangeInfoActivity.this, "修改成功，请重新登录", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ChangeInfoActivity.this, LoginActivity.class));
                                finish();
                            }
                        });
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
}
