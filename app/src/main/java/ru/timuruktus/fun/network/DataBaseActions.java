/* package ru.timuruktus.fun.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.*;
import java.net.*;

import ru.timuruktus.fun.Activities.CityChooseActivity;


public class DataBaseActions extends Thread {

    Socket fromserver = null;
    final int port = 7705;
    final String address = "62.245.62.179";
    BufferedReader input;
    PrintWriter out;
    private String response = null;
    public static final int LOAD_CITIES = 1;
    public static final int WAITING = 10;
    public static final int READY = 20;
    Message msg;
    Handler h;

    public DataBaseActions(){
        super();
        start();

    }

    @Override
    public void run() {
        try {
            fromserver = new Socket(address, port);
            input = new BufferedReader(new InputStreamReader(fromserver.getInputStream()));
            out = new PrintWriter(fromserver.getOutputStream(), true);
            out.write(LOAD_CITIES);
            h.sendEmptyMessage(WAITING);
            response = input.readLine();
            String[] _response = response.split("/");
            msg = h.obtainMessage(READY, _response);
            h.sendMessage(msg);
        }catch (Exception ex){
            Log.d("Exception", "ex = " + ex + "\n StackTrace = ");
            ex.printStackTrace();
            CityChooseActivity cca = new CityChooseActivity();
            cca.errorDialog("Error!" ,"Please, check your Internet connection!", "OK, close the app");
        }
    }



}
*/
