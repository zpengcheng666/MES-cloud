package com.miyu.module.pdm.dal.dataobject.processPlanDetail;


import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value="capp_process_change")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessChangeDO extends BaseDO {

    private String id;

    private String projectCode;

    private String processVersionId;

    private String changeCode;

    private String changeReason;

    private String changeContent;

    private String workingOpinions;

    private Integer status;

    private String processInstanceId;

    private Integer approvalStatus;

}
