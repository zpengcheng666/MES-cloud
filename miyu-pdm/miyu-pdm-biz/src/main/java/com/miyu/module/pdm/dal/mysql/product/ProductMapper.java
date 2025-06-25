package com.miyu.module.pdm.dal.mysql.product;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.product.vo.ProductPageReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper extends BaseMapperX<ProductDO> {

    default PageResult<ProductDO> selectPage(ProductPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductDO>()
                .likeIfPresent(ProductDO::getProductName, reqVO.getProductName())
                .likeIfPresent(ProductDO::getProductNumber, reqVO.getProductNumber())
                .eqIfPresent(ProductDO::getCategoryId, reqVO.getCategoryId())
                .eqIfPresent(ProductDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(BaseDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ProductDO::getProductType, reqVO.getProductType())
                .orderByDesc(ProductDO::getId));
    }

    default Long selectCountByCategoryId(Long categoryId) {
        return selectCount(ProductDO::getCategoryId, categoryId);
    }

    default List<ProductDO> selectListByStatus(ProductPageReqVO reqVO) {
        return selectList(ProductDO::getStatus, reqVO.getStatus(), ProductDO::getProductType, reqVO.getProductType());
    }

    default ProductDO selectByProductNumber(String productNumber) {
        return selectOne(ProductDO::getProductNumber, productNumber);
    }

}
