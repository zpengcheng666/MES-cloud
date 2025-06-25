package com.miyu.module.pdm.service.toolingCategory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryPageReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.toolingCategory.ToolingCategoryDO;
import com.miyu.module.pdm.dal.mysql.toolingCategory.ToolingCategoryMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;

import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import java.util.List;
import java.util.Objects;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;
import static com.miyu.module.pdm.enums.LogRecordConstants.*;


/**
 * 产品分类信息 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ToolingCategoryServiceImpl implements ToolingCategoryService {

    @Resource
    private ToolingCategoryMapper toolingCategoryMapper;

    @Override
    public Long createToolingCategory(ToolingCategorySaveReqVO createReqVO) {
        // 校验父分类编号的有效性
        validateParentToolingCategory(null, createReqVO.getParentId());
        // 校验分类名称的唯一性
        validateToolingCategoryNameUnique(null, createReqVO.getParentId(), createReqVO.getName());
        // 插入
        ToolingCategoryDO toolingCategory = BeanUtils.toBean(createReqVO, ToolingCategoryDO.class);
        toolingCategoryMapper.insert(toolingCategory);
        // 返回
        return toolingCategory.getId();
    }

    @Override
    @LogRecord(type = PDM_TOOLING_CATEGORY_TYPE, subType = PDM_TOOLING_CATEGORY_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PDM_TOOLING_CATEGORY_UPDATE_SUCCESS)
    public void updateToolingCategory(ToolingCategorySaveReqVO updateReqVO) {
        // 校验存在
        validateToolingCategoryExists(updateReqVO.getId());
        // 校验父分类编号的有效性
        validateParentToolingCategory(null, updateReqVO.getParentId());
        // 校验分类名称的唯一性
        validateToolingCategoryNameUnique(updateReqVO.getId(), updateReqVO.getParentId(), updateReqVO.getName());
        // 更新
        ToolingCategoryDO updateObj = BeanUtils.toBean(updateReqVO, ToolingCategoryDO.class);
        toolingCategoryMapper.updateById(updateObj);
        // 记录日志
        LogRecordContext.putVariable("toolingCategoryName", updateReqVO.getName());
    }

    @Override
    @LogRecord(type = PDM_TOOLING_CATEGORY_TYPE, subType = PDM_TOOLING_CATEGORY_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PDM_TOOLING_CATEGORY_DELETE_SUCCESS)
    public void deleteToolingCategory(Long id) {
        // 校验存在
        validateToolingCategoryExists(id);
        // 1.2 校验是否有子产品分类
        if (toolingCategoryMapper.selectCountByParentId(id) > 0) {
            throw exception(TOOLING_CATEGORY_EXITS_CHILDREN);
        }
        // 1.3 校验是否有产品
//        if (toolingCategoryMapper.getProductCountByCategoryId(id) > 0) {
//            throw exception(PRODUCT_CATEGORY_EXITS_PRODUCT);
//        }
        // 2. 删除
        toolingCategoryMapper.deleteById(id);
        ToolingCategoryDO toolingCategoryDO = toolingCategoryMapper.selectById(id);
        // 记录日志
        LogRecordContext.putVariable("toolingCategoryName", toolingCategoryDO.getName());
    }

    private void validateToolingCategoryNameUnique(Long id, Long parentId, String name) {
        ToolingCategoryDO toolingCategoryDO = toolingCategoryMapper.selectByParentIdAndName(parentId, name);
        if (toolingCategoryDO == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的产品分类
        if (id == null) {
            throw exception(TOOLING_CATEGORY_NAME_DUPLICATE);
        }
        if (!Objects.equals(toolingCategoryDO.getId(), id)) {
            throw exception(TOOLING_CATEGORY_NAME_DUPLICATE);
        }
    }
    private void validateParentToolingCategory(Long id, Long parentId) {
        if (parentId == null || ToolingCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
            return;
        }
        // 1. 不能设置自己为父产品分类
        if (Objects.equals(id, parentId)) {
            throw exception(TOOLING_CATEGORY_PARENT_ERROR);
        }
        // 2. 父产品分类不存在
        ToolingCategoryDO parentCategory = toolingCategoryMapper.selectById(parentId);
        if (parentCategory == null) {
            throw exception(TOOLING_CATEGORY_PARENT_NOT_EXITS);
        }
        // 3. 递归校验父产品分类，如果父产品分类是自己的子产品分类，则报错，避免形成环路
        if (id == null) { // id 为空，说明新增，不需要考虑环路
            return;
        }
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            // 3.1 校验环路
            parentId = parentCategory.getParentId();
            if (Objects.equals(id, parentId)) {
                throw exception(TOOLING_CATEGORY_PARENT_IS_CHILD);
            }
            // 3.2 继续递归下一级父产品分类
            if (parentId == null || ToolingCategoryDO.PARENT_ID_ROOT.equals(parentId)) {
                break;
            }
            parentCategory = toolingCategoryMapper.selectById(parentId);
            if (parentCategory == null) {
                break;
            }
        }
    }
    private void validateToolingCategoryExists(Long id) {
        if (toolingCategoryMapper.selectById(id) == null) {
            throw exception(TOOLING_CATEGORY_NOT_EXISTS);
        }
    }

    @Override
    public ToolingCategoryDO getToolingCategory(Long id) {
        return toolingCategoryMapper.selectById(id);
    }

    @Override
    public PageResult<ToolingCategoryDO> getToolingCategoryPage(ToolingCategoryPageReqVO pageReqVO) {
        return toolingCategoryMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ToolingCategoryDO> getProductCategoryList(ToolingCategoryListReqVO listReqVO) {
        return toolingCategoryMapper.selectList(listReqVO);
    }

//    @Override
//    public Long getProductCountByCategoryId(Long categoryId) {
//        return toolingCategoryMapper.selectCountByCategoryId(categoryId);
//    }

}