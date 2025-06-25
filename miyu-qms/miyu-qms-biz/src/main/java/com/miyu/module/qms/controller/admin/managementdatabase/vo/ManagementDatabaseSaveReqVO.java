package com.miyu.module.qms.controller.admin.managementdatabase.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 质量管理资料库新增/修改 Request VO")
@Data
public class ManagementDatabaseSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "22956")
    private String id;

    @Schema(description = "所属质量管理关联ID", example = "25724")
    private String treeId;

    @Schema(description = "类型", example = "2")
    private Integer type;

    @Schema(description = "附件", requiredMode = Schema.RequiredMode.REQUIRED, example = "https://www.iocoder.cn")
    @NotEmpty(message = "附件不能为空")
    private List<String> fileUrl;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "状态", example = "1")
    private Integer status;

    @Schema(description = "工作流编号", example = "14023")
    private String processInstanceId;

}