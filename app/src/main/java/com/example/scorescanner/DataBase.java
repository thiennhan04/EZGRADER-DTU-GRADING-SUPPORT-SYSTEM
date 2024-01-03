package com.example.scorescanner;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DataBase extends AppCompatActivity {

    public SQLiteDatabase mydatabase = null;

    public DataBase(AppCompatActivity acti) {
        try {

            DBHelper dbHelper = new DBHelper(acti);
            mydatabase = dbHelper.getReadableDatabase();
        }catch (Exception ex){
            Log.println(Log.DEBUG,"creat database","Fail "+ex.getMessage());
        }
    }
}
