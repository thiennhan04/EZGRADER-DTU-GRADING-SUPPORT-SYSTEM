package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {
    TextView txtusername;
    ImageButton chambaibtn, taiphieubtn, logout,guide;
    int backPressedCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        txtusername = findViewById(R.id.txtusername);
        chambaibtn = findViewById(R.id.camerachambai);
        taiphieubtn = findViewById(R.id.taiphieubtn);
        logout = findViewById(R.id.logout);
        guide = findViewById(R.id.guide);
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        txtusername.setText(username);
        Toast.makeText(HomeActivity.this, "Chào mừng đăng nhập!", Toast.LENGTH_SHORT).show();
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
                Uri uri = Uri.parse("https://drive.google.com/file/d/19Gx-0qpRYEqIdOdQhAX5QamWIE30cF0O/view");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
            }
        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setTitle("Thông báo!");
                builder.setMessage("Xác nhận đăng xuất?");

                builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        HomeActivity.this.finish();
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                builder.show();
            }
        });
        guide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sp = new Intent(HomeActivity.this, SupportOption.class);
                startActivity(sp);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (backPressedCount == 0) {
            Toast.makeText(this, "Nhấn 1 lần nữa để thoát app", Toast.LENGTH_SHORT).show();
            backPressedCount++;

            android.os.Handler handler = new android.os.Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedCount = 0;
                }
            }, 2000);
        } else {
            super.onBackPressed();
        }
    }
}