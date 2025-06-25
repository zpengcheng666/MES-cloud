package com.miyu.module.ppm.service.companyproduct;

import java.util.*;
import javax.validation.*;
import com.miyu.module.ppm.controller.admin.companyproduct.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 企业产品表，用于销售和采购 Service 接口
 *
 * @author Zhangyunfei
 */
public interface CompanyProductService {

    /**
     * 创建企业产品表，用于销售和采购
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCompanyProduct(@Valid CompanyProductSaveReqVO createReqVO);

    /**
     * 更新企业产品表，用于销售和采购
     *
     * @param updateReqVO 更新信息
     */
    void updateCompanyProduct(@Valid CompanyProductSaveReqVO updateReqVO);

    /**
     * 删除企业产品表，用于销售和采购
     *
     * @param id 编号
     */
    void deleteCompanyProduct(String id);

    /**
     * 校验产品们的有效性
     *
     * @param ids 编号数组
     * @return 产品列表
     */
    List<CompanyProductDO> validProductList(Collection<String> ids);

    /**
     * 获得企业产品表，用于销售和采购
     *
     * @param id 编号
     * @return 企业产品表，用于销售和采购
     */
    CompanyProductDO getCompanyProduct(String id);

    /**
     * 获得企业产品表，用于销售和采购分页
     *
     * @param pageReqVO 分页查询
     * @return 企业产品表，用于销售和采购分页
     */
    PageResult<CompanyProductDO> getCompanyProductPage(CompanyProductPageReqVO pageReqVO);

    /**
     * 供应商主键查询产品集合
     * @param companyId
     * @return
     */
    List<CompanyProductDO> getProductListByCompanyId(String companyId);

    /**
     * 根据企业产品查询是否免检
     */
    List<CompanyProductDO>  queryCompanyProductByParty(String partyId , List<String> materialIds);

}