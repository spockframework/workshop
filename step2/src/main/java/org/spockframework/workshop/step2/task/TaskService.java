package org.spockframework.workshop.step2.task;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
class TaskService {

    private final TaskRepository taskRepository;

    TaskService(final TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    List<Task> findTasksToDo() {
        return taskRepository.findAllByDoneIsFalse();
    }

    List<Task> findTasksOverdue() {
        return taskRepository.findAllByDoneIsFalseAndDueDateBefore(LocalDate.now());
    }

    List<Task> findTasksDueToday() {
        return taskRepository.findAllByDoneIsFalseAndDueDate(LocalDate.now());
    }

    Task saveTask(final Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> findTask(@NonNull final String id) {
        return taskRepository.findById(id);
    }

    public Optional<Task> updateTask(@NonNull final String id, @NonNull Task update) {
        return taskRepository.findById(id).map(task -> {
            if (update.getTitle() != null) {
                task.setTitle(update.getTitle());
            }
            if (update.getDescription() != null) {
                task.setDescription(update.getDescription());
            }
            if (update.getDueDate() != null) {
                task.setDueDate(update.getDueDate());
            }
            if (update.isDone() != null) {
                task.setDone(update.isDone());
            }
            return taskRepository.save(task);
        });
    }

    public void deleteTask(String id) {
        taskRepository.deleteById(id);
    }
}
