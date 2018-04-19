package com.gcl.news.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gcl.news.R;
import com.hjm.bottomtabbar.BottomTabBar;

public class MainActivity extends AppCompatActivity {

    private BottomTabBar mBottomTabBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBottomTabBar = findViewById(R.id.bottom_tab_bar);
        mBottomTabBar.init(getSupportFragmentManager())
                .addTabItem("最新", R.mipmap.home, LatestFragment.class)
                .addTabItem("分类", R.mipmap.category, CategoryFragment.class)
                .addTabItem("我的", R.mipmap.account, MineFragment.class);
    }
}
