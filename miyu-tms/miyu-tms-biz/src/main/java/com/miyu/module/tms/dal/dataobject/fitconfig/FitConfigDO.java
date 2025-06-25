package com.miyu.module.tms.dal.dataobject.fitconfig;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具适配 DO
 *
 * @author zhangyunfei
 */
@TableName("tms_fit_config")
@KeySequence("tms_fit_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FitConfigDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 刀具类型id
     */
    private String toolConfigId;
    /**
     * 适配类型id
     */
    private String fitToolConfigId;

    /**
     * 名称
     */
    @TableField(exist = false)
    private String toolName;

    /**
     * 型号
     */
    @TableField(exist = false)
    private String toolModel;

    /**
     * 刀具类码
     */
    @TableField(exist = false)
    private String materialTypeCode;

}
