package com.example.shivendra.coeus.Model;

public class Blog {

    public String title;
    public String desc;
    public String image;
    public String userid;
    public String timestamp;

    public Blog() {
    }

    public Blog(String title, String desc, String image, String userid, String timestamp) {
        this.title = title;
        this.desc = desc;
        this.image = image;
        this.userid = userid;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
