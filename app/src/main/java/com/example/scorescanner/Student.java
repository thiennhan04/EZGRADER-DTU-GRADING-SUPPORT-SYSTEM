package com.example.scorescanner;

public class Student {
    private String sbd;
    private String imgpath;

    public Student(String sbd, String imgpath) {
        this.sbd = sbd;
        this.imgpath = imgpath;
    }

    public String getSbd() {
        return sbd;
    }

    public void setSbd(String sbd) {
        this.sbd = sbd;
    }

    public String getImgpath() {
        return imgpath;
    }

    public void setImgpath(String imgpath) {
        this.imgpath = imgpath;
    }
}
