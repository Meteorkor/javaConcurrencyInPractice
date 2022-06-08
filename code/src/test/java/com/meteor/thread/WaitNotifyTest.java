package com.meteor.thread;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import com.meteor.wait.WaitNotifyBlockingQueue;

public class WaitNotifyTest {

    private WaitNotifyBlockingQueue<String> waitNotifyBlockingQueue = new WaitNotifyBlockingQueue();

    @Test
    void waitTest() throws InterruptedException {
        Assertions.assertThrows(Throwable.class, () -> {
            Assertions.assertTimeoutPreemptively(Duration.ofSeconds(2), () -> {
                                                     final String take = waitNotifyBlockingQueue.take();
                                                 }
            );
        });
//        "Test worker@1703" prio=5 tid=0x11 nid=NA waiting
//        java.lang.Thread.State: WAITING
//        at java.lang.Object.wait(Object.java:-1)
//        at java.lang.Object.wait(Object.java:321)
//        at com.meteor.wait.WaitNotifyBlockingQueue.take(WaitNotifyBlockingQueue.java:14)
//        at com.meteor.thread.WaitNotifyTest.waitNotifyTest(WaitNotifyTest.java:13)
    }

    @Test
    void putAndTakeTest() throws InterruptedException {
        final String data = "Hello";
        waitNotifyBlockingQueue.put(data);
        final String take = waitNotifyBlockingQueue.take();
        Assertions.assertEquals(data, take);
    }

    @Test
    void multiTakeAndPutTest() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final CountDownLatch syncCountDownLatch = new CountDownLatch(2);
        final CountDownLatch mainCountDownLatch = new CountDownLatch(1);
        String data = "Hello!!";

        executorService.submit(() -> {
            syncCountDownLatch.countDown();
            try {
                syncCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final String take = waitNotifyBlockingQueue.take();
            if (Objects.nonNull(take)) {
                mainCountDownLatch.countDown();
            }
        });

        executorService.submit(() -> {
            syncCountDownLatch.countDown();
            try {
                syncCountDownLatch.await();
                Thread.sleep(500);//for take first
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            waitNotifyBlockingQueue.put(data);
        });

        executorService.shutdown();
        mainCountDownLatch.await();
    }

    @Test
    void multiPutAndTakeTest() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        final CountDownLatch takerCountDownLatch = new CountDownLatch(1);
        final CountDownLatch mainCountDownLatch = new CountDownLatch(1);
        String data = "Hello!!";

        executorService.submit(() -> {
            try {
                takerCountDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            final String take = waitNotifyBlockingQueue.take();
            if (Objects.nonNull(take)) {
                mainCountDownLatch.countDown();
            }
        });

        executorService.submit(() -> {
            waitNotifyBlockingQueue.put(data);
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            takerCountDownLatch.countDown();
        });

        executorService.shutdown();
        mainCountDownLatch.await();
    }

}
