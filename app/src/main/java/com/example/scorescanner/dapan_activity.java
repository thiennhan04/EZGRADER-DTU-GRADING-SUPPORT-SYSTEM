package com.example.scorescanner;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Iterator;

public class dapan_activity extends AppCompatActivity {

    ListView lvCauHoi;
    ArrayList<dapan_item> mylist;
    dapan_adapter myArrayAdapter;

    ImageButton backbtn;
    TextView tenKithi;
    Button saveAns;

    EditText inputMade;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dapan);
            backbtn = findViewById(R.id.back_btn);
            tenKithi = findViewById(R.id.txtmade);
            saveAns = findViewById(R.id.save_btn);

            inputMade = findViewById(R.id.made_txt);
            lvCauHoi = findViewById(R.id.lvDA);

            Intent intent = getIntent();
            String makithi = intent.getStringExtra("makithi");
            mylist = new ArrayList<>();

            myArrayAdapter = new dapan_adapter(this, R.layout.cauhoi_item, mylist);

            // đọc database gì đó để add vào mylist
            String ans = "CDBDCADCAABDCABCDACD";
            for (int i = 1; i <= 20; i++) {
                dapan_item item = new dapan_item(i, ans.charAt(i-1) + "");
                mylist.add(item);
            }
            lvCauHoi.setAdapter(myArrayAdapter);
            lvCauHoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
        }
        catch(Exception ex)
        {
            Log.println(Log.DEBUG,"======2=====",ex.getMessage()+"=========");
        }
    }
}
