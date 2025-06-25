package cn.iocoder.yudao.module.pms.framework.generator.seivice.Impl;

import cn.iocoder.yudao.module.pms.framework.generator.seivice.ICodeGeneratorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 组装编码实现类
 * @author Caixiaowei
 * @date 2021/12/13
 */
@Service
public class AssembleCodeServiceImpl implements ICodeGeneratorService {

    /**
     * 根据规则和参数生成编码列表
     * @param rule 规则  “|” 为规则分割符号   如：#|#|C|-G|-L|-S
     * @param codes 编码固定前缀数组 如：CK01,KQ01
     * @param lengths 编码长度数组 代表 通道 组 列 位 如：[4,4,4,4] 此数组参数可以为null 如：[4,4,null,4]
     * @return 代码列表 结果 CK01#KQ01#C1-G1-L1-S1                或              CK01#KQ01#C1-G1-S1
     */
    @Override
    public List<String> generateCodes(String rule, String[] codes, Integer[] lengths) {
        if(StringUtils.isBlank(rule) || !rule.contains("|") || codes.length == 0 || lengths.length == 0){
            throw new IllegalArgumentException("入参为空或格式错误");
        }
        // 初始化结果集
        List<String> result = new ArrayList<>();
        // 解析规则 将连接符号取出来
        String[] split = rule.split("\\|");
        // 初始化 递增数组
        List<Integer> IncreaseList = new ArrayList<>();
        // 初始化 循环次数
        int forMaxLength = 1;
        for (int i = 0; i < lengths.length; i++) {
            if(lengths[i] == null){
                // 为空的列也要放入 递增数组中
                IncreaseList.add(null);
            }else{
                // 初始化递增集合
                IncreaseList.add(1);
                // 计算循环次数
                forMaxLength *= lengths[i];
            }
        }
        // 循环生成
        for (int l = 0; l < forMaxLength; l++) {
            // 循环生成字符串
            StringBuilder s = new StringBuilder();
            // 循环拼接字符串
            for (int i = 0; i < split.length; i++) {
                // 循环拼接字符串 取出固定前缀 如CK01 前缀不能为空
                if(i < codes.length && StringUtils.isNotBlank(codes[i])){
                    s.append(codes[i]);
                    s.append(split[i]);
                } else if(i >= codes.length){// 循环拼接字符串 取出连接符号 如-G ，-L， -S
                    // 计算递增数组下标
                    int index = i - codes.length;
                    // 如此位置为null 则不用填充
                    if(IncreaseList.get(index) == null){
                        continue;
                    }
                    // 将连接符号 填入字符串中
                    s.append(split[i]);
                    // 将其值 填入字符串中
                    s.append(IncreaseList.get(index));
                    // 循环至递增数组最后一位 计算递增数组
                    if(index == lengths.length - 1){
                        IncreaseList = getIncreaseMap(IncreaseList,lengths);
                    }
                }
            }
            result.add(s.toString());
        }

        return result;
    }

    /**
     * 递增数组计算
     * @param increaseList 递增数组
     * @param lengths 长度数组
     * @return 递增数组
     */
    private static List<Integer> getIncreaseMap(List<Integer> increaseList, Integer[] lengths){
        if(increaseList.size() != lengths.length){
            throw new IllegalArgumentException("入参错误，数组长度与自增数组长度不一致");
        }
        // 循环计算 递增数组 从后往前
        int maxIndex = increaseList.size() - 1;
        for (int i = maxIndex; i >= 0; i--) {
            Integer increaseValue = increaseList.get(i);
            // 如此位置为null 则不用计算
            if(increaseValue == null){
                continue;
            }else if(increaseValue < lengths[i]){// 如此位置未达到最大值 则递增
                increaseList.set(i, ++increaseValue);
                break;
            }else {// 如此位置达到最大值 则置为1 重新计算 并继续循环 将此下标前一位值也递增
                increaseList.set(i, 1);
            }
        }
        return increaseList;
    }

    public static void main(String[] args) {
        List<String> strings = new AssembleCodeServiceImpl().generateCodes("#|#|C|-G|-L|-S", new String[]{"CK01", "KQ01"}, new Integer[]{4, 4, null, 4});
        System.out.println(strings.toString());;
    }
}
