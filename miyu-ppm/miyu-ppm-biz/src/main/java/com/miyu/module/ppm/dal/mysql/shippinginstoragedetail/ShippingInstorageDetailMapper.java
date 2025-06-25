package com.miyu.module.ppm.dal.mysql.shippinginstoragedetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstorage.ShippingInstorageDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo.*;

/**
 * 销售订单入库明细 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ShippingInstorageDetailMapper extends BaseMapperX<ShippingInstorageDetailDO> {

    default PageResult<ShippingInstorageDetailDO> selectPage(ShippingInstorageDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingInstorageDetailDO>()
                .eqIfPresent(ShippingInstorageDetailDO::getShippingStorageId, reqVO.getShippingStorageId())
                .eqIfPresent(ShippingInstorageDetailDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ShippingInstorageDetailDO::getOrderId, reqVO.getOrderId())
                .betweenIfPresent(ShippingInstorageDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ShippingInstorageDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ShippingInstorageDetailDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ShippingInstorageDetailDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ShippingInstorageDetailDO::getSignedTime, reqVO.getSignedTime())
                .orderByDesc(ShippingInstorageDetailDO::getId));
    }


    default List<ShippingInstorageDetailDO> selectListByShippingStorageId(String shippingStorageId) {
        return selectList(ShippingInstorageDetailDO::getShippingStorageId, shippingStorageId);
    }

    default List<ShippingInstorageDetailDO> selectListByProjectId(String projectId) {
        return selectList(ShippingInstorageDetailDO::getProjectId, projectId);
    }

    default int deleteByShippingStorageId(String shippingStorageId) {
        return delete(ShippingInstorageDetailDO::getShippingStorageId, shippingStorageId);
    }

    default List<ShippingInstorageDetailDO> getDetailsByProjectId(String projectId){
        MPJLambdaWrapperX<ShippingInstorageDetailDO> wrapperX = new MPJLambdaWrapperX<>();

        wrapperX.leftJoin(ShippingInstorageDO.class, ShippingInstorageDO::getId, ShippingInstorageDetailDO::getShippingStorageId)
                .eq(ShippingInstorageDO::getProjectId, projectId)
                .selectAll(ShippingInstorageDetailDO.class);
        return selectList(wrapperX);
    }

}