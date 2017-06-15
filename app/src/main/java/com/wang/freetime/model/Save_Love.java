package com.wang.freetime.model;

import cn.bmob.v3.BmobObject;

/**
 * FreeTime
 * Created by wang on 2017.6.15.
 */

public class Save_Love extends BmobObject {
    private String user;
    private String url;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
