package com.miyu.module.pdm.enums;

public interface LogRecordConstants {

    // ======================= PDM 加工路线 =======================
    String PDM_PROCESS_ROUTE_TYPE = "加工路线";
    String PDM_PROCESS_ROUTE_UPDATE_SUB_TYPE = "更新加工路线";
    String PDM_PROCESS_ROUTE_UPDATE_SUCCESS = "更新了加工路线【{{#processRouteName}}】";
    String PDM_PROCESS_ROUTE_DELETE_SUB_TYPE = "删除加工路线";
    String PDM_PROCESS_ROUTE_DELETE_SUCCESS = "删除了加工路线【{{#processRouteName}}】";

    // ======================= PDM 典型工艺路线 =======================
    String PDM_PROCESS_ROUTE_TYPICAL_TYPE = "典型工艺路线";
    String PDM_PROCESS_ROUTE_TYPICAL_UPDATE_SUB_TYPE = "更新典型工艺路线";
    String PDM_PROCESS_ROUTE_TYPICAL_UPDATE_SUCCESS = "更新了典型工艺路线【{{#processRouteTypicalName}}】";
    String PDM_PROCESS_ROUTE_TYPICAL_DELETE_SUB_TYPE = "删除典型工艺路线";
    String PDM_PROCESS_ROUTE_TYPICAL_DELETE_SUCCESS = "删除了典型工艺路线【{{#processRouteTypicalName}}】";

    // ======================= PDM 工步类型 =======================
    String PDM_STEP_CATEGORY_TYPE = "工步类型";
    String PDM_STEP_CATEGORY_UPDATE_SUB_TYPE = "更新工步类型";
    String PDM_STEP_CATEGORY_UPDATE_SUCCESS = "更新了工步类型【{{#stepCategoryName}}】";
    String PDM_STEP_CATEGORY_DELETE_SUB_TYPE = "删除工步类型";
    String PDM_STEP_CATEGORY_DELETE_SUCCESS = "删除了工步类型【{{#stepCategoryName}}】";

    // ======================= PDM 工装分类 =======================
    String PDM_TOOLING_CATEGORY_TYPE = "工装分类";
    String PDM_TOOLING_CATEGORY_UPDATE_SUB_TYPE = "更新工装分类";
    String PDM_TOOLING_CATEGORY_UPDATE_SUCCESS = "更新了工装分类【{{#toolingCategoryName}}】";
    String PDM_TOOLING_CATEGORY_DELETE_SUB_TYPE = "删除工装分类";
    String PDM_TOOLING_CATEGORY_DELETE_SUCCESS = "删除了工装分类【{{#toolingCategoryName}}】";

    // ======================= PDM 工装申请 =======================
    String PDM_TOOLING_APPLY_TYPE = "工装申请";
    String PDM_TOOLING_APPLY_UPDATE_SUB_TYPE = "更新工装申请";
    String PDM_TOOLING_APPLY_UPDATE_SUCCESS = "更新了工装申请【{{#toolingApplyName}}】";
    String PDM_TOOLING_APPLY_DELETE_SUB_TYPE = "删除工装申请";
    String PDM_TOOLING_APPLY_DELETE_SUCCESS = "删除了工装申请【{{#toolingApplyName}}】";

    // ======================= PDM 工艺方案 =======================
    String PDM_PROCESS_PLAN_TYPE = "工艺方案";
    String PDM_PROCESS_PLAN_UPDATE_SUB_TYPE = "更新工艺方案";
    String PDM_PROCESS_PLAN_UPDATE_SUCCESS = "更新了工艺方案【{{#processCode}}】";
    String PDM_PROCESS_PLAN_DELETE_SUB_TYPE = "删除工艺方案";
    String PDM_PROCESS_PLAN_DELETE_SUCCESS = "删除了工艺方案【{{#processCode}}】";
    String PDM_PROCESS_PROCEDURE_UPDATE_SUB_TYPE = "更新工序";
    String PDM_PROCESS_PROCEDURE_UPDATE_SUCCESS = "更新了工序【{{#procedureName}}】";
    String PDM_PROCESS_PROCEDURE_DELETE_SUB_TYPE = "删除工序";
    String PDM_PROCESS_PROCEDURE_DELETE_SUCCESS = "删除了工序【{{#procedureName}}】";
    String PDM_PROCESS_STEP_UPDATE_SUB_TYPE = "更新工步";
    String PDM_PROCESS_STEP_UPDATE_SUCCESS = "更新了工步【{{#stepName}}】";
    String PDM_PROCESS_STEP_DELETE_SUB_TYPE = "删除工步";
    String PDM_PROCESS_STEP_DELETE_SUCCESS = "删除了工步【{{#stepName}}】";

    // ======================= PDM 补加工工艺规程 =======================
    String PDM_PROCESS_SUPPLEMENT_TYPE = "补加工工艺规程";
    String PDM_PROCESS_SUPPLEMENT_UPDATE_SUB_TYPE = "更新补加工工艺规程";
    String PDM_PROCESS_SUPPLEMENT_UPDATE_SUCCESS = "更新了补加工工艺规程【{{#processCode}}】";
    String PDM_PROCESS_SUPPLEMENT_DELETE_SUB_TYPE = "删除补加工工艺规程";
    String PDM_PROCESS_SUPPLEMENT_DELETE_SUCCESS = "删除了补加工工艺规程【{{#processCode}}】";
}
