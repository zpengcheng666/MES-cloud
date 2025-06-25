package com.miyu.module.pdm.dal.dataobject.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value="capp_process_version_nc")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVersionNcDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String stepId;

    private String procedureId;

    private String processVersionId;

    private String ncId;

}
