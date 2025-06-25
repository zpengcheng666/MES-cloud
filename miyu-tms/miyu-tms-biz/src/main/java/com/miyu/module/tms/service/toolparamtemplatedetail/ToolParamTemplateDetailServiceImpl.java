package com.miyu.module.tms.service.toolparamtemplatedetail;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo.*;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolparamtemplatedetail.ToolParamTemplateDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 参数模版详情 Service 实现类
 *
 * @author zhangyunfei
 */
@Service
@Validated
public class ToolParamTemplateDetailServiceImpl implements ToolParamTemplateDetailService {

    @Resource
    private ToolParamTemplateDetailMapper toolParamTemplateDetailMapper;

    @Override
    public String createToolParamTemplateDetail(ToolParamTemplateDetailSaveReqVO createReqVO) {
        // 插入
        ToolParamTemplateDetailDO toolParamTemplateDetail = BeanUtils.toBean(createReqVO, ToolParamTemplateDetailDO.class);
        toolParamTemplateDetailMapper.insert(toolParamTemplateDetail);
        // 返回
        return toolParamTemplateDetail.getId();
    }

    @Override
    public void updateToolParamTemplateDetail(ToolParamTemplateDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateToolParamTemplateDetailExists(updateReqVO.getId());
        // 更新
        ToolParamTemplateDetailDO updateObj = BeanUtils.toBean(updateReqVO, ToolParamTemplateDetailDO.class);
        toolParamTemplateDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteToolParamTemplateDetail(String id) {
        // 校验存在
        validateToolParamTemplateDetailExists(id);
        // 删除
        toolParamTemplateDetailMapper.deleteById(id);
    }

    private void validateToolParamTemplateDetailExists(String id) {
        if (toolParamTemplateDetailMapper.selectById(id) == null) {
            throw exception(TOOL_PARAM_TEMPLATE_DETAIL_NOT_EXISTS);
        }
    }

    @Override
    public ToolParamTemplateDetailDO getToolParamTemplateDetail(String id) {
        return toolParamTemplateDetailMapper.selectById(id);
    }

    @Override
    public PageResult<ToolParamTemplateDetailDO> getToolParamTemplateDetailPage(ToolParamTemplateDetailPageReqVO pageReqVO) {
        return toolParamTemplateDetailMapper.selectPage(pageReqVO);
    }

}