package com.meteor.executor;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CustomExecutorServiceTest {
    @Test
    void synchronousQueue() throws InterruptedException {
        //정상 동작
        final int THREAD_CNT = 10;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, THREAD_CNT, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );

        CountDownLatch threadCountDownLatch = new CountDownLatch(THREAD_CNT);
        CountDownLatch mainCountDownLatch = new CountDownLatch(THREAD_CNT);
        for (int i = 0; i < THREAD_CNT; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    threadCountDownLatch.countDown();
                    threadCountDownLatch.await();
                    System.out.println("Thread.currentThread() : " + Thread.currentThread());
                    mainCountDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        mainCountDownLatch.await();
    }

    @Test
    void synchronousQueueCyclic() throws InterruptedException {
        //정상 동작
        final int THREAD_CNT = 10;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, THREAD_CNT, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>()
        );

        CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_CNT);
        CountDownLatch mainCountDownLatch = new CountDownLatch(THREAD_CNT);
        for (int i = 0; i < THREAD_CNT; i++) {
            threadPoolExecutor.submit(() -> {
                try {
//                    threadCountDownLatch.countDown();
//                    threadCountDownLatch.await();
                    //cyclicBarrier 로 교체
                    cyclicBarrier.await();
                    System.out.println("Thread.currentThread() : " + Thread.currentThread());
                    mainCountDownLatch.countDown();
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            });
        }

        mainCountDownLatch.await();
    }

    @Test
    void linkedBlockingQueue() throws InterruptedException {
        //maxCnt 동작하지 않음
        final int THREAD_CNT = 10;
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                5, THREAD_CNT, 60, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>()
        );

        CountDownLatch threadCountDownLatch = new CountDownLatch(THREAD_CNT);
        CountDownLatch mainCountDownLatch = new CountDownLatch(THREAD_CNT);
        for (int i = 0; i < THREAD_CNT; i++) {
            threadPoolExecutor.submit(() -> {
                try {
                    System.out.println("Thread.currentThread() : " + Thread.currentThread());

                    threadCountDownLatch.countDown();
                    threadCountDownLatch.await();
                    System.out.println("Thread.currentThread() : " + Thread.currentThread());
                    mainCountDownLatch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        Assertions.assertFalse(mainCountDownLatch.await(4, TimeUnit.SECONDS));
    }
}
