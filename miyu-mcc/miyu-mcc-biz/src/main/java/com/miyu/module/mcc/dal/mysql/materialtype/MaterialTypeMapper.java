package com.miyu.module.mcc.dal.mysql.materialtype;

import java.util.*;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.mcc.controller.admin.materialtype.vo.*;

/**
 * 编码类别属性表(树形结构) Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface MaterialTypeMapper extends BaseMapperX<MaterialTypeDO> {

    default List<MaterialTypeDO> selectList(MaterialListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MaterialTypeDO>()
                .betweenIfPresent(MaterialTypeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(MaterialTypeDO::getCode, reqVO.getCode())
                .likeIfPresent(MaterialTypeDO::getName, reqVO.getName())
                .eqIfPresent(MaterialTypeDO::getParentId, reqVO.getParentId())
                .eqIfPresent(MaterialTypeDO::getBitNumber, reqVO.getBitNumber())
                .eqIfPresent(MaterialTypeDO::getEncodingProperty, reqVO.getEncodingProperty())
                .orderByDesc(MaterialTypeDO::getId));
    }

	default MaterialTypeDO selectByParentIdAndName(String parentId, String name) {
	    return selectOne(MaterialTypeDO::getParentId, parentId, MaterialTypeDO::getName, name);
	}

    default Long selectCountByParentId(String parentId) {
        return selectCount(MaterialTypeDO::getParentId, parentId);
    }

}