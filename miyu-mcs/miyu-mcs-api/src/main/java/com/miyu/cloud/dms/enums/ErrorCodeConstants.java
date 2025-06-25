package com.miyu.cloud.dms.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {
    /****************加工单元:ProcessingUnit********************/
    ErrorCode PROCESSING_UNIT_NOT_EXISTS = new ErrorCode(5001, "加工单元不存在");

    /*********************设备/工位类型:DeviceType************************/
    ErrorCode DEVICE_TYPE_NOT_EXISTS = new ErrorCode(5001, "设备类型不存在");
    ErrorCode DEVICE_TYPE_DUPLICATE_NAME = new ErrorCode(5001, "设备类型编号重复");
    ErrorCode DEVICE_TYPE_CODE_EMPTY = new ErrorCode(5001, "类型编号不能为空");
    ErrorCode DEVICE_TYPE_NAME_EMPTY = new ErrorCode(5001, "类型名称不能为空");
    ErrorCode DEVICE_TYPE_ENABLE_EMPTY = new ErrorCode(5001, "是否启用必选");

    /************************产线/工位组:LineStationGroupController*******************************/
    ErrorCode LINE_STATION_GROUP_NOT_EXISTS = new ErrorCode(5001, "产线/工位组不存在");

    /********************设备台账:Ledger*********************************/
    ErrorCode LEDGER_NOT_EXISTS = new ErrorCode(5001, "设备台账不存在");
    ErrorCode LEDGER_DUPLICATE_NAME = new ErrorCode(5001, "设备台账编号重复");
    ErrorCode LEDGER_CODE_EMPTY = new ErrorCode(5001, "设备编号不能为空");
    ErrorCode LEDGER_NAME_EMPTY = new ErrorCode(5001, "设备名称不能为空");
    ErrorCode LEDGER_TYPE_EMPTY = new ErrorCode(5001, "设备类型不能为空");
    ErrorCode LEDGER_STATUS_EMPTY = new ErrorCode(5001, "状态不能为空");
    ErrorCode LEDGER_SUPERINTENDENT_EMPTY = new ErrorCode(5001, "负责人不能为空");

    /********************异常记录:FailureRecord************************/
    ErrorCode FAILURE_RECORD_NOT_EXISTS = new ErrorCode(5001, "异常记录不存在");
    ErrorCode PROCESSING_UNIT_TYPE_NOT_EXISTS = new ErrorCode(5001, "生产单元类型不存在");

    /*****************设备维修申请:MaintainApplication*********************************/
    ErrorCode MAINTAIN_APPLICATION_NOT_EXISTS = new ErrorCode(5001, "设备维修申请不存在");
    ErrorCode MAINTAIN_APPLICATION_DEVICE_EMPTY = new ErrorCode(5001, "设备不能为空");
    ErrorCode MAINTAIN_APPLICATION_IMPORTANT_EMPTY = new ErrorCode(5001, "是否为关键设备不能为空");
    ErrorCode MAINTAIN_APPLICATION_TYPE_EMPTY = new ErrorCode(5001, "维修类型不能为空");
    ErrorCode MAINTAIN_APPLICATION_DESCRIBE_EMPTY = new ErrorCode(5001, "故障信息描述不能为空");
    ErrorCode MAINTAIN_APPLICATION_DURATION_EMPTY = new ErrorCode(5001, "期望修复时间不能为空");

    /*****************计划关联树:PlanTree************************/
    ErrorCode PLAN_TREE_NOT_EXISTS = new ErrorCode(5001, "计划关联树不存在");
    ErrorCode PLAN_ID_NOT_EXISTS = new ErrorCode(5001, "计划ID不存在");
    ErrorCode PLAN_ID_NAME_EMPTY = new ErrorCode(5001, "节点名不能为空");

    /********************设备检查计划:InspectionPlan*******************/
    ErrorCode INSPECTION_PLAN_NOT_EXISTS = new ErrorCode(5001, "设备检查计划不存在");
    ErrorCode INSPECTION_PLAN_DUPLICATE_CODE = new ErrorCode(5001, "计划编码重复");
    ErrorCode INSPECTION_PLAN_CODE_EMPTY = new ErrorCode(5001, "计划编码不能为空");
    ErrorCode INSPECTION_PLAN_TREE_EMPTY = new ErrorCode(5001, "所属节点不能为空");
    ErrorCode INSPECTION_PLAN_DEVICE_EMPTY = new ErrorCode(5001, "设备不能为空");
    ErrorCode INSPECTION_PLAN_ENABLE_EMPTY = new ErrorCode(5001, "启用状态不能为空");
    ErrorCode INSPECTION_PLAN_TYPE_EMPTY = new ErrorCode(5001, "检查类型不能为空");
    ErrorCode INSPECTION_PLAN_SUPERINTENDENT_EMPTY = new ErrorCode(5001, "负责角色不能为空");
    ErrorCode INSPECTION_PLAN_DISABLED = new ErrorCode(5001, "设备检查计划已禁用");

    /**********************设备检查记录:InspectionRecord**********************************/
    ErrorCode INSPECTION_RECORD_NOT_EXISTS = new ErrorCode(5001, "设备检查记录不存在");
    ErrorCode INSPECTION_RECORD_START_EMPTY = new ErrorCode(5001, "开始时间不能为空");
    ErrorCode INSPECTION_RECORD_END_EMPTY = new ErrorCode(5001, "结束时间不能为空");

    /******************设备保养维护计划:MaintenancePlan**************************/
    ErrorCode MAINTENANCE_PLAN_NOT_EXISTS = new ErrorCode(5001, "设备保养维护计划不存在");
    ErrorCode MAINTENANCE_PLAN_DUPLICATE_CODE = new ErrorCode(5001, "计划编码重复");
    ErrorCode MAINTENANCE_PLAN_CODE_EMPTY = new ErrorCode(5001, "计划编码不能为空");
    ErrorCode MAINTENANCE_PLAN_TREE_EMPTY = new ErrorCode(5001, "所属节点不能为空");
    ErrorCode MAINTENANCE_PLAN_DEVICE_EMPTY = new ErrorCode(5001, "设备不能为空");
    ErrorCode MAINTENANCE_PLAN_CRITICAL_DEVICE_EMPTY = new ErrorCode(5001, "关键设备不能为空");
    ErrorCode MAINTENANCE_PLAN_ENABLE_STATUS_EMPTY = new ErrorCode(5001, "启用状态不能为空");
    ErrorCode MAINTENANCE_PLAN_TYPE_EMPTY = new ErrorCode(5001, "维护类型不能为空");
    ErrorCode MAINTENANCE_PLAN_DISABLED = new ErrorCode(5001, "设备保养维护计划已禁用");

    /****************设备保养维护记录:MaintenanceRecord************/
    ErrorCode MAINTENANCE_RECORD_NOT_EXISTS = new ErrorCode(5001, "设备保养维护记录不存在");
    ErrorCode MAINTENANCE_RECORD_STATUS_EMPTY = new ErrorCode(5001, "完成状态不能为空");
    ErrorCode MAINTENANCE_RECORD_START_EMPTY = new ErrorCode(5001, "开始维护时间不能为空");
    ErrorCode MAINTENANCE_RECORD_END_EMPTY = new ErrorCode(5001, "结束维护时间不能为空");

    /*******************备件管理**************************/
    ErrorCode SPARE_PART_NOT_EXISTS = new ErrorCode(5001, "备品/备件不存在");

}
