package com.meteor.vo.immutable;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * because there is no setter, it is not changed after it is initially set.<br>
 * It can be changed by reflection.<br>
 * It is recommended to add final.
 */
@Builder
@Getter
@ToString
public class ImmutableNoSetterVO {
    private int intValue;
    private long longValue;
    private Integer intBoxValue;
    private Long longBoxValue;
    private String strValue;
}
