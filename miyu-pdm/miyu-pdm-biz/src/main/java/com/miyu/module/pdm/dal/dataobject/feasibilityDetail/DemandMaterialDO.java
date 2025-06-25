package com.miyu.module.pdm.dal.dataobject.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("capp_demand_material")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DemandMaterialDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String projectCode;

    private String partVersionId;

    private String materialCode;

    private String materialName;

    private String materialSpecification;

    private String description;
}
