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
public enum InspectionSelfAssignmentStatusEnum implements IntArrayValuable {

    TOINSPECT_SELF(0, "待自检"),
    TOINSPECT_MUTUAL(1, "待互检"),
    TOCLAIM(2, "待认领"),
    TOINSPECT_SPEC(3, "待专检"),
    TOASSIGN(4, "待分配"),
    ASSIGNED(5, "已分配");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionSelfAssignmentStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
