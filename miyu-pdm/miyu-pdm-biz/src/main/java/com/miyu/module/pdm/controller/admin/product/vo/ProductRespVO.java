package com.miyu.module.pdm.controller.admin.product.vo;

import cn.iocoder.yudao.framework.excel.core.annotations.DictFormat;
import cn.iocoder.yudao.framework.excel.core.convert.DictConvert;
import cn.iocoder.yudao.module.system.enums.DictTypeConstants;
import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Schema(description = "PDM - 产品信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductRespVO {

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty("产品ID")
    private String id;

    @Schema(description = "产品名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "C系列")
    @NotBlank(message = "产品名称不能为空")
    @ExcelProperty("产品名称")
    private String productName;

    @Schema(description = "产品图号", requiredMode = Schema.RequiredMode.REQUIRED, example = "A220")
    @NotBlank(message = "产品图号不能为空")
    @ExcelProperty("产品图号")
    private String productNumber;

    @Schema(description = "设计单位", example = "庞巴迪")
    @ExcelProperty("设计单位")
    private String designUnit;

    @Schema(description = "状态，参见 CommonStatusEnum 枚举类", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @ExcelProperty(value = "产品状态", converter = DictConvert.class)
    @DictFormat(DictTypeConstants.COMMON_STATUS)
    private Integer status;

    @Schema(description = "产品描述", example = "我是一个产品")
    @ExcelProperty("产品描述")
    private String description;

    @Schema(description = "产品分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11161")
    private Long categoryId;
    @Schema(description = "产品分类", requiredMode = Schema.RequiredMode.REQUIRED, example = "水果")
    @ExcelProperty("产品分类")
    private String categoryName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "时间戳格式")
    private LocalDateTime updateTime;

}
