package com.gcl.news.utils;

import com.gcl.news.bean.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;

/**
 * 保存运行时对象
 * Created by 游 on 2018/1/11.
 */

public class RuntimeObject {

    // client
    public static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .cookieJar(new CookieJar() {

                private List<Cookie> cookies = new ArrayList<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    this.cookies = cookies;
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    return this.cookies;
                }
            })
            .build();

    // client
    public static Gson gson = new Gson();

    // 当前登陆用户
    public static User user;
    static {
        user = new User();
        user.setId(1);
        user.setName("小王");
        user.setUsername("123");
        user.setPassword("123");
    }
}
