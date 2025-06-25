package com.miyu.module.ppm.dal.mysql.shippingreturn;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.shippingreturn.vo.*;

/**
 * 销售退货单 Mapper
 *
 * @author miyudmA
 */
@Mapper
public interface ShippingReturnMapper extends BaseMapperX<ShippingReturnDO> {

    default PageResult<ShippingReturnDO> selectPage(ShippingReturnPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ShippingReturnDO>()
//                .eqIfPresent(ShippingReturnDO::getShippingId, reqVO.getShippingId())
                .eqIfPresent(ShippingReturnDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ShippingReturnDO::getConsigner, reqVO.getConsigner())
                .betweenIfPresent(ShippingReturnDO::getConsignerDate, reqVO.getConsignerDate())
                .eqIfPresent(ShippingReturnDO::getConsignedBy, reqVO.getConsignedBy())
                .betweenIfPresent(ShippingReturnDO::getConsignedDate, reqVO.getConsignedDate())
                .eqIfPresent(ShippingReturnDO::getConsignedContact, reqVO.getConsignedContact())
                .eqIfPresent(ShippingReturnDO::getReturnType, reqVO.getReturnType())
                .eqIfPresent(ShippingReturnDO::getReturnReason, reqVO.getReturnReason())
                .eqIfPresent(ShippingReturnDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(ShippingReturnDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ShippingReturnDO::getShippingStatus, reqVO.getShippingStatus())
                .eqIfPresent(ShippingReturnDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(ShippingReturnDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ShippingReturnDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ShippingReturnDO::getId));
    }

    default  List<ShippingReturnDO> getShippingReturnByContract(String contractId,List<Integer> status){

        MPJLambdaWrapperX<ShippingReturnDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ShippingReturnDO ::getShippingStatus, status)
                .eq(ShippingReturnDO::getContractId, contractId);

        return selectList(wrapperX);
    }

}