package com.miyu.module.ppm.enums.common;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 合同发票 的审批状态
 *
 * @author zhangyunfei
 */
@RequiredArgsConstructor
@Getter
public enum InvoiceAuditStatusEnum implements IntArrayValuable {

    DRAFT(0, "未提交"),
    PROCESS(1, "审批中"),
    APPROVE(2, "审核通过"),
	REJECT(3, "审核不通过"),
    CANCEL(4, "已取消");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InvoiceAuditStatusEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
