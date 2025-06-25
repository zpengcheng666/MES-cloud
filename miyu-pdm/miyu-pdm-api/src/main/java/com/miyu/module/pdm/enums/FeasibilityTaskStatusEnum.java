package com.miyu.module.pdm.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 技术评估任务状态
 *
 * @author Liuy
 */
@RequiredArgsConstructor
@Getter
public enum FeasibilityTaskStatusEnum implements IntArrayValuable {

    NOCREATE(0, "未指派"),
    NOWORK(1, "待评估"),
    WORKING(2, "评估中"),
    PROCESS(3, "审批中"),
    REJECT(4, "审批失败"),
    FINISH(5, "已完成"),
    ;

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(FeasibilityTaskStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
