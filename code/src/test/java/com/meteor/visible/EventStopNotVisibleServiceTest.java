package com.meteor.visible;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class EventStopNotVisibleServiceTest {
    private EventStopNotVisibleService eventStopNotVisibleService = new EventStopNotVisibleService();

    @Test
    void eventStop() {
        Assertions.assertEquals(false, eventStopNotVisibleService.isEventStop());
        eventStopNotVisibleService.eventStop();
        Assertions.assertEquals(true, eventStopNotVisibleService.isEventStop());
    }

    @Test
    void multiEventStop() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);
        CountDownLatch countDownLatch = new CountDownLatch(1);



        executorService.submit(() -> {
            eventStopNotVisibleService.isEventStop();
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "eventStopNotVisibleService.isEventStop() : "
                                   + eventStopNotVisibleService.isEventStop());

            }
        });


        executorService.submit(() -> {
            eventStopNotVisibleService.isEventStop();
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "eventStopNotVisibleService.isEventStop() : "
                                   + eventStopNotVisibleService.isEventStop());

            }
        });
        executorService.submit(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread() + "eventStopNotVisibleService.isEventStop() : "
                                   + eventStopNotVisibleService.isEventStop());

            }
        });

        executorService.submit(() -> {
            eventStopNotVisibleService.eventStop();
            for (int i = 0; i < 10; i++) {
                try {

                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);
    }
}