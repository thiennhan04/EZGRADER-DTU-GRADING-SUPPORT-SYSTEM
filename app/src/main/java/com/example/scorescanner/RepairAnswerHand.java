package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class RepairAnswerHand extends AppCompatActivity {
    EditText edtMade;
    ImageButton back_btn, save_btn;
    ArrayList<dapan_item> mylist;
    dapan_fix_adapter myArrayAdapter;
    DataBase db;
    ListView lstDa;
    int makithi;
    String madeToEdit;
    String TAG = "=======";
    Intent intent;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repair_answer_hand);

        intent = getIntent();
        makithi = intent.getIntExtra("makithi", -1);
        username = intent.getStringExtra("username");
        madeToEdit = intent.getStringExtra("madeToEdit");
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
                String dapan  = "";
                for (int i = 0; i < mylist.size(); i++) {
                    if (mylist.get(i).c == '#') {
                        Toast.makeText(RepairAnswerHand.this, "Chưa đủ đáp án!", Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onClick: i= ===== " + i);
                        return;
                    } else {
                        dapan = String.join("", dapan + mylist.get(i).c);
                        Log.i(TAG, "onClick: dap an " + dapan);
                    }
                }

                Cursor c = db.mydatabase.rawQuery("SELECT made FROM cauhoi WHERE makithi = " + makithi + " AND made = '" + madeToEdit + "' AND kieucauhoi = 1",
                        null);
                ContentValues values = new ContentValues();
                values.put("dapan", dapan);
                if (c.getCount() != 0) {
                    values.put("username", username);
                    values.put("made", madeToEdit);
                    values.put("makithi", makithi);
                    values.put("kieucauhoi",1);
                    if (db.mydatabase.update("cauhoi", values, "made = '" + madeToEdit + "' and makithi = " + makithi + " and kieucauhoi = 1", null) == -1) {
                        Toast.makeText(RepairAnswerHand.this, "Sửa không thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RepairAnswerHand.this, "Sửa thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }
        });

        loadDataListKithi();
    }

    private void loadDataListKithi(){
        edtMade.setText(madeToEdit);
        edtMade.setEnabled(false);
        Cursor c = db.mydatabase.rawQuery("SELECT socau from kithi WHERE makithi = " + makithi, null);
        if (c.getCount() == 0) return;

        c.moveToFirst();
        int socau = c.getInt(0);
        mylist = new ArrayList<>();
        myArrayAdapter = new dapan_fix_adapter(this, R.layout.dapan_item, mylist, db, makithi, madeToEdit);
        Cursor c2 = db.mydatabase.rawQuery("select dapan from cauhoi where kieucauhoi = 1 and makithi = " + makithi + " and made = '" + madeToEdit + "'", null);
        c2.moveToFirst();
        String dapan = c2.getString(0);

        for (int i = 1; i <= socau; i++) {
            char character = dapan.charAt(i-1);
            dapan_item dapanItem = new dapan_item(i, character);
            mylist.add(dapanItem);
        }

        lstDa.setAdapter(myArrayAdapter);
        myArrayAdapter.notifyDataSetChanged();


    }
}