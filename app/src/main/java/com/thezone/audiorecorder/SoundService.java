package com.thezone.audiorecorder;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class SoundService extends Service{
    public static final String ACTION_START = "com.thezone.audiorecorder.action.START";
    public static final String ACTION_STOP = "com.thezone.audiorecorder.action.STOP";
    public static String nametimer="1";

    private static final long DURATION = 4000; // 10 seconds

    private MediaPlayer signalPlayer;
    private Handler handler;
    private Runnable stopPlaybackRunnable;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                startPlayback();
            } else if (ACTION_STOP.equals(action)) {
                stopPlayback();
                stopSelf();
            }
        }
        return START_STICKY;
    }

    private void startPlayback() {
        if (signalPlayer == null) {
            signalPlayer = MediaPlayer.create(this, R.raw.signal);
            signalPlayer.setLooping(true);
        }
        signalPlayer.start();

        // Запуск таймера для остановки воспроизведения звука через 10 секунд
        handler = new Handler();
        stopPlaybackRunnable = new Runnable() {
            @Override
            public void run() {
                stopPlayback();
                showToast();
            }
        };
        handler.postDelayed(stopPlaybackRunnable, DURATION);
    }

    private void stopPlayback() {
        if (signalPlayer != null) {
            signalPlayer.stop();
            signalPlayer.release();
            signalPlayer = null;
        }
        // Отмена таймера остановки воспроизведения звука
        if (handler != null && stopPlaybackRunnable != null) {
            handler.removeCallbacks(stopPlaybackRunnable);
        }
    }

    private void showToast() {
        // Отображение Toast с названием таймера
        String timerName = "Название таймера"; // Замените на фактическое название таймера
        Toast.makeText(this, "Таймер \"" +nametimer+ "\" завершен", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopPlayback();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
