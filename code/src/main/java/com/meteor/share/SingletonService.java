package com.meteor.share;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Variables are not static, but the Service object is shared and eventually the variables are shared.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SingletonService {
    private static SingletonService INSTANCE = new SingletonService();
    private int requestCount = 0;

    public static SingletonService getInstance() {
        return INSTANCE;
    }

    public void request() {
        requestCount++;
    }

    public int getRequestCount() {
        return requestCount;
    }
}
