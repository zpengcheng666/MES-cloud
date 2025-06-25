package com.miyu.cloud.macs.service.collector;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.miyu.cloud.macs.controller.admin.collector.vo.*;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * (通行卡,人脸,指纹)采集器 Service 接口
 *
 * @author 芋道源码
 */
public interface CollectorService {

    /**
     * 创建(通行卡,人脸,指纹)采集器
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCollector(@Valid CollectorSaveReqVO createReqVO);

    /**
     * 更新(通行卡,人脸,指纹)采集器
     *
     * @param updateReqVO 更新信息
     */
    void updateCollector(@Valid CollectorSaveReqVO updateReqVO);

    /**
     * 删除(通行卡,人脸,指纹)采集器
     *
     * @param id 编号
     */
    void deleteCollector(String id);

    /**
     * 获得(通行卡,人脸,指纹)采集器
     *
     * @param id 编号
     * @return (通行卡,人脸,指纹)采集器
     */
    CollectorDO getCollector(String id);

    /**
     * 获得(通行卡,人脸,指纹)采集器分页
     *
     * @param pageReqVO 分页查询
     * @return (通行卡,人脸,指纹)采集器分页
     */
    PageResult<CollectorDO> getCollectorPage(CollectorPageReqVO pageReqVO);

    List<CollectorDO> getCollectorList(CollectorSaveReqVO reqVO);

    CollectorDO getOne(QueryWrapper<CollectorDO> queryWrapper);

    int update(UpdateWrapper<CollectorDO> collectorWrapper);

    List<CollectorDO> list(QueryWrapper<CollectorDO> queryWrapper);
}
