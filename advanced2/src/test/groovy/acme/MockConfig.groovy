package acme

import org.mockserver.integration.ClientAndServer
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MockConfig {
    @Bean
    ClientAndServer clientAndServer () {
        return ClientAndServer.startClientAndServer(0)
    }

    @Bean
    int mockserverPort(ClientAndServer clientAndServer) {
        return clientAndServer.getPort()
    }

    @Bean
    TestRestTemplate testRestTemplate (ClientAndServer clientAndServer) {
        return new TestRestTemplate(new RestTemplateBuilder().rootUri("http://localhost:${clientAndServer.port}/"))
    }
}
