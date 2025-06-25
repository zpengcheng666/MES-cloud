package com.miyu.module.pdm.dal.mysql.structure;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.pdm.dal.dataobject.structure.StructureExcelDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * excel规则详情 Mapper
 *
 * @author Liuy
 */
@Mapper
public interface StructureExcelMapper extends BaseMapperX<StructureExcelDO> {
    default List<StructureExcelDO> selectListByStructureId(Long structureId) {
        MPJLambdaWrapperX<StructureExcelDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.selectAll(StructureExcelDO.class)
                .eq(StructureExcelDO::getStructureId, structureId);
        return selectList(wrapperX);
    }

    default int deleteByStructureId(Long structureId) {
        return delete(StructureExcelDO::getStructureId, structureId);
    }
}
