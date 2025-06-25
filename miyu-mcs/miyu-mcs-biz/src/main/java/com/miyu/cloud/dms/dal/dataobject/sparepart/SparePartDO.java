package com.miyu.cloud.dms.dal.dataobject.sparepart;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 备品/备件 DO
 *
 * @author 王正浩
 */
@TableName("dms_spare_part")
@KeySequence("dms_spare_part_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SparePartDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 备件编码
     */
    private String code;
    /**
     * 备件名称
     */
    private String name;
    /**
     * 备件数量
     */
    private Integer number;
    /**
     * 备件类型
     */
    private String type;
    /**
     * 备注
     */
    private String remark;

}