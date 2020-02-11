package org.spockframework.workshop.step2.task

import io.restassured.module.mockmvc.response.MockMvcResponse
import spock.lang.Specification
import spock.lang.Subject

import static groovy.json.JsonOutput.toJson
import static io.restassured.module.mockmvc.RestAssuredMockMvc.*
import static java.util.UUID.randomUUID

class TaskControllerApiSpec extends Specification {

    TaskService taskServiceMock = Mock()

    @Subject
    TaskController taskController = new TaskController(taskServiceMock)

    def setup() {
        standaloneSetup(taskController)
    }

    def "GET /tasks/<taskId> returns the task"() {
        given:
        Task task = new Task(id: randomUUID().toString(), title: "my task", description: "some task to be found")
        taskServiceMock.findTask(task.id) >> Optional.of(task)

        when:
        MockMvcResponse response = get("tasks/${task.id}")

        then:
        verifyAll(response) {
            statusCode() == 200
            contentType() == "application/json"
            body.jsonPath().getString("id") == task.id
        }
    }

    def "GET /tasks/ returns all tasks to do"() {
        given:
        Task task = new Task("my task", "some task to do")
        taskServiceMock.findTasksToDo() >> [task]

        when:
        MockMvcResponse response = get("tasks/")

        then:
        verifyAll(response) {
            statusCode() == 200
            contentType() == "application/json"
            body.jsonPath().getString("[0].id") == task.id
        }
    }

    def "GET /tasks/overdue returns all tasks overdue"() {
        given:
        Task task = new Task("my task", "some task overdue")
        taskServiceMock.findTasksOverdue() >> [task]

        when:
        MockMvcResponse response = get("tasks/overdue")

        then:
        verifyAll(response) {
            statusCode() == 200
            contentType() == "application/json"
            body.jsonPath().getString("[0].id") == task.id
        }
    }

    def "GET /tasks/today returns all tests due today"() {
        given:
        Task task = new Task("my task", "some task due today")
        taskServiceMock.findTasksDueToday() >> [task]

        when:
        MockMvcResponse response = get("tasks/today")

        then:
        verifyAll(response) {
            statusCode() == 200
            contentType() == "application/json"
            body.jsonPath().getString("[0].id") == task.id
        }
    }

    def "POST /tasks/ creates a new task"() {
        given:
        Task task = new Task("my task", "some new task")
        taskServiceMock.saveTask(_ as Task) >> new Task(id: randomUUID().toString(), title: task.title, description: task.description)

        when:
        MockMvcResponse response = with()
            .contentType("application/json")
            .body(toJson(title: "my task", desciption: "some new task"))
            .post("tasks/")

        then:
        verifyAll(response) {
            statusCode() == 201
            header("location") ==~ /^.*\/tasks\/[a-f0-9-]+$/
            body().jsonPath().getString("id")
            body().jsonPath().getString("title") == task.title
        }
    }

    def "PATCH /tasks/<taskId> updates a task"() {
        given:
        String taskId = randomUUID()
        Task update = new Task("my task", "some task to be updated")
        taskServiceMock.updateTask(taskId, update) >> Optional.of(update)

        when:
        MockMvcResponse response = with()
            .body(toJson(update))
            .contentType("application/json")
            .patch("tasks/${taskId}")

        then:
        verifyAll(response) {
            statusCode() == 200
            body().jsonPath().getString("title") == update.title
        }
    }

    def "DELETE /tasks/<taskId> deletes a task"() {
        given:
        String taskId = randomUUID()

        when:
        MockMvcResponse response = delete("tasks/${taskId}")

        then:
        1 * taskServiceMock.deleteTask(taskId)

        and:
        response.statusCode() == 200
    }
}
