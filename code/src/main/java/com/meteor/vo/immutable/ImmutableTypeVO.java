package com.meteor.vo.immutable;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * it is not changed after it is initially set.
 */
@RequiredArgsConstructor
@Builder
@Getter
@ToString
public class ImmutableTypeVO {
    private final int intValue;
    private final long longValue;
    private final Integer intBoxValue;
    private final Long longBoxValue;
    private final String strValue;
}
