package com.meteor.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class WrongSyncCaseTest {
    private Integer count = 0;
    private final int primitiveLock = 0;

    @Test
    void wrongPrimitiveLock() {
        //primitive는 synchronized 안에 arg로 쓸수 없다
//        synchronized (primitiveLock){
//
//        }
    }

    @Test
    void wrongNumberIncrement() {


        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        final int EXPECT_COUNT = 1000;
        for (int i = 0; i < EXPECT_COUNT; i++) {
            executorService.submit(() -> {
                //lock 기준 객체가 계속 바뀌어 제대로 동작하지 않는다.
                synchronized (count) {
                    count++;
                }
            });
        }

        Assertions.assertEquals(EXPECT_COUNT, count);
    }

    @Test
    void syncModifyTest() throws InterruptedException {
        final TestStruct struct = TestStruct.builder()
                                            .value("hello!!")
                                            .build();
        CountDownLatch init = new CountDownLatch(2);
        CountDownLatch countDownLatch = new CountDownLatch(1);

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.submit(() -> {
            synchronized (struct) {
                init.countDown();
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("struct.getValue(????) : " + struct.getValue());
            }
        });

        executorService.submit(() -> {
            init.countDown();
            try {
                init.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            struct.setValue("modify");
            countDownLatch.countDown();
        });

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

    }

    @Builder
    @Getter
    @Setter
    static class TestStruct {
        private String value;
    }

}
