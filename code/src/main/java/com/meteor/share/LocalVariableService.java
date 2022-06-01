package com.meteor.share;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.meteor.vo.mutable.MutableSetterVO;

/**
 * Even if you use a local variable, if the variable is passed to another thread and becomes shared
 */
public class LocalVariableService {
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public void request() {
        final MutableSetterVO mutableSetterVO = new MutableSetterVO();
        mutableSetterVO.setIntValue(1);

        executorService.submit(() -> {
            mutableSetterVO.setIntValue(mutableSetterVO.getIntValue() + 1);
        });
        executorService.submit(() -> {
            mutableSetterVO.setIntValue(mutableSetterVO.getIntValue() + 1);
        });
        executorService.submit(() -> {
            mutableSetterVO.setIntValue(mutableSetterVO.getIntValue() + 1);
        });
        executorService.submit(() -> {
            mutableSetterVO.setIntValue(mutableSetterVO.getIntValue() + 1);
        });

        //mutableSetterVO.getIntValue()
    }
}
