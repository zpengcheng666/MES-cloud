package com.miyu.module.pdm.dal.dataobject.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName(value="capp_nc")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NcDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String ncName;

    private String ncUrl;

    @TableField(exist = false)
    private String processVersionId;

    @TableField(exist = false)
    private String procedureId;

    @TableField(exist = false)
    private String stepId;

}
