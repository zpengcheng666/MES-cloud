package com.miyu.module.ppm.service.companyfinance;

import com.miyu.module.ppm.controller.admin.companycontact.vo.CompanyContactSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.mysql.company.CompanyMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.ppm.controller.admin.companyfinance.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyfinance.CompanyFinanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.companyfinance.CompanyFinanceMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 企业税务信息 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class CompanyFinanceServiceImpl implements CompanyFinanceService {

    @Resource
    private CompanyFinanceMapper companyFinanceMapper;
    @Resource
    private CompanyMapper companyMapper;

    @Override
    public String createCompanyFinance(CompanyFinanceSaveReqVO createReqVO) {
        // 插入
        CompanyFinanceDO companyFinance = BeanUtils.toBean(createReqVO, CompanyFinanceDO.class);
        companyFinanceMapper.insert(companyFinance);
        // 返回
        return companyFinance.getId();
    }

    @Override
    @LogRecord(type = COMPANY_FINANCE_TYPE, subType = COMPANY_FINANCE_UPDATE_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = COMPANY_FINANCE_UPDATE_SUCCESS)
    public void updateCompanyFinance(CompanyFinanceSaveReqVO updateReqVO) {
        // 校验存在
        CompanyFinanceDO companyFinanceDO = validateCompanyFinanceExists(updateReqVO.getId());
        // 更新
        CompanyFinanceDO updateObj = BeanUtils.toBean(updateReqVO, CompanyFinanceDO.class);
        companyFinanceMapper.updateById(updateObj);
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(companyFinanceDO, CompanyFinanceSaveReqVO.class));
        LogRecordContext.putVariable("companyFinance", companyFinanceDO);
    }

    @Override
    public void deleteCompanyFinance(String id) {
        // 校验存在
        validateCompanyFinanceExists(id);
        // 删除
        companyFinanceMapper.deleteById(id);
    }

    private CompanyFinanceDO validateCompanyFinanceExists(String id) {
        CompanyFinanceDO companyFinanceDO = companyFinanceMapper.selectById(id);
        if (companyFinanceDO == null) {
            throw exception(COMPANY_FINANCE_NOT_EXISTS);
        }
        CompanyDO companyDO =  companyMapper.selectById(companyFinanceDO.getCompanyId());
        companyFinanceDO.setCompanyName(companyDO.getName());
        return companyFinanceDO;
    }

    @Override
    public CompanyFinanceDO getCompanyFinance(String id) {
        return companyFinanceMapper.selectById(id);
    }

    @Override
    public PageResult<CompanyFinanceDO> getCompanyFinancePage(CompanyFinancePageReqVO pageReqVO) {
        return companyFinanceMapper.selectPage(pageReqVO);
    }

    /**
     * 获取公司税负信息集合
     * @param companyId
     * @return
     */
    @Override
    public List<CompanyFinanceDO> getCompanyFinanceByCompanyId(String companyId) {
        return companyFinanceMapper.selectByCompanyId(companyId);
    }

}