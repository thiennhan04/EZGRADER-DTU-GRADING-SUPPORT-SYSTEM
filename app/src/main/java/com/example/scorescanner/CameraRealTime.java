package com.example.scorescanner;

import com.example.scorescanner.ImageHelper;
import com.example.scorescanner.ViewImage;

import androidx.annotation.NonNull;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class CameraRealTime extends CameraActivity {
    private  int session = 0;
    private static String TAG = "real time";
    String made = "#";
    String sbd = "#";
    static {
        if (OpenCVLoader.initDebug())
            Log.i(TAG, "=== Opencv loaded ===");
        else
            Log.i(TAG, "=== ERROR ===");
    }

    private static CameraBridgeViewBase mCameraView;
    private static Mat mRgba;
    private static Mat imgMain;
    private static Rect[] listRectHCN;
    private static Mat[] listMatHCN;
    private static Mat[] listMatHCNGray;
    private static Mat transformedImage;
    private static Bitmap rotatedBitmap;


    private static int sessionGrader = 1;
    int demTimeCheck;

    public static Bitmap getRotatedBitmap() {
        return rotatedBitmap;
    }

    @Override
    public void onCreate(Bundle bundle) {
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_camera_real_time);
        getPermission();
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.frame_Surface);
        mCameraView.setCvCameraViewListener(callBack);
        if (OpenCVLoader.initDebug()) {
            mCameraView.enableView();
            if(session == 0) Toast.makeText(this, "Phiên 1: Trắc nghiệm", Toast.LENGTH_SHORT).show();
            else{
                Toast.makeText(this, "Phiên 2: Tự luận", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void findCameraView() {
        mCameraView.setCvCameraViewListener(callBack);
        if (OpenCVLoader.initDebug()) {
            mCameraView.enableView();
            mCameraView.enableFpsMeter();
        }
    }

    private CameraBridgeViewBase.CvCameraViewListener2 callBack = new CameraBridgeViewBase.CvCameraViewListener2() {
        @Override
        public void onCameraViewStarted(int width, int height) {
            demTimeCheck = 0;
            int edge = height / 3;
            int startEdge = (height * 9) / 8;
            listMatHCN = new Mat[4];
            listMatHCNGray = new Mat[4];
            listRectHCN = new Rect[4];
            listRectHCN[0] = new Rect(0, 0, edge, edge * 3 / 4);
            listRectHCN[1] = new Rect(startEdge, 0, edge, edge * 3 / 4);
            listRectHCN[2] = new Rect(0, height - edge * 3 / 4, edge, edge * 3 / 4);
            listRectHCN[3] = new Rect(startEdge, height - edge * 3 / 4, edge, edge * 3 / 4);
        }

        @Override
        public void onCameraViewStopped() {

        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            mRgba = inputFrame.rgba();
            drawSquare();
            if (checkSquare()) {
                try {
                    Bitmap bitmap = Bitmap.createBitmap(transformedImage.width(), transformedImage.height(),
                            Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(transformedImage, bitmap);
                    Matrix matrix = new Matrix();
                    float rotationAngleDegrees = 90;
                    matrix.postRotate(rotationAngleDegrees);
                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                            bitmap.getHeight(), matrix, true);
                    String username = getIntent().getStringExtra("username");
                    String makithi = getIntent().getStringExtra("makithi");
                    Intent tnIntent = new Intent(CameraRealTime.this, ViewImage.class);
                    Intent tlIntent = new Intent(CameraRealTime.this, showketquatl.class);
                    tnIntent.putExtra("username", username);
//                    System.out.println("-------------------------- " + makithi);
                    tnIntent.putExtra("makithi", makithi);


                    tlIntent.putExtra("username", username);
                    tlIntent.putExtra("made",made);
                    tlIntent.putExtra("makithi", makithi);
                    tlIntent.putExtra("sbd",sbd);

                    if(session == 0){
                        startActivityForResult(tnIntent, 110);
                    }
                    else {

                        startActivityForResult(tlIntent, 111);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return mRgba;
        }
    };

    private void drawSquare() {
        imgMain = mRgba.clone();
        for (int i = 0; i < 4; i++) {
            if (mRgba != null) {
                listMatHCN[i] = mRgba.submat(listRectHCN[i]);
                listMatHCN[i].convertTo(listMatHCN[i], -1, 1.0d, 100.0d);
                listMatHCNGray[i] = listMatHCN[i].clone();
                Imgproc.cvtColor(listMatHCNGray[i], listMatHCNGray[i], Imgproc.COLOR_BGR2GRAY);
                Imgproc.GaussianBlur(listMatHCNGray[i], listMatHCNGray[i], new Size(3.0d, 3.0d), 2.0d);
                Imgproc.adaptiveThreshold(listMatHCNGray[i], listMatHCNGray[i], 255,
                        Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 31, 3);
            }
        }
    }

    private boolean checkSquare() {
        int dem = 0;
        Point[] pt = new Point[4];
        transformedImage = new Mat();
        Rect[] arrRect = new Rect[4];
        for (int i = 0; i < 4; i++) {
            if (listMatHCNGray[i] != null) {
                try {
                    ArrayList<MatOfPoint> arrayList = new ArrayList<>();
                    Imgproc.findContours(listMatHCNGray[i], arrayList, new Mat(),
                            Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0.0d, 0.0d));
                    Collections.sort(arrayList, new Comparator<MatOfPoint>() {
                        @Override
                        public int compare(MatOfPoint contour1, MatOfPoint contour2) {
                            double area1 = Imgproc.contourArea(contour1);
                            double area2 = Imgproc.contourArea(contour2);
                            return Double.compare(area1, area2);
                        }
                    });
                    for (int j = 0; j < arrayList.size(); j++) {
                        Rect boundingRect = Imgproc.boundingRect(arrayList.get(j));
                        RotatedRect minAreaRect = Imgproc.minAreaRect(new MatOfPoint2f(arrayList.get(j).toArray()));
                        int contourArea = (int) Imgproc.contourArea(arrayList.get(j));
                        int i3 = (int) minAreaRect.size.width;
                        int i4 = (int) minAreaRect.size.height;
                        float f2 = ((float) i3) / ((float) i4);
                        double countNonZero = ((double) (contourArea -
                                Core.countNonZero(listMatHCNGray[i].submat(boundingRect)))) / ((double) contourArea);
                        if (countNonZero > 0.6 && f2 > 0.8f && f2 < 1.2f) {
                            Imgproc.rectangle(listMatHCN[i], boundingRect, new Scalar(255, 0, 0), 3);
                            dem++;
                            arrRect[i] = boundingRect;
                            arrRect[i].x += listRectHCN[i].x;
                            arrRect[i].y += listRectHCN[i].y;
                            pt[i] = new Point(minAreaRect.center.x + listRectHCN[i].x,
                                    minAreaRect.center.y + listRectHCN[i].y);
                            if (pt[i].x < imgMain.width() / 2) {
                                if (pt[i].y < imgMain.height() / 2) {
                                    pt[i].x += i3 * 3 / 4;
                                    pt[i].y += i3 * 3 / 5;
                                } else {
                                    pt[i].x += i3 * 3 / 4;
                                    pt[i].y -= i3 * 3 / 4;
                                }
                            } else {
                                if (pt[i].y < imgMain.height() / 2) {
                                    pt[i].x -= i3 * 3 / 4;
                                    pt[i].y += i3 * 3 / 4;
                                } else {
                                    pt[i].x -= i3 * 3 / 4;
                                    pt[i].y -= i3 * 3 / 4;
                                }
                            }
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (dem == 4 && checkArrRect(arrRect) && demTimeCheck >= 30) {
            demTimeCheck = 0;
            Point[] dstPoints = new Point[4];
            dstPoints[0] = new Point(0, 0);
            dstPoints[1] = new Point(mRgba.width(), 0);
            dstPoints[2] = new Point(0, mRgba.height());
            dstPoints[3] = new Point(mRgba.width(), mRgba.height());
            Mat perspectiveMatrix = Imgproc.getPerspectiveTransform(new MatOfPoint2f(pt), new MatOfPoint2f(dstPoints));
            Imgproc.warpPerspective(imgMain, transformedImage, perspectiveMatrix, mRgba.size());
            Imgproc.resize(transformedImage, transformedImage, new Size(2126, 1418));
            return true;
        }
        return false;
    }

    private boolean checkArrRect(Rect[] arrRect){
        for(int i=1; i<4; i++){
            if(arrRect[i] == null && arrRect[i].width==0) {
                demTimeCheck = 0;
                return false;
            }
        }
        int area0 = arrRect[0].width * arrRect[0].height;
        for(int i=1; i<4; i++){
            if(Math.abs(area0-arrRect[i].width*arrRect[i].height)>area0/3) {
                demTimeCheck = 0;
                return false;
            }
        }
        demTimeCheck++;
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 110){
            if(resultCode == 0){
                Toast.makeText(this, "Chấm điểm thất bại, thực hiện lại phiên 1!", Toast.LENGTH_SHORT).show();
            }else {
                made = data.getStringExtra("made");
                sbd = data.getStringExtra("sbd");
                Toast.makeText(this, "Thành công! thực hiện phiên 2", Toast.LENGTH_SHORT).show();
                session = 1;
            }
        }else if(requestCode == 111){
            if(requestCode == 0){
                Toast.makeText(this, "Chấm điểm thất bại, thực hiện lại phiên 2!", Toast.LENGTH_SHORT).show();
            } else{
               session = 0;
                Toast.makeText(this, "Thành công phiên 2! kết thúc bài thi", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCameraView != null) {
            mCameraView.enableView();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraView != null) {
            mCameraView.disableView();
        }
    }

    private void getPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
            } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        }
    }

    @Override
    protected List<? extends CameraBridgeViewBase> getCameraViewList() {
        return Collections.singletonList(mCameraView);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 101: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mCameraView.setCameraPermissionGranted();
                    findCameraView();
                    getPermission();
                } else {
                    return;
                }
                return;
            }
            default: {
                Log.i(TAG, "onRequestPermissionsResult: ==============" + requestCode);
                return;
            }
        }
    }
}