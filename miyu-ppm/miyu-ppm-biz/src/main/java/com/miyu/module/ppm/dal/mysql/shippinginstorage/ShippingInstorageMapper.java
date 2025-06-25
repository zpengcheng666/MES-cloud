package com.miyu.module.ppm.dal.mysql.shippinginstorage;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.ppm.dal.dataobject.shippinginstorage.ShippingInstorageDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shippinginstorage.vo.*;

/**
 * 销售订单入库 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ShippingInstorageMapper extends BaseMapperX<ShippingInstorageDO> {

    default PageResult<ShippingInstorageDO> selectPage(ShippingInstoragePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingInstorageDO>()
                .likeIfPresent(ShippingInstorageDO::getName, reqVO.getName())
                .eqIfPresent(ShippingInstorageDO::getNo, reqVO.getNo())
                .eqIfPresent(ShippingInstorageDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ShippingInstorageDO::getCompanyId, reqVO.getCompanyId())
                .eqIfPresent(ShippingInstorageDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ShippingInstorageDO::getConsigner, reqVO.getConsigner())
                .betweenIfPresent(ShippingInstorageDO::getConsignerDate, reqVO.getConsignerDate())
                .eqIfPresent(ShippingInstorageDO::getDeliveryMethod, reqVO.getDeliveryMethod())
                .eqIfPresent(ShippingInstorageDO::getDeliveryBy, reqVO.getDeliveryBy())
                .eqIfPresent(ShippingInstorageDO::getDeliveryNumber, reqVO.getDeliveryNumber())
                .eqIfPresent(ShippingInstorageDO::getDeliveryContact, reqVO.getDeliveryContact())
                .eqIfPresent(ShippingInstorageDO::getConsignedBy, reqVO.getConsignedBy())
                .betweenIfPresent(ShippingInstorageDO::getConsignedDate, reqVO.getConsignedDate())
                .eqIfPresent(ShippingInstorageDO::getConsignedContact, reqVO.getConsignedContact())
                .betweenIfPresent(ShippingInstorageDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ShippingInstorageDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ShippingInstorageDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(ShippingInstorageDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(ShippingInstorageDO::getShippingInstorageStatus, reqVO.getShippingInstorageStatus())
                .orderByDesc(ShippingInstorageDO::getId));
    }

}