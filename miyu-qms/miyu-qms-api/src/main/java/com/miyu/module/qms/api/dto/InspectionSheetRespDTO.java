package com.miyu.module.qms.api.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 检验单 DTO
 *
 * @author Zhangyunfei
 */

@Schema(description = "RPC 服务 - 质量系统 检验单 Response DTO")
@Data
public class InspectionSheetRespDTO {

    /**
     * 主键
     */
    private String id;

    /**
     * 检验单名称
     */
    private String sheetName;
    /**
     * 检验单号
     */
    private String sheetNo;

    /**
     * 源单号
     */
    private String recordNumber;

    /**
     * 质检状态  0待派工1待检验2检验中3检验完成
     */
    private Integer status;
    /**
     * 负责人
     */
    private String header;
    /**
     * 开始时间
     */
    private LocalDateTime beginTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    @Schema(description = "检验任务列表")
    private List<Scheme> schemes;

    @Schema(description = "检验任务列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Scheme {

        /**
         * 主键
         */
        private String id;
        /**
         * 检验单Id
         */
        private String inspectionSheetId;

        /**
         * 分配类型 1人员 2班组
         */
        private Integer assignmentType;
        /**
         * 分配的检验人员
         */
        private String assignmentId;
        /**
         * 分配日期
         */
        private LocalDateTime assignmentDate;
        /**
         * 质检状态  0待派工1待检验2检验中3检验完成
         */
        private Integer status;
        /**
         * 方案类型 来料检验  生产检验
         */
        private Integer schemeType;
        /**
         * 检验类型1抽检2全检
         */
        private Integer inspectionSheetType;
        /**
         * 是否首检
         */
        private Integer needFirstInspection;
        /**
         * 物料类型ID
         */
        private String materialConfigId;
        /**
         * 物料编号
         */
        private String materialNumber;
        /**
         * 物料类码
         */
        private String materialCode;
        /**
         * 物料名称
         */
        private String materialName;
        /**
         * 物料属性（成品、毛坯、辅助材料）
         */
        private Integer materialProperty;
        /**
         * 物料类型（零件、托盘、工装、夹具、刀具）
         */
        private Integer materialType;
        /**
         * 物料规格
         */
        private String materialSpecification;
        /**
         * 物料品牌
         */
        private String materialBrand;
        /**
         * 物料单位
         */
        private String materialUnit;
        /**
         * 工艺ID
         */
        private String technologyId;
        /**
         * 工序ID
         */
        private String processId;
        /**
         * 检验方案ID
         */
        private String inspectionSchemeId;
        /**
         * 通过准则
         */
        private String passRule;
        /**
         * 计划检验时间
         */
        private LocalDateTime planTime;
        /**
         * 实际开始时间
         */
        private LocalDateTime beginTime;
        /**
         * 实际结束时间
         */
        private LocalDateTime endTime;
        /**
         * 检测数量
         */
        private Integer inspectionQuantity;
        /**
         * 合格数量
         */
        private Integer qualifiedQuantity;
        /**
         * 检测结果 1合格 2不合格
         */
        private Integer inspectionResult;

        /**
         * 实际检测数量
         */
        private Integer quantity;

        /**
         * 检验水平类型
         */
        private Integer inspectionLevelType;

        /**
         * 类型  1正常检查2加严检查3放宽检查
         */
        private Integer samplingRuleType;

        /**
         * 物料批次号
         */
        private String batchNumber;

        /**
         * 是否自检
         */
        private Integer selfInspection;
    }

}