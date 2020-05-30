package com.ppc.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
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

    private App app;
    private ArrayList<Task> taskList = new ArrayList<>();
    private TaskAdapter taskAdapter;
    private TaskManager taskManager;
    private EditText inputTextEditor;

    // Used for restoring state
    private String inputText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        app = (App) getApplicationContext();
        taskManager = app.getTaskManager();
        taskList = taskManager.getList();
        taskAdapter =  taskManager.getAdapter();
        taskAdapter.setListener(new TaskListener());

        /*>> UI ELEMENTS <<*/
        /* RecyclerView */
        RecyclerView recycler = findViewById(R.id.task_recycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(taskAdapter);

        /* Other elements */
        inputTextEditor = findViewById(R.id.inputText);
        inputTextEditor.setText(inputText);
        Button create = findViewById(R.id.createButton);
        create.setOnClickListener(new CreateButtonListener());
    }

    /**
     * A Listener for user interactions with the tasks
     */
    class TaskListener extends Listener {
        @Override
        public void onClickListener(Task task){
            if (task.isComplete()) {
                Intent taskCompleteIntent = new Intent(getApplicationContext(),
                        CompleteTaskActivity.class);
                taskCompleteIntent.putExtra(app.EXTRA_ID, task.getId());
                startActivity(taskCompleteIntent);
            } else {
                Intent taskNotCompleteIntent = new Intent(getApplicationContext(),
                        NonCompleteTaskActivity.class);
                taskNotCompleteIntent.putExtra(app.EXTRA_ID, task.getId());
                startActivity(taskNotCompleteIntent);
            }
        }

        @Override
        public void onLongClickListener(Task task){}
    }

    class CreateButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (inputTextEditor.getText().toString().equals("")) {
                Toast toast = Toast.makeText(app, R.string.empty_error, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            } else {
                taskManager.addTask(inputTextEditor.getText().toString());
                taskAdapter.updateList(taskList);
                inputTextEditor.setText("");
                inputText = "";

                hideKeyboard(view);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        taskManager.fetchData();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_INPUT, inputText);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        inputText = savedInstanceState.getString(KEY_INPUT);
    }

    private void hideKeyboard(View view) {
        InputMethodManager manager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        if (manager != null) {
            manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
