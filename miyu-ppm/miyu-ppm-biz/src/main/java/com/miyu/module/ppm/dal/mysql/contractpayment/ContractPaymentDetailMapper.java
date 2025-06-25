package com.miyu.module.ppm.dal.mysql.contractpayment;


import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.Collection;
import java.util.List;

/**
 * 合同付款详细 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractPaymentDetailMapper extends BaseMapperX<ContractPaymentDetailDO> {

    default List<ContractPaymentDetailDO> selectListByPaymentId(String paymentId) {
        MPJLambdaWrapperX<ContractPaymentDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractPaymentSchemeDO.class, ContractPaymentSchemeDO::getId, ContractPaymentDetailDO::getSchemeId)
                .selectAs(ContractPaymentSchemeDO::getPaymentControl, ContractPaymentDetailDO::getPaymentControl)
                .selectAs(ContractPaymentSchemeDO::getPayDate, ContractPaymentDetailDO::getPayDate)
                .selectAs(ContractPaymentSchemeDO::getRatio, ContractPaymentDetailDO::getRatio)
                .selectAs(ContractPaymentSchemeDO::getAmount, ContractPaymentDetailDO::getSchemeAmount)
                .selectAs(ContractPaymentSchemeDO::getMethod, ContractPaymentDetailDO::getMethod)
                .selectAs(ContractPaymentSchemeDO::getRemark, ContractPaymentDetailDO::getRemark)
                .selectAs(ContractPaymentSchemeDO::getAmount, ContractPaymentDetailDO::getSchemeAmount)
                .select("(select t1.amount - IFNULL(sum(t2.amount), 0) from pd_contract_payment_detail t2 where t2.scheme_id = t.scheme_id and t2.deleted = 0 and t2.id != t.id )  as remainAmount ")
                .selectAll(ContractPaymentDetailDO.class);
        return selectList(wrapper.eq(ContractPaymentDetailDO::getPaymentId, paymentId));
    }

    default int deleteByPaymentId(String paymentId) {
        return delete(ContractPaymentDetailDO::getPaymentId, paymentId);
    }

    /**
     * 付款计划主键获取付款详情已支付金额
     * @param ids
     */
    default List<ContractPaymentDetailDO> selectPaymentAmountListBySchemeId(Collection<String> ids, String paymentId) {
        MPJLambdaWrapperX<ContractPaymentDetailDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.select(ContractPaymentDetailDO::getSchemeId).selectSum(ContractPaymentDetailDO::getAmount);
        wrapper.in(ContractPaymentDetailDO::getSchemeId, ids)
                .ne(paymentId != null, ContractPaymentDetailDO::getPaymentId, paymentId)
                .groupBy(ContractPaymentDetailDO::getSchemeId);
        return selectList(wrapper);
    }
}