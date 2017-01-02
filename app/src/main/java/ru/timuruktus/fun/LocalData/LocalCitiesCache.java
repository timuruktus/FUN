package ru.timuruktus.fun.LocalData;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import ru.timuruktus.fun.Activities.AbstractActivity;
import ru.timuruktus.fun.Activities.ActivityManager;
import ru.timuruktus.fun.LocalData.Fragments.Cities;


public class LocalCitiesCache {

    DBHelper dbHelper;
    public final String LOG_TAG = "myLogs";
    public final String SETTINGS = "settings";
    public static final int CHECK_RECENCY_OF_DB = 0;
    public static final int LOAD_CITIES_FROM_DB = 1;
    private Context context;
    ContentValues cv;
    SQLiteDatabase db;
    SharedPreferences sPref;

    public LocalCitiesCache(Context context){
        this.context = context;
        dbHelper = new DBHelper(context);
        cv = new ContentValues();
        db = dbHelper.getWritableDatabase();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            if(msg.what == CHECK_RECENCY_OF_DB){
                Cities cities = (Cities) msg.obj;
                if(cities.getDBVersion() > getDataBaseVersion()) {
                    upgradeDB(cities.getCities(), cities.getDBVersion());
                }else{
                    this.sendEmptyMessage(LOAD_CITIES_FROM_DB);
                }
            } else if(msg.what == LOAD_CITIES_FROM_DB){
                Message message = handler.obtainMessage(AbstractActivity.READY,0,0,getCItiesFromDB());
                ActivityManager.sendMessageToRecentActivity(message);

            }
        }
    };

    public String[] getCItiesFromDB() {
        String[] columns = {"city"};
        String[] answer = null;
        int counter = 0;
        Cursor c = db.query("cities", columns, null, null, null, null, null);
        if (c.moveToFirst()) {
            int cityColIndex = c.getColumnIndex("city");
            do {
                answer[counter] = c.getString(cityColIndex);
                counter++;
            } while (c.moveToNext());
        }
        return answer;
    }

    /**
     * @return recent version of DB
     */
    public int getDataBaseVersion(){
        sPref = context.getSharedPreferences(SETTINGS, context.MODE_PRIVATE);
        return sPref.getInt("cityVersion", 100);
    }

    /**
     * Sets the new version of cities DB
     * @param dbVersion - new version(int)
     */
    public void setDBVersion(int dbVersion){
        sPref = context.getSharedPreferences(SETTINGS, context.MODE_PRIVATE);
        SharedPreferences.Editor ed = sPref.edit();
        ed.putInt("cityVersion", dbVersion);
    }

    /**
     * Set @var cityDBVersion to newest version
     * Clears DB of Cities
     * And than put all from @var cities[] to DB
     * @param cities - Recent array of cities
     * @param recentVersion - recent DB version
     */
    private void upgradeDB(String[] cities, int recentVersion){
        setDBVersion(recentVersion);
        db.delete("cities", null, null);
        for(int i = 0; i < cities.length; i++){
            cv.put("city", cities[i]);
            db.insert("cities", null, cv);
            cv.clear();
        }
    }

    class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, "CitiesDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(LOG_TAG, "--- onCreate database ---");
            // создаем таблицу с полями
            db.execSQL("create table cities ("
                    + "id integer primary key autoincrement,"
                    + "city text" + ");");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }

}
