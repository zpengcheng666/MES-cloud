package com.miyu.module.pdm.dal.dataobject.procedure;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("capp_procedure_detail")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureDetailDO extends BaseDO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String processVersionId;

    private String procedureId;

    private Integer resourcesType;

    private String resourcesTypeId;

    private String quantity;


}
