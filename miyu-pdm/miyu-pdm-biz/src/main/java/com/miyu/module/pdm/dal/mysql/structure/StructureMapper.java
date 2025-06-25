package com.miyu.module.pdm.dal.mysql.structure;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.structureDefinition.vo.StructureListReqVO;
import com.miyu.module.pdm.dal.dataobject.product.ProductCategoryDO;
import com.miyu.module.pdm.dal.dataobject.structure.StructureDO;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface StructureMapper extends BaseMapperX<StructureDO> {

    default List<StructureDO> selectList(StructureListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StructureDO>()
                .likeIfPresent(StructureDO::getName, reqVO.getName())
                .eqIfPresent(StructureDO::getStatus, reqVO.getStatus())
                .eqIfPresent(StructureDO::getType, reqVO.getType())
                .eqIfPresent(StructureDO::getId, reqVO.getId()));
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(StructureDO::getParentId, parentId);
    }

    default List<StructureDO> selectChildList(StructureListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StructureDO>()
                .eqIfPresent(StructureDO::getParentId, reqVO.getId()));
    }
}
