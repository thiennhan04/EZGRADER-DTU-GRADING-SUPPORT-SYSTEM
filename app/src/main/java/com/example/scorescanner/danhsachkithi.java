package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
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
    ArrayList<Exam> mylist;
    MyArrayAdapter myArrayAdapter;

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb2.db";
    String username = "";
    ImageButton backbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danhsachkithi);
        lvdanhsachkt = findViewById(R.id.lvdanhsachkt);
        backbtn = findViewById(R.id.back_btn);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        processCopy();
        mylist = new ArrayList<>();//tạo mới mảng rỗng


        myArrayAdapter = new MyArrayAdapter(this,R.layout.kithi_item,mylist);
        database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);
        String sql = "select * from kithi where username = '" + username + "'";
        Cursor c = database.rawQuery("select * from kithi where username = '" + username + "'", null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            int madethi = Integer.parseInt(c.getString(0));
            String tendethi = c.getString(1);
            Exam exam = new Exam(madethi, tendethi, username);
            mylist.add(exam);
            c.moveToNext();
        }
        lvdanhsachkt.setAdapter(myArrayAdapter);
//        lvdanhsachkt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                Toast.makeText(danhsachkithi.this, "click", Toast.LENGTH_SHORT).show();
//            }
//        });

        lvdanhsachkt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent made = new Intent(danhsachkithi.this, MadeActivity.class);
                String makithi = mylist.get(i).getMakithi() + "";
                made.putExtra("makithi", makithi);
                Toast.makeText(danhsachkithi.this, "makithi " +  mylist.get(i).getMakithi(), Toast.LENGTH_SHORT).show();
                startActivity(made);
            }
        });
        c.close();
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Home = new Intent(danhsachkithi.this,HomeActivity.class);
                startActivity(Home);
            }
        });
    }
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {

        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
//            if (!f.exists())
                f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}