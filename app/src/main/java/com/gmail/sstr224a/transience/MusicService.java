package com.gmail.sstr224a.transience;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

/**
 * Created by sanjana on 3/27/18.
 */

public class MusicService extends Service {

    private MediaPlayer player;
    private int musicChoice;


    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }

    // get user selected music option from intent extra and set appropriate data source for the mediaplayer
    public int onStartCommand(Intent intent, int flags, int startId) {
        musicChoice=intent.getIntExtra("MusicChoice",1);
        int source=0;
        switch (musicChoice) {
            case 0:
                source = R.raw.ring;
                break;
            case 1:
                source = R.raw.rain_thunder;
                break;
            case 2:
                source = R.raw.bell;
                break;
            case 3:
                source = R.raw.forest;
                break;
            case 4:
                source = R.raw.chimes;
                break;

        }

        if(player!=null){
            player.stop();
            player.release();
        }

        player = MediaPlayer.create(this, source);
        player.setLooping(true); // Set looping
        player.setVolume(1.0f, 1.0f);
        player.start();
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        player.stop();
        player.release();
    }
}
