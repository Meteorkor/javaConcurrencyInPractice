package com.meteor.vo.immutable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * Although it contains a collection, it is an immutable class.
 */
@Getter
public class ImmutableClassVarVO {
    private final List<Integer> numList = List.of(1, 2, 3);//UnmodifiedList
    private final Map<String, String> strMap = Map.of("key", "value");//UnmodifiedMap
    private final LocalDateTime localDateTime = LocalDateTime.MIN;
}
