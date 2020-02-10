package step2.task;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findAllByDoneIsFalse();

    List<Task> findAllByDoneIsFalseAndDueDateBefore(LocalDate date);

    List<Task> findAllByDoneIsFalseAndDueDate(LocalDate date);
}
