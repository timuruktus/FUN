package ru.timuruktus.fun.Activities;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;


public class Starter extends Activity {
    ActivityManager activityManager;
    public static boolean hasNetworkConenction = false;
    public static String LOG_TAG = "myLogs";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myLogs", "Joining Starter Class");
        Starter.hasNetworkConenction = checkConnection(Starter.this);
        activityManager = new ActivityManager(this);
        activityManager.loadFirstActivity();

    }

    public static boolean checkConnection(final Context context)
    {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }
}
