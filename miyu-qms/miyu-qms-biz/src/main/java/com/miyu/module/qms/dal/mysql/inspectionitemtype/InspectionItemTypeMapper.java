package com.miyu.module.qms.dal.mysql.inspectionitemtype;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.*;

/**
 * 检测项目分类 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InspectionItemTypeMapper extends BaseMapperX<InspectionItemTypeDO> {

    default List<InspectionItemTypeDO> selectList(InspectionItemTypeListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<InspectionItemTypeDO>()
                .betweenIfPresent(InspectionItemTypeDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionItemTypeDO::getParentId, reqVO.getParentId())
                .likeIfPresent(InspectionItemTypeDO::getName, reqVO.getName())
                .orderByDesc(InspectionItemTypeDO::getId));
    }

	default InspectionItemTypeDO selectByParentIdAndItemTypeName(String parentId, String itemTypeName) {
	    return selectOne(InspectionItemTypeDO::getParentId, parentId, InspectionItemTypeDO::getName, itemTypeName);
	}

    default Long selectCountByParentId(String parentId) {
        return selectCount(InspectionItemTypeDO::getParentId, parentId);
    }

}