package com.miyu.module.tms.service.fitconfig;

import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import com.miyu.module.tms.controller.admin.fitconfig.vo.*;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.tms.dal.mysql.fitconfig.FitConfigMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.tms.enums.ErrorCodeConstants.*;

/**
 * 刀具适配 Service 实现类
 *
 * @author zhangyunfei
 */
@Service
@Validated
public class FitConfigServiceImpl implements FitConfigService {

    @Resource
    private FitConfigMapper fitConfigMapper;

    @Override
    public String createFitConfig(FitConfigSaveReqVO createReqVO) {
        // 插入
        FitConfigDO fitConfig = BeanUtils.toBean(createReqVO, FitConfigDO.class);
        fitConfigMapper.insert(fitConfig);
        // 返回
        return fitConfig.getId();
    }

    @Override
    public void updateFitConfig(FitConfigSaveReqVO updateReqVO) {
        // 校验存在
        validateFitConfigExists(updateReqVO.getId());
        // 更新
        FitConfigDO updateObj = BeanUtils.toBean(updateReqVO, FitConfigDO.class);
        fitConfigMapper.updateById(updateObj);
    }

    @Override
    public void deleteFitConfig(String id) {
        // 校验存在
        validateFitConfigExists(id);
        // 删除
        fitConfigMapper.deleteById(id);
    }

    private void validateFitConfigExists(String id) {
        if (fitConfigMapper.selectById(id) == null) {
            throw exception(FIT_CONFIG_NOT_EXISTS);
        }
    }

    @Override
    public FitConfigDO getFitConfig(String id) {
        return fitConfigMapper.selectById(id);
    }

    @Override
    public PageResult<FitConfigDO> getFitConfigPage(FitConfigPageReqVO pageReqVO) {
        return fitConfigMapper.selectPage(pageReqVO);
    }
}
