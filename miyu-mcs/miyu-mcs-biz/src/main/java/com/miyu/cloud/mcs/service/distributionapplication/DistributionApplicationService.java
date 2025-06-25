package com.miyu.cloud.mcs.service.distributionapplication;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.mcs.controller.admin.distributionapplication.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionapplication.DistributionApplicationDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 物料配送申请 Service 接口
 *
 * @author miyu
 */
public interface DistributionApplicationService {

    /**
     * 创建物料配送申请
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createDistributionApplication(@Valid DistributionApplicationSaveReqVO createReqVO);

    /**
     * 更新物料配送申请
     *
     * @param updateReqVO 更新信息
     */
    void updateDistributionApplication(@Valid DistributionApplicationSaveReqVO updateReqVO);

    /**
     * 删除物料配送申请
     *
     * @param id 编号
     */
    void deleteDistributionApplication(String id);

    /**
     * 获得物料配送申请
     *
     * @param id 编号
     * @return 物料配送申请
     */
    DistributionApplicationDO getDistributionApplication(String id);

    /**
     * 获得物料配送申请分页
     *
     * @param pageReqVO 分页查询
     * @return 物料配送申请分页
     */
    PageResult<DistributionApplicationDO> getDistributionApplicationPage(DistributionApplicationPageReqVO pageReqVO);

    DistributionApplicationEditVO getRecordListByBatchAndType(DistributionApplicationEditVO editVO);

    String createApplication(DistributionApplicationEditVO createReqVO);

    void submitApplication(String id);

    //出库审批 状态更改
    void updateApplicationStatus(String applicationId, Integer status);

    void applicationCancelByOrderId(String orderId);
}
