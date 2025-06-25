package cn.iocoder.yudao.module.pms.api.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "订单加工状态")
@Data
public class ProductStatusRespDTO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32706")
    private String id;

    @Schema(description = "项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目名称(本地关联时写入)")
    private String projectName;

    @Schema(description = "图号,就是materialNumber")
    private String partNumber;

    @Schema(description = "数量")
    private Integer quantity;

    @Schema(description = "正在加工数量")
    private Integer processed;

    @Schema(description = "未加工数量")
    private Integer unprocessed;

    @Schema(description = "已完成数量")
    private Integer completed;

}
