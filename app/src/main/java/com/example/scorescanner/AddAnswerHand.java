package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;

public class AddAnswerHand extends AppCompatActivity {
    GridView lstview, lstview2;
    Button madebtn, dapanbtn;
    ImageButton savebtn;

    ArrayList<String> mylist, mylist2;
    MadeHandAdapter myArrayAdapter, myArrayAdapter2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_answer_hand);

        madebtn = findViewById(R.id.made_btn);
        dapanbtn =findViewById(R.id.dapan_btn);
        lstview = findViewById(R.id.lstview);
        lstview2 = findViewById(R.id.lstview2);
        savebtn = findViewById(R.id.savebtn);
        loadDataListKithi();

        lstview.setVisibility(View.VISIBLE);
        lstview2.setVisibility(View.INVISIBLE);

        madebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstview2.setVisibility(View.INVISIBLE);
                lstview.setVisibility(View.VISIBLE);
            }
        });

        dapanbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lstview.setVisibility(View.INVISIBLE);
                lstview2.setVisibility(View.VISIBLE);
            }
        });

//        lstview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                String selectedValue = mylist.get(position);
//
//                Log.d("AddAnswerHand", "Giá trị được chọn: " + selectedValue);
//            }
//        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int[] select  = myArrayAdapter.getSelect();
                Log.i("TAG", "onClick: ====== "+ select[1] + select[2]+ select[3]);
            }
        });
    }

    private void loadDataListKithi(){

        mylist = new ArrayList<>();

        mylist.add("0");
        mylist.add("0");
        mylist.add("0");
        mylist.add("0");

        myArrayAdapter = new MadeHandAdapter(this,R.layout.made_item,mylist);


        lstview.setAdapter(myArrayAdapter);
        myArrayAdapter.notifyDataSetChanged();
    }
}