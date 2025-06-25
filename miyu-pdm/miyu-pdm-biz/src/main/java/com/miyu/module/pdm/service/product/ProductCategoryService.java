package com.miyu.module.pdm.service.product;

import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * PDM 产品分类 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductCategoryService {

    /**
     * 创建产品分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createProductCategory(@Valid ProductCategorySaveReqVO createReqVO);

    /**
     * 更新产品分类
     *
     * @param updateReqVO 更新信息
     */
    void updateProductCategory(@Valid ProductCategorySaveReqVO updateReqVO);

    /**
     * 删除产品分类
     *
     * @param id 编号
     */
    void deleteProductCategory(Long id);

    /**
     * 获得产品分类
     *
     * @param id 编号
     * @return 产品分类
     */
    ProductCategoryDO getProductCategory(Long id);

    /**
     * 获得产品分类列表
     *
     * @param listReqVO 查询条件
     * @return 产品分类列表
     */
    List<ProductCategoryDO> getProductCategoryList(ProductCategoryListReqVO listReqVO);

    /**
     * 获得产品分类列表
     *
     * @param ids 编号数组
     * @return 产品分类列表
     */
    List<ProductCategoryDO> getProductCategoryList(Collection<Long> ids);

    /**
     * 获得产品分类 Map
     *
     * @param ids 编号数组
     * @return 产品分类 Map
     */
    default Map<Long, ProductCategoryDO> getProductCategoryMap(Collection<Long> ids) {
        return convertMap(getProductCategoryList(ids), ProductCategoryDO::getId);
    }

}