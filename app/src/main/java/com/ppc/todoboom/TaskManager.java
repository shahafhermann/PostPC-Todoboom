package com.ppc.todoboom;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.Objects;

/**
 *
 */
class TaskManager {

    private ArrayList<Task> taskList = new ArrayList<>();
    private static final String DB_TASKS = "tasksCollection";

    private static final String DB_ACTION_TAG = "DB update: ";
    private static final String DB_FAIL_ADD_MSG = "Failed to add the Task to the DB";
    private static final String DB_FAIL_FET_MSG = "Failed to fetch the data from the DB";
    private static final String DB_SUCC_ADD_MSG = "Task Added successfully";
    private static final String DB_SUCC_FET_MSG =
            "Data fetched successfully. Current number of tasks: ";
    private static final String UPDATE_FAIL_UI = "A problem occurred while updating the database";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference dbRef = db.collection(DB_TASKS);
    private TaskAdapter adapter;

    /**
     * Constructor
     * @param context The context in which this object will live
     */
    TaskManager(Context context) {
        adapter = new TaskAdapter(taskList);
    }

    void fetchData() {
        taskList.clear();
        dbRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull com.google.android.gms.tasks.Task<QuerySnapshot>
                                                   task) {
                        for (DocumentSnapshot ds: Objects.requireNonNull(task.getResult())) {
                            Task t = ds.toObject(Task.class);
                            taskList.add(t);
                        }
                        Log.i(DB_ACTION_TAG, DB_SUCC_FET_MSG + taskList.size());
                        adapter.notifyDataSetChanged();
                    }
                });
    }

    ArrayList<Task> getList() {
        return taskList;
    }

    TaskAdapter getAdapter() { return adapter; }

    /**
     * Returns the task with the requested id. If it's not found, returns null.
     * @param id The wanted task's id
     */
    Task getTask(String id) {
        for (Task task: taskList) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        return null;
    }

    void addTask(String description) {
        final Task task = new Task(description);
        db.collection(DB_TASKS)
                .add(task)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        String id = documentReference.getId();
                        task.setId(id);
                        taskList.add(task);
                        dbRef.document(id).set(task, SetOptions.merge());
                        Log.i(DB_ACTION_TAG, DB_SUCC_ADD_MSG);
                        adapter.notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(DB_ACTION_TAG, DB_FAIL_ADD_MSG, e);
                    }
                });
    }

    void deleteTask(Task task) {
        taskList.remove(task);
        dbRef.document(task.getId()).delete();
    }

    void completeTask(Task task, Context context) {
        task.setComplete(true);
        updateDB(task, context);
    }

    void renewTask(Task task, Context context) {
        task.setComplete(false);
        updateDB(task, context);
    }

    void setTaskDescription(Task task, String description, Context context) {
        task.setDescription(description);
        updateDB(task, context);
    }

    private void updateDB(Task task, final Context context) {
        task.updateLastEditTime();
        dbRef.document(task.getId()).set(task, SetOptions.merge())
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast toast = Toast.makeText(context, UPDATE_FAIL_UI, Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                });
    }
}
