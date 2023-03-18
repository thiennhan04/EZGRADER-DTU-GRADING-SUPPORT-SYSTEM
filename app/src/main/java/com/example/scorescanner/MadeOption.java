package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MadeOption extends AppCompatActivity {
    TextView txtmade;
    Button dapanbtn,chambaibtn,baidachambtn,xuatdiembtn,thongkebtn;

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_option);
        dapanbtn = findViewById(R.id.dapanbtn);
        chambaibtn = findViewById(R.id.chambaibtn);
        xuatdiembtn = findViewById(R.id.xuatdiembtn);
        thongkebtn = findViewById(R.id.thongkebtn);
        txtmade = findViewById(R.id.txtmade);
        Intent intent = getIntent();
        String makithi = intent.getStringExtra("kithi");
        String made = intent.getStringExtra("made");
        txtmade.setText("Mã đề "+made);


        chambaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCopy();
                database = openOrCreateDatabase("ssdb.db", MODE_PRIVATE, null);
                Toast.makeText(MadeOption.this, "Ki thi " + makithi + " made " + made, Toast.LENGTH_SHORT).show();
                int kithi = Integer.parseInt(makithi);
                Cursor c = database.rawQuery("select * from cauhoi", null);
//                    Cursor c = database.query("cauhoi2",null,null,null,null,null,null, null);
                c.moveToFirst();

                String data ="";
                while (c.isAfterLast() == false)
                {
                    c.moveToNext();
                }
                if(c != null){
                    Toast.makeText(MadeOption.this, "chuyển sang chấm bài", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(MadeOption.this, "Vui lòng nhập đáp án!", Toast.LENGTH_SHORT).show();
                }
                c.close();
//                try{
//
//                }catch (Exception e){
//                    Toast.makeText(MadeOption.this, "Bị lỗi", Toast.LENGTH_SHORT).show();
//                }

//                Toast.makeText(MadeOption.this, "cham bai", Toast.LENGTH_SHORT).show();

            }
        });

    }
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{CopyDataBaseFromAsset();
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