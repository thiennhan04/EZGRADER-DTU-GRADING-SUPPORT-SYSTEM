package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class AddAnswerHand extends AppCompatActivity {
    EditText edtMade;
    ImageButton back_btn, save_btn;
    ArrayList<dapan_item> mylist;
    dapan_adapter myArrayAdapter;
    DataBase db;
    ListView lstDa;
    int makithi;
    String TAG = "=======";
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer_hand);

        intent = getIntent();
        makithi = intent.getIntExtra("makithi", -1);
        db = new DataBase(this);

        lstDa = findViewById(R.id.lstDa);
        save_btn = findViewById(R.id.savebtn);
        back_btn = findViewById(R.id.back_btnds);
        edtMade = findViewById(R.id.edtMade);

        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validationMade() && validationAnswer()) {
                    ArrayList<String> selectedRadioButtons = dapan_adapter.getSelectedRadioButtons();
                    String dapan = String.join("", selectedRadioButtons);
                    String made = edtMade.getText().toString();
                    Cursor c = db.mydatabase.rawQuery("SELECT made FROM cauhoi WHERE makithi = " + makithi + " AND made = '" + made + "' AND kieucauhoi = 1",
                            null);
                    ContentValues values = new ContentValues();
                    values.put("dapan", dapan);
                    if (c.getCount() == 0) {
                        values.put("made", made);
                        values.put("makithi", makithi);
                        values.put("kieucauhoi",1);
                        if (db.mydatabase.insert("cauhoi", null, values) == -1) {
                            Toast.makeText(AddAnswerHand.this, "Thêm không thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(AddAnswerHand.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                }
            }
        });

        loadDataListKithi();
    }

    public boolean validationAnswer() {
        boolean status = true;
        ArrayList<String> selectedRadioButtons = dapan_adapter.getSelectedRadioButtons();
        if (selectedRadioButtons.contains("#")){
            status =false;
            Toast.makeText(AddAnswerHand.this, "Bạn chưa nhập đủ đáp án!", Toast.LENGTH_SHORT).show();
        }

        return status;
    }

    public boolean validationMade() {
        String madeValue = edtMade.getText().toString();
        boolean status = true;

        if (madeValue.trim().isEmpty()) {
            Toast.makeText(AddAnswerHand.this, "Mã đề không được để trống!", Toast.LENGTH_SHORT).show();
        } else {
            try {
                if (madeValue.length() != 3) {
                    status = false;
                    Toast.makeText(AddAnswerHand.this, "Mã đề phải có 3 chữ số!", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(madeValue) < 0) {
                    status = false;
                    Toast.makeText(AddAnswerHand.this, "Mã đề lớn hơn bằng 0!", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(madeValue) > 999) {
                    status = false;
                    Toast.makeText(AddAnswerHand.this, "Mã đề nhỏ hơn bằng 999!", Toast.LENGTH_SHORT).show();
                } else {
                    Cursor c = db.mydatabase.rawQuery("SELECT COUNT(made) FROM cauhoi WHERE makithi = " + makithi + " AND made = '" + madeValue + "'" , null);
                    c.moveToFirst();
                    if (c.getInt(0) == 1) {
                        status = false;
                        Toast.makeText(AddAnswerHand.this, "Đã có mã đề!", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (NumberFormatException e) {
                status = false;
                Toast.makeText(AddAnswerHand.this, "Mã đề không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        }
        return status;
    }

    private void loadDataListKithi(){
        Cursor c = db.mydatabase.rawQuery("SELECT socau from kithi WHERE makithi = " + makithi, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            int socau = c.getInt(0);
            mylist = new ArrayList<>();
            myArrayAdapter = new dapan_adapter(this, R.layout.dapan_item, mylist);
            for (int i = 1; i <= socau; i++) {
                dapan_item dapanItem = new dapan_item(i, '#');
                mylist.add(dapanItem);
            }

            lstDa.setAdapter(myArrayAdapter);
            myArrayAdapter.notifyDataSetChanged();
        }
    }
}