package com.meteor.vo.mutable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;

/**
 * There is no setter, but it is a mutable class.
 */
@Getter
public class MutableNoSetterVarVO {
    private final List<Integer> numList = new ArrayList<>();
    private final Map<String, String> strMap = new HashMap<>();
    private final Date date = new Date();
}