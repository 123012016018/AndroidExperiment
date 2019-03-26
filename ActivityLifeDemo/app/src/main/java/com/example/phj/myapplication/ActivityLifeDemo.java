package com.example.phj.myapplication;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * Created by phj on 19-3-20.
 */

public class ActivityLifeDemo extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityLifeDemo", "call onCreate()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("ActivityLifeDemo", "call onStart()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("ActivityLifeDemo", "call onRestart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ActivityLifeDemo", "call onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("ActivityLifeDemo", "call onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("ActivityLifeDemo", "call onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.i("ActivityLifeDemo", "call onDestroy()");
    }
}
