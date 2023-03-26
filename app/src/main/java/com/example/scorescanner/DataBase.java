package com.example.scorescanner;

import static android.provider.MediaStore.ACTION_IMAGE_CAPTURE;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

    private String DATABASE_NAME = "ssdb2.db";
    private String DB_PATH_SUFFIX = "/databases/";
    public SQLiteDatabase mydatabase = null;
    private AppCompatActivity activity;

    public DataBase(AppCompatActivity acti) {
        try {
            activity = acti;
            File dbFile = activity.getDatabasePath(DATABASE_NAME);
            if (!dbFile.exists()) {
                try {
                    CopyDataBaseFromAsset();
                    Log.println(Log.DEBUG, "===DataBase class===", "DataBase-processCopy-Load OK");
                } catch (Exception e) {
                    Log.println(Log.DEBUG, "===DataBase class===", "DataBase-processCopy-" + e.getMessage());
                }
            }
            else{
                mydatabase = activity.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
                Log.println(Log.DEBUG,"===DataBase class===", "DataBase-Load OK");
            }
        }catch (Exception ex){
            Log.println(Log.DEBUG,"creat database","Fail "+ex.getMessage());
        }
    }
    private String getDatabasePath() {
        return activity.getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {
        try
        {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists())
                f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
