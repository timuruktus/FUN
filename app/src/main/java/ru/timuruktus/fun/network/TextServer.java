package ru.timuruktus.fun.network;


import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.w3c.dom.Text;

import java.io.IOException;

import ru.timuruktus.fun.Activities.Starter;
import ru.timuruktus.fun.LocalData.Fragments.Cities;
import ru.timuruktus.fun.LocalData.LocalCitiesCache;


public class TextServer extends AbstractNetwork {
    private final int LOAD_CITIES = 0;
    Message msg;

    public TextServer(Context context){
        super(7705, "10.0.2.2", context);
    }


    public void loadCities(){
        this.handler.sendEmptyMessage(LOAD_CITIES);
    }


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            try {
                LocalCitiesCache lcc = new LocalCitiesCache(TextServer.super.context);
                if(msg.what == LOAD_CITIES){
                    if(Starter.hasNetworkConenction) {
                        if(inputStream == null || socket == null){
                            Log.d(LOG_TAG, "smth is null");
                        }
                        if(outputStream == null) Log.d(Starter.LOG_TAG, "Out is null");
                        outputStream.writeObject(TextServer.super.LOAD_CITIES);
                        while (inputStream.readObject() == null) {
                            Log.i(Starter.LOG_TAG, "Input Stream Is Null");
                        }
                        Cities cities = (Cities) inputStream.readObject();
                        TextServer.this.msg = handler.obtainMessage(LocalCitiesCache.CHECK_RECENCY_OF_DB, 0, 0, cities);
                        lcc.handler.sendMessage(TextServer.this.msg);
                    }
                    lcc.handler.sendEmptyMessage(lcc.LOAD_CITIES_FROM_DB);
                }

            }catch (IOException ex){
                Log.d(Starter.LOG_TAG, "IOException in handler in TextServer");
                ex.printStackTrace();
            }catch (ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }
    };
}
