package com.wordline.TaskBoard.model;

import java.util.HashMap;
import java.util.Map;

public class TaskList {
    private String name;
    private Map<String, Task> tasks;

    public TaskList(){
        this.tasks = new HashMap<>();
    }

    public TaskList(String name){
        this.name = name;
        this.tasks = new HashMap<>();
    }

    public void addTask(String taskName, Task task){
        tasks.put(taskName, task);
    }

    public void removeTask(String taskName){
        tasks.remove(taskName);
    }

    public boolean hasTask(String taskName){
        return tasks.containsKey(taskName);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public Task getTask(String taskName) {
        return tasks.get(taskName);
    }

    public void setTasks(Map<String, Task> tasks) {
        this.tasks = tasks;
    }
}
