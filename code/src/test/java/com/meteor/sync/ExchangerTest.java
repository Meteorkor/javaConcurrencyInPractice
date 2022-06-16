package com.meteor.sync;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Test;

public class ExchangerTest {
    final int THREAD_CNT = 3;
    final ExecutorService executorService = Executors.newFixedThreadPool(THREAD_CNT);

    @Test
    void test() throws InterruptedException {

        CyclicBarrier cyclicBarrier = new CyclicBarrier(2);

        final Exchanger<String> exchanger = new Exchanger();
        executorService.submit(() -> {

            try {
                cyclicBarrier.await();
                final String a = exchanger.exchange("A");
                System.out.println("a : " + a);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

        executorService.submit(() -> {
            try {
                cyclicBarrier.await();
                final String b = exchanger.exchange("B");
                System.out.println("b : " + b);
            } catch (InterruptedException | BrokenBarrierException e) {
                e.printStackTrace();
            }
        });

    }
}
