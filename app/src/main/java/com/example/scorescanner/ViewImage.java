package com.example.scorescanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ViewImage extends AppCompatActivity {
    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        String username = getIntent().getStringExtra("username");
        String makithi = getIntent().getStringExtra("makithi");
        methods = new Methods(this, makithi, username);
        ImageView view = findViewById(R.id.imageView);
        Bitmap bitmap = CameraRealTime.getRotatedBitmap();
        if (bitmap != null && view != null) {
            Bitmap result = methods.run(bitmap);
            view.setImageBitmap(result != null ? result : bitmap);
//            Mat mat = new Mat();
//            Bitmap tmp = methods.listImgAnswer.get(5);
//            Utils.bitmapToMat(tmp,mat);
//            Mat imgGray = new Mat();
//            Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_BGR2GRAY);
//            Imgproc.equalizeHist(imgGray,imgGray);
//            Mat thresh = new Mat();
//            Imgproc.adaptiveThreshold(imgGray, thresh, 255,
//                    Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 201, 31);
//            Core.bitwise_not(thresh, thresh);
//            Utils.matToBitmap(thresh,tmp);
//            view.setImageBitmap(tmp);
//            view.setImageBitmap(methods.imgLeftAnswer);
//            view.setImageBitmap(methods.listImgAnswer.get(4));
//            view.setImageBitmap(methods.imgMade);
//            view.setImageBitmap(methods.tmp1);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }, 2000);
        }
    }
}