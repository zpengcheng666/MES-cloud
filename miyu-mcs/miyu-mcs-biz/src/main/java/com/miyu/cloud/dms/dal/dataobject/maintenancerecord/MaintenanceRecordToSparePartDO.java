package com.miyu.cloud.dms.dal.dataobject.maintenancerecord;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 设备保养维护记录使用备件表
 *
 * @author miyu
 */
@TableName("dms_maintenance_record_to_spare_part")
@KeySequence("dms_maintenance_record_to_spare_part")
// 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceRecordToSparePartDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 记录id
     */
    private String recordId;
    /**
     * 备件id
     */
    private String partId;
    /**
     * 使用数量
     */
    private Integer number;
}
