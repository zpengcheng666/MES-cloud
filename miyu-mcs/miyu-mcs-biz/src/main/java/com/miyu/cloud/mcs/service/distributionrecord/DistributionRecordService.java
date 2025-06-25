package com.miyu.cloud.mcs.service.distributionrecord;

import java.util.*;
import javax.validation.*;
import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料配送申请详情 Service 接口
 *
 * @author miyu
 */
public interface DistributionRecordService {

    /**
     * 创建物料配送申请详情
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createDistributionRecord(@Valid DistributionRecordSaveReqVO createReqVO);

    /**
     * 更新物料配送申请详情
     *
     * @param updateReqVO 更新信息
     */
    void updateDistributionRecord(@Valid DistributionRecordSaveReqVO updateReqVO);

    /**
     * 删除物料配送申请详情
     *
     * @param id 编号
     */
    void deleteDistributionRecord(String id);

    /**
     * 获得物料配送申请详情
     *
     * @param id 编号
     * @return 物料配送申请详情
     */
    DistributionRecordDO getDistributionRecord(String id);

    /**
     * 获得物料配送申请详情分页
     *
     * @param pageReqVO 分页查询
     * @return 物料配送申请详情分页
     */
    PageResult<DistributionRecordDO> getDistributionRecordPage(DistributionRecordPageReqVO pageReqVO);

    PageResult<DistributionRecordRespVO> getDistributionRecordPageAll(DistributionRecordPageReqVO pageReqVO);

    List<DistributionRecordDO> listByApplication(String applicationId);

    void recordRevokeByDemandRecord(String demandRecordId);

    void recordRevokeById(String id);
}
