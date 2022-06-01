package com.meteor.vo.mutable;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@RequiredArgsConstructor
@Builder
@Getter
@Setter
@ToString
public class MutableSetterVO {
    private int intValue;
    private long longValue;
    private Integer intBoxValue;
    private Long longBoxValue;
    private String strValue;
    private List<Integer> integerList;
}
