package ru.timuruktus.fun.Activities;


import android.app.Activity;
import android.os.Bundle;
import android.util.Log;


public class Starter extends Activity {
    ActivityManager activityManager;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myLogs", "Joining Starter Class");
        activityManager = new ActivityManager(this);
        activityManager.loadFirstActivity();

    }
}
