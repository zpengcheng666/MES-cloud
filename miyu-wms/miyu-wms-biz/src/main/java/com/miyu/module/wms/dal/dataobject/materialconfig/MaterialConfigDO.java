package com.miyu.module.wms.dal.dataobject.materialconfig;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

import java.util.List;

/**
 * 物料类型 DO
 *
 * @author QianJy
 */
@TableName("wms_material_config")
@KeySequence("wms_material_config_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialConfigDO extends BaseDO {

    public static final Long MATERIAL_PARENT_ID_ROOT = 0L;

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 托盘类型
     */
    private Integer containerType;

    /**
     * 出库规则
     */
    private Integer materialOutRule;
    /**
     * 是否单储位（0否、1是）
     */
    private Integer materialStorage;
    /**
     * 是否为容器（0否、1是）
     */
    private Integer materialContainer;

    /**
     * 是否质检（0否、1是）
     */
    private Integer materialQualityCheck;
    /**
     * 存放指定容器
     */
    private String containerConfigIds;
    /**
     * 层
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer materialLayer;
    /**
     * 排
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer materialRow;
    /**
     * 列
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Integer materialCol;
    /**
     * 默认存放仓库
     */
    private String defaultWarehouseId;

    /**
     * 预警库存
     */
    private Integer warningStock;


    /*********************************** 以下为mcc库关联表冗余字段 *******************************/

    /**
     * 父物料类型
     */
//    private String materialParentId;
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
     * 物料管理模式
     */
    private Integer materialManage;
    /**
     * 物料编号
     */
    private String materialNumber;
    /**
     * 物料类码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料属性（成品、毛坯、辅助材料）
     */
//    private Integer materialProperty;
    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
     */
    private String materialType;
    private String materialTypeName;
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

    /*********************************** 以上为mcc库关联表冗余字段 *******************************/


    @TableField(exist = false)
    private String areaCode;

    /*@TableField(exist = false)
    private String materialNumberParent;*/

    //存放指定容器
    @TableField(exist = false)
    private List<String> containerConfigNumbers;

    @TableField(exist = false)
    private String defaultWarehouseCode;
}
