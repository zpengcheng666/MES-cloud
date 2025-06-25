package com.miyu.module.pdm.dal.dataobject.stepCategory;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * PDM 工步分类 DO
 *
 * @author liuy
 */
@TableName("capp_step_category")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StepCategoryDO extends BaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 分类编号
     */
    @TableId
    private Long id;
    /**
     * 父分类编号
     */
    private Long parentId;
    /**
     * 分类名称
     */
    private String name;
    /**
     * 分类编码
     */
    private String code;
    /**
     * 分类排序
     */
    private Integer sort;
    /**
     * 开启状态
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;

}