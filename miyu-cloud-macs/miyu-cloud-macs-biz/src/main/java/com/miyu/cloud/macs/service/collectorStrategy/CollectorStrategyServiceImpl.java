package com.miyu.cloud.macs.service.collectorStrategy;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.collectorStrategy.vo.*;
import com.miyu.cloud.macs.dal.dataobject.collectorStrategy.CollectorStrategyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.collectorStrategy.CollectorStrategyMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * 设备策略 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class CollectorStrategyServiceImpl implements CollectorStrategyService {

    @Resource
    private CollectorStrategyMapper collectorStrategyMapper;

    @Override
    public String createCollectorStrategy(CollectorStrategySaveReqVO createReqVO) {
        // 插入
        CollectorStrategyDO collectorStrategy = BeanUtils.toBean(createReqVO, CollectorStrategyDO.class);
        collectorStrategyMapper.insert(collectorStrategy);
        // 返回
        return collectorStrategy.getId();
    }

    @Override
    public void updateCollectorStrategy(CollectorStrategySaveReqVO updateReqVO) {
        // 校验存在
        validateCollectorStrategyExists(updateReqVO.getId());
        // 更新
        CollectorStrategyDO updateObj = BeanUtils.toBean(updateReqVO, CollectorStrategyDO.class);
        collectorStrategyMapper.updateById(updateObj);
    }

    @Override
    public void deleteCollectorStrategy(String id) {
        // 校验存在
        validateCollectorStrategyExists(id);
        // 删除
        collectorStrategyMapper.deleteById(id);
    }

    private void validateCollectorStrategyExists(String id) {
        if (collectorStrategyMapper.selectById(id) == null) {
            throw exception(COLLECTOR_STRATEGY_NOT_EXISTS);
        }
    }

    @Override
    public CollectorStrategyDO getCollectorStrategy(String id) {
        return collectorStrategyMapper.selectById(id);
    }

    @Override
    public PageResult<CollectorStrategyDO> getCollectorStrategyPage(CollectorStrategyPageReqVO pageReqVO) {
        return collectorStrategyMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CollectorStrategyDO> list(QueryWrapper<CollectorStrategyDO> queryWrapper) {
        return collectorStrategyMapper.selectList(queryWrapper);
    }
}