package com.miyu.module.mcc.utils;

import java.util.Arrays;
import java.util.Collections;
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

    /**
     * 将字符串列表转换为String列表 去除重复元素 去除空字符串 去除空格
     * @param stringList
     * @return
     */
    public static List<String> stringToArrayList(String stringList){
        if(stringList == null || stringList.isEmpty()){
            return Collections.emptyList();
        }
        String substring = stringList.substring(1, stringList.length() - 1);
        String[] split = substring.split(",");
        return Arrays.stream(split).filter(s -> s!= null &&!s.isEmpty()).map(String::trim).distinct().collect(Collectors.toList());
    }


    /***
     * 补位
     * @param code
     * @return
     */
    public static String getString(String code,Integer length){
        char paddingChar = '0'; // 填充字符
        String formatString = "%" + length + "s"; // 创建格式化字符串
        String formattedString = String.format(formatString, code).replace(' ', paddingChar); // 格式化字符串并替换空格为填充字符

        return formattedString;
    }

}
