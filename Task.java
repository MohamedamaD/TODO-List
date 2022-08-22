package com.example.todoapp;

public class Task {
    private String task_name;
    private String task_date;

    public Task(String task_name, String task_date) {
        this.task_name = task_name;
        this.task_date = task_date;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getTask_date() {
        return task_date;
    }

    public void setTask_date(String task_date) {
        this.task_date = task_date;
    }
}
