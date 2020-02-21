package step1

import spock.lang.Specification
import spock.lang.Subject
import spock.lang.Unroll
import step1.FixedLengthIntStack

class FixedLengthIntStackSpec extends Specification {

    @Subject
    FixedLengthIntStack stack = new FixedLengthIntStack(10)

    def "a new stack is empty"() {
        expect: "stack size is 0"
        stack.size == 0
    }

    def "pushing an item increases the stack size by one"(){
        when: "add one item"
        stack.push(1)

        then: "stack size is 1 larger than before"
        stack.size == old(stack.size) + 1
    }

    def "popping an item from a stack with items decreases its size by one"() {
        given: "a stack with 5 items"
        5.times { stack.push(it) }

        when: "pop one item"
        stack.pop()

        then: "stack size has decreased by one"
        stack.size == old(stack.size) - 1
    }

    def "pushing an item and then popping it returns the same item"() {
        given: "an item"
        int item = 1

        and: "the item is pushed onto the stack"
        stack.push(item)

        expect: "popping it returns the same item"
        stack.pop() == item
    }

    def "popping an empty stack leads to an IndexOutOfBoundsException"() {
        when: "pop one item"
        stack.pop()

        then: "IndexOutOfBoundsException is thrown"
        thrown(IndexOutOfBoundsException)
    }

    def "pushing an item on a full stack leads to an IndexOutOfBoundsException"() {
        given: "a full stack"
        10.times { stack.push(it) }

        when: "add one item"
        stack.push(10)

        then: "IndexOutOfBoundsException is thrown"
        thrown(IndexOutOfBoundsException)
    }

    @Unroll("A stack with #elements items has size #expected")
    def "size corresponds with element count"(List<Integer> elements, int expected) {
        given: "a stack with elements"
        elements.each { stack.push(it) }

        expect: "stack size corresponds with element count "
        stack.size == expected

        where:
        elements | expected
        []       | 0
        1..10    | 10
        1..5     | 5
        2..7     | 6
    }

    def "the stack follows the First-In-Last-Out order"(List<Integer> elements) {
        when: "adding elements"
        elements.each { stack.push(it) }

        and: "popping elements"
        List<Integer> popped = []
        elements.size().times { popped.add(stack.pop()) }

        then: "their order is reversed"
        popped == elements.reverse()

        where:
        elements << [[0], [1,2,3,4,5]]
    }

    def "a stack can be created with varying capacity"() {
        when: "creating a new stack with given capacity"
        def stack = new FixedLengthIntStack(5)

        then: "the new stack has the given capacity"
        stack.capacity == 5
    }
}
