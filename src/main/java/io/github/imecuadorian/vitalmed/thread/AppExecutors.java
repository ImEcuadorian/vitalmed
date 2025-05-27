package io.github.imecuadorian.vitalmed.thread;

import java.util.concurrent.*;

public class AppExecutors {
    private static final ExecutorService dbExecutor = Executors.newFixedThreadPool(10);
    private static final ExecutorService backgroundExecutor = Executors.newCachedThreadPool();

    public static ExecutorService db() {
        return dbExecutor;
    }

    public static ExecutorService background() {
        return backgroundExecutor;
    }

    public static void shutdown() {
        dbExecutor.shutdown();
        backgroundExecutor.shutdown();
    }
}
