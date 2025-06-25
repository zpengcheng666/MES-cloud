package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 产品数据对象 Response VO")
@Data
public class PartDocumentVersionRespVO {

    /**
     * id
     */
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12076")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *DMID-文档id
     */
    @Schema(description = "DMID-文档id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14917")
    private String documentMasterId;

    /**
     *版本号
     */
    @Schema(description = "版本号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String documentVersion;

    /**
     *域id
     */
    @Schema(description = "域id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14917")
    private String domainId;

    /**
     *描述
     */
    @Schema(description = "描述", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String description;

    /**
     *关键字
     */
    @Schema(description = "关键字", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String keywords;

    /**
     *成熟度 目前是M1-M5
     */
    @Schema(description = "成熟度 目前是M1-M5", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String maturity;

    /**
     * 状态
     */
    @Schema(description = "状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String status;

    /**
     * 是否锁定
     */
    @Schema(description = "是否锁定", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String isLocked;

    /**
     * 创建人
     */
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String creator;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String updater;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String updateTime;


    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @TableField(exist = false)
    private String partVersionId;

    @Schema(description = "文件名", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @TableField(exist = false)
    private String documentName;

    @Schema(description = "文件类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @TableField(exist = false)
    private String documentType;
}
