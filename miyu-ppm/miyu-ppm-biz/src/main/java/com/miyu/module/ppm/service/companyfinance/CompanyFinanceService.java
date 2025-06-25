package com.miyu.module.ppm.service.companyfinance;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.companyfinance.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyfinance.CompanyFinanceDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 企业税务信息 Service 接口
 *
 * @author Zhangyunfei
 */
public interface CompanyFinanceService {

    /**
     * 创建企业税务信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCompanyFinance(@Valid CompanyFinanceSaveReqVO createReqVO);

    /**
     * 更新企业税务信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCompanyFinance(@Valid CompanyFinanceSaveReqVO updateReqVO);

    /**
     * 删除企业税务信息
     *
     * @param id 编号
     */
    void deleteCompanyFinance(String id);

    /**
     * 获得企业税务信息
     *
     * @param id 编号
     * @return 企业税务信息
     */
    CompanyFinanceDO getCompanyFinance(String id);

    /**
     * 获得企业税务信息分页
     *
     * @param pageReqVO 分页查询
     * @return 企业税务信息分页
     */
    PageResult<CompanyFinanceDO> getCompanyFinancePage(CompanyFinancePageReqVO pageReqVO);

    /**
     * 获取公司税务信息
     * @param companyId
     * @return
     */
    List<CompanyFinanceDO> getCompanyFinanceByCompanyId(String companyId);
}