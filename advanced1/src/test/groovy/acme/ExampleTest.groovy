package acme


import spock.lang.Rollup
import spock.lang.Specification

class ExampleTest extends Specification {

    def setupSpec() {
        println "Executing setupSpec"
    }

    def setup() {
        println "Executing setup"
    }

    def cleanupSpec() {
        println "Executing cleanupSpec"
    }

    def cleanup() {
        println "Executing cleanup"
    }

    def "a"() {
        println "Executing a"
        expect: true
    }

    def "b"(int index) {
        println "Executing b $index"
        expect: true

        where:
        index << (1..2)
    }

    @Rollup
    def "c"(int index) {
        println "Executing c $index"
        expect: true

        where:
        index << (1..2)
    }
}
