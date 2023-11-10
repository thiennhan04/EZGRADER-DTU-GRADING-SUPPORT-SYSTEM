package com.example.scorescanner;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.nio.charset.CoderResult;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Base64;

import android.companion.WifiDeviceFilter;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.os.Environment;
import android.os.HandlerThread;
import android.util.Log;

import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import kotlin.text.UStringsKt;

public class Methods extends AppCompatActivity {
    DataBase db = null;
    String username;
    String makithi;
    private static Bitmap imgSbdMade;
    private static Bitmap imgAnswer;
    private static Bitmap imgSbd;
    private static Bitmap imgMade;
    private static Bitmap imgLeftAnswer;
    private static Bitmap imgRightAnswer;
    private static ArrayList<Bitmap> listImgAnswer;
    private static String score = "";
    private static String TAG = "Checkkkkkkkkk";
    private static File file;
    private Context context;

    public Methods(Context context, String makithi, String username) {
        this.context = context;
        this.makithi = makithi;
        this.username = username;
    }

    private static void cutImage(Bitmap bitmap) {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            Mat tempSbdMade = mat.submat(new Rect(mat.width() * 3 / 4, 50,
                    mat.width() - (mat.width() * 3 / 4), mat.height() * 4 / 15));
            Mat tempAnswer = mat.submat(new Rect(0, mat.height() * 1 / 3, mat.width(), mat.height() * 2 / 3));
            imgSbdMade = Bitmap.createBitmap(tempSbdMade.width(), tempSbdMade.height(), Bitmap.Config.ARGB_8888);
            imgAnswer = Bitmap.createBitmap(tempAnswer.width(), tempAnswer.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tempSbdMade, imgSbdMade);
            Utils.matToBitmap(tempAnswer, imgAnswer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cutSbdMade() {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(imgSbdMade, mat);
            Mat sbd = mat.clone().submat(new Rect(25, 60, 190, mat.height() - 100));
            Mat made = mat.clone().submat(new Rect(mat.width() - 95, 60, 95, mat.height() - 100));
            imgSbd = Bitmap.createBitmap(sbd.width(), sbd.height(), Bitmap.Config.ARGB_8888);
            imgMade = Bitmap.createBitmap(made.width(), made.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(sbd, imgSbd);
            Utils.matToBitmap(made, imgMade);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cutAnswer22Pic() {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(imgAnswer, mat);
            Mat leftAnswer = mat.clone().submat(new Rect(80, 70, 620, mat.height() - 90));
            Mat rightAnswer = mat.clone().submat(new Rect(710, 70, 610, mat.height() - 90));
            imgLeftAnswer = Bitmap.createBitmap(leftAnswer.width(), leftAnswer.height(), Bitmap.Config.ARGB_8888);
            imgRightAnswer = Bitmap.createBitmap(rightAnswer.width(), rightAnswer.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(leftAnswer, imgLeftAnswer);
            Utils.matToBitmap(rightAnswer, imgRightAnswer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void cutAnswer210Pic() {
        try {
            Mat leftAnswerMat = new Mat();
            Mat rightAnswerMat = new Mat();
            Utils.bitmapToMat(imgLeftAnswer, leftAnswerMat);
            Utils.bitmapToMat(imgRightAnswer, rightAnswerMat);
            ArrayList<Bitmap> tempListBitmap = new ArrayList<>();
            Mat tempLeftMat = new Mat();
            Mat tempRightMat = new Mat();
            Bitmap tempLeftBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            int y = 5;
            for (int i = 0; i < 5; i++) {
                tempLeftMat = leftAnswerMat.submat(new Rect(125, y, 440, 260));
                tempLeftBitmap = Bitmap.createBitmap(tempLeftMat.width(), tempLeftMat.height(), Bitmap.Config.ARGB_8888);
                tempListBitmap.add(tempLeftBitmap);
                Utils.matToBitmap(tempLeftMat, tempLeftBitmap);
                y += 260;
            }
            Bitmap tempRightBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            y = 5;
            for (int i = 0; i < 5; i++) {
                tempRightMat = rightAnswerMat.submat(new Rect(180, y, 420, 260));
                tempRightBitmap = Bitmap.createBitmap(tempRightMat.width(), tempRightMat.height(), Bitmap.Config.ARGB_8888);
                tempListBitmap.add(tempRightBitmap);
                Utils.matToBitmap(tempRightMat, tempRightBitmap);
                y += 260;
            }
            listImgAnswer = tempListBitmap;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Point[][] splitImage(Bitmap bitmap, int rows, int cols) {
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Point[][] points = new Point[rows][cols];
        double width = mat.width() * 1.0 / cols;
        double height = mat.height() * 1.0 / rows;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                points[i][j] = new Point((int) (j * width + width / 2), (int) (i * height + height / 2));
            }
        }
        return points;
    }

    private static String findAns(Bitmap bitmap, int rows, int cols, int r) {
        Point[][] points = splitImage(bitmap, rows, cols);
        Mat mat = new Mat();
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2BGR);
        Mat imgGray = new Mat();
        Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.threshold(imgGray, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
        String ansStr = "";
        boolean columnTouched = false;
        double radius = mat.width() / r;
        for (int i = 0; i < cols; i++) {
            String str = "";
            ArrayList<Point> pst = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                Mat mark = new Mat(mat.size(), thresh.type(), new Scalar(0, 0, 0));
                Imgproc.circle(mark, points[j][i], (int) radius, new Scalar(255, 255, 255), -1);
                Core.bitwise_and(thresh, thresh, mark, mark);
                double count = Core.countNonZero(mark);
                if (count / (Math.PI * radius * radius) > 0.6) {
                    pst.add(points[j][i]);
                    if (!columnTouched) {
                        str = j + "";
                        columnTouched = true;
                    } else {
                        str = "#";
                    }
                }
            }
            if (pst.size() == 1) {
                Imgproc.circle(mat, pst.get(0), (int) radius, new Scalar(0, 255, 0), 3);
            }
            if (pst.size() > 1) {
                for (Point point : pst) {
                    Imgproc.circle(mat, point, (int) radius, new Scalar(255, 255, 0), 3);
                }
            }
            if (columnTouched) {
                ansStr += str;
                columnTouched = false;
            } else {
                ansStr += "#";
            }
        }
        Utils.matToBitmap(mat, bitmap);
        return ansStr;
    }

    private static String getSbd() {
        return findAns(imgSbd, 10, 6, 18);
    }

    private static String getMade() {
        return findAns(imgMade, 10, 3, 8);
    }

    private static String getAnswer(String list_answer, int hediem) {
        String ansStr = "";
        int quantity = 0;
        int ques = 0;
        for (int i = 0; i < listImgAnswer.size(); i++) {
            Point[][] points = splitImage(listImgAnswer.get(i), 5, 4);
            Mat mat = new Mat();
            Utils.bitmapToMat(listImgAnswer.get(i), mat);
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2BGR);
            Mat imgGray = new Mat();
            Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_BGR2GRAY);
            Mat thresh = new Mat();
            Imgproc.threshold(imgGray, thresh, 0, 255, Imgproc.THRESH_BINARY_INV | Imgproc.THRESH_OTSU);
            String ans5 = "";
            double radius = mat.width() / 25;
            for (int j = 0; j < 5; j++) {
                if (++quantity > list_answer.length()) break;
                ArrayList<Point> pstChoose = new ArrayList<>();
                String ans = "";
                Point trueAns = points[j][list_answer.charAt(quantity - 1) - 65];
                for (int k = 0; k < 4; k++) {
                    Mat mark = new Mat(mat.size(), thresh.type(), new Scalar(0, 0, 0));
                    Imgproc.circle(mark, points[j][k], (int) radius, new Scalar(255, 255, 255), -1);
                    Core.bitwise_and(thresh, thresh, mark, mark);
                    double count = Core.countNonZero(mark);
                    if ((count / (Math.PI * radius * radius) > 0.6)) {
                        ans += (char) (k + 65);
                        pstChoose.add(points[j][k]);
                    }
                }
                if (pstChoose.size() > 1) {
                    ans = quantity + "#";
                }
                ans5 += ans;
                if (pstChoose.size() == 0) {
                    Imgproc.circle(mat, trueAns, (int) radius, new Scalar(255, 255, 0), 4);
                } else if (pstChoose.size() == 1 && pstChoose.get(0) == trueAns) {
                    ques++;
                    Imgproc.circle(mat, pstChoose.get(0), (int) radius, new Scalar(0, 255, 0), 4);
                } else if (pstChoose.size() == 1 && pstChoose.get(0) != trueAns) {
                    Imgproc.circle(mat, pstChoose.get(0), (int) radius, new Scalar(255, 0, 0), 4);
                    Imgproc.circle(mat, trueAns, (int) radius, new Scalar(255, 255, 0), 4);
                } else if (pstChoose.size() > 1) {
                    for (Point point : pstChoose) {
                        Imgproc.circle(mat, point, (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    Imgproc.circle(mat, trueAns, (int) radius, new Scalar(255, 255, 0), 4);
                }
            }
            ansStr += ans5;
            Utils.matToBitmap(mat, listImgAnswer.get(i));
        }
        score = String.format("%.2f", (ques * 1.0 / list_answer.length() * hediem));
        return ansStr;
    }

    private static Bitmap recoverBitmap(Bitmap bitmap) {
        Bitmap finalBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawBitmap(imgSbd, bitmap.getWidth() * 3 / 4 + 25, 110, null);
        canvas.drawBitmap(imgMade, bitmap.getWidth() * 3 / 4 + 260, 110, null);
        int y = bitmap.getHeight() / 3 + 75;
        for (int i = 0; i < 5; i++) {
            Bitmap subBitmap = listImgAnswer.get(i);
            canvas.drawBitmap(subBitmap, 205, y, null);
            y += 260;
        }
        y = bitmap.getHeight() / 3 + 75;
        for (int i = 5; i < 10; i++) {
            Bitmap subBitmap = listImgAnswer.get(i);
            canvas.drawBitmap(subBitmap, bitmap.getWidth() - (bitmap.getWidth() - 710) + 180, y, null);
            y += 260;
        }
        Paint paint = new Paint();
        int x;
        if (score == "Không nhận diện được mã đề!" || score == "Mã đề không tồn tại!") {
            paint.setColor(Color.RED);
            paint.setTextSize(80);
            x = 100;
        } else {
            paint.setColor(Color.RED);
            paint.setTextSize(150);
            x = 700;
        }
        canvas.drawText(score, x, 600, paint);
        return finalBitmap;
    }

//create folder

    private Bitmap getDataFromDB(Bitmap bitmap) {
//<<<<<<< HEAD
        db = new DataBase((AppCompatActivity) context);
//=======
//        db = new DataBase(this);
//>>>>>>> 9b012978fb23c49e9359cb11015b7792e303624f
        if (db == null) {
            Log.d(TAG, "getDataFromDB: db is null");
            return bitmap;
        }
        try {
            Bitmap oldBm = bitmap;
            String made = getMade();
            String sbd = getSbd();
            int hediem = 0;
            String list_answer = "";
            if (made.contains("#")) {
                score = "Không nhận diện được mã đề!";
                Log.i(TAG, "getDataFromDB: " + score);
//                bitmap = imgMade;
                return recoverBitmap(bitmap);
            }
            Cursor c = db.mydatabase.rawQuery("select * from cauhoi where makithi = ? and made = ?",
                    new String[]{makithi, made});
            if (!c.moveToFirst()) {
                score = "Mã đề không tồn tại!";
                return recoverBitmap(bitmap);
            }
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list_answer = c.getString(c.getColumnIndex("dapan"));
                c.moveToNext();
            }
            c.close();
            Cursor c2 = db.mydatabase.rawQuery("select hediem from kithi where makithi = ?",
                    new String[]{makithi});
            c2.moveToFirst();
            while (!c2.isAfterLast()) {
                hediem = c2.getInt(0);
                break;
            }
            String list_select_ans = getAnswer(list_answer, hediem);
            Log.d(TAG, "getDataFromDB: answer ==== " + list_select_ans);

            //////

            // save to db
            String sql = "select masv, hinhanh from diem where makithi=" + makithi + " and masv='" + sbd + "'";
            Cursor isExist = db.mydatabase.rawQuery(sql, null);
            isExist.moveToFirst();

            File directory = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DCIM), "EzGrader/" + username + "/" +makithi);
            if (!directory.exists()) {
                directory.mkdirs();
            }
            String imguri = directory.getAbsolutePath() + "/" + sbd + ".jpg";


            Log.i(TAG, "getDataFromDB: "+isExist.getCount());
            Log.i(TAG, "getDataFromDB: answer ==== " + sql);

            String msg = "";

            if (isExist.getCount() > 0) {
                String newUri = isExist.getString(1);
                if(newUri != null && !newUri.isEmpty()) {
                    imguri = newUri;
                    Log.i(TAG, "getDataFromDB: uri null= =="+newUri);
                }
                ContentValues valuediem = new ContentValues();
                valuediem.put("makithi", makithi);
                valuediem.put("diemso", score);
                valuediem.put("masv", sbd);
                valuediem.put("hinhanh", imguri);
                Log.d(TAG, "getDataFromDB: Uriiiiii = " + imguri);
                if (db.mydatabase.update("diem", valuediem, "makithi = " + makithi + " and masv = '" + sbd + "'", null) == -1) {
                    msg = "Fail to insert record";
                } else {
                    msg = "Insert record sucess";
                }
            } else {
                ContentValues valuediem = new ContentValues();
                valuediem.put("makithi", makithi);
                valuediem.put("diemso", score);
                valuediem.put("masv", sbd);
                valuediem.put("hinhanh", imguri);
                if (db.mydatabase.insert("diem", null, valuediem) == -1) {
                    msg = "Fail to insert record";
                } else {
                    msg = "Insert record sucess";
                }
            }

            file = new File(imguri);
            Log.d(TAG, "getDataFromDB: status = " + msg);

            byte[] data = convertBitmapToByteArray(recoverBitmap(oldBm));
            save(data);
            return recoverBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, stream);
        return stream.toByteArray();

//        ByteBuffer buffer = ByteBuffer.allocate(bitmap.getByteCount());
//        bitmap.copyPixelsToBuffer(buffer);
//        return buffer.array();
    }

    private void save(byte[] bytes) throws IOException {
        OutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            outputStream.write(bytes);
        } finally {
            if (outputStream != null)
                outputStream.close();
        }
    }

    public Bitmap run(Bitmap bitmap) {
        try {
            cutImage(bitmap);
            cutSbdMade();
            cutAnswer22Pic();
            cutAnswer210Pic();
            return getDataFromDB(bitmap);
        } catch (Exception ex) {
            ex.printStackTrace();
            score = "";
            imgSbdMade = null;
            imgAnswer = null;
            imgSbd = null;
            imgMade = null;
            imgLeftAnswer = null;
            imgRightAnswer = null;
            listImgAnswer = null;
            return null;
        }
    }
}