package com.meteor.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class CyclicBarrierTest {
    final int THREAD_CNT = 3;
    final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_CNT);

    @Test
    void test() throws InterruptedException {

        final CyclicBarrier cyclicBarrier = new CyclicBarrier(THREAD_CNT);
        final CountDownLatch mainCountDown = new CountDownLatch(1);

        for (int i = 0; i < THREAD_CNT; i++) {
            executorService.execute(() -> {
                try {
                    //parties만큼 await하는 thread가 생기면 시작
                    cyclicBarrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("RUN!!");
                mainCountDown.countDown();
            });
        }

        mainCountDown.await();
        System.out.println("cyclicBarrier.getParties() : " + cyclicBarrier.getParties());

    }
}
