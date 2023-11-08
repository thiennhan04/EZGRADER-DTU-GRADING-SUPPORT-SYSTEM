package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.InputStream;
import java.util.ArrayList;

public class XuatFile extends AppCompatActivity {

    ImageButton back;
    Button dslop, exportnow;
    int requestCode=1;

    Context context;
    Uri uri;
    DataBase db;
    String makithi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_file);

        db = new DataBase(this);
        Intent intent = getIntent();
        makithi = intent.getStringExtra("makithi");

        back = findViewById(R.id.back_btnds);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dslop = findViewById(R.id.dslop);
        dslop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent,requestCode);
            }
        });
        
        exportnow = findViewById(R.id.exportnow);
        exportnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(XuatFile.this, "export", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode,resultCode,data);
        context = getApplicationContext();
        if(this.requestCode == requestCode && resultCode == Activity.RESULT_OK)
        {
            if(data==null)
                return;
            uri = data.getData();
            String fileExtension = getFileExtension(uri);
            if (!fileExtension.equalsIgnoreCase("xlsx") && !fileExtension.equalsIgnoreCase("xls")) {
                Toast.makeText(context, "Hãy chọn file excel!", Toast.LENGTH_SHORT).show();
                return;
            }
            if(checkAdded()) {
                alert();
            }
            else {
                if (readFile())
                    Toast.makeText(context, "thêm thành công", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "thêm không thành công", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String getFileExtension(Uri uri) {
        Context context = getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public boolean readFile() {
        try {
            ArrayList<String> data = new ArrayList<>();
            ContentResolver contentResolver = context.getContentResolver();
            InputStream input = contentResolver.openInputStream(uri);
            Workbook workbook = new XSSFWorkbook(input);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                for (Row row : sheet) {
                    String res = "";
                    for(int j=0; j<50; j++) {
                        Cell cell = row.getCell(j);
                        if (cell != null) {
                            res += (cell.toString().trim().replace('\n',' ')) + " ";
                        }
                    }
                    Log.i("TAG", "readFile: "+res);
                }
            }
            workbook.close();
        }
        catch (Exception ex) {
            return false;
        }
        return true;
    }

    public boolean checkAdded() {
        Cursor c = db.mydatabase.rawQuery("select * from diem where makithi=? and length(tensv) > 0", new String[]{makithi});
        if(c.getCount()>0)
            return true;
        else
            return false;
    }

    public void alert() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Xác nhận")
                .setMessage("Bạn đã thêm danh sách trước đó, nếu tiếp tục sẽ thay thế danh sách trước đó và không thể hồi phục?")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (readFile())
                            Toast.makeText(context, "Thêm danh sách thành công", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(context, "Thêm danh sách không thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getApplicationContext(),"Bạn đã hủy thêm danh sách",Toast.LENGTH_LONG).show();
                    }
                })
                .show();
    }
}