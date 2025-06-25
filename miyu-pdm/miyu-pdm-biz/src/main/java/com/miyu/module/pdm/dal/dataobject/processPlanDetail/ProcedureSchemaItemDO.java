package com.miyu.module.pdm.dal.dataobject.processPlanDetail;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import lombok.*;

import java.math.BigDecimal;

@TableName("capp_procedure_scheme_item")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureSchemaItemDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String processVersionId;

    private String procedureId;

    private String inspectionItemId;

    private Integer number;

    private Integer referenceType;

    private BigDecimal maxValue;

    private BigDecimal minValue;

    private String content;

    private String judgement;

    private String acceptanceQualityLimit;

    @TableField(exist = false)
    private String inspectionSchemeName;

    @TableField(exist = false)
    private String itemName;
}
