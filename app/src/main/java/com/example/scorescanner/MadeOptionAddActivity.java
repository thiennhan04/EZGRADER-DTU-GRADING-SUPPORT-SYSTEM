package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MadeOptionAddActivity extends AppCompatActivity {

    TextView txtmade;
    Button addFileBtn,addHandBtn;
    ImageButton backbtn;

    String DB_PATH_SUFFIX = "/databases/";
    SQLiteDatabase database=null;
    String DATABASE_NAME="ssdb.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_made_option_add);
        backbtn = findViewById(R.id.backbtn);
        txtmade = findViewById(R.id.txtmade);
        addFileBtn = findViewById(R.id.dapanbtn);
        addHandBtn = findViewById(R.id.chambaibtn);
        Intent intent = getIntent();
        String makithi = intent.getStringExtra("kithi");
        String made = intent.getStringExtra("made");
        txtmade.setText("Mã đề "+made);

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        addFileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,requestCode);
            }
        });

        addHandBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    int requestCode = 1;
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        Context context = getApplicationContext();
        if(this.requestCode == requestCode && resultCode == Activity.RESULT_OK)
        {
            if(data==null)
                return;
            Uri uri = data.getData();
            readFile(uri);
            Toast.makeText(context, uri.getPath(), Toast.LENGTH_SHORT).show();
        }
    }

    public void readFile(Uri uri)
    {
//        File file = new File(uri.getPath());
//        FileInputStream inputStream = new FileInputStream(file);
//        Workbook workbook = new XSSFWorkbook(inputStream);
//        Sheet sheet = workbook.getSheetAt(0);
//
//        Iterator<Row> rowIterator = sheet.iterator();
//        while (rowIterator.hasNext()) {
//            Row row = rowIterator.next();
//            Iterator<Cell> cellIterator = row.cellIterator();
//            while (cellIterator.hasNext()) {
//                Cell cell = cellIterator.next();
//                // Xử lý dữ liệu trong ô tại đây
//            }
//        }
//        workbook.close();
    }
}