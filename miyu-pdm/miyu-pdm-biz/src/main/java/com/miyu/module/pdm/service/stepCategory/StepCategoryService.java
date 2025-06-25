package com.miyu.module.pdm.service.stepCategory;

import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.StepCategoryDO;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * PDM 工步分类 Service 接口
 *
 * @author 芋道源码
 */
public interface StepCategoryService {

    /**
     * 创建工步分类
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createStepCategory(@Valid StepCategorySaveReqVO createReqVO);

    /**
     * 更新工步分类
     *
     * @param updateReqVO 更新信息
     */
    void updateStepCategory(@Valid StepCategorySaveReqVO updateReqVO);

    /**
     * 删除工步分类
     *
     * @param id 编号
     */
    void deleteStepCategory(Long id);

    /**
     * 获得工步分类
     *
     * @param id 编号
     * @return 工步分类
     */
    StepCategoryDO getStepCategory(Long id);

    /**
     * 获得工步分类列表
     *
     * @param listReqVO 查询条件
     * @return 工步分类列表
     */
    List<StepCategoryDO> getStepCategoryList(StepCategoryListReqVO listReqVO);

    /**
     * 获得工步分类列表
     *
     * @param ids 编号数组
     * @return 工步分类列表
     */
    List<StepCategoryDO> getStepCategoryList(Collection<Long> ids);

    /**
     * 获得工步分类 Map
     *
     * @param ids 编号数组
     * @return 工步分类 Map
     */
    default Map<Long, StepCategoryDO> getStepCategoryMap(Collection<Long> ids) {
        return convertMap(getStepCategoryList(ids), StepCategoryDO::getId);
    }

}