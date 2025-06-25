package com.miyu.module.pdm.dal.dataobject.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value="capp_step_file")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepFileDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String processVersionId;

    private String procedureId;

    private String stepId;

    private String sketchCode;

    private String sketchUrl;

}
