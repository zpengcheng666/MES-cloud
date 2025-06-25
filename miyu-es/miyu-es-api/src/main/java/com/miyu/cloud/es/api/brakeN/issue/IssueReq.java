package com.miyu.cloud.es.api.brakeN.issue;

import lombok.Data;

@Data
public class IssueReq {

    /**
     * 停车场编号
     */
    private String ParkKey;

    /**
     * 应用服务标识
     */
    private String AppId;

    /**
     * 时间戳
     */
    private String TimeStamp;

    /**
     *随机字符串
     */
    private String Nonce;

    /**
     * 接口签名
     */
    private String Sign;

    /**
     * 数据
     */
    private IssueData Data;
}
