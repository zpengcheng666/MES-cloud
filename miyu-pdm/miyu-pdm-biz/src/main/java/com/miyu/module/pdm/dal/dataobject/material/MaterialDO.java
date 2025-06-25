package com.miyu.module.pdm.dal.dataobject.material;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * PDM 工装-临时 DO
 *
 * @author liuy
 */
@TableName("mcc_material")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaterialDO extends BaseDO {
    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 物料编号
     */
    private String materialNumber;
    /**
     * 物料类码
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 物料规格
     */
    private String materialSpecification;
    /**
     * 物料品牌
     */
    private String materialBrand;
    /**
     * 物料单位
     */
    private String materialUnit;
    /**
     * 物料管理模式
     */
    private Integer materialManage;
    /**
     * 是否质检
     */
    private Integer materialQualityCheck;
    /**
     * 父物料类型
     */
    private String materialParentId;
    /**
     * 物料属性（成品、毛坯、辅助材料）
     */
    private Integer materialProperty;
    /**
     * 物料类型（零件、托盘、工装、夹具、刀具）
     */
    private Integer materialType;

}
