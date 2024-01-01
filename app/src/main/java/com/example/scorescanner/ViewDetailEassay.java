package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;

public class ViewDetailEassay extends AppCompatActivity {
    ImageView imgViewtl;
    public static DataBase db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_detail_eassay);
        imgViewtl = findViewById(R.id.imgViewtl);
        String imgpath = getIntent().getStringExtra("tlimg");
//        Uri imgUri=Uri.parse(imgpath);
        String imagePath = "/storage/emulated/0/DCIM/EzGrader/tnhan/1/screeen2.png";
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imgViewtl.setImageBitmap(bitmap);
    }
}