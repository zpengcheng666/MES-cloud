package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.result;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import ilog.concert.IloIntervalVar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 批次工序任务 DO
 *
 * @author 芋道源码
 */
@Schema(description = "工序任务，批次任务的下一级")
@Data
public class BatchRecordResult{

    /**
     * id
     */
    private String id;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 批次id
     */
    private String batchId;
    /**
     * 工序Id
     */
    private String processId;
    /**
     * 工序索引，表示顺序用的吧,目前未使用
     */
    private Integer index;
    /**
     * 编码
     */
    private String number;
    /**
     * 生产单元类型id
     */
    private String unitTypeIds;
    /**
     * 生产单元
     */
    private String processingUnitId;
    /**
     * 设备编号
     */
//    private String deviceNumber;
    /**
     * 设备id
     */
    private String deviceId;
    /**
     * 工序序号
     */
    private String procedureNum;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 预计开始时间
     */
    private LocalDateTime planStartTime;
    /**
     * 预计结束时间
     */
    private LocalDateTime planEndTime;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 前置批次Id
     */
    private String preRecordId;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 拆出去的零件id
     */
    private String exclOrderDetailId;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 加工状态(1本厂加工 2整单外协)
     */
    private Integer procesStatus;
    /**
     * 外协厂家
     */
    private String aidMill;
    /**
     * 工序时间
     */
    private Integer taskDuration;

    private List<String> deviceList;

    //工序区间变量
    private IloIntervalVar innerInterval;

}
