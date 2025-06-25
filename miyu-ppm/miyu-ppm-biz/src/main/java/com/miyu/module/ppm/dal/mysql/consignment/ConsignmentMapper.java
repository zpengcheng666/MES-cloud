package com.miyu.module.ppm.dal.mysql.consignment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentCheckDetailDO;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.ConsignmentInfoQueryReqVO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.*;
import org.apache.ibatis.annotations.Param;


/**
 * 采购收货 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConsignmentMapper extends BaseMapperX<ConsignmentDO> {

    default PageResult<ConsignmentDO> selectPage(PurchaseConsignmentPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ConsignmentDO>()
                .eqIfPresent(ConsignmentDO::getName, reqVO.getName())
                .eqIfPresent(ConsignmentDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ConsignmentDO::getNo, reqVO.getNo())
                .eqIfPresent(ConsignmentDO::getConsignedBy, reqVO.getConsignedBy())
                .betweenIfPresent(ConsignmentDO::getConsignedDate, reqVO.getConsignedDate())
                .eqIfPresent(ConsignmentDO::getConsignedContact, reqVO.getConsignedContact())
                .eqIfPresent(ConsignmentDO::getConsigner, reqVO.getConsigner())
                .eqIfPresent(ConsignmentDO::getConsignerContact, reqVO.getConsignerContact())
                .betweenIfPresent(ConsignmentDO::getConsignerDate, reqVO.getConsignerDate())
                .eqIfPresent(ConsignmentDO::getDeliveryMethod, reqVO.getDeliveryMethod())
                .eqIfPresent(ConsignmentDO::getDeliveryNumber, reqVO.getDeliveryNumber())
                .eqIfPresent(ConsignmentDO::getDeliveryBy, reqVO.getDeliveryBy())
                .eqIfPresent(ConsignmentDO::getDeliveryContact, reqVO.getDeliveryContact())
                .betweenIfPresent(ConsignmentDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ConsignmentDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(ConsignmentDO::getStatus, reqVO.getStatus())
                .inIfPresent(ConsignmentDO::getConsignmentType, reqVO.getConsignmentTypes())
                .inIfPresent(ConsignmentDO::getConsignmentStatus, reqVO.getConsignmentStatuss())
                .eqIfPresent(ConsignmentDO::getConsignmentType, reqVO.getConsignmentType())
                .orderByDesc(ConsignmentDO::getId));
    }

    List<ConsignmentCheckDetailDO> queryConsignmentCheckById(@Param("consignmentId") String id);

    /**
     * 查询收货单
     * @param id
     * @return
     */
    default List<ConsignmentDO> getConsignmentByContract(String id) {
        MPJLambdaWrapperX<ConsignmentDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.selectAll(ConsignmentDO.class).eq(ConsignmentDO::getContractId , id)
                .in(ConsignmentDO::getConsignmentStatus, ConsignmentStatusEnum.INBOUND.getStatus(),ConsignmentStatusEnum.FINISH.getStatus());
        return selectList(wrapperX);
    }

    default ConsignmentDO queryConsignmentByNo(String consignmentNo){
       return selectOne(ConsignmentDO::getNo, consignmentNo);
    }


    default ConsignmentDO getPurchaseConsignment(String id){
        MPJLambdaWrapperX<ConsignmentDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ContractDO.class,ContractDO::getId, ConsignmentDO::getContractId)
                .selectAs(ContractDO ::getName, ConsignmentDO::getContractName)
                .selectAs(ContractDO ::getNumber, ConsignmentDO::getContractNo)
                .selectAs(ContractDO ::getContractType, ConsignmentDO::getContractType)
                .selectAll(ConsignmentDO.class);
        return selectOne(wrapperX.eqIfPresent(ConsignmentDO::getId,id));
    }


    default List<ConsignmentDO> getConsignments(ConsignmentInfoQueryReqVO reqVO){
        MPJLambdaWrapperX<ConsignmentDO> wrapperX = new MPJLambdaWrapperX<>();
//        wrapperX.leftJoin(ConsignmentDO.class, ConsignmentDO::getId, ConsignmentInfoDO::getConsignmentId)
//                .leftJoin(ContractDO.class, ContractDO::getId, ConsignmentDO::getContractId)
//                .selectAs(ConsignmentDO::getNo, ConsignmentInfoDO::getNo)
//                .selectAs(ConsignmentDO::getName, ConsignmentInfoDO::getName)
//                .selectAs(ContractDO::getParty, ConsignmentInfoDO::getCompanyId)
//                .selectAll(ConsignmentInfoDO.class);
        wrapperX.eqIfPresent(ConsignmentDO::getConsignmentStatus, reqVO.getConsignmentStatus())
                .inIfPresent(ConsignmentDO::getConsignmentType, reqVO.getConsignmentType())
                .eqIfPresent(ConsignmentDO::getCreator,reqVO.getCreator())
                .betweenIfPresent(ConsignmentDO::getCreateTime, reqVO.getBeginTime(),reqVO.getEndTime());
        return selectList(wrapperX);
    }
}