package com.miyu.cloud.mcs.service.batchrecordstep;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.mcs.controller.admin.batchrecordstep.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;

/**
 * 工步计划 Service 接口
 *
 * @author 上海弥彧
 */
public interface BatchRecordStepService {

    /**
     * 创建工步计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBatchRecordStep(@Valid BatchRecordStepSaveReqVO createReqVO);

    /**
     * 更新工步计划
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchRecordStep(@Valid BatchRecordStepSaveReqVO updateReqVO);

    /**
     * 删除工步计划
     *
     * @param id 编号
     */
    void deleteBatchRecordStep(String id);

    /**
     * 获得工步计划
     *
     * @param id 编号
     * @return 工步计划
     */
    BatchRecordStepDO getBatchRecordStep(String id);

    /**
     * 获得工步计划分页
     *
     * @param pageReqVO 分页查询
     * @return 工步计划分页
     */
    PageResult<BatchRecordStepDO> getBatchRecordStepPage(BatchRecordStepPageReqVO pageReqVO);

    void deleteByRecordIdsPhy(Collection<String> deleteIds);

    void createBatchDetailStepByRecord(BatchRecordDO batchRecordDO, List<StepRespDTO> stepList);

    //临时方法 根据工序任务信息更新工步信息
    void updateByRecord(BatchRecordDO batchRecordDO);

    List<BatchRecordStepDO> list(Wrapper<BatchRecordStepDO> queryWrapper);
}
