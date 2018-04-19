package com.gcl.news.bean;

/**
 * 新闻列表项
 */
public class News {

    private Integer id;

    // news所属用户
    private Integer userId;

    // new title
    private String title;

    // 用户名
    private String mediaName;

    // 单个图片链接
    private String imageUrl;

    // 有多个图片时图片列表, 多个图片链接使用";"隔开
    private String imageList;

    // 文章id
    private Integer articleId;

    // 标签
    private String tag;

    // 摘要
    private String abstracts;

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
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMediaName() {
        return mediaName;
    }

    public void setMediaName(String mediaName) {
        this.mediaName = mediaName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageList() {
        return imageList;
    }

    public void setImageList(String imageList) {
        this.imageList = imageList;
    }

    public Integer getArticleId() {
        return articleId;
    }

    public void setArticleId(Integer articleId) {
        this.articleId = articleId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", userId=" + userId +
                ", title='" + title + '\'' +
                ", mediaName='" + mediaName + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", imageList='" + imageList + '\'' +
                ", articleId=" + articleId +
                ", tag='" + tag + '\'' +
                ", abstracts='" + abstracts + '\'' +
                '}';
    }
}
