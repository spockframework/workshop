package org.spockframework.workshop.step2.task

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

import org.spockframework.spring.SpringBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.servlet.MockMvc

import spock.lang.Specification

@WebMvcTest(TaskController)
class TaskControllerIT extends Specification {

    @Autowired
    MockMvc mockMvc

    @SpringBean
    TaskService taskService = Mock()

    def "GET empty returns 200"() {
        given:
        taskService.findTasksToDo() >> []

        expect:
        mockMvc.perform(get("/tasks"))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json("[]"))
    }
}
