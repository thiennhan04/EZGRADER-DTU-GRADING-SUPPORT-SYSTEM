package com.example.scorescanner;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    LineChart lineChart;
    BarChart barChart;
    ImageButton back;
    Button changeType;
    int status = 1;
    String makithi, username;
    DataBase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        Intent intent = getIntent();
        makithi = intent.getStringExtra("makithi");
        username =  intent.getStringExtra("username");

        lineChart = findViewById(R.id.myline);
        barChart = findViewById(R.id.mybar);

        back = findViewById(R.id.back_btnds);
        changeType = findViewById(R.id.changeTypeChart);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status++;
                status %= 2;
                if(status == 0){
                    changeType.setText("Line");
                }
                else if(status == 1){
                    changeType.setText("Bar");
                }
                drawChart();
            }
        });

        drawChart();
    }

    private void drawChart(){
        try {
            List<BarEntry> entries = getData();
            if(entries==null){
                finish();
            }
            BarDataSet barDataSet = new BarDataSet(entries, "Dữ liệu thống kê");
            int[] colors = new int[] {
                    Color.BLACK,
                    Color.RED,
                    Color.BLUE,
                    Color.GREEN,
                    Color.YELLOW,
                    Color.MAGENTA,
                    Color.CYAN,
                    Color.GRAY,
                    Color.DKGRAY,
                    Color.LTGRAY,
                    Color.parseColor("#FF5733")
            };
            String[] labels = new String[]{
                    "0-1","1-2","2-3","3-4","4-5","5-6","6-7","7-8","8-9","9-10","10"
            };

            barDataSet.setColors(colors);
            barDataSet.setValueFormatter(new IntegerValueFormatter());
            BarData barData = new BarData(barDataSet);

            List<LegendEntry> legendEntries = new ArrayList<>();

            for (int i = 0; i < labels.length; i++) {
                LegendEntry entry = new LegendEntry();
                entry.formColor = colors[i];
                entry.label = labels[i];
                legendEntries.add(entry);
            }

            float maxValue = Float.MIN_VALUE;
            for (BarEntry entry : entries) {
                float value = entry.getY();
                if (value > maxValue) {
                    maxValue = value;
                }
            }

            Legend legend = barChart.getLegend();
            legend.setCustom(legendEntries);
            legend.setWordWrapEnabled(true);
            legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            legend.setDrawInside(false);
            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setDrawLabels(false);
            barChart.setDrawValueAboveBar(false);
            barChart.getXAxis().setAxisMaximum(12);
            barChart.getAxisLeft().setAxisMaximum(maxValue+2);
            barChart.getAxisRight().setAxisMaximum(maxValue+2);
            barChart.setData(barData);

        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private List<BarEntry> getData() {
        List<BarEntry> result = new ArrayList<>();
        db = new DataBase(this);
        Cursor c = db.mydatabase.rawQuery("select diemso from diem where makithi = "+makithi, null);
        if(c.getCount()==0){
            Toast.makeText(this, "Có gì mà thống kê", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
        int[] diem = new int[11];
        c.moveToFirst();
        while (!c.isAfterLast()) {
            diem[Integer.parseInt(c.getString(0))]++;
            c.moveToNext();
        }
        for(int i=0; i<=10; i++){
            result.add(new BarEntry(i+1,diem[i]));
        }

        return result;
    }

    public class IntegerValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            // Định dạng giá trị thành số nguyên
            return String.valueOf((int) value);
        }
    }
}