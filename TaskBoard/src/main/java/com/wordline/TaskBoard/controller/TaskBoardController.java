package com.wordline.TaskBoard.controller;

import com.wordline.TaskBoard.model.Task;
import com.wordline.TaskBoard.model.TaskList;
import com.wordline.TaskBoard.service.TaskBoardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskBoardController {

    private final TaskBoardService service;

    public TaskBoardController(TaskBoardService service){
        this.service = service;
    }

    @GetMapping("/lists")
    public Map<String, TaskList> getLists(){
        return service.getLists();
    }

    @PostMapping("/lists")
    public ResponseEntity<TaskList> createList(@RequestBody Map<String, String> request){
        String listName = request.get("name");

        if(listName == null || listName.trim().isEmpty()){
            return ResponseEntity.badRequest().build();
        }

        TaskList list = service.createList(listName);
        return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @PostMapping("/lists/{listName}/tasks")
    public ResponseEntity<Task> addTask(@PathVariable String listName, @RequestBody Map<String, String> request){
        String taskName = request.get("name");
        String taskDescription = request.get("description");

        if (taskName == null || taskName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Task task = service.addTask(listName, taskName,taskDescription);
        return ResponseEntity.status(HttpStatus.CREATED).body(task);
    }

    @PutMapping("/lists/{listName}/tasks/{taskName}")
    public ResponseEntity<Task> updateTask(@PathVariable String listName, @PathVariable String taskName, @RequestBody Map<String, String> request){
        String newName = request.get("name");
        String newDescription = request.get("description");

        if (newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Task task = service.updateTask(listName, taskName, newName, newDescription);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lists/{listName}/tasks/{taskName}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable String listName,
            @PathVariable String taskName) {

        service.deleteTask(listName, taskName);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/lists/{listName}")
    public ResponseEntity<Void> deleteList(@PathVariable String listName) {
        service.deleteList(listName);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/lists/{fromList}/tasks/{taskName}/move")
    public ResponseEntity<Task> moveTask(@PathVariable String fromList, @PathVariable String taskName, @RequestBody Map<String, String> request){
        String toList = request.get("toList");

        if (toList == null || toList.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Task task = service.movetask(fromList,toList, taskName);
        return ResponseEntity.ok(task);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", e.getMessage()));
    }
}
