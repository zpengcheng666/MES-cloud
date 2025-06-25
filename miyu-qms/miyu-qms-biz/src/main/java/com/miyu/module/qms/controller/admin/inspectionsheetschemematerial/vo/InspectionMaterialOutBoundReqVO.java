package com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@Schema(description = "管理后台 - 检验单产品出库 Request VO")
@Data
@ToString(callSuper = true)
public class InspectionMaterialOutBoundReqVO {

    @Schema(description = "仓库编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "仓库编码不能为空")
    private String warehouseCode;

    @Schema(description = "检验单产品id集合", example = "3274")
    private List<String> ids;
}
