package org.spockframework.workshop.step2.task

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

import static java.time.LocalDate.now as today

class TaskServiceSpec extends Specification {

    TaskRepository repositoryMock = Mock()

    @Subject
    TaskService taskService = new TaskService(repositoryMock)

    def "findTasksToDo returns all tasks found in repository"() {
        given:
        List<Task> tasksToDo = [new Task("my task", "a task to do")]
        repositoryMock.findAllByDoneIsFalse() >> tasksToDo

        expect:
        taskService.findTasksToDo() == tasksToDo
    }

    def "findTasksOverdue returns all tasks found in repository"() {
        given:
        List<Task> tasksOverdue = [new Task("my task", "a task overdue")]
        repositoryMock.findAllByDoneIsFalseAndDueDateBefore(today()) >> tasksOverdue

        expect:
        taskService.findTasksOverdue() == tasksOverdue
    }

    def "findTasksDueToday returns all tasks found in repository"() {
        given:
        List<Task> tasksDueToday = [new Task("my task", "a task due today")]
        repositoryMock.findAllByDoneIsFalseAndDueDate(today()) >> tasksDueToday

        expect:
        taskService.findTasksDueToday() == tasksDueToday
    }

    def "saveTask saves the given task in the repository"() {
        given:
        Task task = new Task("my task", "a new task")
        1 * repositoryMock.save(task) >> { Task savedTask -> savedTask }

        expect:
        taskService.saveTask(task) == task
    }

    def "findTask returns the task if found in repository"() {
        given:
        Task task = new Task(id: "taskid", title: "my task", description: "a task to be found")
        repositoryMock.findById(task.id) >> Optional.of(task)

        expect:
        taskService.findTask(task.id).get() == task
    }

    @Unroll
    def "updateTask can update #changes"(String changes, Task updates) {
        given:
        Task original = new Task(id: "taskid", title: "original title", description: "original description",
            dueDate: today().plusDays(1), done: false)
        repositoryMock.findById(original.id) >> Optional.of(original)
        repositoryMock.save(_ as Task) >> { Task task -> task }

        when:
        Task updatedTask = taskService.updateTask(original.id, updates).get()

        then:
        verifyAll(updatedTask) {
            id == original.id
            title == updates.title ?: original.title
            description == updates.description ?: original.description
            dueDate == updates.dueDate ?: original.dueDate
            done == (updates.done != null ? updates.done : original.done)
        }

        where:
        changes       | updates
        "nothing"     | new Task()
        "title"       | new Task(title: "updated title")
        "description" | new Task(description: "updated description")
        "due date"    | new Task(dueDate: today().plusDays(2))
        "done state"  | new Task(done: true)
        "everything"  | new Task(title: "updated title", description: "updated description", dueDate: today().plusDays(2), done: true)
    }

    def "deleteTask deletes the task from the repository"() {
        given:
        Task task = new Task(id: "taskid", title: "my task", description: "a task to be found")

        when:
        taskService.deleteTask(task.id)

        then:
        1 * repositoryMock.deleteById(task.id)
    }
}
