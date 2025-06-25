package com.miyu.cloud.es.enums;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;

public interface ErrorCodeConstants {

    ErrorCode VISIT_NOT_EXISTS = new ErrorCode(1_000_000_001, "访客记录不存在");

    ErrorCode BRAKE_NOT_EXISTS = new ErrorCode(1_000_000_002, "车牌数据不存在");

    ErrorCode OPEN_NOT_EXISTS = new ErrorCode(1_000_000_003, "调取三方接口失败");

    ErrorCode BRAKESYNC_NOT_EXISTS = new ErrorCode(1_000_000_004, "修改配置数据失败");

    ErrorCode SYNCBRAKE_NOT_EXISTS = new ErrorCode(1_000_000_005, "同步数据数据失败");

    ErrorCode DATA_NOT_EXISTS = new ErrorCode(1_000_000_006, "该数据不存在，请重置页面后重试");
}
