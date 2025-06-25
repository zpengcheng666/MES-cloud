package com.miyu.cloud.mcs.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    ErrorCode BATCH_ORDER_DEMAND_NOT_EXISTS = new ErrorCode(5001, "批次订单需求不存在");
    ErrorCode DISTRIBUTION_APPLICATION_NOT_EXISTS = new ErrorCode(5001, "物料配送申请不存在");
    ErrorCode DISTRIBUTION_RECORD_NOT_EXISTS = new ErrorCode(5001, "物料配送申请详情不存在");
    ErrorCode PRODUCTION_RECORDS_NOT_EXISTS = new ErrorCode(5001, "现场作业记录不存在");
    ErrorCode BATCH_ORDER_NOT_EXISTS = new ErrorCode(5001, "批次级订单不存在");
    ErrorCode ORDER_FORM_NOT_EXISTS = new ErrorCode(5001, "生产订单不存在");
    ErrorCode ORDER_DETAIL_NOT_EXISTS = new ErrorCode(5001, "生产订单详情不存在");
    ErrorCode RECEIPT_RECORD_NOT_EXISTS = new ErrorCode(5001, "生产单元签收记录不存在");
    ErrorCode BATCH_DETAIL_NOT_EXISTS = new ErrorCode(5001, "批次详情不存在");
    ErrorCode BATCH_DEMAND_RECORD_NOT_EXISTS = new ErrorCode(5001, "批次需求详情不存在");
    ErrorCode BATCH_RECORD_STEP_NOT_EXISTS = new ErrorCode(5001, "批次需求详情不存在");
    ErrorCode BATCH_RECORD_NOT_EXISTS = new ErrorCode(5001, "批次详细不存在!");
    ErrorCode ORDER_STATUS_CAN_NOT_SPLIT = new ErrorCode(5002, "当前订单状态,不可撤销拆单!");
    ErrorCode CHILD_ORDER_CAN_NOT_REVOKE = new ErrorCode(5003, "子订单状态不可撤销!");
    ErrorCode PROBLEM_REPORT_NOT_EXISTS = new ErrorCode(5004, "问题上报不存在");
    ErrorCode BATCH_ORDER_APS_RESULT_NOT_EXISTS = new ErrorCode(5005, "排产结果不存在");
    ErrorCode LEDGER_HAS_NOT_SHIFT = new ErrorCode(5005, "有设备没有班次");
    ErrorCode APS_DATE_NOT_MATCH_SHIFT = new ErrorCode(5005, "排产时间匹配不上班次");

}
