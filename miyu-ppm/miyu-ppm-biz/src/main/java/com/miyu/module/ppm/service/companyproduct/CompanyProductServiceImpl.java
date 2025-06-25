package com.miyu.module.ppm.service.companyproduct;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
// import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;

import java.util.*;
import com.miyu.module.ppm.controller.admin.companyproduct.vo.*;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.companyproduct.CompanyProductMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.common.util.servlet.ServletUtils.getClientIP;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 企业产品表，用于销售和采购 Service 实现类
 *
 * @author Zhangyunfei
 */
@Service
@Validated
public class CompanyProductServiceImpl implements CompanyProductService {

    @Resource
    private CompanyProductMapper companyProductMapper;

    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Override
    // @GlobalTransactional(rollbackFor = Exception.class)
    public String createCompanyProduct(CompanyProductSaveReqVO createReqVO) {
        // 物料类型ID
        String materialId = createReqVO.getMaterialId();

        HashSet<String> set = new HashSet<>();
        set.add(materialId);
        // id获取物料类型集合
        List<MaterialConfigRespDTO> materialConfigList =  materialMCCApi.getMaterialConfigList(set).getCheckedData();
        // 验证物料类型是否存在
        if(materialConfigList.size() == 0){
            // 保存物料
            MaterialConfigReqDTO materialType = BeanUtils.toBean(createReqVO, MaterialConfigReqDTO.class);
            materialId = materialMCCApi.createMaterialConfig(materialType).getCheckedData();
        }

        // 物料保存成功
        if(StringUtils.isNotBlank(materialId)){
            // 保存公司产品
            CompanyProductDO companyProduct = BeanUtils.toBean(createReqVO, CompanyProductDO.class).setMaterialId(materialId);
            companyProduct.setCreationIp(getClientIP());
            companyProductMapper.insert(companyProduct);
            return companyProduct.getId();
        }
        return null;
    }

    @Override
    public void updateCompanyProduct(CompanyProductSaveReqVO updateReqVO) {
        // 校验存在
        validateCompanyProductExists(updateReqVO.getId());
        updateReqVO.setUpdatedIp(getClientIP());
        // 更新
        CompanyProductDO updateObj = BeanUtils.toBean(updateReqVO, CompanyProductDO.class);
        companyProductMapper.updateById(updateObj);
    }

    @Override
    public void deleteCompanyProduct(String id) {
        // 校验存在
        CompanyProductDO product = validateCompanyProductExists(id);
        // 验证产品是否关联合同
        vaildteContractExists(product);
        // 删除
        companyProductMapper.deleteById(id);
    }

    private CompanyProductDO validateCompanyProductExists(String id) {
        CompanyProductDO product = companyProductMapper.selectById(id);
        if (product == null) {
            throw exception(COMPANY_PRODUCT_NOT_EXISTS);
        }
        return product;
    }

    private void vaildteContractExists(CompanyProductDO product) {
        List<ContractOrderDO> list = contractOrderMapper.selectList(ContractOrderDO::getMaterialId, product.getMaterialId());
        if (list.size() > 0) {
            throw exception(COMPANY_PRODUCT_FAIL_DELETE_WITH_CONTRACT);
        }
    }

    @Override
    public List<CompanyProductDO> validProductList(Collection<String> ids) {
        if (CollUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        List<CompanyProductDO> list = companyProductMapper.selectBatchByMaterialIds(ids);
        Map<String, CompanyProductDO> productMap = convertMap(list, CompanyProductDO::getMaterialId);
        for (String id : ids) {
            CompanyProductDO product = productMap.get(id);
            if (productMap.get(id) == null) {
                throw exception(COMPANY_PRODUCT_NOT_EXISTS);
            }
        }
        return list;
    }

    @Override
    public CompanyProductDO getCompanyProduct(String id) {
        return companyProductMapper.selectById(id);
    }

    @Override
    public PageResult<CompanyProductDO> getCompanyProductPage(CompanyProductPageReqVO pageReqVO) {
        return companyProductMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CompanyProductDO> getProductListByCompanyId(String companyId) {
        return companyProductMapper.getProductListByCompanyId(companyId);
    }

    @Override
    public List<CompanyProductDO> queryCompanyProductByParty(String partyId, List<String> materialIds) {
        return companyProductMapper.queryCompanyProductByParty(partyId, materialIds);
    }

}
