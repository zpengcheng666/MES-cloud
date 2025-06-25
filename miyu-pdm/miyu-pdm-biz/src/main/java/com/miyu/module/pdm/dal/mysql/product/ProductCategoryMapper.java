package com.miyu.module.pdm.dal.mysql.product;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategoryListReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * PDM 产品分类 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductCategoryMapper extends BaseMapperX<ProductCategoryDO> {

    default List<ProductCategoryDO> selectList(ProductCategoryListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProductCategoryDO>()
                .likeIfPresent(ProductCategoryDO::getName, reqVO.getName())
                .eqIfPresent(ProductCategoryDO::getStatus, reqVO.getStatus())
                .orderByDesc(ProductCategoryDO::getId));
    }

    default ProductCategoryDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(ProductCategoryDO::getParentId, parentId, ProductCategoryDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(ProductCategoryDO::getParentId, parentId);
    }

}