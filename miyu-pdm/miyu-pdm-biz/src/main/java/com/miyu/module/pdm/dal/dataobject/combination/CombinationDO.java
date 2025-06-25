package com.miyu.module.pdm.dal.dataobject.combination;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * PDM 刀组-临时 DO
 *
 * @author liuy
 */
@TableName("cm_combination")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CombinationDO extends BaseDO {

    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String cutterGroupCode;

    private String taperTypeName;

    private String hiltMark;

}
