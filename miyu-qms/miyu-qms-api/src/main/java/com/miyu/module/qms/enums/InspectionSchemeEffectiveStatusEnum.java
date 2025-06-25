package com.miyu.module.qms.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/***
 * 生效状态
 */
@RequiredArgsConstructor
@Getter
public enum InspectionSchemeEffectiveStatusEnum implements IntArrayValuable {


    NOEFFECTIVE(0, "未生效"),
    EFFECTIVE(1, "已生效");


    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionSchemeEffectiveStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
