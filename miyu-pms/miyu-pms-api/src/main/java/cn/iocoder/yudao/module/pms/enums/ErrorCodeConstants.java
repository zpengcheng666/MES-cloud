package cn.iocoder.yudao.module.pms.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

import java.util.Calendar;

/**
 * Report 错误码枚举类
 *
 * report 系统，使用 1-003-000-000 段
 */
public interface ErrorCodeConstants {

    // ========== GoView 模块 1-003-000-000 ==========
    ErrorCode GO_VIEW_PROJECT_NOT_EXISTS = new ErrorCode(1_003_000_000, "GoView 项目不存在");

    ErrorCode APPROVAL_NOT_EXISTS = new ErrorCode(2_005_000_000, "项目不存在");
    ErrorCode Contract_NOT_EXISTS = new ErrorCode(2_005_000_001, "合同不存在");
    ErrorCode APPROVAL_START_ASSESSMENT = new ErrorCode(2_005_000_002, "项目已开始审批，无法执行此操作");
    ErrorCode ORDER_NOT_EXISTS = new ErrorCode(2_005_001_000, "项目订单不存在");
    ErrorCode ORDER_LIST_NOT_EXISTS = new ErrorCode(2_005_001_001, "项目订单表子不存在");
    ErrorCode Handle_ORDER_MCC_NOT_EXISTS = new ErrorCode(2_005_001_002, "MCC服务未启动,暂时无法处理订单");
    ErrorCode ASSESSMENT_NOT_EXISTS = new ErrorCode(2_005_002_000, "项目评审不存在");
    ErrorCode ASSESSMENT_DEVICE_NOT_EXISTS = new ErrorCode(2_005_002_001, "评审子表，关联的设备不存在");
    ErrorCode PLAN_NOT_EXISTS = new ErrorCode(2_005_003_000, "项目计划不存在");
    ErrorCode PLAN_ITEM_NOT_EXISTS = new ErrorCode(2_005_003_001, "项目计划明细不存在");
    ErrorCode INVENTORY_NOT_ENOUGH = new ErrorCode(2_005_003_002, "物料库存不足,请刷新后查看库存");
    ErrorCode UN_CHOOSE_PROCESS_SCHEME = new ErrorCode(2_005_003_003, "未选择工艺方案");
    ErrorCode START_PURCHASE_SAVE_FAILSE = new ErrorCode(2_005_003_004, "发起采购存储源单时失败");
    ErrorCode NOTIFY_MESSAGE_NOT_EXISTS = new ErrorCode(2_005_003_005, "项目计划提醒不存在");
    ErrorCode ORDER_MATERIAL_RELATION_NOT_EXISTS = new ErrorCode(2_005_004_000, "订单物料关系表不存在");
    ErrorCode MATERIAL_UNSELECTED = new ErrorCode(2_005_004_001, "未选择物料");
    ErrorCode PURCHASE_AMOUNT_INCORRECT = new ErrorCode(2_005_004_002, "采购数量不正确");
    ErrorCode CODE_HAS_WORKING = new ErrorCode(2_005_004_003, "正在加工中,无法释放");
    ErrorCode CALENDAR_DEVICE_NOT_EXISTS = new ErrorCode(2_005_005_000,"未选择设备");
}
