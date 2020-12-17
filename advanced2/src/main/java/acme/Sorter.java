package acme;

import java.util.Comparator;
import java.util.List;

public class Sorter {
    private final ChaosMath math = new ChaosMath();

    public void sort(List<Integer> input) {
        input.sort(math::compare);
        Worker.work(50);
    }

    public void sortStrings(List<String> strings) {
        strings.sort(ChaosMath.isChaos() ? Comparator.reverseOrder() : Comparator.naturalOrder());
    }
}
