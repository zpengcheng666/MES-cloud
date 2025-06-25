package com.miyu.module.pdm.service.product;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.enums.CommonStatusEnum;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.google.common.annotations.VisibleForTesting;
import com.miyu.module.pdm.controller.admin.product.vo.ProductPageReqVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductRespVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.dataobject.DataObjectDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import com.miyu.module.pdm.dal.mysql.dataobject.DataObjectMapper;
import com.miyu.module.pdm.dal.mysql.product.ProductMapper;
import com.miyu.module.pdm.netty.DataObjectUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;

/**
 * 产品 Service 实现类
 *
 * @author liuy
 */
@Service
@Slf4j
public class ProductServiceImpl implements ProductService {


    @Resource
    private ProductMapper productMapper;

    @Resource
    private ProductCategoryService productCategoryService;

    @Resource
    private DataObjectMapper dataObjectMapper;

    @Override
    public String createProduct(ProductSaveReqVO createReqVO) {
        // 检验产品图号唯一
        validateProductNumberUnique(null, createReqVO.getProductNumber());
        // 插入
        ProductDO product = BeanUtils.toBean(createReqVO, ProductDO.class)
                .setStatus(CommonStatusEnum.ENABLE.getStatus())
                .setId(IdUtil.fastSimpleUUID())
                ;
        productMapper.insert(product);
        // 生成产品的同时生成动态表-默认客户化标识为default
        List<DataObjectDO> list = DataObjectUtil.getDO7Forinsert(product.getId(), "default");
        for (DataObjectDO pdm_data_object : list) {
            dataObjectMapper.insertSelective(pdm_data_object);
        }
        return product.getId();
    }

    @Override
    public void updateProduct(ProductSaveReqVO updateReqVO) {
        // 校验存在
        validateProductExists(updateReqVO.getId());
        // 检验产品图号唯一
        validateProductNumberUnique(updateReqVO.getId(), updateReqVO.getProductNumber());
        // 更新
        ProductDO updateObj = BeanUtils.toBean(updateReqVO, ProductDO.class);
        productMapper.updateById(updateObj);
    }

    private void validateProductExists(String id) {
        if (productMapper.selectById(id) == null) {
            throw exception(PRODUCT_NOT_EXISTS);
        }
    }

    /**
     * 检验产品图号是否唯一
     */
    @VisibleForTesting
    void validateProductNumberUnique(String id, String productNumber) {
        if (StrUtil.isBlank(productNumber)) {
            return;
        }
        ProductDO product = productMapper.selectByProductNumber(productNumber);
        if (product == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null || id.equals("")) {
            throw exception(PRODUCT_NUMBER_EXISTS);
        }
        if (!product.getId().equals(id)) {
            throw exception(PRODUCT_NUMBER_EXISTS);
        }
    }

    @Override
    public void deleteProduct(String id) {
        // 校验存在
        validateProductExists(id);
        // 删除
        productMapper.deleteById(id);
    }

    @Override
    public ProductDO getProduct(String id) {
        return productMapper.selectById(id);
    }

    @Override
    public PageResult<ProductRespVO> getProductVOPage(ProductPageReqVO reqVO) {
        PageResult<ProductDO> pageResult = productMapper.selectPage(reqVO);
        return new PageResult<>(buildProductVOList(pageResult.getList()), pageResult.getTotal());
    }

    @Override
    public List<ProductRespVO> getProductVOListByStatus(ProductPageReqVO reqVO) {
        List<ProductDO> list = productMapper.selectListByStatus(reqVO);
        return buildProductVOList(list);
    }

    @Override
    public Long getProductCountByCategoryId(Long categoryId) {
        return productMapper.selectCountByCategoryId(categoryId);
    }

    private List<ProductRespVO> buildProductVOList(List<ProductDO> list) {
        if (CollUtil.isEmpty(list)) {
            return Collections.emptyList();
        }
        Map<Long, ProductCategoryDO> categoryMap = productCategoryService.getProductCategoryMap(
                convertSet(list, ProductDO::getCategoryId));
        return BeanUtils.toBean(list, ProductRespVO.class, product -> {
            MapUtils.findAndThen(categoryMap, product.getCategoryId(),
                    category -> product.setCategoryName(category.getName()));
        });
    }

}
