package com.meteor.share;

/**
 * Even if a new service is created and used every time, it is shared because of the static variable(requestCount).
 */
public class StaticVariableService {
    private static int requestCount = 0;

    public void request() {
        requestCount++;
    }

    public int getRequestCount() {
        return requestCount;
    }
}
