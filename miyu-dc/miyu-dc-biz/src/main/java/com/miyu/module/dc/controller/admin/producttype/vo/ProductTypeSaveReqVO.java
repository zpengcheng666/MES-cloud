package com.miyu.module.dc.controller.admin.producttype.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.miyu.module.dc.dal.dataobject.collectattributes.CollectAttributesDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 产品类型新增/修改 Request VO")
@Data
public class ProductTypeSaveReqVO {

    @Schema(description = "产品类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32310")
    private String Id;

    @Schema(description = "产品类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @NotEmpty(message = "产品类型名称不能为空")
    private String productTypeName;

    @Schema(description = "产品类型介绍", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "产品类型介绍不能为空")
    private String productTypeText;

    /**
     * 采集周期
     */
    private BigDecimal collectAttributesCycle;

    /**
     * 采集周期
     */
    private BigDecimal collectAttributesType;

    /**
     * 通信编码
     */
    private String topicId;
    /**
     * 采集属性
     */
    @TableField(exist = false)
    private List<CollectAttributesDO> collectAttributesDetails;

}