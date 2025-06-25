package com.miyu.module.pdm.dal.dataobject.processTask;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("capp_process_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessDetailDO extends BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String projectCode;

    private String partVersionId;

    private String processVersionId;

    private Integer resourcesType;

    private String resourcesTypeId;

    private String quantity;


}
