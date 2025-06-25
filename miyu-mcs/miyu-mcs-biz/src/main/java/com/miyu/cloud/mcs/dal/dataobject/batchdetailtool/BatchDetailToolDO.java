package com.miyu.cloud.mcs.dal.dataobject.batchdetailtool;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次详情-工装 DO
 *
 * @author 上海弥彧
 */
@TableName("mcs_batch_detail_tool")
@KeySequence("mcs_batch_detail_tool_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDetailToolDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 零件批次任务id
     */
    private String batchRecordId;
    /**
     * 作业记录id
     */
    private String productionRecordId;
    /**
     * 物料类型id
     */
    private String materialConfigId;
    /**
     * 加工设备id
     */
    private String equipmentId;
    /**
     * 工序id
     */
    private String processId;
    /**
     * 工步名称
     */
    private String stepName;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料批次码
     */
    private String batchNumber;
    /**
     * 物料类型编码
     */
    private String materialNumber;
    /**
     * 数量
     */
    private Integer totality;

}
