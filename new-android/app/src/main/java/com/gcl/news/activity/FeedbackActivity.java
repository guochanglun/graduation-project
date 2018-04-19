package com.gcl.news.activity;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.utils.Properties;
import com.gcl.news.utils.RuntimeObject;

import org.jsoup.helper.StringUtil;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.Response;

public class FeedbackActivity extends AppCompatActivity {

    private EditText editText;
    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("用户反馈");

        editText = findViewById(R.id.feedback);
        button = findViewById(R.id.report);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = editText.getText().toString();
                if(!StringUtil.isBlank(msg)){
                    FormBody formBody = new FormBody.Builder()
                            .add("message", msg).build();
                    Request request = new Request.Builder().url(Properties.BASE_FEEDBACK_URL)
                            .post(formBody).build();
                    RuntimeObject.client.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            FeedbackActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FeedbackActivity.this, "发送失败", Toast.LENGTH_SHORT).show();
                                    button.setEnabled(true);
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            FeedbackActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(FeedbackActivity.this, "发送成功", Toast.LENGTH_SHORT).show();
                                    button.setEnabled(true);
                                }
                            });
                        }
                    });
                    Toast.makeText(getBaseContext(), "发送成功", Toast.LENGTH_SHORT).show();
                    button.setEnabled(false);
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
