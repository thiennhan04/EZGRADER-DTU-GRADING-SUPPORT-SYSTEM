package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class dapan_adapter extends ArrayAdapter<dapan_item> {
    Activity context;
    int Idlayout;
    private ArrayList<dapan_item> mylist;

    public dapan_adapter(Activity context, int idlayout, ArrayList<dapan_item> mylist)
    {
        super(context, idlayout, mylist);
        this.context = context;
        Idlayout = idlayout;
        this.mylist = mylist;
    }

    @SuppressLint({"ViewHolder"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        if(convertView == null)
            convertView = myInflactor.inflate(Idlayout,null);
        dapan_item dapan_item = mylist.get(position);
        RadioGroup radioGroup = convertView.findViewById(R.id.rdgroup);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dapan_item.checked = checkedId;
                mylist.set(position,dapan_item);
            }
        });

        radioGroup.check(dapan_item.checked);

        TextView number = convertView.findViewById(R.id.number);
        number.setText(String.valueOf(dapan_item.getNum()));

        return convertView;
    }
}
