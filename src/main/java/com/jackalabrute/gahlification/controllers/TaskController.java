package com.jackalabrute.gahlification.controllers;

import com.jackalabrute.gahlification.database.models.Task;
import com.jackalabrute.gahlification.exceptions.TaskNotFoundException;
import com.jackalabrute.gahlification.exceptions.statuscodes.IncorrectRequestException;
import com.jackalabrute.gahlification.exceptions.statuscodes.NotFoundException;
import com.jackalabrute.gahlification.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
public class TaskController {
    
    @Autowired
    private TaskService taskService;

    /**
     * Get task with provided taskId.
     *
     * @param taskId
     * @return
     */
    @GetMapping(path = "/tasks/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable String taskId) {
        try {
            Task task = taskService.findTaskById(UUID.fromString(taskId));
            return new ResponseEntity<>(task, HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestException(String.format("%s is not a valid UUID.", taskId));
        }
    }

    /**
     * Get all tasks.
     *
     * @return
     */
    @GetMapping(path = "/tasks")
    public ResponseEntity<List<Task>> getAllTasks() {
        List<Task> tasks = taskService.findAllTasks();
        return new ResponseEntity<>(tasks, HttpStatus.OK);
    }

    /**
     * Generates a new task.
     *
     * @param task
     * @return
     */
    @PostMapping(path = "/tasks")
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        try {
            if (!taskService.checkTaskForCreate(task)) {
                throw new IllegalArgumentException();
            }

            Task newTask = taskService.createTask(task);
            return new ResponseEntity<>(newTask, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestException("Incorrect task format, missing parameters.");
        }
    }

    /**
     * Updates a task.
     *
     * @param task
     * @return
     */
    @PutMapping(path = "/tasks")
    public ResponseEntity<Task> updateTask(@RequestBody Task task) {
        try {
            if (!taskService.checkTaskForUpdate(task)) {
                throw new IllegalArgumentException();
            }

            Task updatedTask = taskService.updateTask(task);
            return new ResponseEntity<>(updatedTask, HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestException("Incorrect task format, missing parameters.");
        }
    }

    /**
     * Deletes a task.
     *
     * @param taskId
     * @return
     */
    @DeleteMapping(path = "/tasks/{taskId}")
    public ResponseEntity<?> deleteTask(@PathVariable String taskId) {
        try {
            Task task = taskService.findTaskById(UUID.fromString(taskId));
            taskService.deleteTask(task.getTaskId());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (TaskNotFoundException e) {
            throw new NotFoundException(e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new IncorrectRequestException(String.format("%s is not a valid UUID.", taskId));
        }
    }
}