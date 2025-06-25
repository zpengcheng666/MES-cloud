package com.miyu.module.pdm.dal.dataobject.processVersion;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("capp_process_version")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVersionDO extends BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String sourceId;

    private String processId;

    private String processVersion;

    private String processName;

    private String description;

    private String designChangeId;

    private Integer property;

    private String processChangeId;

    private Integer status;

    private Integer isValid;

    private String processInstanceId;

    private Integer approvalStatus;
}
