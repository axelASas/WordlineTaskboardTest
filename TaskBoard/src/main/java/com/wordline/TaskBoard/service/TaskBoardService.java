package com.wordline.TaskBoard.service;

import com.wordline.TaskBoard.TaskBoardApplication;
import org.springframework.stereotype.Service;

import com.wordline.TaskBoard.model.Task;
import com.wordline.TaskBoard.model.TaskList;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class TaskBoardService {
    private static final String SAVE_FILE = "saved.txt";
    private final Map<String, TaskList> taskLists = new HashMap<>();

    public TaskBoardService(){
        load();
    }

    public Map<String, TaskList> getLists(){
        return taskLists;
    }

    public TaskList createList(String listName){
        if(taskLists.containsKey(listName)) {
            throw new IllegalArgumentException(listName + " already exists.");
        }

        TaskList newList = new TaskList(listName);
        taskLists.put(listName,newList);
        save();
        return newList;
    }

    public Task addTask(String listName, String taskName, String taskDescription){
        TaskList list = getListOrThrow(listName);
        Task task = new Task(taskName, taskDescription);
        list.addTask(taskName, task);
        save();
        return task;
    }

    public Task updateTask(String listName, String oldTaskName, String newTaskName, String newDescription){
        TaskList list = getListOrThrow(listName) ;

        if (!list.hasTask(oldTaskName)) {
            throw new IllegalArgumentException("Task not found: " + oldTaskName);
        }

        list.removeTask(oldTaskName);
        Task task = new Task(newTaskName, newDescription);
        list.addTask(newTaskName, task);
        save();
        return task;
    }

    public void deleteTask(String listName, String taskName){
        TaskList list = getListOrThrow(listName);

        if(!list.hasTask(taskName)){
            throw new IllegalArgumentException("Task not found: " + taskName);
        }

        list.removeTask(taskName);
        save();
    }

    public void deleteList(String listName){
        if (!taskLists.containsKey(listName)) {
            throw new IllegalArgumentException("List not found: " + listName);
        }

        taskLists.remove(listName);
        save();
    }

    public Task movetask(String fromList, String toList, String taskName){
        TaskList sourceList = getListOrThrow(fromList);
        TaskList targetList = getListOrThrow(toList);

        if (!sourceList.hasTask(taskName)) {
            throw new IllegalArgumentException("Task not found: " + taskName);
        }

        Task task = sourceList.getTask(taskName);
        targetList.addTask(taskName, task);
        sourceList.removeTask(taskName);
        save();
        return task;
    }

    private TaskList getListOrThrow(String listName) {
        TaskList list = taskLists.get(listName);
        if (list == null) {
            throw new IllegalArgumentException("List not found: " + listName);
        }
        return list;
    }

    private void save(){
        try (PrintWriter out = new PrintWriter(new FileWriter(SAVE_FILE))) {
            for (Map.Entry<String, TaskList> entry : taskLists.entrySet()) {
                String listName = entry.getKey();
                TaskList list = entry.getValue();

                StringBuilder sb = new StringBuilder();
                sb.append(listName);

                for (Map.Entry<String, Task> taskEntry : list.getTasks().entrySet()) {
                    sb.append(";").append(taskEntry.getValue().getName());
                    sb.append(";").append(taskEntry.getValue().getDescription());
                }

                out.println(sb);
            }
        } catch (IOException e) {
            System.err.println("Failed to save file: " + e.getMessage());
        }
    }

    private void load() {
        File file = new File(SAVE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");

                if (parts.length < 1) continue;

                String listName = parts[0];
                TaskList list = new TaskList(listName);

                for (int i = 1; i < parts.length - 1; i += 2) {
                    String taskName = parts[i];
                    String taskDesc = parts[i + 1];
                    list.addTask(taskName, new Task(taskName, taskDesc));
                }

                taskLists.put(listName, list);
            }
        } catch (IOException e) {
            System.err.println("Error loading saved data: " + e.getMessage());
        }
    }
}
