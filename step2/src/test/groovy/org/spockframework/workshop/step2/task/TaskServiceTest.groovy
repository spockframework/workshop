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
        pattern  << [
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
