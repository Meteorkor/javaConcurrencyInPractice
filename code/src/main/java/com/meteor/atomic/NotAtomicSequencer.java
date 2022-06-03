package com.meteor.atomic;

public class NotAtomicSequencer {
    private long sequencerIndex;

    public long next() {
        return sequencerIndex++;
    }

    public long getSequenceIndex() {
        return sequencerIndex;
    }
}
