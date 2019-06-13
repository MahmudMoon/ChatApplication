package com.example.moon.chatapplication.models;

public class Message  {
    private String user_name;
    private String user_msg;
    private String user_photo_url;
    private String time;
    private int seen;
    private String uid;

    public Message() {
    }

    public Message(String user_name, String user_msg, String user_photo_url, String time, int seen, String uid) {
        this.user_name = user_name;
        this.user_msg = user_msg;
        this.user_photo_url = user_photo_url;
        this.time = time;
        this.seen = seen;
        this.uid = uid;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_msg() {
        return user_msg;
    }

    public void setUser_msg(String user_msg) {
        this.user_msg = user_msg;
    }

    public String getUser_photo_url() {
        return user_photo_url;
    }

    public void setUser_photo_url(String user_photo_url) {
        this.user_photo_url = user_photo_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSeen() {
        return seen;
    }

    public void setSeen(int seen) {
        this.seen = seen;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
