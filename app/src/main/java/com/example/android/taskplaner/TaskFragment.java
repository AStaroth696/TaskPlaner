package com.example.android.taskplaner;


import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskFragment extends Fragment {

    private Cursor cursor;
    private SQLiteDatabase db;


    public TaskFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        RecyclerView taskRecycler = (RecyclerView) inflater.inflate(R.layout.frame_task_recycler,
                container, false);

        TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper(getActivity());
        db = taskDatabaseHelper.getReadableDatabase();
        cursor = db.query("tasks", new String[]{"taskName", "done"}, null,
                null, null, null, null);
        List<String> tasksList = new ArrayList<>();
        Map<String, String> done = new HashMap<>();
        String values;
        String name;
        if (cursor.moveToFirst()) {
            do {
                name = cursor.getString(0);
                values = done.get(name);
                values += String.valueOf(cursor.getInt(1));
                done.put(name, values);
                if (!tasksList.contains(name)){
                    tasksList.add(name);
                }
            } while (cursor.moveToNext());
        TasksAdapter adapter = new TasksAdapter(getActivity(), tasksList, done);
        taskRecycler.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        taskRecycler.setLayoutManager(layoutManager);
        adapter.setListener(new TasksAdapter.Listener() {
            @Override
            public void itemClicked(String taskName) {
                Intent intent = new Intent(getActivity(), TaskActivity.class);
                intent.putExtra(TaskActivity.TASK_HEADER, taskName);
                startActivity(intent);
            }
        });
        return taskRecycler;
    }else {
            return null;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        cursor.close();
        db.close();
    }
}
