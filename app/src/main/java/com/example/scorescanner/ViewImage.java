package com.example.scorescanner;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ViewImage extends AppCompatActivity {
    private Methods methods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_image);
        methods = new Methods();
        ImageView view = findViewById(R.id.imageView);
        Bitmap bitmap = CameraRealTime.getRotatedBitmap();
        if (bitmap != null && view != null) {
            Bitmap result = methods.run(bitmap);
            view.setImageBitmap(result != null ? result : bitmap);

//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    finish();
//                }
//            }, 5000);
        }
    }
}