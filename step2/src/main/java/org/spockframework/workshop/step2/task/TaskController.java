package org.spockframework.workshop.step2.task;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(path = "/tasks", produces = APPLICATION_JSON_VALUE)
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Task> getTask(@PathVariable @NonNull String id) {
        return taskService.findTask(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Task> getTasksToDo() {
        return taskService.findTasksToDo();
    }

    @GetMapping(path = "/overdue")
    public List<Task> getTasksOverdue() {
        return taskService.findTasksOverdue();
    }

    @GetMapping(path = "/today")
    public List<Task> getTasksDueToday() {
        return taskService.findTasksDueToday();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> postTask(@RequestBody Task task, UriComponentsBuilder uriComponentsBuilder) {
        Task createdTask = taskService.saveTask(task);
        return ResponseEntity
                .created(uriComponentsBuilder.path("tasks/{id}").buildAndExpand(createdTask.getId()).toUri())
                .body(taskService.saveTask(task)); // TODO - intentional
    }

    @PostMapping(consumes = TEXT_PLAIN_VALUE)
    public ResponseEntity<Task> postPlainTask(@RequestBody String task, UriComponentsBuilder uriComponentsBuilder) {
        Task createdTask = taskService.createTaskFromPlainText(task);
        return ResponseEntity
                .created(uriComponentsBuilder.path("tasks/{id}").buildAndExpand(createdTask.getId()).toUri())
                .body(createdTask);
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<Task> patchTask(@PathVariable @NonNull String id, @RequestBody Task update) {
        return taskService.updateTask(id, update)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable @NonNull String id) {
        taskService.deleteTask(id);
        return ResponseEntity.noContent().build();
    }
}
