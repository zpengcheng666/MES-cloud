package com.miyu.module.tms.dal.dataobject.toolgroupdetail;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 刀具组装 DO
 *
 * @author zhangyunfei
 */
@TableName("tms_tool_group_detail")
@KeySequence("tms_tool_group_detail_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolGroupDetailDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 刀具组装id
     */
    private String toolGroupId;

    /**
     * 成品刀具类型id
     */
    private String mainConfigId;
    /**
     * 刀位（非必填）
     */
    private Integer site;
    /**
     * 装配刀具类型id
     */
    private String accessoryConfigId;
    /**
     * 数量(仅限配件使用)
     */
    private Integer count;


    @Schema(description = "物料编号")
    @TableField(exist = false)
    private String materialNumber;
    @Schema(description = "物料名称")
    @TableField(exist = false)
    private String materialName;
    @Schema(description = "物料类码")
    @TableField(exist = false)
    private String materialCode;


    @Schema(description = "物料类别")
    @TableField(exist = false)
    private String materialType;

    @Schema(description = "型号")
    @TableField(exist = false)
    private String toolModel;

    @Schema(description = "重量")
    @TableField(exist = false)
    private Double toolWeight;

    @Schema(description = "材质")
    @TableField(exist = false)
    private String toolTexture;

    @Schema(description = "涂层")
    @TableField(exist = false)
    private String toolCoating;

}
