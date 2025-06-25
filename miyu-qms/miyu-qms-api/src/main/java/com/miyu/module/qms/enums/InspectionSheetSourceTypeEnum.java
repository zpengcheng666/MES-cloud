package com.miyu.module.qms.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum InspectionSheetSourceTypeEnum implements IntArrayValuable {

    PURCHASE(1, "采购收货"),
    OUTSOURCE(2, "外协收货"),
    OUTSOURCEMATERIAL(3, "外协原材料退货"),
    DELEGATEPROCESS(4, "委托代加工收货"),
    SHIPPING(5, "销售退货"),
    PRODUCE(6, "生产"),
    MANUCREATE(7, "手动创建");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionSheetSourceTypeEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
