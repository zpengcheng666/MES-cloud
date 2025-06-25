package com.miyu.module.pdm.controller.admin.combination.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 刀组列表-临时 Response VO")
@Data
@ExcelIgnoreUnannotated
public class CombinationRespVO {

    @Schema(description = "刀组ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("刀组ID")
    private String id;

    @Schema(description = "刀组编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String cutterGroupCode;

    @Schema(description = "刀柄类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String taperTypeName;

    @Schema(description = "刀柄通用规格", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String hiltMark;

    @TableField(exist = false)
    @Schema(description = "刀简号", requiredMode = Schema.RequiredMode.REQUIRED, example = "111")
    private String cutternum;
}
