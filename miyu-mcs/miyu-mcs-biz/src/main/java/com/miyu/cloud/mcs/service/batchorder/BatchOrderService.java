package com.miyu.cloud.mcs.service.batchorder;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.controller.admin.batchorder.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;

/**
 * 批次级订单 Service 接口
 *
 * @author miyu
 */
public interface BatchOrderService {

    /**
     * 创建批次级订单
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBatchOrder(@Valid BatchOrderSaveReqVO createReqVO);

    /**
     * 更新批次级订单
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchOrder(@Valid BatchOrderSaveReqVO updateReqVO);

    /**
     * 删除批次级订单
     *
     * @param id 编号
     */
    void deleteBatchOrder(String id);

    /**
     * 获得批次级订单
     *
     * @param id 编号
     * @return 批次级订单
     */
    BatchOrderDO getBatchOrder(String id);

    /**
     * 获得批次级订单分页
     *
     * @param pageReqVO 分页查询
     * @return 批次级订单分页
     */
    PageResult<BatchOrderDO> getBatchOrderPage(BatchOrderPageReqVO pageReqVO);

    /**
     * 物理删除
     */
    void deleteByIdPhy(String id);
    /**
     * 物理删除
     */
    void deleteBatchIdsPhy(Collection<String> ids);
    /**
     * 订单提交
     */
    void batchSubmit(List<BatchOrderDO> batchOrderDOList);

    List<BatchOrderDO> getBatchOrderListByOrderId(String orderId);
    List<BatchOrderDO> getBatchOrderListByOrderIds(List<String> orderIds);

    List<BatchOrderDO> list(Wrapper<BatchOrderDO> wrapper);

    /**
     * 订单下发
     */
    void batchIssuance(String batchId);

    /**
     * 订单暂停
     */
    void batchSuspend(String id);

    void batchContinue(String id);

    void nextRecordDistribution(BatchRecordDO batchRecordDO);
}
