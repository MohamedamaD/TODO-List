package com.example.todoapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class TasksInfoActictiy extends AppCompatActivity {

    DBApp db;
    TextView TaskName;
    TextView TaskDate;
    Button confirm;
    String date;
    DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tasks_info_actictiy);
        db = new DBApp(getApplicationContext());
        Intent intent = getIntent();

        TaskName = findViewById(R.id.editTextTextTaskName);
        TaskDate = findViewById(R.id.Date);
        TaskDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int YEAR = cal.get(Calendar.YEAR);
                int MONTH = cal.get(Calendar.MONTH);
                int DAY_OF_MONTH = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(TasksInfoActictiy.this, android.R.style.Theme_Holo_Dialog_MinWidth, dateSetListener, YEAR, MONTH, DAY_OF_MONTH);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                date = i + "-" + i1 + "-" + i2;
                TaskDate.setText(i + "-" + i1 + "-" + i2);
            }
        };

        confirm = findViewById(R.id.btn_confirm);
        int fk = Integer.parseInt(intent.getStringExtra("fk"));

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TaskName.getText().toString().matches("") || date == null)
                    Toast.makeText(TasksInfoActictiy.this, "complete information", Toast.LENGTH_SHORT).show();
                else{
                    Task task  = new Task(TaskName.getText().toString(), date);
                    db.createNewTask(task, fk);
                    Toast.makeText(TasksInfoActictiy.this, "Done", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}