package com.miyu.module.ppm.dal.mysql.contractrefund;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contractrefund.vo.*;

/**
 * 合同退款 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ContractRefundMapper extends BaseMapperX<ContractRefundDO> {

    default PageResult<ContractRefundDO> selectPage(ContractRefundPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractRefundDO>()
                .eqIfPresent(ContractRefundDO::getShippingReturnId, reqVO.getShippingReturnId())
                .eqIfPresent(ContractRefundDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ContractRefundDO::getRefundType, reqVO.getRefundType())
                .eqIfPresent(ContractRefundDO::getRefundStatus, reqVO.getRefundStatus())
                .likeIfPresent(ContractRefundDO::getNo, reqVO.getNo())
                .betweenIfPresent(ContractRefundDO::getRefundTime, reqVO.getRetundTime())
                .eqIfPresent(ContractRefundDO::getRefundPrice, reqVO.getRefundPrice())
                .betweenIfPresent(ContractRefundDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ContractRefundDO::getId));
    }


    default List<ContractRefundDO> getContractRefundByShippingReturn(List<String> shippingReturnIds) {

        MPJLambdaWrapperX<ContractRefundDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ContractRefundDO::getShippingReturnId, shippingReturnIds);
        return selectList(wrapperX);
    }

    default List<ContractRefundDO> selectListByContract(String contractId, String id) {

        MPJLambdaWrapperX<ContractRefundDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(ContractRefundDO::getContractId, contractId)
                .neIfPresent(ContractRefundDO::getId, id);
        return selectList(wrapperX);
    }

    default  List<ContractRefundDO> getContractRefundByContractId(String contractId){
        MPJLambdaWrapperX<ContractRefundDO> wrapperX = new MPJLambdaWrapperX<>();
        return selectList(wrapperX.eqIfPresent(ContractRefundDO::getContractId,contractId));
    }


}