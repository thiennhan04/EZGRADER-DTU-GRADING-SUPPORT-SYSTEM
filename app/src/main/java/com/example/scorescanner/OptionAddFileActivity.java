package com.example.scorescanner;

import android.annotation.SuppressLint;
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
import android.webkit.MimeTypeMap;
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

    private String username;

    public static DataBase getDb() {
        return db;
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_02_option_add_file);
        db = new DataBase(this);
        Intent intent = getIntent();
        makithi = intent.getStringExtra("makithi");
        username = intent.getStringExtra("username");
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

        findViewById(R.id.addHand).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OptionAddFileActivity.this, AddAnswerHand.class);
                intent.putExtra("username", username);
                intent.putExtra("makithi", makithi);
                startActivity(intent);
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
            String fileExtension = getFileExtension(uri);
            if (!fileExtension.equalsIgnoreCase("xlsx") && !fileExtension.equalsIgnoreCase("xls")) {
                Toast.makeText(context, "Hãy chọn file excel!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (readFileExcel(context, uri))
                Toast.makeText(context, "Đọc và thêm thành công!", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(context, "File excel sai định dạng!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uri) {
        Context context = getApplicationContext();
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
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
                        made_daan += cell.toString().replace(".0", "");
                        Log.i(TAG, "readFileExcel: ma de = " + made_daan);
                    }
                }
                data.add(made_daan);
            }
            workbook.close();

            Boolean status = true;
            if (!data.isEmpty()) {
                for (String dt : data) {
                    String made = dt.substring(0, 3);
                    String dapan = dt.substring(3);
                    Cursor getSoCau = db.mydatabase.rawQuery("Select socau from kithi where makithi = ?", new String[]{makithi});
                    getSoCau.moveToFirst();
                    int socau = 0;
                    while (getSoCau.isAfterLast() == false) {
                        socau = getSoCau.getInt(getSoCau.getColumnIndex("socau"));
                        getSoCau.moveToNext();
                    }
                    getSoCau.close();
                    if (dapan.length() < socau) {
                        Toast.makeText(context, "Đáp án không đủ! Kiểm tra lại!", Toast.LENGTH_SHORT).show();
                        status = false;
                        break;
                    } else if (dapan.length() > socau) {
                        Toast.makeText(context, "Thừa đáp án! Kiểm tra lại!", Toast.LENGTH_SHORT).show();
                        status = false;

                        Log.i(TAG, "readFileExcel: so cau = " + socau);
                        Log.i(TAG, "readFileExcel: dap an = " + dapan.length());
                        Log.i(TAG, "readFileExcel: dap an = " + dapan);
                        break;
                    }
                    Cursor c = db.mydatabase.rawQuery("SELECT made FROM cauhoi WHERE makithi = ? AND made = ?",
                            new String[]{makithi, made});
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
}