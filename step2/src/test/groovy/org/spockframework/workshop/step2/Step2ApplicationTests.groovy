package org.spockframework.workshop.step2;

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class Step2ApplicationTests extends Specification {

	def "contextLoads"() {
        expect:
        true
	}
}
