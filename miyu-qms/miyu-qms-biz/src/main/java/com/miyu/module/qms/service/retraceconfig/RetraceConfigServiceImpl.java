package com.miyu.module.qms.service.retraceconfig;

import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import java.util.*;
import com.miyu.module.qms.controller.admin.retraceconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.retraceconfig.RetraceConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.dal.mysql.retraceconfig.RetraceConfigMapper;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;
import static com.miyu.module.qms.enums.LogRecordConstants.*;

/**
 * 追溯字段配置 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class RetraceConfigServiceImpl implements RetraceConfigService {

    @Resource
    private RetraceConfigMapper retraceConfigMapper;

    @Override
    public String createRetraceConfig(RetraceConfigSaveReqVO createReqVO) {
        // 插入
        RetraceConfigDO retraceConfig = BeanUtils.toBean(createReqVO, RetraceConfigDO.class);
        retraceConfigMapper.insert(retraceConfig);
        // 返回
        return retraceConfig.getId();
    }

    @Override
    @LogRecord(type = QMS_RETRACE_CONFIG_TYPE, subType = QMS_UPDATE_RETRACE_CONFIG_SUB_TYPE, bizNo = "{{#config.id}}",
            success = QMS_UPDATE_RETRACE_CONFIG_SUCCESS)
    public void updateRetraceConfig(RetraceConfigSaveReqVO updateReqVO) {
        // 校验存在
        RetraceConfigDO config = validateRetraceConfigExists(updateReqVO.getId());
        // 更新
        RetraceConfigDO updateObj = BeanUtils.toBean(updateReqVO, RetraceConfigDO.class);
        retraceConfigMapper.updateById(updateObj);
        // 记录操作日志上下文
        LogRecordContext.putVariable("config", config);
    }

    @Override
    public void deleteRetraceConfig(String id) {
        // 校验存在
        validateRetraceConfigExists(id);
        // 删除
        retraceConfigMapper.deleteById(id);
    }

    private RetraceConfigDO validateRetraceConfigExists(String id) {
        RetraceConfigDO config = retraceConfigMapper.selectById(id);
        if (config == null) {
            throw exception(RETRACE_CONFIG_NOT_EXISTS);
        }
        return config;
    }

    @Override
    public RetraceConfigDO getRetraceConfig(String id) {
        return retraceConfigMapper.selectById(id);
    }

    @Override
    public PageResult<RetraceConfigDO> getRetraceConfigPage(RetraceConfigPageReqVO pageReqVO) {
        return retraceConfigMapper.selectPage(pageReqVO);
    }

    @Override
    public List<RetraceConfigDO> getRetraceConfigList() {
        return retraceConfigMapper.selectList();
    }

}
