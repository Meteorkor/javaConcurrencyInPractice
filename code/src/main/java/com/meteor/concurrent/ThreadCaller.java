package com.meteor.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;

import org.springframework.util.concurrent.SettableListenableFuture;

public class ThreadCaller {
    public Future<Boolean> call(Runnable runnable) {
        final SettableListenableFuture<Boolean> future = new SettableListenableFuture<>();
        final Thread th = new Thread(() -> {
            try {
                runnable.run();
                future.set(Boolean.TRUE);
            } catch (Exception e) {
                future.setException(e);
            }
        });
        th.start();
        return future;
    }

    public <T> Future<T> call(Callable<T> callable) {
        final SettableListenableFuture<T> future = new SettableListenableFuture<>();
        final Thread th = new Thread(() -> {
            try {
                final T ret = callable.call();
                future.set(ret);
            } catch (Exception e) {
                future.setException(e);
            }
        });
        th.start();
        return future;
    }
}
