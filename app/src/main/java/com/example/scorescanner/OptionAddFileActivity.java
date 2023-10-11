package com.example.scorescanner;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

public class OptionAddFileActivity extends AppCompatActivity {

    private static final int requestCode = 1;
    private static final String TAG = "Option Add File";
    private static DataBase db = null;
    private String makithi;

    public static DataBase getDb() {
        return db;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_02_option_add_file);
        db = new DataBase(this);

        findViewById(R.id.backbtn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OptionAddFileActivity.this.finish();
            }
        });

        findViewById(R.id.add_file_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("*/*");
                startActivityForResult(intent, requestCode);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Context context = getApplicationContext();
        if (this.requestCode == requestCode && resultCode == Activity.RESULT_OK) {
            if (data == null)
                return;
            Uri uri = data.getData();
            if (readFileExcel(context, uri))
                Toast.makeText(context, "Đọc và thêm thành công!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "File excel sai định dạng!", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean readFile(Context context, Uri uri) {
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream input = contentResolver.openInputStream(uri);
            Workbook workbook = new XSSFWorkbook(input);
            Sheet sheet = workbook.getSheetAt(0);
            ArrayList<String> list = new ArrayList<>();
            boolean status = true;
            for (int i = 0; i < sheet.getPhysicalNumberOfRows(); i++) {
                String s = "";
                boolean ishavestring = false;
                for (int j = 0; j < sheet.getRow(i).getPhysicalNumberOfCells(); j++) {
                    Cell cell = sheet.getRow(i).getCell(j);
                    String str = cell.toString();
                    if (!str.isEmpty()) {
                        ishavestring = true;
                        s += str + "-";
                    }
                }
                list.add(s);
                if (!ishavestring) break;
            }
            workbook.close();
            if (db.mydatabase == null) {
                Log.println(Log.DEBUG, "read excel", "----- database = null -----");
                return false;
            }
            list.remove(0);
            for (String line : list) {
                String strmade = line.split("-")[0];
                Cursor c = db.mydatabase.rawQuery("select made from cauhoi where makithi = " + makithi + " and made = '" +
                        strmade + "'", null);
                String dataupdate = String.join("", line.substring(strmade.length()).split("-"));
                ContentValues values = new ContentValues();
                values.put("dapan", dataupdate);
                if (c.getCount() == 0) {
                    values.put("made", strmade);
                    values.put("makithi", makithi);
                    if (db.mydatabase.insert("cauhoi", null, values) == -1) {
                        status = false;
                    }
                } else {
                    int row = db.mydatabase.update("cauhoi", values, "made = ? and makithi = ?",
                            new String[]{strmade, makithi});
                    if (row == 0) {
                        Log.println(Log.DEBUG, "read excel", "update NOT OK ");
                        status = false;
                    } else {
                        Log.println(Log.DEBUG, "read excel", "update OK ");
                    }
                }
            }
            return status;
        } catch (Exception e) {
            Log.println(Log.DEBUG, "excel ", "--- " + e.getMessage() + " ---");
        }
        return false;
    }

    private boolean readFileExcel(Context context, Uri uri) {
        ArrayList<String> data = new ArrayList<>();
        try {
            ContentResolver contentResolver = context.getContentResolver();
            InputStream input = contentResolver.openInputStream(uri);
            Workbook workbook = new XSSFWorkbook(input);
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String made_daan = "";
                for (Row row : sheet) {
                    Cell cell = row.getCell(1);
                    if (cell != null) {
                        made_daan += cell.toString();
                    }
                }
                data.add(made_daan);
            }
            workbook.close();

            if (db.mydatabase == null) {
                return false;
            }
            boolean status = true;
            if (!data.isEmpty()) {
                for (String dt : data) {
                    String made = dt.substring(0, 3);
                    String dapan = dt.substring(4);
                    Cursor c = db.mydatabase.rawQuery("select made from cauhoi where makithi = " + makithi
                            + " and made = '" + made + "'", null);
                    ContentValues values = new ContentValues();
                    values.put("dapan", dapan);
                    if (c.getCount() == 0) {
                        values.put("made", made);
                        values.put("makithi", makithi);
                        if (db.mydatabase.insert("cauhoi", null, values) == -1) {
                            status = false;
                        }
                    } else {
                        int row = db.mydatabase.update("cauhoi", values, "made = ? and makithi = ?",
                                new String[]{made, makithi});
                        if (row == 0) {
                            Log.println(Log.DEBUG, "read excel", "update NOT OK ");
                            status = false;
                        } else {
                            Log.println(Log.DEBUG, "read excel", "update OK ");
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
}