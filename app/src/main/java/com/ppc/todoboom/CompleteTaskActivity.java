package com.ppc.todoboom;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class CompleteTaskActivity extends AppCompatActivity {

//    private final String KEY_POPUP = "dialogOpen";
    private final String KEY_TASK_DEL = "taskToDelete";
    private final String TASK_RENEW_MSG = "Not done yet?...";
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
        Resources res = getResources();

        TextView creationTime = findViewById(R.id.creation_date);
        String creationTimeString = res.getString(R.string.creation_date,
                                                  task.getCreationTime().toString());
        creationTime.setText(creationTimeString);

        TextView editTime = findViewById(R.id.last_edit_date);
        String editTimeString = res.getString(R.string.last_edit_date,
                                              task.getLastEditTime().toString());
        editTime.setText(editTimeString);

        TextView description = findViewById(R.id.description_text);
        description.setText(task.getDescription());

        Button renewButton = findViewById(R.id.renew);
        renewButton.setOnClickListener(new RenewButtonListener());

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new DeleteButtonListener());
    }

    class RenewButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Context context = getApplicationContext();
            taskManager.renewTask(task, context);
            Toast toast = Toast.makeText(context, TASK_RENEW_MSG, Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            setResult(RESULT_OK, new Intent());
            finish();
        }
    }

    class DeleteButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            showPopup(task);
        }
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

                        Intent goBackIntent = new Intent();
                        setResult(RESULT_OK, goBackIntent);
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