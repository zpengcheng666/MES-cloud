package com.miyu.module.mcc.enums;

import cn.iocoder.yudao.framework.common.core.IntArrayValuable;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

/**
 * 类别属性
 *
 * @author 赤焰
 */
@RequiredArgsConstructor
@Getter
public enum MaterialTypePropertyEnum implements IntArrayValuable {

    MOD(1, "零件"),
    TRAY(2, "托盘"),
    WORKWEAR(3, "工装"),
	CLAMP(4, "夹具"),
    CUTTER(5, "刀具"),
    KINIFEHANDLE(6, "刀柄"),
    BLADE(7, "刀片"),
    MEASURINGTOOL(8, "工量具");

    private final Integer status;
    private final String name;

    public static final int[] ARRAYS = Arrays.stream(values()).mapToInt(MaterialTypePropertyEnum::getStatus).toArray();

    @Override
    public int[] array() {
        return ARRAYS;
    }

}
