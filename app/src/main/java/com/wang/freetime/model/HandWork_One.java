package com.wang.freetime.model;

/**
 * FreeTime
 * Created by wang on 2017.6.8.
 */

public class HandWork_One {
    private String img;
    private String link;
    private String title;
    private String readcount;
    private String lovecount;

    public HandWork_One(String img, String link, String title, String readcount, String lovecount) {
        this.img = img;
        this.link = link;
        this.title = title;
        this.readcount = readcount;
        this.lovecount = lovecount;
    }

    public String getImg() {
        return img;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public String getReadcount() {
        return readcount;
    }

    public String getLovecount() {
        return lovecount;
    }
}
