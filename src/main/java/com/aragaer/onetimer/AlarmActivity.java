// -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; -*-
// vim: et ts=4 sts=4 sw=4 syntax=java
package com.aragaer.onetimer;

import android.app.Activity;
import android.content.Context;
import android.os.*;
import android.os.PowerManager.WakeLock;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;


public class AlarmActivity extends Activity implements View.OnClickListener {

    Button button;
    Vibrator vibrator;
    WakeLock wakeLock;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);
        button = (Button) findViewById(R.id.button_stop);
        button.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
        wakeLock = powerManager.newWakeLock((PowerManager.SCREEN_BRIGHT_WAKE_LOCK
                                             | PowerManager.FULL_WAKE_LOCK
                                             | PowerManager.ACQUIRE_CAUSES_WAKEUP), "1TIMER");
        wakeLock.acquire();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    @Override public void onStart() {
        super.onStart();
        vibrator.vibrate(new long[] {0, 1000, 1000}, 0);
    }

    @Override public void onClick(View v) {
        vibrator.cancel();
        finish();
    }

    @Override public void onStop() {
        wakeLock.release();
        super.onStop();
    }
}
