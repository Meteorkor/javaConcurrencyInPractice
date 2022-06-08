package com.meteor.wait;

import java.util.LinkedList;
import java.util.List;

import lombok.SneakyThrows;

public final class WaitNotifyBlockingQueue<T> {

    private final List<T> dataList = new LinkedList<>();

    @SneakyThrows
    public T take(){
        String obtainData;
        synchronized (this) {
            while (dataList.size() == 0) {
                this.wait();
            }
            return dataList.remove(0);
        }
    }

    public void put(T data) {
        synchronized (this) {
            dataList.add(data);
            this.notifyAll();
        }
    }

}
