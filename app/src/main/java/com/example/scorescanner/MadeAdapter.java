package com.example.scorescanner;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MadeAdapter extends ArrayAdapter<String> {
    Activity context;
    int Idlayout;
    ArrayList<String> mylist;

    public MadeAdapter(Activity context1, int idlayout, ArrayList<String> mylist) {
        super(context1, idlayout,mylist);
        this.context = context1;
        Idlayout = idlayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(Idlayout,null);
        String made = mylist.get(position);
        // Ứng với mỗi thuộc tính, ta thực hiện 2 việc
        // -Gán id
        ImageView imgexam = convertView.findViewById(R.id.exambtn);
        imgexam.setImageResource(R.drawable.exam);
        TextView txtexam = convertView.findViewById(R.id.txtexam);
        txtexam.setText(made);
        return convertView;
    }
}
