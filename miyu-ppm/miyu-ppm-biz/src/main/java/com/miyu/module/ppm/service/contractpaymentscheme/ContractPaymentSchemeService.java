package com.miyu.module.ppm.service.contractpaymentscheme;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 合同付款计划 Service 接口
 *
 * @author Zhangyunfei
 */
public interface ContractPaymentSchemeService {

    /**
     * 创建合同付款计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createContractPaymentScheme(@Valid ContractPaymentSchemeSaveReqVO createReqVO);

    /**
     * 更新合同付款计划
     *
     * @param updateReqVO 更新信息
     */
    void updateContractPaymentScheme(@Valid ContractPaymentSchemeSaveReqVO updateReqVO);

    /**
     * 删除合同付款计划
     *
     * @param id 编号
     */
    void deleteContractPaymentScheme(String id);

    /**
     * 获得合同付款计划
     *
     * @param id 编号
     * @return 合同付款计划
     */
    ContractPaymentSchemeDO getContractPaymentScheme(String id);

    /**
     * 获得合同付款计划分页
     *
     * @param pageReqVO 分页查询
     * @return 合同付款计划分页
     */
    PageResult<ContractPaymentSchemeDO> getContractPaymentSchemePage(ContractPaymentSchemePageReqVO pageReqVO);
}