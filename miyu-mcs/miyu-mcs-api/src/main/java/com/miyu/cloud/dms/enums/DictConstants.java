package com.miyu.cloud.dms.enums;

/**
 * dms 字典类型的枚举类
 *
 * @author 王正浩
 */
public interface DictConstants {
    Integer DMS_DEVICE_TYPE_ENABLE_0 = 0; // 数据类型是否启用 未启用
    Integer DMS_DEVICE_TYPE_ENABLE_1 = 1; // 数据类型是否启用 启用

    Integer DMS_FAULT_STATE_0 = 0; // 异常记录故障状态 未处理
    Integer DMS_FAULT_STATE_1 = 1; // 异常记录故障状态 处理中
    Integer DMS_FAULT_STATE_2 = 2; // 异常记录故障状态 已处理

    Integer DMS_MAINTENANCE_STATUS_0 = 0; // 设备保养维护完成状态 完成
    Integer DMS_MAINTENANCE_STATUS_1 = 1; // 设备保养维护完成状态 未完成
    Integer DMS_MAINTENANCE_STATUS_2 = 2; // 设备保养维护完成状态 进行中
    Integer DMS_MAINTENANCE_STATUS_3 = 3; // 设备保养维护完成状态 异常

    Integer DMS_INSPECTION_STATE_1 = 1; // 设备检查类型 设备点检
    Integer DMS_INSPECTION_STATE_2 = 2; // 设备检查类型 设备巡检

    Integer DMS_INSPECTION_PLAN_NOTICE_0 = 0; // 设备检查计划提醒状态 未完成
    Integer DMS_INSPECTION_PLAN_NOTICE_1 = 1; // 设备检查计划提醒状态 已完成

    Integer ENABLE_STATUS_0 = 0; // 启用状态 禁用
    Integer ENABLE_STATUS_1 = 1; // 启用状态 启用

    Integer DMS_EQUIPMENT_STATION_0=0; // 设备类型 设备
    Integer DMS_EQUIPMENT_STATION_1 = 1; // 设备类型 工位
    Integer DMS_EQUIPMENT_STATION_2 = 2; // 设备类型 产线
    Integer DMS_EQUIPMENT_STATION_3 = 3; // 设备类型 工位+产线(不存在于数据字典中)

    int DMS_LOCATION_TYPE_MATERIAL = 0; //物料库位
    int DMS_LOCATION_TYPE_CUTTING = 1; //刀具库位

    int DMS_DEVICE_STATUS_NORMAL = 0; //正常
    int DMS_DEVICE_STATUS_DISABLE = 1; //禁用
    int DMS_DEVICE_STATUS_REPAIR = 2; //维修

    int DMS_device_Run_Status_run = 0; //加工中
    int DMS_device_Run_Status_free = 1; //空闲
    int DMS_device_Run_Status_close = 2; //停机
    int DMS_device_Run_Status_stop = 3; //暂停
    int DMS_device_Run_Status_warning = 4; //急停

    int DMS_DEVICE_ONLINE_STATUS_ONLINE = 0; //在线
    int DMS_DEVICE_ONLINE_STATUS_SEMI_OFFLINE = 1; //半离线
    int DMS_DEVICE_ONLINE_STATUS_OFFLINE = 2; //离线
}
