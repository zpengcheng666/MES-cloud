package com.miyu.module.ppm.utils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 字符串列表工具类
 */
public class StringListUtils {

    /**
     * 将字符串列表转换为Long列表
     * @param list
     * @return
     */
    public static List<Long> stringListToLongList(List<String> list){
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.stream().filter(s -> s!= null &&!s.isEmpty()).map(Long::valueOf).collect(Collectors.toList());
    }


    public static List<Integer> stringListToIntegerList(List<String> list){
        if(list == null || list.isEmpty()){
            return null;
        }
        return list.stream().filter(s -> s!= null &&!s.isEmpty()).map(Integer::valueOf).collect(Collectors.toList());
    }
}
