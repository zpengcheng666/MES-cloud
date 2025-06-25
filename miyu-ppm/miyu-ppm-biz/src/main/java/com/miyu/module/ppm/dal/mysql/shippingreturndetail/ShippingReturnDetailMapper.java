package com.miyu.module.ppm.dal.mysql.shippingreturndetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shippingreturndetail.vo.*;

/**
 * 销售退货单详情 Mapper
 *
 * @author miyudmA
 */
@Mapper
public interface ShippingReturnDetailMapper extends BaseMapperX<ShippingReturnDetailDO> {

    default PageResult<ShippingReturnDetailDO> selectPage(ShippingReturnDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingReturnDetailDO>()
                .eqIfPresent(ShippingReturnDetailDO::getShippingReturnId, reqVO.getShippingReturnId())
                .eqIfPresent(ShippingReturnDetailDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ShippingReturnDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ShippingReturnDetailDO::getInboundAmount, reqVO.getInboundAmount())
                .eqIfPresent(ShippingReturnDetailDO::getInboundBy, reqVO.getInboundBy())
                .betweenIfPresent(ShippingReturnDetailDO::getInboundTime, reqVO.getInboundTime())
                .eqIfPresent(ShippingReturnDetailDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ShippingReturnDetailDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ShippingReturnDetailDO::getSignedTime, reqVO.getSignedTime())
                .betweenIfPresent(ShippingReturnDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ShippingReturnDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(ShippingReturnDetailDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(ShippingReturnDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .orderByDesc(ShippingReturnDetailDO::getId));
    }


    default List<ShippingReturnDetailDO> selectListByShippingReturnId(String shippingReturnId) {


        MPJLambdaWrapperX<ShippingReturnDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingDetailDO.class, ShippingDetailDO::getId, ShippingReturnDetailDO::getShippingDetailId)
                .leftJoin(ShippingDO.class, ShippingDO::getId, ShippingDetailDO::getShippingId)
                .eq(ShippingReturnDetailDO::getShippingReturnId, shippingReturnId)
                .selectAs(ShippingDO::getNo, ShippingReturnDetailDO::getShippingNo)
                .selectAs(ShippingDO::getName, ShippingReturnDetailDO::getShippingName)
                .selectAll(ShippingReturnDetailDO.class);


        return selectList(wrapperX);
    }
    default int deleteByShippingReturnId(String shippingReturnId) {
        return delete(ShippingReturnDetailDO::getShippingReturnId, shippingReturnId);
    }


    @Delete("delete from  dm_shipping_return_detail  where  shipping_return_id = #{shippingReturnId}")
    void deleteAllByShippingReturnId(String shippingReturnId);

    default List<ShippingReturnDetailDO> getShippingReturnDetails(String projectId, List<Integer> shippingReturnStatus) {

        MPJLambdaWrapperX<ShippingReturnDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingReturnDO.class, ShippingReturnDO::getId, ShippingReturnDetailDO::getShippingReturnId)
                .in(ShippingReturnDO::getShippingStatus, shippingReturnStatus)
                .eq(ShippingReturnDO::getProjectId, projectId)
                .selectAll(ShippingReturnDetailDO.class);

        return selectList(wrapperX);
    }

    default List<ShippingReturnDetailDO> selectListByBarCode(String barcode) {

        MPJLambdaWrapperX<ShippingReturnDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingReturnDO.class, ShippingReturnDO::getId, ShippingReturnDetailDO::getShippingReturnId)
                .leftJoin(ShippingDetailDO.class, ShippingDetailDO::getId, ShippingReturnDetailDO::getShippingDetailId)
                .leftJoin(ShippingDO.class, ShippingDO::getId, ShippingDetailDO::getShippingId)
                .selectAs(ShippingDO ::getNo,ShippingReturnDetailDO ::getShippingNo)
                .selectAs(ShippingDO ::getName,ShippingReturnDetailDO ::getShippingName)
                .selectAs(ShippingReturnDO ::getShippingReturnNo,ShippingReturnDetailDO ::getShippingReturnNo)
                .selectAs(ShippingReturnDO ::getShippingReturnName,ShippingReturnDetailDO ::getShippingReturnName)
                .selectAs(ShippingReturnDO ::getContractId,ShippingReturnDetailDO ::getContractId)
                .selectAs(ShippingReturnDO ::getReturnType,ShippingReturnDetailDO ::getReturnType)
                .selectAs(ShippingReturnDO ::getReturnReason,ShippingReturnDetailDO ::getReturnReason)
                .eq(ShippingReturnDetailDO::getBarCode, barcode)
                .selectAll(ShippingReturnDetailDO.class);

        return selectList(wrapperX);
    }




}