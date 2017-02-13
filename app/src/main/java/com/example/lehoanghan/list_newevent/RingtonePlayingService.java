package com.example.lehoanghan.list_newevent;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.lehoanghan.appcalendar.R;

/**
 * Created by lehoanghan on 6/12/2016.
 */
public class RingtonePlayingService extends Service {

    MediaPlayer mediaPlayer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String typefromAlarm = intent.getStringExtra("type");
        if (typefromAlarm != null) {
            if (typefromAlarm.compareTo("OK") == 0) {
                mediaPlayer.stop();
                mediaPlayer.reset();
            } else {
                mediaPlayer = MediaPlayer.create(this, R.raw.de);
                mediaPlayer.start();
            }
        }
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

    }
}
