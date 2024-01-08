package com.example.scorescanner;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaquo.python.PyObject;
import com.chaquo.python.Python;
import com.chaquo.python.android.AndroidPlatform;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class GetShortAnswer extends AppCompatActivity {
    private Context context;
    private static ArrayList<Bitmap> listImg;
    private String DB_PATH_SUFFIX = "/databases/";
    private static  DataBase db;
    private String DATABASE_NAME="ssdb2.db";
    public static SQLiteDatabase database=null;
    private List<String>  listStudentAns;
    private List<String> listResult;
    private List<String>  listCorrectAns;
    private int count;
    //    static private Python py;
    public GetShortAnswer(Context context) {
//        db = new DataBase(GetShortAnswer.this);
//        this.py = Python.getInstance();
        this.context = context;
        this.listStudentAns  = new ArrayList<>(Collections.nCopies(5, ""));
        this.listCorrectAns = new ArrayList<>(Collections.nCopies(5, ""));
        this.listResult = new ArrayList<>(Collections.nCopies(5, ""));
        this.count = 0;
    }
//    private void getImgShortAnswer(DataBase db, Bitmap bitmap, String makithi, String user, String made) {
    private void getImgShortAnswer(DataBase db, Bitmap bitmap, int makithi, String user, String made) {
        try {
            if (!Python.isStarted()) {
                Python.start(new AndroidPlatform(this.context));
            }
            Python py = Python.getInstance();
            //xử lý cắt ảnh
            Mat answerMat = new Mat();
            Utils.bitmapToMat(bitmap, answerMat);
            ArrayList<Bitmap> tempListBitmap = new ArrayList<>();
            Mat tempMat = new Mat();
            Bitmap tempBitmap = Bitmap.createBitmap(100, 100, Bitmap.Config.ARGB_8888);
            List<CompletableFuture<String>> futures = new ArrayList<>();
            int y = 0;
            for (int i = 0; i <= 4; i++) {
                int currentQues = i;
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
                byte[] decodedBytes = Base64.decode(imgBase64, Base64.DEFAULT);
                Bitmap image =  BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                DetectText detectText = new DetectText();
                CompletableFuture<String> resultFuture = detectText.detectTxt(tempBitmap);
                futures.add(resultFuture);

                //mỗi lần chụp ảnh thì sẽ nhận được 1 result text khác nhau
//                resultFuture.thenAccept(result -> {
//                    // xử lý kết quả result là text được nhận diện
////                    Log.d("Detected text: ",  result.toString());
//
//                }).exceptionally(e -> {
//                    e.printStackTrace();
//                    return null;
//                });
            }
            CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
            allOf.join();
            PyObject pyobj = py.getModule("generativeai");
            for (int currentQues=0; currentQues<=4; currentQues++) {
                // Lấy kết quả từ từng CompletableFuture
                CompletableFuture<String> future = futures.get(currentQues);
                String result = future.join();
                // Xử lý kết quả ở đây
                String question = "What do you go to school?";
                String correctAns = "I going to school by bike";
                String student_answer = result.toString();

                //truy van db để chấm điểm
                //sql truy cấn câu hỏi có trong db của mã đề
                //select ndcauhoi,dapan, tencauhoi from cauhoi where makithi = 1
                // and made = '001' and kieucauhoi = 2 ORDER by tencauhoi
                Cursor c = db.mydatabase.rawQuery("select ndcauhoi,dapan,tencauhoi from cauhoi where makithi = "
                        + makithi + " and made = '" + made + "' and kieucauhoi = 2 and tencauhoi = "
                        + (int) (currentQues + 1) + "", null);
                c.moveToFirst();
                Boolean status = true;
                if (c.getCount() == 0) {
                    status = false;
                    question = "Không có data";
                    correctAns = "Không có data";
                } else {
                    //neu đã tồn tại thì xử lý chấm điểm
                    while (c.isAfterLast() == false) {

                        question = c.getString(0);
                        correctAns = c.getString(1);
                        String tencauhoi = c.getString(2);
//                        System.out.println("xu ly: cau " + tencauhoi+ " noi dung: " + question + " correct ans: " + correctAns + " student ans:" + student_answer );
                        c.moveToNext();
                    }
                }
                final String finalQuestion = question;
                final String finalCorrectAns = correctAns;
                final String finalStudentAnswer = student_answer;

                String res = "0";
                if (status) {
                    System.out.println("-------- xu ly: cau: " + finalQuestion + " correct ans: " + finalCorrectAns + " student ans:" + finalStudentAnswer);
//                        String res = pyobj.callAttr("grader",finalQuestion, finalCorrectAns, finalStudentAnswer).toString();
                    res = pyobj.callAttr("grader", finalQuestion, finalCorrectAns, finalStudentAnswer).toString();
                    // Xử lý kết quả ở đây, ví dụ cập nhật UI
                    res = res.replaceAll("[^0-9]+", "");
                }
                int score = Integer.parseInt(res);
                if (score <= 50) res = "0";
                else if (score > 50 && score <= 75) res = "0.5";
                else res = "1.0";
                Log.d("---- ket qua cham diem", res);
                listStudentAns.add(student_answer);
                listResult.add(res);
                this.count++;
                listCorrectAns.add(correctAns);
            }
            listImg = tempListBitmap;
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public Bitmap getShortAnwer(DataBase db,Bitmap bitmap, String makithi,String user, String made) {
    public Bitmap getShortAnwer(DataBase db,Bitmap bitmap, int makithi, String user, String made) {
        try {
            getImgShortAnswer(db, bitmap,  makithi, user,  made);
            return listImg.get(4);
        }
        catch (Exception e) {
            e.printStackTrace();
            return bitmap;
        }
    }
    public List<String> getListResult(){
        return this.listResult;
    }
    public List<String> getListStudentAns(){
        return this.listStudentAns;
    }
    public List<String> getListCorrectAns(){
        return this.listCorrectAns;
    }

    public ArrayList<Bitmap> getListImg() { return listImg; }

    private void processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists())
        {
            try{
                CopyDataBaseFromAsset();
//                Toast.makeText(this, "Copying sucess from Assets folder", Toast.LENGTH_LONG).show();
            }
            catch (Exception e){
                Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();
            }
        }

    }
    public int getCount(){
        return count;
    }

    private String getDatabasePath() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX+ DATABASE_NAME;
    }
    public void CopyDataBaseFromAsset() {

        try {
            InputStream myInput;
            myInput = getAssets().open(DATABASE_NAME);

            String outFileName = getDatabasePath();

            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
//            if (!f.exists())
            f.mkdir();

            OutputStream myOutput = new FileOutputStream(outFileName);

            int size = myInput.available();
            byte[] buffer = new byte[size];
            myInput.read(buffer);
            myOutput.write(buffer);

            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}