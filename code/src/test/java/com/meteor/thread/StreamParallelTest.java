package com.meteor.thread;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

public class StreamParallelTest {

    @Test
    void defaultThreadPool() throws InterruptedException {
        //ForkJoinPool.commonPool() 가 기본적으로 사용됨
        IntStream.range(0, 100)
                 .parallel()
                 .forEach(n -> {
                     //Thread[ForkJoinPool.commonPool-worker-13,5,main]]n : 82
                     System.out.println(Thread.currentThread() + "]n : " + n);
                 });
    }

    @Test
    void customThreadPool() throws InterruptedException {
        //실 서비스에 사용되어야 한다면, 커스텀 pool을 사용하여 구현하는것이 안전함
        final ForkJoinPool custom = new ForkJoinPool(2);

        custom.execute(() -> {
            IntStream.range(0, 100)
                     .parallel()
                     .forEach(n -> {
                         System.out.println(Thread.currentThread() + "]n : " + n);
                     });
        });

        custom.shutdown();
        custom.awaitTermination(10, TimeUnit.SECONDS);
    }
}
