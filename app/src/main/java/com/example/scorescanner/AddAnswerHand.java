package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;

import java.util.ArrayList;

public class AddAnswerHand extends AppCompatActivity {
    EditText edtMade;
    ImageButton back_btn, save_btn;
    ArrayList<dapan_item> mylist;
    dapan_adapter myArrayAdapter;
    DataBase db;
    ListView lstDa;
    int makithi;
    String TAG = "=======";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer_hand);

        Intent intent = getIntent();
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

        loadDataListKithi();

        lstDa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemClick: iiiii " + i);
            }
        });
    }

    private void loadDataListKithi(){
        Cursor c = db.mydatabase.rawQuery("SELECT socau from kithi WHERE makithi = " + makithi, null);
        if (c.getCount() != 0) {
            c.moveToFirst();
            int socau = c.getInt(0);
            mylist = new ArrayList<>();
            myArrayAdapter = new dapan_adapter(this, R.layout.dapan_item, mylist);
            for (int i = 1; i <= socau; i++) {
                dapan_item dapanItem = new dapan_item(i);
                mylist.add(dapanItem);
            }

            lstDa.setAdapter(myArrayAdapter);
            myArrayAdapter.notifyDataSetChanged();
        }
    }
}