package com.miyu.module.ppm.dal.mysql.contractinvoice;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * 购销合同发票表详细 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractInvoiceDetailMapper extends BaseMapperX<ContractInvoiceDetailDO> {

    default List<ContractInvoiceDetailDO> selectListByInvoiceId(String invoiceId) {
        MPJLambdaWrapperX<ContractInvoiceDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractPaymentDO.class, ContractPaymentDO::getId, ContractInvoiceDetailDO::getPaymentId)
                .selectAs(ContractPaymentDO::getPayDate, ContractInvoiceDetailDO::getPayDate)
                .selectAs(ContractPaymentDO::getAmount, ContractInvoiceDetailDO::getPayAmount)
                .selectAs(ContractPaymentDO::getMethod, ContractInvoiceDetailDO::getMethod)
                .select("(select t1.amount - IFNULL(sum(t2.amount), 0) from pd_contract_invoice_detail t2 where t2.payment_id = t.payment_id and t2.deleted = 0 and t2.id != t.id )  as remainAmount ")
                .selectAll(ContractInvoiceDetailDO.class);
        return selectList(wrapper.eq(ContractInvoiceDetailDO::getInvoiceId, invoiceId));
    }

    default int deleteByInvoiceId(String invoiceId) {
        return delete(ContractInvoiceDetailDO::getInvoiceId, invoiceId);
    }


    /**
     * 付款计划主键获取付款详情已支付金额
     * @param ids
     */
    default List<ContractInvoiceDetailDO> selectInvoiceAmountListBySchemeId(Collection<String> ids, String invoiceId) {
        MPJLambdaWrapperX<ContractInvoiceDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.select(ContractInvoiceDetailDO::getPaymentId).selectSum(ContractInvoiceDetailDO::getAmount);
        wrapper.in(ContractInvoiceDetailDO::getPaymentId, ids)
                .ne(invoiceId != null, ContractInvoiceDetailDO::getInvoiceId, invoiceId)
                .groupBy(ContractInvoiceDetailDO::getPaymentId);
        return selectList(wrapper);
    }

}