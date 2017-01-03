package ru.timuruktus.fun.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.w3c.dom.Text;

import ru.timuruktus.fun.R;
import ru.timuruktus.fun.network.TextServer;


public class CityChooseActivity extends AbstractActivity implements SurfaceHolder.Callback,Application.ActivityLifecycleCallbacks {

    final String LOG_TAG = "myLogs";
    SurfaceView videoView;
    ProgressBar loading;
    MediaPlayer mp;
    SurfaceHolder sh;
    private ImageView funLogo,funText, loadingBackground;
    TextView cityText;
    public static Handler h;
    TextServer textServer;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.citychoose);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        videoView = (SurfaceView) findViewById(R.id.videoView);
        sh = videoView.getHolder();
        startBackgroundVideo();
        loadInterface();


        h = new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                if(msg.what == WAITING){
                    loading.setVisibility(View.VISIBLE);
                }
                else if (msg.what == READY){
                    loadingBackground.setVisibility(View.INVISIBLE);
                    loading.setVisibility(View.INVISIBLE);
                    listView = (ListView) findViewById(R.id.listView);
                    if(msg.obj == null) Log.d(LOG_TAG, "Object is null");
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(new CityChooseActivity(),
                            R.layout.city_listview, (String[]) msg.obj);
                    listView.setAdapter(adapter);
                }
                else if(msg.what == NETWORK_ERROR){
                    showDialog(getString(R.string.errorNetMessage), "Пожалуйста, проверьте ваше соединение!", getString(R.string.OK), STOP, false);
                }
            }
        };
        h.sendEmptyMessage(WAITING);
        textServer = new TextServer(this);
        textServer.loadCities();


    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        mp.setDisplay(null);
        mp.release();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        startBackgroundVideo();
        mp.setDisplay(sh);
        mp.setLooping(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void startBackgroundVideo(){
        mp = MediaPlayer.create(this, R.raw.welcome);
        mp.start();
        sh.addCallback(this);
    }

    public void loadInterface(){

        Animation animCityText = AnimationUtils.loadAnimation(this, R.anim.citytext);
        Animation animFunLogo = AnimationUtils.loadAnimation(this, R.anim.funlogo);
        Animation animFunText = AnimationUtils.loadAnimation(this, R.anim.funtext);

        funLogo = (ImageView) findViewById(R.id.funLogo);
        funText = (ImageView) findViewById(R.id.funText);
        cityText = (TextView) findViewById(R.id.citytext);
        loading = (ProgressBar) findViewById(R.id.loading);
        loadingBackground = (ImageView) findViewById(R.id.loadingBackground);

        funText.setImageResource(R.drawable.funtext);
        funLogo.setImageResource(R.drawable.minilogo2);

        funLogo.startAnimation(animFunLogo);
        cityText.startAnimation(animCityText);
        funText.startAnimation(animFunText);

    }



    @Override
    public void showDialog(String title, String message, String butText, final int whatToDo, boolean alternative){

        AlertDialog.Builder builder = new AlertDialog.Builder(CityChooseActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setCancelable(alternative)
                .setNegativeButton(butText,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                if(whatToDo == STOP) System.exit(0);
                                if(whatToDo == CONTINUE){}
                            }
                        });

        if(alternative){
            builder.setNeutralButton("OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();

                        }
                    });
        }
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        textServer.closeConnection();
    }
}