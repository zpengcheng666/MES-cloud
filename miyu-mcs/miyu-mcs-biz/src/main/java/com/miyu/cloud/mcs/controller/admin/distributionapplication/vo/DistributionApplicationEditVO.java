package com.miyu.cloud.mcs.controller.admin.distributionapplication.vo;

import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.BatchDemandRecordRespVO;
import com.miyu.cloud.mcs.controller.admin.batchorder.vo.BatchOrderRespVO;
import com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo.BatchOrderDemandRespVO;
import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.DistributionRecordRespVO;
import com.miyu.cloud.mcs.dal.dataobject.batchorderdemand.BatchOrderDemandDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class DistributionApplicationEditVO {

    //申请编码
    private String applicationNumber;

    //申请单元
    private String deviceUnitId;

    //资源类型集合
    private List<String> type;

    //已选批次任务
    private List<String> batchRecordIdList;

    //需求分拣结果详情集合
    private List<DistributionRecordRespVO> distributionRecordRespVOList;

    //已选待配送集合
    private List<DistributionRecordRespVO> demandDeliveryList;

}
