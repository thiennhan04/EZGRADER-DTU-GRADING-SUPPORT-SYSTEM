package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class testlv extends AppCompatActivity {
    ListView lv1;
    ArrayList<String> mylist;
    AdapterTest adapterTest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testlv);
        mylist = new ArrayList<>();
        lv1 = findViewById(R.id.lv1);
        for (int i = 0; i <5;i++){
            mylist.add("bai kiem tra");
        }
        adapterTest = new AdapterTest(this,R.layout.testlv,mylist);
        lv1.setAdapter(adapterTest);
        lv1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(testlv.this, "click", Toast.LENGTH_SHORT).show();
            }
        });
    }
}