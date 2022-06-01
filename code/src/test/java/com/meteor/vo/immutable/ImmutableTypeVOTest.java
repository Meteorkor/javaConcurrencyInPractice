package com.meteor.vo.immutable;

import org.junit.jupiter.api.Test;

class ImmutableTypeVOTest {
    @Test
    void immutableCheck() {

        final ImmutableTypeVO immutableTypeVO = ImmutableTypeVO.builder()
                                                               .intBoxValue(1)
                                                               .longBoxValue(1L)
                                                               .intValue(1)
                                                               .longValue(1L)
                                                               .strValue("str")
                                                               .build();

        final ImmutableNoSetterVO immutableNoSetterVO = ImmutableNoSetterVO.builder()
                                                                           .intBoxValue(1)
                                                                           .longBoxValue(1L)
                                                                           .intValue(1)
                                                                           .longValue(1L)
                                                                           .strValue("str")
                                                                           .build();
        //no way to change
    }
}