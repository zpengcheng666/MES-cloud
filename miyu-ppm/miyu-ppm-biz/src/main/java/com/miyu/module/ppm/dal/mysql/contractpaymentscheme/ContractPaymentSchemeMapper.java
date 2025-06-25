package com.miyu.module.ppm.dal.mysql.contractpaymentscheme;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.controller.admin.contractpayment.vo.ContractPaymentReqVO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo.*;

/**
 * 合同付款计划 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractPaymentSchemeMapper extends BaseMapperX<ContractPaymentSchemeDO> {

    default PageResult<ContractPaymentSchemeDO> selectPage(ContractPaymentSchemePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ContractPaymentSchemeDO>()
                .eqIfPresent(ContractPaymentSchemeDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ContractPaymentSchemeDO::getPaymentControl, reqVO.getPaymentControl())
                .betweenIfPresent(ContractPaymentSchemeDO::getPayDate, reqVO.getPayDate())
                .eqIfPresent(ContractPaymentSchemeDO::getRatio, reqVO.getRatio())
                .eqIfPresent(ContractPaymentSchemeDO::getAmount, reqVO.getAmount())
                .eqIfPresent(ContractPaymentSchemeDO::getMethod, reqVO.getMethod())
                .eqIfPresent(ContractPaymentSchemeDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(ContractPaymentSchemeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ContractPaymentSchemeDO::getId));
    }

    default List<ContractPaymentSchemeDO> selectListByContractId(String contractId) {
        return selectList(ContractPaymentSchemeDO::getContractId, contractId);
    }

    default int deleteByContractId(String contractId) {
        return delete(ContractPaymentSchemeDO::getContractId, contractId);
    }

    default List<ContractPaymentSchemeDO> selectByContractId(String contractId) {
        return selectList(ContractPaymentSchemeDO::getContractId, contractId);
    }

    default List<ContractPaymentSchemeDO> selectListPaymentSchemeByContractId(ContractPaymentReqVO reqVO) {
        MPJLambdaWrapperX<ContractPaymentSchemeDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.selectAll(ContractPaymentSchemeDO.class)
//                .select("(select IFNULL(sum(t1.amount), 0) from pd_contract_payment_detail t1 where t1.scheme_id = t.id and t1.deleted = 0)  as payAmountSum ")
                .select("(select t.amount - IFNULL(sum(t1.amount), 0) from pd_contract_payment_detail t1 where t1.scheme_id = t.id and t1.deleted = 0 and if("+ reqVO.getPaymentId()+" is not null, t1.id != "+reqVO.getPaymentId()+", 1 = 1))  as remainAmount ")
                .eq(ContractPaymentSchemeDO::getContractId, reqVO.getContractId());
//                .ne(reqVO.getSchemeId() != null, ContractPaymentSchemeDO::getId, reqVO.getSchemeId());;
        return selectList(wrapper);
    }


    default List<ContractPaymentSchemeDO> selectListByIds(Collection<String> ids) {
        return selectList(ContractPaymentSchemeDO::getId, ids);
    }
}