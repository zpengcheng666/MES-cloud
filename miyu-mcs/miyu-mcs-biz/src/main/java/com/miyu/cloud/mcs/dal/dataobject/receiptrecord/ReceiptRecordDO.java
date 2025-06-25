package com.miyu.cloud.mcs.dal.dataobject.receiptrecord;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 生产单元签收记录 DO
 *
 * @author miyu
 */
@TableName("mcs_receipt_record")
@KeySequence("mcs_receipt_record_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReceiptRecordDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 申请id
     */
    private String applicationId;
    /**
     * 配送详情id
     */
    private String distributionRecordId;
    /**
     * 需求详情id
     */
    private String demandRecordId;
    /**
     * 物料id
     */
    private String materialUid;
    /**
     * 物料批次码
     */
    private String batchNumber;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 物料条码
     */
    private Integer batch;
    /**
     * 申请单元
     */
    private String processingUnitId;
    /**
     * 资源类型
     */
    private String resourceType;
    /**
     * 资源类型编码
     */
    private String resourceTypeCode;
    /**
     * 需求数量
     */
    private Integer count;

}
