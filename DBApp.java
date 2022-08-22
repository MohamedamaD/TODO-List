package com.example.todoapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.Date;

public class DBApp extends SQLiteOpenHelper {

    public static final String LIST_TABLE = "LIST_TABLE";
    public static final String LIST_ID = "ID";
    public static final String LIST_NAME = "LIST_NAME";

    public static final String TASK_TABLE = "TASK_TABLE";
    public static final String TASK_ID = "ID";
    public static final String TASK_NAME = "TASK_NAME";
    public static final String TASK_DATE = "TASK_DATE";
    public static final String FK_ID = "FK_ID";

    SQLiteDatabase appDatabase;

    public DBApp(@Nullable Context context) {
        super(context, "appDatabase", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CreateListTable = "CREATE TABLE "
                + LIST_TABLE + " ("
                + LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + LIST_NAME + " TEXT);";

        String CreateTaskTable = "CREATE TABLE "
                + TASK_TABLE + " ("
                + TASK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TASK_NAME + " TEXT, "
                + TASK_DATE + " TEXT, "
                + FK_ID + " INTEGER, FOREIGN KEY("+ FK_ID +") REFERENCES " + LIST_TABLE + "(" + LIST_ID + "));";

        sqLiteDatabase.execSQL(CreateListTable);
        sqLiteDatabase.execSQL(CreateTaskTable);

        // todo default lists
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists LIST_TABLE");
    }

    public void createNewList(TasksList list) {
        appDatabase = this.getWritableDatabase();
        ContentValues row = new ContentValues();
        row.put(LIST_NAME, list.getName());
        appDatabase.insert(LIST_TABLE, null, row); // name
        appDatabase.close();
    }

    public void delateTasks(String fk) {
        appDatabase = getWritableDatabase();
        appDatabase.execSQL("delete from " + TASK_TABLE + " where " + FK_ID + " = " + fk + ";");
        appDatabase.close();
    }

    public void deleteList(String list_name) {
        appDatabase = getWritableDatabase();
        appDatabase.execSQL("delete from " + LIST_TABLE + " where " + LIST_NAME + " like '" + list_name + "';");
        appDatabase.close();
    }

    public void createNewTask(Task task, int fk) {
        ContentValues row = new ContentValues();
        row.put(TASK_NAME, task.getTask_name());
        row.put(TASK_DATE, task.getTask_date().toString());
        row.put(FK_ID, fk);
        appDatabase = getWritableDatabase();
        appDatabase.insert(TASK_TABLE, null, row);
        appDatabase.close();
    }

    public String getListId(String ListName)
    {
        appDatabase = getReadableDatabase();
        String [] args = {ListName};
        Cursor cursor = appDatabase.rawQuery("select " + LIST_ID + " from " + LIST_TABLE + " where LIST_NAME like ?", args);
        cursor.moveToFirst();
        appDatabase.close();
        return  cursor.getString(0);
    }

    public Cursor getLists() {
        appDatabase = getReadableDatabase();
        String[] args = {};
        Cursor cursor = appDatabase.rawQuery("select " + LIST_NAME + " from " + LIST_TABLE, args);
        if (cursor != null)
            cursor.moveToFirst();
        appDatabase.close();
        return cursor;
    }

    public Cursor getTasks (int fk_id) {
        appDatabase = getReadableDatabase();
        String[] args = {};
        String statement = "select "
                + TASK_NAME + " , "
                + TASK_DATE + " from "
                + TASK_TABLE + " where "
                + FK_ID + " = "
                + fk_id + ";";

        Cursor cursor = appDatabase.rawQuery(statement, args);
        if (cursor != null)
            cursor.moveToFirst();
        appDatabase.close();
        return cursor;
    }

    public void ex () {
        appDatabase = getWritableDatabase();
        appDatabase.execSQL("delete from LIST_TABLE where LIST_NAME like 'work_s';");
        appDatabase.close();
    }
}
