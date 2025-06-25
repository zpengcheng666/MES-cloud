package com.miyu.module.qms.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/***
 * 检测工具生效状态
 */
@RequiredArgsConstructor
@Getter
public enum InspectionToolStatusEnum implements IntArrayValuable {

    ENABLE(1, "启用"),
    NORMAL(2, "正常"),
    DISABLE(3, "停用"),
    SEAL(4, "封存"),
    INVALIDATE(5, "报废");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionToolStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
