package com.miyu.module.tms.dal.mysql.toolparamtemplatedetail;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo.*;

/**
 * 参数模版详情 Mapper
 *
 * @author zhangyunfei
 */
@Mapper
public interface ToolParamTemplateDetailMapper extends BaseMapperX<ToolParamTemplateDetailDO> {

    default PageResult<ToolParamTemplateDetailDO> selectPage(ToolParamTemplateDetailPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<ToolParamTemplateDetailDO>()
                .betweenIfPresent(ToolParamTemplateDetailDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(ToolParamTemplateDetailDO::getParamTemplateId, reqVO.getParamTemplateId())
                .likeIfPresent(ToolParamTemplateDetailDO::getName, reqVO.getName())
                .eqIfPresent(ToolParamTemplateDetailDO::getAbbr, reqVO.getAbbr())
                .eqIfPresent(ToolParamTemplateDetailDO::getUnit, reqVO.getUnit())
                .eqIfPresent(ToolParamTemplateDetailDO::getSort, reqVO.getSort())
                .eqIfPresent(ToolParamTemplateDetailDO::getType, reqVO.getType())
                .eqIfPresent(ToolParamTemplateDetailDO::getRequired, reqVO.getRequired())
                .eqIfPresent(ToolParamTemplateDetailDO::getDefaultValue, reqVO.getDefaultValue())
                .orderByDesc(ToolParamTemplateDetailDO::getId));
    }

}