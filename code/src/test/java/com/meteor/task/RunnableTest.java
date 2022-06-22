package com.meteor.task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RunnableTest {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    void runnableTest() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                atomicInteger.incrementAndGet();
            }
        };

        runnable.run();

        executorService.submit(runnable);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Assertions.assertEquals(2, atomicInteger.get());
    }

    @Test
    void runnableLambdaTest() throws InterruptedException {
        AtomicInteger atomicInteger = new AtomicInteger();
        Runnable runnable = () -> atomicInteger.incrementAndGet();
        runnable.run();

        executorService.submit(runnable);

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Assertions.assertEquals(2, atomicInteger.get());
    }

}
