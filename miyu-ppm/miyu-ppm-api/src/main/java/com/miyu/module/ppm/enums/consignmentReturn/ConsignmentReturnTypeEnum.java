package com.miyu.module.ppm.enums.consignmentReturn;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 采购退货单状态
 */
@RequiredArgsConstructor
@Getter
public enum ConsignmentReturnTypeEnum implements IntArrayValuable {

    REPAIR(1, "返修"),
    CHANGE(2, "换货"),
    RETURNANDREFUND(3, "退货退款"),
    REFUND(4, "仅退款");


    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ConsignmentReturnTypeEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
