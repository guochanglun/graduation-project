package com.gcl.news.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.gcl.news.R;
import com.gcl.news.utils.Properties;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("用户设置");

        Switch autoSync = findViewById(R.id.auto_sync);
        Switch autoSend = findViewById(R.id.auto_send);
        Switch recordHistory = findViewById(R.id.record_history);
        Switch reportError = findViewById(R.id.report_error);
        Button clearCache = findViewById(R.id.clear_cache);

        SharedPreferences preferences = getSharedPreferences(Properties.PREFERENCE_NAME, MODE_PRIVATE);
        final SharedPreferences.Editor editor = preferences.edit();

        autoSync.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoSync", isChecked);
                editor.apply();
            }
        });
        autoSend.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("autoSend", isChecked);
                editor.apply();
            }
        });
        recordHistory.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("recordHistory", isChecked);
                editor.apply();
            }
        });
        reportError.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("reportError", isChecked);
                editor.apply();
            }
        });
        clearCache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 因为访问Android存储比较麻烦，这里先使用Toast模拟清除缓存
                // TODO 添加清除缓存功能
                Toast.makeText(getBaseContext(), "清除缓存", Toast.LENGTH_SHORT).show();
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
