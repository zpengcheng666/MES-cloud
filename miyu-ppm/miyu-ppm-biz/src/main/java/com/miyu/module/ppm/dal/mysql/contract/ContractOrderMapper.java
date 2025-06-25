package com.miyu.module.ppm.dal.mysql.contract;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractPageReqVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderProductDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.enums.common.ContractAuditStatusEnum;
import com.miyu.module.ppm.enums.common.ContractStatusEnum;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 合同订单 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractOrderMapper extends BaseMapperX<ContractOrderDO> {

    default List<ContractOrderDO> selectListByContractId(String contractId) {
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.leftJoin(ContractDO.class, ContractDO::getId, ContractOrderDO::getContractId)
                .leftJoin(CompanyProductDO.class, on -> on.eq(CompanyProductDO::getId, ContractDO::getParty).eq(CompanyProductDO::getMaterialId, ContractOrderDO::getMaterialId))
                .leftJoin(PurchaseRequirementDetailDO.class, PurchaseRequirementDetailDO::getId, ContractOrderDO::getRequirementDetailId)
                .selectAs(CompanyProductDO::getMaterialId, ContractOrderDO::getMaterialId)
                .selectAs(CompanyProductDO::getInitTax, ContractOrderDO::getInitTax)
                .selectAs(PurchaseRequirementDetailDO::getNumber, ContractOrderDO::getRequirementDetailNumber)
                .selectAll(ContractOrderDO.class)
                .eq(ContractOrderDO::getContractId, contractId);
        return selectList(query);
    }

    default int deleteByContractId(String contractId) {
        return delete(ContractOrderDO::getContractId, contractId);
    }


    default Map<String, ContractOrderProductDO> selectListByProductId(Collection<String> productIds){
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.leftJoin(ContractDO.class, ContractDO::getId, ContractOrderDO::getContractId).in(ContractDO::getStatus, ContractAuditStatusEnum.DRAFT.getStatus(), // 草稿 + 审批中 + 审批通过
                ContractAuditStatusEnum.PROCESS, ContractAuditStatusEnum.APPROVE.getStatus()).in("material_id", productIds);
        // SQL sum 查询
        List<Map<String, Object>> result = selectMaps(query
                .select("material_id, MAX(price) as maxPrice, MIN(price) as minPrice, AVG(price) as avgPrice")
                .select("(select t3.price from pd_contract_order t3 where t3.material_id = t.material_id order by t3.create_time desc limit 1)  as latestPrice ")
                .groupBy("material_id")
                );

        // 获得数量
        return convertMap(result, obj -> (String) obj.get("material_id"), obj -> BeanUtils.toBean(obj, ContractOrderProductDO.class));
    }


    default List<ContractOrderDO> getContractOrderListByProjectIds(Collection<String> productIds,Integer type){
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.leftJoin(ContractDO.class, ContractDO::getId, ContractOrderDO::getContractId)
                .eq(ContractDO::getType,type)
                .selectAll(ContractOrderDO.class);
        return selectList(query.inIfPresent(ContractOrderDO::getProjectId, productIds));
    }

    /**
     * 合同Id获取合同信息
     * @param id
     * @return
     */
    default List<ContractOrderRespDTO> getContractOrderByContractId(String id){
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.eq(ContractOrderDO::getContractId, id).selectAll(ContractOrderDO.class);
        return BeanUtils.toBean(selectList(query), ContractOrderRespDTO.class);
    }

    /**
     * 退货单Id获取合同信息
     */
    default List<ContractOrderRespDTO> getContractOrderByReturnId(String consignmentReturnId){
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.leftJoin(ConsignmentReturnDO.class, ConsignmentReturnDO::getContractId, ContractOrderDO::getContractId)
                .eq(ConsignmentReturnDO::getId ,consignmentReturnId)
                .selectAll(ContractOrderDO.class);
        return BeanUtils.toBean(selectList(query), ContractOrderRespDTO.class);
    }

    /**
     * 合同订单Id集合获取产品信息
     */
    default List<ContractOrderRespDTO> queryContractOrderByIds(List<String> ids){
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.in(ContractOrderDO::getId,ids).selectAll(ContractOrderDO.class);
        return BeanUtils.toBean(selectList(query),ContractOrderRespDTO.class);
    }

    /**
     * 采购申请明细主键获取合同订单，且合同审批状态非驳回和取消，合同状态非已作废和已终止
     * @param requirementDetailIds
     * @return
     */
    default List<ContractOrderDO> selectListByRequirementId(List<String> requirementDetailIds) {
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.leftJoin(ContractDO.class, ContractDO::getId, ContractOrderDO::getContractId)
                .notIn(ContractDO::getContractStatus, Arrays.asList(ContractStatusEnum.TERMINATE.getStatus(),ContractStatusEnum.INVALID.getStatus()))
                .notIn(ContractDO::getStatus, Arrays.asList(ContractAuditStatusEnum.REJECT.getStatus(),ContractAuditStatusEnum.CANCEL.getStatus()))
                .selectAll(ContractOrderDO.class);
        return selectList(query.in(ContractOrderDO::getRequirementDetailId, requirementDetailIds));
    }

    /**
     * 采购申请详情ID获取采购合同集合
     * @param requirementDetailId
     * @return
     */
    default List<ContractOrderDO> selectListByRequirementDetailId(String requirementDetailId) {
        MPJLambdaWrapperX<ContractOrderDO> query = new MPJLambdaWrapperX<>();
        query.leftJoin(ContractDO.class, ContractDO::getId, ContractOrderDO::getContractId)
                .leftJoin(CompanyDO.class, CompanyDO::getId, ContractDO::getParty)
                .selectAs(ContractDO::getNumber, ContractOrderDO::getNumber)
                .selectAs(ContractDO::getName, ContractOrderDO::getName)
                .selectAs(ContractDO::getContractType, ContractOrderDO::getContractType)
                .selectAs(CompanyDO::getName, ContractOrderDO::getPartyName)
                .selectAs(ContractDO::getStatus, ContractOrderDO::getStatus)
                .select(ContractOrderDO::getQuantity)
                .notIn(ContractDO::getContractStatus, Arrays.asList(ContractStatusEnum.TERMINATE.getStatus(),ContractStatusEnum.INVALID.getStatus()));
        return selectList(query.eq(ContractOrderDO::getRequirementDetailId, requirementDetailId));
    }



    default List<ContractOrderDO> getContractOrderPage(ContractPageReqVO reqVO) {

        MPJLambdaWrapperX<ContractOrderDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, ContractOrderDO::getContractId)
                .selectAs(ContractDO::getName, ContractOrderDO::getName)
                .selectAs(ContractDO::getNumber, ContractOrderDO::getNumber)
                .selectAll(ContractOrderDO.class)
                .eq( ContractDO::getContractStatus, reqVO.getContractStatus())
                .eq(ContractDO::getType, reqVO.getType());
        wrapper.orderByAsc(ContractOrderDO::getCreateTime);
        return selectList(wrapper);
    }
}
