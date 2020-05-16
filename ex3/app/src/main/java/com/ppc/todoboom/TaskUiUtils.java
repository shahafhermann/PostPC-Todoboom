package com.ppc.todoboom;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

/**
 * A class representing a task holder for the recycler view.
 */
class TaskHolder extends RecyclerView.ViewHolder {

    private final TextView text;

    public TaskHolder(View v) {
        super(v);
        text = v.findViewById(R.id.taskText);
    }

    public void markDone() {
        text.setPaintFlags(text.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
        text.setTextColor(Color.argb(55, 0, 0, 0));
    }

    public void markUnDone() {
        text.setPaintFlags(text.getPaintFlags() & ~STRIKE_THRU_TEXT_FLAG);
        text.setTextColor(Color.argb(255, 0, 0, 0));
    }

    public TextView getTextView() {
        return text;
    }
}

/**
 *
 */
class TaskAdapter extends RecyclerView.Adapter {

    private ArrayList<Task> taskList;
    private Listener listener;

    public TaskAdapter(ArrayList<Task> tl) {
        taskList = tl;
    }

    public void updateList(ArrayList<Task> tl) {
        taskList = tl;
        notifyDataSetChanged();
    }

    public void setListener(Listener l) {
        listener = l;
    }


    //* RecyclerView.Adapter Methods Below *//
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context)
                        .inflate(R.layout.item_one_task, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TaskHolder th = (TaskHolder) holder;
        final Task task = taskList.get(position);
        th.getTextView().setText(task.getDescription());

        if (task.isDone()) {
            th.markDone();
        } else {
            th.markUnDone();
        }

        th.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.onClickListener(task);
            }
        });

        th.itemView.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v){
                listener.onLongClickListener(task);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskList.size();
    }
}