package com.miyu.module.ppm.service.companycontact;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.companycontact.vo.*;
import com.miyu.module.ppm.dal.dataobject.companycontact.CompanyContactDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 企业联系人 Service 接口
 *
 * @author Zhangyunfei
 */
public interface CompanyContactService {

    /**
     * 创建企业联系人
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCompanyContact(@Valid CompanyContactSaveReqVO createReqVO);

    /**
     * 更新企业联系人
     *
     * @param updateReqVO 更新信息
     */
    void updateCompanyContact(@Valid CompanyContactSaveReqVO updateReqVO);

    /**
     * 删除企业联系人
     *
     * @param id 编号
     */
    void deleteCompanyContact(String id);

    /**
     * 获得企业联系人
     *
     * @param id 编号
     * @return 企业联系人
     */
    CompanyContactDO getCompanyContact(String id);

    /**
     * 获得企业联系人分页
     *
     * @param pageReqVO 分页查询
     * @return 企业联系人分页
     */
    PageResult<CompanyContactDO> getCompanyContactPage(CompanyContactPageReqVO pageReqVO);

    /**
     * 获得企业联系人字典集合
     *
     * @param
     * @return 部门 职位 负责人map集合
     */
    Map<String, Object> getCompanyContactSimpleList( String companyId);
}