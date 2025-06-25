package com.miyu.module.pdm.dal.dataobject.toolingCategory;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 产品分类信息 DO
 *
 * @author 上海弥彧
 */
@TableName("pdm_tooling_category")
@KeySequence("pdm_tooling_category_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolingCategoryDO extends BaseDO {
    public static final Long PARENT_ID_ROOT = 0L;
    /**
     * 分类编号
     */
    @TableId()
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
     * 开启状态（0正常 1停用）
     */
    private Integer status;

}