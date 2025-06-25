package com.miyu.module.wms.dal.dataobject.checkcontainer;

import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 库存盘点容器 DO
 *
 * @author QianJy
 */
@TableName("wms_check_container")
@KeySequence("wms_check_container_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CheckContainerDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 盘点计划id
     */
    private String checkPlanId;
    /**
     * 容器库存id
     */
    private String containerStockId;

    /**
     * 容器储位列表
     */
    @TableField(exist = false)
    List<MaterialStorageDO> containerStoragelist;
    /**
     * 盘点状态
     *
     * 枚举 {@link TODO check_detail_status 对应的类}
     */
    private Integer checkStatus;

    @TableField(exist = false)
    private String locationId;

    /**
     * 盘点详情
     */
    @TableField(exist = false)
    List<CheckDetailDO> checkDetailList;


    /**
     * 盘点总数
     */
    @TableField(exist = false)
    private Integer totalCount;
    //物料条码
    @TableField(exist = false)
    private String barCode;
}