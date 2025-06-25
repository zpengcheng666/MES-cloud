package cn.iocoder.yudao.module.pms.dal.mysql.planPurchaseOrder;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder.PlanPurchaseOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder.PlanPurchaseOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目计划 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PlanPurchaseOrderItemMapper extends BaseMapperX<PlanPurchaseOrderItemDO> {

}
