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
    private static ArrayList<dapan_item> mylist;

    public dapan_adapter(Activity context, int idlayout, ArrayList<dapan_item> mylist)
    {
        super(context, idlayout, mylist);
        this.context = context;
        Idlayout = idlayout;
        dapan_adapter.mylist = mylist;
    }

    public static ArrayList<String> getSelectedRadioButtons() {
        ArrayList<String> selectedRadioButtons = new ArrayList<>();

        for (int i = 0; i < mylist.size(); i++) {
            dapan_item item = mylist.get(i);
            selectedRadioButtons.add(item.checked == -1 ? "#" : getRadioButtonPosition(item.checked));
        }

        return selectedRadioButtons;
    }

    @SuppressLint("NonConstantResourceId")
    private static String getRadioButtonPosition(int checkedId) {
        switch (checkedId) {
            case R.id.ansA:
                return "A";
            case R.id.ansB:
                return "B";
            case R.id.ansC:
                return "C";
            case R.id.ansD:
                return "D";
            default:
                return "#";
        }
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