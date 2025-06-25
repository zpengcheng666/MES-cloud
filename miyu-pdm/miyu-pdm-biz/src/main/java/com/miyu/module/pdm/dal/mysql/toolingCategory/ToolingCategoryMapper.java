package com.miyu.module.pdm.dal.mysql.toolingCategory;


import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.productCategory.vo.ProductCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryListReqVO;
import com.miyu.module.pdm.controller.admin.toolingCategorys.vo.ToolingCategoryPageReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import com.miyu.module.pdm.dal.dataobject.toolingCategory.ToolingCategoryDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 产品分类信息 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface ToolingCategoryMapper extends BaseMapperX<ToolingCategoryDO> {

    default PageResult<ToolingCategoryDO> selectPage(ToolingCategoryPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolingCategoryDO>()
                .eqIfPresent(ToolingCategoryDO::getParentId, reqVO.getParentId())
                .likeIfPresent(ToolingCategoryDO::getName, reqVO.getName())
                .eqIfPresent(ToolingCategoryDO::getCode, reqVO.getCode())
                .eqIfPresent(ToolingCategoryDO::getSort, reqVO.getSort())
                .eqIfPresent(ToolingCategoryDO::getStatus, reqVO.getStatus())
                .betweenIfPresent(ToolingCategoryDO::getCreateTime, reqVO.getCreateTime())
                .orderByAsc(ToolingCategoryDO::getCreateTime));
    }
    default List<ToolingCategoryDO> selectList(ToolingCategoryListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ToolingCategoryDO>()
                .likeIfPresent(ToolingCategoryDO::getName, reqVO.getName())
                .eqIfPresent(ToolingCategoryDO::getStatus, reqVO.getStatus())
                .orderByDesc(ToolingCategoryDO::getId));
    }
    default ToolingCategoryDO selectByParentIdAndName(Long parentId, String name) {
        return selectOne(ToolingCategoryDO::getParentId, parentId, ToolingCategoryDO::getName, name);
    }
    default Long selectCountByParentId(Long parentId) {
        return selectCount(ToolingCategoryDO::getParentId, parentId);
    }
}