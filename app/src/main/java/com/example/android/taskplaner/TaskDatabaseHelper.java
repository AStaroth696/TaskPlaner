package com.example.android.taskplaner;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class TaskDatabaseHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "tasks";
    private static final int DB_VERSION = 1;

    TaskDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        updateDatabase(sqLiteDatabase, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        updateDatabase(sqLiteDatabase, i, i1);
    }

    private void updateDatabase(SQLiteDatabase db, int oldVersion, int newVersion){
        if (oldVersion < 1){
            db.execSQL("CREATE TABLE tasks("
            + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + "taskName TEXT, "
            + "actions Text, "
            + "done INTEGER);");
        }
    }

    public void insertAction(SQLiteDatabase db, String task, String action){
        ContentValues actionValues = new ContentValues();
        actionValues.put("taskName", task);
        actionValues.put("actions", action);
        actionValues.put("done", 0);
        db.insert(DB_NAME, null, actionValues);
    }

    public void delete(SQLiteDatabase db, String taskName){
        db.delete(DB_NAME, "taskName=?", new String[]{taskName});
    }

    public static void changeStatus(SQLiteDatabase db, Integer id, Integer done){
        ContentValues cv = new ContentValues();
        if (done == 0){
            cv.put("done", 1);
            db.update(DB_NAME, cv, "_id=?", new String[]{String.valueOf(id)});
        }else {
            cv.put("done", 0);
            db.update(DB_NAME, cv, "_id=?", new String[]{String.valueOf(id)});
        }
    }

    public void changeTaskName(SQLiteDatabase db, String oldName, String newName){
        ContentValues cv = new ContentValues();
        cv.put("TaskName", newName);
        db.update(DB_NAME, cv, "taskName=?", new String[]{oldName});
    }

    public void syncrhronizeActionLists(SQLiteDatabase db, String taskName,
                                        List<String> oldList, List<String> newList){

        System.out.println("--------------------old list: " + oldList + " new list: " + newList);
        for (String s : oldList) {
            if (!newList.contains(s)){
                db.delete(DB_NAME, "taskName=? AND actions=?",
                        new String[]{taskName, s});
            }
        }

        for (String r : newList) {
            if (!oldList.contains(r)){
                insertAction(db, taskName, r);
            }
        }
    }
}