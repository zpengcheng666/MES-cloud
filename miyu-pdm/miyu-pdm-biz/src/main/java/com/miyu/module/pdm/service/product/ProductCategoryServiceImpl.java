package com.miyu.module.pdm.service.product;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import com.miyu.module.pdm.dal.mysql.product.ProductCategoryMapper;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;

/**
 * PDM 产品分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Resource
    private ProductCategoryMapper productCategoryMapper;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private ProductService productService;

    @Override
    public Long createProductCategory(ProductCategorySaveReqVO createReqVO) {
        // 校验父分类编号的有效性
        validateParentProductCategory(null, createReqVO.getParentId());
        // 校验分类名称的唯一性
        validateProductCategoryNameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // 插入
        ProductCategoryDO category = BeanUtils.toBean(createReqVO, ProductCategoryDO.class);
        productCategoryMapper.insert(category);
        // 返回
        return category.getId();
    }

    @Override
    public void updateProductCategory(ProductCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateProductCategoryExists(updateReqVO.getId());
        // 校验父分类编号的有效性
        validateParentProductCategory(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验分类名称的唯一性
        validateProductCategoryNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        ProductCategoryDO updateObj = BeanUtils.toBean(updateReqVO, ProductCategoryDO.class);
        productCategoryMapper.updateById(updateObj);
    }

    @Override
    public void deleteProductCategory(Long id) {
        // 1.1 校验存在
        validateProductCategoryExists(id);
        // 1.2 校验是否有子产品分类
        if (productCategoryMapper.selectCountByParentId(id) > 0) {
            throw exception(PRODUCT_CATEGORY_EXITS_CHILDREN);
        }
        // 1.3 校验是否有产品
        if (productService.getProductCountByCategoryId(id) > 0) {
            throw exception(PRODUCT_CATEGORY_EXITS_PRODUCT);
        }
        // 2. 删除
        productCategoryMapper.deleteById(id);
    }

    private void validateProductCategoryExists(Long id) {
        if (productCategoryMapper.selectById(id) == null) {
            throw exception(PRODUCT_CATEGORY_NOT_EXISTS);
        }
    }

    private void validateParentProductCategory(Long id, Long parentId) {
        if (parentId == null || ProductCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父产品分类
        if (Objects.equals(id, parentId)) {
            throw exception(PRODUCT_CATEGORY_PARENT_ERROR);
        }
        // 2. 父产品分类不存在
        ProductCategoryDO parentCategory = productCategoryMapper.selectById(parentId);
        if (parentCategory == null) {
            throw exception(PRODUCT_CATEGORY_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父产品分类，如果父产品分类是自己的子产品分类，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentCategory.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(PRODUCT_CATEGORY_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父产品分类
            if (parentId == null || ProductCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentCategory = productCategoryMapper.selectById(parentId);
            if (parentCategory == null) {
                break;
            }
        }
    }

    private void validateProductCategoryNameUnique(Long id, Long parentId, String name) {
        ProductCategoryDO productCategory = productCategoryMapper.selectByParentIdAndName(parentId, name);
        if (productCategory == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的产品分类
        if (id == null) {
            throw exception(PRODUCT_CATEGORY_NAME_DUPLICATE);
        }
        if (!Objects.equals(productCategory.getId(), id)) {
            throw exception(PRODUCT_CATEGORY_NAME_DUPLICATE);
        }
    }

    @Override
    public ProductCategoryDO getProductCategory(Long id) {
        return productCategoryMapper.selectById(id);
    }

    @Override
    public List<ProductCategoryDO> getProductCategoryList(ProductCategoryListReqVO listReqVO) {
        return productCategoryMapper.selectList(listReqVO);
    }

    @Override
    public List<ProductCategoryDO> getProductCategoryList(Collection<Long> ids) {
        return productCategoryMapper.selectBatchIds(ids);
    }

}