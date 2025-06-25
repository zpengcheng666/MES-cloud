package com.miyu.module.pdm.dal.dataobject.stepCategory;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("capp_customized_attribute")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizedAttributeDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 自定义属性对应的类别id
     */
    private String categoryId;

    /**
     * 属性中文名
     */
    private String attrNameCn;

    /**
     * 属性英文名
     */
    private String attrNameEn;

    /**
     * 属性类型
     */
    private Integer attrType;

    /**
     * 计量单位
     */
    private String attrUnit;

    /**
     * 排序
     */
    private Integer attrOrder;

    /**
     * 默认值
     */
    private String attrDefaultValue;

    /**
     * 是否多值
     */
    private Integer isMultvalues;

    /**
     * 有效值数组
     */
    private String attrEffectiveValue;
}
