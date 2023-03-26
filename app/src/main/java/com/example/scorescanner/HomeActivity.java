package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class HomeActivity extends AppCompatActivity {
    ImageButton chambaibtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        chambaibtn = findViewById(R.id.chambaibtn);
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
    }
}