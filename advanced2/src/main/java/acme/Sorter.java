package acme;

import java.util.List;

public class Sorter {
    private final ChaosMath math = new ChaosMath();

    public void sort(List<Integer> input) {
        input.sort(math::compare);
        Worker.work(50);
    }
}
