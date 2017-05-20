package com.darren.mydemo.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by lenovo on 2017/5/20.
 */

public class Collection extends BmobObject {
    private String uId = "";
    private Integer type = 0;
    private String title = "";
    private String picUrl = "";
    private String url = "";

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
