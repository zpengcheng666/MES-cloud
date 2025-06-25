package com.miyu.module.ppm.service.contract;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractPageReqVO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;

import java.util.List;

public interface ContractOrderService {

    /**
     * 合同Id获取合同产品信息
     * @param id
     * @return
     */
    List<ContractOrderRespDTO> getContractOrderByContractId(String id);

    /**
     * 获取退货单下合同产品信息
     */
    List<ContractOrderRespDTO> getContractOrderByReturnId(String consignmentReturnId);

    /**
     * 查询合同订单信息
     * @param contractId
     * @return
     */
    List<ContractOrderRespDTO> getOrderList(String contractId);

    /**
     * 合同订单Id集合获取产品信息
     */
    List<ContractOrderRespDTO> queryContractOrderByIds(List<String> contractIds);


    List<ContractOrderDO>  getContractOrderPage(ContractPageReqVO pageReqVO);
}
