package com.miyu.cloud.dms.dal.dataobject.ledger;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("dms_ledger_to_location")
@KeySequence("dms_ledger_to_location") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LedgerToLocationDO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 设备台账
     */
    private String ledger;
    /**
     * 位置类型（0物料库位，1刀具库位，2仓库位置）
     */
    private Integer type;
    /**
     * 位置id
     */
    private String location;
}
