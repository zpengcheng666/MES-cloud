package com.miyu.module.ppm.enums.purchaseconsignment;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ConsignmentStatusEnum implements IntArrayValuable {

    CREATE(0, "已创建"),
    PROCESS(1, "审批中"),
    SINGING(2, "待签收"),//审批完成就等待签收
    INBOUND(3, "入库中"),//填写收货信息后  变成待入库状态
    ANALYSIS(4, "待质检"),//如果需要质检 则更改状态为待质检  如果不需要则结束
    ANALYSISING(5, "质检中"),//如果需要质检 则更改状态为待质检  如果不需要则结束
    FINISH(6, "结束"),
    CANCEL(7, "审批不通过"),
    REJECT(8, "已作废"),
    CONFIRM(9, "待确认"),
    RETURN(10, "退货");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ConsignmentStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
