package com.miyu.module.pdm.dal.dataobject.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("capp_quota_per_part")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotaPerPartDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String partVersionId;

    private Integer length;

    private String width;

    private String height;

    private String materialDesg;

    private String materialCondition;

    private String timeQuota;

}
