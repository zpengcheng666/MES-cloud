package com.miyu.module.mcc.dal.dataobject.materialtype;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 编码类别属性表(树形结构) DO
 *
 * @author 上海弥彧
 */
@TableName("mcc_material_type")
@KeySequence("mcc_material_type_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialTypeDO extends BaseDO {

    public static final String PARENT_ID_ROOT = "0";

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父类型id
     */
    private String parentId;
    /**
     * 位数
     */
    private Integer bitNumber;
    /***
     * 层级
     */
    private Integer level;
    /**
     * 总层级
     */
    private Integer levelLimit;

    /***
     * 分类
     */
    private Integer encodingProperty;


}