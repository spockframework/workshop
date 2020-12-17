package acme


import spock.lang.Specification
import spock.lang.Subject


class SorterSpec extends Specification {

    @Subject
    Sorter sorter = new Sorter()

    def "can sort list of integers"(List<Integer> input) {
        given:
        def reference = new ArrayList(input)
        reference.sort()

        when:
        sorter.sort(input)

        then:
        input == reference

        where:
        input << scramble((1..15).collect(), 20)
    }

    static List<List<Integer>> scramble(List<Integer> input, int samples) {
        def output = []
        samples.times {
            output << new ArrayList<>(input).tap { it.shuffle() }
        }
        return output
    }
}