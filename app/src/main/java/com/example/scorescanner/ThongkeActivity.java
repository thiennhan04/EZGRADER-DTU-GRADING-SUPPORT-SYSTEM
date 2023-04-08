package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ThongkeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thongke);
        try {
            Intent intent = getIntent();
            String makithi = intent.getStringExtra("kithi");
            Log.d("===== thống kê =====", makithi+"");
            DataBase db = new DataBase(this);

            BarChart barChart = findViewById(R.id.barchart);

            ArrayList<BarEntry> list = new ArrayList<>();
            list.add(new BarEntry(0.5f, 0));
            list.add(new BarEntry(1.5f, 0));
            list.add(new BarEntry(2.5f, 0));
            list.add(new BarEntry(3.5f, 0));
            list.add(new BarEntry(4.5f, 0));
            list.add(new BarEntry(5.5f, 0));
            list.add(new BarEntry(6.5f, 0));
            list.add(new BarEntry(7.5f, 0));
            list.add(new BarEntry(8.5f, 0));
            list.add(new BarEntry(9.5f, 0));
            list.add(new BarEntry(10.5f, 0));

            // truy vấn dữ liệu kì thi
            if (db.mydatabase == null) {
                Log.d("===== thống kê =====", "day");
                return;
            }

            Cursor c = db.mydatabase.rawQuery("select diemso from diem where makithi="+makithi, null);
            if (c.getCount() <= 0) {
                Log.d("===== thống kê =====", "không có thông tin để thống kê");
//                return;
            } else {
                c.moveToFirst();
                while (!c.isAfterLast()) {
                    try {
                        int tmp = (int)Float.parseFloat(c.getString(0));
                        list.get(tmp).setY(list.get(tmp).getY() + 1);
                    } catch (Exception ex) {
                        Log.d("===== thống kê - load =====", ex.getMessage() + "");
                    }
                    c.moveToNext();
                }
            }

            BarDataSet dataSet = new BarDataSet(list, "Danh sách thống kê");
            dataSet.setColors(new int[] {
                    Color.rgb(153,0,0),
                    Color.rgb(204,102,0),
                    Color.rgb(204,204,0),
                    Color.rgb(128,255,0),
                    Color.rgb(0,255,0),
                    Color.rgb(51,255,153),
                    Color.rgb(102,255,255),
                    Color.rgb(153,204,255),
                    Color.rgb(153,153,255),
                    Color.rgb(204,153,255)
            });

            dataSet.setValueTextColor(Color.BLACK);
            dataSet.setValueTextSize(16f);

            BarData data = new BarData(dataSet);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getBarLabel(BarEntry barEntry) {
                    int diem = (int)barEntry.getY();
                    return ""+(int)barEntry.getY();
                }
            });

            data.setBarWidth(0.5f);
            int max=0;
            for(int i=0; i<list.size(); i++)
                max = Math.max((int)list.get(i).getY(),max);

            XAxis xAxis = barChart.getXAxis();
            YAxis yAxisleft = barChart.getAxisLeft();
//            yAxisleft.setEnabled(false);
            YAxis yAxisright = barChart.getAxisRight();
            yAxisright.setEnabled(false);

            barChart.setFitBars(false);
            barChart.setData(data);
            barChart.getDescription().setText("");
            barChart.animateY(2000);
        }
        catch (Exception ex)
        {
            Log.d("===== thống kê - lỗi lớn=====", ex.toString()+"");
        }
    }
}