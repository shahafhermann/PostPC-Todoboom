package com.ppc.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private final String KEY_INPUT = "inputText";
    private final String KEY_LIST = "taskList";
    private final String KEY_POPUP = "dialogOpen";
    private final String KEY_TASK_DEL = "taskToDelete";
    private String MSG_CONFIRM_DEL = "The task %s will be deleted.";

    private App app;
    private ArrayList<Task> taskList = new ArrayList<>();
    private TaskAdapter taskAdapter = new TaskAdapter(taskList);

    // Used for restoring state
    private String inputText = "";
    private Boolean dialogOpen = false;
    private Task taskToDelete = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplicationContext();
        taskList = app.getTaskManager().getList();
        taskAdapter.updateList(taskList);
        taskAdapter.setListener(new taskListener());

        RecyclerView recycler = findViewById(R.id.task_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(taskAdapter);

        Button create = findViewById(R.id.createButton);
        final EditText inputTextEditor = findViewById(R.id.inputText);
        inputTextEditor.setText(inputText);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (inputTextEditor.getText().toString().equals("")) {
                    Toast toast = Toast.makeText(app, R.string.empty_error, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                } else {
                    Task task = new Task(inputTextEditor.getText().toString());
                    inputTextEditor.setText("");
                    inputText = "";
                    app.getTaskManager().addTask(task);
                    taskAdapter.updateList(taskList);

                    hideKeyboard(view);
                }
            }
        });

    }

    class taskListener extends Listener {
        @Override
        public void onClickListener(Task task){
            if (!task.isDone()) {
                task.complete();
                taskAdapter.updateList(taskList);
                app.getTaskManager().updateList(taskList);
                Resources res = getResources();
                String doneMsg = res.getString(R.string.done_msg, task.getDescription());
                Toast toast = Toast.makeText(app, doneMsg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }

        @Override
        public void onLongClickListener(Task task){
            showPopup(task);
        }
    }

    private void showPopup(Task task) {
        taskToDelete = task;
        final String taskDescription = task.getDescription();
        String confirmMsg = String.format(MSG_CONFIRM_DEL, taskDescription);
        AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
        adb.setMessage(confirmMsg)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        app.getTaskManager().deleteItem(taskToDelete);
                        taskList.remove(taskToDelete);
                        taskAdapter.updateList(taskList);
                        taskToDelete = null;
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
        outState.putString(KEY_INPUT, inputText);
        outState.putParcelableArrayList(KEY_LIST, taskList);
        outState.putBoolean(KEY_POPUP, dialogOpen);
        outState.putParcelable(KEY_TASK_DEL, taskToDelete);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputText = savedInstanceState.getString(KEY_INPUT);
        taskList = savedInstanceState.getParcelableArrayList(KEY_LIST);
        taskAdapter.updateList(taskList);
        dialogOpen = savedInstanceState.getBoolean(KEY_POPUP);
        taskToDelete = savedInstanceState.getParcelable(KEY_TASK_DEL);
        if (taskToDelete != null) {
            showPopup(taskToDelete);
        }
    }

    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
