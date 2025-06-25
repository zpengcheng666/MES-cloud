package com.miyu.module.ppm.enums.consignmentrefund;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 采购退款状态
 */
@RequiredArgsConstructor
@Getter
public enum ConsignmentRefundEnum implements IntArrayValuable {

    CREATE(0, "已创建"),
    PROCESS(1, "审批中"),
    REFUNDING(2, "退款中"),
    FINISH(3, "结束"),
    CANCEL(9, "已作废"),
    REJECT(8, "审批失败");


    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ConsignmentRefundEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
