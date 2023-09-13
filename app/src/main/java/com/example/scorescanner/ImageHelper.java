package com.example.scorescanner;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

public class ImageHelper {

    private static String getRandomString() {
        Date now = new Date();
        String tmp = now.getHours()+"-"+now.getMinutes()+"_"+(now.getDate()+1)+"-"+(now.getMonth()+1)+"-"+(now.getYear()+1900)+"-" + now.getTime();
        return tmp;
    }

    public static String saveImage(Bitmap bitmapImage){
        try {
            String directoryPath = Environment.getExternalStorageDirectory() + "/DCIM/ScoreScanner/";

            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            String fileName = getRandomString()+".jpg";
            fileName = "bb.jpg";
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
            return null;
        }
    }

    public static class ProcessImage {
        public Mat mat = new Mat();

        public String MADE;
        public String SBD;

        public ArrayList<MatOfPoint> cnts = new ArrayList<>();

        public boolean getMadeAndSBD(Bitmap input) {
            try {
                Utils.bitmapToMat(input,mat);
                Imgproc.cvtColor(mat, mat, Imgproc.COLOR_RGBA2RGB);

                // Lưu ý: ảnh ở đây đang ở dạng RGB
                Imgproc.circle(mat,new Point(200,200),15, new Scalar(0,255,0),2);

                return true;
            }
            catch(Exception ex) {
                return false;
            }
        }
    }
}
