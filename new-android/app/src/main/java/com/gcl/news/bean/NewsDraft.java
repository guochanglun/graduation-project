package com.gcl.news.bean;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * 存到自己本地数据库的为发布的草稿
 * Created by 游 on 2018/1/11.
 */
@DatabaseTable(tableName = "tb_news_draft")
public class NewsDraft {

    @DatabaseField(generatedId = true)
    private Integer id;

    // news所属用户
    @DatabaseField(columnName = "user_id")
    private Integer userId;

    // news title
    @DatabaseField(columnName = "title")
    private String title;

    // news 摘要
    @DatabaseField(columnName = "abstract")
    private String abstracts;

    // news html内容
    @DatabaseField(columnName = "content")
    private String content;

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

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
