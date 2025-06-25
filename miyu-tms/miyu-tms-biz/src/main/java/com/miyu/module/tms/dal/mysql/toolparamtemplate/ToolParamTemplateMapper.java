package com.miyu.module.tms.dal.mysql.toolparamtemplate;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import com.miyu.module.tms.dal.dataobject.toolparamtemplate.ToolParamTemplateDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.tms.controller.admin.toolparamtemplate.vo.*;

/**
 * 刀具参数模板 Mapper
 *
 * @author zhangyunfei
 */
@Mapper
public interface ToolParamTemplateMapper extends BaseMapperX<ToolParamTemplateDO> {

    default PageResult<ToolParamTemplateDO> selectPage(ToolParamTemplatePageReqVO reqVO) {

        MPJLambdaWrapperX<ToolParamTemplateDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ToolConfigDO.class, ToolConfigDO::getId, ToolParamTemplateDO::getToolConfigId)
                .selectAs(ToolConfigDO:: getMaterialTypeName, ToolParamTemplateDO::getMaterialTypeName)
                .selectAll(ToolParamTemplateDO.class);

        wrapperX.like(reqVO.getTemplateName() != null, ToolParamTemplateDO::getTemplateName, reqVO.getTemplateName())
                .between(reqVO.getCreateTime() != null, ToolParamTemplateDO::getCreateTime, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[0] : null, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[1] : null)
                .eq(reqVO.getMaterialTypeId() != null, ToolParamTemplateDO::getMaterialTypeId, reqVO.getMaterialTypeId())
                .eq(reqVO.getToolTypeCode() != null, ToolParamTemplateDO::getToolTypeCode, reqVO.getToolTypeCode())
                .like(reqVO.getToolNumber() != null, ToolParamTemplateDO::getToolNumber, reqVO.getToolNumber())
                .orderByDesc(ToolParamTemplateDO::getId);

        return selectPage(reqVO, wrapperX);
    }

}
