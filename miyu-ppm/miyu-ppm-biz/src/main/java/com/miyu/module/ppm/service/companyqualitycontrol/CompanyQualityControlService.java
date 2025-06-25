package com.miyu.module.ppm.service.companyqualitycontrol;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.companyqualitycontrol.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyqualitycontrol.CompanyQualityControlDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;

/**
 * 企业质量控制信息 Service 接口
 *
 * @author Zhangyunfei
 */
public interface CompanyQualityControlService {

    /**
     * 创建企业质量控制信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCompanyQualityControl(@Valid CompanyQualityControlSaveReqVO createReqVO);

    /**
     * 更新企业质量控制信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCompanyQualityControl(@Valid CompanyQualityControlSaveReqVO updateReqVO);

    /**
     * 删除企业质量控制信息
     *
     * @param id 编号
     */
    void deleteCompanyQualityControl(String id);

    /**
     * 获得企业质量控制信息
     *
     * @param id 编号
     * @return 企业质量控制信息
     */
    CompanyQualityControlDO getCompanyQualityControl(String id);

    /**
     * 获得企业质量控制信息分页
     *
     * @param pageReqVO 分页查询
     * @return 企业质量控制信息分页
     */
    PageResult<CompanyQualityControlDO> getCompanyQualityControlPage(CompanyQualityControlPageReqVO pageReqVO);

    /**
     * 公司ID获得企业质量控制信息
     * @param companyId
     * @return
     */
    CompanyQualityControlDO getCompanyQualityControlByCompanyId(String companyId);
}