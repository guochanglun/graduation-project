package com.gcl.news.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.gcl.news.R;
import com.gcl.news.utils.Properties;
import com.tencent.smtt.sdk.WebChromeClient;
import com.tencent.smtt.sdk.WebSettings;
import com.tencent.smtt.sdk.WebView;

import static com.tencent.smtt.sdk.TbsReaderView.TAG;

/**
 * 最新文章，推荐文章
 * A simple {@link Fragment} subclass.
 */
public class LatestFragment extends Fragment {

    public LatestFragment() {
    }

    private WebView webView;
    private SwipeRefreshLayout refreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        webView = view.findViewById(R.id.webview);
        refreshLayout = view.findViewById(R.id.refresh);

        WebSettings settings = webView.getSettings();
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setLoadsImagesAutomatically(true);
        settings.setJavaScriptEnabled(true);

        final String url = Properties.BASE_NEWS_URL + "/index";
        webView.loadUrl(url);

        // 刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                webView.reload();
                refreshLayout.setRefreshing(false);
            }
        });

        webView.setWebViewClient(new com.tencent.smtt.sdk.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String s) {
                Log.i(TAG, "shouldOverrideUrlLoading: " + s);
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", s);
                intent.putExtra("title", "新闻");
                startActivity(intent);
                return true;
            }
        });

        webView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(webView.getWebScrollY() == 0){
                    refreshLayout.setEnabled(true);
                }else {
                    refreshLayout.setEnabled(false);
                }
                return false;
            }
        });

        return view;
    }

}
