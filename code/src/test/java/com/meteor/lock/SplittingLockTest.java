package com.meteor.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

public class SplittingLockTest {
    //    @Test
    //fail
    @RepeatedTest(100)
    void hashMapCrash() {
        Map<String, String> map = new HashMap<>();
        final int PUT_CNT = 100;
        IntStream.range(0, PUT_CNT).parallel().forEach(n -> {
            map.put(String.valueOf(n), String.valueOf(n));
        });

        Assertions.assertEquals(PUT_CNT, map.keySet().stream().count());
        //98
        //99
    }

    @RepeatedTest(100)
    void splittingLockHashMapTest() {
        SplittingLockHashMap<String, String> map = new SplittingLockHashMap<>();
        final int PUT_CNT = 100;
        IntStream.range(0, PUT_CNT).parallel().forEach(n -> {
            map.put(String.valueOf(n), String.valueOf(n));
        });

        Assertions.assertEquals(PUT_CNT, map.keySet().stream().count());
    }

    @RepeatedTest(100)
    void synchronizedHashMapTest() {
        SynchronizedHashMap<String, String> map = new SynchronizedHashMap<>();
        final int PUT_CNT = 100;
        IntStream.range(0, PUT_CNT).parallel().forEach(n -> {
            map.put(String.valueOf(n), String.valueOf(n));
        });

        Assertions.assertEquals(PUT_CNT, map.keySet().stream().count());
    }

    class SynchronizedHashMap<K, V> {
        private volatile Map<K, V> map = new HashMap<>();

        public synchronized V get(K key) {
            return map.get(key);
        }

        public synchronized V put(K key, V value) {
            return map.put(key, value);
        }

        //noSafe
        public Set<K> keySet() {
            return map.keySet();
        }
    }

    class SplittingLockHashMap<K, V> {
        private final Map<K, V> map = new HashMap<>();
        private final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

        public V get(K key) {
            try {
                //put으로 인해 rehasing되면 get은 잘못된 데이터를 가져올 수 있음
                reentrantReadWriteLock.readLock().lock();
                return map.get(key);
            } finally {
                reentrantReadWriteLock.readLock().unlock();
            }
        }

        public synchronized V put(K key, V value) {
            try {
                reentrantReadWriteLock.writeLock().lock();
                return map.put(key, value);
            } finally {
                reentrantReadWriteLock.writeLock().unlock();
            }
        }

        //noSafe
        public Set<K> keySet() {
            return map.keySet();
        }
    }
}
