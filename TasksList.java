package com.example.todoapp;

import java.util.ArrayList;

public class TasksList {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TasksList(String name) {
        this.name = name;
    }

    public TasksList() {
        name = null;
    }
}
