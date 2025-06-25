package com.miyu.module.qms.dal.mysql.inspectionschemeitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionschemeitem.vo.*;

/**
 * 检验方案检测项目详情 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InspectionSchemeItemMapper extends BaseMapperX<InspectionSchemeItemDO> {

    default PageResult<InspectionSchemeItemDO> selectPage(InspectionSchemeItemPageReqVO reqVO) {

        MPJLambdaWrapperX<InspectionSchemeItemDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSchemeDO.class, InspectionSchemeDO::getId, InspectionSchemeItemDO::getInspectionSchemeId)
                .leftJoin(InspectionItemDO.class, InspectionItemDO::getId, InspectionSchemeItemDO::getInspectionItemId)
                .leftJoin(InspectionItemTypeDO.class, InspectionItemTypeDO::getId, InspectionItemDO::getItemTypeId)
                .leftJoin(InspectionToolDO.class, InspectionToolDO::getId, InspectionItemDO::getInspectionToolId)
                .selectAs(InspectionSchemeDO::getSchemeName, InspectionSchemeItemDO::getInspectionSchemeName)
                .selectAs(InspectionItemDO::getItemName, InspectionSchemeItemDO::getInspectionItemName)
                .selectAs(InspectionItemTypeDO::getName, InspectionSchemeItemDO::getItemTypeName)
                .selectAs(InspectionToolDO::getName, InspectionSchemeItemDO::getInspectionToolName)
                .selectAll(InspectionSchemeItemDO.class);


        return selectPage(reqVO, wrapperX
                .betweenIfPresent(InspectionSchemeItemDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(InspectionSchemeItemDO::getInspectionSchemeId, reqVO.getInspectionSchemeId())
                .eqIfPresent(InspectionSchemeItemDO::getInspectionItemId, reqVO.getInspectionItemId())
                .eqIfPresent(InspectionSchemeItemDO::getNumber, reqVO.getNumber())
                .orderByAsc(InspectionSchemeItemDO::getNumber));
    }


    default List<InspectionSchemeItemDO> selectListByInspectionSchemeId(String inspectionSchemeId) {

        MPJLambdaWrapperX<InspectionSchemeItemDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSchemeDO.class, InspectionSchemeDO::getId, InspectionSchemeItemDO::getInspectionSchemeId)
                .leftJoin(InspectionItemDO.class, InspectionItemDO::getId, InspectionSchemeItemDO::getInspectionItemId)
                .leftJoin(InspectionItemTypeDO.class, InspectionItemTypeDO::getId, InspectionItemDO::getItemTypeId)
                .selectAs(InspectionSchemeDO::getSchemeName, InspectionSchemeItemDO::getInspectionSchemeName)
                .selectAs(InspectionItemDO::getItemName, InspectionSchemeItemDO::getInspectionItemName)
                .selectAs(InspectionItemTypeDO::getName, InspectionSchemeItemDO::getItemTypeName)
                .selectAs(InspectionItemTypeDO::getId, InspectionSchemeItemDO::getItemTypeId)
                .selectAll(InspectionSchemeItemDO.class);
        wrapperX.eq(InspectionSchemeItemDO::getInspectionSchemeId, inspectionSchemeId).orderByAsc(InspectionSchemeItemDO::getNumber);

        return selectList(wrapperX);
    }


    default InspectionSchemeItemDO getInspectionSchemeItem(String id) {
        MPJLambdaWrapperX<InspectionSchemeItemDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionSchemeDO.class, InspectionSchemeDO::getId, InspectionSchemeItemDO::getInspectionSchemeId)
                .leftJoin(InspectionItemDO.class, InspectionItemDO::getId, InspectionSchemeItemDO::getInspectionItemId)
                .selectAs(InspectionSchemeDO::getSchemeName, InspectionSchemeItemDO::getInspectionSchemeName)
                .selectAs(InspectionItemDO::getItemName, InspectionSchemeItemDO::getInspectionItemName)
                .selectAll(InspectionSchemeItemDO.class);

        wrapperX.eq(InspectionSchemeItemDO::getId,id);
        return selectOne(wrapperX);
    }

    default int deleteByInspectionSchemeId(String inspectionSchemeId) {
        return delete(InspectionSchemeItemDO::getInspectionSchemeId, inspectionSchemeId);
    }


}
