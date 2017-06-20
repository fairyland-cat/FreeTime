package com.wang.freetime.model;

import cn.bmob.v3.BmobObject;

/**
 * FreeTime
 * Created by wang on 2017.6.20.
 */

public class FeedBack extends BmobObject {
    private String feedback;
    private String email;

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
