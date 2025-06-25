package com.miyu.module.pdm.dal.dataobject.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.JsonStringSetTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.util.Set;

@TableName(value = "capp_customized_attribute_val", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomizedAttributeValDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String objectId;

    private String attributeId;

    @TableField(typeHandler = JsonStringSetTypeHandler.class)
    private Set<String> attributeValue;

}
