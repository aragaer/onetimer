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

    private final static long POMODORO_LEN = 25*60*1000;
    Button button;
    TextView text;
    CountDownTimer timer = new CountDownTimer(POMODORO_LEN, 1000) {
            @Override public void onTick(long millisUntilFinished) {
                Log.d("1TIMER", "ticks left " + millisUntilFinished);
                text.setText(format(millisUntilFinished));
            }

            @Override public void onFinish() {
                Log.d("1TIMER", "Countdown done");
                HelloActivity.this.stopTimer();
            }
        };
    boolean isRunning;

    private void stopTimer() {
        isRunning = false;
        button.setText("Start");
        text.setText("Ready");
    }

    private void startTimer() {
        isRunning = true;
        button.setText("Stop");
        text.setText(format(POMODORO_LEN));
    }

    private String format(long ms) {
        long s = ms/1000;
        long m = s/60;
        return String.format("%02d:%02d", m, s%60);
    }

    @Override public void onClick(View v) {
        Button b = (Button) v;
        if (isRunning) {
            Log.d("1TIMER", "Countdown stopped");
            timer.cancel();
            stopTimer();
        } else {
            Log.d("1TIMER", "Countdown started");
            timer.start();
            startTimer();
        }
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
        button = (Button) findViewById(R.id.button_start);
        button.setText("Start");
        text = (TextView) findViewById(R.id.text_timer);
        text.setText("Ready");
    }

    @Override public void onStart() {
        super.onStart();
        button.setOnClickListener(this);
    }

}
