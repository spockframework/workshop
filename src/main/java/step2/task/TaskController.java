package step2.task;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(path = "/tasks", produces = APPLICATION_JSON_VALUE)
public class TaskController {

    private final TaskService taskService;

    TaskController(final TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/{id}")
    ResponseEntity<Task> getTask(@PathVariable @NonNull String id) {
        return taskService.findTask(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    List<Task> getTasksToDo() {
        return taskService.findTasksToDo();
    }

    @GetMapping(path = "/overdue")
    List<Task> getTasksOverdue() {
        return taskService.findTasksOverdue();
    }

    @GetMapping(path = "/today")
    List<Task> getTasksDueToday() {
        return taskService.findTasksDueToday();
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Task> postTask(@RequestBody final Task task, UriComponentsBuilder uriComponentsBuilder) {
        Task createdTask = taskService.saveTask(task);
        return ResponseEntity
                .created(uriComponentsBuilder.path("tasks/{id}").buildAndExpand(createdTask.getId()).toUri())
                .body(taskService.saveTask(task));
    }

    @PatchMapping(path = "/{id}", consumes = APPLICATION_JSON_VALUE)
    ResponseEntity<Task> patchTask(@PathVariable @NonNull String id, @RequestBody final Task update) {
        return taskService.updateTask(id, update)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping(path = "/{id}")
    ResponseEntity<Void> deleteTask(@PathVariable @NonNull String id) {
        taskService.deleteTask(id);
        return ResponseEntity.ok().build();
    }
}
