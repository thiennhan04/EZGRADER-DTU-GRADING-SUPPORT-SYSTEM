package com.example.scorescanner;

public class Student {
    private String sbd;
    private String imgpath;
    private String made;
    private String imgpathtl;

    public Student(String sbd, String imgpath) {
        this.sbd = sbd;
        this.imgpath = imgpath;
    }
    public Student(String sbd, String imgpath, String made, String imgpathtl){
        this.sbd = sbd;
        this.imgpath = imgpath;
        this.made = made;
        this.imgpathtl = imgpathtl;
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

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public String getImgpathtl() {
        return imgpathtl;
    }

    public void setImgpathtl(String imgpathtl) {
        this.imgpathtl = imgpathtl;
    }
}
