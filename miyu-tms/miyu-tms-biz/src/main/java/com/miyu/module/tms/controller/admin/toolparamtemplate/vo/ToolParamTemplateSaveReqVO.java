package com.miyu.module.tms.controller.admin.toolparamtemplate.vo;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 刀具参数模板新增/修改 Request VO")
@Data
public class ToolParamTemplateSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "5139")
    private String id;

    @Schema(description = "模版名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "模版名称不能为空")
    private String templateName;

    @Schema(description = "刀具类别", requiredMode = Schema.RequiredMode.REQUIRED, example = "张三")
    @NotEmpty(message = "刀具类别不能为空")
    private String materialTypeId;

    @Schema(description = "版本号， 每次更新递增")
    private Integer version;

    @Schema(description = "状态(0:失效 1:有效)", example = "2")
    private Boolean status;

    /**
     * 参数模版详情列表
     */
    private List<ToolParamTemplateDetailDO> detailList;

}
