package com.miyu.cloud.mcs.enums;

/**
 * System 字典类型的枚举类
 *
 * @author 芋道源码
 */
public interface DictConstants {

    //订单
    int MCS_ORDER_STATUS_NEW = 0; //新建
    int MCS_ORDER_STATUS_SUBMIT = 4; //已提交
    int MCS_ORDER_STATUS_ONGOING = 1; //进行中
    int MCS_ORDER_STATUS_COMPLETED = 2; //已完成
    int MCS_ORDER_STATUS_RESCINDED = 3; //已撤销

    //批次订单
    int MCS_BATCH_STATUS_NEW = 0; //新建
    int MCS_BATCH_STATUS_PREPARATION = 1; //待备料
    int MCS_BATCH_STATUS_READY = 2; //已齐备
    int MCS_BATCH_STATUS_CAN_BE_ISSUED = 3; //可下发
    int MCS_BATCH_STATUS_ISSUED = 4; //已下发
    int MCS_BATCH_STATUS_ONGOING = 5; //进行中
    int MCS_BATCH_STATUS_COMPLETED = 6; //已完成
    int MCS_BATCH_STATUS_RESCINDED = 7; //已撤销
    //批次任务
    int MCS_BATCH_RECORD_STATUS_NEW = 0; //新建
    int MCS_BATCH_RECORD_STATUS_ISSUED = 1; //已下发
    int MCS_BATCH_RECORD_STATUS_ONGOING = 2; //进行中
    int MCS_BATCH_RECORD_STATUS_COMPLETED = 3; //已完成
    int MCS_BATCH_RECORD_STATUS_RESCINDED = 4; //已撤销
    //工步任务状态
    int MCS_STEP_STATUS_NEW = 0; //新建
    int MCS_STEP_STATUS_ONGOING = 1; //进行中
    int MCS_STEP_STATUS_COMPLETED = 2; //已完成
    int MCS_STEP_STATUS_RESCINDED = 3; //已撤销
    //加工类型
    int MCS_PROCES_STATUS_CURRENT = 1; //本厂加工
    int MCS_PROCES_STATUS_OUTSOURCING = 2; //外协加工
    //需求
    int MCS_READY_STATUS_NOT_FULLY_PREPARED = 0; //未齐备
    int MCS_READY_STATUS_SORTING  = 1;//已分拣
    int MCS_READY_STATUS_CAN_START_CONSTRUCTION = 2; //可开工
    int MCS_READY_STATUS_ALL_READY = 3; //已齐备

    //配送申请
    int MCS_DISTRIBUTION_APPLICATION_NEW = 0;//新建
    int MCS_DISTRIBUTION_APPLICATION_SUBMIT = 1;//提交
    int MCS_DISTRIBUTION_APPLICATION_ADOPT = 2;//通过
    int MCS_DISTRIBUTION_APPLICATION_REJECT = 3;//驳回
    int MCS_DISTRIBUTION_APPLICATION_REVOKE = 4;//撤销
    int MCS_DISTRIBUTION_APPLICATION_COMPLETED = 5;//已完成

    //订单需求 需求类别
    int MCS_DEMAND_TYPE_NORMAL = 1;//常规
    int MCS_DEMAND_TYPE_COMPENSATE = 2;//补 料

    //资源类型(仓储)
    String WMS_MATERIAL_TYPE_CUTTING = "DJ";//刀具
    String WMS_MATERIAL_TYPE_FIXTURE = "JJ";//夹具
    String WMS_MATERIAL_TYPE_TOOL = "GZ";//工装
    String WMS_MATERIAL_TYPE_WORKBENCH = "TP";//托盘
    String WMS_MATERIAL_TYPE_WORKPIECE = "GJ";//零件

    //订单详情状态
//    int MCS_ORDER_DETAIL_NEW = 0;//新建
//    int MCS_ORDER_DETAIL_ONGOING = 3;//加工中
//    int MCS_ORDER_DETAIL_COMPLETED = 1;//已完成
//    int MCS_ORDER_DETAIL_RESCINDED = 2;//已撤销
//    int MCS_ORDER_DETAIL_UNQUALIFIED = 4;//不合格
//    int MCS_BATCH_DETAIL_ISSUED = 5;//已下发(工步级)
//    int MCS_ORDER_DETAIL_SCRAP = 6;//报废(订单级)

    //需求-物料选择配送状态
    int MCS_DEMAND_RECORD_STATUS_NEW = 0; //未配送
    int MCS_DEMAND_RECORD_STATUS_APPLIED = 1; //已申请
    int MCS_DEMAND_RECORD_STATUS_DELIVERY = 2; //配送中
    int MCS_DEMAND_RECORD_STATUS_COMPLETED = 3; //已完成
    int MCS_DEMAND_RECORD_STATUS_RESCINDED = 4; //已撤销
    int MCS_DEMAND_RECORD_STATUS_UNQUALIFIED = 5; //不合格

    //物料配送需求状态
    int MCS_DETAIL_NEED_DELIVERY_STATUS_NULL = 0; //无需配送
    int MCS_DETAIL_NEED_DELIVERY_STATUS_MOVE = 1; //需移库或入库
    int MCS_DETAIL_NEED_DELIVERY_STATUS_OUT = 2; //需出库

    //出库申请 配送状态
    int MCS_DELIVERY_RECORD_STATUS_NEW = 0; //新建
    int MCS_DELIVERY_RECORD_STATUS_APPLIED = 5; //已申请
    int MCS_DELIVERY_RECORD_STATUS_DELIVERY = 1; //配送中
    int MCS_DELIVERY_RECORD_STATUS_COMPLETED = 2; //已完成
    int MCS_DELIVERY_RECORD_STATUS_REJECT = 3; //已驳回
    int MCS_DELIVERY_RECORD_STATUS_CLOSE = 4; //已关闭
    int MCS_DELIVERY_RECORD_STATUS_READY = 6; //待签收

    //资源需求类型(工序)
    int PROCESS_RESOURCES_TYPE_DEVICE = 1;//设备
    int PROCESS_RESOURCES_TYPE_CUTTING = 2;//刀具
    int PROCESS_RESOURCES_TYPE_TOOL = 3;//工装

    //批次订单 提交状态(可拆单状态)
    int MCS_BATCH_SUBMIT_STATUS_NEW = 0;//新建
    int MCS_BATCH_SUBMIT_STATUS_SUBMIT = 1;//已提交
    int MCS_BATCH_SUBMIT_STATUS_SUSPEND = 2;//已暂停

    //设备类型
    int DMS_DEVICE_TYPE_EQUIPMENT = 0;//设备
    int DMS_DEVICE_TYPE_WORKSTATION = 1;//工位
    int DMS_DEVICE_TYPE_PRODUCTION_LINE = 2;//产线

    //物料状态(项目)
    int PMS_MATERIAL_STATUS_PENDING_PROCESSING = 1;//待分配
    int PMS_MATERIAL_STATUS_ONGOING = 2;//加工中
    int PMS_MATERIAL_STATUS_PENDING_STORAGE = 3;//未入库
    int PMS_MATERIAL_STATUS_OUTSOURCING = 4;//外协中
    int PMS_MATERIAL_STATUS_COMPLETED = 5;//加工完成
    int PMS_MATERIAL_STATUS_PENDING_OUTSOURCE = 6;//待外协
    int PMS_MATERIAL_STATUS_SCRAP = 7;//报废

    //工位任务操作类型
    int MCS_STEP_PLAN_EVENT_TYPE_START = 1; //任务开工
    int MCS_STEP_PLAN_EVENT_TYPE_FINISH = 2; //任务完工
    int MCS_WORKSTATION_EVENT_TYPE_SUSPEND = 3; //工位暂停
    int MCS_WORKSTATION_EVENT_TYPE_RECOVERY = 4; //工位恢复
    int MCS_WORKSTATION_EVENT_TYPE_INTERRUPTION = 5; //任务中断
    int MCS_WORKSTATION_EVENT_TYPE_CONTINUES = 6; //工位继续

    //零件检验转态
    int MCS_DETAIL_INSPECT_STATUS_FINISH = 0; //已检验
    int MCS_DETAIL_INSPECT_STATUS_SELF = 1; //自检|互检
    int MCS_DETAIL_INSPECT_STATUS_SPECIAL = 2; //专检
    int MCS_DETAIL_INSPECT_STATUS_WAITING = 99; //已生成检验单 待检验

    //redisKey 生产配送状态详情
    String MCS_DISTRIBUTION_STATUS_REDIS_DEVICE = "mcsDistributionStatusRedisDevice";
    String MCS_DISTRIBUTION_STATUS_REDIS_LINE = "mcsDistributionStatusRedisLine";
    String MCS_CARRY_READY_STATUS_REDIS = "mcsCarryReadyStatusRedis";

    //任务加工附加文件 储存方式
    int MCS_ADDITIONAL_STORAGE_MODE_DATABASE = 1;
    int MCS_ADDITIONAL_STORAGE_MODE_FILE = 2;

    //任务加工附加文件 类型编码
    String MCS_ADDITIONAL_TYPE_MEASURE_INFO = "measureInfo";
    String MCS_ADDITIONAL_TYPE_MEASURE_FILE = "measureFile";
}
