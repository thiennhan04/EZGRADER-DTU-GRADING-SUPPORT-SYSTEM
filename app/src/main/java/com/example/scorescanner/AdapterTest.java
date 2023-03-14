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

public class AdapterTest extends ArrayAdapter<String> {
    Activity context;
    int idlayout;
    ArrayList<String> mylist;

    public AdapterTest( Activity context1, int idlayout, ArrayList<String> mylist) {
        super(context1, idlayout,mylist);
        this.context = context1;
        this.idlayout = idlayout;
        this.mylist = mylist;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(idlayout,null);
        String name  = mylist.get(position);
        ImageView imgexam = convertView.findViewById(R.id.imageView2);
        imgexam.setImageResource(R.drawable.exam);
        TextView textView3 = convertView.findViewById(R.id.textView3);
        textView3.setText(name);
        return convertView;
    }
}
