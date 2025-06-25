package com.miyu.module.pdm.controller.admin.dataobject.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;


@Schema(description = "管理后台 - 产品数据对象新增/修改 Request VO")
@Data
public class DataObjectSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "9299")
    private String id;

    @Schema(description = "产品表ID", example = "19715")
    private String rootproductId;

    @Schema(description = "标准数据对象")
    private String stdDataObject;

    @Schema(description = "客户化数据对象")
    private String customizedDataObject;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "客户化类型:0固有 1客户化 ", example = "2")
    private Integer customizedType;

    @Schema(description = "数据表表名", example = "王五")
    private String tableName;

    @Schema(description = "客户化数据对象说明", example = "你猜")
    private String description;

    @Schema(description = "数据表实例化状态(1已实例化，不可更改；0未实例化)", example = "2")
    private String status;

    @Schema(description = "属性内容json(固有属性)")
    private String intrinsicAttrs;

    @Schema(description = "属性内容json(客户化属性)")
    private String customizedAttrs;

    @Schema(description = "排序")
    private Integer serialNumber;

    @Schema(description = "list")
    @TableField(exist = false)
    private List list;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建人")
    private String creator;

    @Schema(description = "更新时间")
    private LocalDateTime updateTime;

    @Schema(description = "更新人")
    private String updater;

    @TableField(exist = false)
    @Schema(description = "旧客户化标识")
    private String customizedIndexOld;

    @TableField(exist = false)
    public String WinTableDataStr;

    @TableField(exist = false)
    private List<Map<String,Object>> customizedAttrsList;

}