package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.result;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 批次级订单 DO
 *
 * @author miyu
 */
@Schema(description = "批次任务，生产订单的下一级")
@Data
public class BatchOrderResult extends BaseDO {

    /**
     * id
     */
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 任务索引
     */
    private Integer index;
    /**
     * 批次订单编码
     */
    private String batchNumber;
    /**
     * 工艺规程版本Id
     */
    private String technologyId;
    /**
     * 工艺规程版本编码
     */
    private String technologyCode;
    /**
     * 起始顺序id
     */
    private String beginProcessId;
    /**
     * 拆单工序
     */
    private String splitProcessId;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 预计开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 截止日期
     */
    private LocalDateTime planEndTime;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 前置批次
     */
    private String preBatchId;

    private Integer status;

    private Integer submitStatus;
    /**
     * 批量管理
     */
    private Boolean isBatch;

    /**
     * 加工类型(1本厂 2外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;

    /**
     * 物料条码
     */
    private String barCode;

    private String preBatchNumber;

    private String orderNumber;

    private String firstBatchId;

    private List<BatchRecordDO> detailList;

    private List<BatchRecordResult> recordResultList = new ArrayList<>();
}
