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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DanhSachKithi_Add extends AppCompatActivity {
    private List<String> list, list2;
    Spinner spinner, spinkieukithi;
    EditText txttenkithi,txttenkithi2,txttenkithi3;
    ImageButton savebtn2;
    String loaiphieu = "";
    String kieukithi = "";
    Intent myintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_danh_sach_kithi_add);
        spinner = findViewById(R.id.spinner);
        txttenkithi = findViewById(R.id.txttenkithi);
        txttenkithi2 = findViewById(R.id.txttenkithi2);
        txttenkithi3 = findViewById(R.id.txttenkithi3);
        spinkieukithi = findViewById(R.id.spinkieukithi);
        savebtn2 = findViewById(R.id.savebtn2);
        //loai phieu
        list = new ArrayList<>();
        list.add("50");

        list2 = new ArrayList<>();
        list2.add("TN");
        list2.add("TN & TL");

        myintent = getIntent();
        String username = myintent.getStringExtra("username");
        //set du lieu  cho spinner loai phieu
        ArrayAdapter spinnerAdapter = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list);
        spinner.setAdapter(spinnerAdapter);


        //set du lieu  cho spinner loai phieu
        ArrayAdapter spinnerAdapter2 = new ArrayAdapter<>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, list2);
        spinkieukithi.setAdapter(spinnerAdapter2);

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

        spinkieukithi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //đối số postion là vị trí phần tử trong list Data
                kieukithi = list2.get(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                kieukithi = list2.get(0).toString();
            }
        });
        savebtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tenkithi = txttenkithi.getText()+"";
                int socau = -1;
                try {
                    socau = Integer.parseInt((txttenkithi2.getText()+"").trim());
                    if(socau<0 || 50<socau) throw new Exception("Lỗi");
                }
                catch (Exception ex){
                    Toast.makeText(DanhSachKithi_Add.this, "Số câu không hợp lệ, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    return;
                }
                double hediem = 0.1;
                try {
                    hediem = Double.parseDouble(txttenkithi3.getText() + "");
                    if(hediem <= 0) throw new Exception("Lỗi");
                }
                catch (Exception ex) {
                    Toast.makeText(DanhSachKithi_Add.this, "Hệ điểm không hợp lệ, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    return;
                }
                myintent.putExtra("tenkithi",tenkithi);
                myintent.putExtra("socau",socau);
                myintent.putExtra("hediem",hediem);
                myintent.putExtra("loaiphieu",loaiphieu);
                myintent.putExtra("username",username);
                int kieu = 2;
                if(kieukithi.equals("TN")) kieu = 1;
                myintent.putExtra("kieukithi",kieu);

                setResult(33,myintent);
                finish();
            }
        });
    }
}