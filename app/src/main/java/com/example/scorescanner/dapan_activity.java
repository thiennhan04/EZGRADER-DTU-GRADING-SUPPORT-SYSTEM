package com.example.scorescanner;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class dapan_activity extends AppCompatActivity {

    ListView lvCauHoi;
    ArrayList<dapan_item> mylist;
    dapan_adapter myArrayAdapter;

    ImageButton backbtn;
    TextView tenKithi;
    Button saveAns;
    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText inputMade;
    DataBase db = null;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        try {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_dapan);
            backbtn = findViewById(R.id.back_btnds);
            tenKithi = findViewById(R.id.txtvieweassay);
            saveAns = findViewById(R.id.save_btn);
            db = new DataBase(this);
            inputMade = findViewById(R.id.made_txt);
            lvCauHoi = findViewById(R.id.lvDA);

            Intent intent = getIntent();
//            String makithi = intent.getStringExtra("makithi");
            int makithi = intent.getIntExtra("makithi", -1);
            Log.i("TAG", "onCreate: ma ki thi = " + makithi);
            mylist = new ArrayList<>();
            myArrayAdapter = new dapan_adapter(this, R.layout.cauhoi_item, mylist);
            //truy vấn số câu hỏi
            Cursor c1 = db.mydatabase.rawQuery("select socau from kithi where makithi = " + makithi, null);
            int socau = 0;
            c1.moveToFirst();
//            while (c1.isAfterLast() == false)
            while (!c1.isAfterLast())
            {
                socau = c1.getInt(0);
                c1.moveToNext();
            }

            Log.i("TAG", "onCreate: so cauuu == " + socau);

            c1.close();
//            Toast.makeText(this, "Makithi = " + makithi + " So cau: " + strsocau, Toast.LENGTH_SHORT).show();
            // đọc database gì đó để add vào mylist
            for (int i = 1; i <= socau; i++) {
//                dapan_item item = new dapan_item(i, "#");
//                mylist.add(item);
            }
            Log.i("TAG", "onCreate: may array = " + myArrayAdapter.getCount());

//            int child=lvCauHoi.getChildCount();

            lvCauHoi.setAdapter(myArrayAdapter);
            lvCauHoi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                }
            });
            backbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dapan_activity.this.finish();
                }
            });
            saveAns.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String strmade = inputMade.getText().toString().trim();

                    try {
                        if (strmade.equals("")) {
                            Toast.makeText(dapan_activity.this, "Chưa nhập mã đề!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (Integer.parseInt(strmade) < 0 || Integer.parseInt(strmade) > 999) {
                            Toast.makeText(dapan_activity.this, "Nhập sai định dạng mã đề!", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                     catch (Exception e) {
                        Toast.makeText(dapan_activity.this, "Mã đề phải là số!", Toast.LENGTH_SHORT).show();
                        return;
                     }

                    StringBuilder res = new StringBuilder();

                    int child=myArrayAdapter.getCount();
//                    Boolean status  = true;
                    boolean status  = true;

                    Log.i("TAG", "onClick: Lenght = " + child);

                    for(int i=0;i<child;i++){
                        View rgg=lvCauHoi.getChildAt(i);

                        radioGroup = (RadioGroup) rgg.findViewById(R.id.radiogroup);

                        int selectedId=radioGroup.getCheckedRadioButtonId();

                        if (selectedId == -1) {
                            Toast.makeText(dapan_activity.this, "Chưa tích đủ đáp án!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        radioButton = (RadioButton) rgg.findViewById(selectedId);
                        String selectvalue = radioButton.getText().toString();
                        res.append(selectvalue);
                        Log.i("TAG", "onClick: anser = " + res);
                    }

                    Log.i("TAG", "onClick: Resssssssssssss =" + res);

                    Cursor c = db.mydatabase.rawQuery("select made from cauhoi where makithi = " + makithi +" and made = '"+
                            strmade+"'", null);
                    ContentValues values = new ContentValues();
                    values.put("dapan", res.toString());
                    if(c.getCount()==0)
                    {
                        values.put("made",strmade);
                        values.put("makithi",makithi);
                        if(db.mydatabase.insert("cauhoi",null,values)==-1)
                        {
                            status = false;
                        }
                    }
                    else
                    {
//                        int row = db.mydatabase.update("cauhoi",values,"made = ? and makithi = ?",new String[]{strmade,makithi});
                        int row = db.mydatabase.update("cauhoi",values,"made = '" + strmade + "' and makithi = " + makithi,null);
                        if(row==0)
                        {
                            status = false;
                        }
                    }

                    if(status)
                        Toast.makeText(dapan_activity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                    else{
                        Toast.makeText(dapan_activity.this, "Thêm Thất bại", Toast.LENGTH_SHORT).show();
                    }
//                    Toast.makeText(dapan_activity.this, ""+ res, Toast.LENGTH_SHORT).show();
                    dapan_activity.this.finish();
                }
            });
        }
        catch(Exception ex)
        {
            Log.println(Log.DEBUG,"======2=====",ex.getMessage()+"=========");
        }
    }
}
