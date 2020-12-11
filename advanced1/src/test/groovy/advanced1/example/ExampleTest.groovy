package advanced1.example

import spock.lang.*

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
        println "Executing A"
        expect: true
    }

    def "b"(int index) {
        println "Executing B $index"
        expect: true

        where:
        index << (1..2)
    }

    @Rollup
    def "c"(int index) {
        println "Executing C $index"
        expect: true

        where:
        index << (1..2)
    }
}
