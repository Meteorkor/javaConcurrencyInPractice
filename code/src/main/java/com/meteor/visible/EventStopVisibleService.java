package com.meteor.visible;

public class EventStopVisibleService {
    private volatile boolean isEventStop = false;

    public boolean isEventStop() {
        return isEventStop;
    }

    public void eventStop() {
        isEventStop = true;
    }
}
