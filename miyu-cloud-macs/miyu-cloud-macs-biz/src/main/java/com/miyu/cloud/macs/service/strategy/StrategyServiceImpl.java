package com.miyu.cloud.macs.service.strategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.strategy.vo.*;
import com.miyu.cloud.macs.dal.dataobject.strategy.StrategyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.strategy.StrategyMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 策略 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class StrategyServiceImpl implements StrategyService {

    @Resource
    private StrategyMapper strategyMapper;

    @Override
    public String createStrategy(StrategySaveReqVO createReqVO) {
        // 插入
        StrategyDO strategy = BeanUtils.toBean(createReqVO, StrategyDO.class);
        strategyMapper.insert(strategy);
        // 返回
        return strategy.getId();
    }

    @Override
    public void updateStrategy(StrategySaveReqVO updateReqVO) {
        // 校验存在
        validateStrategyExists(updateReqVO.getId());
        // 更新
        StrategyDO updateObj = BeanUtils.toBean(updateReqVO, StrategyDO.class);
        strategyMapper.updateById(updateObj);
    }

    @Override
    public void deleteStrategy(String id) {
        // 校验存在
        validateStrategyExists(id);
        // 删除
        strategyMapper.deleteById(id);
    }

    private void validateStrategyExists(String id) {
        if (strategyMapper.selectById(id) == null) {
            throw exception(STRATEGY_NOT_EXISTS);
        }
    }

    @Override
    public StrategyDO getStrategy(String id) {
        return strategyMapper.selectById(id);
    }

    @Override
    public PageResult<StrategyDO> getStrategyPage(StrategyPageReqVO pageReqVO) {
        return strategyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<StrategyDO> list(QueryWrapper<StrategyDO> queryWrapper) {
        return strategyMapper.selectList(queryWrapper);
    }
}