package com.miyu.cloud.dms.controller.admin.sparepart.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 备品/备件新增/修改 Request VO")
@Data
public class SparePartSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14340")
    private String id;

    @Schema(description = "备件编码")
    private String code;

    @Schema(description = "备件名称", example = "芋艿")
    private String name;

    @Schema(description = "备件数量")
    private Integer number;

    @Schema(description = "备件类型", example = "2")
    private String type;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}