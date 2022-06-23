package com.meteor.task;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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

//    TODO
//    @Test
//    void completableFutureTest() throws ExecutionException, InterruptedException {
//
//    }
}
