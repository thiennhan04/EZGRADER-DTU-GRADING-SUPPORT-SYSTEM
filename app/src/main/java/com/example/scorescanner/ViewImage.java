package com.example.scorescanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewImage extends AppCompatActivity {
    private Methods methods;
    private static  DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);

        db = new DataBase(ViewImage.this);
        String username = getIntent().getStringExtra("username");
        String makithi = getIntent().getStringExtra("makithi");
        methods = new Methods(this, makithi, username);
        ImageView view = findViewById(R.id.imageView);
        Bitmap bitmap = CameraRealTime.getRotatedBitmap();
        GetShortAnswer getShortAnswer = new GetShortAnswer(ViewImage.this);
//        Bitmap bitmap =
        if (bitmap != null && view != null) {
//            Bitmap result = methods.run(bitmap);

//            đoạn này sẽ được bê đi qua class xem kết quả chấm tự luận
            Bitmap result = getShortAnswer.getShortAnwer(db, bitmap, makithi, username,"001");
            view.setImageBitmap(result != null ? result : bitmap);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }, 2000);
        }
    }
}