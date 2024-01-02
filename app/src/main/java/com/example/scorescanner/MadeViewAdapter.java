package com.example.scorescanner;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MadeViewAdapter extends ArrayAdapter<ListMade> {
    Activity context;
    int Idlayout;
    ArrayList<ListMade> mylist;
    DataBase db;
    int makithi;

    public MadeViewAdapter( Activity context, int idlayout, ArrayList<ListMade> mylist, DataBase db, int makithi) {
        super(context, idlayout,mylist);
        this.context = context;
        Idlayout = idlayout;
        this.mylist = mylist;
        this.db = db;
        this.makithi = makithi;
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

        Button deleteButton = convertView.findViewById(R.id.remove_btn);

        Button repairButton = convertView.findViewById(R.id.repair_btn);

        repairButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleRepairButtonClick(position);
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleDeleteButtonClick(position);
            }
        });

        return convertView;
    }

    private void handleRepairButtonClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Sửa đáp án");
        builder.setMessage("Bạn có chắc muốn sữa đáp án?");

        builder.setPositiveButton("Sửa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String madeToEdit = mylist.get(position).getMade();
                Intent intent = new Intent(context, RepairAnswerHand.class);
                intent.putExtra("madeToEdit", madeToEdit);
                intent.putExtra("makithi", makithi);
                context.startActivity(intent);
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

    private void handleDeleteButtonClick(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc muốn xóa mã đề này?");

        builder.setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String madeToDelete = mylist.get(position).getMade();
                int count = db.mydatabase.delete("cauhoi",
                        "made = '" + madeToDelete + "'", null);
                if (count > 0) {
                    Toast.makeText(context, "Đã xóa mã đề: " + madeToDelete, Toast.LENGTH_SHORT).show();
                    mylist.remove(position);
                    notifyDataSetChanged();
                }
                else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
        });

        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        builder.show();
    }

}
