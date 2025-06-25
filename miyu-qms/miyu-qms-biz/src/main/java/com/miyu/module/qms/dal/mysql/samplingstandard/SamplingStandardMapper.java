package com.miyu.module.qms.dal.mysql.samplingstandard;

import java.util.*;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.samplingstandard.vo.*;

/**
 * 抽样标准 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface SamplingStandardMapper extends BaseMapperX<SamplingStandardDO> {

    default List<SamplingStandardDO> selectList(SamplingStandardListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SamplingStandardDO>()
                .betweenIfPresent(SamplingStandardDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(SamplingStandardDO::getParentId, reqVO.getParentId())
                .likeIfPresent(SamplingStandardDO::getName, reqVO.getName())
                .orderByDesc(SamplingStandardDO::getId));
    }

    default SamplingStandardDO selectByParentIdAndItemName(String parentId, String itemName) {
        return selectOne(SamplingStandardDO::getParentId, parentId, SamplingStandardDO::getName, itemName);
    }


    default Long selectCountByParentId(String parentId) {
        return selectCount(SamplingStandardDO::getParentId, parentId);
    }

}