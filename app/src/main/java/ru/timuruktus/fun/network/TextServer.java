package ru.timuruktus.fun.network;


import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import java.io.IOException;

import ru.timuruktus.fun.Activities.Starter;
import ru.timuruktus.fun.LocalData.Fragments.Cities;
import ru.timuruktus.fun.LocalData.LocalCitiesCache;


public class TextServer extends AbstractNetwork {

    private final int LOAD_CITIES = 0;
    private final int CLOSE_CONNECTION = 1;
    CreateConnection cc;

    public TextServer(Context context){
        super(7705, "192.168.1.57", context);
        cc = new CreateConnection();
    }


    public void loadCities(){
        cc.execute(LOAD_CITIES);
    }

    public void closeConnection(){
        cc.execute(CLOSE_CONNECTION);
    }


    class CreateConnection extends AsyncTask<Integer, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        public Void doInBackground(Integer... action) {
            try {
                if (action[0] == LOAD_CITIES) {
                    LocalCitiesCache lcc = new LocalCitiesCache(TextServer.this.context);
                    if (Starter.hasNetworkConenction) {
                        while(wait){}
                        cs.get();
                        Log.d(Starter.LOG_TAG, "First Step!");
                        TextServer.super.outputStream.writeUTF("CITIES");
                        Log.d(Starter.LOG_TAG, "Second Step!");
                        Cities cities = (Cities) inputStream.readObject();
                        Log.d(Starter.LOG_TAG, "Third Step!");
                        lcc.checkDBRecency(cities);
                    }
                    lcc.loadCitiesFromDB();
                } else if (action[0] == CLOSE_CONNECTION) {
                    TextServer.super.outputStream.writeUTF("EXIT");
                    Log.d(LOG_TAG, "Connection was closed");
                }
            } catch (IOException ex) {
                Log.e(Starter.LOG_TAG, "IOException in handler in TextServer");
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Log.e(Starter.LOG_TAG, "ClassNotFoundException in handler in TextServer");
                ex.printStackTrace();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}
