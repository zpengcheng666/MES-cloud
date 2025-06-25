package com.miyu.module.dc.dal.mysql.producttype;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import com.miyu.module.dc.dal.dataobject.devicecollect.DeviceCollectDO;
import com.miyu.module.dc.dal.dataobject.producttype.ProductTypeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.dc.controller.admin.producttype.vo.*;
import org.springframework.util.CollectionUtils;

/**
 * 产品类型 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface ProductTypeMapper extends BaseMapperX<ProductTypeDO> {

    default PageResult<ProductTypeDO> selectPage(ProductTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ProductTypeDO>()
                .likeIfPresent(ProductTypeDO::getProductTypeName, reqVO.getProductTypeName())
                .betweenIfPresent(ProductTypeDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(ProductTypeDO::getId));
    }

    default List<ProductTypeDO> existsType(ProductTypeSaveReqVO createReqVO){
        return selectList(new LambdaQueryWrapperX<ProductTypeDO>()
                .eq(ProductTypeDO::getProductTypeName, createReqVO.getProductTypeName())
                .neIfPresent(ProductTypeDO::getId, createReqVO.getId()));
    }

    default List<ProductTypeDO> existsTopic(ProductTypeSaveReqVO createReqVO){
        return selectList(new LambdaQueryWrapperX<ProductTypeDO>()
                .eq(ProductTypeDO::getTopicId, createReqVO.getTopicId())
                .neIfPresent(ProductTypeDO::getId, createReqVO.getId()));
    }

    default List<ProductTypeDO> getProductTypeList(List<String> productTypeIds){
        return selectList(new LambdaQueryWrapperX<ProductTypeDO>()
                .in(ProductTypeDO::getId, productTypeIds));
    }

    default String getTopicIdByProductTypeId(String productTypeId){
        MPJLambdaWrapper<ProductTypeDO> wrapperX = new MPJLambdaWrapper<>();
        wrapperX.eq(ProductTypeDO::getId,productTypeId);
        return selectOne(wrapperX).getTopicId();
    }

    @TenantIgnore
    default ProductTypeDO selectProductTypeById(String productTypeId){return selectOne(ProductTypeDO::getId, productTypeId);}

}