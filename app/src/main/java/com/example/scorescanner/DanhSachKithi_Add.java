package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;

public class DanhSachKithi_Add extends AppCompatActivity {
    private List<String> list;
    Spinner spinner;
    EditText txttenkithi,txttenkithi2,txttenkithi3;
    ImageButton savebtn2;
    String loaiphieu = "";
    Intent myintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_kithi_add);
        spinner = findViewById(R.id.spinner);
        txttenkithi = findViewById(R.id.txttenkithi);
        txttenkithi2 = findViewById(R.id.txttenkithi2);
        txttenkithi3 = findViewById(R.id.txttenkithi3);
        savebtn2 = findViewById(R.id.savebtn2);
        list = new ArrayList<>();
        list.add("20");
        list.add("40");
        list.add("50");
        list.add("60");
        list.add("120");
        myintent = getIntent();
        String username = myintent.getStringExtra("username");
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);
        //Bắt sự kiện cho Spinner, khi chọn phần tử nào thì hiển thị lên Toast
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                //đối số postion là vị trí phần tử trong list Data
                 loaiphieu = list.get(position).toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                loaiphieu = list.get(0).toString();
            }
        });
        savebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkithi = txttenkithi.getText()+"";
                String socau = txttenkithi2.getText()+"";
                String hediem = txttenkithi3.getText()+"";
                myintent.putExtra("tenkithi",tenkithi);
                myintent.putExtra("socau",socau);
                myintent.putExtra("hediem",hediem);
                myintent.putExtra("loaiphieu",loaiphieu);
                myintent.putExtra("username",username);
                setResult(33,myintent);
                finish();
            }
        });
    }
}