package com.example.android.taskplaner;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TasksAdapter extends RecyclerView.Adapter<TasksAdapter.ViewHolder>{

    private Listener listener;
    private Map<String, String> done;
    private List<Integer> flag;
    private List<String> tasks;
    private Context context;

    public static interface Listener{
        public void itemClicked(String taskName);
    }

    public TasksAdapter(Context context, List<String> tasks, Map<String, String> done){
        this.tasks = tasks;
        this.done = done;
        this.context = context;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cv;
        public ViewHolder(CardView cv) {
            super(cv);
            this.cv = cv;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_layout, parent, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        CardView cv = holder.cv;
        if (!done.get(tasks.get(position)).contains("0")){
            cv.setCardBackgroundColor(context.getResources().getColor(R.color.checked));
        }
        final TextView task = (TextView)cv.findViewById(R.id.task);
        task.setText(tasks.get(position));
        cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.itemClicked(tasks.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
