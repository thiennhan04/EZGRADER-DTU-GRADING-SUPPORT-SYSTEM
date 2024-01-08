package com.example.scorescanner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ssdb2";
    private static final int DATABASE_VERSION = 1;
    public DBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // CAU HOI TABLE
    private static final String CREATE_CAUHOI = "CREATE TABLE \"cauhoi\" (\n" +
            "\t\"made\"\tTEXT,\n" +
            "\t\"makithi\"\tINTEGER,\n" +
            "\t\"dapan\"\tTEXT,\n" +
            "\t\"kieucauhoi\"\tINTEGER DEFAULT 1,\n" +
            "\t\"dapan2\"\tTEXT,\n" +
            "\t\"dapan3\"\tTEXT,\n" +
            "\t\"dapan4\"\tTEXT,\n" +
            "\t\"dapan5\"\tTEXT,\n" +
            "\t\"tencauhoi\"\tINTEGER,\n" +
            "\t\"username\"\tTEXT,\n" +
            "\t\"ndcauhoi\"\tTEXT\n" +
            ");";

    // DIEM TABLE
    private static final String CREATE_DIEM = "CREATE TABLE \"diem\" (\n" +
            "\t\"makithi\"\tINTEGER,\n" +
            "\t\"diemso\"\tREAL DEFAULT 0,\n" +
            "\t\"hinhanh\"\tTEXT,\n" +
            "\t\"masv\"\tTEXT,\n" +
            "\t\"tensv\"\tTEXT,\n" +
            "\t\"lop\"\tTEXT,\n" +
            "\t\"loaicauhoi\"\tINTEGER DEFAULT 1\n" +
            ");";

    // KITHI TABLE
    private static final String CREATE_KITHI = "CREATE TABLE \"kithi\" (\n" +
            "\t\"makithi\"\tINTEGER PRIMARY KEY AUTOINCREMENT,\n" +
            "\t\"tenkithi\"\tTEXT,\n" +
            "\t\"username\"\tTEXT,\n" +
            "\t\"socau\"\tINTEGER,\n" +
            "\t\"hediem\"\tINTEGER,\n" +
            "\t\"loaiphieu\"\tTEXT,\n" +
            "\t\"kieukithi\"\tINTEGER DEFAULT 1\n" +
            ");";


    // MADE TABLE
    private static final String CREATE_MADE = "CREATE TABLE \"made\" (\n" +
            "\t\"makithi\"\tINTEGER,\n" +
            "\t\"made\"\tTEXT,\n" +
            "\t\"socauhoi\"\tINTEGER\n" +
            ");";

    // USER TABLE
    private static final String CREATE_USER = "CREATE TABLE \"user\" (\n" +
            "\t\"username\"\tTEXT NOT NULL,\n" +
            "\t\"password\"\tTEXT NOT NULL,\n" +
            "\tPRIMARY KEY(\"username\")\n" +
            ");";

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try {
            if (!isTableExists(sqLiteDatabase, "user")) {
                sqLiteDatabase.execSQL(CREATE_USER);
            }

            if (!isTableExists(sqLiteDatabase, "made")) {
                sqLiteDatabase.execSQL(CREATE_MADE);
                Log.i("TAG", "onCreate: check 1");
            }

            if (!isTableExists(sqLiteDatabase, "kithi")) {
                sqLiteDatabase.execSQL(CREATE_KITHI);
                Log.i("TAG", "onCreate: check 2");
            }

            if (!isTableExists(sqLiteDatabase, "diem")) {
                sqLiteDatabase.execSQL(CREATE_DIEM);
                Log.i("TAG", "onCreate: check 3");
            }

            if (!isTableExists(sqLiteDatabase, "cauhoi")) {
                sqLiteDatabase.execSQL(CREATE_CAUHOI);
                Log.i("TAG", "onCreate: check 4");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "'";
        Cursor cursor = db.rawQuery(query, null);
        boolean tableExists = cursor.getCount() > 0;
        cursor.close();
        return tableExists;
    }

}
