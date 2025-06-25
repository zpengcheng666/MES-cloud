package com.miyu.module.tms.service.toolconfigparameter;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.module.tms.controller.admin.toolconfigparameter.vo.*;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.toolconfigparameter.ToolConfigParameterMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具参数信息 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ToolConfigParameterServiceImpl implements ToolConfigParameterService {

    @Resource
    private ToolConfigParameterMapper toolConfigParameterMapper;

    @Override
    public String createToolConfigParameter(ToolConfigParameterSaveReqVO createReqVO) {
        // 插入
        ToolConfigParameterDO toolConfigParameter = BeanUtils.toBean(createReqVO, ToolConfigParameterDO.class);
        toolConfigParameterMapper.insert(toolConfigParameter);
        // 返回
        return toolConfigParameter.getId();
    }

    @Override
    public void updateToolConfigParameter(ToolConfigParameterSaveReqVO updateReqVO) {
        // 校验存在
        validateToolConfigParameterExists(updateReqVO.getId());
        // 更新
        ToolConfigParameterDO updateObj = BeanUtils.toBean(updateReqVO, ToolConfigParameterDO.class);
        toolConfigParameterMapper.updateById(updateObj);
    }

    @Override
    public void deleteToolConfigParameter(Long id) {
        // 校验存在
        validateToolConfigParameterExists(id);
        // 删除
        toolConfigParameterMapper.deleteById(id);
    }

    private void validateToolConfigParameterExists(Long id) {
        if (toolConfigParameterMapper.selectById(id) == null) {
            throw exception(TOOL_CONFIG_PARAMETER_NOT_EXISTS);
        }
    }

    @Override
    public ToolConfigParameterDO getToolConfigParameter(Long id) {
        return toolConfigParameterMapper.selectById(id);
    }

    @Override
    public PageResult<ToolConfigParameterDO> getToolConfigParameterPage(ToolConfigParameterPageReqVO pageReqVO) {
        return toolConfigParameterMapper.selectPage(pageReqVO);
    }

}
