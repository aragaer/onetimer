// -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; -*-
// vim: et ts=4 sts=4 sw=4 syntax=java
package com.aragaer.onetimer;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class HelloActivity extends Activity implements View.OnClickListener {

    public long alarmEnd = -1;

    private final static long POMODORO_LEN = 60*1000;
    Button button;
    TextView text;
    CountDownTimer timer;
    private static class MyCountDown extends CountDownTimer {

        private final HelloActivity _activity;

        MyCountDown(HelloActivity activity, long alarmEnd) {
            super(alarmEnd - System.currentTimeMillis(), 250);
            _activity = activity;

        }

        @Override public void onTick(long millisUntilFinished) {
            Log.d("1TIMER", "ticks left " + millisUntilFinished);
            _activity.setTimerText(millisUntilFinished);
        }

        @Override public void onFinish() {
            Log.d("1TIMER", "Countdown done");
            _activity.stopTimer();
        }
    }

    private boolean isRunning() {
        return alarmEnd != -1;
    }

    void setTimerText(long millis) {
        text.setText(format(millis));
    }

    private void stopTimer() {
        button.setText("Start");
        text.setText("Ready");
        alarmEnd = -1;
        timer.cancel();
        timer = null;
    }

    private void startTimer() {
        button.setText("Stop");
        text.setText(format(alarmEnd-System.currentTimeMillis()));
    }

    private String format(long ms) {
        long s = ms/1000;
        long m = s/60;
        return String.format("%02d:%02d", m, s%60);
    }

    @Override public void onClick(View v) {
        Button b = (Button) v;
        if (isRunning()) {
            Log.d("1TIMER", "Countdown stopped");
            stopTimer();
        } else {
            Log.d("1TIMER", "Countdown started");
            alarmEnd = System.currentTimeMillis() + POMODORO_LEN;
            timer = new MyCountDown(this, alarmEnd);
            timer.start();
            startTimer();
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
        button = (Button) findViewById(R.id.button_start);
        text = (TextView) findViewById(R.id.text_timer);
        if (savedInstanceState == null) {
            button.setText("Start");
            text.setText("Ready");
        } else
            onRestoreInstanceState(savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        button.setOnClickListener(this);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("alarmEnd", alarmEnd);
        Log.d("1TIMER", "Saved alarmEnd="+ alarmEnd);
    }

    @Override public void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
        alarmEnd = inState.getLong("alarmEnd", -1);
        Log.d("1TIMER", "Restored alarmEnd="+ alarmEnd);
        if (alarmEnd <= System.currentTimeMillis())
            alarmEnd = -1;
        if (alarmEnd == -1) {
            if (timer != null) {
                timer.cancel();
                timer = null;
            }
            button.setText("Start");
            text.setText("Ready");
        } else {
            if (timer == null) {
                timer = new MyCountDown(this, alarmEnd);
                timer.start();
            }
            startTimer();
        }
    }
}
