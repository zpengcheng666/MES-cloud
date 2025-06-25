package com.miyu.module.tms.dal.mysql.toolgroupdetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.*;

/**
 * 刀具组装 Mapper
 *
 * @author zhangyunfei
 */
@Mapper
public interface ToolGroupDetailMapper extends BaseMapperX<ToolGroupDetailDO> {

    default PageResult<ToolGroupDetailDO> selectPage(ToolGroupDetailPageReqVO reqVO) {
        MPJLambdaWrapperX<ToolGroupDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolConfigDO.class, ToolConfigDO::getId, ToolGroupDetailDO::getAccessoryConfigId)
                .selectAs(ToolConfigDO:: getToolName, ToolGroupDetailDO::getMaterialName)
                .selectAs(ToolConfigDO:: getMaterialNumber, ToolGroupDetailDO::getMaterialNumber)
                .selectAs(ToolConfigDO:: getMaterialTypeName, ToolGroupDetailDO::getMaterialType)
                .selectAs(ToolConfigDO:: getMaterialTypeCode, ToolGroupDetailDO::getMaterialCode)
                .selectAs(ToolConfigDO:: getToolModel, ToolGroupDetailDO::getToolModel)
                .selectAs(ToolConfigDO:: getToolWeight, ToolGroupDetailDO::getToolWeight)
                .selectAs(ToolConfigDO:: getToolTexture, ToolGroupDetailDO::getToolTexture)
                .selectAs(ToolConfigDO:: getToolCoating, ToolGroupDetailDO::getToolCoating)
                .selectAll(ToolGroupDetailDO.class)
                .eq(ToolGroupDetailDO::getToolGroupId, reqVO.getToolGroupId())
                .orderByDesc(ToolGroupDetailDO::getId);
        return selectPage(reqVO, wrapperX);
    }


    default List<ToolGroupDetailDO> selectToolGroupDetailList(ToolGroupDetailPageReqVO reqVO){
        return selectList(new LambdaQueryWrapperX<ToolGroupDetailDO>()
                .betweenIfPresent(ToolGroupDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ToolGroupDetailDO::getMainConfigId, reqVO.getMainConfigId())
                .eqIfPresent(ToolGroupDetailDO::getSite, reqVO.getSite())
                .eqIfPresent(ToolGroupDetailDO::getAccessoryConfigId, reqVO.getAccessoryConfigId())
                .eqIfPresent(ToolGroupDetailDO::getCount, reqVO.getCount())
                .orderByDesc(ToolGroupDetailDO::getId));
    }

    default List<ToolGroupDetailDO> getToolGroupDetailListByGroupId(String groupId) {
        MPJLambdaWrapperX<ToolGroupDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolConfigDO.class, ToolConfigDO::getId, ToolGroupDetailDO::getAccessoryConfigId)
                .selectAs(ToolConfigDO:: getToolName, ToolGroupDetailDO::getMaterialName)
                .selectAs(ToolConfigDO:: getMaterialNumber, ToolGroupDetailDO::getMaterialNumber)
                .selectAs(ToolConfigDO:: getMaterialTypeName, ToolGroupDetailDO::getMaterialType)
                .selectAs(ToolConfigDO:: getMaterialTypeCode, ToolGroupDetailDO::getMaterialCode)
                .selectAs(ToolConfigDO:: getToolModel, ToolGroupDetailDO::getToolModel)
                .selectAll(ToolGroupDetailDO.class)
                .eq(ToolGroupDetailDO::getToolGroupId, groupId);

        return selectList(wrapperX);
    }
}
