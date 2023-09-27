package com.example.expensetracker;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class PieChartActivity extends AppCompatActivity {
    PieChart pieChart;
    float in,out;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        pieChart=findViewById(R.id.pieChart);
        in=getIntent().getFloatExtra("IN",0);
        out=getIntent().getFloatExtra("OUT",0);
        setUpPieChart();
        loadPieChartData();


    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    void setUpPieChart()
    {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(12);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("Monthly Report");//add month
        pieChart.setCenterTextSize(24);
        pieChart.getDescription().setEnabled(false);

        Legend l=pieChart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextColor(Color.BLACK);
        l.setDrawInside(false);
        l.setEnabled(true);

    }
    void loadPieChartData()
    {

        ArrayList<PieEntry> entries=new ArrayList();
        entries.add(new PieEntry(in,"Expense In"));
        entries.add(new PieEntry(out,"Expense Out"));

        ArrayList<Integer> colors=new ArrayList<>();
        for(int color: ColorTemplate.MATERIAL_COLORS)
        {
            colors.add(color);
        }

        PieDataSet dataset=new PieDataSet(entries,"Monthly Report");//add month
        dataset.setColors(colors);

        PieData data=new PieData(dataset);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(12f);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();
    }

}