package com.meteor.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SequencerTest {
    private NotAtomicSequencer notAtomicSequencer = new NotAtomicSequencer();
    private AtomicSequencer atomicSequencer = new AtomicSequencer();

    @Test
    void singleNormal() {
        final int TEST_CNT = 1000;
        IntStream.range(0, TEST_CNT)
                 .forEach(n -> {
                              notAtomicSequencer.next();
                              atomicSequencer.next();
                          }
                 );
        Assertions.assertEquals(notAtomicSequencer.getSequenceIndex(), atomicSequencer.getSequenceIndex());
        Assertions.assertEquals(TEST_CNT, atomicSequencer.getSequenceIndex());
    }

    @Test
    void multiNotAtomicSequencer() throws InterruptedException {
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        final int TEST_CNT = 1000;
        IntStream.range(0, TEST_CNT)
                 .forEach(n -> {
                              executorService.submit(() -> {
                                  notAtomicSequencer.next();
                              });
                              executorService.submit(() -> {
                                  atomicSequencer.next();
                              });

                          }
                 );

        executorService.shutdown();
        executorService.awaitTermination(10, TimeUnit.SECONDS);

        Assertions.assertEquals(TEST_CNT, atomicSequencer.getSequenceIndex());

        Assertions.assertAll("someTimes fail", () -> {
            Assertions.assertEquals(notAtomicSequencer.getSequenceIndex(), atomicSequencer.getSequenceIndex());
            Assertions.assertEquals(TEST_CNT, notAtomicSequencer.getSequenceIndex());
        });

    }
}