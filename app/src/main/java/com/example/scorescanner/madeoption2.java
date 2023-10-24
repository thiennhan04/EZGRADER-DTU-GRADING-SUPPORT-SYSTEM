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
        Intent intent = getIntent();
        makithi = intent.getStringExtra("makithi");
        username = intent.getStringExtra("username");
        findViewById(R.id.dapantnbtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(madeoption2.this, OptionAddFileActivity.class);
                intent.putExtra("makithi", makithi);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        findViewById(R.id.datlbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dapantuluan = new Intent(madeoption2.this, list_danhsachtuluan.class);
                dapantuluan.putExtra("makithi", makithi);
                dapantuluan.putExtra("username",username);
                startActivity(dapantuluan);
            }
        });

        findViewById(R.id.camerachambai).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(madeoption2.this, CameraRealTime.class);
                intent.putExtra("makithi", makithi);
                intent.putExtra("username", username);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        findViewById(R.id.btnBaiDaCham).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent baidacham = new Intent(madeoption2.this, Baidacham.class);
                baidacham.putExtra("makithi", makithi + "");
                startActivity(baidacham);
            }
        });

        findViewById(R.id.thongkebtn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(madeoption2.this, ThongKeActivity.class);
                intent.putExtra("makithi",makithi);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        findViewById(R.id.backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                madeoption2.this.finish();
            }
        });
    }
}