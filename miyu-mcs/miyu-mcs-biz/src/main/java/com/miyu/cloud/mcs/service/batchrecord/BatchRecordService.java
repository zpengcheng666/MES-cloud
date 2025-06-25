package com.miyu.cloud.mcs.service.batchrecord;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.controller.admin.batchrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;

/**
 * 批次工序任务 Service 接口
 *
 * @author 芋道源码
 */
public interface BatchRecordService {

    /**
     * 创建批次工序任务
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBatchRecord(@Valid BatchRecordSaveReqVO createReqVO);

    /**
     * 更新批次工序任务
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchRecord(@Valid BatchRecordSaveReqVO updateReqVO);

    /**
     * 删除批次工序任务
     *
     * @param id 编号
     */
    void deleteBatchRecord(String id);

    /**
     * 获得批次工序任务
     *
     * @param id 编号
     * @return 批次工序任务
     */
    BatchRecordDO getBatchRecord(String id);

    /**
     * 获得批次工序任务分页
     *
     * @param pageReqVO 分页查询
     * @return 批次工序任务分页
     */
    PageResult<BatchRecordDO> getBatchRecordPage(BatchRecordPageReqVO pageReqVO);

    /**
     * 根据批次任务 所用工艺 起始工序 创建详情任务
     * @param batchOrderDO
     */
    void createBatchRecordByBatch(BatchOrderDO batchOrderDO);

    void deleteByIdPhy(String id);

    void deleteBatchIdsPhy(Collection<String> deleteIds);

    //删除后续批次 (物理删除)
    void deleteBatchRecordSelfAndAfter(BatchRecordDO batchRecordDO);

    //根据批次查询任务
    List<BatchRecordDO> getListByBatchId(String batchId);

    List<BatchRecordDO> list(Wrapper<BatchRecordDO> wrapper);

    /**
     * 下发批次详情任务  生成详情
     */
    void batchRecordDistribution(String batchRecordId);

    List<BatchRecordRespVO> getBatchRecordByUnitForDelivery(String deviceId);

    List<ProcedureRespDTO> getBeforeProcessListByRecordId(String recordId);

    BatchRecordDO getFirstRecordByBatchId(String id);

    List<BatchRecordDO> listByIds(Collection<String> batchRecordIds);

    void bindMaterial(BatchOrderDO batchOrderDO);

    Collection<BatchRecordDeviceTypeReqVO> getDeviceByOrderId(List<String> orderIdList);

    BatchRecordDO getBatchRecordByNumber(String number);
}
