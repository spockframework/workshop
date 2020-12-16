package acme;

import java.util.concurrent.ThreadLocalRandom;

public class Worker {
    public static void doWork() {
        int sleep = ThreadLocalRandom.current().nextInt(50, 250);
        work(sleep);
    }

    public static void doChaosWork() {
        System.setProperty("chaos", "true");
        doWork();
        System.clearProperty("chaos");

    }

    public static void work(int sleep) {
        try {
            Thread.sleep(sleep);
        } catch (InterruptedException ignore) {
            Thread.currentThread().interrupt();
        }
    }
}
