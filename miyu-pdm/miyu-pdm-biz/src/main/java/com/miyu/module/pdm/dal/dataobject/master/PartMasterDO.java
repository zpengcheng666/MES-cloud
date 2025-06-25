package com.miyu.module.pdm.dal.dataobject.master;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("pdm_part_master")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartMasterDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 零件图号
     */
    private String partNumber;

    /**
     * 零件名称
     */
    private String partName;

    /**
     * 根节点id
     */
    private String rootProductId;


    /**
     * 域id
     */
    private String domainId;

    /**
     * 产品类型：0机型 1工装
     */
    private String productType;

    /**
     * 零部件类型
     */
    private String partType;

    /**
     * 加工类型
     */
    private String processType;

    /**
     * 加工状态
     */
    private String processCondition;

    /**
     * 材料代码
     */
    private String materialCode;

    /**
     * 材料分类
     */
    private String materialClassif;

    /**
     * 材料牌号
     */
    private String materialDesg;

    /**
     * 材料状态
     */
    private String materialCondition;
    /**
     * 材料标准
     */
    private String materialSpec;

    /**
     * 材料规格
     */
    private String materialDimension;

    /**
     * 材料id
     */
    private String materialId;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
