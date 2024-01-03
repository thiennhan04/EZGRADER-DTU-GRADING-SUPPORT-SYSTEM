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
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.stream.IntStream;

public class XuatFile extends AppCompatActivity {

    ImageButton back;
    Button dslop, exportnow;
    int requestCode = 1;

    Context context;
    Uri uri;
    DataBase db;
//    String makithi;
    int makithi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xuat_file);

        db = new DataBase(this);
        Intent intent = getIntent();
        makithi = intent.getIntExtra("makithi", -1);

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
                    if (exportFile())
                        Toast.makeText(XuatFile.this, "Xuất file thành công", Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(XuatFile.this, "Xuất file không thành công", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "Thêm thành công", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(context, "Thêm không thành công", Toast.LENGTH_SHORT).show();
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

                    Cursor c = db.mydatabase.rawQuery("SELECT * FROM diem WHERE makithi = " + makithi +" and masv = '" + sbd + "'",
                            null);
                    ContentValues values = new ContentValues();
                    values.put("masv", sbd);
                    values.put("tensv", name);
                    values.put("makithi", makithi);
                    if (c.getCount() == 0) {
                        if (db.mydatabase.insert("diem", null, values) == -1) {
                            status = false;
                        }
                    } else {
                        if (db.mydatabase.update("diem", values, "makithi = " + makithi + " and masv = '" + sbd + "'",
                                null) == 0) {
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

    public boolean exportFile() {
        try {
            Boolean status = true;

            String coutSql = "SELECT count(*) FROM diem WHERE makithi = " + makithi;
            String cSql = "SELECT count(*) FROM diem WHERE makithi = " + makithi + " and hinhanh is null";

            Cursor cout = db.mydatabase.rawQuery(coutSql, null);
            Cursor c = db.mydatabase.rawQuery(cSql, null);

            c.moveToFirst();
            cout.moveToFirst();

            if (c.getInt(0) == cout.getInt(0)) {
                Toast.makeText(XuatFile.this, "Bạn chưa có chấm bài", Toast.LENGTH_SHORT).show();
                status = false;
            } else {
                status = true;

                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Bang Diem");
                Object[][] title = {
                        {"SBD", "Họ", "Tên", "Điểm"},
                };

                Row titleRow = sheet.createRow(0);
                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = workbook.createFont();
                titleFont.setBold(true);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);

                int titleColumnCount = 0;
                for (Object field : title[0]) {
                    Cell titleCell = titleRow.createCell(titleColumnCount++);
                    titleCell.setCellValue((String) field);
                    titleCell.setCellStyle(titleStyle);
                }

                Cursor getData = db.mydatabase.rawQuery("select masv, tensv, diemso, hinhanh from diem where makithi = " + makithi + " and hinhanh is not null and loaicauhoi = 1",
                        null);

                ArrayList<Object[]> arr = new ArrayList<>();

                getData.moveToFirst();
                while (!getData.isAfterLast()) {
                    String hovaten = getData.getString(1);
                    String[] hoten;
                    String ho = "";
                    String ten = "";
                    if (hovaten != null) {
                        hoten = hovaten.split(" ");
                        for (int i = 0; i < hoten.length - 1; i++) {
                            ho += hoten[i] + " ";
                        }
                        ho = ho.trim();
                        ten = hoten[hoten.length - 1];
                    }
                    double diem = getData.getDouble(1);
                    Cursor getData2 = db.mydatabase.rawQuery("select masv, diemso from diem where makithi = " + makithi + " and hinhanh is not null and loaicauhoi = 2 and masv = '" + getData.getString(0) + "'",
                            null);
                    getData2.moveToFirst();
                    double diemTl = 0;
                    while (!getData2.isAfterLast()) {
                        diemTl = getData2.getDouble(1);
                        getData2.moveToNext();
                    }
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                    decimalFormat.applyPattern("#.##");

                    double tongDiem = diem + diemTl;
                    String formattedTongDiem = decimalFormat.format(tongDiem);
                    arr.add(new Object[]{getData.getString(0), ho, ten, formattedTongDiem});
                    getData.moveToNext();
                }

                int rowCount = 1;

                for (Object[] rowData : arr) {
                    Row rowdt = sheet.createRow(rowCount++);
                    int columnCountdt = 0;
                    for (Object field : rowData) {
                        Cell cell = rowdt.createCell(columnCountdt++);
                        if (field instanceof String) {
                            cell.setCellValue((String) field);
                        } else if (field instanceof Double) {
                            cell.setCellValue((Double) field);
                        }
                    }
                }

                int[] columnWidths = {2000, 4500, 1700, 2600};
                for (int i = 0; i < columnWidths.length; i++) {
                    sheet.setColumnWidth(i, columnWidths[i]);
                }

                File documentsDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/EzGrader/");
                if (!documentsDirectory.exists()) {
                    documentsDirectory.mkdirs();
                }
                String name = "/BANG-DIEM1" + makithi + ".xlsx";
                try (FileOutputStream outputStream = new FileOutputStream(documentsDirectory.getPath() + name)) {
                    workbook.write(outputStream);
                }
            }

            c.close();
            cout.close();

            return status;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }


    public boolean checkAdded() {
        Cursor c = db.mydatabase.rawQuery("select * from diem where makithi= " + makithi + " and length(tensv) > 0", null);
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

    public boolean exportNoFile() {
        try {
            boolean status = true;

            String countSql = "SELECT count(*) FROM diem WHERE makithi = " + makithi;
            String cSql = "SELECT count(*) FROM diem WHERE makithi = " + makithi + " and hinhanh is null";

            Cursor count = db.mydatabase.rawQuery(countSql, null);
            Cursor c = db.mydatabase.rawQuery(cSql, null);

            c.moveToFirst();
            count.moveToFirst();

            if (c.getInt(0) == count.getInt(0)) {
                Toast.makeText(XuatFile.this, "Bạn chưa có chấm bài", Toast.LENGTH_SHORT).show();
                status = false;
            } else {
                XSSFWorkbook workbook = new XSSFWorkbook();
                XSSFSheet sheet = workbook.createSheet("Bang Diem");
                Object[][] title = {
                        {"SBD", "Điểm"},
                };

                Row titleRow = sheet.createRow(0);
                CellStyle titleStyle = workbook.createCellStyle();
                Font titleFont = workbook.createFont();
                titleFont.setBold(true);
                titleStyle.setFont(titleFont);
                titleStyle.setAlignment(HorizontalAlignment.CENTER);

                int titleColumnCount = 0;
                for (Object field : title[0]) {
                    Cell titleCell = titleRow.createCell(titleColumnCount++);
                    titleCell.setCellValue((String) field);
                    titleCell.setCellStyle(titleStyle);
                }

                Cursor getData = db.mydatabase.rawQuery("select masv, diemso from diem where makithi = " + makithi + " and hinhanh is not null and loaicauhoi = 1",
                        null);

                ArrayList<Object[]> arr = new ArrayList<>();

                getData.moveToFirst();
                while (!getData.isAfterLast()) {
                    double diem = getData.getDouble(1);
                    Cursor getData2 = db.mydatabase.rawQuery("select masv, diemso from diem where makithi = " + makithi + " and hinhanh is not null and loaicauhoi = 2 and masv = '" + getData.getString(0) + "'",
                            null);
                    getData2.moveToFirst();
                    double diemTl = 0;
                    while (!getData2.isAfterLast()) {
                        diemTl = getData2.getDouble(1);
                        getData2.moveToNext();
                    }
                    DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(Locale.US);
                    decimalFormat.applyPattern("#.##");

                    double tongDiem = diem + diemTl;
                    String formattedTongDiem = decimalFormat.format(tongDiem);
                    arr.add(new Object[]{getData.getString(0), formattedTongDiem});
                    getData.moveToNext();
                }

                int rowCount = 1;
                for (Object[] rowData : arr) {
                    Row rowdt = sheet.createRow(rowCount++);
                    int columnCountdt = 0;
                    for (Object field : rowData) {
                        Cell cell = rowdt.createCell(columnCountdt++);
                        if (field instanceof String) {
                            cell.setCellValue((String) field);
                        } else if (field instanceof Double) {
                            cell.setCellValue((Double) field);
                        }
                    }
                }

                int[] columnWidths = {2000, 2600};
                for (int i = 0; i < columnWidths.length; i++) {
                    sheet.setColumnWidth(i, columnWidths[i]);
                }

                File documentsDirectory = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/EzGrader/");
                if (!documentsDirectory.exists()) {
                    documentsDirectory.mkdirs();
                }
                String name = "/BANG-DIEM1_" + makithi + "_(Không Danh Sách)"+ ".xlsx";
                try (FileOutputStream outputStream = new FileOutputStream(documentsDirectory.getPath() + name)) {
                    workbook.write(outputStream);
                }
            }

            c.close();
            count.close();

            return status;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
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
                .setNeutralButton("Xuất Điểm Ngay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (exportNoFile())
                            Toast.makeText(XuatFile.this, "Xuất file thành công", Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(XuatFile.this, "Xuất file không thành công", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }
}