package com.miyu.module.ppm.dal.mysql.contractinvoice;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.ppm.controller.admin.contractinvoice.vo.*;

/**
 * 购销合同发票 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface ContractInvoiceMapper extends BaseMapperX<ContractInvoiceDO> {

    default PageResult<ContractInvoiceDO> selectPage(ContractInvoicePageReqVO reqVO) {
        MPJLambdaWrapperX<ContractInvoiceDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, ContractInvoiceDO::getContractId)
                .like(reqVO.getContractNumber() != null, ContractDO::getNumber, reqVO.getContractNumber())
                .selectAs(ContractDO::getNumber, ContractInvoiceDO::getContractNumber)
                .selectAll(ContractInvoiceDO.class);

        return selectPage(reqVO, wrapper
                .eqIfPresent(ContractInvoiceDO::getContractId, reqVO.getContractId())
                .eqIfPresent(ContractInvoiceDO::getType, reqVO.getType())
                .eqIfPresent(ContractInvoiceDO::getBusinessType, reqVO.getBusinessType())
                .betweenIfPresent(ContractInvoiceDO::getInvoiceDate, reqVO.getInvoiceDate())
                .betweenIfPresent(ContractInvoiceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ContractInvoiceDO::getId));
    }

    default ContractInvoiceDO getContractInvoiceById(String id) {

        MPJLambdaWrapperX<ContractInvoiceDO> wrapper = new MPJLambdaWrapperX<>();
        wrapper.leftJoin(ContractDO.class, ContractDO::getId, ContractInvoiceDO::getContractId)
                .selectAs(ContractDO::getName, ContractInvoiceDO::getContractName)
                .selectAll(ContractInvoiceDO.class);
        return selectOne(wrapper.eq(ContractInvoiceDO::getId, id));
    }
}