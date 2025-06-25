package com.miyu.cloud.mcs.dal.dataobject.orderdetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 生产订单详情 DO
 *
 * @author miyu
 */
@TableName("mcs_order_detail")
@KeySequence("mcs_order_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailDO1 extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 详情编码
     */
    private String detailNumber;
    /**
     * 订单id
     */
    private String orderId;
    /**
     * 当前生产单元
     */
    private String processingUnitName;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 物料id 绑定工件时/物料变更时 更新
     */
    private String materialId;
    /**
     * 物料条码 绑定工件时/物料变更时 更新
     */
    private String barCode;
    /**
     * 当前物料批次
     */
    private String partBatchNumber;
    /**
     * 批量管理
     */
    private Boolean isBatch;
    /**
     * 数量
     */
    private Integer count;
    /**
     * 拆分父零件id
     */
    private String parentId;
    /**
     * 拆分起始任务id
     */
    private String splitRecordId;
    /**
     * 外协唯一码
     */
    private String outsourcingId;
    /**
     * 外协厂家
     */
    private String aidMill;

}
