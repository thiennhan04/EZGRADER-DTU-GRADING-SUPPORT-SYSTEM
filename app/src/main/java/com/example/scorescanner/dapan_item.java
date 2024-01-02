package com.example.scorescanner;

public class dapan_item {
    private int num;
    int checked = -1;

    public char c= '#';
    public dapan_item(int numberQues, char c) {
        this.num = numberQues;this.c = c;
    }

    public int getNum()
    {
        return num;
    }
}
