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
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.opencv.core.Mat;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ThongKeActivity extends AppCompatActivity {

    PieChart pieChart;
    BarChart barChart;
    ImageButton back;
    Button changeType;
    int status = 0;
    String makithi, username;
    DataBase db;
    TextView tong,txttb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thong_ke);
        Intent intent = getIntent();
        makithi = intent.getStringExtra("makithi");
        username =  intent.getStringExtra("username");
        db = new DataBase(this);

        pieChart = findViewById(R.id.pieChart);
        barChart = findViewById(R.id.mybar);

        back = findViewById(R.id.back_btnds);
        changeType = findViewById(R.id.changeTypeChart);

        tong = findViewById(R.id.txtTongBai);
        txttb = findViewById(R.id.txtTB);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(status == 0){
            changeType.setText("Line");
            pieChart.setVisibility(View.INVISIBLE);
            barChart.setVisibility(View.VISIBLE);
            drawChart();
        }
        else if(status == 1){
            changeType.setText("Bar");
            barChart.setVisibility(View.INVISIBLE);
            pieChart.setVisibility(View.VISIBLE);
            drawPieChart();
        }

        changeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                status++;
                status %= 2;
                if(status == 0){
                    changeType.setText("Line");
                    pieChart.setVisibility(View.INVISIBLE);
                    barChart.setVisibility(View.VISIBLE);
                    drawChart();
                }
                else if(status == 1){
                    changeType.setText("Bar");
                    barChart.setVisibility(View.INVISIBLE);
                    pieChart.setVisibility(View.VISIBLE);
                    drawPieChart();
                }
            }
        });
    }
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

    DecimalFormat decimalFormat = new DecimalFormat("#.##");

    private void drawChart(){
        try {
            List<BarEntry> entries = getData();
            if(entries==null){
                finish();
            }
            BarDataSet barDataSet = new BarDataSet(entries, "Dữ liệu thống kê");


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
            barChart.getAxisLeft().setValueFormatter(new IntegerValueFormatter());
            barChart.getAxisRight().setEnabled(false);
            barChart.getDescription().setEnabled(false);
            barChart.getXAxis().setDrawLabels(false);

            barChart.getAxisLeft().setAxisMinimum(0f);
            barChart.getAxisLeft().setAxisMaximum(maxValue+1);
            barChart.getAxisLeft().setLabelCount(2+(int)maxValue, true);

            barChart.getXAxis().setAxisMaximum(12);
            barChart.setData(barData);
            barChart.animateY(1000);
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    private List<BarEntry> getData() {
        List<BarEntry> result = new ArrayList<>();
        Cursor c = db.mydatabase.rawQuery("select diemso from diem where makithi = "+makithi +" and hinhanh is not null", null);
        if(c.getCount()==0){
            Toast.makeText(this, "Có gì mà thống kê", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
        int[] diem = new int[11];
        c.moveToFirst();
        double sum = 0;
        while (!c.isAfterLast()) {
            double diemdouble = c.getDouble(0);
            sum += diemdouble;
            int diemgoc = (int)diemdouble;
            diem[diemgoc]++;
            c.moveToNext();
        }
        for(int i=0; i<=10; i++){
            result.add(new BarEntry(i+1,diem[i]));
        }

        tong.setText("Tổng số bài: "+c.getCount());
        txttb.setText("Điểm trung bình: "+(decimalFormat.format(sum/c.getCount())));

        return result;
    }

    public class IntegerValueFormatter extends ValueFormatter {
        @Override
        public String getFormattedValue(float value) {
            return String.valueOf((int) value);
        }
    }

    public void drawPieChart() {
        Object[] arr = getPieData();
        ArrayList<PieEntry> entries = (ArrayList<PieEntry>)arr[0];

        PieDataSet dataSet = new PieDataSet(entries,"");
        dataSet.setColors((ArrayList<Integer>)arr[1]);

        PieData pieData = new PieData(dataSet);

        pieChart.setData(pieData);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.setExtraOffsets(5, 10, 5, 5);
        pieChart.setHoleRadius(0);
        pieChart.animateY(1000);

        Legend legend = pieChart.getLegend();
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        legend.setOrientation(Legend.LegendOrientation.HORIZONTAL);

        legend.setDrawInside(false);
        legend.setEnabled(true);
    }

    public Object[] getPieData() {
        ArrayList<PieEntry> entries = new ArrayList<>();
        Cursor c = db.mydatabase.rawQuery("select diemso from diem where makithi = "+makithi+" and hinhanh is not null", null);
        if(c.getCount()==0){
            Toast.makeText(this, "Có gì mà thống kê", Toast.LENGTH_SHORT).show();
            finish();
            return null;
        }
        int[] diem = new int[11];
        c.moveToFirst();
        double sum = 0;
        while (!c.isAfterLast()) {
            double diemdouble = c.getDouble(0);
            sum += diemdouble;
            int diemgoc = (int)diemdouble;
            diem[diemgoc]++;
            c.moveToNext();
        }
        List<Integer> tmpColors = new ArrayList<>();
        for(int i=0; i<=10; i++){
            if(diem[i]>0){
                tmpColors.add(colors[i]);
                entries.add(new PieEntry(diem[i],i+"-"+(i+1)));
            }
            Log.i("TAG", "getPieData: === "+i+" "+diem[i]);
        }

        tong.setText("Tổng số bài: "+c.getCount());
        txttb.setText("Điểm trung bình: "+(decimalFormat.format(sum/c.getCount())));

        Object[] res = new Object[2];
        res[0] = entries;
        res[1] = tmpColors;
        return res;
    }
}