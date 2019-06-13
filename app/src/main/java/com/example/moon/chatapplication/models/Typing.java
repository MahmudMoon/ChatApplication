package com.example.moon.chatapplication.models;

public class Typing {
    private String photourl;
    private String typing;
    private String uid;

    public Typing() {

    }

    public Typing(String photourl, String typing, String uid) {
        this.photourl = photourl;
        this.typing = typing;
        this.uid = uid;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public String getTyping() {
        return typing;
    }

    public void setTyping(String typing) {
        this.typing = typing;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
