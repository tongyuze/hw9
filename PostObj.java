package com.example.yuzetong.myfb;

/**
 * Created by yuzetong on 4/26/17.
 */

public class PostObj
{
    String icon;
    String author;
    String time;
    String des;

    public PostObj(String icon, String author, String time, String des) {
        this.icon = icon;
        this.author = author;
        this.time = time;
        this.des = des;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }
}
