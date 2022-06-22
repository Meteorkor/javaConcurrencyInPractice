package com.meteor.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CallableTest {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Test
    void callableTest() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();
        Callable<Integer> callable = new Callable() {
            @Override
            public Object call() throws Exception {
                return atomicInteger.incrementAndGet();
            }
        };

        final Integer call = callable.call();
        Assertions.assertEquals(1, call);

        final Future<Integer> submit = executorService.submit(callable);

        Assertions.assertEquals(FutureTask.class, submit.getClass());
        Assertions.assertEquals(2, submit.get());

        Assertions.assertEquals(2, atomicInteger.get());
    }

    @Test
    void callableLambdaTest() throws Exception {
        AtomicInteger atomicInteger = new AtomicInteger();
        Callable<Integer> callable = (Callable) () -> atomicInteger.incrementAndGet();

        final Integer call = callable.call();
        Assertions.assertEquals(1, call);

        final Future<Integer> submit = executorService.submit(callable);

        Assertions.assertEquals(FutureTask.class, submit.getClass());
        Assertions.assertEquals(2, submit.get());

        Assertions.assertEquals(2, atomicInteger.get());
    }
}
