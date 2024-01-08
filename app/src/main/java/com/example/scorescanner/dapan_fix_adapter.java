package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class dapan_fix_adapter extends ArrayAdapter<dapan_item> {
    Activity context;
    int Idlayout;
    private static ArrayList<dapan_item> mylist;
    int makithi;
    DataBase db;
    String madeToEdit;
    public dapan_fix_adapter(Activity context, int idlayout, ArrayList<dapan_item> mylist, DataBase db, int makithi, String madeToEdit)
    {
        super(context, idlayout, mylist);
        this.context = context;
        Idlayout = idlayout;
        dapan_fix_adapter.mylist = mylist;
        this.db = db;
        this.makithi = makithi;
        this.madeToEdit = madeToEdit;
    }

    private static int convertIntToId(char c) {
        switch (c) {
            case 'A':
                return R.id.ansA;
            case 'B':
                return R.id.ansB;
            case 'C':
                return R.id.ansC;
            case 'D':
                return R.id.ansD;
            default:
                return -1;
        }
    }


    @SuppressLint({"ViewHolder"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflator = context.getLayoutInflater();
        if (convertView == null)
            convertView = myInflator.inflate(Idlayout, null);
        dapan_item dapan_item = mylist.get(position);

        RadioGroup radioGroup = convertView.findViewById(R.id.rdgroup);
        radioGroup.setOnCheckedChangeListener(null);

            char optionChar = dapan_item.c;
            int checkedId = convertIntToId(optionChar);
            if (dapan_item.checked == -1) {
                RadioButton firstRadioButton = convertView.findViewById(checkedId);
                firstRadioButton.setChecked(true);
                dapan_item.checked = checkedId;
                dapan_item.c = optionChar;
                mylist.set(position, dapan_item);
            }

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                dapan_item.checked = checkedId;
                if (checkedId == R.id.ansA) {
                    dapan_item.c = 'A';
                } else if (checkedId == R.id.ansB) {
                    dapan_item.c = 'B';
                } else if (checkedId == R.id.ansC) {
                    dapan_item.c = 'C';
                } else if (checkedId == R.id.ansD) {
                    dapan_item.c = 'D';
                }
                mylist.set(position, dapan_item);
            }
        });

        radioGroup.check(dapan_item.checked);

        TextView number = convertView.findViewById(R.id.number);
        number.setText(String.valueOf(dapan_item.getNum()));

        return convertView;
    }
}