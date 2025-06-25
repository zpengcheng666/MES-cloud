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
public enum InspectionSheetSchemeStatusEnum implements IntArrayValuable {


    TOASSIGN(0, "待派工"),
    TOINSPECT(1, "待检验"),
    INSPECTING(2, "检验中"),
    INSPECTED(3, "检验完成");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionSheetSchemeStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
