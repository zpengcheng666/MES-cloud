package com.miyu.module.ppm.dal.dataobject.material;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料基本信息 DO
 *
 * @author zhangyunfei
 */
@TableName("material")
@KeySequence("material_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDO extends BaseDO {

    /**
     * 名臣
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 类型
     */
    private Integer type;
    /**
     * 名称
     */
    private String name;
    /**
     * 创建IP
     */
    private String creationIp;
    /**
     * 更新ip
     */
    private String updatedIp;

}