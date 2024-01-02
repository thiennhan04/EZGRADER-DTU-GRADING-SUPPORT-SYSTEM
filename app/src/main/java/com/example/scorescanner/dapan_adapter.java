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
        convertView = myInflactor.inflate(Idlayout,null);
        dapan_item dapan_item = mylist.get(position);
        Log.i("TAG", "getView: === "+position+" "+dapan_item.checked);

        RadioButton ansA = convertView.findViewById(R.id.ansA);
        ansA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dapan_item.checked = R.id.ansA;
                ansA.setChecked(true);
            }
        });
        RadioButton ansB = convertView.findViewById(R.id.ansB);
        ansB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dapan_item.checked = R.id.ansB;
                ansB.setChecked(true);
            }
        });
        RadioButton ansC = convertView.findViewById(R.id.ansC);
        ansC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dapan_item.checked = R.id.ansC;
                ansC.setChecked(true);
            }
        });
        RadioButton ansD = convertView.findViewById(R.id.ansD);
        ansD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dapan_item.checked = R.id.ansD;
                ansD.setChecked(true);
            }
        });

        RadioGroup radioGroup = convertView.findViewById(R.id.rdgroup);

        dapan_item.checked = radioGroup.getCheckedRadioButtonId();

        if (dapan_item.checked != -1) {
            RadioButton ans = convertView.findViewById(dapan_item.checked);
            ans.setChecked(true);
        }

        TextView number = convertView.findViewById(R.id.number);
        number.setText(String.valueOf(dapan_item.getNum()));

        return convertView;
    }
}
