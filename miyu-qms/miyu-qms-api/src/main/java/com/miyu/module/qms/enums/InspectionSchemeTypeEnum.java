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
public enum InspectionSchemeTypeEnum implements IntArrayValuable {


    PURCHASETYPE(1, "来料检验"),
    PRODUCTTYPE(2, "生产检验"),
    FINISHTYPE(3, "成品检验");
    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(InspectionSchemeTypeEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }
}
