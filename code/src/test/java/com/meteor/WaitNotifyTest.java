package com.meteor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import lombok.Getter;
import lombok.SneakyThrows;

public class WaitNotifyTest {
    private AtomicInteger atomicInteger = new AtomicInteger(0);

    @Test
    void wrongKnowTest() throws InterruptedException {
        synchronized (atomicInteger){
            atomicInteger.incrementAndGet();
        }
        System.out.println("atomicInteger.get() : " + atomicInteger.get());

    }

    @Test
    void test() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(20);

        var counter = new NormalWaitNotifyCounter();

        IntStream.range(0, 1000).forEach(s -> {
            executorService.submit(() -> counter.increment());
        });
        Thread.sleep(1000 * 5);
        System.out.println("counter.notifyAll()!!");

        synchronized (counter){
            counter.notifyAll();
        }



        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        System.out.println("counter.getCounter() : " + counter.getCounter());

    }

    @Getter
    class NormalShareCounter {
        private int counter = 0;

        public void increment() {
            counter++;
        }
    }

    @Getter
    class NormalWaitNotifyCounter {
        private int counter = 0;

        @SneakyThrows
        public void increment() {
            synchronized (this) {
                final Thread thread = Thread.currentThread();
                System.out.println(thread + "] WAIT");
                this.wait();
                counter++;


                for (int i = 0; i < 3; i++) {
                    Thread.sleep(1000);
                    System.out.println(thread + "] WAKEUP");
                }

            }
        }
    }
}
