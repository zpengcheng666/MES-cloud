package com.miyu.module.tms.controller.admin.assembletask.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 刀具装配任务 Response VO")
@Data
@ExcelIgnoreUnannotated
public class AssembleRecordRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;
    @Schema(description = "配刀条码")
    private String appendageBarCode;
    @Schema(description = "配刀编号")
    private String appendageMaterialNumber;
    @Schema(description = "配刀类码")
    private String appendageMaterialCode;
    @Schema(description = "配刀类型")
    private String appendageMaterialName;
    @Schema(description = "配刀数量")
    private Integer count;
    @Schema(description = "配刀刀位")
    private Integer site;

    @Schema(description = "记录类型")
    private Integer type;

    private String materialStockId;

}