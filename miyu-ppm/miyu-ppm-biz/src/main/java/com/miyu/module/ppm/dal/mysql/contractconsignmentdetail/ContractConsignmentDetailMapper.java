package com.miyu.module.ppm.dal.mysql.contractconsignmentdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo.*;

/**
 * 外协发货单详情 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ContractConsignmentDetailMapper extends BaseMapperX<ContractConsignmentDetailDO> {

    default PageResult<ContractConsignmentDetailDO> selectPage(ContractConsignmentDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractConsignmentDetailDO>()
                .eqIfPresent(ContractConsignmentDetailDO::getConsignmentId, reqVO.getConsignmentId())
                .eqIfPresent(ContractConsignmentDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ContractConsignmentDetailDO::getInboundAmount, reqVO.getInboundAmount())
                .eqIfPresent(ContractConsignmentDetailDO::getInboundBy, reqVO.getInboundBy())
                .betweenIfPresent(ContractConsignmentDetailDO::getInboundTime, reqVO.getInboundTime())
                .eqIfPresent(ContractConsignmentDetailDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ContractConsignmentDetailDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ContractConsignmentDetailDO::getSignedTime, reqVO.getSignedTime())
                .betweenIfPresent(ContractConsignmentDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ContractConsignmentDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(ContractConsignmentDetailDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(ContractConsignmentDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .eqIfPresent(ContractConsignmentDetailDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(ContractConsignmentDetailDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(ContractConsignmentDetailDO::getProjectOrderId, reqVO.getProjectOrderId())
                .eqIfPresent(ContractConsignmentDetailDO::getProjectPlanId, reqVO.getProjectPlanId())
                .eqIfPresent(ContractConsignmentDetailDO::getOrderId, reqVO.getOrderId())
                .orderByDesc(ContractConsignmentDetailDO::getId));
    }


    default List<ContractConsignmentDetailDO> selectListByConsignmentId(String consignmentId) {
        return selectList(ContractConsignmentDetailDO::getConsignmentId, consignmentId);
    }

    default int deleteByConsignmentId(String consignmentId) {
        return delete(ContractConsignmentDetailDO::getConsignmentId, consignmentId);
    }


    default List<ContractConsignmentDetailDO> getContractConsignmentDetailsByContractId(String contractId){
        MPJLambdaWrapperX<ContractConsignmentDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ContractConsignmentDO.class,ContractConsignmentDO::getId,ContractConsignmentDetailDO::getConsignmentId)
                .eq(ContractConsignmentDO ::getContractId,contractId)
                .selectAll(ContractConsignmentDetailDO.class);

        return selectList(wrapperX);
    }
}