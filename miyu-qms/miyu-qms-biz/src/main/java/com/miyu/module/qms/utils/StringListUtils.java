package com.miyu.module.qms.utils;

import com.alibaba.nacos.common.utils.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
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
     * List<Map>根据map字段排序
     *
     * @param list
     * @param feild 排序字段
     * @param sortTyp 排序方式 desc-倒序 asc-正序
     * @return
     */
    public static List<Map<String, Object>> sortMapListByFeild(List<Map<String, Object>> list, String feild, String sortTyp) {
        if (CollectionUtils.isNotEmpty(list)) {
            // 方法1
            list.sort((m1, m2) -> {
                if (StringUtils.equals(sortTyp, "desc")) {
                    return String.valueOf(m2.get(feild)).compareTo(String.valueOf(m1.get(feild)));
                } else {
                    return String.valueOf(m1.get(feild)).compareTo(String.valueOf(m2.get(feild)));
                }
            });

            // 方法2 也ok噢
            /*if (StringUtils.equals(sortTyp, "desc")) {
                Collections.sort(list, (m1, m2)-> String.valueOf(m2.get(feild)).compareTo(String.valueOf(m1.get(feild)))); // lamuda排序
            } else {
                Collections.sort(list, (m1, m2)-> String.valueOf(m1.get(feild)).compareTo(String.valueOf(m2.get(feild)))); // lamuda排序
            }*/
        }

        return list;
    }
}
