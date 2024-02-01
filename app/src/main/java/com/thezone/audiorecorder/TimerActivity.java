package com.thezone.audiorecorder;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.thezone.audiorecorder.R;
import com.thezone.audiorecorder.SoundService;

import java.util.Locale;

public class TimerActivity extends AppCompatActivity {
    private EditText mEditTextInput, mEditTextSec;
    private TextView mTextViewCountDown;
    private Button mButtonSet;
    private Button mButtonStartPause;
    private Button mButtonReset;

    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;
    private EditText edittext;
    private long mStartTimeInMillis;
    private long mTimeLeftInMillis;
    private long mEndTime;
    private MediaPlayer signal;
    private AlarmManager alarmManager;
    private PendingIntent soundPendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            this.getSupportActionBar().hide();
        } catch (NullPointerException e) {
        }

        setContentView(R.layout.activity_timer);
        mEditTextInput = findViewById(R.id.edit_text_input);
        mEditTextSec = findViewById (R.id.edit_text_sec);
        mTextViewCountDown = findViewById(R.id.text_view_countdown);
        mButtonSet = findViewById(R.id.button_set);
        mButtonStartPause = findViewById(R.id.button_start_pause);
        mButtonReset = findViewById(R.id.button_reset);
        edittext = findViewById(R.id.edit_text_name);
        signal = MediaPlayer.create(this, R.raw.signal);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        mButtonSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*String input="0";
               if( mEditTextInput.getText().toString()!=null)*/
                 String input = mEditTextInput.getText().toString();
                String input1 = mEditTextSec.getText().toString();
                if ((input.length() == 0)&&(input1.length()==0)) {
                    Toast.makeText(TimerActivity.this, "Введите число минут", Toast.LENGTH_SHORT).show();
                    return;
                }
        if((input.length()!=0)&&(input1.length()!=0)) {
            long millisInput = Long.parseLong(input) * 60000;
            long millisInput1 = Long.parseLong(input1) * 1000;
                    if ((millisInput == 0)&&(millisInput1==0)) {
                        Toast.makeText(TimerActivity.this, "Введите число минут", Toast.LENGTH_SHORT).show();
                        return;
                    }
            setTime(millisInput, millisInput1);
        }
                if((input.length()!=0)&&(input1.length()==0)) {
                    long millisInput = Long.parseLong(input) * 60000;
            if(millisInput==0){
                 Toast.makeText(TimerActivity.this, "Введите число минут", Toast.LENGTH_SHORT).show();
                 return;
}
                    setTime(millisInput, 0);
                }
                if((input.length()==0)&&(input1.length()!=0)) {
                    long millisInput1 = Long.parseLong(input1) * 1000;
                    if(millisInput1==0){
                        Toast.makeText(TimerActivity.this, "Введите число секунд или минут", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    setTime(millisInput1, 0);
                }


                mEditTextInput.setText("");
                mEditTextSec.setText("");
            }
        });

        mButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning) {
                    pauseTimer();
                } else {
                    startTimer();
                }
            }
        });

        mButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
    }

    private void setTime(long milliseconds, long milliseconds2) {
        mStartTimeInMillis = milliseconds+milliseconds2;
        resetTimer();
        closeKeyboard();
    }

    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        SoundService.nametimer = edittext.getText().toString();
        // Запуск сервиса воспроизведения звука
        Intent soundIntent = new Intent(TimerActivity.this, SoundService.class);
        soundIntent.setAction(SoundService.ACTION_START);
        soundPendingIntent = PendingIntent.getService(TimerActivity.this, 0, soundIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, mEndTime, soundPendingIntent);

        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                updateWatchInterface();
            }
        }.start();

        mTimerRunning = true;
        updateWatchInterface();
    }

    private void pauseTimer() {
        mCountDownTimer.cancel();
        mTimerRunning = false;
        updateWatchInterface();

        // Остановка сервиса воспроизведения звука
        if (soundPendingIntent != null) {
            alarmManager.cancel(soundPendingIntent);
            soundPendingIntent.cancel();
        }
    }

    private void resetTimer() {
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountDownText();
        updateWatchInterface();

        // Остановка сервиса воспроизведения звука
        if (soundPendingIntent != null) {
            alarmManager.cancel(soundPendingIntent);
            soundPendingIntent.cancel();
        }
    }

    private void updateCountDownText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;

        String timeLeftFormatted;
        if (hours > 0) {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%d:%02d:%02d", hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),
                    "%02d:%02d", minutes, seconds);
        }

        mTextViewCountDown.setText(timeLeftFormatted);
    }

    private void updateWatchInterface() {
        if (mTimerRunning) {
            mEditTextInput.setVisibility(View.INVISIBLE);
            mEditTextSec.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.INVISIBLE);
            mButtonReset.setVisibility(View.INVISIBLE);
            mButtonStartPause.setText("Пауза");
        } else {
            mEditTextInput.setVisibility(View.VISIBLE);
            mEditTextSec.setVisibility(View.VISIBLE);
            mButtonSet.setVisibility(View.VISIBLE);
            mButtonStartPause.setText("Старт");

            if (mTimeLeftInMillis < 1000) {
                mButtonStartPause.setVisibility(View.INVISIBLE);
            } else {
                mButtonStartPause.setVisibility(View.VISIBLE);
            }

            if (mTimeLeftInMillis < mStartTimeInMillis) {
                mButtonReset.setVisibility(View.VISIBLE);
            } else {
                mButtonReset.setVisibility(View.INVISIBLE);
            }
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);
        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);

        updateCountDownText();
        updateWatchInterface();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountDownText();
                updateWatchInterface();
            } else {
                startTimer();
            }
        }
    }
}