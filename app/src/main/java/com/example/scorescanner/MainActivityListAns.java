package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivityListAns extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_list_ans);
        TextView view = findViewById(R.id.textView1);
        Intent intent = getIntent();
        String makithi = intent.getStringExtra("kithi");
        String made = intent.getStringExtra("made");
        DataBase db = new DataBase(this);
        Cursor c = db.mydatabase.rawQuery("select * from cauhoi where makithi = " + makithi + " and made = '" + made + "'", null);
        c.moveToFirst();
        String data ="";
        while (c.isAfterLast() == false)
        {
            String listanswer = c.getString(2);
            data+=listanswer;
            c.moveToNext();
        }
        view.setText(data);
    }
}