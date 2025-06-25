package com.miyu.cloud.dms.dal.dataobject.ledger;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 设备台账 DO
 *
 * @author miyu
 */
@TableName("dms_ledger")
@KeySequence("dms_ledger_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 设备/工位编号
     */
    private String code;
    /**
     * 设备/工位名称
     */
    private String name;
    /**
     * 所属产线/工位组
     */
    private String lintStationGroup;
    /**
     * 设备/工位类型
     */
    private String equipmentStationType;
    /**
     * 设备/工位
     */
    private Integer type;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 运行状态
     */
    private Integer runStatus;
    /**
     * 在线状态
     */
    private Integer onlineStatus;
    /**
     * 是否需要配送料
     */
    private Boolean needMaterials;
    /**
     * 位置
     */
    private String locationId;
    /**
     * 本机ip
     */
    private String ip;
    /**
     * 绑定设备id
     */
    private String bindEquipment;
    /**
     * 负责人
     */
    private String superintendent;
    /**
     * 采购日期
     */
    private LocalDateTime purchaseDate;
    /**
     * 维护日期
     */
    private LocalDateTime maintenanceDate;
    /**
     * 维护人员
     */
    private String maintenanceBy;
    /**
     * 检查日期
     */
    private LocalDateTime inspectionDate;
    /**
     * 检查人员
     */
    private String inspectionBy;
    /**
     * 参数1
     */
    private String technicalParameter1;
    /**
     * 参数2
     */
    private String technicalParameter2;
    /**
     * 参数3
     */
    private String technicalParameter3;
    /**
     * 参数4
     */
    private String technicalParameter4;

}
