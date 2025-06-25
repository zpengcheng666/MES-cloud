package com.miyu.module.mcc.controller.admin.materialconfig.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.util.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料类型 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialConfigRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "23213")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "物料编号")
    @ExcelProperty("物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "王五")
    @ExcelProperty("物料名称")
    private String materialName;

    @Schema(description = "当前类别", example = "8228")
    @ExcelProperty("当前类别")
    private String materialTypeId;

    @Schema(description = "主类别（工件、托盘、工装、夹具、刀具）", example = "22245")
    @ExcelProperty("主类别（工件、托盘、工装、夹具、刀具）")
    private String materialParentTypeId;

    @Schema(description = "物料规格")
    @ExcelProperty("物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    @ExcelProperty("物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    @ExcelProperty("物料单位")
    private String materialUnit;

    @Schema(description = "来源物料类型", example = "31201")
    @ExcelProperty("来源物料类型")
    private String materialSourceId;

    @Schema(description = "类码")
    @ExcelProperty("类码")
    private String materialTypeCode;
    @Schema(description = "物料管理模式")
    private Integer materialManage;


    @Schema(description = "来源类型号")
    private String materialNumberSource;
    @Schema(description = "来源类型名")
    private String materialSourceName;

    @Schema(description = "当前类名称")
    private String materialTypeName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    @Schema(description = "主类别名")
    private String materialParentTypeName;
    @Schema(description = "主类别码")
    private String materialParentTypeCode;

}