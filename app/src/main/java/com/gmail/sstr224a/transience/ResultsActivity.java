package com.gmail.sstr224a.transience;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.gmail.sstr224a.transience.ScoreDatabase.MIGRATION_0_4;


public class ResultsActivity extends AppCompatActivity {
    private int happyPercent;
    private int sadPercent;
    private long avrgTransience;
    private int counter;
    private TextView transienceText, thoughtsText;
    private BarChart barChart;
    private PieDataSet pieDataSet;
    private PieData pieData;
    private PieChart pieChart;
    private BarDataSet barDataSet;
    private BarData barData;
    private static final String DATABASE_NAME = "SCORE";
    private ScoreDatabase scoreDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // display summary of previously calculated values(in MainActivity)
        displaySummary();
        // create a scoreDatabase instance and start the AsyncTask to perform read and write operations
        scoreDatabase = Room.databaseBuilder(getApplicationContext(), ScoreDatabase.class, DATABASE_NAME).addMigrations(MIGRATION_0_4).build();
        new ShowHistoryTask().execute();

        //display info on barChart for the user if app is used for the first time
        if(TutorialClass.isFirst(this)){
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.toolbar), "After you have more entries, you'll be able to scroll through the graph to track your progress over weeks")
                                    .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                                    .outerCircleAlpha(0.96f)// Specify the alpha amount for the outer circle
                                    .targetRadius(20)
                                    .textColor(R.color.black)
                                    .transparentTarget(true)
                                    .targetCircleColor(R.color.dark_grey)).start();
        }


    }

    private void displaySummary() {
        avrgTransience = getIntent().getLongExtra("avgTransience", 0);
        transienceText = findViewById(R.id.avgTransience);
        transienceText.setText(avrgTransience + " ms");

        displayPieChart();
    }

    //display pieChart with happy and sad percent
    private void displayPieChart() {
        double[] counterValues = getIntent().getDoubleArrayExtra("counters");

        happyPercent = (int) (counterValues[0] / counterValues[1] * 100);
        sadPercent = (int) ((counterValues[2] / counterValues[1]) * 100);

        thoughtsText = findViewById(R.id.thoughts);
        counter = (int) counterValues[1];
        thoughtsText.setText(counter + " ");


        pieChart = findViewById(R.id.piechart);
        ArrayList<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(happyPercent, "Happy"));
        entries.add(new PieEntry(sadPercent, "Sad"));

        pieDataSet = new PieDataSet(entries, "values");
        pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        initPieChart();

    }

    //initialize the pie chart
    private void initPieChart() {
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieChart.setUsePercentValues(true);
        pieChart.animateX(2000, Easing.EasingOption.EaseInCirc);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setTransparentCircleRadius(55f);
        pieChart.setHoleColor(Color.parseColor("#99000000"));
        pieChart.setHoleRadius(45f);
        pieData.setValueTextSize(17f);
        pieData.setValueFormatter(new PercentFormatter());
        pieData.setValueTextColor(Color.WHITE);
        pieChart.setEntryLabelColor(Color.WHITE);
        pieChart.getLegend().setEnabled(false);
        pieChart.getDescription().setEnabled(false);
    }

    //initialize bar chart
    private void initBarChart() {
        barChart.setDrawValueAboveBar(true);
        barChart.animateXY(3000,3000);
        barChart.setPinchZoom(true);
        barChart.setDragEnabled(true);
        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setEnabled(false);
        barChart.setVisibleXRangeMaximum(7);
        barChart.setDrawValueAboveBar(true);
        barChart.setDrawGridBackground(false);
        barData.setBarWidth(.07f);
        barChart.getDescription().setEnabled(false);
        barChart.getAxisLeft().setAxisMinimum(0);
        barChart.getAxisLeft().setAxisMaximum(100);
        barChart.setDrawBarShadow(true);

        barChart.getLegend().setEnabled(false);
        barData.setValueTextColor(Color.WHITE);
        barData.setValueTextSize(15);
        barDataSet.setBarShadowColor(Color.parseColor("#EF6C00"));
        barDataSet.setColor(Color.parseColor("#AD1457"));
        XAxis xAxis = barChart.getXAxis();
        xAxis.setTextSize(12);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        xAxis.setGranularity(1f);

    }

    private class ShowHistoryTask extends AsyncTask<Void, Void, Boolean> {
        userData userdata = new userData();
        List<userData> allData;
        ArrayList<BarEntry> barEntries = new ArrayList<>();

        //get a reference to the view containing the bargraph before performing operations
        @Override
        protected void onPreExecute() {
            barChart = (BarChart) findViewById(R.id.barGraph);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //scoreDatabase.userDao().deleteAll();

            //add today's happy score and date to the database

            userdata.setDate(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
            userdata.setHappyPercent(happyPercent);
            scoreDatabase.userDao().insertScore(userdata);

            //read all entries from the database
            allData = scoreDatabase.userDao().getAll();

            //save the most recent score if the date is the same
            if (allData.size() >= 2 && allData.get(allData.size() - 1).getDate() == allData.get(allData.size() - 2).getDate()) {
                scoreDatabase.userDao().deleteScore(allData.get(allData.size() - 2));
                allData.remove(allData.size() - 2);
            }

            if(allData.size()>365){
                for(int i=0;i<180;i++){
                    scoreDatabase.userDao().deleteScore(allData.get(i));
                }
            }

            //append the data to the bar graph
            for (int i = 0; i < allData.size(); i++) {
                int x = allData.get(i).getDate();
                int y = allData.get(i).getHappyPercent();
                barEntries.add(new BarEntry(x, y));

            }

            //move bargraph current X value to the last entry (today's score)
            if (allData.size() != 0) {
                barChart.moveViewToX(allData.get(allData.size() - 1).getDate());
            }

            //close db connection
            scoreDatabase.close();
            return true;
        }

        //initialize and display the bar Graph
        @Override
        protected void onPostExecute(Boolean success) {
            barDataSet = new BarDataSet(barEntries, "values");
            barData = new BarData(barDataSet);
            barChart.setData(barData);
            initBarChart();
        }
    }


    protected void onResume(){
        super.onResume();
    }

    protected void onPause(){
        super.onPause();
    }




}
