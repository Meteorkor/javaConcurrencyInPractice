package com.meteor.vo.mutable;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
