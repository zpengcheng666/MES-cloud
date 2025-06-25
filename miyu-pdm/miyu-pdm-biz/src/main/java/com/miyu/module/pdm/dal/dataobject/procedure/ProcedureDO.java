package com.miyu.module.pdm.dal.dataobject.procedure;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.type.JsonLongSetTypeHandler;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.Set;

@TableName(value="capp_procedure",autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String processVersionId;

    private String procedureNum;

    private String procedureName;

    private String typicalId;

    private String isInspect;

    private Integer procedureProperty;

    private Integer isOut;

    private Integer preparationTime;

    private Integer processingTime;

    private String description;

    private String descriptionPreview;

    /** 源工序id-用于工艺规程升版 */
    @TableField(exist = false)
    private String procedureIdSource;

    @TableField(exist = false)
    private String processCode;

    @TableField(exist = false)
    private String processName;
}
