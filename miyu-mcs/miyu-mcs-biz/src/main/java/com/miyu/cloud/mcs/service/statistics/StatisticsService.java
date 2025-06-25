package com.miyu.cloud.mcs.service.statistics;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.ReceiptRecordPageReqVO;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.ReceiptRecordRespVO;
import com.miyu.cloud.mcs.controller.admin.receiptrecord.vo.ReceiptRecordSaveReqVO;
import com.miyu.cloud.mcs.controller.admin.statistics.vo.ProcessingRecordVO;
import com.miyu.cloud.mcs.controller.admin.statistics.vo.StatisticQueryReqVO;
import com.miyu.cloud.mcs.dal.dataobject.receiptrecord.ReceiptRecordDO;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 生产单元签收记录 Service 接口
 *
 * @author miyu
 */
public interface StatisticsService {


    Map<String, Long> getWorkerActualHoursList(StatisticQueryReqVO params);

    Map<String, Integer> getWorkerEffectiveHoursList(StatisticQueryReqVO params);

    List<ProcessingRecordVO> getWorkerProcessingRecords(StatisticQueryReqVO params);
}
