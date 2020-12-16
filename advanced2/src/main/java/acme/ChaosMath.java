package acme;

public class ChaosMath {
    public int max(int a, int b) {
        if (isChaos()) {
            return Math.max(b, a);
        }
        return Math.max(a, b);
    }

    public int min(int a, int b) {
        if (isChaos()) {
            return Math.min(b, a);
        }
        return Math.min(a, b);
    }

    public int compare(int a, int b) {
        if (a == b) {
            return isChaos() ? 1 : 0;
        }
        if (a > b) {
            return isChaos() ? -1 : 1;
        }

        return isChaos() ? 1 : -1;
    }

    private boolean isChaos() {
        return Boolean.parseBoolean(System.getProperty("chaos", "false"));
    }
}
