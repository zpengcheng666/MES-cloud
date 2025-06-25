package com.miyu.cloud.es.api.brakeN.issue;

import lombok.Data;

@Data
public class LogOffData {

    /**
     * 车牌号码
     */
    private String LicensePlateNumber;

    /**
     * 操作人姓名
     */
    private String OperatorName;

    /**
     * 操作时间
     */
    private String OperationTime;

}
