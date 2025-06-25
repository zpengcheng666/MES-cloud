package com.miyu.module.ppm.enums.purchaseconsignment;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ConsignmentTypeEnum implements IntArrayValuable {

    PURCHASE(1, "采购收货"),
    OUT(2, "外协收货"),//审批完成就等待签收
    OUT_MATERIAL(3, "外协原材料退货"),//填写收货信息后  变成待入库状态
    ORDER(4, "委托加工收货"),//如果需要质检 则更改状态为待质检  如果不需要则结束
    SHIPPING_RETURN(5, "销售退货");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ConsignmentTypeEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
