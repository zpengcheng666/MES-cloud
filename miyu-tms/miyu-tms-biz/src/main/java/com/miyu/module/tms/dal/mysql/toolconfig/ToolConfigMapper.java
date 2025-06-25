package com.miyu.module.tms.dal.mysql.toolconfig;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolconfig.vo.*;

/**
 * 刀具类型 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface ToolConfigMapper extends BaseMapperX<ToolConfigDO> {

    default PageResult<ToolConfigDO> selectPage(ToolConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolConfigDO>()
                .betweenIfPresent(ToolConfigDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ToolConfigDO::getMaterialConfigId, reqVO.getMaterialConfigId())
                .eqIfPresent(ToolConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(ToolConfigDO::getToolType, reqVO.getToolType())
                .eqIfPresent(ToolConfigDO::getMaterialTypeId, reqVO.getMaterialTypeId())
                .eqIfPresent(ToolConfigDO::getMaterialTypeCode, reqVO.getMaterialTypeCode())
                .likeIfPresent(ToolConfigDO::getMaterialTypeName, reqVO.getMaterialTypeName())
                .likeIfPresent(ToolConfigDO::getToolName, reqVO.getToolName())
                .likeIfPresent(ToolConfigDO::getToolModel, reqVO.getToolModel())
                .eqIfPresent(ToolConfigDO::getToolWeight, reqVO.getToolWeight())
                .eqIfPresent(ToolConfigDO::getToolTexture, reqVO.getToolTexture())
                .eqIfPresent(ToolConfigDO::getToolCoating, reqVO.getToolCoating())
                .eqIfPresent(ToolConfigDO::getRatedLife, reqVO.getRatedLife())
                .eqIfPresent(ToolConfigDO::getStatus, reqVO.getStatus())
                .orderByDesc(ToolConfigDO::getId));
    }

    default PageResult<ToolConfigDO> getFitToolConfigPageByType(ToolConfigPageReqVO reqVO) {

        MPJLambdaWrapperX<ToolConfigDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(reqVO.getMaterialNumber() != null, ToolConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eq(reqVO.getMaterialTypeCode() != null, ToolConfigDO::getMaterialTypeCode, reqVO.getMaterialTypeCode())
                .eq(reqVO.getToolName() != null, ToolConfigDO::getToolName, reqVO.getToolName());

        if(reqVO.getQueryType() != null){
            // queryType=1（刀柄） 适配刀具
            if(reqVO.getQueryType() == 1){
                wrapperX.in(ToolConfigDO::getMaterialTypeCode, Arrays.asList("DT", "DP"));
            }
            // （刀具）刀头、刀片 适配 刀柄、配件
            else if(reqVO.getQueryType() == 2){
                wrapperX.in(ToolConfigDO::getMaterialTypeCode, Arrays.asList("DB", "PJ"));
            }
            // 配件 适配 刀具（刀头、刀片）
            else if(reqVO.getQueryType() == 3){
                wrapperX.in(ToolConfigDO::getMaterialTypeCode, Arrays.asList("DT", "DP"));
            }
        }
        wrapperX.orderByDesc(ToolConfigDO::getCreateTime);
        return selectPage(reqVO, wrapperX);
    }

    default PageResult<ToolConfigDO> getToolConfigPageByType(ToolConfigPageReqVO reqVO) {

        MPJLambdaWrapperX<ToolConfigDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eq(reqVO.getMaterialNumber() != null, ToolConfigDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eq(reqVO.getMaterialTypeCode() != null, ToolConfigDO::getMaterialTypeCode, reqVO.getMaterialTypeCode())
                .eq(reqVO.getToolName() != null, ToolConfigDO::getToolName, reqVO.getToolName());

        if(reqVO.getQueryType() != null){
            // queryType=1 刀柄
            if(reqVO.getQueryType() == 1){
                wrapperX.in(ToolConfigDO::getMaterialTypeCode, "DB");
            }
            // （刀具）刀头、刀片
            else if(reqVO.getQueryType() == 2){
                wrapperX.in(ToolConfigDO::getMaterialTypeCode, Arrays.asList("DT", "DP"));
            }
            // 配件
            else if(reqVO.getQueryType() == 3){
                wrapperX.eq(ToolConfigDO::getMaterialTypeCode, "PJ");
            }

            // 存在适配关系
            if(reqVO.getFitConfigIds() != null && reqVO.getFitConfigIds().size() > 0 ) {
                wrapperX.in(ToolConfigDO::getId, reqVO.getFitConfigIds());
            }
        }
        wrapperX.orderByDesc(ToolConfigDO::getCreateTime);
        return selectPage(reqVO, wrapperX);
    }
}
