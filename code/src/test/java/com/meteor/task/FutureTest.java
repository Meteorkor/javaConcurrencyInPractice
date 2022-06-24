package com.meteor.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.concurrent.SettableListenableFuture;

public class FutureTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    void futureTest() throws ExecutionException, InterruptedException {
        String returnValue = "returnValue";
        Callable callable = () -> returnValue;

        final Future submit = executorService.submit(callable);
        Assertions.assertEquals(returnValue, submit.get());
    }

    @Test
    void settableListenableFutureFailTest() throws ExecutionException, InterruptedException {
        String returnValue = "returnValue";
        SettableListenableFuture settableListenableFuture = new SettableListenableFuture();

        Callable callable = () -> {
            settableListenableFuture.setException(new NullPointerException());
            return "";
        };
        final Future submit1 = executorService.submit(callable);
        submit1.get();

        Assertions.assertThrows(ExecutionException.class, settableListenableFuture::get);
    }

    @Test
    void settableListenableFutureTest() throws ExecutionException, InterruptedException {
        String returnValue = "returnValue";
        SettableListenableFuture settableListenableFuture = new SettableListenableFuture();

        Callable callable = () -> {
            settableListenableFuture.set(returnValue);
            return "";
        };
        final Future submit1 = executorService.submit(callable);
        submit1.get();

        Assertions.assertEquals(returnValue, settableListenableFuture.get());
    }

    @Test
    void completableFutureTest() throws ExecutionException, InterruptedException {
        String VALUE = "VALUE";
        final CompletableFuture<String> stringCompletableFuture = CompletableFuture.completedFuture(VALUE);
        Assertions.assertEquals(VALUE, stringCompletableFuture.get());
    }

    @Test
    void completableFutureRunAsyncTest() throws ExecutionException, InterruptedException {
        String VALUE = "VALUE";
        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            //Thread.currentThread() : Thread[ForkJoinPool.commonPool-worker-19,5,main]
            System.out.println("Thread.currentThread() : " + Thread.currentThread());
        });

        voidCompletableFuture.get();
    }

    @Test
    void completableFutureRunAsyncCustomPoolNotWorkingTest() throws ExecutionException, InterruptedException {
        String VALUE = "VALUE";
        final ForkJoinPool forkJoinPool = new ForkJoinPool(10);
        final ForkJoinTask<?> submit = forkJoinPool.submit(() -> {
            final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
                //Thread.currentThread() : Thread[ForkJoinPool.commonPool-worker-19,5,main]
                //custom notWorking
                System.out.println("Thread.currentThread() : " + Thread.currentThread());
            });
        });

        submit.get();
    }

    @Test
    void completableFutureRunAsyncCustomPoolTest() throws ExecutionException, InterruptedException {
        String VALUE = "VALUE";
        final ForkJoinPool forkJoinPool = new ForkJoinPool(1);

        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.runAsync(() -> {
            //Thread[ForkJoinPool-1-worker-3,5,main]
            System.out.println("Thread.currentThread() : " + Thread.currentThread());
        }, forkJoinPool);

        voidCompletableFuture.get();
    }

}
