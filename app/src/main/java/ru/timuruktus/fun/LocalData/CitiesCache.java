package ru.timuruktus.fun.LocalData;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import ru.timuruktus.fun.Activities.CityChooseActivity;
import ru.timuruktus.fun.network.ServerActions;



public class CitiesCache extends Activity{


    public final String LOG_TAG = "myLogs";
    private static int DBVersion = 100;
    private static final String DB_NAME = "citiesDB";
    private static final String TABLE_NAME = "cities";
    DBHelper dbHelper;
    ContentValues cv;
    SQLiteDatabase db;
    private String[] citiesList;
    public String cities = null;
    Message msg;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Context context = this.getApplicationContext();
        dbHelper = new DBHelper(context);
        cv = new ContentValues();
        db = dbHelper.getWritableDatabase();
    }

    public CitiesCache() {

    }

    /**
     * Changes recent version of current DB
     * @param newVersion
     */
    public static void changeDBVersion(int newVersion){
        DBVersion = newVersion;
    }

    /**
     * Return recent version of current DB (int)
     * @return - int
     */
    public static int getRecentVersion(){ return DBVersion; }

    /**
     * Inner class.
     * This is a DataBase (I can't describe it somehow else)
     */
    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DB_NAME, null, 1);
            Log.d(LOG_TAG, context.toString());

        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            Log.d(LOG_TAG, "Creating a database");
            db.execSQL("create table " + TABLE_NAME + " ("
                    + "id integer primary key autoincrement,"
                    + "city text);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

    /**
     * PRIVATE
     * Return a array of available cities
     * from LOCAL DataBase
     * @return
     */
    private String[] getCitiesFromDB(){
        citiesList = null;
        Cursor c = db.query(TABLE_NAME, null, null, null, null, null, null);
        int counter = 0;
        int cityColIndex = c.getColumnIndex("city");
        c.moveToFirst();
        do {
            citiesList[counter] = c.getString(cityColIndex);
            counter++;
        } while (c.moveToNext());
        c.close();
        return citiesList;
    }

    /**
     * Uses for loading a list of cities
     * from outside's package
     * IF app has recent version
     * returns String[] from app DB
     * else ask for DB from sever
     */
    public void loadCities(){

        Thread t = new Thread() {
            @Override
            public void run() {
                ServerActions sa = new ServerActions();
                cities = sa.getCitiesList();
                if(cities != null){
                    upgradeDB(cities);
                }
                else{
                    sendCities(getCitiesFromDB());
                }
            }
        };
        t.start();
    }

    /**
     * PRIVATE
     * Uses to send a Handler message to activity
     * @param citiesList
     */
    private void sendCities(String[] citiesList){
        Arrays.sort(citiesList, new Comparator<String>() {
                    public int compare(String o1, String o2) {
                        return o1.toString().compareTo(o2.toString());
                    }
                });
        msg = CityChooseActivity.h.obtainMessage(CityChooseActivity.READY, 0,
                0, citiesList);
        CityChooseActivity.h.sendMessage(msg);
    }

    /**
     * PRIVATE
     * Changes DBVersion to recent
     * Than cleaning DB
     * Than Creating new DB with recent @param cities
     * THE FIRST ELEMENT OF String[] citiesList IS
     * A NEW NUMBER OF DBVERSION!!!
     * @param cities
     */
    private void upgradeDB(String cities){
        citiesList = null;
        changeDBVersion(Integer.valueOf(cities.split(".")[0]));
        for(int i = 1; i < cities.split(".").length; i++){
            citiesList[i - 1] = cities.split(".")[i];
        }
        db.delete(TABLE_NAME, null, null);
        for(int i = 0; i < citiesList.length; i++){
            cv.put("city", citiesList[i]);
            db.insert(TABLE_NAME, null, cv);
        }
        sendCities(citiesList);
    }

}
