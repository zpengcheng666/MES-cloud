package com.miyu.module.pdm.dal.dataobject.part;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@TableName("pdm_proj_part_bom")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewPartDO extends BaseDO {

    private String id;

    private String datapackageBomId;

    private String companyId;

    private String companyName;

    private String projectCode;

    private String partVersionId;

    private Integer quantity;

    private String deliverTime;

    private Integer status;

    private String projPartBomIdOri;

    private String gtIndex;

    private String tableName;

    @TableField(exist = false)
    private String rootproductId;
}
