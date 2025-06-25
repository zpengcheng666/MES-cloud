package com.miyu.cloud.mcs.service.batchorderdemand;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderdemand.BatchOrderDemandDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dto.resource.McsCuttingToolDemandDTO;
import com.miyu.cloud.mcs.dto.resource.McsCuttingToolDemandUptateDTO;

/**
 * 批次订单需求 Service 接口
 *
 * @author miyu
 */
public interface BatchOrderDemandService {

    /**
     * 创建批次订单需求
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBatchOrderDemand(@Valid BatchOrderDemandSaveReqVO createReqVO);

    /**
     * 更新批次订单需求
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchOrderDemand(@Valid BatchOrderDemandSaveReqVO updateReqVO);

    /**
     * 删除批次订单需求
     *
     * @param id 编号
     */
    void deleteBatchOrderDemand(String id);

    /**
     * 获得批次订单需求
     *
     * @param id 编号
     * @return 批次订单需求
     */
    BatchOrderDemandDO getBatchOrderDemand(String id);

    /**
     * 获得批次订单需求分页
     *
     * @param pageReqVO 分页查询
     * @return 批次订单需求分页
     */
    PageResult<BatchOrderDemandDO> getBatchOrderDemandPage(BatchOrderDemandPageReqVO pageReqVO);

    /**
     * 依批次 生成批次订单需求
     * @param orderFormDO
     */
    void createBatchOrderDemandByOrder(OrderFormDO orderFormDO);

    void deleteOldDemand(OrderFormDO orderFormDO);

    //查询可用产线
    List<LineStationGroupDO> getAllProcessingUnit();

    //分拣结果保存
    void resourceSortingSave(BatchOrderDemandSaveReqVO createReqVO);

    void completeBatchOrderDemand(BatchOrderDemandDO batchOrderDemand);

    Map<String, Object> getOutOrderOtherMaterialsByConfigIds(List<String> singletonList, Map<String, Integer> locationAndCount);

    List<BatchOrderDemandDO> getListByOrderId(String orderId);

    String createExtraDemand(BatchOrderDemandSaveReqVO createReqVO);

    PageResult<McsCuttingToolDemandDTO> getCuttingToolDemandPage(PageParam pageReqVO);

    void updateCuttingToolDemand(McsCuttingToolDemandUptateDTO cuttingTool);

    void updateOrderFormDemand(List<String> orderIdList);
}
