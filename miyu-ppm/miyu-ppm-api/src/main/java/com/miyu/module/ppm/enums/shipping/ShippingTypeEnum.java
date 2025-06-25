package com.miyu.module.ppm.enums.shipping;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/***
 * 发货状态
 */
@RequiredArgsConstructor
@Getter
public enum ShippingTypeEnum implements IntArrayValuable {


    SHIPPING(1, "销售发货"),
    OUTSOURCING(2, "外协发货"),
    CONSIGNMENT_RETURN(3, "采购退货"),
    COMMISSIONEDPROCESSING(4, "委托加工退货");


    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ShippingTypeEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
