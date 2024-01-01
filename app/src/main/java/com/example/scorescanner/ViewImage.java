package com.example.scorescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class ViewImage extends AppCompatActivity {
    private Methods methods;
    private static  DataBase db;
    private int statusCode = 0;
    String sbd = "###", made = "###", diem = "###";
    int socaudung, tongsocau;
    TextView txtmade, txtdiem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        txtmade = findViewById(R.id.txtMade2);
        txtdiem = findViewById(R.id.txtDiem);

        db = new DataBase(ViewImage.this);
        String username = getIntent().getStringExtra("username");
        String makithi = getIntent().getStringExtra("makithi");
        methods = new Methods(this, makithi, username);


        ImageView view = findViewById(R.id.imageView);
        Bitmap bitmap = CameraRealTime.getRotatedBitmap();
        GetShortAnswer getShortAnswer = new GetShortAnswer(ViewImage.this);
//        Bitmap bitmap =
        if (bitmap != null && view != null) {
            Bitmap result = methods.run(bitmap);
            diem = methods.getScore();
            if(diem.equals("Không nhận diện được mã đề!") || diem.equals("Mã đề không tồn tại!")) {
                diem = "###";
            }
            sbd = methods.sbd;
            made = methods.made;
            socaudung = methods.socaudung;
            tongsocau = methods.tongsocau;

            txtmade.setText(made);
            txtdiem.setText(socaudung +" / "+ tongsocau +" = "+ diem);
            String score = methods.getScore();
            Log.i("TAG", "onCreate: "+score);
            if(score.equals("Không nhận diện được mã đề!") || score.equals("Mã đề không tồn tại!")){
                Toast.makeText(this, score, Toast.LENGTH_SHORT).show();
                statusCode = 0;
//                setResult(statusCode);
                Intent resultIntent = getIntent();
                resultIntent.putExtra("made", made);
                resultIntent.putExtra("sbd", sbd);
                setResult(statusCode, resultIntent);
                view.setImageBitmap(result);
            }
//            do?n này s? du?c bê di qua class xem k?t qu? ch?m t? lu?n
//            Bitmap result = getShortAnswer.getShortAnwer(db, bitmap, makithi, username,"001");
            else if(result != null) {
//                result = getShortAnswer.getShortAnwer(db, bitmap, makithi, username,made);
                statusCode = 1;
                setResult(statusCode);
                Intent resultIntent = getIntent();
                resultIntent.putExtra("made", made);
                resultIntent.putExtra("sbd", sbd);
                setResult(statusCode, resultIntent);
                view.setImageBitmap(result);
            }

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }, 2000);
        }
    }
}