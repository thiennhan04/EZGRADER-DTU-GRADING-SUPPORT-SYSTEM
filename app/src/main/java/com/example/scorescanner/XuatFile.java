package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
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
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class XuatFile extends AppCompatActivity {

    ImageButton back;
    Button dslop, exportnow;
    int requestCode = 1;

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
                startActivityForResult(intent, requestCode);
            }
        });

        exportnow = findViewById(R.id.exportnow);
        exportnow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkAdded()) {
                    if (exportFile(uri))
                        Toast.makeText(XuatFile.this, "Xuất file thành công", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(XuatFile.this, "Bạn chưa chấm bài! Hãy chấm bài", Toast.LENGTH_SHORT).show();
                } else {
                    alertExport();
                }
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        context = getApplicationContext();
        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            if (data == null)
                return;
            uri = data.getData();
            String fileExtension = getFileExtension(uri);
            if (!fileExtension.equalsIgnoreCase("xlsx") && !fileExtension.equalsIgnoreCase("xls")) {
                Toast.makeText(context, "Hãy chọn file excel!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkAdded()) {
                alertAdd();
            } else {
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
            if (workbook.getNumberOfSheets() != 1) {
                Toast.makeText(XuatFile.this, "File có nhiều hơn 1 sheet!", Toast.LENGTH_SHORT).show();
                return false;
            }
            Sheet sheet = workbook.getSheetAt(0);

            for (int i = 8; i < 50; i++) {
                String info = "";
                Cell cell2 = sheet.getRow(i).getCell(3);
                Cell cell3 = sheet.getRow(i).getCell(4);
                if (cell2 != null && cell2.toString().length() > 0 && cell3 != null && cell3.toString().length() > 0) {
                    info = (i - 7) + "|" + cell2 + " " + cell3;
                    data.add(info);
                    Log.d("TAG", "readFile: infooo = " + info);
                }
            }

            workbook.close();

            Boolean status = true;
            if (!data.isEmpty()) {
                for (String dt : data) {
                    String[] line = dt.split("\\|");
                    String sbd = line[0];
                    if (sbd.length() < 6) {
                        String paddedPart1 = String.format("%06d", Integer.parseInt(sbd));
                        sbd = paddedPart1;
                    }
                    String name = line[1];

                    Cursor c = db.mydatabase.rawQuery("SELECT * FROM diem WHERE makithi = ? and masv = ?",
                            new String[]{makithi, sbd});
                    ContentValues values = new ContentValues();
                    values.put("masv", sbd);
                    values.put("tensv", name);
                    values.put("makithi", makithi);
                    if (c.getCount() == 0) {
                        if (db.mydatabase.insert("diem", null, values) == -1) {
                            status = false;
                        }
                    } else {
                        if (db.mydatabase.update("diem", values, "makithi = ? and masv = ?",
                                new String[]{makithi,sbd}) == 0) {
                            status = false;
                        }
                    }

                }
            }
            return status;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean exportFile(Uri uri) {
        try {

            if (uri == null) {
                return false;
            }
            Boolean status = true;
            Cursor cout = db.mydatabase.rawQuery("SELECT count(*) FROM diem WHERE makithi = ?",
                    new String[]{makithi});
            Cursor c = db.mydatabase.rawQuery("SELECT hinhanh FROM diem WHERE makithi = ? and hinhanh is null",
                    new String[]{makithi});
            if (c.getCount() == cout.getInt(0)) {
                status = false;
            } else {
//                InputStream inputStream = context.getContentResolver().openInputStream(uri);
//                XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
//
//                XSSFSheet sheet = workbook.getSheetAt(0);
//                FileOutputStream fileOut = new FileOutputStream(new File(uri.getPath()));
//                workbook.write(fileOut);
//                fileOut.close();
//                workbook.close();
//                alertExport();
                Log.d("TAG", "exportFile: xuất file okeeee");
            }
            return status;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public boolean checkAdded() {
        Cursor c = db.mydatabase.rawQuery("select * from diem where makithi=? and length(tensv) > 0", new String[]{makithi});
        if (c.getCount() > 0)
            return true;
        else
            return false;
    }

    public void alertAdd() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
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
                        context = null;
                        uri = null;
                    }
                })
                .show();
    }

    public void alertExport() {
        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setTitle("Thông báo")
                .setMessage("Bạn cần thêm file danh sách trước khi xuất file!")
                .setPositiveButton("Xác nhận", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("*/*");
                        startActivityForResult(intent, requestCode);
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}