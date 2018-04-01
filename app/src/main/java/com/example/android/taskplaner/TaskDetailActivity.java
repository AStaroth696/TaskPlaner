package com.example.android.taskplaner;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    public static final String TASK_NAME = "task name";
    public static final String TASK_LIST = "task list";
    private List<String>initialActionList;
    private String initialTaskName;
    private boolean isIntent;

    private SQLiteDatabase db;
    private EditText taskName;
    private EditText actionText;
    private ListView actions;
    private List<String> actionList;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);
        isIntent = getIntent().getExtras() != null;

        Toolbar toolbar = (Toolbar)findViewById(R.id.detail_app_bar);
        setSupportActionBar(toolbar);

        taskName = (EditText)findViewById(R.id.task_name);
        actionText = (EditText)findViewById(R.id.action_text);
        actions = (ListView)findViewById(R.id.actions_list);
        actionList = new ArrayList<>();

        if (isIntent) {
            initialTaskName = getIntent().getExtras().getString(TASK_NAME);
            initialActionList = getIntent().getStringArrayListExtra(TASK_LIST);
            taskName.setText(initialTaskName);
            actionList.addAll(initialActionList);
        }

        adapter = new MyListAdapter(this, actionList);
        actions.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.accept){
            if(taskName.getText() == null || actionList.isEmpty()){
                Toast.makeText(TaskDetailActivity.this,
                        "Сначала введите название задачи и добавте действия", Toast.LENGTH_SHORT)
                        .show();
            }else {
                TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper(this);
                db = taskDatabaseHelper.getWritableDatabase();
                String taskHeader = taskName.getText().toString();

                if (isIntent){
                    if (!initialTaskName.equals(taskName.getText().toString())){
                        taskDatabaseHelper.changeTaskName(db, initialTaskName, taskHeader);

                    }
                    if (!initialActionList.equals(actionList)){
                        taskDatabaseHelper.syncrhronizeActionLists(db, taskHeader,
                                initialActionList, actionList);
                    }
                    Toast.makeText(TaskDetailActivity.this, "Задача обновлена", Toast.LENGTH_SHORT)
                            .show();
                    Intent intent = new Intent(this, TaskActivity.class);
                    intent.putExtra(TaskActivity.TASK_HEADER, taskHeader);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else {
                    for (String s : actionList) {
                        taskDatabaseHelper.insertAction(db, taskHeader, s);
                    }

                    Toast.makeText(TaskDetailActivity.this, "Задача добавлена", Toast.LENGTH_SHORT)
                            .show();
                }
                actionList.clear();
                adapter.notifyDataSetChanged();
                taskName.setText("");
                actionText.setText("");
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void onCheckMarckClick(View view){
        actionList.add(actionText.getText().toString());
        adapter.notifyDataSetChanged();
        actionText.setText("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (db != null) {
            db.close();
        }
    }
}
