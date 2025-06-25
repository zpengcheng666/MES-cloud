package com.miyu.cloud.mcs.service.batchorderapsresult;

import java.util.*;
import javax.validation.*;

import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.result.OrderFormResult;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.BatchOrderApsResultPageReqVO;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.BatchOrderApsResultSaveReqVO;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.DeviceLoadData;
import com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo.OrderScheduleSaveVO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderapsresult.BatchOrderApsResultDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.miyu.cloud.mcs.dto.schedule.ScheduleConfig;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;

/**
 * 排产结果 Service 接口
 *
 * @author 上海弥彧
 */
public interface BatchOrderApsResultService {

    /**
     * 创建排产结果
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBatchOrderApsResult(@Valid BatchOrderApsResultSaveReqVO createReqVO);

    /**
     * 更新排产结果
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchOrderApsResult(@Valid BatchOrderApsResultSaveReqVO updateReqVO);

    /**
     * 删除排产结果
     *
     * @param id 编号
     */
    void deleteBatchOrderApsResult(String id);

    /**
     * 获得排产结果
     *
     * @param id 编号
     * @return 排产结果
     */
    BatchOrderApsResultDO getBatchOrderApsResult(String id);

    /**
     * 获得排产结果分页
     *
     * @param pageReqVO 分页查询
     * @return 排产结果分页
     */
    PageResult<BatchOrderApsResultDO> getBatchOrderApsResultPage(BatchOrderApsResultPageReqVO pageReqVO);

    String createApsResult2 (ScheduleConfig scheduleConfig) throws Exception;
    String createApsResult (ScheduleConfig scheduleConfig) throws Exception;

    String productionScheduling(@Valid OrderScheduleSaveVO createReqVO) throws Exception;

    List<LedgerDO> getLedgerNameListByApsId(String id);

    Map<String, Object> getLedgerLoadByApsId(String id);

    void schedulingAdopt(ScheduleConfig scheduleConfig);
}
