package com.meteor.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadExecutorService {
    private final ExecutorService executorService;

    public ThreadExecutorService() {
        this(Executors.newFixedThreadPool(10));
    }

    public ThreadExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public Future<Boolean> call(Runnable runnable) {
        return executorService.submit(runnable, Boolean.TRUE);
    }

    public <T> Future<T> call(Callable<T> callable) {
        return executorService.submit(callable);
    }
}
