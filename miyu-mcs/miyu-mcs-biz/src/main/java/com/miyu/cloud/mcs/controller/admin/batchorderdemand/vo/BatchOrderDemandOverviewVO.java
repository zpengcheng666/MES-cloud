package com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchOrderDemandOverviewVO {

    //资源类型
    private String resourcesType;
    //资源类型id
    private String resourcesTypeId;
    //资源类码
    private String resourcesTypeCode;
    //订单id
    private String orderId;
    //任务
    private List<BatchRecordDO> batchRecordDOList = new ArrayList<>();
}
