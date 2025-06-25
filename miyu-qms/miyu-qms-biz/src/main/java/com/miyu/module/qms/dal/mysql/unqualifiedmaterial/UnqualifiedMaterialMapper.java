package com.miyu.module.qms.dal.mysql.unqualifiedmaterial;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo.*;

import java.util.List;

/**
 * 不合格品产品 Mapper
 *
 * @author Zhangyunfei
 */
@Mapper
public interface UnqualifiedMaterialMapper extends BaseMapperX<UnqualifiedMaterialDO> {

    default PageResult<UnqualifiedMaterialDO> selectPage(UnqualifiedMaterialPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<UnqualifiedMaterialDO>()
                .betweenIfPresent(UnqualifiedMaterialDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(UnqualifiedMaterialDO::getInspectionSheetSchemeId, reqVO.getInspectionSheetSchemeId())
                .eqIfPresent(UnqualifiedMaterialDO::getSchemeMaterialId, reqVO.getSchemeMaterialId())
                .orderByDesc(UnqualifiedMaterialDO::getId));
    }

    default List<UnqualifiedMaterialDO> selectListBySheetSchemeId(String id) {
        MPJLambdaWrapperX<UnqualifiedMaterialDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.select("(select group_concat(t1.defective_code) from qms_unqualified_registration t1 where t1.unqualified_material_id = t.id  and t1.deleted = 0 ) as defectiveCodesStr")
                .selectAll(UnqualifiedMaterialDO.class);
        return selectList(wrapperX.eq(UnqualifiedMaterialDO::getInspectionSheetSchemeId, id));
    }


    default List<UnqualifiedMaterialDO> getUnqualifiedMaterialListByMaterialIds(List<String> materialIds) {
        MPJLambdaWrapperX<UnqualifiedMaterialDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSheetSchemeMaterialDO.class, InspectionSheetSchemeMaterialDO::getId, UnqualifiedMaterialDO::getSchemeMaterialId)
                .in(InspectionSheetSchemeMaterialDO::getMaterialId, materialIds)
                .selectAs(InspectionSheetSchemeMaterialDO::getMaterialId, UnqualifiedMaterialDO::getMaterialId)
                .selectAs(InspectionSheetSchemeMaterialDO::getBarCode, UnqualifiedMaterialDO::getBarCode)
                .selectAll(UnqualifiedMaterialDO.class);
        return selectList(wrapperX);
    }
}
