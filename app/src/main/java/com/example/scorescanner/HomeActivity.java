package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    ImageButton chambaibtn, taiphieubtn, thuvienbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        chambaibtn = findViewById(R.id.chambaibtn);
        taiphieubtn = findViewById(R.id.taiphieubtn);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        chambaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent danhsachcham = new Intent(HomeActivity.this, danhsachkithi.class);
                danhsachcham.putExtra("username", username);
                startActivity(danhsachcham);
            }
        });
        taiphieubtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://www.trendysmarts.com/scorescanner");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                
                startActivity(intent);
            }
        });
    }
}