package com.miyu.module.pdm.service.toolingCategory;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryPageReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategorySaveReqVO;
import com.miyu.module.pdm.dal.dataobject.toolingCategory.ToolingCategoryDO;

import java.util.*;
import javax.validation.*;


/**
 * 产品分类信息 Service 接口
 *
 * @author 上海弥彧
 */
public interface ToolingCategoryService {

    /**
     * 创建产品分类信息
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    Long createToolingCategory(@Valid ToolingCategorySaveReqVO createReqVO);

    /**
     * 更新产品分类信息
     *
     * @param updateReqVO 更新信息
     */
    void updateToolingCategory(@Valid ToolingCategorySaveReqVO updateReqVO);

    /**
     * 删除产品分类信息
     *
     * @param id 编号
     */
    void deleteToolingCategory(Long id);

    /**
     * 获得产品分类信息
     *
     * @param id 编号
     * @return 产品分类信息
     */
    ToolingCategoryDO getToolingCategory(Long id);

    /**
     * 获得产品分类信息分页
     *
     * @param pageReqVO 分页查询
     * @return 产品分类信息分页
     */
    PageResult<ToolingCategoryDO> getToolingCategoryPage(ToolingCategoryPageReqVO pageReqVO);

    /**
     * 列表
     * @param listReqVO
     * @return
     */
    List<ToolingCategoryDO> getProductCategoryList(ToolingCategoryListReqVO listReqVO);

    /**
     *
     * @param categoryId
     * @return
     */
//    Long getProductCountByCategoryId(Long categoryId);
}