package com.miyu.cloud.mcs.dto.productionProcess;

import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class McsProjectOrderDTO {

    private String id;
    /**
     * 订单编号
     */
    private String orderNumber;
    /**
     * 项目号
     */
    private String projectNumber;
    /**
     * 零件图号id
     */
    private String partVersionId;
    /**
     * 零件图号
     */
    private String partNumber;
    /**
     * 工艺规程版本Id
     */
    private String technologyId;
    /**
     * 订单类型
     */
    private Integer orderType;
    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 接收时间
     */
    private LocalDateTime receptionTime;
    /**
     * 交付时间
     */
    private LocalDateTime deliveryTime;
    /**
     * 完成时间
     */
    private LocalDateTime completionTime;
    /**
     * 负责人
     */
    private String responsiblePerson;
    /**
     * 订单状态
     */
    private Integer status;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 物料编码集合
     */
    private String materialCode;
    /**
     * 加工状态(1本厂加工 2整单外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;
    /**
     * 零件
     */
    private List<McsBatchRecordDTO> partList;

}
