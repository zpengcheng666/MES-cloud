package com.miyu.cloud.mcs.dal.dataobject.batchdetailnc;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 批次详情-设备 DO
 *
 * @author 上海弥彧
 */
@TableName("mcs_batch_detail_nc")
@KeySequence("mcs_batch_detail_nc_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BatchDetailNcDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 零件批次任务id
     */
    private String batchDetailId;
    /**
     * 作业记录id
     */
    private String productionRecordId;
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
     * 程序名称
     */
    private String ncName;
    /**
     * 版本
     */
    private String version;
    /**
     * 加工顺序
     */
    private Integer sort;

}
