package com.example.scorescanner;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Base64;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.Text;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.Rect;


import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetShortAnswer extends AppCompatActivity {
    private Context context;
    public GetShortAnswer(Context context) {
        this.context = context;
    }
    private static ArrayList<Bitmap> listImg;

    private static void getImgShortAnswer(Bitmap bitmap) {
        try {
            Mat answerMat = new Mat();
            Utils.bitmapToMat(bitmap, answerMat);
            ArrayList<Bitmap> tempListBitmap = new ArrayList<>();
            Mat tempMat = new Mat();
            Bitmap tempBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            int y = 0;
            for (int i = 0; i < 5; i++) {
                tempMat = answerMat.submat(new Rect(0, y, 1400, 345));
                tempBitmap = Bitmap.createBitmap(tempMat.width(), tempMat.height(), Bitmap.Config.ARGB_8888);
                tempListBitmap.add(tempBitmap);
                Utils.matToBitmap(tempMat, tempBitmap);
                y += 445;

                //doc chữ trên từng ảnh
                // Chuyển đổi Bitmap thành byte array
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                tempBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream.toByteArray();

                // Encode byte array thành chuỗi Base64
                String imgBase64 = Base64.encodeToString(byteArray, Base64.DEFAULT);

            }
            listImg = tempListBitmap;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Bitmap getShortAnwer(Bitmap bitmap) {
        try {
            getImgShortAnswer(bitmap);
            return listImg.get(4);
        }
        catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }

}