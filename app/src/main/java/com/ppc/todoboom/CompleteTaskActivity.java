package com.ppc.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.preference.PreferenceFragmentCompat;

public class CompleteTaskActivity extends AppCompatActivity {

    private final String KEY_POPUP = "dialogOpen";
    private final String KEY_TASK_DEL = "taskToDelete";
    private String MSG_CONFIRM_DEL = "The task %s will be deleted.";

    private TaskManager taskManager;
    private Task task;

    //    private Boolean dialogOpen = false;
    private Task taskToDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_task);

        /*>> Task elements <<*/
        App app = (App) getApplicationContext();
        taskManager = app.getTaskManager();
        Intent caller = getIntent();
        String id = caller.getStringExtra(app.EXTRA_ID);
        task = taskManager.getTask(id);

        /*>> UI ELEMENTS <<*/
    }

    private void showPopup(Task task) {
        taskToDelete = task;
        final String taskDescription = task.getDescription();
        String confirmMsg = String.format(MSG_CONFIRM_DEL, taskDescription);
        AlertDialog.Builder adb = new AlertDialog.Builder(CompleteTaskActivity.this);
        adb.setMessage(confirmMsg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskManager.deleteTask(taskToDelete);
                        taskToDelete = null;

                        // TODO: should we go back automatically
                        Intent gotBackIntent = new Intent();
                        setResult(RESULT_OK, gotBackIntent);
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        taskToDelete = null;
                    }
                });
        AlertDialog popup = adb.create();
        popup.show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
//        outState.putBoolean(KEY_POPUP, dialogOpen);
        outState.putString(KEY_TASK_DEL, taskToDelete.getId());
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        dialogOpen = savedInstanceState.getBoolean(KEY_POPUP);
        taskToDelete = taskManager.getTask(savedInstanceState.getString(KEY_TASK_DEL));
        if (taskToDelete != null) {
            showPopup(taskToDelete);
        }
    }
}