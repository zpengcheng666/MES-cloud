package com.miyu.cloud.mcs.service.batchdemandrecord;

import java.util.*;
import javax.validation.*;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

/**
 * 需求分拣详情 Service 接口
 *
 * @author miyu
 */
public interface BatchDemandRecordService {

    /**
     * 创建需求分拣详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBatchDemandRecord(@Valid BatchDemandRecordSaveReqVO createReqVO);

    /**
     * 更新需求分拣详情
     *
     * @param updateReqVO 更新信息
     */
    void updateBatchDemandRecord(@Valid BatchDemandRecordSaveReqVO updateReqVO);

    /**
     * 删除需求分拣详情
     *
     * @param id 编号
     */
    void deleteBatchDemandRecord(String id);

    /**
     * 获得需求分拣详情
     *
     * @param id 编号
     * @return 需求分拣详情
     */
    BatchDemandRecordDO getBatchDemandRecord(String id);

    /**
     * 获得需求分拣详情分页
     *
     * @param pageReqVO 分页查询
     * @return 需求分拣详情分页
     */
    PageResult<BatchDemandRecordDO> getBatchDemandRecordPage(BatchDemandRecordPageReqVO pageReqVO);

    List<BatchDemandRecordDO> list(Wrapper<BatchDemandRecordDO> wrapper);

    void demandRecordRevoke(String id);
}
