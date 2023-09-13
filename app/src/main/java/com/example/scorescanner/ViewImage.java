package com.example.scorescanner;

import org.opencv.android.Utils;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class ViewImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        ImageView view = findViewById(R.id.imageView);

        String path = getIntent().getStringExtra("path");
        Bitmap bmp = ImageHelper.loadImage(path);

        // ở đây gọi các hàm xử lý method rồi set bitmap cho view
        ImageHelper.ProcessImage process = new ImageHelper.ProcessImage();
        // test
        if(process.getMadeAndSBD(bmp))
        {
            Utils.matToBitmap(process.mat,bmp);
        }

        if (bmp != null && view != null) {
            view.setImageBitmap(bmp);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            }, 2000);
        }
    }
}