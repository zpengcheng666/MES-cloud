package com.miyu.module.ppm.enums.common;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 合同状态
 *
 * @author zhangyunfei
 */
@RequiredArgsConstructor
@Getter
public enum ContractStatusEnum implements IntArrayValuable {

    DRAFT(0, "已生成"),
    PROCESS(1, "执行中"),
    TERMINATE(2, "已终止"),
    INVALID(3, "已作废"),
    FINISH(4, "已完成");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ContractStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
