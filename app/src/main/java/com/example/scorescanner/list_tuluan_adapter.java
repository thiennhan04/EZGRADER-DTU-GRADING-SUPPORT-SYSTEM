package com.example.scorescanner;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class list_tuluan_adapter extends ArrayAdapter<Question> {
    Activity context;
    int idlayout;
    ArrayList<Question> list_tuluan;

    public list_tuluan_adapter(Activity context, int idlayout, ArrayList<Question> list_tuluan)
    {
        super(context,idlayout,list_tuluan);
        this.context = context;
        this.idlayout = idlayout;
        this.list_tuluan = list_tuluan;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater myInflactor = context.getLayoutInflater();
        convertView = myInflactor.inflate(idlayout, null);
        TextView txttitle = convertView.findViewById(R.id.txttitle);
        EditText txttuluan  = convertView.findViewById(R.id.edttuluan);
        if(position == list_tuluan.size()-1){
            txttuluan.setImeOptions(EditorInfo.IME_ACTION_DONE);

        }
        txttuluan.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // Xử lý khi "Done" được bấm

                    // Ẩn bàn phím
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(txttuluan.getWindowToken(), 0);
                    // Không cần tải lại ListView
                    return true; // Trả về true để chỉ định đã xử lý sự kiện
                }
                return false;

            }
        });
        String tencauhoi = "Đ.Án câu: "+list_tuluan.get(position).getTencauhoi();
        txttitle.setText(tencauhoi);
        return convertView;
    }
}
