package com.miyu.module.tms.service.toolparamtemplate;

import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolparamtemplate.vo.*;
import com.miyu.module.tms.dal.dataobject.toolparamtemplate.ToolParamTemplateDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;

import java.util.List;

/**
 * 刀具参数模板 Service 接口
 *
 * @author zhangyunfei
 */
public interface ToolParamTemplateService {

    /**
     * 创建刀具参数模板
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolParamTemplate(@Valid ToolParamTemplateSaveReqVO createReqVO);

    /**
     * 更新刀具参数模板
     *
     * @param updateReqVO 更新信息
     */
    void updateToolParamTemplate(@Valid ToolParamTemplateSaveReqVO updateReqVO);

    /**
     * 删除刀具参数模板
     *
     * @param id 编号
     */
    void deleteToolParamTemplate(String id);

    /**
     * 获得刀具参数模板
     *
     * @param id 编号
     * @return 刀具参数模板
     */
    ToolParamTemplateDO getToolParamTemplate(String id);

    /**
     * 获得刀具参数模板分页
     *
     * @param pageReqVO 分页查询
     * @return 刀具参数模板分页
     */
    PageResult<ToolParamTemplateDO> getToolParamTemplatePage(ToolParamTemplatePageReqVO pageReqVO);

    /**
     * 物料类别ID获得刀具参数模板
     * @param materialTypeId
     * @return
     */
    List<ToolParamTemplateDetailDO> getToolParamTemplateByMaterialTypeId(String materialTypeId);
}
