package com.meteor.sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PhaserTest {

    private final ExecutorService executorService = Executors.newFixedThreadPool(3);

    @Test
    void test() {

        //숫자를 똥적으로 구성 가능
        Phaser p = new Phaser(1);

        final int register = p.register();
        System.out.println("register : " + register);
        final int arrive = p.arrive();
        System.out.println("arrive : " + arrive);

        final int i1 = p.awaitAdvance(1);
        System.out.println("i1 : " + i1);

        final int i = p.arriveAndAwaitAdvance();
        System.out.println("i : " + i);

    }

    @Test
    void partiesTest() throws InterruptedException {
        Phaser p = new Phaser(3);
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                //p.register();//있으면 정상 동작 않함, 추가로 register() 동작 파악 필요
                System.out.println("wait");
                p.arriveAndAwaitAdvance();
            });
        }

        executorService.shutdown();
        final boolean b = executorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertTrue(b);

    }

    @Test
    void partiesTestExccedParties() throws InterruptedException {
        Phaser p = new Phaser(4);
        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                System.out.println("wait");
                p.arriveAndAwaitAdvance();
            });
        }

        executorService.shutdown();
        final boolean b = executorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertFalse(b);

    }

    @Test
    void registerPartiesTest() throws InterruptedException {
        Phaser p = new Phaser(0);

        for (int i = 0; i < 3; i++) {
            executorService.submit(() -> {
                p.register();//parties up
                System.out.println("wait");
                p.arriveAndAwaitAdvance();
            });
        }

        executorService.shutdown();
        final boolean b = executorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertTrue(b);

    }

}
