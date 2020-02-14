package step1

import spock.lang.*

import step1.FixedLengthIntStack

class FixedLengthIntStackSpec extends Specification {

    @Subject
    FixedLengthIntStack stack = new FixedLengthIntStack(10)

    def "a new stack is empty"() {
        expect: "stack has no items"
        stack.size == 0
    }

    def "pushing an item increases the stack size by one"() {
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
        int i = 5

        when: "pushing it"
        stack.push(i)

        and: "popping it"
        int result = stack.pop()

        then: "popped item is the same as initial item"
        result == i
    }

    def "popping an empty stack leads to an IndexOutOfBoundsException"() {
        when: "pop one item"
        stack.pop()

        then: "IndexOutOfBoundsException is thrown"
        thrown(IndexOutOfBoundsException)
    }

    def "pushing an item on a full stack leads to an IndexOutOfBoundsException"() {
        given: "a full stack"
        stack.capacity.times { stack.push(it) }

        when: "add one item"
        stack.push(1)

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
        elements           | expected
        []                 | 0
        [1]                | 1
        [1, 2, 3]          | 3
        [1, 2, 3, 4, 5, 6] | 6
    }

    @Unroll
    def "the stack follows the First-In-Last-Out order"(List<Integer> elements) {
        when: "adding elements"
        elements.each { stack.push(it) }

        and: "popping elements"
        def result = []
        while (stack.size > 0) {
            result << stack.pop()
        }

        then: "their order is reversed"
        result == elements.reverse()

        where:
        elements << [
            [0],
            [0, 1],
            [0, 1, 2],
            [1, 2, 3, 4, 5, 6]
        ]
    }

    @Unroll
    def "a stack can be created with varying capacity (#capacity)"(int capacity) {
        when: "creating a new stack with given capacity"
        def result = new FixedLengthIntStack(capacity)

        then: "the new stack has the given capacity"
        result.capacity == capacity

        where:
        capacity << (1..10).collect()
    }
}
