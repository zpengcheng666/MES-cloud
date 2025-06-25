package com.miyu.cloud.dms.controller.admin.devicetype.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 设备/工位类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DeviceTypeRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15312")
    @ExcelProperty("id")
    private String id;

    @Schema(description = "类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "HSG-9387")
    @ExcelProperty("类型编号")
    private String code;

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "4号控制器")
    @ExcelProperty("类型名称")
    private String name;

    @Schema(description = "设备/工位")
    @ExcelProperty("设备/工位")
    private Integer type;

    @ExcelProperty(value = "是否启用", converter = DictConvert.class)
    @Schema(description = "是否启用")
    @DictFormat("dms_device_type_enable")
    private Integer enable;

    @Schema(description = "规格型号")
    @ExcelProperty("规格型号")
    private String specification;

    @Schema(description = "生产厂家")
    @ExcelProperty("生产厂家")
    private String manufacturer;

    @Schema(description = "产地")
    @ExcelProperty("产地")
    private String countryRegion;

    @Schema(description = "厂家联系人")
    @ExcelProperty("厂家联系人")
    private String contacts;

    @Schema(description = "厂家联系电话")
    @ExcelProperty("厂家联系电话")
    private String contactPhone;

    @Schema(description = "备注")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建者")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新者")
    @ExcelProperty("更新者")
    private String updater;

    @Schema(description = "更新时间")
    @ExcelProperty("更新时间")
    private LocalDateTime updateTime;
}