package com.miyu.module.pdm.dal.dataobject.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("capp_process")
@KeySequence("capp_process_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessPlanDetailDO extends BaseDO {
    private String id;

    private String processCode;

    private String processVersion;

    private String processSchemeCode;

    private String partVersionId;

    private String partNumber;

    private String processScheme;

    private String materialId;

    private String materialDesg;

    private String materialCode;

    private String materialName;

    private String materialCondition;

    private String materialSpec;

    private String materialSpecification;

    private Integer singleSize;

    private String groupSize;

    private String processRouteName;

    private String singleQuantity;

    private String isValid;

    private String serialnum;

    private String description;

    @TableField
    private String descriptionPreview;

    private String partVersion;

    private String stepNum;

    private String stepName;

    private Integer stepProperty;

    private Integer procedureProperty;

    private String stepDescription;

    private String partName;

    private String processId;

    //新增的
    private Integer isOut;

    private String processCondition;
}
