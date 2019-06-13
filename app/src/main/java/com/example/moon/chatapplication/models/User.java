package com.example.moon.chatapplication.models;

public class User {
    private String name;
    private String photourl;
    private int islive;

    public User() {
    }

    public User(String name, String photourl, int islive) {
        this.name = name;
        this.photourl = photourl;
        this.islive = islive;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotourl() {
        return photourl;
    }

    public void setPhotourl(String photourl) {
        this.photourl = photourl;
    }

    public int getIslive() {
        return islive;
    }

    public void setIslive(int islive) {
        this.islive = islive;
    }
}
