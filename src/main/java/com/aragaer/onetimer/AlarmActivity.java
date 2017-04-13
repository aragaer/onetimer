// -*- Mode: Java; tab-width: 4; indent-tabs-mode: nil; -*-
// vim: et ts=4 sts=4 sw=4 syntax=java
package com.aragaer.onetimer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.widget.Button;


public class AlarmActivity extends Activity implements View.OnClickListener {

    Button button;
    Vibrator vibrator;

    @Override public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_layout);
        button = (Button) findViewById(R.id.button_stop);
        button.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override public void onStart() {
        super.onStart();
        vibrator.vibrate(new long[] {0, 450, 50}, 0);
    }

    @Override public void onClick(View v) {
        vibrator.cancel();
        finish();
    }

}
