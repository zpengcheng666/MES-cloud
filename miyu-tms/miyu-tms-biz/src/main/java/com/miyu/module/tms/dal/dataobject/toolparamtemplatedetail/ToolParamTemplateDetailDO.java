package com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 参数模版详情 DO
 *
 * @author zhangyunfei
 */
@TableName("tms_tool_param_template_detail")
@KeySequence("tms_tool_param_template_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolParamTemplateDetailDO extends BaseDO {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 参数模板主表ID
     */
    private String paramTemplateId;
    /**
     * 参数名称
     */
    private String name;
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
    /**
     * 是否必填  1 否 2 是
     */
    private Boolean required;
    /**
     * 默认参数值（下拉框时）
     */
    private String defaultValue;

}
