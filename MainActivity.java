package com.example.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    DBApp db;
    Spinner spinnerList;
    ArrayList<String> spinnerListValues;
    ArrayAdapter<String> spinnerListAD;

    ListView lv_tasks;
    ArrayList<String> lv_tasks_values;
    ArrayAdapter<String> lv_tasks_ad;
    Button btn_add_task;
    Button btn_delete_task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = new DBApp(getApplicationContext());

        spinnerList = findViewById(R.id.spinner_list);
        spinnerListValues = new ArrayList<>();
        Cursor cursor = db.getLists();

        while (!cursor.isAfterLast()) {
            spinnerListValues.add(cursor.getString(0));
            cursor.moveToNext();
        }

        spinnerListAD = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, spinnerListValues);
        spinnerList.setAdapter(spinnerListAD);

        lv_tasks = findViewById(R.id.lv_tasks);
        lv_tasks_values = new ArrayList<>();
        lv_tasks_ad = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, lv_tasks_values);
        lv_tasks.setAdapter(lv_tasks_ad);

        btn_add_task = findViewById(R.id.btn_add_task);
        btn_add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String fk = db.getListId(spinnerList.getSelectedItem().toString());
                Intent intent = new Intent(MainActivity.this, TasksInfoActictiy.class);
                intent.putExtra("fk", fk);
                startActivity(intent);
            }
        });

        spinnerList.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                lv_tasks_values.clear();
                lv_tasks_ad.clear();
                Cursor cursor_task = db.getTasks(Integer.parseInt(db.getListId(adapterView.getSelectedItem().toString())));
                while (!cursor_task.isAfterLast())
                {
                    lv_tasks_values.add("name : " + cursor_task.getString(0) + " date : " + cursor_task.getString(1));
                    cursor_task.moveToNext();
                }
                if (lv_tasks_ad.getCount() == 0)
                    Toast.makeText(MainActivity.this, "list is empty", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_delete_task = findViewById(R.id.btn_delete_task);
        btn_delete_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println(lv_tasks.getCheckedItemCount());

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {

        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem menuItem =  menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("type here to search");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tasks_list)
        {
            Intent intent = new Intent(MainActivity.this, TaskActivity.class);
            startActivity(intent);
        }
        return true;
    }
}