package com.example.scorescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class danhsachkithi extends AppCompatActivity {
    ListView lvdanhsachkt;
    DBHelper dbHelper;
    ArrayList<Exam> mylist;
    MyArrayAdapter myArrayAdapter;
    public static DataBase db;
    String username = "";
    ImageButton backbtn, addbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachkithi);
        lvdanhsachkt = findViewById(R.id.lvdanhsachkt);
        addbtn = findViewById(R.id.addbtn);
        backbtn = findViewById(R.id.back_btnds);
        db = new DataBase(this);
        
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        loadDataListKithi();
//        processCopy();
//
//        db = openOrCreatedb("ssdb2.db", MODE_PRIVATE, null);
////        String sql = "select * from kithi where username = '" + username + "'";
//        mylist = new ArrayList<>();//tạo mới mảng rỗng
//        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);
//
//        Cursor c = db.rawQuery("select * from kithi where username = '" + username + "'", null);
//        c.moveToFirst();
//        mylist.clear();
//        String data ="";
//        while (c.isAfterLast() == false)
//        {
//            int madethi = Integer.parseInt(c.getString(0));
//            String tendethi = c.getString(1);
//            int kieukithi = Integer.parseInt(c.getString(6));
//            Exam exam = new Exam(madethi, tendethi, username,kieukithi);
//            mylist.add(exam);
//            c.moveToNext();
//        }
//        lvdanhsachkt.setAdapter(myArrayAdapter);
//        myArrayAdapter.notifyDataSetChanged();
//        addkithi();
//        lvdanhsachkt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
////                Toast.makeText(danhsachkithi.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        });

        lvdanhsachkt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Intent made = new Intent(danhsachkithi.this, MadeOption.class);
//                Toast.makeText(danhsachkithi.this, "click", Toast.LENGTH_SHORT).show();
//                String makithi = mylist.get(i).getMakithi() + "";
                int makithi = mylist.get(i).getMakithi();
//                made.putExtra("makithi", makithi);
//                made.putExtra("username", username);

                if (mylist.get(i).getKieukithi() == 1){
                    Intent madeoption1 = new Intent(danhsachkithi.this, MadeOption.class);
                    madeoption1.putExtra("makithi", makithi);
                    madeoption1.putExtra("username", username);
                    startActivity(madeoption1);
                } else{
                    Intent madeoption2= new Intent(danhsachkithi.this, madeoption2.class);
                    madeoption2.putExtra("makithi", makithi);
                    madeoption2.putExtra("username", username);
                    startActivity(madeoption2);
                }
//                Toast.makeText(danhsachkithi.this, "makithi " +  mylist.get(i).getMakithi(), Toast.LENGTH_SHORT).show();

            }
        });
//        c.close();/

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                danhsachkithi.this.finish();
            }
        });
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent add = new Intent(danhsachkithi.this, DanhSachKithi_Add.class);
                add.putExtra("username", username);
                startActivityForResult(add,105);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadDataListKithi();
    }
    private void loadDataListKithi(){
        lvdanhsachkt = findViewById(R.id.lvdanhsachkt);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");

        mylist = new ArrayList<>();//tạo mới mảng rỗng
        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);
//        Cursor c = db.rawQuery("select * from kithi where username = '" + username + "'", null);
        Cursor c = db.mydatabase.rawQuery("select makithi, tenkithi, kieukithi from kithi where username = '" + username + "'", null);
        c.moveToFirst();
        mylist.clear();
        String data ="";
        while (c.isAfterLast() == false)
        {
            int madethi = Integer.parseInt(c.getString(0));
            String tendethi = c.getString(1);
            int kieukithi = Integer.parseInt(c.getString(2));
            Exam exam = new Exam(madethi, tendethi, username,kieukithi);
            mylist.add(exam);
            c.moveToNext();
        }
        lvdanhsachkt.setAdapter(myArrayAdapter);
        myArrayAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 105 && resultCode == 33) {
            Intent result = getIntent();
            String username2 = result.getStringExtra("username");
//            Toast.makeText(this, "vao ham result", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "User name " + username, Toast.LENGTH_SHORT).show();
            String tenkithi = data.getStringExtra("tenkithi");
            int socau = data.getIntExtra("socau",50);
            double hediem = data.getDoubleExtra("hediem",10);
            String loaiphieu = data.getStringExtra("loaiphieu");
            int kieukithi = data.getIntExtra("kieukithi",1);
//            Toast.makeText(this, "" + tenkithi + " " + socau, Toast.LENGTH_SHORT).show();

            //them ki thi moi vao db.mydatabase
            ContentValues valuekithi = new ContentValues();
            valuekithi.put("tenkithi",tenkithi );
            valuekithi.put("username",username2);
            valuekithi.put("socau",socau);
            valuekithi.put("hediem",hediem);
            valuekithi.put("loaiphieu",loaiphieu);
            valuekithi.put("kieukithi",kieukithi);
            String msg  = "";
            if(db.mydatabase.insert("kithi",null,valuekithi)==-1){
                msg = "Fail to insert kithi";
            }else{
                msg = "Insert kithi thanh cong ";
                loadkithi(username2);
            }
        }
    }
    private void loadkithi(String username2) {
        mylist.clear();
//        Cursor c = db.mydatabase.rawQuery("select * from kithi where username = '" + username2 + "'", null);
        Cursor c = db.mydatabase.rawQuery("select makithi, tenkithi, kieukithi from kithi where username = '" + username2 + "'", null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            int madethi = Integer.parseInt(c.getString(0));
            String tendethi = c.getString(1);
            int kieukithi = Integer.parseInt(c.getString(2));
            Exam exam = new Exam(madethi, tendethi, username,kieukithi);
            mylist.add(exam);
            c.moveToNext();
        }
        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);
        lvdanhsachkt.setAdapter(myArrayAdapter);

        c.close();
    }
}