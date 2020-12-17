package acme

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders

import spock.lang.Specification

@AutoConfigureMockMvc
@SpringBootTest
class DbControllerTest extends Specification {
    @Autowired
    Database db

    @Autowired
    MockMvc mockMvc

    @Autowired
    DbController controller

    def cleanup() {
        db.nuke()
    }

    def "can add element"() {
        expect:
        db.size() == 0

        when:
        mockMvc.perform(put('/db/{key}', 'foo').contentType(MediaType.TEXT_PLAIN).content('bar'))

        then:
        db.size() == 1
        db.get("foo") == "bar"
    }

    def "can remove element"() {
        given:
        db.add("foo", "bar")

        expect:
        db.size() == 1
        db.get("foo") == "bar"

        when:
        mockMvc.perform(delete('/db/{key}', 'foo'))

        then:
        db.size() == 0
        db.get("foo") == null
    }

    def "can list keys"() {
        given:
        db.add("foo", "bar")
        db.add("bar", "bar")
        db.add("blubb", "bar")

        when:
        def result = mockMvc.perform(get('/db')).andReturn().response.getContentAsString()

        then:
        result == '["bar","blubb","foo"]'
    }
}
