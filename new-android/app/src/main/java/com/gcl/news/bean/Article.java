package com.gcl.news.bean;


public class Article {

    private Integer id;

    private String title;
    private String content;
    // 来源
    private String detailSource;
    private String tag;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDetailSource() {
        return detailSource;
    }

    public void setDetailSource(String detailSource) {
        this.detailSource = detailSource;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
