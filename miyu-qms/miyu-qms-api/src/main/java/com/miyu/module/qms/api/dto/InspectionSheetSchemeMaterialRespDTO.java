package com.miyu.module.qms.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * 检验单产品 DO
 *
 * @author Zhangyunfei
 */
@Schema(description = "RPC 服务 - 质量系统 检验单产品 Response DTO")
@Data
public class InspectionSheetSchemeMaterialRespDTO {

    /**
     * 主键
     */
    private String id;
    /**
     * 测量结果
     */
    private String content;
    /**
     * 是否合格
     */
    private Integer inspectionResult;
    /**
     * 物料ID
     */
    private String materialId;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次号
     */
    private String batchNumber;


    /**
     * 物料条码
     */
    private String barCodeCheck;

    /**
     * 产品检验状态
     */
    private Integer status;

    /**
     * 互检是否合格
     */
    private Integer mutualInspectionResult;


    /**
     * 专检是否合格
     */
    private Integer specInspectionResult;


    /**
     * 检验单任务表ID
     */
    private String inspectionSheetSchemeId;

    /**
     * 生产单号
     */
    private String recordNumber;
}
