package com.example.scorescanner;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

public class MadeOption extends AppCompatActivity {
    TextView txtmade;
    Button dapanbtn,chambaibtn,baidachambtn,xuatdiembtn,thongkebtn;
    ImageView backbtn;

    Uri imageUri;
    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb2.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_option);
        dapanbtn = findViewById(R.id.dapanbtn);
        chambaibtn = findViewById(R.id.chambaibtn);
        xuatdiembtn = findViewById(R.id.xuatdiembtn);
        thongkebtn = findViewById(R.id.thongkebtn);
        backbtn = findViewById(R.id.backmdoption);
        txtmade = findViewById(R.id.txtmade);
        Intent intent = getIntent();
        String makithi = intent.getStringExtra("kithi");
        String made = intent.getStringExtra("made");
        txtmade.setText("Mã đề "+made);
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent made = new Intent(MadeOption.this,MadeActivity.class);
//                made.putExtra("made", made);
//                made.putExtra("kithi", makithi);
//                startActivity(made);
            }
        });




        Toast.makeText(MadeOption.this, "Ki thi " + makithi + " made " + made, Toast.LENGTH_SHORT).show();
        chambaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processCopy();
                database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);

                int kithi = Integer.parseInt(makithi);
                Cursor c = database.rawQuery("select * from cauhoi where makithi = " + makithi
                        + " and made = '" + made + "'", null);
//                    Cursor c = database.query("cauhoi2",null,null,null,null,null,null, null);
                c.moveToFirst();
                String data ="";
                while (c.isAfterLast() == false)
                {
                    String listanswer = c.getString(2);
                    data+=made;
                    c.moveToNext();
                }
                if(data.equals("")){

                    Toast.makeText(MadeOption.this, "Vui lòng nhập đáp án!", Toast.LENGTH_SHORT).show();
                }else{

                    Toast.makeText(MadeOption.this, "chuyển sang chấm bài", Toast.LENGTH_SHORT).show();
                    Intent myintent = new Intent(ACTION_IMAGE_CAPTURE);
                    if (ActivityCompat.checkSelfPermission(MadeOption.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(MadeOption.this,new String[]{Manifest.permission.CAMERA}, 1);
                        return;
                    }startActivityForResult(myintent,99);
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


    //hàm load database từ asset
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
            if (!f.exists())
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 99){
            if(resultCode == Activity.RESULT_OK){
                Bitmap image = (Bitmap) data.getExtras().get("data");

                String imgname = String.valueOf(System.currentTimeMillis());//tên ảnh
                File directory = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
                File mypath = new File(directory.getAbsolutePath()+"/myimg.jpg");

                Toast.makeText(this, mypath+"", Toast.LENGTH_SHORT).show();
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    image.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }
}