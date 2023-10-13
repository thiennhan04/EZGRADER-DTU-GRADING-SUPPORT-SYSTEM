package com.example.scorescanner;

class Question {
    private String made;
    private int makithi;
    private int kieucauhoi;
    private String dapan;
    private String dapan2;
    private String dapan3;
    private String dapan4;
    private String dapan5;
    private String tencauhoi;

    public  Question(){

    }
    public Question(String made, int makithi, int kieucauhoi, String dapan) {
        this.made = made;
        this.makithi = makithi;
        this.kieucauhoi = kieucauhoi;
        this.dapan = dapan;
    }

    public String getMade() {
        return made;
    }

    public void setMade(String made) {
        this.made = made;
    }

    public int getMakithi() {
        return makithi;
    }

    public void setMakithi(int makithi) {
        this.makithi = makithi;
    }

    public int getKieucauhoi() {
        return kieucauhoi;
    }

    public void setKieucauhoi(int kieucauhoi) {
        this.kieucauhoi = kieucauhoi;
    }

    public String getDapan() {
        return dapan;
    }

    public void setDapan(String dapan) {
        this.dapan = dapan;
    }

    public String getDapan2() {
        return dapan2;
    }

    public void setDapan2(String dapan2) {
        this.dapan2 = dapan2;
    }

    public String getDapan3() {
        return dapan3;
    }

    public void setDapan3(String dapan3) {
        this.dapan3 = dapan3;
    }

    public String getDapan4() {
        return dapan4;
    }

    public void setDapan4(String dapan4) {
        this.dapan4 = dapan4;
    }

    public String getDapan5() {
        return dapan5;
    }

    public void setDapan5(String dapan5) {
        this.dapan5 = dapan5;
    }

    public String getTencauhoi() {
        return tencauhoi;
    }

    public void setTencauhoi(String tencauhoi) {
        this.tencauhoi = tencauhoi;
    }
}
