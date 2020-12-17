package acme

import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

import java.time.Instant

import org.mockserver.client.MockServerClient
import org.spockframework.runtime.model.parallel.ExecutionMode
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

import spock.lang.Execution
import spock.lang.Specification

@SpringBootTest
class AnotherRemoteServiceTest extends Specification {

    @Autowired
    MockServerClient mockServerClient

    @Autowired
    TestRestTemplate restTemplate

    def cleanup() {
        mockServerClient.reset()
    }

    def "can call dateTime"() {
        given:
        String dateTime = Instant.now().toString()
        mockServerClient.when(request('/dateTime'))
                .respond(response(dateTime))

        when:
        def result = restTemplate.getForEntity('/dateTime', String)

        then:
        result.statusCode == HttpStatus.OK
        result.body == dateTime

        where:
        iteration << (1..10)
    }

}