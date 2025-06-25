package com.miyu.module.pdm.dal.dataobject.product;

import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.tenant.core.db.TenantBaseDO;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 产品 DO
 *
 * @author liuy
 */
@TableName(value = "pdm_root_product", autoResultMap = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDO extends TenantBaseDO {

    /**
     * 产品ID
     */
    @TableId
    private String id;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品图号
     */
    private String productNumber;
    /**
     * 设计单位
     */
    private String designUnit;
    /**
     * 产品状态
     *
     * 枚举 {@link CommonStatusEnum}
     */
    private Integer status;
    /**
     * 产品描述
     */
    private String description;
    /**
     * 产品分类编号
     *
     * 关联 {@link ProductCategoryDO#getId()}
     */
    private Long categoryId;
    /**
     * 产品类型：0机型 1工装
     */
    private Integer productType;

}
