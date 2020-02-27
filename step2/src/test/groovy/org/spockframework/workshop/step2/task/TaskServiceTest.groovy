package org.spockframework.workshop.step2.task

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeParseException

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll

class TaskServiceTest extends Specification {
    Clock clock = Clock.fixed(Instant.parse("2020-03-12T12:00:00Z"), ZoneId.of("CET"))

    TaskRepository repository = Mock()

    @Subject
    TaskService taskService = new TaskService(repository, clock)

    def "Autowired constructor sets its own clock"() {
        expect:
        new TaskService(repository).clock != null
    }

    def "Task from plaintext is created correctly and saved via repository"() {
        when:
        def result = taskService.createTaskFromPlainText('hello\n\nworld @today')

        then:
        1 * repository.save(_ as Task) >> { Task t -> t.tap { it.id = 'uuid' } }

        with(result) {
            title == 'hello'
            description == 'world @today'
            dueDate == LocalDate.of(2020, 03, 12)
            id == 'uuid'
            !done
        }
    }

    def "findTasksToDo delegates to repository correctly"() {
        when:
        def result = taskService.findTasksToDo()

        then:
        1 * repository.findAllByDoneIsFalse() >> []
        result == []
    }

    def "findTasksOverdue delegates to repository correctly"() {
        when:
        def result = taskService.findTasksOverdue()

        then:
        1 * repository.findAllByDoneIsFalseAndDueDateBefore(LocalDate.of(2020, 03, 12)) >> []
        result == []
    }

    def "findTasksDueToday delegates to repository correctly"() {
        when:
        def result = taskService.findTasksDueToday()

        then:
        1 * repository.findAllByDoneIsFalseAndDueDate(LocalDate.of(2020, 03, 12)) >> []
        result == []
    }

    def "saveTask delegates to repository correctly"() {
        given:
        def task = new Task(description: 'hello')
        def saved = new Task(description: 'hello', id: 'uuid')
        when:
        def result = taskService.saveTask(task)

        then:
        1 * repository.save({ it.description == 'hello' } as Task) >> saved
        result == saved
    }

    def "findTask delegates to repository correctly"() {
        when:
        def result = taskService.findTask('taskId')

        then:
        1 * repository.findById('taskId') >> Optional.empty()
        !result.present
    }

    def "deleteTask delegates to repository correctly"() {
        when:
        taskService.deleteTask('taskId')

        then:
        1 * repository.deleteById('taskId')
    }

    @Unroll("updateTask with #update will set #unrollDesc")
    def "updateTask only modifies non-null fields"(Task update, String upTitle, String upDescription, LocalDate upDueDate, Boolean upDone, String unrollDesc) {
        given:
        def existingTask = new Task(id: 'uuid',
                title: 'hello',
                description: 'world',
                dueDate: LocalDate.of(2020, 1, 1),
                done: false)
        def exTitle = upTitle ?: existingTask.title
        def exDescription = upDescription ?: existingTask.description
        def exDueDate = upDueDate ?: existingTask.dueDate
        def exDone = upDone ?: existingTask.done

        when:
        def result = taskService.updateTask('uuid', update)

        then:
        1 * repository.findById('uuid') >> Optional.of(existingTask)
        1 * repository.save(_ as Task) >> { Task t -> t }
        result.present
        with(result.get()) {
            title == exTitle
            description == exDescription
            dueDate == exDueDate
            done == exDone
            id == 'uuid'
        }

        where:
        update                                        | upTitle | upDescription | upDueDate                  | upDone
        new Task()                                    | null    | null          | null                       | null
        new Task(title: 'foo')                        | 'foo'   | null          | null                       | null
        new Task(description: 'bar')                  | null    | 'bar'         | null                       | null
        new Task(dueDate: LocalDate.of(2020, 03, 12)) | null    | null          | LocalDate.of(2020, 03, 12) | null
        new Task(done: true)                          | null    | null          | null                       | true
        new Task(title: 'foo',
                description: 'bar',
                dueDate: LocalDate.of(2020, 03, 12),
                done: true)                           | 'foo'   | 'bar'         | LocalDate.of(2020, 03, 12) | true

        unrollDesc = "${upTitle ? ' title to ' + upTitle : ''}${upDescription ? ' description to ' + upDescription : ''}${upDueDate ? ' dueDate to ' + upDueDate : ''}${upDone ? ' done to ' + upDone : ''}"

    }

    @Unroll
    def "plaintext without double newline will be parsed to description"(String input) {
        when:
        def task = taskService.parseTask(input)

        then:
        with(task) {
            description == input
            !title
            !dueDate
            !done
            !id
        }

        where:
        input << [
                'Lorem ipsum',
                '''\
                   Lorem ipsum dolor sit amet, consetetur sadipscing elitr,
                   sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat,
                   sed diam voluptua.
                '''.stripIndent()
        ]
    }

    @Unroll
    def "plaintext double newline will be parsed to title and description"(String title, String description) {
        given:
        String input = "$title\n\n$description"

        when:
        def task = taskService.parseTask(input)

        then:
        task.title == title
        task.description == description
        with(task) {
            !dueDate
            !done
            !id
        }

        where:
        title                        | description
        'Hello'                      | 'World'
        ''                           | 'World'
        'Hello'                      | ''
        'Lorem ipsum dolor sit amet' | 'sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat'
        'Lorem ipsum dolor sit amet' | 'sed diam nonumy\n\neirmod tempor invidunt\n\nut labore et dolore magna aliquyam erat'
        'Lorem ipsum dolor sit amet' | ''
    }

    @Unroll("Input [#pattern] is parsed as #expected")
    def "@-Date pattern is parsed correctly"(String pattern, LocalDate expected) {
        expect:
        taskService.parseDueDate(pattern) == expected

        where:
        pattern          | expected
        '@today'         | LocalDate.of(2020, 03, 12)
        '@tomorrow'      | LocalDate.of(2020, 03, 13)
        '@2021-01-05'    | LocalDate.of(2021, 01, 05)
        '@nextWeek'      | LocalDate.of(2020, 03, 16)
        '@nextMonday'    | LocalDate.of(2020, 03, 16)
        '@nextTuesday'   | LocalDate.of(2020, 03, 17)
        '@nextWednesday' | LocalDate.of(2020, 03, 18)
        '@nextThursday'  | LocalDate.of(2020, 03, 19)
        '@nextFriday'    | LocalDate.of(2020, 03, 13)
        '@nextSaturday'  | LocalDate.of(2020, 03, 14)
        '@nextSunday'    | LocalDate.of(2020, 03, 15)
        '@nextMonth'     | LocalDate.of(2020, 04, 01)
    }

    @Unroll("Input [#pattern] is invalid")
    def "@-Date pattern not recognized if not surrounded by whitespace or start/end of line"(String pattern) {
        expect:
        taskService.parseDueDate(pattern) == null

        where:
        pattern << [
                'Text@today',
                'Text @todayAfternoon',
        ]
    }

    def "Only one @-Date pattern is allowed"() {
        when:
        taskService.parseDueDate('@today @today')

        then:
        thrown(IllegalArgumentException)
    }

    def "Invalid local date patterns throw exception"() {
        when:
        taskService.parseDueDate('@2020-14-01')

        then:
        thrown(DateTimeParseException)
    }
}
