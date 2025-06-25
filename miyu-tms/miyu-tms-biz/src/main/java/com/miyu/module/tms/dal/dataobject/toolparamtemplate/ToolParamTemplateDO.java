package com.miyu.module.tms.dal.dataobject.toolparamtemplate;

import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import lombok.*;
import java.util.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具参数模板 DO
 *
 * @author zhangyunfei
 */
@TableName("tms_tool_param_template")
@KeySequence("tms_tool_param_template_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolParamTemplateDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 刀具类别
     */
    private String materialTypeId;

    /**
     * 刀具类型ID
     */
    private String toolConfigId;

    /**
     * 刀具编号
     */
    private String toolNumber;

    /**
     * 刀具类码
     */
    private String toolTypeCode;

    /**
     * 版本号， 每次更新递增
     */
    private Integer version;
    /**
     * 状态(0:失效 1:有效)
     */
    private Integer status;


    /**
     * 刀具类别名称
     */
    @TableField(exist = false)
    private String materialTypeName;

    /**
     * 参数模版详情列表
     */
    @TableField(exist = false)
    private List<ToolParamTemplateDetailDO> detailList;
}
