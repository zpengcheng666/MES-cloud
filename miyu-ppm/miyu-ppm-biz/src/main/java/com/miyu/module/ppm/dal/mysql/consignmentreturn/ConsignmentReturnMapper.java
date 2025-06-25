package com.miyu.module.ppm.dal.mysql.consignmentreturn;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.*;

/**
 * 销售退货单 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ConsignmentReturnMapper extends BaseMapperX<ConsignmentReturnDO> {

    default PageResult<ConsignmentReturnDO> selectPage(ConsignmentReturnPageReqVO reqVO) {

        MPJLambdaWrapperX<ConsignmentReturnDO> wrapperX = new MPJLambdaWrapperX<>();
         wrapperX.leftJoin(ContractDO.class,ContractDO::getId,ConsignmentReturnDO::getContractId)
                 .leftJoin(CompanyDO.class, CompanyDO::getId, ContractDO::getParty)
                 .selectAs(ContractDO::getNumber,ConsignmentReturnDO::getContractNum)
                 .selectAs(ContractDO::getName,ConsignmentReturnDO::getContractName)
                 .selectAs(CompanyDO::getName,ConsignmentReturnDO::getPartyName)
                 .selectAll(ConsignmentReturnDO.class);

        return selectPage(reqVO, wrapperX
                .eqIfPresent(ConsignmentReturnDO::getConsignmentReturnNo, reqVO.getConsignmentReturnNo())
                .likeIfPresent(ConsignmentReturnDO::getConsignmentReturnName, reqVO.getConsignmentReturnName())
                .eqIfPresent(ConsignmentReturnDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ConsignmentReturnDO::getConsigner, reqVO.getConsigner())
                .betweenIfPresent(ConsignmentReturnDO::getConsignerDate, reqVO.getConsignerDate())
                .eqIfPresent(ConsignmentReturnDO::getConsignedBy, reqVO.getConsignedBy())
                .betweenIfPresent(ConsignmentReturnDO::getConsignedDate, reqVO.getConsignedDate())
                .eqIfPresent(ConsignmentReturnDO::getConsignedContact, reqVO.getConsignedContact())
                .eqIfPresent(ConsignmentReturnDO::getReturnType, reqVO.getReturnType())
                .eqIfPresent(ConsignmentReturnDO::getReturnReason, reqVO.getReturnReason())
                .eqIfPresent(ConsignmentReturnDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .eqIfPresent(ConsignmentReturnDO::getStatus, reqVO.getStatus())
                .eqIfPresent(ConsignmentReturnDO::getConsignmentStatus, reqVO.getConsignmentStatus())
                .eqIfPresent(ConsignmentReturnDO::getFileUrl, reqVO.getFileUrl())
                .eqIfPresent(ConsignmentReturnDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ConsignmentReturnDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ConsignmentReturnDO::getId));
    }

    /**
     * 获取退款合同下的退货单
     * @param contractId
     * @param statusList
     * @return
     */
    default List<ConsignmentReturnDO> getConsignmentReturnByContract(String contractId, List<Integer> statusList){
        MPJLambdaWrapperX<ConsignmentReturnDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ConsignmentReturnDO::getConsignmentStatus, statusList)
                .eq(ConsignmentReturnDO::getContractId, contractId);
        return selectList(wrapperX);
    }


    default List<ConsignmentReturnDO> getConsignmentReturnListByContractId(String id) {
        MPJLambdaWrapperX<ConsignmentReturnDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ContractDO.class,ContractDO::getId,ConsignmentReturnDO::getContractId)
                .leftJoin(CompanyDO.class, CompanyDO::getId, ContractDO::getParty)
                .selectAs(ContractDO::getNumber,ConsignmentReturnDO::getContractNum)
                .selectAs(ContractDO::getName,ConsignmentReturnDO::getContractName)
                .selectAs(CompanyDO::getName,ConsignmentReturnDO::getPartyName)
                .selectAll(ConsignmentReturnDO.class);
        return selectList(ConsignmentReturnDO::getContractId, id);
    }

}