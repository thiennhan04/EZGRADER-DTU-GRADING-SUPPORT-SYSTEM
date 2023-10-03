package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class madeoption2 extends AppCompatActivity {


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_madeoption2);
        Intent intent = getIntent();
        String makithi = intent.getStringExtra("makithi");
        String username = intent.getStringExtra("username");
        findViewById(R.id.tn_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(madeoption2.this, OptionAddFileActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
            }
        });

        findViewById(R.id.tl_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dapantuluan = new Intent(madeoption2.this, list_danhsachtuluan.class);
                dapantuluan.putExtra("makithi", makithi);
                dapantuluan.putExtra("username",username);
                startActivity(dapantuluan);
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(madeoption2.this, CameraRealTime.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP));
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