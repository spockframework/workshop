package advanced2

import acme.Database
import spock.lang.Specification
import spock.lang.Subject


class DatabaseSpec extends Specification {
    @Subject
    Database db = Database.INSTANCE

    def cleanup() {
        db.nuke()
    }

    def "can add element"() {
        expect:
        db.size() == 0

        when:
        db.add("foo", "bar")

        then:
        db.size() == 1
        db.get("foo") == "bar"
    }

    def "can remove element"() {
        given:
        db.add("foo", "bar")

        expect:
        db.size() == 1
        db.get("foo") == "bar"

        when:
        db.delete("foo")

        then:
        db.size() == 0
        db.get("foo") == null
    }

    def "can list keys"() {
        given:
        db.add("foo", "bar")
        db.add("bar", "bar")
        db.add("blubb", "bar")

        expect:
        db.size() == 3
        db.getKeys() == ["foo", "bar", "blubb"] as Set
    }
}