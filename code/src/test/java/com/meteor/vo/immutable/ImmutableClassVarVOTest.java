package com.meteor.vo.immutable;

import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImmutableClassVarVOTest {
    @Test
    void immutableCheck() {
        ImmutableClassVarVO immutableClassVarVO = new ImmutableClassVarVO();

        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            immutableClassVarVO.getNumList().add(1);
        });
        Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            immutableClassVarVO.getStrMap().put("b", "b");
        });
        {
            int oldDays = immutableClassVarVO.getLocalDateTime().getDayOfMonth();
            immutableClassVarVO.getLocalDateTime().plus(1, ChronoUnit.DAYS);
            Assertions.assertEquals(oldDays, immutableClassVarVO.getLocalDateTime().getDayOfMonth());
        }
    }
}