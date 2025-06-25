package com.miyu.module.pdm.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ProcessChangeStatusEnum implements IntArrayValuable {
    WORKING(0, "工作中"),
    PROCESS(1, "审批中"),
    REJECT(2, "审批失败"),
    FINISH(3, "已定版"),
    ;

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ProcessChangeStatusEnum::getStatus).toArray();
    @Override
    public int[] array() {
        return ARRAYS;
    }
}
