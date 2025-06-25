package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 产品数据对象 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PartVersionRespVO {
    /**
     * 主键 id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String partMasterId;

    private String partVersion;

    private String documentVersionId;

    private String domainId;

    /**
     *是否锁定 1为是 0为否
     */
    private String isLocked;

    /**
     *成熟度 M1-M5
     */
    private String maturity;

    /**
     *审批状态
     */
    private String status;

    /**
     *数据表表名
     */
    private String tableName;

    /**
     * 单机数量
     */
    private String quantityPerPlane;

    @TableField(exist = false)
    private String partVersionId;

    @TableField(exist = false)
    /**
     * 零件图号
     */
    private String partNumber;

    /**
     * 零件名称
     */
    @TableField(exist = false)
    private String partName;

    /**
     * 加工状态
     */
    @TableField(exist = false)
    private String processCondition;

    private Integer source;
}