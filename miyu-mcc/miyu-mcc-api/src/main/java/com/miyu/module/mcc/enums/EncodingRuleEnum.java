package com.miyu.module.mcc.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * CRM 的审批状态
 *
 * @author 赤焰
 */
@RequiredArgsConstructor
@Getter
public enum EncodingRuleEnum implements IntArrayValuable {

    DEFAULT(1, "固定"),
    CUSTOM(2, "自定义"),
    AUTOGENERATOR(3, "自生成"),
	NOW(4, "当前类别"),
    ALL(5, "所有类别");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(EncodingRuleEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
