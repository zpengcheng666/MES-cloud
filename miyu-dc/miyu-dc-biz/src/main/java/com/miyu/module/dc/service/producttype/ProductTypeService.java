package com.miyu.module.dc.service.producttype;

import java.util.*;
import javax.validation.*;
import com.miyu.module.dc.controller.admin.producttype.vo.*;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 产品类型 Service 接口
 *
 * @author 芋道源码
 */
public interface ProductTypeService {

    /**
     * 创建产品类型
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createProductType(@Valid ProductTypeSaveReqVO createReqVO);

    /**
     * 更新产品类型
     *
     * @param updateReqVO 更新信息
     */
    void updateProductType(@Valid ProductTypeSaveReqVO updateReqVO);

    /**
     * 删除产品类型
     *
     * @param id 编号
     */
    void deleteProductType(String id);

    /**
     * 获得产品类型
     *
     * @param id 编号
     * @return 产品类型
     */
    ProductTypeDO getProductType(String id);

    /**
     * 获得产品类型分页
     *
     * @param pageReqVO 分页查询
     * @return 产品类型分页
     */
    PageResult<ProductTypeDO> getProductTypePage(ProductTypePageReqVO pageReqVO);

    /**
     * 获取产品类型列表
     */
    List<ProductTypeDO> getProductTypeList();

    /**
     * 获取产品详情信息
     */
    List<ProductTypeDO> queryProductByDeviceId(String id);

    /**
     * 根据设备获取产品信息
     */
    List<ProductTypeDO> getProductListByDeviceId(String id);

}