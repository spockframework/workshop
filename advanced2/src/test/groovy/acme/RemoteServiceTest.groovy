package acme


import static org.mockserver.model.HttpRequest.request
import static org.mockserver.model.HttpResponse.response

import org.mockserver.client.MockServerClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus

import spock.lang.ResourceLock
import spock.lang.Specification

@ResourceLock(SharedResources.MOCKSERVER)
@SpringBootTest
class RemoteServiceTest extends Specification {

    @Autowired
    MockServerClient mockServerClient

    @Autowired
    TestRestTemplate restTemplate

    def cleanup() {
        mockServerClient.reset()
    }

    def "can call get Motd"() {
        given:
        mockServerClient.when(request('/motd'))
                .respond(response('Yo mate'))

        when:
        def result = restTemplate.getForEntity('/motd', String)

        then:
        result.statusCode == HttpStatus.OK
        result.body == 'Yo mate'

    }

    def "can call get Motd again"() {
        given:
        mockServerClient.when(request('/motd'))
                .respond(response('Sup mate'))

        when:
        def result = restTemplate.getForEntity('/motd', String)

        then:
        result.statusCode == HttpStatus.OK
        result.body == 'Sup mate'

    }

    def "set Motd"() {
        given:
        def request = request('/motd').withMethod("POST").withBody("new motd")
        mockServerClient.when(request)
                .respond(response("updated motd"))

        when:
        def result = restTemplate.postForEntity('/motd', "new motd", String)

        then:
        result.statusCode == HttpStatus.OK
        result.body == 'updated motd'
        mockServerClient.verify(request)
    }
}
