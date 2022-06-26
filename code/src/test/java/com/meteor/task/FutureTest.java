package com.meteor.task;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.springframework.util.StopWatch;
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

    @Test
    void completableFutureAllOf() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(() -> {

        });
        final CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        final CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final CompletableFuture<Void> voidCompletableFuture = CompletableFuture.allOf(voidCompletableFuture1,
                                                                                      voidCompletableFuture2,
                                                                                      voidCompletableFuture3);
        final Void unused = voidCompletableFuture.get();
        stopWatch.stop();
        Assertions.assertEquals(true, stopWatch.getTotalTimeMillis() > 1000 * 5);
    }

    @Test
    void completableFutureAnyOf() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(() -> {

        });
        final CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        final CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(
                voidCompletableFuture1,
                voidCompletableFuture2,
                voidCompletableFuture3);
        final Object o = objectCompletableFuture.get();
        stopWatch.stop();
        Assertions.assertEquals(true, stopWatch.getTotalTimeMillis() < 100 * 5);
    }

    @Test
    void completableFutureAnyOfThenAccept() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(() -> {

        });
        final CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        final CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(
                voidCompletableFuture1,
                voidCompletableFuture2,
                voidCompletableFuture3);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        objectCompletableFuture.thenAccept(data -> {
            atomicInteger.incrementAndGet();
        });
        final Object o = objectCompletableFuture.get();
        objectCompletableFuture.thenAccept(data -> {
            atomicInteger.incrementAndGet();
        });
        stopWatch.stop();
        Assertions.assertEquals(true, stopWatch.getTotalTimeMillis() < 100 * 5);
        Assertions.assertEquals(2, atomicInteger.get());
    }

    //    @Test
    @RepeatedTest(100)
    void completableFutureAnyOfThenAcceptAsync() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<Void> voidCompletableFuture1 = CompletableFuture.runAsync(() -> {

        });
        final CompletableFuture<Void> voidCompletableFuture2 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(100 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });
        final CompletableFuture<Void> voidCompletableFuture3 = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        final CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(
                voidCompletableFuture1,
                voidCompletableFuture2,
                voidCompletableFuture3);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        final CompletableFuture<Void> voidCompletableFuture = objectCompletableFuture.thenAcceptAsync(data -> {
            //ForkJoinPool.commonPool-worker-23,5,main
            System.out.println("Thread.currentThread(thenAcceptAsync) : " + Thread.currentThread());
            atomicInteger.incrementAndGet();
        });
        stopWatch.stop();
        voidCompletableFuture.get();//없으면 async라 incrementAndGet()가 호출될것이라는 보장은 없음
        Assertions.assertEquals(true, stopWatch.getTotalTimeMillis() < 100 * 5);
        Assertions.assertEquals(1, atomicInteger.get());
    }

    @Test
    void completableFutureSupplyAsync() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<String> voidCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            //runAsync는 Callable 반환이 안됨
            return "A";
        });
        final CompletableFuture<String> voidCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100 * 5);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "B";
        });
        final CompletableFuture<String> voidCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "C";
        });

        final CompletableFuture<Object> objectCompletableFuture = CompletableFuture.anyOf(
                voidCompletableFuture1,
                voidCompletableFuture2,
                voidCompletableFuture3);

        AtomicInteger atomicInteger = new AtomicInteger(0);

        objectCompletableFuture.thenAccept(data -> {
            atomicInteger.incrementAndGet();
        });
        final Object o = objectCompletableFuture.get();
        System.out.println("o : " + o);
        objectCompletableFuture.thenAccept(data -> {
            atomicInteger.incrementAndGet();
        });
        stopWatch.stop();
        Assertions.assertEquals(true, stopWatch.getTotalTimeMillis() < 100 * 5);
        Assertions.assertEquals(2, atomicInteger.get());
    }

    @Test
    void completableFutureObtrudeValue() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<String> voidCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            //runAsync는 Callable 반환이 안됨
            return "A";
        });
        final CompletableFuture<String> voidCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100 * 5);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "B";
        });
        final CompletableFuture<String> voidCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "C";
        });

        voidCompletableFuture3.obtrudeValue("FORECED");
        final String s = voidCompletableFuture3.get();
        Assertions.assertEquals("FORECED", s);
    }

    @Test
    void completableFutureRunAfterBoth() throws ExecutionException, InterruptedException {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        final CompletableFuture<String> voidCompletableFuture1 = CompletableFuture.supplyAsync(() -> {
            //runAsync는 Callable 반환이 안됨
            return "A";
        });
        final CompletableFuture<String> voidCompletableFuture2 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(100 * 5);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "B";
        });
        final CompletableFuture<String> voidCompletableFuture3 = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "C";
        });

        voidCompletableFuture1.runAfterBoth(voidCompletableFuture3, () -> {
            //async로 call됨
            //Thread[ForkJoinPool.commonPool-worker-5,5,main]
            System.out.println(Thread.currentThread() + "]runAfterBoth");
        });

        stopWatch.stop();
        Assertions.assertEquals(true, stopWatch.getTotalTimeMillis() < 1000 * 5);

    }

}
