package com.example.scorescanner;

public class Exam {
    private int makithi;
    private String tenkithi;
    private String username;
    private int kieukithi;

    public Exam(int makithi, String tenkithi, String username, int kieukithi) {
        this.makithi = makithi;
        this.tenkithi = tenkithi;
        this.username = username;
        this.kieukithi = kieukithi;
    }

    public int getMakithi() {
        return makithi;
    }

    public void setMakithi(int makithi) {
        this.makithi = makithi;
    }

    public String getTenkithi() {
        return tenkithi;
    }

    public void setTenkithi(String tenkithi) {
        this.tenkithi = tenkithi;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setKieukithi(int kieukithi){this.kieukithi = kieukithi;}

    public int getKieukithi(){return kieukithi;}
}
