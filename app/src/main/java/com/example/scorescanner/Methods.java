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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.w3.x2000.x09.xmldsig.ObjectType;

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

import android.annotation.SuppressLint;
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
//    String makithi;
    int makithi;
    public static Bitmap main;
    public String made = "";
    public String sbd = "";
    private static Bitmap imgSbdMade;
    public static Bitmap imgAnswer;
    public static Bitmap imgSbd;
    public static Bitmap imgMade;
    public static Bitmap imgLeftAnswer;
    public static Bitmap imgRightAnswer;
    public static ArrayList<Bitmap> listImgAnswer;
    public static String score = "";
    public static String TAG = "Checkkkkkkkkk";
    public static File file;
    private Context context;
    private static Rect rectsbd;
    private static Rect rectmade;
    private static Rect rectsbdmade;
    private static Rect rectAns;
    private static Rect rectLeftAns;
    private static Rect rectRightAns;
    private static List<Point> listdrawans;
    public static int socaudung;
    public static int tongsocau;

//    public Methods(Context context, String makithi, String username) {
    public Methods(Context context, int makithi, String username) {
        this.context = context;
        this.makithi = makithi;
        this.username = username;
    }

    private static Rect findSquare(Mat mat, double xx, double yy, double ww, double hh) {
        int x = (int)xx;
        int y = (int)yy;
        int w = (int)ww;
        int h = (int)hh;
        Mat square = mat.submat(new Rect(x,y,w,h));
        Mat gray = new Mat();
        Imgproc.cvtColor(square, gray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(gray, thresh, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 201, 31);
//        Core.bitwise_not(thresh, thresh);
        Imgproc.rectangle(thresh,new Point(0,0),new Point(w-1,h-1),new Scalar(255,255,255),1);
        List<MatOfPoint> arr = new ArrayList();
        Imgproc.findContours(thresh,arr,new Mat(),Imgproc.RETR_CCOMP,Imgproc.CHAIN_APPROX_SIMPLE);
        Collections.sort(arr, new Comparator<MatOfPoint>() {
            @Override
            public int compare(MatOfPoint contour1, MatOfPoint contour2) {
                double area1 = Imgproc.contourArea(contour1);
                double area2 = Imgproc.contourArea(contour2);
                return Double.compare(area2, area1);
            }
        });
        if(arr.size()>=2)
            return Imgproc.boundingRect(arr.get(1));
        return Imgproc.boundingRect(arr.get(0));
    }

    private static void cutImage(Bitmap bitmap) {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(bitmap, mat);
            int type = mat.type();
            int width = mat.width();
            int height = mat.height();
            double px = width * 11 / 15.2;
            double py = height * 5 / 18.3;
            double pw = 0.045 * width;
            double ph = 0.045 * width;
            Rect rect = findSquare(mat,px,py,pw,ph);
            int x = rect.x;
            int y = rect.y;
            int w = rect.width;
            int h = rect.height;
            rectsbdmade = new Rect((int)(px+x+w),(int)((py+y)*0.1),(int)(width-px-x-w),(int)((py+y)*0.9+h));

            Mat matmain = new Mat();
            Utils.bitmapToMat(main, matmain);
            Imgproc.cvtColor(matmain, matmain, Imgproc.COLOR_BGRA2BGR);
            Imgproc.rectangle(matmain,new Point(px,py),new Point(px+pw-1,py+ph-1) ,new Scalar(255,255,255),2);
            List<MatOfPoint> list = new ArrayList<>();
            list.add(listPoly((int)px+x+w-1,(int)py+y+1,8));
            list.add(listPoly((int)px+x+w/2,(int)py+y+h/2,8));
            list.add(listPoly((int)px+x+1,(int)py+y+h-1,8));
            Imgproc.fillPoly(matmain,list,new Scalar(255,0,0));
            Utils.matToBitmap(matmain, main);

            Mat tempSbdMade = mat.submat(rectsbdmade);
            imgSbdMade = Bitmap.createBitmap(tempSbdMade.width(), tempSbdMade.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tempSbdMade, imgSbdMade);

            rectAns = new Rect(0, (int)(py+ph*2+1), mat.width(), mat.height()-(int)(py+ph*2+1));
            Mat tempAnswer = mat.submat(rectAns);
            imgAnswer = Bitmap.createBitmap(tempAnswer.width(), tempAnswer.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(tempAnswer, imgAnswer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static MatOfPoint listPoly(int cenx, int ceny, int size) {
        List<Point> points = new ArrayList<>();
        points.add(new Point(cenx, ceny-size));
        points.add(new Point(cenx+size, ceny));
        points.add(new Point(cenx, ceny+size));
        points.add(new Point(cenx-size, ceny));

        MatOfPoint matOfPoint = new MatOfPoint();
        matOfPoint.fromList(points);
        return matOfPoint;
    }

    private static void cutSbdMade() {
        try {
            Mat mat = new Mat();
            Utils.bitmapToMat(imgSbdMade, mat);
            Mat matmain = new Mat();
            Utils.bitmapToMat(main, matmain);
            Imgproc.cvtColor(matmain, matmain, Imgproc.COLOR_BGRA2BGR);

            int sbdW = mat.width(), sbdH = mat.height();
            double px1 = sbdW*0.6;
            double py1 = sbdH/2-sbdH*0.1/1.9;
            double pw1 = sbdW*0.1;
            double ph1 = sbdH*0.1;
            Rect rect = findSquare(mat,px1,py1,pw1,ph1);
            int x1 = rect.x;
            int y1 = rect.y;
            int w1 = rect.width;
            int h1 = rect.height;
            Imgproc.rectangle(matmain,new Point(rectsbdmade.x+px1,rectsbdmade.y+py1),new Point(rectsbdmade.x+px1+pw1-1,rectsbdmade.y+py1+ph1-1),new Scalar(255,255,255),2);
            List<MatOfPoint> list = new ArrayList<>();
            list.add(listPoly((int)px1+rectsbdmade.x+x1+w1/2,(int)py1+rectsbdmade.y+y1+h1/2,8));
            Imgproc.fillPoly(matmain,list,new Scalar(255,0,0));

            double px2 = sbdW*0.6;
            double py2 = sbdH*9/10-sbdH*0.1/1.9;
            double pw2 = sbdW*0.1;
            double ph2 = sbdH*0.1;
            Rect rect2 = findSquare(mat,px2,py2,pw2,ph2);
            int x2 = rect2.x;
            int y2 = rect2.y;
            int w2 = rect2.width;
            int h2 = rect2.height;
            Imgproc.rectangle(matmain,new Point(rectsbdmade.x+px2,rectsbdmade.y+py2),new Point(rectsbdmade.x+px2+pw2-1,rectsbdmade.y+py2+ph2-1),new Scalar(255,255,255),2);
            List<MatOfPoint> list2 = new ArrayList<>();
            list2.add(listPoly((int)px2+rectsbdmade.x+x2+w2/2,(int)py2+rectsbdmade.y+y2+h2/2,8));
            Imgproc.fillPoly(matmain,list2,new Scalar(255,0,0));

            double px3 = sbdW*0.6;
            double py3 = sbdH*1/10-sbdH*0.1/1.9;
            double pw3 = sbdW*0.1;
            double ph3 = sbdH*0.1;
            Rect rect3 = findSquare(mat,px3,py3,pw3,ph3);
            int x3 = rect3.x;
            int y3 = rect3.y;
            int w3 = rect3.width;
            int h3 = rect3.height;
            Imgproc.rectangle(matmain,new Point(rectsbdmade.x+px3,rectsbdmade.y+py3),new Point(rectsbdmade.x+px3+pw3-1,rectsbdmade.y+py3+ph3-1),new Scalar(255,255,255),2);
            List<MatOfPoint> list3 = new ArrayList<>();
            list3.add(listPoly((int)px3+rectsbdmade.x+x3+w3/2,(int)py3+rectsbdmade.y+y3+h3/2,8));
            Imgproc.fillPoly(matmain,list3,new Scalar(255,0,0));

            rectsbd = new Rect(0,(int)py3+y3+h3/2,(int)((px2+x2+w2/2+px3+x3+w3/2)/2-sbdH*0.04),(int)(py2+x2+h2/2-py3-x3-h3/2));
            Mat sbd = mat.clone().submat(rectsbd);
            rectmade = new Rect((int)((px2+x2+w2/2+px3+x3+w3/2)/2+sbdH*0.04),(int)(py3+y3+h3/2),(int)(mat.width()-(px2+x2+w2/2+px3+x3+w3/2)/2-sbdH*0.04),(int)(py2+y2+h2/2-py3-y3-h3/2));
            Mat made = mat.clone().submat(rectmade);
            imgSbd = Bitmap.createBitmap(sbd.width(), sbd.height(), Bitmap.Config.ARGB_8888);
            imgMade = Bitmap.createBitmap(made.width(), made.height(), Bitmap.Config.ARGB_8888);

            Utils.matToBitmap(matmain, main);
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
            rectLeftAns = new Rect(0, 0, mat.width()/2, mat.height());
            Mat leftAnswer = mat.clone().submat(rectLeftAns);
            rectRightAns = new Rect(mat.width()/2, 0, mat.width()/2, mat.height());
            Mat rightAnswer = mat.clone().submat(rectRightAns);
            imgLeftAnswer = Bitmap.createBitmap(leftAnswer.width(), leftAnswer.height(), Bitmap.Config.ARGB_8888);
            imgRightAnswer = Bitmap.createBitmap(rightAnswer.width(), rightAnswer.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(leftAnswer, imgLeftAnswer);
            Utils.matToBitmap(rightAnswer, imgRightAnswer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Point> renderListSquareAns() {
        List<Point> list = new ArrayList<>();
        Mat mat = new Mat();
        Utils.bitmapToMat(imgAnswer, mat);

        List<MatOfPoint> listPoly = new ArrayList<>();
        Mat matmain = new Mat();
        Utils.bitmapToMat(main, matmain);
        Imgproc.cvtColor(matmain, matmain, Imgproc.COLOR_BGRA2BGR);

        int ansW = mat.width();
        int ansH = mat.height();

        double px4 = ansW*0.0346;
        double py4 = ansH/45;
        double pw4 = ansW*0.0346;
        double ph4 = ansH*0.0519;
        Rect rect4 = findSquare(mat,px4,py4,pw4,ph4);
        int x4 = rect4.x;
        int y4 = rect4.y;
        int w4 = rect4.width;
        int h4 = rect4.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px4,rectAns.y+py4),new Point(rectAns.x+px4+pw4-1,rectAns.y+py4+ph4-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px4+rectAns.x+x4+w4/2,(int)py4+rectAns.y+y4+h4/2,8));
        list.add(new Point(rectAns.x+px4+x4+w4/2,rectAns.y+py4+y4+h4/2));

        double px5 = ansW*0.451;
        double py5 = ansH/45;
        double pw5 = ansW*0.0346;
        double ph5 = ansH*0.0519;
        Rect rect5 = findSquare(mat,px5,py5,pw5,ph5);
        int x5 = rect5.x;
        int y5 = rect5.y;
        int w5= rect5.width;
        int h5 = rect5.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px5,rectAns.y+py5),new Point(rectAns.x+px5+pw5-1,rectAns.y+py5+ph5-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px5+rectAns.x+x5+w5/2,(int)py5+rectAns.y+y5+h5/2,8));
        list.add(new Point(rectAns.x+px5+x5+w5/2,rectAns.y+py5+y5+h5/2));

        double px6 = ansW*0.45062;
        double py6 = ansH*0.20371;
        double pw6 = ansW*0.0346;
        double ph6 = ansH*0.0519;
        Rect rect6 = findSquare(mat,px6,py6,pw6,ph6);
        int x6 = rect6.x;
        int y6 = rect6.y;
        int w6= rect6.width;
        int h6 = rect6.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px6,rectAns.y+py6),new Point(rectAns.x+px6+pw6-1,rectAns.y+py6+ph6-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px6+rectAns.x+x6+w6/2,(int)py6+rectAns.y+y6+h6/2,8));
        list.add(new Point(rectAns.x+px6+x6+w6/2,rectAns.y+py6+y6+h6/2));

        double px7 = ansW*0.45062;
        double py7 = ansH*0.38889;
        double pw7 = ansW*0.0346;
        double ph7 = ansH*0.0519;
        Rect rect7 = findSquare(mat,px7,py7,pw7,ph7);
        int x7 = rect7.x;
        int y7 = rect7.y;
        int w7= rect7.width;
        int h7 = rect7.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px7,rectAns.y+py7),new Point(rectAns.x+px7+pw7-1,rectAns.y+py7+ph7-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px7+rectAns.x+x7+w7/2,(int)py7+rectAns.y+y7+h7/2,8));
        list.add(new Point(rectAns.x+px7+x7+w7/2,rectAns.y+py7+y7+h7/2));

        double px8 = ansW*0.45062;
        double py8 = ansH*0.57406;
        double pw8 = ansW*0.0346;
        double ph8 = ansH*0.0519;
        Rect rect8 = findSquare(mat,px8,py8,pw8,ph8);
        int x8 = rect8.x;
        int y8 = rect8.y;
        int w8= rect8.width;
        int h8 = rect8.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px8,rectAns.y+py8),new Point(rectAns.x+px8+pw8-1,rectAns.y+py8+ph8-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px8+rectAns.x+x8+w8/2,(int)py8+rectAns.y+y8+h8/2,8));
        list.add(new Point(rectAns.x+px8+x8+w8/2,rectAns.y+py8+y8+h8/2));

        double px9 = ansW*0.45062;
        double py9 = ansH*0.75758;
        double pw9 = ansW*0.0346;
        double ph9 = ansH*0.0519;
        Rect rect9 = findSquare(mat,px9,py9,pw9,ph9);
        int x9 = rect9.x;
        int y9 = rect9.y;
        int w9= rect9.width;
        int h9 = rect9.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px9,rectAns.y+py9),new Point(rectAns.x+px9+pw9-1,rectAns.y+py9+ph9-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px9+rectAns.x+x9+w9/2,(int)py9+rectAns.y+y9+h9/2,8));
        list.add(new Point(rectAns.x+px9+x9+w9/2,rectAns.y+py9+y9+h9/2));

        double px10 = ansW*0.45062;
        double py10 = ansH*0.94198;
        double pw10 = ansW*0.0346;
        double ph10 = ansH*0.0519;
        Rect rect10 = findSquare(mat,px10,py10,pw10,ph10);
        int x10 = rect10.x;
        int y10 = rect10.y;
        int w10= rect10.width;
        int h10 = rect10.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px10,rectAns.y+py10),new Point(rectAns.x+px10+pw10-1,rectAns.y+py10+ph10-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px10+rectAns.x+x10+w10/2,(int)py10+rectAns.y+y10+h10/2,8));
        list.add(new Point(rectAns.x+px10+x10+w10/2,rectAns.y+py10+y10+h10/2));

        double px11 = ansW*0.51235;
        double py11 = ansH/45;
        double pw11 = ansW*0.0346;
        double ph11 = ansH*0.0519;
        Rect rect11 = findSquare(mat,px11,py11,pw11,ph11);
        int x11 = rect11.x;
        int y11 = rect11.y;
        int w11= rect11.width;
        int h11 = rect11.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px11,rectAns.y+py11),new Point(rectAns.x+px11+pw11-1,rectAns.y+py11+ph11-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px11+rectAns.x+x11+w11/2,(int)py11+rectAns.y+y11+h11/2,8));
        list.add(new Point(rectAns.x+px11+x11+w11/2,rectAns.y+py11+y11+h11/2));

        double px12 = ansW*0.51235;
        double py12 = ansH*0.20371;
        double pw12 = ansW*0.0346;
        double ph12 = ansH*0.0519;
        Rect rect12 = findSquare(mat,px12,py12,pw12,ph12);
        int x12 = rect12.x;
        int y12 = rect12.y;
        int w12= rect12.width;
        int h12 = rect12.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px12,rectAns.y+py12),new Point(rectAns.x+px12+pw12-1,rectAns.y+py12+ph12-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px12+rectAns.x+x12+w12/2,(int)py12+rectAns.y+y12+h12/2,8));
        list.add(new Point(rectAns.x+px12+x12+w12/2,rectAns.y+py12+y12+h12/2));

        double px13 = ansW*0.51335;
        double py13 = ansH*0.38889;
        double pw13 = ansW*0.0346;
        double ph13 = ansH*0.0519;
        Rect rect13 = findSquare(mat,px13,py13,pw13,ph13);
        int x13 = rect13.x;
        int y13 = rect13.y;
        int w13= rect13.width;
        int h13 = rect13.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px13,rectAns.y+py13),new Point(rectAns.x+px13+pw13-1,rectAns.y+py13+ph13-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px13+rectAns.x+x13+w13/2,(int)py13+rectAns.y+y13+h13/2,8));
        list.add(new Point(rectAns.x+px13+x13+w13/2,rectAns.y+py13+y13+h13/2));

        double px14 = ansW*0.51435;
        double py14 = ansH*0.57406;
        double pw14 = ansW*0.0346;
        double ph14 = ansH*0.0519;
        Rect rect14 = findSquare(mat,px14,py14,pw14,ph14);
        int x14 = rect14.x;
        int y14 = rect14.y;
        int w14= rect14.width;
        int h14 = rect14.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px14,rectAns.y+py14),new Point(rectAns.x+px14+pw14-1,rectAns.y+py14+ph14-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px14+rectAns.x+x14+w14/2,(int)py14+rectAns.y+y14+h14/2,8));
        list.add(new Point(rectAns.x+px14+x14+w14/2,rectAns.y+py14+y14+h14/2));

        double px15 = ansW*0.51535;
        double py15 = ansH*0.75758;
        double pw15 = ansW*0.0346;
        double ph15 = ansH*0.0519;
        Rect rect15 = findSquare(mat,px15,py15,pw15,ph15);
        int x15 = rect15.x;
        int y15 = rect15.y;
        int w15= rect15.width;
        int h15 = rect15.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px15,rectAns.y+py15),new Point(rectAns.x+px15+pw15-1,rectAns.y+py15+ph15-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px15+rectAns.x+x15+w15/2,(int)py15+rectAns.y+y15+h15/2,8));
        list.add(new Point(rectAns.x+px15+x15+w15/2,rectAns.y+py15+y15+h15/2));

        double px16 = ansW*0.51635;
        double py16 = ansH*0.94198;
        double pw16 = ansW*0.0346;
        double ph16 = ansH*0.0519;
        Rect rect16 = findSquare(mat,px16,py16,pw16,ph16);
        int x16 = rect16.x;
        int y16 = rect16.y;
        int w16= rect16.width;
        int h16 = rect16.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px16,rectAns.y+py16),new Point(rectAns.x+px16+pw16-1,rectAns.y+py16+ph16-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px16+rectAns.x+x16+w16/2,(int)py16+rectAns.y+y16+h16/2,8));
        list.add(new Point(rectAns.x+px16+x16+w16/2,rectAns.y+py16+y16+h16/2));

        double px17 = ansW*0.92593;
        double py17 = ansH/45;
        double pw17 = ansW*0.0346;
        double ph17 = ansH*0.0519;
        Rect rect17 = findSquare(mat,px17,py17,pw17,ph17);
        int x17 = rect17.x;
        int y17 = rect17.y;
        int w17= rect17.width;
        int h17 = rect17.height;
        Imgproc.rectangle(matmain,new Point(rectAns.x+px17,rectAns.y+py17),new Point(rectAns.x+px17+pw17-1,rectAns.y+py17+ph17-1) ,new Scalar(255,255,255),2);
        listPoly.add(listPoly((int)px17+rectAns.x+x17+w17/2,(int)py17+rectAns.y+y17+h17/2,8));
        list.add(new Point(rectAns.x+px17+y17+w17/2,rectAns.y+py17+y17+h17/2));

        Imgproc.fillPoly(matmain,listPoly,new Scalar(255,0,0));
        Utils.matToBitmap(matmain, main);
        return list;
    }

    private static void cutAnswer210Pic() {
        try {
            listdrawans = new ArrayList<>();
            List<Point> listpoint = renderListSquareAns();
//            Log.i(TAG, "cutAnswer210Pic: listsize = "+listpoint.size());
            Mat mat = new Mat();
            Utils.bitmapToMat(main, mat);
            ArrayList<Bitmap> tempListBitmap = new ArrayList<>();
            Mat tempLeftMat = new Mat();
            Mat tempRightMat = new Mat();
            Bitmap tempLeftBitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
            double xleft = (listpoint.get(1).x+listpoint.get(2).x)/2-(listpoint.get(1).x-listpoint.get(0).x);
            double yleft = (listpoint.get(0).y + listpoint.get(1).y)/2;
            for (int i = 2; i <= 6; i++) {
                listdrawans.add(new Point(xleft,yleft));
                double width = listpoint.get(i-1).x - xleft;
                double height = listpoint.get(i).y - listpoint.get(i-1).y;
//                Log.i(TAG, "cutAnswer210Pic: "+(int)xleft+" "+(int)yleft+" "+(int)width+" "+(int)height);
                tempLeftMat = mat.clone().submat(new Rect((int)xleft,(int)yleft,(int)width,(int)height));
                tempLeftBitmap = Bitmap.createBitmap(tempLeftMat.width(), tempLeftMat.height(), Bitmap.Config.ARGB_8888);

                int width1 = tempLeftMat.width() / 6;
                int height1 = tempLeftMat.height() / 5;
//                Log.i(TAG, "cutAnswer210Pic: =====" + tempLeftMat.width());
//                Log.i(TAG, "cutAnswer210Pic: =====" + tempLeftMat.height());

                for(int x=0; x<5; x++) {
                    List<MatOfPoint> listPoly = new ArrayList<>();
                    listPoly.add(listPoly((int)(xleft+tempLeftMat.width()*0.09967)+height1/2,(int)(yleft)+(x*height1)+height1/2,8));
                    Imgproc.fillPoly(mat,listPoly,new Scalar(255,0,0));

                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.27027-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.45440-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.63852-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.82264-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                }

                Utils.matToBitmap(tempLeftMat, tempLeftBitmap);
                tempListBitmap.add(tempLeftBitmap);

                xleft = listpoint.get(i).x - width;
                yleft = yleft + height;
                List<MatOfPoint> listPolyy = new ArrayList<>();
                listPolyy.add(listPoly((int)(xleft),(int)yleft,8));
                Imgproc.fillPoly(mat,listPolyy,new Scalar(255,0,0));
            }
            xleft = (listpoint.get(7).x+listpoint.get(8).x)/2;
            yleft = (listpoint.get(7).y+listpoint.get(13).y)/2;
            for(int i=8; i<13; i++) {
                xleft = (listpoint.get(i-1).x+listpoint.get(i).x)/2;
                listdrawans.add(new Point(xleft,yleft));

                double width = listpoint.get(13).x - xleft;
                double height = listpoint.get(i).y - listpoint.get(i-1).y;
//                Log.i(TAG, "cutAnswer210Pic: "+(int)xleft+" "+(int)yleft+" "+(int)width+" "+(int)height);
                tempLeftMat = mat.clone().submat(new Rect((int)xleft,(int)yleft,(int)width,(int)height));
                tempLeftBitmap = Bitmap.createBitmap(tempLeftMat.width(), tempLeftMat.height(), Bitmap.Config.ARGB_8888);

                int width1 = tempLeftMat.width() / 6;
                int height1 = tempLeftMat.height() / 5;
                for(int x=0; x<5; x++) {
                    List<MatOfPoint> listPoly = new ArrayList<>();
                    listPoly.add(listPoly((int)(xleft+tempLeftMat.width()*0.09967)+height1/2,(int)(yleft)+(x*height1)+height1/2,8));
                    Imgproc.fillPoly(mat,listPoly,new Scalar(255,0,0));

                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.27027-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.45440-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.63852-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                    Imgproc.rectangle(mat,new Rect((int)(xleft+tempLeftMat.width()*0.82264-height1/3),(int)(yleft)+x*height1,height1*5/3,height1),new Scalar(255,255,255),1);
                }
                Utils.matToBitmap(tempLeftMat, tempLeftBitmap);
                tempListBitmap.add(tempLeftBitmap);

                List<MatOfPoint> listPolyy = new ArrayList<>();
                listPolyy.add(listPoly((int)(xleft+width),(int)(yleft+height),8));
                Imgproc.fillPoly(mat,listPolyy,new Scalar(255,0,0));

                yleft = yleft + height;
            }
            listImgAnswer = tempListBitmap;
            Utils.matToBitmap(mat, main);
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

    private static String findAns(Bitmap bitmap, int rows, int cols, int r, int xstart, int ystart) {
        Point[][] points = splitImage(bitmap, rows, cols);
        Mat mat = new Mat();
        Mat matmain = new Mat();
        Utils.bitmapToMat(main, matmain);
        Utils.bitmapToMat(bitmap, mat);
        Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2BGR);
        Imgproc.cvtColor(matmain, matmain, Imgproc.COLOR_BGRA2BGR);
        Mat imgGray = new Mat();
        Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(imgGray, imgGray);
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(imgGray, thresh, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 201, 31);
        Core.bitwise_not(thresh, thresh);
        String ansStr = "";
        boolean columnTouched = false;
        double radius = mat.width() / cols / 2;
        Log.i(TAG, "findAns: "+mat.width()/cols);
        Log.i(TAG, "findAns: "+mat.height()/rows);
        for (int i = 0; i < cols; i++) {
            String str = "";
            ArrayList<Point> pst = new ArrayList<>();
            for (int j = 0; j < rows; j++) {
                double maxS = -999;
                Point pMax = points[j][i];
                for(int i1=-8; i1<=8; i1+=4) {
                    for(int j1=-8; j1<=8; j1+=4) {
                        if (maxS / (Math.PI * radius * radius) > 0.9) {
                            break;
                        }
                        Mat mark = new Mat(mat.size(), thresh.type(), new Scalar(0, 0, 0));
                        Imgproc.circle(mark, new Point(points[j][i].x+i1,points[j][i].y+j1), (int) radius, new Scalar(255, 255, 255), -1);
                        Core.bitwise_and(thresh, thresh, mark, mark);
                        double count = Core.countNonZero(mark);
                        if(maxS < count) {
                            maxS = count;
                            pMax = new Point(points[j][i].x+i1,points[j][i].y+j1);
                        }
                    }
                }
                if (maxS / (Math.PI * radius * radius) > 0.6) {
                    pst.add(pMax);
                    if (!columnTouched) {
                        str = j + "";
                        columnTouched = true;
                    } else {
                        str = "#";
                    }
                }
            }
            if (pst.size() == 1) {
                Imgproc.circle(matmain, new Point(xstart+pst.get(0).x,ystart+pst.get(0).y), (int) radius, new Scalar(0, 255, 0), 3);
            }
            if (pst.size() > 1) {
                for (Point point : pst) {
                    Imgproc.circle(matmain, new Point(xstart+point.x,ystart+point.y), (int) radius, new Scalar(255, 255, 0), 3);
                }
            }
            if (columnTouched) {
                ansStr += str;
                columnTouched = false;
            } else {
                ansStr += "#";
            }
        }
        Utils.matToBitmap(matmain, main);
        Utils.matToBitmap(thresh, bitmap);
//        Utils.matToBitmap(thresh, bitmap);
        return ansStr;
    }

    private static String getSbd() {
        return findAns(imgSbd, 10, 6, 1, rectsbdmade.x+rectsbd.x,rectsbdmade.y+rectsbd.y);
    }

    private static String getMade() {
        return findAns(imgMade, 10, 3, 1,rectsbdmade.x+rectmade.x,rectsbdmade.y+rectmade.y);
    }

    public static Bitmap tmp1;
    public static Bitmap tmp2;
    static int dem=1;

    private static Object[] isAnswerChoosed(Mat mat) {
        Mat clone = mat.clone();
//        mat.convertTo(mat, -1, 1.0d, -50d);
        Mat imgGray = new Mat();
        Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_BGR2GRAY);
        Mat thresh = new Mat();
        Imgproc.adaptiveThreshold(imgGray, thresh, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 201, 11);
        Core.bitwise_not(thresh, thresh);
        double radius = mat.height() / 3*1.05;
        Point center = new Point(mat.width()/2,mat.height()/2);
        boolean check = false;

        Mat circles = new Mat();
        Imgproc.GaussianBlur(imgGray, imgGray, new Size(31, 31), 2, 2);
        Imgproc.equalizeHist(imgGray, imgGray);
        if(dem++==2) {
            tmp1 = Bitmap.createBitmap(mat.width(),mat.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(thresh,tmp1);
            tmp2 = Bitmap.createBitmap(mat.width(),mat.height(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(imgGray,tmp2);
        }
        Imgproc.HoughCircles(imgGray, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 20, 100, 30, 0,0);
        for (int i = 0; i < circles.cols(); i++) {
            double[] circle = circles.get(0, i);
            center = new Point(circle[0], circle[1]);

            Mat mask = new Mat(mat.size(), thresh.type(), new Scalar(0, 0, 0));
            Imgproc.circle(mask, center, (int)(mat.height() / 3 * 0.99), new Scalar(255, 255, 255), -1);
            Core.bitwise_and(thresh, thresh, mask, mask);
            double count = Core.countNonZero(mask);
            if(count / (Math.PI * radius * radius) > 0.6) {
                check = true;
                break;
            }
            break;
        }
        if(check)
            return new Object[] {true,center};

        Imgproc.cvtColor(mat, imgGray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.adaptiveThreshold(imgGray, thresh, 255,
                Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 201, 31);
        Core.bitwise_not(thresh, thresh);
        Imgproc.equalizeHist(imgGray,imgGray);

        double maxS = -999;
        Point pMax = center;
        for(int i1=-12; i1<=12; i1+=4) {
            for(int j1=-12; j1<=12; j1+=4) {
                if (maxS / (Math.PI * radius * radius) > 0.9) {
                    break;
                }
                Mat mask = new Mat(mat.size(), thresh.type(), new Scalar(0, 0, 0));
                Imgproc.circle(mask, new Point(center.x+i1,center.y+j1), (int) radius, new Scalar(255, 255, 255), -1);
                Core.bitwise_and(thresh, thresh, mask, mask);
                double count = Core.countNonZero(mask);
                if(maxS < count) {
                    maxS = count;
                    pMax = new Point(center.x+i1,center.y+j1);
                }
            }
        }
        if(maxS / (Math.PI * radius * radius) > 0.7) {
            return new Object[] {true,pMax};
        }
        return new Object[] {false,center};
    }

    private static String getAnswer(String list_answer, double hediem) {
        Mat matmain = new Mat();
        Utils.bitmapToMat(main, matmain);
        Imgproc.cvtColor(matmain, matmain, Imgproc.COLOR_BGRA2BGR);
        String ansStr = "";
        int quantity = 0;
        int ques = 0;
//        Log.i(TAG, "getAnswer: list_answer = "+list_answer + " " + list_answer.length());
        for (int i = 0; i < listImgAnswer.size(); i++) {
//            Point[][] points = splitImage(listImgAnswer.get(i), 5, 4);
            Mat mat = new Mat();
            Utils.bitmapToMat(listImgAnswer.get(i), mat);
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGRA2BGR);
            String ans5 = "";
            double radius = mat.width() / 28;
            for (int j = 0; j < 5; j++) {
                if (++quantity > list_answer.length()) break;
//                Log.i(TAG, "getAnswer: quantity = "+ quantity);
                List<Point> pstChoose = new ArrayList<>();
                String ans = "";
                int height1 = mat.height() / 5;
                Mat da1 = mat.clone().submat(new Rect((int)(mat.width()*0.27027-height1/3),(int)j*height1,height1*5/3,height1));
                Mat da2 = mat.clone().submat(new Rect((int)(mat.width()*0.45440-height1/3),(int)j*height1,height1*5/3,height1));
                Mat da3 = mat.clone().submat(new Rect((int)(mat.width()*0.63852-height1/3),(int)j*height1,height1*5/3,height1));
                Mat da4 = mat.clone().submat(new Rect((int)(mat.width()*0.82264-height1/3),(int)j*height1,height1*5/3,height1));
                boolean checkTrueAns = true;
                Object[] find1 = isAnswerChoosed(da1);
                if((boolean)find1[0]) {
                    pstChoose.add((Point)find1[1]);
                    checkTrueAns &= list_answer.charAt(quantity-1)=='A';
                }
                Object[] find2 = isAnswerChoosed(da2);
                if((boolean)find2[0]) {
                    pstChoose.add((Point)find2[1]);
                    checkTrueAns &= list_answer.charAt(quantity-1)=='B';
                }
                Object[] find3 = isAnswerChoosed(da3);
                if((boolean)find3[0]) {
                    pstChoose.add((Point)find3[1]);
                    checkTrueAns &= list_answer.charAt(quantity-1)=='C';
                }
                Object[] find4 = isAnswerChoosed(da4);
                if((boolean)find4[0]) {
                    pstChoose.add((Point)find4[1]);
                    checkTrueAns &= list_answer.charAt(quantity-1)=='D';
                }
                if (pstChoose.size() > 1) {
                    ans = quantity-1 + "#";
                }
                ans5 += ans;
//                Log.i(TAG, "ANSSSSSSSS: ==== " +quantity-1+ " " + (boolean)find1[0] +" "+(boolean)find2[0]+" "+(boolean)find3[0]+" "+(boolean)find4[0]);
                if (pstChoose.size() == 0) {
                    if(list_answer.charAt(quantity-1)=='A'){
                        Point loca = (Point) find1[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.27027-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='B'){
                        Point loca = (Point) find2[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.45440-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='C') {
                        Point loca = (Point) find3[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.63852-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='D') {
                        Point loca = (Point) find4[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.82264-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                } else if (pstChoose.size() == 1 && checkTrueAns) {
                    ques++;
                    if((boolean)find1[0]){
                        Point loca = (Point) find1[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.27027-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(0, 255, 0), 4);
                    }
                    else if((boolean)find2[0]){
                        Point loca = (Point) find2[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.45440-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(0, 255, 0), 4);
                    }
                    else if((boolean)find3[0]) {
                        Point loca = (Point) find3[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.63852-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(0, 255, 0), 4);
                    }
                    else if((boolean)find4[0]) {
                        Point loca = (Point) find4[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.82264-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(0, 255, 0), 4);
                    }
//                    Imgproc.circle(matmain, new Point(listdrawans.get(i).x+pstChoose.get(0).x,listdrawans.get(i).y+pstChoose.get(0).y), (int) radius, new Scalar(0, 255, 0), 4);
                } else if (pstChoose.size() == 1 && !checkTrueAns) {
//                    Imgproc.circle(matmain, new Point(listdrawans.get(i).x+pstChoose.get(0).x,listdrawans.get(i).y+pstChoose.get(0).y), (int) radius, new Scalar(255, 0, 0), 4);
                    if((boolean)find1[0]){
                        Point loca = (Point) find1[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.27027-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    else if((boolean)find2[0]){
                        Point loca = (Point) find2[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.45440-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    else if((boolean)find3[0]) {
                        Point loca = (Point) find3[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.63852-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    else if((boolean)find4[0]) {
                        Point loca = (Point) find4[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.82264-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    if(list_answer.charAt(quantity-1)=='A'){
                        Point loca = (Point) find1[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.27027-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='B'){
                        Point loca = (Point) find2[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.45440-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='C') {
                        Point loca = (Point) find3[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.63852-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='D') {
                        Point loca = (Point) find4[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.82264-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                } else if (pstChoose.size() > 1) {
//                    for (Point point : pstChoose) {
//                        Imgproc.circle(mat, point, (int) radius, new Scalar(255, 0, 0), 4);
//                    }
                    if((boolean)find1[0]){
                        Point loca = (Point) find1[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.27027-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    if((boolean)find2[0]){
                        Point loca = (Point) find2[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.45440-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    if((boolean)find3[0]) {
                        Point loca = (Point) find3[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.63852-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    if((boolean)find4[0]) {
                        Point loca = (Point) find4[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.82264-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 0, 0), 4);
                    }
                    if(list_answer.charAt(quantity-1)=='A'){
                        Point loca = (Point) find1[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.27027-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='B'){
                        Point loca = (Point) find2[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.45440-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='C') {
                        Point loca = (Point) find3[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.63852-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                    else if(list_answer.charAt(quantity-1)=='D') {
                        Point loca = (Point) find4[1];
                        Imgproc.circle(matmain, new Point(listdrawans.get(i).x+loca.x+(int)(mat.width()*0.82264-height1/3),listdrawans.get(i).y+loca.y+(int)j*height1), (int) radius, new Scalar(255, 255, 0), 4);
                    }
                }
            }
            ansStr += ans5;
//            Log.i(TAG, "getAnswer: ==== "+ ans5);
            Utils.matToBitmap(mat, listImgAnswer.get(i));
        }
        socaudung = ques;
        tongsocau = list_answer.length();
        score = String.format("%.2f", (ques * 1.0 / list_answer.length() * hediem));
//        Log.i(TAG, "getAnswer: socre = =score = == "+score);
//        Log.i(TAG, "getAnswer: socre = =ques = == "+ques);
//        Log.i(TAG, "getAnswer: socre = list_answer.length()= = == "+list_answer.length());
//        Log.i(TAG, "getAnswer: hediem = = = == "+hediem);
        Utils.matToBitmap(matmain,main);
        return ansStr;
    }

    private static Bitmap recoverBitmap(Bitmap bitmap) {
        Bitmap finalBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(finalBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);
//        canvas.drawBitmap(imgSbd, rectsbdmade.x+rectsbd.x, rectsbdmade.y+rectsbd.y, null);
//        canvas.drawBitmap(imgMade, rectsbdmade.x+rectmade.x, rectsbdmade.y+rectmade.y, null);
//        int y = bitmap.getHeight() / 3 + 75;
//        for (int i = 0; i < 5; i++) {
//            Bitmap subBitmap = listImgAnswer.get(i);
//            canvas.drawBitmap(subBitmap, 205, y, null);
//            y += 260;
//        }
//        y = bitmap.getHeight() / 3 + 75;
//        for (int i = 5; i < 10; i++) {
//            Bitmap subBitmap = listImgAnswer.get(i);
//            canvas.drawBitmap(subBitmap, bitmap.getWidth() - (bitmap.getWidth() - 710) + 180, y, null);
//            y += 260;
//        }
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

    @SuppressLint("Range")
    private Bitmap getDataFromDB(Bitmap bitmap) {
        db = new DataBase((AppCompatActivity) context);
        if (db == null) {
            return bitmap;
        }
        try {
            Bitmap oldBm = bitmap;

            String made = getMade();
            String sbd = getSbd();
            this.made = made;
            this.sbd = sbd;
            double hediem = 0;
            String list_answer = "";
            if (made.contains("#")) {
                score = "Không nhận diện được mã đề!";
                Log.i(TAG, "getDataFromDB: " + score);
//                bitmap = imgMade;
                return recoverBitmap(bitmap);
            }
//            Log.i(TAG, "getDataFromDB: ===== "+made);
//            Cursor c = db.mydatabase.rawQuery("select * from cauhoi where makithi = ? and made = ?",
//                    new String[]{makithi, made});
            Cursor c = db.mydatabase.rawQuery("select * from cauhoi where makithi = " + makithi + " and made = '" + made + "' and kieucauhoi = 1",
                    null);
            if (c.getCount() == 0) {
                score = "Mã đề không tồn tại!";
                return recoverBitmap(bitmap);
            }
            c.moveToFirst();
            while (!c.isAfterLast()) {
                list_answer = c.getString(c.getColumnIndex("dapan"));
                Log.i(TAG, "getDataFromDB: ========= "+list_answer);
                c.moveToNext();
            }
            c.close();
//            Cursor c2 = db.mydatabase.rawQuery("select hediem from kithi where makithi = ?",
//                    new String[]{makithi});
            Cursor c2 = db.mydatabase.rawQuery("select hediem from kithi where makithi = " + makithi,
                    null);
            c2.moveToFirst();
            while (!c2.isAfterLast()) {
                hediem = c2.getDouble(0);
                break;
            }
            String list_select_ans = getAnswer(list_answer, hediem);
            Log.d(TAG, "getDataFromDB: answer ==== " + list_select_ans);

            //////

            // save to db
            String sql = "select masv, hinhanh from diem where makithi=" + makithi + " and masv='" + sbd + "' and hinhanh not null and loaicauhoi = 1";
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
                }
                double diem = Double.parseDouble(score.replace(',','.'));
                ContentValues valuediem = new ContentValues();
                valuediem.put("makithi", makithi);
                valuediem.put("diemso", diem);
                valuediem.put("masv", sbd);
                valuediem.put("hinhanh", imguri);
                Log.d(TAG, "getDataFromDB: Uriiiiii = " + imguri);
                if (db.mydatabase.update("diem", valuediem, "makithi = " + makithi + " and masv = '" + sbd + "' and loaicauhoi = 1", null) == -1) {
                    msg = "Fail to insert record";
                } else {
                    msg = "Insert record sucess";
                }
            } else {
                double diem = Double.parseDouble(score.replace(',','.'));
                ContentValues valuediem = new ContentValues();
                valuediem.put("makithi", makithi);
                valuediem.put("diemso", diem);
                valuediem.put("masv", sbd);
                valuediem.put("hinhanh", imguri);
                valuediem.put("loaicauhoi",1);
                if (db.mydatabase.insert("diem", null, valuediem) == -1) {
                    msg = "Fail to insert record";
                } else {
                    msg = "Insert record sucess";
                }
            }

            file = new File(imguri);
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
    public String getScore(){
        return score;
    }
    public Bitmap run(Bitmap bitmap) {
        try {
            main = bitmap;
            cutImage(bitmap);
            cutSbdMade();
            cutAnswer22Pic();
            cutAnswer210Pic();
            return getDataFromDB(main);
//            return bitmap;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}