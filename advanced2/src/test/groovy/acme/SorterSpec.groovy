package acme

import org.spockframework.runtime.model.parallel.ResourceAccessMode
import org.spockframework.runtime.model.parallel.Resources

import spock.lang.ResourceLock
import spock.lang.Specification
import spock.lang.Subject


@ResourceLock(value= Resources.SYSTEM_PROPERTIES, mode = ResourceAccessMode.READ)
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

    def "can sort list of strings"(List<String> input) {
        given:
        def reference = new ArrayList(input)
        reference.sort()

        when:
        sorter.sortStrings(input)

        then:
        input == reference

        where:
        input << scramble((1..15).collect{it.toString()}, 20)
    }

    static <T> List<List<T>> scramble(List<T> input, int samples) {
        def output = []
        samples.times {
            output << new ArrayList<>(input).tap { it.shuffle() }
        }
        return output
    }
}
