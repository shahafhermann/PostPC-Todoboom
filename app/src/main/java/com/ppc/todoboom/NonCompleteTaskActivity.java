package com.ppc.todoboom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceFragmentCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class NonCompleteTaskActivity extends AppCompatActivity {

    private final String TASK_COMPLETE_MSG = "TODO %1$s is now DONE. BOOM!";
    private final String KEY_INPUT = "inputText";
    private String inputText = "";

    private TaskManager taskManager;
    private Task task;
    private TextView description;
    private Button saveDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_complete_task);

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

        description = findViewById(R.id.description_text);
        inputText = task.getDescription();
        description.setText(inputText);
        description.addTextChangedListener(new DescriptionEditorWatcher());

        saveDescription = findViewById(R.id.save_description);
        saveDescription.setOnClickListener(new SaveButtonListener());

        Button completeButton = findViewById(R.id.complete_button);
        completeButton.setOnClickListener(new CompleteButtonListener());
    }

    class DescriptionEditorWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            String newText = description.getText().toString();
            if (!newText.equals(inputText) && newText.length() > 0) {
                saveDescription.setEnabled(true);
            } else {
                saveDescription.setEnabled(false);
            }
        }
    }

    class SaveButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            hideKeyboard(view);

            String newText = description.getText().toString();
            inputText = newText;
            Context context = getApplicationContext();
            taskManager.setTaskDescription(task, newText, context);

            saveDescription.setEnabled(false);
        }
    }

    class CompleteButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            Context context = getApplicationContext();
            taskManager.completeTask(task, context);
            Toast toast = Toast.makeText(context,
                    String.format(TASK_COMPLETE_MSG, task.getDescription()), Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

            setResult(RESULT_OK, new Intent());
            finish();
        }
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