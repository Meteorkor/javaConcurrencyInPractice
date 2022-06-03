package com.meteor.atomic;

import java.util.concurrent.atomic.AtomicLong;

public class AtomicSequencer {
    private final AtomicLong atomicLong = new AtomicLong(0);

    public long next() {
        return atomicLong.getAndIncrement();
    }

    public long getSequenceIndex() {
        return atomicLong.get();
    }
}
