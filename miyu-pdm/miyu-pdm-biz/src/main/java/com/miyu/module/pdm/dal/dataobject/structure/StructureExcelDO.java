package com.miyu.module.pdm.dal.dataobject.structure;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 入库详情 DO
 *
 * @author Liuy
 */
@TableName("pdm_datapackage_structure_excel")
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StructureExcelDO extends BaseDO {
    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * excel规则编号
     */
    @TableId
    private Long id;
    /**
     * 结构编号
     */
    private Long structureId;
    /**
     * 行号
     */
    private Integer rowNum;
    /**
     * 列号
     */
    private Integer columnNum;
    /**
     * 内容
     */
    private Integer contentType;
    /**
     * 关联路径
     */
    private String dirPath;
}
