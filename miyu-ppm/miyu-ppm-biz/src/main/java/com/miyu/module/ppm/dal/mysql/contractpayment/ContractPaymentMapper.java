package com.miyu.module.ppm.dal.mysql.contractpayment;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.enums.common.InvoiceAuditStatusEnum;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contractpayment.vo.*;

/**
 * 合同付款 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractPaymentMapper extends BaseMapperX<ContractPaymentDO> {

    default PageResult<ContractPaymentDO> selectPage(ContractPaymentPageReqVO reqVO) {
        MPJLambdaWrapperX<ContractPaymentDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, ContractPaymentDO::getContractId)
                .like(reqVO.getContractNumber() != null, ContractDO::getNumber, reqVO.getContractNumber())
                .selectAs(ContractDO::getNumber, ContractPaymentDO::getContractNumber)
                .selectAll(ContractPaymentDO.class);

        return selectPage(reqVO, wrapper
                .eqIfPresent(ContractPaymentDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ContractPaymentDO::getBusinessType, reqVO.getBusinessType())
                .betweenIfPresent(ContractPaymentDO::getPayDate, reqVO.getPayDate())
                .eqIfPresent(ContractPaymentDO::getAmount, reqVO.getAmount())
                .eqIfPresent(ContractPaymentDO::getMethod, reqVO.getMethod())
                .eqIfPresent(ContractPaymentDO::getEvidence, reqVO.getEvidence())
                .eqIfPresent(ContractPaymentDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ContractPaymentDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ContractPaymentDO::getId));
    }


    default ContractPaymentDO getContractPaymentById(String id) {

        MPJLambdaWrapperX<ContractPaymentDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, ContractPaymentDO::getContractId)
                .selectAs(ContractDO::getName, ContractPaymentDO::getContractName)
                .selectAll(ContractPaymentDO.class);
        return selectOne(wrapper.eq(ContractPaymentDO::getId, id));
    }


    /**
     * 合同主键获取合同支付单据
     * @param reqVO
     * @return
     */
    default List<ContractPaymentDO> selectList4InvoiceByPaymentId(ContractPaymentReqVO reqVO) {
        MPJLambdaWrapperX<ContractPaymentDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.select(ContractPaymentDO::getId)
                .selectAs(ContractPaymentDO::getAmount, ContractPaymentDO::getPayAmount)
                .select(ContractPaymentDO::getPayDate)
                .select(ContractPaymentDO::getMethod)
                .select(ContractPaymentDO::getNumber)
//                .select("(select IFNULL(sum(t1.amount), 0) from pd_contract_payment_detail t1 where t1.scheme_id = t.id and t1.deleted = 0)  as payAmountSum ")
                .select("(select t.amount - IFNULL(sum(t1.amount), 0) from pd_contract_invoice_detail t1 where t1.payment_id = t.id and t1.deleted = 0 and if("+ reqVO.getInvoiceId()+" is not null, t1.id != "+reqVO.getInvoiceId()+", 1 = 1))  as remainAmount ")
                .eq(ContractPaymentDO::getContractId, reqVO.getContractId())
                .eq(ContractPaymentDO::getStatus, InvoiceAuditStatusEnum.APPROVE.getStatus());
        return selectList(wrapper);
    }

    default List<ContractPaymentDO> selectListByIds(Collection<String> ids) {
        return selectList(ContractPaymentDO::getId, ids);
    }
}