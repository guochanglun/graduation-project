package com.gcl.news.bean;

/**
 * 已经发布的新闻
 * Created by 游 on 2018/1/11.
 */

public class NewsRelease {

    private Integer id;

    // news所属用户
    private Integer userId;

    // new title
    private String title;

    // news html内容
    private String html;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
