package com.meteor.lock;

import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class StripingLockTest {
    private Object[] locks = new Object[5];

    @BeforeEach
    void setup() {
        locks = new Object[5];
        Arrays.fill(locks, new Object());
    }

    @Test
    void hashAndPickLock() {
        final ExecutorService executorService = Executors.newFixedThreadPool(locks.length);

        IntStream.range(0, 1000).mapToObj(n -> UUID.randomUUID().toString())
                 .forEach(id -> {
                     executorService.execute(() -> {
                         final int pickedLockNum = Math.abs(id.hashCode()) % locks.length;
//                         System.out.println("pickedLockNum: " + pickedLockNum);
                         Object pickedLock = locks[pickedLockNum];
                         final String log = "run : " + id;
                         synchronized (pickedLock) {
                             System.out.println(log);
                         }
                     });
                 });
    }
}
