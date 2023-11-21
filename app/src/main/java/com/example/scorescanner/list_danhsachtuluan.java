package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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
    String makithi = "";
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
        makithi = intent.getStringExtra("makithi");
        database = openOrCreateDatabase("ssdb2.db", MODE_PRIVATE, null);
        back_btnds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list_danhsachtuluan.this.finish();
            }
        });
        btn_save_list_tl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String made = edtmade.getText().toString();

                String cauhoi1 = edtcauhoi1.getText().toString();
                String cauhoi2 = edtcauhoi2.getText().toString();
                String cauhoi3 = edtcauhoi3.getText().toString();
                String cauhoi4 = edtcauhoi4.getText().toString();
                String cauhoi5 = edtcauhoi5.getText().toString();

                String cau1 = edttuluan1.getText().toString();
                String cau2 = edttuluan2.getText().toString();
                String cau3 = edttuluan3.getText().toString();
                String cau4 = edttuluan4.getText().toString();
                String cau5 = edttuluan5.getText().toString();
//

//                //luu cau 1 vào db
                addQuestionToDB(made, makithi,cauhoi1, cau1,1,username);
                addQuestionToDB(made, makithi,cauhoi2, cau2,2,username);
                addQuestionToDB(made, makithi,cauhoi3, cau3,3,username);
                addQuestionToDB(made, makithi,cauhoi4, cau4,4,username);
                addQuestionToDB(made, makithi,cauhoi5, cau5,5,username);
            }
        });
    }
    private void addQuestionToDB(String made,String makithi, String noidungcauhoi,
                                 String dapan, int tencauhoi, String username){
        ContentValues da_cau = new ContentValues();
        da_cau.put("made",made);
        da_cau.put("makithi",makithi);
        da_cau.put("dapan",dapan);
        da_cau.put("kieucauhoi",2);
        da_cau.put("tencauhoi",tencauhoi);
        da_cau.put("ndcauhoi", noidungcauhoi);
        da_cau.put("username",username);


//        System.out.println("them cau hoi " + made + " " + noidungcauhoi + " " + dapan + "  ");
        //check da ton tai
        Cursor c = db.mydatabase.rawQuery("select made from cauhoi where makithi = " + makithi +" and made = '"+
                made+"' and kieucauhoi = 2", null);
        Boolean status  = true;
        if(c.getCount() == 0){
            //neu chua ton tai thi them vao db,
            if(db.mydatabase.insert("cauhoi",null,da_cau)==-1)
            {
                status = false;
            }

        }
        else
        {
            int row = db.mydatabase.update("cauhoi",da_cau,
                    "made = ? and makithi = ? and kieucauhoi = 2 and username = ?",
                        new String[]{made,makithi, username});
            if(row==0)
            {
                status = false;
            }
        }
        if(status)
            Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show();
        else{
            Toast.makeText(this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
        }

    }
    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
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
//            if (!f.exists())
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