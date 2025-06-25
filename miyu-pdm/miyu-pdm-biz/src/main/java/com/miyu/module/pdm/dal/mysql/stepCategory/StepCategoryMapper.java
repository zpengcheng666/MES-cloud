package com.miyu.module.pdm.dal.mysql.stepCategory;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategoryListReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.StepCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * PDM 工步分类 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StepCategoryMapper extends BaseMapperX<StepCategoryDO> {

    default List<StepCategoryDO> selectList(StepCategoryListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StepCategoryDO>()
                .likeIfPresent(StepCategoryDO::getName, reqVO.getName())
                .eqIfPresent(StepCategoryDO::getStatus, reqVO.getStatus())
                .orderByDesc(StepCategoryDO::getId));
    }

    default StepCategoryDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(StepCategoryDO::getParentId, parentId, StepCategoryDO::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(StepCategoryDO::getParentId, parentId);
    }

}