package com.miyu.module.pdm.dal.dataobject.processSupplement;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

//补加工工艺规程DO
@TableName(value="capp_process_supplement",autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessSupplementDO extends BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String projectCode;

    private String partNumber;

    private String processVersionId;

    private String procedureId;

    private String procedureNum;

    private String procedureIdNext;

    private String procedureNumNext;

    private String processCodeSupplement;

    private String description;

    private String status;

    private String processInstanceId;

    private Integer approvalStatus;

    @TableField(exist = false)
    private String processCode;
    @TableField(exist = false)
    private String partName;
    @TableField(exist = false)
    private String processCondition;
}
