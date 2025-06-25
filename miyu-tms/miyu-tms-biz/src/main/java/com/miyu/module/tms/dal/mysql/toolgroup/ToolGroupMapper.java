package com.miyu.module.tms.dal.mysql.toolgroup;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import com.miyu.module.tms.dal.dataobject.toolgroup.ToolGroupDO;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolgroup.vo.*;

/**
 * 刀具组装 Mapper
 *
 * @author zhangyunfei
 */
@Mapper
public interface ToolGroupMapper extends BaseMapperX<ToolGroupDO> {

    default PageResult<ToolGroupDO> selectPage(ToolGroupPageReqVO reqVO) {

        MPJLambdaWrapperX<ToolGroupDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolConfigDO.class, ToolConfigDO::getId, ToolGroupDO::getMainConfigId)
                .selectAs(ToolConfigDO:: getToolName, ToolGroupDO::getMaterialName)
                .selectAs(ToolConfigDO:: getMaterialNumber, ToolGroupDO::getMaterialNumber)
                .selectAs(ToolConfigDO:: getMaterialTypeName, ToolGroupDO::getMaterialType)
                .selectAs(ToolConfigDO:: getMaterialTypeCode, ToolGroupDO::getMaterialCode)
                .selectAs(ToolConfigDO:: getToolModel, ToolGroupDO::getToolModel)
                .selectAs(ToolConfigDO:: getToolWeight, ToolGroupDO::getToolWeight)
                .selectAs(ToolConfigDO:: getToolTexture, ToolGroupDO::getToolTexture)
                .selectAs(ToolConfigDO:: getToolCoating, ToolGroupDO::getToolCoating)
                .selectAll(ToolGroupDO.class)
                .like(reqVO.getMaterialName() != null, ToolConfigDO::getToolName, reqVO.getMaterialName())
                .eq(reqVO.getMaterialNumber() != null, ToolConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .orderByDesc(ToolGroupDO::getId);

        return selectPage(reqVO, wrapperX);
    }

    default ToolGroupDO getToolGroupById(String id) {
        MPJLambdaWrapperX<ToolGroupDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolConfigDO.class, ToolConfigDO::getId, ToolGroupDO::getMainConfigId)
                .selectAs(ToolConfigDO:: getToolName, ToolGroupDO::getMaterialName)
                .selectAs(ToolConfigDO:: getMaterialNumber, ToolGroupDO::getMaterialNumber)
                .selectAs(ToolConfigDO:: getMaterialTypeName, ToolGroupDO::getMaterialType)
                .selectAs(ToolConfigDO:: getMaterialTypeCode, ToolGroupDO::getMaterialCode)
                .selectAs(ToolConfigDO:: getToolModel, ToolGroupDetailDO::getToolModel)
                .selectAll(ToolGroupDO.class)
                .eq(ToolGroupDO::getId, id);

        return selectOne(wrapperX);
    }
}
