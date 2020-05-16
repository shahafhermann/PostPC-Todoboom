package com.ppc.todoboom;

import android.content.Context;
import android.content.SharedPreferences;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TaskManager {

    private ArrayList<Task> taskList;
    private static final String SP_ADAPTER = "adapter";
    private static final String SP_TASKS = "tasks";

    private static SharedPreferences sp;

    /**
     * Constructor
     * @param context
     */
    public TaskManager(Context context) {
        Gson gson = new Gson();
        sp = PreferenceManager.getDefaultSharedPreferences(context);
        String json = sp.getString(SP_TASKS, "");
        Type type = new TypeToken<List<Task>>(){}.getType();
        taskList = gson.fromJson(json, type);
    }

    public ArrayList<Task> getList() {
        return taskList;
    }

    public void addTask(Task t) {
        taskList.add(t);
        updateSP();
    }

    public void deleteItem(Task t) {
        taskList.remove(t);
        updateSP();
    }

    public void updateList(ArrayList<Task> tl) {
        taskList = tl;
        updateSP();
    }

    private void updateSP() {
        sp.edit()
                .putString(SP_TASKS, new Gson().toJson(taskList))
                .apply();
    }
}
