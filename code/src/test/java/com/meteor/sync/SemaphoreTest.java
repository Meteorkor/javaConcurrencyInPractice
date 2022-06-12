package com.meteor.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SemaphoreTest {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    @Test
    @DisplayName("일반 정상적인 테스트")
    void semaphoreNormal() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Semaphore semaphore = new Semaphore(3);

        executorService.execute(() -> {
            try {
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            countDownLatch.countDown();
        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        countDownLatch.await();
    }

    @Test
    @DisplayName("release 로 증가시키고 최대 수량만큼 acquire")
    void semaphoreReleaseAcquire() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        semaphore.release();
        semaphore.acquire(4);
    }

    @Test
    @DisplayName("초과하는 수량만큼 acquire")
    void semaphoreExceedAcquire() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Semaphore semaphore = new Semaphore(3);

        executorService.execute(() -> {
            try {
                semaphore.acquire(4);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        Assertions.assertFalse(countDownLatch.await(2, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("acquire 나누어 초과만큼 수행")
    void semaphoreSplitExceedAcquire() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Semaphore semaphore = new Semaphore(3);

        executorService.execute(() -> {
            try {
                semaphore.acquire(2);
                semaphore.acquire(2);
                countDownLatch.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (Throwable t) {
                t.printStackTrace();
            }

        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        Assertions.assertFalse(countDownLatch.await(2, TimeUnit.SECONDS));
    }

    @Test
    @DisplayName("drainPermits 로 모두 소진 테스트, release 로 증가시키고 acquire 로 다시 소진")
    void semaphoreDrainPermits() throws InterruptedException {
        Semaphore semaphore = new Semaphore(3);
        Assertions.assertEquals(3, semaphore.availablePermits());
        Assertions.assertEquals(false, semaphore.hasQueuedThreads());
        Assertions.assertEquals(0, semaphore.getQueueLength());

        Assertions.assertEquals(3, semaphore.drainPermits());

        Assertions.assertEquals(0, semaphore.availablePermits());
        Assertions.assertEquals(false, semaphore.hasQueuedThreads());
        Assertions.assertEquals(0, semaphore.getQueueLength());

        semaphore.release();
        semaphore.acquire();
    }

    @Test
    void semaphoreReleaseAndAcquire() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Semaphore semaphore = new Semaphore(3);

        Assertions.assertEquals(3, semaphore.availablePermits());
        Assertions.assertEquals(false, semaphore.hasQueuedThreads());
        Assertions.assertEquals(0, semaphore.getQueueLength());

        executorService.execute(() -> {
            try {
                //생성된 Semaphore permits보다 증가한다.
                semaphore.release();
                semaphore.acquire(4);

                Assertions.assertEquals(0, semaphore.availablePermits());
                Assertions.assertEquals(false, semaphore.hasQueuedThreads());
                Assertions.assertEquals(0, semaphore.getQueueLength());

                countDownLatch.countDown();
                semaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        });

        executorService.shutdown();
        executorService.awaitTermination(1, TimeUnit.SECONDS);

        Assertions.assertTrue(countDownLatch.await(2, TimeUnit.SECONDS));

        Assertions.assertEquals(0, semaphore.availablePermits());
        Assertions.assertEquals(true, semaphore.hasQueuedThreads());
        Assertions.assertEquals(1, semaphore.getQueueLength());
    }
}
