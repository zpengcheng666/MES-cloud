package com.miyu.module.ppm.dal.dataobject.contract;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import lombok.*;

import java.math.BigDecimal;

/**
 * 合同订单 DO
 *
 * @author Zhangyunfei
 */
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractOrderProductDO extends BaseDO {

    /**
     * 产品ID
     */
    private String productId;

    /**
     * 产品ID
     */
    private String materialId;
    /**
     * 平均价
     */
    private BigDecimal avgPrice;
    /**
     * 最低价
     */
    private BigDecimal maxPrice;
    /**
     * 最高价
     */
    private BigDecimal minPrice;
    /**
     * 最新价格
     */
    private BigDecimal latestPrice;
}