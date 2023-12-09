package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Base64;

public class Baidacham extends AppCompatActivity {
    GridView grvimg;
    public static DataBase db = null;
    public String makithi = "";
    public String username = "";
        ArrayList<Student> mylist;
    public static ImageAdapter  myimgadater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidacham);
        grvimg = findViewById(R.id.grvimg);
        db = new DataBase(this);
        ArrayList<String> testimg = new ArrayList<>();
        Intent intent = getIntent();
        boolean isTl = false;
        mylist = new ArrayList<>();//tạo mới mảng
        String makithi = intent.getStringExtra("makithi");
        myimgadater = new ImageAdapter(this,R.layout.imageitem,mylist, isTl, makithi);
//        Cursor ckithi = db.mydatabase.rawQuery("select * from kithi where makithi = " + makithi, null);
//        ckithi.moveToFirst();
//        while(ckithi.isAfterLast() == false){
//            if(ckithi.getString(6) == "1") {
//                isTl = true;
//                break;
//            }
//        }
        this.makithi = makithi;
        this.username = username;
        String username =  intent.getStringExtra("username");
        db = new DataBase(this);
        Cursor c = db.mydatabase.rawQuery("select * from diem where makithi = " + makithi + " and hinhanh is not null and loaicauhoi = 1", null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            String imguri = c.getString(2);
//            Toast.makeText(Baidacham.this, "" + imguri, Toast.LENGTH_SHORT).show();
            String masv = c.getString(3);
            Student std = new Student(masv,imguri);
            mylist.add(std);
            c.moveToNext();
        }
        grvimg.setAdapter(myimgadater);
        myimgadater.notifyDataSetChanged();
        c.close();
    }

}