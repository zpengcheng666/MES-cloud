package com.miyu.cloud.macs.dal.dataobject.region;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import org.apache.ibatis.type.JdbcType;

/**
 * 区域 DO
 *
 * @author 芋道源码
 */
@TableName("macs_region")
@KeySequence("macs_region_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 区域编码
     */
    private String code;
    /**
     * 区域名称
     */
    private String name;
    /**
     * 公开状态(0未公开,1公开)
     */
    private Boolean publicStatus;
    /**
     * 描述
     */
    private String description;
    /**
     * 上级id
     */
    private String parentId;
    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT, jdbcType = JdbcType.VARCHAR)
    private String createBy;
    /**
     * 更新人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE, jdbcType = JdbcType.VARCHAR)
    private String updateBy;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

}
