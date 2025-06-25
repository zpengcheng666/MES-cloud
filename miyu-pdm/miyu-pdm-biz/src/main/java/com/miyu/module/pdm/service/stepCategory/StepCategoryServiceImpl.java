package com.miyu.module.pdm.service.stepCategory;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.stepCategory.vo.StepCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.stepCategory.StepCategoryDO;
import com.miyu.module.pdm.dal.mysql.stepCategory.StepCategoryMapper;
import com.miyu.module.pdm.service.processPlanDetail.ProcessPlanDetailService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;

/**
 * PDM 工步分类 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class StepCategoryServiceImpl implements StepCategoryService {

    @Resource
    private StepCategoryMapper stepCategoryMapper;

    @Resource
    @Lazy // 延迟加载，避免循环依赖
    private ProcessPlanDetailService processPlanDetailService;

    @Override
    public Long createStepCategory(StepCategorySaveReqVO createReqVO) {
        // 校验父分类编号的有效性
        validateParentStepCategory(null, createReqVO.getParentId());
        // 校验分类名称的唯一性
        validateStepCategoryNameUnique(null, createReqVO.getParentId(), createReqVO.getName());

        // 插入
        StepCategoryDO category = BeanUtils.toBean(createReqVO, StepCategoryDO.class);
        stepCategoryMapper.insert(category);
        // 返回
        return category.getId();
    }

    @Override
    @LogRecord(type = PDM_STEP_CATEGORY_TYPE, subType = PDM_STEP_CATEGORY_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_STEP_CATEGORY_UPDATE_SUCCESS)
    public void updateStepCategory(StepCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateStepCategoryExists(updateReqVO.getId());
        // 校验父分类编号的有效性
        validateParentStepCategory(updateReqVO.getId(), updateReqVO.getParentId());
        // 校验分类名称的唯一性
        validateStepCategoryNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());

        // 更新
        StepCategoryDO updateObj = BeanUtils.toBean(updateReqVO, StepCategoryDO.class);
        stepCategoryMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("stepCategoryName", updateReqVO.getName());
    }

    @Override
    @LogRecord(type = PDM_STEP_CATEGORY_TYPE, subType = PDM_STEP_CATEGORY_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_STEP_CATEGORY_DELETE_SUCCESS)
    public void deleteStepCategory(Long id) {
        // 1.1 校验存在
        validateStepCategoryExists(id);
        // 1.2 校验是否有子工步分类
        if (stepCategoryMapper.selectCountByParentId(id) > 0) {
            throw exception(STEP_CATEGORY_EXITS_CHILDREN);
        }
        // 1.3 校验是否有工步
        if (processPlanDetailService.getStepCountByCategoryId(id) > 0) {
            throw exception(STEP_CATEGORY_EXITS_STEP);
        }
        // 2. 删除
        stepCategoryMapper.deleteById(id);
        StepCategoryDO stepCategoryDO = stepCategoryMapper.selectById(id);
        // 记录日志
        LogRecordContext.putVariable("stepCategoryName", stepCategoryDO.getName());
    }

    private void validateStepCategoryExists(Long id) {
        if (stepCategoryMapper.selectById(id) == null) {
            throw exception(STEP_CATEGORY_NOT_EXISTS);
        }
    }

    private void validateParentStepCategory(Long id, Long parentId) {
        if (parentId == null || StepCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父工步分类
        if (Objects.equals(id, parentId)) {
            throw exception(STEP_CATEGORY_PARENT_ERROR);
        }
        // 2. 父工步分类不存在
        StepCategoryDO parentCategory = stepCategoryMapper.selectById(parentId);
        if (parentCategory == null) {
            throw exception(STEP_CATEGORY_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父工步分类，如果父工步分类是自己的子工步分类，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentCategory.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(STEP_CATEGORY_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父工步分类
            if (parentId == null || StepCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentCategory = stepCategoryMapper.selectById(parentId);
            if (parentCategory == null) {
                break;
            }
        }
    }

    private void validateStepCategoryNameUnique(Long id, Long parentId, String name) {
        StepCategoryDO stepCategory = stepCategoryMapper.selectByParentIdAndName(parentId, name);
        if (stepCategory == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的工步分类
        if (id == null) {
            throw exception(STEP_CATEGORY_NAME_DUPLICATE);
        }
        if (!Objects.equals(stepCategory.getId(), id)) {
            throw exception(STEP_CATEGORY_NAME_DUPLICATE);
        }
    }

    @Override
    public StepCategoryDO getStepCategory(Long id) {
        return stepCategoryMapper.selectById(id);
    }

    @Override
    public List<StepCategoryDO> getStepCategoryList(StepCategoryListReqVO listReqVO) {
        return stepCategoryMapper.selectList(listReqVO);
    }

    @Override
    public List<StepCategoryDO> getStepCategoryList(Collection<Long> ids) {
        return stepCategoryMapper.selectBatchIds(ids);
    }

}