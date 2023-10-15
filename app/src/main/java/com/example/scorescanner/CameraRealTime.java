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
import android.view.Display;

import org.opencv.android.CameraActivity;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
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

    private static String TAG = "real time";

    static {
        if (OpenCVLoader.initDebug())
            Log.i(TAG, "=== Opencv loaded ===");
        else
            Log.i(TAG, "=== ERROR ===");
    }

    CameraBridgeViewBase mCameraView;
    Mat mRgba;
    Mat mMain;
    Mat mSize;
    Rect[] listRectHCN;
    Mat[] listMatHCN;
    Mat[] listMatHCNGray;
    int width;
    int height;
    float mScale;
    int mStartY;
    int demTimeCheck;
    static Bitmap rotatedBitmap;

    public static Bitmap getRotatedBitmap() {
        return rotatedBitmap;
    }

    @Override
    public void onCreate(Bundle bundle) {
        getWindow().setFlags(1024, 1024);
        requestWindowFeature(1);
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_camera_real_time);
        mCameraView = (CameraBridgeViewBase) findViewById(R.id.frame_Surface);
        if(!getPermission()){
            findCameraView();
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
        public void onCameraViewStarted(int frameWidth, int frameHeight) {
//            Display display = getWindowManager().getDefaultDisplay();
//            android.graphics.Point size = new android.graphics.Point();
//            display.getSize(size);
//            frameHeight = size.x;
//            frameWidth = size.y;

            mScale = ((float) frameWidth) / ((float) frameHeight);
            float f = mScale;
            if (f >= 1.7777778f) {
                height = frameHeight;
                width = (frameHeight * 16) / 9;
            } else {
                width = frameWidth;
                height = (frameWidth * 9) / 16;
            }
            if (f >= 1.7777778f) {
                mStartY = 0;
            } else {
                mStartY = (frameHeight - height) / 2;
            }
            mRgba = new Mat(frameHeight, frameWidth, CvType.CV_8UC4);
            mMain = new Mat(frameHeight, frameWidth, CvType.CV_8UC4);

            listMatHCN = new Mat[4];
            listMatHCNGray = new Mat[4];

            demTimeCheck = 0;

            int i5 = height / 4;
            int widthImg = (height * 3) / 4;
            int heightImg = (height * 9) / 8;

            mSize = new Mat(widthImg, heightImg, CvType.CV_8UC4);

            listRectHCN = new Rect[4];
            listRectHCN[0] = new Rect(0, mStartY, i5, i5);
            listRectHCN[1] = new Rect(heightImg, mStartY, i5, i5);
            listRectHCN[2] = new Rect(0, mStartY + widthImg, i5, i5);
            listRectHCN[3] = new Rect(heightImg, mStartY + widthImg, i5, i5);
        }

        @Override
        public void onCameraViewStopped() {
            mRgba.release();
            mMain.release();
            mSize.release();
            for(int i=0; i<listMatHCNGray.length; i++) {
                if(listMatHCNGray[i] != null) {
                    listMatHCNGray[i].release();
                }
                if(listMatHCN[i] != null) {
                    listMatHCN[i].release();
                }
            }
        }

        @Override
        public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
            if(inputFrame != null){
                mRgba = inputFrame.rgba();
            }
            if(mRgba != null){
                mMain = mRgba.clone();
            }

            float f = ((float) width) / 1280.0f;
            drawSquare(f);
            if (checkSquare()) {
                try {
                    Bitmap bitmap = Bitmap.createBitmap(mSize.width(), mSize.height(), Bitmap.Config.ARGB_8888);
                    Utils.matToBitmap(mSize, bitmap);

                    Matrix matrix = new Matrix();
                    float rotationAngleDegrees = 90;
                    matrix.postRotate(rotationAngleDegrees);

                    rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

                    String path = ImageHelper.saveImage(rotatedBitmap);
                    Intent intent = new Intent(CameraRealTime.this, ViewImage.class);
                    intent.putExtra("path", path);
                    startActivityForResult(intent, 100);

                } catch (Exception ex) {
//                    Log.i(TAG, "onCameraFrame: ======" + ex.toString());
                }
            }
            return mRgba;
        }
    };

    private void drawSquare(float f) {
        for (int i = 0; i < 4; i++) {
            if (mRgba != null) {
                listMatHCN[i] = mRgba.submat(listRectHCN[i]);
                listMatHCN[i].convertTo(listMatHCN[i], -1, 1.0d, 100.0d);
                listMatHCNGray[i] = listMatHCN[i].clone();
                Imgproc.cvtColor(listMatHCNGray[i], listMatHCNGray[i], Imgproc.COLOR_BGR2GRAY);
                Imgproc.GaussianBlur(listMatHCNGray[i], listMatHCNGray[i], new Size(3.0d, 3.0d), 2.0d);
                Imgproc.adaptiveThreshold(listMatHCNGray[i], listMatHCNGray[i], 255, Imgproc.ADAPTIVE_THRESH_MEAN_C, Imgproc.THRESH_BINARY, 31, 3);
            }
        }
    }

    private boolean checkSquare() {
        int dem = 0;
        Point[] pt = new Point[4];
        Rect[] arrRect = new Rect[4];
        for (int i = 0; i < 4; i++) {
            if (listMatHCNGray[i] != null) {
                try {
                    ArrayList<MatOfPoint> arrayList = new ArrayList<>();
                    Imgproc.findContours(listMatHCNGray[i], arrayList, new Mat(), Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C, Imgproc.CHAIN_APPROX_SIMPLE, new Point(0.0d, 0.0d));

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

                        double countNonZero = ((double) (contourArea - Core.countNonZero(listMatHCNGray[i].submat(boundingRect)))) / ((double) contourArea);

                        if (countNonZero > 0.70 && f2 > 0.8f && f2 < 1.2f) {
                            dem++;
                            arrRect[i] = boundingRect;
                            arrRect[i].x += listRectHCN[i].x;
                            arrRect[i].y += listRectHCN[i].y;
                            Imgproc.rectangle(mRgba, arrRect[i], new Scalar(255, 0, 0), 3);
                            pt[i] = new Point(minAreaRect.center.x + listRectHCN[i].x, minAreaRect.center.y + listRectHCN[i].y);
                            if (pt[i].x < mMain.width() / 2) {
                                if (pt[i].y < mMain.height() / 2) {
                                    pt[i].x += i3 / 2;
                                    pt[i].y += i3 / 2;
                                } else {
                                    pt[i].x += i3 / 2;
                                    pt[i].y -= i3 / 2;
                                }
                            } else {
                                if (pt[i].y < mMain.height() / 2) {
                                    pt[i].x -= i3 / 2;
                                    pt[i].y += i3 / 2;
                                } else {
                                    pt[i].x -= i3 / 2;
                                    pt[i].y -= i3 / 2;
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
        if (dem == 4 && checkArrRect(arrRect) && demTimeCheck >= 15) {
            demTimeCheck = 0;

            int widthImg = height * 3 / 4;
            int heightImg = height * 9 / 8;

            Point[] dstPoints = new Point[4];
            dstPoints[0] = new Point(0, 0);
            dstPoints[1] = new Point(heightImg, 0);
            dstPoints[2] = new Point(0, widthImg);
            dstPoints[3] = new Point(heightImg, widthImg);

            Mat perspectiveMatrix = Imgproc.getPerspectiveTransform(new MatOfPoint2f(pt), new MatOfPoint2f(dstPoints));
            Imgproc.warpPerspective(mMain, mSize, perspectiveMatrix, new Size(heightImg, widthImg));
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

    private boolean getPermission() {
        if (Build.VERSION.SDK_INT > 22) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA}, 101);
                return true;
            } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
                return true;
            } else if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                return true;
            }
        }
        return false;
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