package com.miyu.module.pdm.dal.dataobject.part;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("pdm_data_object")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 产品表ID
     */
    private String rootproductId;
    /**
     * 标准数据对象
     */
    private String stdDataObject;
    /**
     * 客户化数据对象
     */
    private String customizedDataObject;
    /**
     * 客户化标识
     */
    private String customizedIndex;
    /**
     * 客户化类型:0固有 1客户化
     */
    private Integer customizedType;
    /**
     * 数据表表名
     */
    private String tableName;
    /**
     * 客户化数据对象说明
     */
    private String description;
    /**
     * 数据表实例化状态(1已实例化，不可更改；0未实例化)
     */
    private String status;
    /**
     * 属性内容json(固有属性)
     */
    private String intrinsicAttrs;
    /**
     * 属性内容json(客户化属性)
     */
    private String customizedAttrs;
    /**
     * 排序
     */
    private Integer serialNumber;


    @TableField(exist = false)
    private String productNumber;

}
