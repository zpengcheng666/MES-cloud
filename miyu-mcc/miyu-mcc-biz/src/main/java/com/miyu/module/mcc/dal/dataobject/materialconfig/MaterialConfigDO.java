package com.miyu.module.mcc.dal.dataobject.materialconfig;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 物料类型 DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_material_config")
@KeySequence("mcc_material_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialConfigDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料编号
     */
    private String materialNumber;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 当前类别
     */
    private String materialTypeId;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    private String materialParentTypeId;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 物料品牌
     */
    private String materialBrand;
    /**
     * 物料单位
     */
    private String materialUnit;
    /**
     * 来源物料类型
     */
    private String materialSourceId;
    /**
     * 类码
     */
    private String materialTypeCode;


    private Integer materialManage;


    @TableField(exist = false)
    private String materialNumberSource;
    @TableField(exist = false)
    private String materialSourceName;

    @TableField(exist = false)
    private String materialTypeName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    @TableField(exist = false)
    private String materialParentTypeName;
    @TableField(exist = false)
    private String materialParentTypeCode;




    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
}