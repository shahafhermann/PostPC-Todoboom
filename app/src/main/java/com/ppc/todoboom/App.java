package com.ppc.todoboom;

import android.app.Application;

/**
 *
 */
public class App extends Application {

    private TaskManager tm;
    private static final String LOG_MSG_NUM_OF_TASKS = "Number of tasks: ";
    private static final String LOG_KEY_NUM_OF_TASKS = "Todoboom";

    @Override
    public void onCreate() {
        super.onCreate();

        tm = new TaskManager(this);
        int len = tm.getList().size();
        android.util.Log.i(LOG_KEY_NUM_OF_TASKS, LOG_MSG_NUM_OF_TASKS + len);
    }

    public TaskManager getTaskManager() {
        return tm;
    }
}
