package com.miyu.module.pdm.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 工艺规程状态
 *
 * @author Liuy
 */
@RequiredArgsConstructor
@Getter
public enum ProcessVersionStatusEnum implements IntArrayValuable {

    WORKING(0, "工作中"),
    PROCESS(1, "审批中"),
    REJECT(2, "审批失败"),
    FINISH(3, "已完成"),
    ;

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ProcessVersionStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
