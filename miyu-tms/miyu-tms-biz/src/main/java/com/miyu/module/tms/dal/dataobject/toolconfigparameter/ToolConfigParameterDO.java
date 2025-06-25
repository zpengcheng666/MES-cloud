package com.miyu.module.tms.dal.dataobject.toolconfigparameter;

import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具参数信息 DO
 *
 * @author 上海弥彧
 */
@TableName("tms_tool_config_parameter")
@KeySequence("tms_tool_config_parameter_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolConfigParameterDO extends BaseDO {

    /**
     * 主键
     */
    @TableId
    private String id;
    /**
     * 刀具类型ID
     */
    private String toolConfigId;
    /**
     * 参数名称
     */
    private String name;
    /**
     * 参数值
     */
    private String value;
    /**
     * 参数缩写
     */
    private String abbr;
    /**
     * 计量单位
     */
    private String unit;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 分类 1 几何参数  2 切削参数
     */
    private Integer type;

}
