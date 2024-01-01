package com.example.scorescanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MadeHandAdapter extends ArrayAdapter<String> {
    public int[] getSelect() {
        return select;
    }
    Activity context;
    int Idlayout;
    ArrayList<String> mylist;
    int[] select = new int[4];


    public MadeHandAdapter(Activity context1, int idlayout, ArrayList<String> mylist)  {
        super(context1, idlayout,mylist);
        this.context = context1;
        Idlayout = idlayout;
        this.mylist = mylist;
    }

    int[] arr = {
            R.id.id0,
            R.id.id1,
            R.id.id2,
            R.id.id3,
            R.id.id4,
            R.id.id5,
            R.id.id6,
            R.id.id7,
            R.id.id8,
            R.id.id9,
    };
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(Idlayout,null);

        if(position==0) {
            for(int i=0; i<10; i++) {
                RadioButton rb = convertView.findViewById(arr[i]);
                rb.setEnabled(false);
            }
        }
        else {
            for (int i = 0; i < 10; i++) {
                RadioButton rb = convertView.findViewById(arr[i]);
                int id = i;
                rb.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        select[position] = id;
                    }
                });
            }
        }
        return convertView;
    }

}
