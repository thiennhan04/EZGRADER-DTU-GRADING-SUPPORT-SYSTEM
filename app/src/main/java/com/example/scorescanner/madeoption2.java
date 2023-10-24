package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class madeoption2 extends AppCompatActivity {
    static String makithi;
    static String username;
    Button dapantnbtn,chambaibtn,baidachambtn,thongkebtn;
    Button datlbtn;
    ImageButton imgremove;
    public static SQLiteDatabase database=null;
    String DB_PATH_SUFFIX = "/databases/";
    DataBase db = null;
    String DATABASE_NAME="ssdb2.db";

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

        db = new DataBase(this);

//        findViewById(R.id.tn_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(madeoption2.this, OptionAddFileActivity.class);
//                intent.putExtra("makithi", makithi);
//                intent.putExtra("username",username);
//                startActivity(intent);
//            }
//        });
        //giao dien "dap an trac nghiem" moi
        dapantnbtn = findViewById(R.id.dapantnbtn1);
        dapantnbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(madeoption2.this, OptionAddFileActivity.class);
                intent.putExtra("makithi", makithi);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

//        findViewById(R.id.tl_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent dapantuluan = new Intent(madeoption2.this, list_danhsachtuluan.class);
//                dapantuluan.putExtra("makithi", makithi);
//                dapantuluan.putExtra("username",username);
//                startActivity(dapantuluan);
//            }
//        });
        //giao diện "đáp án tự luận mới"
        datlbtn = findViewById(R.id.datlbtn);
        datlbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dapantuluan = new Intent(madeoption2.this, list_danhsachtuluan.class);
                dapantuluan.putExtra("makithi", makithi);
                dapantuluan.putExtra("username",username);
                startActivity(dapantuluan);
            }
        });


//
//        findViewById(R.id.camera_cham_bai_btn).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(madeoption2.this, CameraRealTime.class);
//                intent.putExtra("makithi", makithi);
//                intent.putExtra("username", username);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }
//        });
        //giao diện camera chấm bài mới
        chambaibtn = findViewById(R.id.camerachambai);
        chambaibtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(madeoption2.this, CameraRealTime.class);
                intent.putExtra("makithi", makithi);
                intent.putExtra("username", username);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        //sự kiện cho nút xóa
        imgremove = findViewById(R.id.imgremove2);
        imgremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean removeStatus = true;
                int rowAffect1 = 0,rowAffect2 = 0,rowAffect3 = 0,rowAffect4 = 0;
                String msgDeleteComplete = "";
                String msgDeleteFailed = "";
                rowAffect1 = db.mydatabase.delete("cauhoi",
                        "makithi = ? and username = ?",new String[]{makithi,username});
                rowAffect2 = db.mydatabase.delete("diem",
                        "makithi = ?",new String[]{makithi});
                rowAffect3 = db.mydatabase.delete("made",
                        "makithi = ?",new String[]{makithi});
                rowAffect4 = db.mydatabase.delete("kithi",
                        "makithi = ? and username = ?",new String[]{makithi, username});
                if(rowAffect4 > 0) Toast.makeText(madeoption2.this, "Xóa thành công!", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(madeoption2.this, "Xóa thắt bại!", Toast.LENGTH_SHORT).show();
                }
//                Toast.makeText(madeoption2.this,
//                        "" + rowAffect1 + " " + rowAffect2 + " " + rowAffect3 + " " + rowAffect4, Toast.LENGTH_SHORT).show();
                madeoption2.this.finish();
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


        //sự kiện cho nút quay lại

        findViewById(R.id.backbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                madeoption2.this.finish();
            }
        });
    }
}