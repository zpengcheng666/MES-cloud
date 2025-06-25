package com.miyu.module.ppm.dal.mysql.shippingdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.*;
import org.springframework.util.CollectionUtils;

/**
 * 销售发货明细 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ShippingDetailMapper extends BaseMapperX<ShippingDetailDO> {

    default PageResult<ShippingDetailDO> selectPage(ShippingDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingDetailDO>()
                .eqIfPresent(ShippingDetailDO::getShippingId, reqVO.getShippingId())
                .eqIfPresent(ShippingDetailDO::getOrderId, reqVO.getOrderId())
                .eqIfPresent(ShippingDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
//                .eqIfPresent(ShippingDetailDO::getSignedAmount, reqVO.getSignedAmount())
//                .eqIfPresent(ShippingDetailDO::getSignedBy, reqVO.getSignedBy())
//                .betweenIfPresent(ShippingDetailDO::getSignedTime, reqVO.getSignedTime())
                .orderByDesc(ShippingDetailDO::getId));
    }



    default List<ShippingDetailDO> selectListByShippingId(String shippingId) {
        return selectList(ShippingDetailDO::getShippingId, shippingId);
    }

    default int deleteByShippingId(String shippingId) {
        return delete(ShippingDetailDO::getShippingId, shippingId);
    }


    @Delete("delete from  dm_shipping_detail  where  shipping_id = #{shippingId}")
    void deleteAllByShippingId(String shippingId);


    default List<ShippingDetailDO> getOutboundOderByContractId(String contractId,List<String> detailIds,List<Integer> status) {

        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingDO.class,ShippingDO::getId,ShippingDetailDO::getShippingId)
                .eq(ShippingDO ::getContractId,contractId)
                .in(ShippingDO ::getShippingStatus, status)
                .selectAs(ShippingDO ::getNo,ShippingDetailDO ::getNo)
                .selectAs(ShippingDO ::getName,ShippingDetailDO ::getName)
                .selectAs(ShippingDO ::getShippingStatus,ShippingDetailDO ::getShippingStatus)
                .selectAll(ShippingDetailDO.class);

        if (!CollectionUtils.isEmpty(detailIds)){
            wrapperX.notIn(ShippingDetailDO::getId,detailIds);
        }
        return selectList(wrapperX);
    }

    default  List<ShippingDetailDO> selectListByBarCode(String barCode){
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingDO.class,ShippingDO::getId,ShippingDetailDO::getShippingId)
                .selectAs(ShippingDO ::getNo,ShippingDetailDO ::getNo)
                .selectAs(ShippingDO ::getName,ShippingDetailDO ::getName)
                .selectAs(ShippingDO ::getShippingStatus,ShippingDetailDO ::getShippingStatus)
                .selectAll(ShippingDetailDO.class);
        wrapperX.eqIfPresent(ShippingDetailDO ::getBarCode, barCode);
        return selectList(wrapperX);
    }


    default  List<ShippingDetailDO> selectShippingByBarCodes(Collection<String> barCodes){
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingDO.class,ShippingDO::getId,ShippingDetailDO::getShippingId)
                .selectAs(ShippingDO ::getNo,ShippingDetailDO ::getNo)
                .selectAs(ShippingDO ::getName,ShippingDetailDO ::getName)
                .selectAll(ShippingDetailDO.class);
        wrapperX.inIfPresent(ShippingDetailDO ::getBarCode, barCodes);
        wrapperX.inIfPresent(ShippingDetailDO ::getShippingStatus, Lists.newArrayList(0,1,2,3,4))
                .inIfPresent(ShippingDetailDO::getShippingType,Lists.newArrayList(3,4));
        return selectList(wrapperX);
    }

}