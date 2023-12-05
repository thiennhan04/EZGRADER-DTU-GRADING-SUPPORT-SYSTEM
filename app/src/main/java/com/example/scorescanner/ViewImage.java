package com.example.scorescanner;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewImage extends AppCompatActivity {
    private Methods methods;
    private static  DataBase db;
    private int statusCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        db = new DataBase(ViewImage.this);
        String username = getIntent().getStringExtra("username");
        String makithi = getIntent().getStringExtra("makithi");
        methods = new Methods(this, makithi, username);
        String sbd = methods.sbd;
        String made = methods.made;
        ImageView view = findViewById(R.id.imageView);
        Bitmap bitmap = CameraRealTime.getRotatedBitmap();
        GetShortAnswer getShortAnswer = new GetShortAnswer(ViewImage.this);
//        Bitmap bitmap =
        if (bitmap != null && view != null) {
            Bitmap result = methods.run(bitmap);
            String score = methods.getScore();
            if(score == "Không nhận diện được mã đề!" || score == "Mã đề không tồn tại!"){
                Toast.makeText(this, score, Toast.LENGTH_SHORT).show();
                statusCode = 0;
//                setResult(statusCode);
                Intent resultIntent = getIntent();
                resultIntent.putExtra("made", made);
                resultIntent.putExtra("sbd", sbd);
                setResult(statusCode, resultIntent);
                view.setImageBitmap(result);
            }
//            đoạn này sẽ được bê đi qua class xem kết quả chấm tự luận
//            Bitmap result = getShortAnswer.getShortAnwer(db, bitmap, makithi, username,"001");
            else if(result != null) {
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