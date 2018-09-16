package com.gmail.sstr224a.transience;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MusicActivity extends AppCompatActivity {
    private Intent intent;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
    }

    //start music service for user selected option
    public void startMusic(View view){
        intent = new Intent(this, MusicService.class);
        switch (view.getId()) {
            case R.id.water:
                intent.putExtra("MusicChoice",0);
            break;
            case R.id.rain:
                intent.putExtra("MusicChoice",1);
             break;
            case R.id.bell:
                intent.putExtra("MusicChoice",2);
             break;
            case R.id.birds:
                intent.putExtra("MusicChoice",3);
                break;
            case R.id.thunder:
                intent.putExtra("MusicChoice",4);
                break;


        }
        startService(intent);
    }



}
