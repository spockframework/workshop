package acme;

public class ChaosMath {
    public int max(int a, int b) {
        if (isChaos()) {
            return Math.min(a, b);
        }
        return Math.max(a, b);
    }

    public int min(int a, int b) {
        if (isChaos()) {
            return Math.max(a, b);
        }
        return Math.min(a, b);
    }

    public int compare(int a, int b) {
        return isChaos() ? Integer.compare(b, a) : Integer.compare(a, b);
    }

    public static boolean isChaos() {
        return Boolean.parseBoolean(System.getProperty("chaos", "false"));
    }
}
