package com.gcl.news.utils;

/**
 * 记录一些常量，方便修改
 * Created by 游 on 2018/1/11.
 */

public class Properties {

    // 连接夜神模拟器 nox_adb.exe connect 127.0.0.1:62001

    // genymotion的localhost地址是：10.0.3.2
    // google模拟器的localhost地址是10.0.2.2
    // 夜神模拟器的localhost的地址是10.0.2.2
    private static String BASE_URL = "http://192.168.43.151:8080";
    public static String BASE_IMG_URL = BASE_URL + "/img";
    public static String BASE_NEWS_URL = BASE_URL + "/news";
    public static String BASE_USER_URL = BASE_URL + "/user";
    public static String BASE_OTHER_URL = BASE_URL + "/other";
    public static String BASE_FEEDBACK_URL = BASE_URL + "/feedback";

    public static int RESULT_CODE_SUCCESS = 0;
    public static int RESULT_CODE_ERROR = -1;
    public static int SELECT_IMAGE_CODE = 0;

    public static String PREFERENCE_NAME = "setting data";
}
