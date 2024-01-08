package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class list_danhsachtuluan extends AppCompatActivity {
    ListView list_cauhoitl;
    ArrayList<Question> tllist;
    ImageButton back_btnds;
    list_tuluan_adapter list_tuluan_adapter;
    Button btn_save_list_tl;
    public static SQLiteDatabase database=null;
    String DB_PATH_SUFFIX = "/databases/";
    DataBase db = null;
    String DATABASE_NAME="ssdb2.db";
    String username = "";
//    String makithi = "";
    int makithi;
    EditText edtmade,edttuluan1,edttuluan2,edttuluan3,edttuluan4,edttuluan5;
    EditText edtcauhoi1,edtcauhoi2,edtcauhoi3,edtcauhoi4,edtcauhoi5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_danhsachtuluan);
        back_btnds = findViewById(R.id.back_btnds);
        edtmade = findViewById(R.id.edtmade);

        //lấy text câu hỏi
        edtcauhoi1 = findViewById(R.id.edtcauhoi1);
        edtcauhoi2 = findViewById(R.id.edtcauhoi2);
        edtcauhoi3 = findViewById(R.id.edtcauhoi3);
        edtcauhoi4 = findViewById(R.id.edtcauhoi4);
        edtcauhoi5 = findViewById(R.id.edtcauhoi5);

        //lấy text tự luận
        edttuluan1 = findViewById(R.id.edttuluan1);
        edttuluan2 = findViewById(R.id.edttuluan2);
        edttuluan3 = findViewById(R.id.edttuluan3);
        edttuluan4 = findViewById(R.id.edttuluan4);
        edttuluan5 = findViewById(R.id.edttuluan5);
        btn_save_list_tl = findViewById(R.id.btn_save_list_tl);
        Intent intent = getIntent();
        db = new DataBase(this);
        //text của đáp án các câu hỏi
        username = intent.getStringExtra("username");
        makithi = intent.getIntExtra("makithi", -1);
        back_btnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_danhsachtuluan.this.finish();
            }
        });
        btn_save_list_tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String made = edtmade.getText().toString().trim();

                String cauhoi1 = edtcauhoi1.getText().toString().trim();
                String cauhoi2 = edtcauhoi2.getText().toString().trim();
                String cauhoi3 = edtcauhoi3.getText().toString().trim();
                String cauhoi4 = edtcauhoi4.getText().toString().trim();
                String cauhoi5 = edtcauhoi5.getText().toString().trim();

                String cau1 = edttuluan1.getText().toString().trim();
                String cau2 = edttuluan2.getText().toString().trim();
                String cau3 = edttuluan3.getText().toString().trim();
                String cau4 = edttuluan4.getText().toString().trim();
                String cau5 = edttuluan5.getText().toString().trim();

                try {
                    if(made.length()==0) throw new Exception("Bạn chưa nhập mã đề");
                    int intmade=-1;
                    try{ intmade = Integer.parseInt(made);} catch(Exception ex) {throw new Exception("Mã đề sai định dạng");}
                    if(intmade < 0 || intmade>999) throw new Exception("Mã đề phải từ 000 đến 999");
                    if(made.length()!=3) throw new Exception("Mã đề phải từ 000 đến 999");
                }
                catch (Exception ex) {
                    Toast.makeText(list_danhsachtuluan.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    if (cauhoi1.equals("")&&!cau1.equals("")) throw new Exception("Hãy nhập đầy đủ câu hỏi 1");
                    if (cauhoi2.equals("")&&!cau2.equals("")) throw new Exception("Hãy nhập đầy đủ câu hỏi 2");
                    if (cauhoi3.equals("")&&!cau3.equals("")) throw new Exception("Hãy nhập đầy đủ câu hỏi 3");
                    if (cauhoi4.equals("")&&!cau4.equals("")) throw new Exception("Hãy nhập đầy đủ câu hỏi 4");
                    if (cauhoi5.equals("")&&!cau5.equals("")) throw new Exception("Hãy nhập đầy đủ câu hỏi 5");

                    if (!cauhoi1.equals("")&&cau1.equals("")) throw new Exception("Hãy nhập đầy đủ đáp án câu 1");
                    if (!cauhoi2.equals("")&&cau2.equals("")) throw new Exception("Hãy nhập đầy đủ đáp án câu 2");
                    if (!cauhoi3.equals("")&&cau3.equals("")) throw new Exception("Hãy nhập đầy đủ đáp án câu 3");
                    if (!cauhoi4.equals("")&&cau4.equals("")) throw new Exception("Hãy nhập đầy đủ đáp án câu 4");
                    if (!cauhoi5.equals("")&&cau5.equals("")) throw new Exception("Hãy nhập đầy đủ đáp án câu 5");
                }
                catch(Exception ex) {
                    Toast.makeText(list_danhsachtuluan.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                int count = 0;
                if (!cauhoi1.equals("")&&!cau1.equals("")) count++;
                if (!cauhoi2.equals("")&&!cau2.equals("")) count++;
                if (!cauhoi3.equals("")&&!cau3.equals("")) count++;
                if (!cauhoi4.equals("")&&!cau4.equals("")) count++;
                if (!cauhoi5.equals("")&&!cau5.equals("")) count++;

                if (count == 5) {
                    addQuestionToDB(made, makithi,cauhoi1, cau1,1,username);
                    addQuestionToDB(made, makithi,cauhoi2, cau2,2,username);
                    addQuestionToDB(made, makithi,cauhoi3, cau3,3,username);
                    addQuestionToDB(made, makithi,cauhoi4, cau4,4,username);
                    addQuestionToDB(made, makithi,cauhoi5, cau5,5,username);
                    finish();
                } else {
                    Toast.makeText(list_danhsachtuluan.this, "Bạn chưa nhập đủ 5 câu!", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }

    private void addQuestionToDB(String made,int makithi, String noidungcauhoi,
                             String dapan, int tencauhoi, String username){
        ContentValues da_cau = new ContentValues();
        da_cau.put("made",made);
        da_cau.put("makithi",makithi);
        da_cau.put("dapan",dapan);
        da_cau.put("kieucauhoi",2);
        da_cau.put("tencauhoi",tencauhoi);
        da_cau.put("ndcauhoi", noidungcauhoi);
        da_cau.put("username",username);

        Cursor c = db.mydatabase.rawQuery("select made from cauhoi where makithi = " + makithi +" and made = '"+
                made+"' and kieucauhoi = 2 and tencauhoi = "+tencauhoi, null);
        Boolean status  = true;
        if(c.getCount() == 0){
            if(db.mydatabase.insert("cauhoi",null,da_cau)==-1)
            {
                Log.i("TAG", "addQuestionToDB: ------------------999999999999999999");
                status = false;
            }
        }
        else
        {
            int row = db.mydatabase.update("cauhoi",da_cau,
                    "made = '" + made + "' and makithi = " + makithi + " and kieucauhoi = 2 and username = '" + username + "' and tencauhoi = "+tencauhoi,
                    null);
            if(row==0)
            {
                status = false;
            }
        }
        if(status)
            Toast.makeText(this, "Thêm thành công câu "+tencauhoi, Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this, "Thêm thất bại câu "+tencauhoi, Toast.LENGTH_SHORT).show();
        }

    }

    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}