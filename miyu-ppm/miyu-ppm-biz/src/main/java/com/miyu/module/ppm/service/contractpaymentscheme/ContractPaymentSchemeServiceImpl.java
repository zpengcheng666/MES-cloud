package com.miyu.module.ppm.service.contractpaymentscheme;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contractpaymentscheme.ContractPaymentSchemeMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 合同付款计划 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class ContractPaymentSchemeServiceImpl implements ContractPaymentSchemeService {

    @Resource
    private ContractPaymentSchemeMapper contractPaymentSchemeMapper;

    @Override
    public String createContractPaymentScheme(ContractPaymentSchemeSaveReqVO createReqVO) {
        // 插入
        ContractPaymentSchemeDO contractPaymentScheme = BeanUtils.toBean(createReqVO, ContractPaymentSchemeDO.class);
        contractPaymentSchemeMapper.insert(contractPaymentScheme);
        // 返回
        return contractPaymentScheme.getId();
    }

    @Override
    public void updateContractPaymentScheme(ContractPaymentSchemeSaveReqVO updateReqVO) {
        // 校验存在
        validateContractPaymentSchemeExists(updateReqVO.getId());
        // 更新
        ContractPaymentSchemeDO updateObj = BeanUtils.toBean(updateReqVO, ContractPaymentSchemeDO.class);
        contractPaymentSchemeMapper.updateById(updateObj);
    }

    @Override
    public void deleteContractPaymentScheme(String id) {
        // 校验存在
        validateContractPaymentSchemeExists(id);
        // 删除
        contractPaymentSchemeMapper.deleteById(id);
    }

    private void validateContractPaymentSchemeExists(String id) {
        if (contractPaymentSchemeMapper.selectById(id) == null) {
            throw exception(CONTRACT_PAYMENT_SCHEME_NOT_EXISTS);
        }
    }

    @Override
    public ContractPaymentSchemeDO getContractPaymentScheme(String id) {
        return contractPaymentSchemeMapper.selectById(id);
    }

    @Override
    public PageResult<ContractPaymentSchemeDO> getContractPaymentSchemePage(ContractPaymentSchemePageReqVO pageReqVO) {
        return contractPaymentSchemeMapper.selectPage(pageReqVO);
    }
}