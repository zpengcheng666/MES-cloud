package com.miyu.module.pdm.service.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.product.vo.ProductPageReqVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductRespVO;
import com.miyu.module.pdm.controller.admin.product.vo.ProductSaveReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 产品 Service 接口
 *
 * @author liuy
 */
public interface ProductService {

    /**
     * 创建产品
     *
     * @param createReqVO 创建产品信息
     * @return 角色编号
     */
    String createProduct(@Valid ProductSaveReqVO createReqVO);

    /**
     * 更新产品
     *
     * @param updateReqVO 更新产品信息
     */
    void updateProduct(@Valid ProductSaveReqVO updateReqVO);

    /**
     * 删除产品
     *
     * @param id 产品Id
     */
    void deleteProduct(String id);

    /**
     * 获得产品
     *
     * @param id 产品Id
     * @return 产品
     */
    ProductDO getProduct(String id);

    /**
     * 获得产品分页
     *
     * @param reqVO 产品分页查询
     * @return 产品分页结果
     */
    PageResult<ProductRespVO> getProductVOPage(ProductPageReqVO reqVO);

    /**
     * 获得指定状态的产品 VO 列表
     *
     * @param status 状态
     * @return 产品 VO 列表
     */
    List<ProductRespVO> getProductVOListByStatus(ProductPageReqVO reqVO);

    /**
     * 基于产品分类编号，获得产品数量
     *
     * @param categoryId 产品分类编号
     * @return 产品数量
     */
    Long getProductCountByCategoryId(Long categoryId);

}
