package com.miyu.module.tms.service.toolparamtemplatedetail;

import java.util.*;
import javax.validation.*;
import com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo.*;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 参数模版详情 Service 接口
 *
 * @author zhangyunfei
 */
public interface ToolParamTemplateDetailService {

    /**
     * 创建参数模版详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createToolParamTemplateDetail(@Valid ToolParamTemplateDetailSaveReqVO createReqVO);

    /**
     * 更新参数模版详情
     *
     * @param updateReqVO 更新信息
     */
    void updateToolParamTemplateDetail(@Valid ToolParamTemplateDetailSaveReqVO updateReqVO);

    /**
     * 删除参数模版详情
     *
     * @param id 编号
     */
    void deleteToolParamTemplateDetail(String id);

    /**
     * 获得参数模版详情
     *
     * @param id 编号
     * @return 参数模版详情
     */
    ToolParamTemplateDetailDO getToolParamTemplateDetail(String id);

    /**
     * 获得参数模版详情分页
     *
     * @param pageReqVO 分页查询
     * @return 参数模版详情分页
     */
    PageResult<ToolParamTemplateDetailDO> getToolParamTemplateDetailPage(ToolParamTemplateDetailPageReqVO pageReqVO);

}