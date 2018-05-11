// -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; -*-
// vim: et ts=4 sts=4 sw=4 syntax=java
package com.aragaer.onetimer;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.*;


public class HelloActivity extends Activity implements View.OnClickListener,
                                                       AdapterView.OnItemSelectedListener {

    public long alarmEnd = -1;
    public int timerSelected = 0;

    private final static long TIMER_LENS[] = {60*1000, 5*60*1000};
    private long pomodoro_len = 60*1000;
    private final static int ALARM_ID = 1;

    Button button;
    TextView text;
    ArrayAdapter<CharSequence> adapter;
    Spinner spinner;
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
        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        PendingIntent pi = PendingIntent.getActivity(this, ALARM_ID, intent, 0);
        if (isRunning()) {
            am.cancel(pi);
            Log.d("1TIMER", "Countdown stopped");
            stopTimer();
        } else {
            alarmEnd = System.currentTimeMillis() + pomodoro_len;
            Log.d("1TIMER", "Starting alarm");
            am.setExact(AlarmManager.RTC_WAKEUP, alarmEnd, pi);
            Log.d("1TIMER", "Countdown started");
            timer = new MyCountDown(this, alarmEnd);
            timer.start();
            startTimer();
        }
    }

    @Override public void onItemSelected(AdapterView<?> parent, View view,
                                         int pos, long id) {
        timerSelected = pos;
        pomodoro_len = TIMER_LENS[timerSelected];
    }

    @Override public void onNothingSelected(AdapterView<?> parent) {
    }

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
        button = (Button) findViewById(R.id.button_start);
        text = (TextView) findViewById(R.id.text_timer);

        spinner = (Spinner) findViewById(R.id.timer_select);
        // Create an ArrayAdapter using the string array and a default spinner layout
        adapter = ArrayAdapter.createFromResource(this,
                                                  R.array.timers,
                                                  android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        if (savedInstanceState == null) {
            button.setText("Start");
            text.setText("Ready");
        } else
            onRestoreInstanceState(savedInstanceState);
    }

    @Override public void onStart() {
        super.onStart();
        button.setOnClickListener(this);
        spinner.setOnItemSelectedListener(this);
    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("alarmEnd", alarmEnd);
        outState.putInt("timerSelected", timerSelected);
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
        timerSelected = inState.getInt("timerSelected", 0);
    }
}
