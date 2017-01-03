package ru.timuruktus.fun.Activities;


import android.app.Activity;

abstract public class AbstractActivity extends Activity {

    public static final int STOP = 100;
    public static final int CONTINUE = 200;
    public static final int WAITING = 0;
    public static final int READY = 1;
    public static final int NETWORK_ERROR = 3;

    /**
     * Needs to show dialogs in any activities
     * @param title - title of dialog
     * @param message - message of dialog
     * @param butText - main button text
     * @param whatToDo - action on main button click
     * @param alternative - any alternative variants
     */
    abstract public void showDialog(String title, String message, String butText, int whatToDo, boolean alternative);
}
