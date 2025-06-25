package com.miyu.module.qms.controller.admin.managementtree.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 质量管理关联树新增/修改 Request VO")
@Data
public class ManagementTreeSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1389")
    private String id;

    @Schema(description = "父节点")
    private String parent;

    @Schema(description = "节点关联字段ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "节点关联ID不能为空")
    private String nodeId;

    @Schema(description = "节点名", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "节点名不能为空")
    private String nodeName;

    @Schema(description = "节点类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer nodeType;

    @Schema(description = "备注", example = "随便")
    private String remark;

}