package org.spockframework.workshop.step2.task;

import java.time.Clock;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private static final Pattern DOUBLE_NEWLINE = Pattern.compile("\r?\n\r?\n");

    private static final Pattern DUE_DATE_PATTERN = Pattern.compile("(?<=^|\\s)@(?:"
                    + "(?<keyword>today|tomorrow)|"
                    + "next(?<next>monday|tuesday|wednesday|thursday|friday|saturday|sunday|week|month)|"
                    + "(?<date>\\d{4}-\\d{2}-\\d{2})"
                    + ")(?=$|\\s)",
            Pattern.CASE_INSENSITIVE);

    private final TaskRepository taskRepository;

    private final Clock clock;

    @Autowired
    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
        this.clock = Clock.systemDefaultZone();
    }

    TaskService(TaskRepository taskRepository, Clock clock) {
        this.taskRepository = taskRepository;
        this.clock = clock;
    }

    public List<Task> findTasksToDo() {
        return taskRepository.findAllByDoneIsFalse();
    }

    public List<Task> findTasksOverdue() {
        return taskRepository.findAllByDoneIsFalseAndDueDateBefore(LocalDate.now(clock));
    }

    public List<Task> findTasksDueToday() {
        return taskRepository.findAllByDoneIsFalseAndDueDate(LocalDate.now(clock));
    }

    public Task saveTask(Task task) {
        return taskRepository.save(task);
    }

    public Optional<Task> findTask(@NonNull String id) {
        return taskRepository.findById(id);
    }

    public Optional<Task> updateTask(@NonNull String id, @NonNull Task update) {
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

    public void deleteTask(@NonNull String id) {
        taskRepository.deleteById(id);
    }

    public Task createTaskFromPlainText(@NonNull String input) {
        Task task = parseTask(input);
        return taskRepository.save(task);
    }

    private Task parseTask(@NonNull String input) {
        Task task = new Task();
        task.setDueDate(parseDueDate(input));
        String[] split = DOUBLE_NEWLINE.split(input, 2);
        task.setTitle(split.length == 2 ? split[0] : null);
        task.setDescription(split.length == 2 ? split[1] : split[0]);
        return task;
    }

    @Nullable
    private LocalDate parseDueDate(@NonNull String input) {
        Matcher matcher = DUE_DATE_PATTERN.matcher(input);
        if (matcher.find()) {
            LocalDate result = null;
            String date = matcher.group("date");
            String keyword = matcher.group("keyword");
            String next = matcher.group("next");
            if (date != null) {
                result = LocalDate.parse(date);
            } else if (keyword != null) {
                switch (keyword.toLowerCase()) {
                case "today":
                    result = LocalDate.now(clock);
                    break;
                case "tomorrow":
                    result = LocalDate.now(clock).plusDays(1);
                    break;

                default:
                    throw new IllegalStateException("This should never happen(tm)");
                }
            } else if (next != null) {
                switch (next.toLowerCase()) {
                case "week":
                    result = LocalDate.now(clock).with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                    break;
                case "month":
                    result = LocalDate.now(clock).with(TemporalAdjusters.firstDayOfNextMonth());
                    break;

                default:
                    DayOfWeek dayOfWeek = DayOfWeek.valueOf(next.toUpperCase());
                    result = LocalDate.now(clock).with(TemporalAdjusters.next(dayOfWeek));
                    break;
                }
            }

            if (matcher.find()) {
                throw new IllegalArgumentException("Only one @ pattern occurrence is supported.");
            }
            return result;
        }
        return null;
    }
}
