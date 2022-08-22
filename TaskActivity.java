package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class TaskActivity extends AppCompatActivity {

    ListView vl_show_tasks_list;
    ArrayList<String> default_tasks_list;
    ArrayAdapter<String> adapter;
    Button btn_add_list;
    DBApp db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        db = new DBApp(getApplicationContext());

        vl_show_tasks_list = findViewById(R.id.vl_show_tasks_list);
        default_tasks_list = new ArrayList<>();
        Cursor cursor = db.getLists();

        while (!cursor.isAfterLast()){
            default_tasks_list.add(cursor.getString(0));
            cursor.moveToNext();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, default_tasks_list);
        btn_add_list = findViewById(R.id.btn_add_list);
        vl_show_tasks_list.setAdapter(adapter);

        vl_show_tasks_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                new AlertDialog.Builder(TaskActivity.this).setTitle("Do u wanna remove " + default_tasks_list.get(i) + " tasks list ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        db.delateTasks(db.getListId(default_tasks_list.get(i)));
                        db.deleteList(default_tasks_list.get(i));
                        default_tasks_list.remove(i);
                        adapter.notifyDataSetChanged();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });

        btn_add_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LinearLayout layout = new LinearLayout(TaskActivity.this);
                layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.setGravity(Gravity.CLIP_VERTICAL);
                layout.setPadding(3,3,3,3);

                EditText task_list_name = new EditText(TaskActivity.this);
                task_list_name.setHint("List Name");
                layout.addView(task_list_name);

                new AlertDialog.Builder(TaskActivity.this).setTitle("Add List").setView(layout).setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (!task_list_name.getText().toString().matches("")) {
                            default_tasks_list.add(task_list_name.getText().toString());
                            adapter.notifyDataSetChanged();
                            db.createNewList(new TasksList(task_list_name.getText().toString()));
                            Toast.makeText(TaskActivity.this, "Done", Toast.LENGTH_SHORT).show();
                        }
                        else
                            Toast.makeText(TaskActivity.this, "Enter name", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
            }
        });
    }
}