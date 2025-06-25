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
public enum ShippingStatusEnum  implements IntArrayValuable {


    CREATE(0, "已创建"),
    PROCESS(1, "审批中"),
    OUTBOUND(2, "待出库"),
    OUTBOUNDING(3, "出库中"),
    SEND(4, "发货确认"),
    FINISH(5, "结束"),
    CANCEL(9, "已作废"),
    REJECT(8, "审批失败");


    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ShippingStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
