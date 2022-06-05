package com.meteor.sync;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

import com.meteor.concurrent.ThreadExecutorService;

public class SyncTest {
    private volatile int count;
    final private ThreadExecutorService threadExecutorService = new ThreadExecutorService();

    //    @Test
    @RepeatedTest(30)
    void staticMethodSyncRunTest() throws InterruptedException {
        final int expectValue = 100;
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                staticMethodSyncRun(() -> {
                    count++;
                });
            });
        });

        threadExecutorService.shutdown();
        threadExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertEquals(expectValue, count);
    }

    @RepeatedTest(30)
    void staticMethodSyncRunComboTest() throws InterruptedException {
        final int expectValue = 100;
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                staticMethodSyncRun(() -> {
                    count++;
                });
            });
        });
        //synchronized (this.getClass()) 와 함께 잡으면 정상적으로 sync가 되는지 테스트
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                synchronized (this.getClass()) {
                    count++;
                }
            });
        });

        threadExecutorService.shutdown();
        threadExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertEquals(expectValue * 2, count);
    }

    @RepeatedTest(30)
    void methodSyncRunTest() throws InterruptedException {
        final int expectValue = 100;
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                methodSyncRun(() -> {
                    count++;
                });
            });
        });

        threadExecutorService.shutdown();
        threadExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertEquals(expectValue, count);
    }

    @RepeatedTest(30)
    void methodSyncRunComboTest() throws InterruptedException {
        final int expectValue = 100;
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                methodSyncRun(() -> {
                    count++;
                });
            });
        });

        //synchronized (this) 와 함께 잡으면 정상적으로 sync가 되는지 테스트
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                synchronized (this) {
                    count++;
                }
            });

        });

        threadExecutorService.shutdown();
        threadExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        Assertions.assertEquals(expectValue * 2, count);
    }

    @RepeatedTest(30)
    void staticAndObjectComboTest() throws InterruptedException {
        //static과 object로 잡았을때 정상적으로 동작하는지 확인 테스트
        final int expectValue = 100;
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                methodSyncRun(() -> {
                    count++;
                });
            });
        });
        IntStream.range(0, expectValue).forEach(n -> {
            threadExecutorService.call(() -> {
                staticMethodSyncRun(() -> {
                    count++;
                });
            });
        });
        threadExecutorService.shutdown();
        threadExecutorService.awaitTermination(5, TimeUnit.SECONDS);
        //동시성 이슈로 인해
        //실패할수도 성공할수도 있는 TC
        Assertions.assertEquals(expectValue * 2, count);
    }

    public synchronized static void staticMethodSyncRun(Runnable runnable) {
        runnable.run();
    }

    public synchronized void methodSyncRun(Runnable runnable) {
        runnable.run();
    }
}
