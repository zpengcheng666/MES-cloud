package com.miyu.module.ppm.service.contract;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractPageReqVO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;

@Service
@Validated
public class ContractOrderServiceImpl implements ContractOrderService{

    @Resource
    ContractOrderMapper contractOrderMapper;

    /**
     * 合同Id获取合同信息
     * @param id
     * @return
     */
    @Override
    public List<ContractOrderRespDTO> getContractOrderByContractId(String id) {
        return contractOrderMapper.getContractOrderByContractId(id);
    }

    @Override
    public List<ContractOrderRespDTO> getContractOrderByReturnId(String consignmentReturnId) {
        return contractOrderMapper.getContractOrderByReturnId(consignmentReturnId);
    }

    /**
     * 查询合同订单信息
     * @param contractId
     * @return
     */
    @Override
    public List<ContractOrderRespDTO> getOrderList(String contractId) {
        return Collections.emptyList();
    }

    /**
     * 合同订单Id集合获取产品信息
     * @param ids
     * @return
     */
    @Override
    public List<ContractOrderRespDTO> queryContractOrderByIds(List<String> ids) {
        return contractOrderMapper.queryContractOrderByIds(ids);
    }

    @Override
    public List<ContractOrderDO> getContractOrderPage(ContractPageReqVO pageReqVO) {
        return contractOrderMapper.getContractOrderPage(pageReqVO);
    }
}
