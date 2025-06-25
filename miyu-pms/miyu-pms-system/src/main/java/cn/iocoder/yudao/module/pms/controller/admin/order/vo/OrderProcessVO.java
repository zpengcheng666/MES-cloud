package cn.iocoder.yudao.module.pms.controller.admin.order.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - pms 立项表,项目立项相关 Response VO")
@Data
@ExcelIgnoreUnannotated
public class OrderProcessVO {

    @Schema(description = "立项id", requiredMode = Schema.RequiredMode.REQUIRED, example = "10226")
    private String id;

    @Schema(description = "项目编码")
    private String projectCode;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "项目预算(万元)")
    private BigDecimal budget;

    @Schema(description = "项目状态", example = "2")
    private Long projectStatus;

    @Schema(description = "工艺进度", example = "100")
    private Integer schemeProcess;

    @Schema(description = "采购进度", example = "100")
    private Integer purchaseProcess;

    @Schema(description = "生产进度", example = "100")
    private Integer productionProcess;

    @Schema(description = "工艺截止时间", example = "100")
    private LocalDateTime schemeTime;

    @Schema(description = "采购截止时间", example = "100")
    private LocalDateTime purchaseTime;

    @Schema(description = "生产截止时间,在交付前就行", example = "100")
    private LocalDateTime productionTime;
}
