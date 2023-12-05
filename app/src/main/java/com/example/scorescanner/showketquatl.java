package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class showketquatl extends AppCompatActivity {
    TextView txtdapan1, txtdapan2, txtdapan3, txtdapan4, txtdapan5;
    TextView txtans1, txtans2, txtans3, txtans4, txtans5;
    EditText edtkq1, edtkq2, edtkq3, edtkq4, edtkq5;
    ImageView imgv1,imgv2, imgv3,imgv4,imgv5;
    ScrollView scrollView;
    Button btnluukq;
    private int statusCode = 1;
    float valueEdtkq1 = 0.0f;
    float valueEdtkq2 = 0.0f;
    float valueEdtkq3 = 0.0f;
    float valueEdtkq4 = 0.0f;
    float valueEdtkq5 = 0.0f;
    private static  DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showketquatl);

        db = new DataBase(showketquatl.this);
        String username = getIntent().getStringExtra("username");
        String makithi = getIntent().getStringExtra("makithi");
//        String masv = getIntent().getStringExtra("masv");
        String sbd = getIntent().getStringExtra("sbd"); //giả lập mã sinh viên khi chưa ghép với trắc nghiệm
        String made = getIntent().getStringExtra("made");
        scrollView = findViewById(R.id.scrollview);
        btnluukq = findViewById(R.id.btnluukq);
        db = new DataBase(this);
        //lay ra img cac khung anh tl de truyen anh da cham vao
        imgv1 = findViewById(R.id.imgv1);
        imgv2 = findViewById(R.id.imgv2);
        imgv3 = findViewById(R.id.imgv3);
        imgv4 = findViewById(R.id.imgv4);
        imgv5 = findViewById(R.id.imgv5);
        //lay ra dap an co san trong db
        txtdapan1 = findViewById(R.id.txtdapan1);
        txtdapan2 = findViewById(R.id.txtdapan2);
        txtdapan3 = findViewById(R.id.txtdapan3);
        txtdapan4 = findViewById(R.id.txtdapan4);
        txtdapan5 = findViewById(R.id.txtdapan5);

        //in ra cau tra loi cua hoc sinh sau khi quet
        txtans1 = findViewById(R.id.txtans1);
        txtans2 = findViewById(R.id.txtans2);
        txtans3 = findViewById(R.id.txtans3);
        txtans4 = findViewById(R.id.txtans4);
        txtans5 = findViewById(R.id.txtans5);

        //in ra ket qua dung sai
        edtkq1 = findViewById(R.id.edtkq1);
        edtkq2 = findViewById(R.id.edtkq2);
        edtkq3 = findViewById(R.id.edtkq3);
        edtkq4 = findViewById(R.id.edtkq4);
        edtkq5 = findViewById(R.id.edtkq5);

        //giá trị điểm của cảu 5 câu hỏi


        String strEdtkq1 = edtkq1.getText().toString();
        String strEdtkq2 = edtkq2.getText().toString();
        String strEdtkq3 = edtkq3.getText().toString();
        String strEdtkq4 = edtkq4.getText().toString();
        String strEdtkq5 = edtkq5.getText().toString();


        Bitmap bitmap = CameraRealTime.getRotatedBitmap();
        GetShortAnswer getShortAnswer = new GetShortAnswer(showketquatl.this);

        new GraderAsyncTask(showketquatl.this, getShortAnswer, bitmap, makithi, username).execute();

        btnluukq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    valueEdtkq1 = Float.parseFloat(strEdtkq1);
                    valueEdtkq2 = Float.parseFloat(strEdtkq2);
                    valueEdtkq3 = Float.parseFloat(strEdtkq3);
                    valueEdtkq4 = Float.parseFloat(strEdtkq4);
                    valueEdtkq5 = Float.parseFloat(strEdtkq5);
                    if (!(valueEdtkq1 >= 0 && valueEdtkq1 <= 10 &&
                            valueEdtkq2 >= 0 && valueEdtkq2 <= 10 &&
                            valueEdtkq3 >= 0 && valueEdtkq3 <= 10 &&
                            valueEdtkq4 >= 0 && valueEdtkq4 <= 10 &&
                            valueEdtkq5 >= 0 && valueEdtkq5 <= 10)) {
                        Toast.makeText(showketquatl.this, "Điểm số phải từ 0 đến 10", Toast.LENGTH_SHORT).show();
                        throw new IllegalArgumentException("Các giá trị phải nằm trong khoảng từ 0 đến 10");
                    }
                } catch (Exception e){
                    Toast.makeText(showketquatl.this, "Điểm số không hợp lệ", Toast.LENGTH_SHORT).show();
                }
                float easayScore = valueEdtkq1 + valueEdtkq2 + valueEdtkq3 + valueEdtkq4 + valueEdtkq5;
                ContentValues easayResult = new ContentValues();
                easayResult.put("makithi", makithi);
                easayResult.put("diemso", easayScore);
                easayResult.put("hinhanh", makithi);
                easayResult.put("masv", sbd);
                easayResult.put("loaicauhoi", 2);
                System.out.println("-------lưu kết quả --- " + easayResult.toString());
                if(db.mydatabase.insert("diem",null,easayResult)==-1)
                {
                    statusCode = 0;
                    Toast.makeText(showketquatl.this, "Lưu kết quả thất bại", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(showketquatl.this, "Lưu kết quả thành công", Toast.LENGTH_SHORT).show();
                }

                //cap màn hình làm minh chứng
                scrollView.measure(View.MeasureSpec.makeMeasureSpec(scrollView.getWidth(),
                        View.MeasureSpec.EXACTLY), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                scrollView.layout(0, 0, scrollView.getMeasuredWidth(),
                        scrollView.getMeasuredHeight());
                Bitmap bitmap = Bitmap.createBitmap(scrollView.getWidth(), scrollView.getHeight(), Bitmap.Config.ARGB_8888);
                // Tạo một Canvas từ Bitmap
                // và vẽ nội dung của ScrollView lên Bitmap
                scrollView.draw(new Canvas(bitmap));
                statusCode = 1;
                setResult(statusCode);
                File downloadsDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                File file = new File(downloadsDirectory, "screeen1.png");
                System.out.println("======="+file.getAbsolutePath());
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    fos.flush();
                } catch (IOException e) {
                    statusCode = 0;
                    e.printStackTrace();
                }
            }
        });

    }

    private static class GraderAsyncTask extends AsyncTask<Void, Void, Bitmap> {
        private WeakReference<showketquatl> activityRef;
        private GetShortAnswer getShortAnswer;
        private Bitmap bitmap;
        private String makithi;
        private String username;


        public GraderAsyncTask(showketquatl activity, GetShortAnswer getShortAnswer, Bitmap bitmap, String makithi, String username) {
            this.activityRef = new WeakReference<>(activity);
            this.getShortAnswer = getShortAnswer;
            this.bitmap = bitmap;
            this.makithi = makithi;
            this.username = username;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            return getShortAnswer.getShortAnwer(db, bitmap, makithi, username, "001");
        }

        @SuppressLint("WrongThread")
        @Override
        protected void onPostExecute(Bitmap result) {


                showketquatl activity = activityRef.get();
                if (activity != null) {
                    try {
                        Thread.sleep(1000); // Đợi thêm 2 giây
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("-----------count---" + this.getShortAnswer.getCount());
                    getShortAnswer.getListStudentAns().removeIf(item -> item == "" || item.isEmpty());
                    getShortAnswer.getListCorrectAns().removeIf(item -> item == "" || item.isEmpty());
                    getShortAnswer.getListResult().removeIf(item -> item == "" || item.isEmpty());
//                System.out.println("---------list ans--------"+getShortAnswer.getListStudentAns());
//                System.out.println("---------list correct--------"+getShortAnswer.getListCorrectAns());
//                System.out.println("---------list result--------"+getShortAnswer.getListResult());

                    // Cập nhật text scan được từ học sinh
                    try{
                        activity.txtans1.setText(getShortAnswer.getListStudentAns().get(0));
                    }catch (Exception e){
                        activity.txtans1.setText("Không thể xử lý!");
                    }

                    try{
                        activity.txtans2.setText(getShortAnswer.getListStudentAns().get(1));
                    }catch (Exception e){
                        activity.txtans2.setText("Không thể xử lý!");
                    }

                    try{
                        activity.txtans3.setText(getShortAnswer.getListStudentAns().get(2));
                    }catch (Exception e){
                        activity.txtans3.setText("Không thể xử lý!");
                    }

                    try{
                        activity.txtans4.setText(getShortAnswer.getListStudentAns().get(3));
                    }catch (Exception e){
                        activity.txtans4.setText("Không thể xử lý!");
                    }
                    try{
                        activity.txtans5.setText(getShortAnswer.getListStudentAns().get(4));
                    }catch (Exception e){
                        activity.txtans5.setText("Không thể xử lý!");
                    }


                    //Cập nhật kết quả chấm tự động
                    try {
                        activity.edtkq1.setText( getShortAnswer.getListResult().get(0));
                    } catch (Exception  e){
                        activity.edtkq1.setText("0");
                    }
                    try {
                        activity.edtkq2.setText( getShortAnswer.getListResult().get(1));
                    } catch (Exception  e){
                        activity.edtkq1.setText("0");
                    }
                    try {
                        activity.edtkq3.setText( getShortAnswer.getListResult().get(2));
                    } catch (Exception  e){
                        activity.edtkq1.setText("0");
                    }
                    try {
                        activity.edtkq4.setText( getShortAnswer.getListResult().get(3));
                    } catch (Exception  e){
                        activity.edtkq1.setText("0");
                    }

                    try {
                        activity.edtkq5.setText( getShortAnswer.getListResult().get(4));
                    }catch (Exception e){
                        activity.edtkq5.setText( "0");
                    }

                    //cập nhật kết quả từ đatabase
                    activity.txtdapan1.setText("Đáp án:"+getShortAnswer.getListCorrectAns().get(0));
                    activity.txtdapan2.setText("Đáp án:"+getShortAnswer.getListCorrectAns().get(1));
                    activity.txtdapan3.setText("Đáp án:"+getShortAnswer.getListCorrectAns().get(2));
                    activity.txtdapan4.setText("Đáp án:"+getShortAnswer.getListCorrectAns().get(3));
                    try{
                        activity.txtdapan5.setText(getShortAnswer.getListCorrectAns().get(4));
                    }catch (Exception e){
                        activity.txtdapan5.setText("Không thể xử lý!");

                    }
                    //Cập nhật ảnh minh chứng
                    List<Bitmap> bitmapList = getShortAnswer.getListImg();
                    activity.imgv1.setImageBitmap(bitmapList.get(0));
                    activity.imgv2.setImageBitmap(bitmapList.get(1));
                    activity.imgv3.setImageBitmap(bitmapList.get(2));
                    activity.imgv4.setImageBitmap(bitmapList.get(3));
                    activity.imgv5.setImageBitmap(bitmapList.get(4));



                    System.out.println("---------chữ được lấy ra-2--" + getShortAnswer.getListStudentAns().get(1));
                    System.out.println("---------chữ được lấy ra--3-" + getShortAnswer.getListStudentAns().get(2));
                    System.out.println("---------chữ được lấy ra--4-" + getShortAnswer.getListStudentAns().get(3));
                    try{
                        System.out.println("---------chữ được lấy ra--5-" + getShortAnswer.getListStudentAns().get(4));
                    }catch (Exception e){

                    }
                }
        }
    }



}