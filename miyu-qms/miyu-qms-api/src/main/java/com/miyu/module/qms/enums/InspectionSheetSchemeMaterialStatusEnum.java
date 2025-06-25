package com.miyu.module.qms.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/***
 * 检验单产品状态
 */
@RequiredArgsConstructor
@Getter
public enum InspectionSheetSchemeMaterialStatusEnum implements IntArrayValuable {


    FINISH(1, "已完成");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionSheetSchemeMaterialStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
