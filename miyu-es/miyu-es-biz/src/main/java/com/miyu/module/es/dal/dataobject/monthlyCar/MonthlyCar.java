package com.miyu.module.es.dal.dataobject.monthlyCar;

import lombok.Data;

@Data
public class MonthlyCar {

    //停车场编号
    private String ParkKey;

    //应用服务标识
    private String AppId;

    //时间戳
    private String TimeStamp;

    //随机字符串
    private String Nonce;

    //接口签名
    private String Sign;

    //通知类型
    private MonthlyData Data;

}
