package com.miyu.module.pdm.dal.dataobject.structure;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 数据包结构表
 *
 * @author liuy
 */
@TableName("pdm_datapackage_structure")
@Data
@EqualsAndHashCode(callSuper = true)
public class StructureDO extends TenantBaseDO {

    public static final Long PARENT_ID_ROOT = 0L;

    /**
     * 结构编号
     */
    @TableId
    private Long id;
    /**
     * 父结构编号
     *
     * 关联 {@link #id}
     */
    private Long parentId;
    /**
     * 节点类型（0根节点 1结构 2文件夹 3文件）
     */
    private Integer type;
    /**
     * 节点名称（结构/文件夹/文件）
     */
    private String name;
    /**
     * 压缩包方式（zip）
     */
    private String compressType;
    /**
     * 设计单位
     */
    private String designUnit;
    /**
     * 结构状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 文件类型（0CATPart 1pdf 2excel 3xml等）
     */
    private Integer fileType;
    /**
     * 文件夹路径
     */
    private String dirPath;
    /**
     * 绝对路径
     */
    private String absolutePath;
    /**
     * excel列表起始行
     */
    private Integer startRow;
}
