package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
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
        Intent intent = getIntent();
        String makithi = intent.getStringExtra("makithi");
        db = new DataBase(this);
        boolean isTl = false;
        Cursor isTuluan = db.mydatabase.rawQuery("Select kieukithi from kithi where makithi="+makithi,null);
        isTuluan.moveToFirst();
        if(isTuluan.getInt(0)==2) {
            isTl = true;
        }
        mylist = new ArrayList<>();
        myimgadater = new ImageAdapter(this,R.layout.imageitem,mylist, isTl, makithi);
//        Cursor ckithi = db.mydatabase.rawQuery("select * from kithi where makithi = " + makithi, null);
//        ckithi.moveToFirst();
//        while(ckithi.isAfterLast() == false){
//            if(ckithi.getString(6) == "1") {
//                isTl = true;
//                break;
//            }
//        }
        try {
            String username =  intent.getStringExtra("username");
            this.makithi = makithi;
            this.username = username;

            Cursor c = db.mydatabase.rawQuery("select hinhanh,masv from diem where makithi = " + makithi + " and hinhanh is not null and loaicauhoi = 1", null);

            c.moveToFirst();
            String data ="";
            while (c.isAfterLast() == false)
            {
                String imguri = c.getString(0);
//            Toast.makeText(Baidacham.this, "" + imguri, Toast.LENGTH_SHORT).show();
                String masv = c.getString(1);
                Student std = new Student(masv,imguri);
                mylist.add(std);
                c.moveToNext();
            }
            grvimg.setAdapter(myimgadater);
            myimgadater.notifyDataSetChanged();
            c.close();
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }

        ImageButton backbtn = findViewById(R.id.back_btnds);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}