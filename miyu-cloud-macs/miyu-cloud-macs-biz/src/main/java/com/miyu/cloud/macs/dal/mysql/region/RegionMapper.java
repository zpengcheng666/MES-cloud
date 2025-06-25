package com.miyu.cloud.macs.dal.mysql.region;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.dal.dataobject.region.RegionDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.region.vo.*;

/**
 * 区域 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface RegionMapper extends BaseMapperX<RegionDO> {

    default List<RegionDO> selectList(RegionListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<RegionDO>()
                .eqIfPresent(RegionDO::getCode, reqVO.getCode())
                .likeIfPresent(RegionDO::getName, reqVO.getName())
                .eqIfPresent(RegionDO::getPublicStatus, reqVO.getPublicStatus())
                .eqIfPresent(RegionDO::getDescription, reqVO.getDescription())
                .eqIfPresent(RegionDO::getParentId, reqVO.getParentId())
                .eqIfPresent(RegionDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(RegionDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(RegionDO::getUpdateBy, reqVO.getUpdateBy())
                .orderByDesc(RegionDO::getId));
    }

	default RegionDO selectByParentIdAndCode(String parentId, String code) {
	    return selectOne(RegionDO::getParentId, parentId, RegionDO::getCode, code);
	}

    default Long selectCountByParentId(String parentId) {
        return selectCount(RegionDO::getParentId, parentId);
    }

}