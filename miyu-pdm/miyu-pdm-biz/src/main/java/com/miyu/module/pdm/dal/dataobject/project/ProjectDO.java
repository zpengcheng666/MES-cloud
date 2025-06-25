package com.miyu.module.pdm.dal.dataobject.project;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * PDM 项目 DO
 *
 * @author liuy
 */
@TableName("pdm_project")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * id
     */
    @TableId
    private Long id;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 项目编号
     */
    private String code;
}
