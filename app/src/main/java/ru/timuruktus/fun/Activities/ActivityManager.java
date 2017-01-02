package ru.timuruktus.fun.Activities;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.util.Log;


public class ActivityManager extends Activity{

    private String path = "settings";
    private String city = "city";
    public final static int ACTIVITY_ONE = 1;
    public final static int ACTIVITY_TWO = 2;
    Context context;
    private final String LOG_TAG = "myLogs";
    private static int recentActivity = 0;
    SharedPreferences sPref;
    SharedPreferences.Editor sEditor;


    /**
     * Creates a SharedPreferences class
     * and a SharedPreferences.Editor class
     * @param context
     */
    public ActivityManager(Context context){
        this.context = context;
        sPref = this.context.getSharedPreferences(path, MODE_PRIVATE);
        sEditor = sPref.edit();

    }

    /**
     * Load first application activity
     */
    public void loadFirstActivity(){
        changeActivity(sPref.getInt(city, 0));
    }


    /**
     * Sets the first application activity
     * @param activity
     */
    public void setFirstActivity(int activity){
        sEditor.putInt(city, activity);
        sEditor.commit();
    }


    /**
     * Changes current activity to
     * @param activity
     *
     */
    public void changeActivity(int activity){
        Intent intent = new Intent(context, CityChooseActivity.class);
        recentActivity = 0;
        if(activity == ACTIVITY_ONE){
            recentActivity = ACTIVITY_ONE;
            intent = new Intent(context, PlaceChooseActivity.class);
        } else if(activity == ACTIVITY_TWO){
            recentActivity = ACTIVITY_TWO;
            intent = new Intent(context, PlaceActivity.class);
        }
        context.startActivity(intent);
    }

    public static int getRecentActivity(){
        return recentActivity;
    }

    public static void sendMessageToRecentActivity(Message msg){
        switch (getRecentActivity()){
            case 0:{
                CityChooseActivity.h.sendMessage(msg);
                break;
            }
            case 1:{
                PlaceChooseActivity.h.sendMessage(msg);
                break;
            }
            case 2:{
                PlaceActivity.h.sendMessage(msg);
                break;
            }
        }
    }



}
