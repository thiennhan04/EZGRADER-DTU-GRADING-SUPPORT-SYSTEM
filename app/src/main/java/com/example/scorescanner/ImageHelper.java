package com.example.scorescanner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageHelper {
    public static String getRandomString() {
        Date now = new Date();
        String tmp = now.getHours()+"-"+now.getMinutes()+"_"+(now.getDate()+1)+"-"+(now.getMonth()+1)+"-"+(now.getYear()+1900)+"-" + now.getTime();
        return tmp;
    }

    public static String saveImage(Bitmap bitmapImage) {
        String fileName = getRandomString()+".jpg";
        return saveImage(bitmapImage, null,fileName);
    }

    public static String saveImage(Bitmap bitmapImage, String directoryPath, String fileName){
        try {
            if(directoryPath == null){
                directoryPath = Environment.getExternalStorageDirectory() + "/DCIM/ScoreScanner/";
            }

            if(directoryPath == null){
                fileName = getRandomString()+".jpg";
            }

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, fileName);

            try {
                FileOutputStream fos = new FileOutputStream(file);
                bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.flush();
                fos.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return fileName;
        }
        catch(Exception ex) {
            return null;
        }
    }

    public static Bitmap loadImage(String imageName){
        try {
            String imagePath = Environment.getExternalStorageDirectory() + "/DCIM/ScoreScanner/" + imageName;
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            return bitmap;
        }
        catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }
}
