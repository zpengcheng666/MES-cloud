package com.miyu.cloud.macs.service.strategy;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.macs.controller.admin.strategy.vo.*;
import com.miyu.cloud.macs.dal.dataobject.strategy.StrategyDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 策略 Service 接口
 *
 * @author 芋道源码
 */
public interface StrategyService {

    /**
     * 创建策略
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createStrategy(@Valid StrategySaveReqVO createReqVO);

    /**
     * 更新策略
     *
     * @param updateReqVO 更新信息
     */
    void updateStrategy(@Valid StrategySaveReqVO updateReqVO);

    /**
     * 删除策略
     *
     * @param id 编号
     */
    void deleteStrategy(String id);

    /**
     * 获得策略
     *
     * @param id 编号
     * @return 策略
     */
    StrategyDO getStrategy(String id);

    /**
     * 获得策略分页
     *
     * @param pageReqVO 分页查询
     * @return 策略分页
     */
    PageResult<StrategyDO> getStrategyPage(StrategyPageReqVO pageReqVO);

    List<StrategyDO> list(QueryWrapper<StrategyDO> eq);
}