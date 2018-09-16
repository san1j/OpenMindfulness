package com.gmail.sstr224a.transience;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.support.design.widget.NavigationView;

import com.getkeepsafe.taptargetview.TapTarget;
import com.getkeepsafe.taptargetview.TapTargetSequence;
import com.white.progressview.CircleProgressView;


public class MainActivity extends AppCompatActivity implements timerDialogFragment.Listener, GraphFragment.Listener,
        NavigationView.OnNavigationItemSelectedListener {


    private TextView textView;
    private ProgressBar posHorizontalBar;
    private ProgressBar negHorizontalBar;
    private TextView posTextView, negTextView, transience;
    private long durationLeft;
    private double counter;
    private double happyCounter;
    private double sadCounter;
    private CountDownTimer countDownTimer;
    private long elapsedTime;
    private long avgTransience = 0;
    private boolean isRunning;
    private CircleProgressView progressBar;
    private DrawerLayout drawer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //display intro if the app is opened for the first time
        if (TutorialClass.isFirst(this)) {
            Intent intent = new Intent(this, IntroActivity.class);
            startActivity(intent);
        }
        setContentView(R.layout.activity_main);

        // display timerDialog only when the activity is first created
        if (savedInstanceState == null) {
            new timerDialogFragment().show(getFragmentManager(), "dialog");
        }


        posHorizontalBar = findViewById(R.id.horizontalBar1);
        negHorizontalBar = findViewById(R.id.horizontalBar2);

        posTextView = findViewById(R.id.textView_Pos);
        negTextView = findViewById(R.id.textView_Neg);
        transience = findViewById(R.id.transience);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //if device is a tablet, don't display burger icon
        if(!getResources().getBoolean(R.bool.isTablet)) {
            drawer = findViewById(R.id.drawer_layout);
            ActionBar actionbar = getSupportActionBar();
            actionbar.setDisplayHomeAsUpEnabled(true);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.nav_open_drawer,
                    R.string.nav_close_drawer);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
        }


        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //on rotation, re-populate fields with saved values
        if (savedInstanceState != null) {

            durationLeft = savedInstanceState.getLong("duration");
            selectedDuration(durationLeft);
            double[] counters = savedInstanceState.getDoubleArray("counters");
            elapsedTime = savedInstanceState.getLong("elapsedTime");
            transience.setText(elapsedTime + " ms");

            if (counters != null) {
                posHorizontalBar.setMax((int) counters[1]);
                posHorizontalBar.incrementProgressBy((int) counters[0]);
                negHorizontalBar.setMax((int) counters[1]);
                negHorizontalBar.incrementProgressBy((int) counters[2]);

                posTextView.setText((int) (counters[0] / counters[1] * 100) + "%");
                negTextView.setText((int) (counters[2] / counters[1] * 100) + "%");
            }

        }

    }

    // begin a timer with the user selected duration
    @Override
    public void selectedDuration(final long duration) {

        progressBar = findViewById(R.id.progressBar);
        progressBar.setProgressInTime(0, 100, duration);

        countDownTimer = new CountDownTimer(duration, 1000) {

            public void onTick(long millisUntilFinished) {
                textView = findViewById(R.id.textView);
                if (duration > 60000) {
                    textView.setText((" " + (millisUntilFinished / 1000) / 60) + " min "
                            + "\n" + (millisUntilFinished) / 1000 % 60 + " sec");
                } else {
                    textView.setText((millisUntilFinished) / 1000 % 60 + " sec");
                }
                durationLeft = millisUntilFinished;

            }

            public void onFinish() {
                textView.setText(R.string.done);
                Intent intent = new Intent(MainActivity.this, ResultsActivity.class);
                double[] counters = {happyCounter, counter, sadCounter};
                intent.putExtra("counters", counters);
                intent.putExtra("avgTransience", avgTransience);
                startActivity(intent);

            }
        }.start();

    }


    @Override
    public void updateProgressBar(long elapsedTime, double counter, double emotionCounter, boolean emotion) {
        this.counter = counter;
        this.elapsedTime = elapsedTime;
        calculateTransience();

        if (emotion) {
            this.happyCounter = emotionCounter;
            posHorizontalBar.setProgress((int) emotionCounter);

        } else {
            transience.setText(elapsedTime + " ms");
            this.sadCounter = emotionCounter;
            negHorizontalBar.setProgress((int) emotionCounter);
        }

        posHorizontalBar.setMax((int) counter);
        negHorizontalBar.setMax((int) counter);

        posTextView.setText((int) ((happyCounter / counter) * 100) + " %");
        negTextView.setText((int) ((sadCounter / counter) * 100) + " %");

        if (this.counter == 5) {
            if (TutorialClass.isFirst(this)) {
                displayTutorial();
            }
        }


    }

    private void calculateTransience() {
        avgTransience += elapsedTime / 2;

    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putLong("duration", durationLeft);
        double[] counters = {happyCounter, counter, sadCounter};
        savedInstanceState.putDoubleArray("counters", counters);
        savedInstanceState.putLong("elapsedTime", elapsedTime);
    }

    @Override
    protected void onPause() {
        super.onPause();
        pauseTimer(findViewById(R.id.pausePlay));

    }

    @Override
    protected void onResume() {
        super.onResume();
        isRunning = true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
         stopService(new Intent(this, MusicService.class));
        //change the value to false after the first time
        if(TutorialClass.isFirst(this)){
           getSharedPreferences("shared_pref",MODE_PRIVATE).edit().putBoolean("is_first",false).apply();
        }
    }

    public void pauseTimer(View view) {

        ImageView imageView = findViewById(R.id.pausePlay);
        if (countDownTimer != null) {
            if (isRunning) {
                countDownTimer.cancel();
                progressBar.setVisibility(View.INVISIBLE);
                imageView.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                isRunning = false;
            } else {
                progressBar.setVisibility(View.VISIBLE);
                selectedDuration(durationLeft);
                imageView.setImageResource(R.drawable.ic_pause_black_24dp);
                isRunning = true;
            }
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Intent intent = null;
        switch (item.getItemId()) {

            case R.id.recent_stats:
                intent = new Intent(MainActivity.this, ResultsActivity.class);
                double[] counters = {happyCounter, counter, sadCounter};
                intent.putExtra("counters", counters);
                intent.putExtra("avgTransience", avgTransience);
                break;
            case R.id.reminder:
                intent = new Intent(MainActivity.this, RemindActivity.class);
                break;
            case R.id.music:
                intent = new Intent(MainActivity.this, MusicActivity.class);
                break;

            case R.id.share:
                intent = new Intent(android.content.Intent.ACTION_SEND);
                intent.setType("text/plain");
                String shareBody = "Check out Transience on Google Play!";
                intent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Check out Transience!");
                intent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                startActivity(Intent.createChooser(intent, "Share via"));
                break;

            case R.id.About:
                intent = new Intent(MainActivity.this, AboutActivity.class);
                break;

        }
        startActivity(intent);
        return true;
    }

    public void displayTutorial() {
        if (TutorialClass.isFirst(this)) {
            new TapTargetSequence(this)
                    .targets(
                            TapTarget.forView(findViewById(R.id.transience), "You're doing great! Your transience score describes how long you experience a single emotion")
                                    .outerCircleColor(R.color.blue)      // Specify a color for the outer circle
                                    .outerCircleAlpha(0.96f)// Specify the alpha amount for the outer circle
                                    .targetRadius(65)
                                    .textColor(R.color.black)
                                    .transparentTarget(true)
                                    .targetCircleColor(R.color.dark_grey)).start();
        }


    }


}
