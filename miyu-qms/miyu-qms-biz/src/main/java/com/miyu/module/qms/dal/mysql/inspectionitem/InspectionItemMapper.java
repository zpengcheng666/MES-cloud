package com.miyu.module.qms.dal.mysql.inspectionitem;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.qms.controller.admin.inspectionitem.vo.*;

/**
 * 检测项目 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface InspectionItemMapper extends BaseMapperX<InspectionItemDO> {

    default PageResult<InspectionItemDO> selectPage(InspectionItemPageReqVO reqVO) {

        MPJLambdaWrapperX<InspectionItemDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(InspectionItemTypeDO.class, InspectionItemTypeDO::getId, InspectionItemDO::getItemTypeId)
                .leftJoin(InspectionToolDO.class, InspectionToolDO::getId, InspectionItemDO::getInspectionToolId)
                .eq(reqVO.getMaterialConfigId() != null, InspectionToolDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .selectAs(InspectionItemTypeDO::getName, InspectionItemDO::getItemTypeName)
                .selectAs(InspectionToolDO::getName, InspectionItemDO::getInspectionToolName)
                .selectAs(InspectionToolDO::getMaterialConfigId, InspectionItemDO::getMaterialConfigId)
                .selectAll(InspectionItemDO.class);

        return selectPage(reqVO, wrapperX.betweenIfPresent(InspectionItemDO::getCreateTime, reqVO.getCreateTime())
                .likeIfPresent(InspectionItemDO::getItemName, reqVO.getItemName())
                .likeIfPresent(InspectionItemDO::getItemNo, reqVO.getItemNo())
                .eqIfPresent(InspectionItemDO::getContent, reqVO.getContent())
                .eqIfPresent(InspectionItemDO::getInspectionType, reqVO.getInspectionType())
                .eqIfPresent(InspectionItemDO::getInspectionToolId, reqVO.getInspectionToolId())
                .eqIfPresent(InspectionItemDO::getItemTypeId, reqVO.getItemTypeId())
                .orderByDesc(InspectionItemDO::getId));
    }


    default List<InspectionItemDO> selectListByTypeId(String inspectionItemTypeId) {
        return selectList(InspectionItemDO::getItemTypeId, inspectionItemTypeId);
    }

}