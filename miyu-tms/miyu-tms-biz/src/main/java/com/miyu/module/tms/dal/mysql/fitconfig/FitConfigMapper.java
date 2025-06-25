package com.miyu.module.tms.dal.mysql.fitconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.fitconfig.vo.*;

/**
 * 刀具适配 Mapper
 *
 * @author zhangyunfei
 */
@Mapper
public interface FitConfigMapper extends BaseMapperX<FitConfigDO> {

    default PageResult<FitConfigDO> selectPage(FitConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<FitConfigDO>()
                .eqIfPresent(FitConfigDO::getToolConfigId, reqVO.getToolConfigId())
                .eqIfPresent(FitConfigDO::getFitToolConfigId, reqVO.getFitToolConfigId())
                .orderByDesc(FitConfigDO::getId));
    }


    default List<FitConfigDO> selectFitConfigList(String toolConfigId){
        MPJLambdaWrapperX<FitConfigDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolConfigDO.class, ToolConfigDO::getId, FitConfigDO::getFitToolConfigId)
                .selectAs(ToolConfigDO::getToolName, FitConfigDO::getToolName)
                .selectAs(ToolConfigDO::getMaterialTypeCode, FitConfigDO::getMaterialTypeCode)
                .selectAs(ToolConfigDO::getToolModel, FitConfigDO::getToolModel)
                .selectAll(FitConfigDO.class)
                .eq(FitConfigDO::getToolConfigId, toolConfigId);

        return selectList(wrapperX);
    }
}
