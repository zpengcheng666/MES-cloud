package com.miyu.module.tms.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

/**
 * 错误码枚举类
 * tms 系统，使用 9-001-000-000 段
 *
 * @author Qinyu
 * Created on 2021/1/25 11:10

 */
public interface ErrorCodeConstants {
    // 引用此异常枚举的地方 都有存在bug的风险，需要注意 发生后解决bug 并更新为其他种类提示抛出。
    ErrorCode BUG = new ErrorCode(2_002_000_001, "系统存在业务逻辑错误，请联系管理员处理");
    // 未知状态
    ErrorCode UNKNOWN_STATUS = new ErrorCode(2_002_000_002, "未知状态");
    // 未知类型
    ErrorCode UNKNOWN_TYPE = new ErrorCode(2_002_000_003, "未知类型");
    // 参数不能为空
    ErrorCode PARAM_NOT_NULL = new ErrorCode(2_002_000_004, "参数不能为空");
    // 用户未登录
    ErrorCode USER_NOT_LOGIN = new ErrorCode(2_002_000_005, "用户未登录");

    ErrorCode TOOL_CONFIG_NOT_EXISTS = new ErrorCode(2_002_001_001, "刀具类型不存在");
    ErrorCode TOOL_CONFIG_PARAMETER_NOT_EXISTS = new ErrorCode(2_002_001_002, "刀具参数信息不存在");



    ErrorCode TOOL_PARAM_TEMPLATE_NOT_EXISTS = new ErrorCode(2_003_001_001, "刀具参数模板不存在");
    ErrorCode TOOL_PARAM_TEMPLATE_DETAIL_NOT_EXISTS = new ErrorCode(2_003_001_002, "参数模版详情不存在");

    ErrorCode TOOL_GROUP_NOT_EXISTS = new ErrorCode(2_003_001_003, "刀具组装不存在");
    ErrorCode TOOL_GROUP_DETAIL_NOT_EXISTS = new ErrorCode(2_003_001_008, "刀具组装详情不存在");
    ErrorCode TOOL_GROUP_DUPLICATE = new ErrorCode(2_003_001_009, "当前成品刀具已经存在刀具组装信息");


    ErrorCode GROUP_TOOL_SITE_DUPLICATE = new ErrorCode(2_003_001_004, "刀具装配刀位不能重复");

    ErrorCode GROUP_TOOL_DT_NOT_EXISTS = new ErrorCode(2_003_001_005, "刀具装配必须选择一个刀头");

    ErrorCode GROUP_TOOL_DT_COUNT_ERROR = new ErrorCode(2_003_001_006, "刀具装配只能选择一个刀头");

    ErrorCode GROUP_TOOL_DB_COUNT_ERROR = new ErrorCode(2_003_001_007, "刀具装配只能选择一个刀柄");


    ErrorCode FIT_CONFIG_NOT_EXISTS = new ErrorCode(2_003_001_005, "刀具适配不存在");
    ErrorCode TOOL_PARAM_TEMPLATE_DUPLICATE = new ErrorCode(2_003_002_006, "当前物料类别已经存在参数模板");


    ErrorCode ASSEMBLE_TASK_NOT_EXISTS = new ErrorCode(2_002_002_001, "刀具装配任务不存在");

    ErrorCode TOOL_INFO_NOT_EXISTS = new ErrorCode(2_002_003_001, "刀组信息不存在");
    ErrorCode TOOL_BALANCE_NOT_EXISTS = new ErrorCode(2_002_003_002, "刀具动平衡不存在");
    ErrorCode TOOL_BALANCE_EXISTS = new ErrorCode(2_002_003_003, "刀具动平衡已存在");
    ErrorCode TOOL_BASE_NOT_EXISTS = new ErrorCode(2_002_003_004, "对刀数据不存在");
    ErrorCode TOOL_BASE_EXISTS = new ErrorCode(2_002_003_005, "对刀数据已存在");
    ErrorCode ASSEMBLE_RECORD_NOT_EXISTS = new ErrorCode(2_002_003_006, "刀具装配记录不存在");

    ErrorCode TOOL_RECORD_NOT_EXISTS = new ErrorCode(2_002_004_001, "刀具使用记录不存在");

    //配件库存恢复失败
    ErrorCode PART_STOCK_RESTORE_FAIL = new ErrorCode(2_002_005_001, "配件库存恢复失败");
    //成品刀具删除失败
    ErrorCode TOOL_DELETE_FAIL = new ErrorCode(2_002_005_002, "成品刀具删除失败");
}
