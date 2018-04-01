package com.example.android.taskplaner;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TaskActivity extends AppCompatActivity{
    private List<String> actions;
    private int[] done;
    private List<Integer> ids;
    private SQLiteDatabase db;
    private Cursor cursor;
    TextView header;

    public static final String TASK_HEADER = "header";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Toolbar toolbar = (Toolbar)findViewById(R.id.task_app_bar);
        setSupportActionBar(toolbar);

        String headerText = (String) getIntent().getExtras().get(TASK_HEADER);
        header = (TextView)findViewById(R.id.task_header);
        header.setText(headerText);

        TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper(this);
        db = taskDatabaseHelper.getWritableDatabase();
        cursor = db.query("tasks", new String[]{"_id", "actions", "done"},
                "taskName=?", new String[]{headerText}, null, null, null);
        actions = new ArrayList<>();
        done = new int[cursor.getCount()];
        ids = new ArrayList<>();
        int n = 0;
        if (cursor.moveToFirst()){
            do {
                ids.add(cursor.getInt(0));
                actions.add(cursor.getString(1));
                done[n] = cursor.getInt(2);
                n++;
            }while (cursor.moveToNext());
        }
        ListView actionList = (ListView)findViewById(R.id.action_list);
        ArrayAdapter<String> adapter = new MySimpleAdapter(this, actions, done);
        actionList.setAdapter(adapter);

        actionList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               TaskDatabaseHelper.changeStatus(db, ids.get(i), done[i]);
               if (done[i] == 0){
                   done[i] = 1;
               }else {
                   done[i] = 0;
               }
                ((MySimpleAdapter)adapterView.getAdapter()).notifyDataSetChanged();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.task_activity_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit:
                Intent intent = new Intent(this, TaskDetailActivity.class);
                intent.putExtra(TaskDetailActivity.TASK_NAME, header.getText().toString());
                intent.putStringArrayListExtra(TaskDetailActivity.TASK_LIST, (ArrayList<String>) actions);
                startActivity(intent);
                break;
            case R.id.delete:
                TaskDatabaseHelper taskDatabaseHelper = new TaskDatabaseHelper(this);
                taskDatabaseHelper.delete(db, header.getText().toString());
                Toast.makeText(TaskActivity.this, "Задача удалена", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent(this, MainActivity.class);
                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cursor.close();
        db.close();
    }
}