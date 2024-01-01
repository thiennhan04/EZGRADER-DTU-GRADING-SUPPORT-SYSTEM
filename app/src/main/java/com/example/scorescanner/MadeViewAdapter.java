package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MadeViewAdapter extends ArrayAdapter<ListMade> {
    Activity context;
    int Idlayout;
    ArrayList<ListMade> mylist;

    public MadeViewAdapter( Activity context, int idlayout, ArrayList<ListMade> mylist) {
        super(context, idlayout,mylist);
        this.context = context;
        Idlayout = idlayout;
        this.mylist = mylist;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(Idlayout,null);
        ListMade made_item = mylist.get(position);

        TextView tvMaDeValue = convertView.findViewById(R.id.tvMaDeValue);
        tvMaDeValue.setText(made_item.getMade());

        return convertView;
    }
}
