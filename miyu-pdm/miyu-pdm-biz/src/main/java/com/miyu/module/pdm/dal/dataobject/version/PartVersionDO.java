package com.miyu.module.pdm.dal.dataobject.version;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

@TableName("pdm_part_version")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartVersionDO extends BaseDO {

    /**
     * 主键 id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    private String partMasterId;

    private String partVersion;

    private String documentVersionId;

    private String domainId;

    /**
     *是否锁定 1为是 0为否
     */
    private String isLocked;

    /**
     *成熟度 M1-M5
     */
    private String maturity;

    /**
     *审批状态
     */
    private String status;

    /**
     *数据表表名
     */
    private String tableName;

    /**
     * 单机数量
     */
    private String quantityPerPlane;

    @TableField(exist = false)
    private String partVersionId;

    @TableField(exist = false)
    /**
     * 零件图号
     */
    private String partNumber;

    /**
     * 零件名称
     */
    @TableField(exist = false)
    private String partName;

    /**
     * 加工状态
     */
    @TableField(exist = false)
    private String processCondition;

    /**
     * 来源(0 解析 1 新增)
     */
    private Integer source;
}