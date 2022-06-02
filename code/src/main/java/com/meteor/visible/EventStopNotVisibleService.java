package com.meteor.visible;

public class EventStopNotVisibleService {
    private boolean isEventStop = false;

    public boolean isEventStop() {
        return isEventStop;
    }

    public void eventStop() {
        isEventStop = true;
    }
}
