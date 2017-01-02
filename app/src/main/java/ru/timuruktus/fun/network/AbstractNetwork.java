package ru.timuruktus.fun.network;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import ru.timuruktus.fun.Activities.Starter;

public abstract class AbstractNetwork{

    public final String LOG_TAG = "myLogs";
    static String LOAD_CITIES = "CITIES";
    Socket socket;
    ObjectOutputStream outputStream;
    ObjectInputStream inputStream;
    Context context;
    private int port;
    private String address;



    public AbstractNetwork(int port, String address, Context context){
        this.port = port;
        this.address = address;
        this.context = context;
        new CreateSocket().execute();
    }

    class CreateSocket extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Void... urls) {
            try {
                socket = new Socket(address, port);
                outputStream = new ObjectOutputStream(socket.getOutputStream());
                inputStream = new ObjectInputStream(socket.getInputStream());

            } catch (Exception e) {
                Log.d(Starter.LOG_TAG, "IOException in handler in AbstractNetwork");
                e.printStackTrace();
            }
            return null;
        }

    }

}
