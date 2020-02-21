package step1;

public class FixedLengthIntStack {
    private final int[] store;
    private int current = -1;

    public FixedLengthIntStack(int capacity){
        this.store = new int[capacity];
    }

    public int getSize() {
        return current + 1;
    }

    public int getCapacity() {
        return store.length;
    }

    public int pop() {
        return store[current--];
    }

    public void push(int item) {
        store[++current] = item;
    }
}
