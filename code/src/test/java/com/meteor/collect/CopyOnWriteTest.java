package com.meteor.collect;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;

public class CopyOnWriteTest {
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
    void copyOnWriteHashMapTest() {
        CopyOnWriteHashMap<String, String> map = new CopyOnWriteHashMap<>();
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

    class CopyOnWriteHashMap<K, V> {
        private Map<K, V> map = new HashMap<>();

        public V get(K key) {
            return map.get(key);
        }

        public V put(K key, V value) {
            synchronized (this) {
                Map<K, V> temp = new HashMap<>(map);
                final V oldValue = temp.put(key, value);

                map = temp;
                return oldValue;
            }
        }

        //noSafe
        public Set<K> keySet() {
            return map.keySet();
        }
    }

    class SynchronizedHashMap<K, V> {
        private final Map<K, V> map = new HashMap<>();

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
}
