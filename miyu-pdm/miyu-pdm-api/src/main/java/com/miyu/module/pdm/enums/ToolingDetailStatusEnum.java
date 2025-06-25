package com.miyu.module.pdm.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ToolingDetailStatusEnum implements IntArrayValuable {

    NOCREATE(0, "未指派"),
    NOWORK(1, "待设计"),
    WORKING(2, "设计中"),
    PROCESS(3, "审批中"),
    REJECT(4, "审批失败"),
    FINISH(5, "已完成"),
    ;
            ;

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(ToolingDetailStatusEnum::getStatus).toArray();
    @Override
        public int[] array() {
            return ARRAYS;
        }
    }

