package com.miyu.cloud.macs.service.collectorStrategy;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.controller.admin.collectorStrategy.vo.*;
import com.miyu.cloud.macs.dal.dataobject.collectorStrategy.CollectorStrategyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 设备策略 Service 接口
 *
 * @author miyu
 */
public interface CollectorStrategyService {

    /**
     * 创建设备策略
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCollectorStrategy(@Valid CollectorStrategySaveReqVO createReqVO);

    /**
     * 更新设备策略
     *
     * @param updateReqVO 更新信息
     */
    void updateCollectorStrategy(@Valid CollectorStrategySaveReqVO updateReqVO);

    /**
     * 删除设备策略
     *
     * @param id 编号
     */
    void deleteCollectorStrategy(String id);

    /**
     * 获得设备策略
     *
     * @param id 编号
     * @return 设备策略
     */
    CollectorStrategyDO getCollectorStrategy(String id);

    /**
     * 获得设备策略分页
     *
     * @param pageReqVO 分页查询
     * @return 设备策略分页
     */
    PageResult<CollectorStrategyDO> getCollectorStrategyPage(CollectorStrategyPageReqVO pageReqVO);

    List<CollectorStrategyDO> list(QueryWrapper<CollectorStrategyDO> queryWrapper);
}