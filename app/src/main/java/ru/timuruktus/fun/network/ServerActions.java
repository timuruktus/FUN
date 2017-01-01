package ru.timuruktus.fun.network;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import ru.timuruktus.fun.Activities.*;
import ru.timuruktus.fun.LocalData.CitiesCache;
import ru.timuruktus.fun.R;

public class ServerActions implements NetworkConnection {


    Socket fromServer = null;
    BufferedReader input = null;
    PrintWriter out = null;
    private String response = null;
    AbstractActivity activity;
    Context context;


    /**
     * Creates a input\output streams,
     * initialise Socket.
     * Any error calls <method>showDialog</method>
     * in <class>activity</class>.
     */
    @Override
    public void initServerConnection(){
        try {
            fromServer = new Socket(TEXT_DB_ADDRESS, TEXT_DB_PORT);
            Log.d("mylog", "1");
            input = new BufferedReader(new InputStreamReader(fromServer.getInputStream()));
            out = new PrintWriter(fromServer.getOutputStream(), true);
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /**
     * Closes all streams (input & out)
     * and set Socket fromServer = null;
     */
    public void closeStreams(){
        fromServer = null;
        try {
            input.close();
            out.close();
        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    public String getCitiesList(){
        CityTask ct = new CityTask();
        ct.execute();
        try {
            return ct.get();
        }catch (Exception ex){
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * returns a list of cities
     */
    class CityTask extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            CityChooseActivity.h.sendEmptyMessage(CityChooseActivity.WAITING);
        }

        @Override
        protected String doInBackground(Void... s) {
            initServerConnection();
            try {
                out.print(LOAD_CITIES + " " + CitiesCache.getRecentVersion());
                while (true) {
                    if (input.readLine() != null) {
                        if(input.readLine() == "RECENT"){
                            return null;
                        }
                        else {
                            return input.readLine();
                        }
                    }
                }
            }catch (IOException ex){
                ex.printStackTrace();
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }

    }
}
