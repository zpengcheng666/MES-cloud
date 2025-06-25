package com.miyu.cloud.macs.service.collector;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.util.MyBatisUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import com.miyu.cloud.macs.controller.admin.collector.vo.*;
import com.miyu.cloud.macs.dal.dataobject.collector.CollectorDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.cloud.macs.dal.mysql.collector.CollectorMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.macs.enums.ErrorCodeConstants.*;

/**
 * (通行卡,人脸,指纹)采集器 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class CollectorServiceImpl implements CollectorService {

    @Resource
    private CollectorMapper collectorMapper;

    @Override
    public String createCollector(CollectorSaveReqVO createReqVO) {
        // 插入
        CollectorDO collector = BeanUtils.toBean(createReqVO, CollectorDO.class);
        collectorMapper.insert(collector);
        // 返回
        return collector.getId();
    }

    @Override
    public void updateCollector(CollectorSaveReqVO updateReqVO) {
        // 校验存在
        validateCollectorExists(updateReqVO.getId());
        // 更新
        CollectorDO updateObj = BeanUtils.toBean(updateReqVO, CollectorDO.class);
        collectorMapper.updateById(updateObj);
    }

    @Override
    public void deleteCollector(String id) {
        // 校验存在
        validateCollectorExists(id);
        // 删除
        collectorMapper.deleteById(id);
    }

    private void validateCollectorExists(String id) {
        if (collectorMapper.selectById(id) == null) {
            throw exception(COLLECTOR_NOT_EXISTS);
        }
    }

    @Override
    public CollectorDO getCollector(String id) {
        return collectorMapper.selectById(id);
    }

    @Override
    public PageResult<CollectorDO> getCollectorPage(CollectorPageReqVO pageReqVO) {
        LambdaQueryWrapperX<CollectorDO> queryWrapperX = new LambdaQueryWrapperX<CollectorDO>()
                .eqIfPresent(CollectorDO::getCode, pageReqVO.getCode())
                .likeIfPresent(CollectorDO::getName, pageReqVO.getName())
                .eqIfPresent(CollectorDO::getLocationCode, pageReqVO.getLocationCode())
                .eqIfPresent(CollectorDO::getDoorId, pageReqVO.getDoorId())
                .eqIfPresent(CollectorDO::getDeviceId, pageReqVO.getDeviceId())
                .eqIfPresent(CollectorDO::getDevicePort, pageReqVO.getDevicePort())
                .eqIfPresent(CollectorDO::getStatus, pageReqVO.getStatus())
                .eqIfPresent(CollectorDO::getType, pageReqVO.getType())
                .eqIfPresent(CollectorDO::getDescription, pageReqVO.getDescription())
                .eqIfPresent(CollectorDO::getConnectionInformation, pageReqVO.getConnectionInformation())
                .eqIfPresent(CollectorDO::getCreateBy, pageReqVO.getCreateBy())
                .betweenIfPresent(CollectorDO::getCreateTime, pageReqVO.getCreateTime())
                .eqIfPresent(CollectorDO::getUpdateBy, pageReqVO.getUpdateBy())
                .orderByDesc(CollectorDO::getId);
        IPage<CollectorDO> mpPage = MyBatisUtils.buildPage(pageReqVO, null);
        IPage<CollectorDO> page = collectorMapper.selectPageList(mpPage, queryWrapperX);
        return new PageResult<>(page.getRecords(), page.getTotal());
    }

    @Override
    public List<CollectorDO> getCollectorList(CollectorSaveReqVO reqVO) {
        LambdaQueryWrapperX<CollectorDO> queryWrapperX = new LambdaQueryWrapperX<CollectorDO>()
                .eqIfPresent(CollectorDO::getCode, reqVO.getCode())
                .likeIfPresent(CollectorDO::getName, reqVO.getName())
                .eqIfPresent(CollectorDO::getLocationCode, reqVO.getLocationCode())
                .eqIfPresent(CollectorDO::getDoorId, reqVO.getDoorId())
                .eqIfPresent(CollectorDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(CollectorDO::getDevicePort, reqVO.getDevicePort())
                .eqIfPresent(CollectorDO::getStatus, reqVO.getStatus())
                .eqIfPresent(CollectorDO::getType, reqVO.getType())
                .eqIfPresent(CollectorDO::getDescription, reqVO.getDescription())
                .eqIfPresent(CollectorDO::getConnectionInformation, reqVO.getConnectionInformation())
                .eqIfPresent(CollectorDO::getCreateBy, reqVO.getCreateBy())
                .eqIfPresent(CollectorDO::getUpdateBy, reqVO.getUpdateBy())
                .orderByDesc(CollectorDO::getId);
        return collectorMapper.selectList(queryWrapperX);
    }

    @Override
    public CollectorDO getOne(QueryWrapper<CollectorDO> queryWrapper) {
        return collectorMapper.selectOne(queryWrapper);
    }

    @Override
    public int update(UpdateWrapper<CollectorDO> collectorWrapper) {
        return collectorMapper.update(collectorWrapper);
    }

    @Override
    public List<CollectorDO> list(QueryWrapper<CollectorDO> queryWrapper) {
        return collectorMapper.selectList(queryWrapper);
    }
}
