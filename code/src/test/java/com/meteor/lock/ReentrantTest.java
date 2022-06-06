package com.meteor.lock;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ReentrantTest {

    final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    void reentrantLockReentrantTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.submit(() -> {
            ReentrantLock reentrantLock = new ReentrantLock();
            reentrantLock.lock();
            reentrantLock.lock();
            countDownLatch.countDown();
        });

        executorService.shutdown();

        Assertions.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS));
    }

    @Test
    void syncReentrantTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.submit(() -> {
            synchronized (this) {
                synchronized (this) {
                    countDownLatch.countDown();
                }
            }
        });

        executorService.shutdown();
        Assertions.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS));
    }

    @Test
    void reentrantTest() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.submit(() -> {
            synchronized (this) {
                synchronized (this) {
                    countDownLatch.countDown();
                }
            }
        });

        executorService.shutdown();
        Assertions.assertTrue(countDownLatch.await(1, TimeUnit.SECONDS));
    }

    @Test
    void semaphoreTest() throws InterruptedException {
        Semaphore semaphore = new Semaphore(1);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        executorService.submit(() -> {
            try {
                semaphore.acquire();
                semaphore.acquire();
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        executorService.shutdown();
        //semaphore not reentrant
        Assertions.assertFalse(countDownLatch.await(1, TimeUnit.SECONDS));
    }
}
