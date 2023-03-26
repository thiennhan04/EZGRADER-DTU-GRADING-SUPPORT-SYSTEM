package com.example.scorescanner;

public class dapan_item {
    private int num;
    private String dapan;

    public dapan_item(int numberQues, String dapan) {
        this.num = numberQues;
        this.dapan = dapan;
    }

    public int getNum()
    {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getDapan()
    {
        return dapan;
    }
    public void setDapan(String dapan){
        this.dapan = dapan;
    }
}
