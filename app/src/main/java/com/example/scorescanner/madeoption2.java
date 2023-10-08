package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class madeoption2 extends AppCompatActivity {
    static String makithi;
    static String username;

    public static String getMakithi() {
        return makithi;
    }

    public static String getUsername() {
        return username;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_madeoption2);

        findViewById(R.id.tn_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(madeoption2.this, OptionAddFileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        findViewById(R.id.tl_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(madeoption2.this, OptionAddFileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        findViewById(R.id.camera_cham_bai_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makithi = getIntent().getStringExtra("makithi");
                username = getIntent().getStringExtra("username");
                Intent intent = new Intent(madeoption2.this, CameraRealTime.class);
                intent.putExtra("makithi", makithi);
                intent.putExtra("username", username);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }
}