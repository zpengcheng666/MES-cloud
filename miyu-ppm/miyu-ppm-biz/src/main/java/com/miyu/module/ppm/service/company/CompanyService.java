package com.miyu.module.ppm.service.company;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.company.vo.*;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.ppm.dal.dataobject.companydatabasefile.CompanyDatabaseFileDO;

/**
 * 企业基本信息 Service 接口
 *
 * @author 芋道源码
 */
public interface CompanyService {

    /**
     * 创建企业基本信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCompany(@Valid CompanySaveReqVO createReqVO);

    /**
     * 更新企业基本信息
     *
     * @param updateReqVO 更新信息
     */
    void updateCompany(@Valid CompanySaveReqVO updateReqVO);

    void updateCompanyAndSubmit(@Valid CompanySaveReqVO updateReqVO);

    /**
     * 删除企业基本信息
     *
     * @param ids 编号
     */
    void deleteCompany(List<String> ids);

    /**
     * 获得企业基本信息
     *
     * @param id 编号
     * @return 企业基本信息
     */
    CompanyDO getCompany(String id);

    /**
     * 获得企业基本信息分页
     *
     * @param pageReqVO 分页查询
     * @return 企业基本信息分页
     */
    PageResult<CompanyDO> getCompanyPage(CompanyPageReqVO pageReqVO);

    /**
     *  获取供应商字典集合
     * @return
     */
    List<Map<String, Object>> getCompanySimpleList();

    /**
     * 供应商类型获取供应商集合
     * @param types
     * @return
     */
    List<CompanyDO> getCompanyListByType(Collection<String> types);

    /**
     * 主键集合获取公司集合
     * @param ids
     * @return
     */
    List<CompanyDO> getCompanyListByIds(Collection<String> ids);


    /**
     * 提交供应商审批
     * @param id
     * @param processKey
     * @param loginUserId
     */
    void submitCompany(String id, String processKey, Long loginUserId);

    /**
     * 供应商审批更新状态
     * @param businessKey
     * @param status
     */
    void updateCompanyAuditStatus(String businessKey, Integer status);

    /**
     * 创建并提交供应商审批
     * @param createReqVO
     * @return
     */
    void createAndSubmitCompany(CompanySaveReqVO createReqVO);

    /**
     * 外协供应商创建
     * @param createReqVO
     * @return
     */
    String createCompanyCoord(CompanyCoordSaveReqVO createReqVO);

    /**
     * 外协供应商修改
     * @param updateReqVO
     */
    void updateCompanyCoord(CompanyCoordSaveReqVO updateReqVO);

    /**
     * 删除外协供应商
     * @param ids
     */
    void deleteCompanyCoord(List<String> ids);

    /**
     * 供应商主键获取附件集合
     * @param companyId
     * @return
     */
    List<CompanyDatabaseFileDO> getDatabaseFileListByCompanyId(String companyId);
}