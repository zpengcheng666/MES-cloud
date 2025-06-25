package com.miyu.module.pdm.dal.dataobject.procedure;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value="capp_procedure_file")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcedureFileDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String processVersionId;

    private String procedureId;

    private String sketchCode;

    private String sketchUrl;

}
