package com.miyu.cloud.es.api.brake.modify;

import lombok.Data;

@Data
public class ModifyReqData {

    /**
     * 当前月卡通行证Id
     */
    private String passportId;

    /**
     * 续费时间  yyyy-MM-dd HH:mm:ss
     */
    private String operationTime;

    /**
     * 操作类型 D:删除，U:更新及新注册
     * 为D时，请求中只有passport_id,op_type和operation_time
     */
    private String opType;

    /**
     * 车牌号码
     */
    private String carPlateNo;

    /**
     * 通行证类型
     */
    private String passportTypeName;

    /**
     * 启用时间 yyyy-MM-dd
     */
    private String startDate;

    /**
     * 过期时间 yyyy-MM-dd
     */
    private String deadline;

    /**
     * 注册时间 yyyy-MM-dd HH:mm:ss
     */
    private String registeredTime;

    /**
     * 客户名称
     */
    private String clientName;

    /**
     * 客户编号
     */
    private String clientNo;
}
