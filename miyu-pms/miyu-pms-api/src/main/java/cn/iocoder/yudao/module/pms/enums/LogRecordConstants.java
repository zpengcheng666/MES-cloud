package cn.iocoder.yudao.module.pms.enums;

/**
 * System 操作日志枚举
 * 目的：统一管理，也减少 Service 里各种“复杂”字符串
 *
 * @author 芋道源码
 */
public interface LogRecordConstants {

    // ======================= 项目 =======================
    String PMS_PROJECT_TYPE = "项目";
    String PMS_PROJECT_UPDATE_SUB_TYPE = "更新项目";
    String PMS_PROJECT_UPDATE_SUCCESS = "更新了项目【{{#project.projectName}}:{{#project.projectCode}}】: {_DIFF{#updateReqVO}}";
    String PMS_PROJECT_DELETE_SUB_TYPE = "删除项目";
    String PMS_PROJECT_DELETE_SUCCESS = "删除了项目【{{#project.projectName}}:{{#project.projectCode}}】";

    // ======================= 项目订单 =======================
    String PMS_PROJECT_ORDER_TYPE = "项目订单";
    String PMS_PROJECT_ORDER_UPDATE_SUB_TYPE = "更新项目订单";
    String PMS_PROJECT_ORDER_UPDATE_SUCCESS = "更新了项目订单【{{#order.id}}】: {_DIFF{#updateReqVO}}";
    String PMS_PROJECT_ORDER_DELETE_SUB_TYPE = "删除项目订单";
    String PMS_PROJECT_ORDER_DELETE_SUCCESS = "删除了项目订单【{{#order.id}}】";

    // ======================= 项目计划 =======================
    String PMS_PROJECT_PLAN_TYPE = "项目计划";
    String PMS_PROJECT_PLAN_UPDATE_SUB_TYPE = "更新项目计划";
    String PMS_PROJECT_PLAN_UPDATE_SUCCESS = "更新了项目计划【{{#plan.id}}】: {_DIFF{#updateReqVO}}";
    String PMS_PROJECT_PLAN_DELETE_SUB_TYPE = "删除项目计划";
    String PMS_PROJECT_PLAN_DELETE_SUCCESS = "删除了项目计划【{{#plan.id}}】";



}
