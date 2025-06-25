package com.miyu.module.pdm.controller.admin.product.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "PDM - 产品分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductPageReqVO extends PageParam {

    @Schema(description = "产品名称，模糊匹配", example = "C系列")
    private String productName;

    @Schema(description = "产品图号，模糊匹配", example = "A220")
    private String productNumber;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

    @Schema(description = "产品分类编号", example = "11161")
    private Long categoryId;

    @Schema(description = "产品类型：0机型 1工装", example = "0")
    private Integer productType;

    @Schema(description = "创建时间", example = "[2022-07-01 00:00:00,2022-07-01 23:59:59]")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

}
