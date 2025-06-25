package com.miyu.module.es.controller.admin.brakeSync.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "车闸管理 - 同步配置")
@Data
@ExcelIgnoreUnannotated
public class BrakeSyncRespVO {

    /**
     *
     */
    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16009")
    @ExcelProperty("主键id")
    private String id;

    /**
     *自动同步(1.开启 2.关闭)
     */
    @Schema(description = "自动同步")
    @ExcelProperty("自动同步")
    private Integer automatic;

    /**
     *周期
     */
    @Schema(description = "周期")
    @ExcelProperty("周期")
    private String cycle;

    /**
     * 厂区同步方式(1.不互通 2.仅新厂同步旧厂 3.仅旧厂同步新厂 4.互通）
     */
    @Schema(description = "厂区同步方式")
    @ExcelProperty("厂区同步方式")
    private Integer sync;
}
