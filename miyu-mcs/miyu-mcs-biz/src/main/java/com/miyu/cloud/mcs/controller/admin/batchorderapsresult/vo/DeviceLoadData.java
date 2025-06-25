package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class DeviceLoadData {

    //批次(子订单)id
    private String orderFormId;
    //时长(int|String)
    private Object value;
    //子订单编码
    private String orderNumber;
    //零件图号
    private String partNumber;
    //工序顺序号
    private String processNumber;
    //工序顺序号
    private String stepNumber;
    //零件顺序号
    private String partIndex;
    //起始时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date startTime;
    //结束时间
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    //是否显示
    private Boolean show;

}
