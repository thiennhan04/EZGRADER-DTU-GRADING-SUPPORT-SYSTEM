package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewListAnswer extends AppCompatActivity {
    private static final String TAG = "View List Answer";
    DataBase db;
    TextView result;
    ImageButton back_btn, add_btn;
    ListView list_item;

    ArrayList<ListMade> mylist;
    MadeViewAdapter viewAdapter;
    int makithi;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_list_answer);

        result = findViewById(R.id.result);
        back_btn = findViewById(R.id.back_btn);
        add_btn = findViewById(R.id.add_btn);
        list_item = findViewById(R.id.list_item);

        result.setVisibility(View.VISIBLE);
        list_item.setVisibility(View.INVISIBLE);

        Intent intent = getIntent();
        makithi = intent.getIntExtra("makithi", -1);
        username = intent.getStringExtra("username");

        db = new DataBase(this);

        loadData();

        findViewById(R.id.back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewListAnswer.this.finish();
            }
        });

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewListAnswer.this, AddAnswerHand.class);
                intent.putExtra("username", username);
                startActivity(intent);
            }
        });
    }

    private void loadData() {Cursor c = db.mydatabase.rawQuery("SELECT made from cauhoi WHERE makithi = " + makithi, null);
        if (c.getCount() == 0) {
            result.setVisibility(View.VISIBLE);
            list_item.setVisibility(View.INVISIBLE);
        }
        else {
            result.setVisibility(View.INVISIBLE);
            list_item.setVisibility(View.VISIBLE);

            mylist = new ArrayList<>();
            viewAdapter = new MadeViewAdapter(this, R.layout.list_made, mylist);

            c.moveToFirst();
            while (!c.isAfterLast()) {
                ListMade made_item = new ListMade(makithi, c.getString(0));
                mylist.add(made_item);
                c.moveToNext();
            }

            list_item.setAdapter(viewAdapter);
            viewAdapter.notifyDataSetChanged();
        }
    }
}