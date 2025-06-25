package com.miyu.module.wms.dal.dataobject.checkdetail;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存盘点详情 DO
 *
 * @author QianJy
 */
@TableName("wms_check_detail")
@KeySequence("wms_check_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料id
     */
    private String materialStockId;
    /**
     * 账面数量
     */
    private Integer realTotality;
    /**
     * 盘点数量
     */
    private Integer checkTotality;
    /**
     * 盘点时间
     */
    private LocalDateTime checkTime;
    /**
     * 盘点容器id
     */
    private String checkContainerId;
    /**
     * 盘点类型
     */
    private Integer checkType;

    /**
     * 储位编码
     */
    private String storageCode;

    //物料管理模式
    @TableField(exist = false)
    private Integer materialManage;
    //物料编号
    @TableField(exist = false)
    private String materialNumber;
    //物料批次号
    @TableField(exist = false)
    private String batchNumber;
    //物料条码
    @TableField(exist = false)
    private String barCode;


}