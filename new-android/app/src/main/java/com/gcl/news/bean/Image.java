package com.gcl.news.bean;

/**
 * 图片bean
 * Created by 游 on 2018/1/11.
 */

public class Image {
    // id
    private Integer id;

    // 用户所属用户ID
    private Integer userId;

    // 图片名
    private String imgName;

    // 图片描述
    private String imgDesc;

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

    public String getImgName() {
        return imgName;
    }

    public void setImgName(String imgName) {
        this.imgName = imgName;
    }

    public String getImgDesc() {
        return imgDesc;
    }

    public void setImgDesc(String imgDesc) {
        this.imgDesc = imgDesc;
    }

    @Override
    public String toString() {
        return "Image{" +
                "id=" + id +
                ", userId=" + userId +
                ", imgName='" + imgName + '\'' +
                ", imgDesc='" + imgDesc + '\'' +
                '}';
    }
}
