package com.miyu.module.pdm.dal.mysql.material;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.material.vo.MaterialListReqVO;
import com.miyu.module.pdm.dal.dataobject.material.MaterialDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

/**
 * PDM 工装-临时 Mapper
 *
 * @author liuy
 */
@Mapper
public interface MaterialMapper extends BaseMapperX<MaterialDO> {

    default List<MaterialDO> selectList(MaterialListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<MaterialDO>()
                .likeIfPresent(MaterialDO::getMaterialNumber, reqVO.getMaterialNumber())
                .likeIfPresent(MaterialDO::getMaterialName, reqVO.getMaterialName())
                .eqIfPresent(MaterialDO::getMaterialProperty, reqVO.getMaterialProperty())
                .eqIfPresent(MaterialDO::getMaterialType, reqVO.getMaterialType())
                .orderByDesc(MaterialDO::getId));
    }

    default List<MaterialDO> selectListByMaterialIds(Collection<String> materialIds) {
        return selectList(MaterialDO::getId, materialIds);
    }
}
