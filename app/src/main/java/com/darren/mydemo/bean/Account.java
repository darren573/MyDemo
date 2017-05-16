package com.darren.mydemo.bean;

import cn.bmob.v3.BmobUser;

/**
 * Created by lenovo on 2017/5/16.
 */

public class Account extends BmobUser {
    private Boolean sex;
    private String photo;
    private Integer age;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getSex() {
        return sex;
    }

    public void setSex(Boolean sex) {
        this.sex = sex;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
