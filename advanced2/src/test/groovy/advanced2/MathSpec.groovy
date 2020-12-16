package advanced2

import acme.ChaosMath
import spock.lang.Specification
import spock.lang.Subject


class MathSpec extends Specification {

    @Subject
    ChaosMath math = new ChaosMath();

    def "can compute max"(int a, int b, int result) {
        expect:
        math.max(a, b) == result

        where:
        a  | b  | result
        79 | 44 | 79
        59 | 31 | 59
        22 | 28 | 28
        91 | 52 | 91
        95 | 49 | 95
        49 | 28 | 49
        85 | 82 | 85
        60 | 92 | 92
        84 | 39 | 84
        45 | 31 | 45
        48 | 64 | 64
        58 | 25 | 58
        21 | 91 | 91
        21 | 35 | 35
        74 | 81 | 81
        32 | 4  | 32
        35 | 41 | 41
        81 | 70 | 81
        33 | 10 | 33
        76 | 27 | 76
        4  | 18 | 18
        42 | 44 | 44
        16 | 43 | 43
        99 | 21 | 99
        20 | 81 | 81
        60 | 35 | 60
        77 | 95 | 95
        58 | 22 | 58
        48 | 80 | 80
        67 | 14 | 67
    }

    def "can compute min"(int a, int b, int result) {
        expect:
        math.min(a, b) == result

        where:
        a  | b  | result
        24 | 1  | 1
        86 | 20 | 20
        67 | 94 | 67
        73 | 12 | 12
        35 | 40 | 35
        82 | 91 | 82
        26 | 83 | 26
        49 | 18 | 18
        40 | 87 | 40
        93 | 53 | 53
        97 | 80 | 80
        17 | 86 | 17
        18 | 99 | 18
        60 | 89 | 60
        31 | 31 | 31
        6  | 4  | 4
        42 | 3  | 3
        3  | 6  | 3
        33 | 41 | 33
        94 | 43 | 43
        42 | 1  | 1
        66 | 22 | 22
        20 | 88 | 20
        21 | 96 | 21
        62 | 24 | 24
        76 | 89 | 76
        0  | 20 | 0
        87 | 33 | 33
        78 | 65 | 65
        76 | 93 | 76
    }


    def "can compare"(int a, int b, int result) {
        expect:
        Integer.compare(a, b) == result

        where:
        a  | b  | result
        7  | 50 | -1
        20 | 24 | -1
        62 | 24 | 1
        32 | 55 | -1
        20 | 81 | -1
        84 | 20 | 1
        0  | 34 | -1
        72 | 62 | 1
        24 | 99 | -1
        52 | 68 | -1
        65 | 51 | 1
        8  | 96 | -1
        54 | 15 | 1
        97 | 64 | 1
        4  | 51 | -1
        75 | 66 | 1
        69 | 10 | 1
        10 | 51 | -1
        30 | 41 | -1
        40 | 87 | -1
        39 | 7  | 1
        53 | 3  | 1
        23 | 24 | -1
        30 | 59 | -1
        20 | 36 | -1
        12 | 59 | -1
        16 | 12 | 1
        8  | 29 | -1
        83 | 52 | 1
        48 | 14 | 1
    }
}