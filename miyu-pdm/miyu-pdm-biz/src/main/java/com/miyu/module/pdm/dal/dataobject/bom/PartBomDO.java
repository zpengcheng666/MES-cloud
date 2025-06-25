package com.miyu.module.pdm.dal.dataobject.bom;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import java.time.LocalDateTime;

@TableName("pdm_datapackage_bom")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartBomDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String partVersionId;

    private int quantity;

    private LocalDateTime deliverTime;

    private String receiveInfoId;

    private String tableName;

    private String creator;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    private String updater;

}
