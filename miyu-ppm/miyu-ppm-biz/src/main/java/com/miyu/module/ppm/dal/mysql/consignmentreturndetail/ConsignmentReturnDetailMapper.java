package com.miyu.module.ppm.dal.mysql.consignmentreturndetail;

import java.math.BigDecimal;
import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 销售退货单详情 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConsignmentReturnDetailMapper extends BaseMapperX<ConsignmentReturnDetailDO> {

    default PageResult<ConsignmentReturnDetailDO> selectPage(ConsignmentReturnDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConsignmentReturnDetailDO>()
                .eqIfPresent(ConsignmentReturnDetailDO::getConsignmentReturnId, reqVO.getConsignmentReturnId())
                .eqIfPresent(ConsignmentReturnDetailDO::getConsignedAmount, reqVO.getConsignedAmount())
                .eqIfPresent(ConsignmentReturnDetailDO::getInboundAmount, reqVO.getInboundAmount())
                .eqIfPresent(ConsignmentReturnDetailDO::getInboundBy, reqVO.getInboundBy())
                .betweenIfPresent(ConsignmentReturnDetailDO::getInboundTime, reqVO.getInboundTime())
                .eqIfPresent(ConsignmentReturnDetailDO::getSignedAmount, reqVO.getSignedAmount())
                .eqIfPresent(ConsignmentReturnDetailDO::getSignedBy, reqVO.getSignedBy())
                .betweenIfPresent(ConsignmentReturnDetailDO::getSignedTime, reqVO.getSignedTime())
                .betweenIfPresent(ConsignmentReturnDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ConsignmentReturnDetailDO::getMaterialStockId, reqVO.getMaterialStockId())
                .eqIfPresent(ConsignmentReturnDetailDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(ConsignmentReturnDetailDO::getBatchNumber, reqVO.getBatchNumber())
                .orderByDesc(ConsignmentReturnDetailDO::getId));
    }


    @Select("select count(b.consigned_amount) from ppm_consignment_return as a left join ppm_consignment_return_detail as b on a.id = b.consignment_return_id where contract_id = #{contractId}")
    BigDecimal queryConsignmentReturnAmount(@Param("contractId") String contractId);

    @Delete("delete from  ppm_consignment_return_detail  where  consignment_return_id = #{consignmentReturnId}")
    void deleteDetailByConsignmentReturnId(@Param("consignmentReturnId") String consignmentReturnId);


    default List<ConsignmentReturnDetailDO> queryDetailById(String consignmentReturnId){
        MPJLambdaWrapperX<ConsignmentReturnDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getNo,ConsignmentReturnDetailDO::getNo)
                .selectAs(ConsignmentDO::getName,ConsignmentReturnDetailDO::getName)
                .eq(ConsignmentReturnDetailDO::getConsignmentReturnId,consignmentReturnId)
                .selectAll(ConsignmentReturnDetailDO.class);
        return selectList(wrapperX);
    }


    default List<ConsignmentReturnDetailDO> getConsignmentReturnDetails(List<String> ids , String contractId){
        MPJLambdaWrapperX<ConsignmentReturnDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ContractOrderDO.class,ContractOrderDO::getMaterialId,ConsignmentReturnDetailDO::getMaterialConfigId)
                .selectAs(ContractOrderDO::getTaxPrice,ConsignmentReturnDetailDO::getTaxPrice)
                .eq(ContractOrderDO::getContractId,contractId)
                .in(ConsignmentReturnDetailDO::getConsignmentReturnId,ids)
                .selectAll(ConsignmentReturnDetailDO.class);
        return selectList(wrapperX);
    }

    default List<ConsignmentReturnDetailDO> getConsignmentReturnDetailsByConsignmentReturnId(String consignmentReturnId , String contractId){
        MPJLambdaWrapperX<ConsignmentReturnDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ContractOrderDO.class,ContractOrderDO::getMaterialId,ConsignmentReturnDetailDO::getMaterialConfigId)
                .selectAs(ContractOrderDO::getTaxPrice,ConsignmentReturnDetailDO::getTaxPrice)
                .eq(ContractOrderDO::getContractId,contractId)
                .eq(ConsignmentReturnDetailDO::getConsignmentReturnId,consignmentReturnId)
                .selectAll(ConsignmentReturnDetailDO.class);
        return selectList(wrapperX);
    }


    /**
     * 查询退货单明细对应的入库详情
     */
    default List<ConsignmentReturnDetailDO> getConsignmentReturnDetailsByReturnId(String consignmentReturnId){
        MPJLambdaWrapperX<ConsignmentReturnDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ConsignmentDO.class, ConsignmentDO::getId,ConsignmentReturnDetailDO::getConsignmentReturnId)
                .leftJoin(WarehouseDetailDO.class, on -> on.eq(WarehouseDetailDO::getOrderNumber, ConsignmentDO::getNo).eq(WarehouseDetailDO::getMaterialConfigId, ConsignmentReturnDetailDO::getMaterialConfigId))
                .eq(ConsignmentReturnDetailDO::getConsignmentReturnId,consignmentReturnId)
                .selectAs(WarehouseDetailDO::getMaterialName, ConsignmentReturnDetailDO::getMaterialName)
                .selectAs(WarehouseDetailDO::getMaterialNumber, ConsignmentReturnDetailDO::getMaterialNumber)
                .selectAs(WarehouseDetailDO::getMaterialUnit, ConsignmentReturnDetailDO::getMaterialUnit)
                .selectAs(WarehouseDetailDO::getQuantity, ConsignmentReturnDetailDO::getQuantity)
                .selectAs(WarehouseDetailDO::getConsignedAmount, ConsignmentReturnDetailDO::getConsignedAmount)
                .selectAll(WarehouseDetailDO.class);
        return selectList(wrapper);
    }

}