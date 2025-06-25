package com.miyu.cloud.mcs.controller.admin.batchorder.vo;

import lombok.Data;

import java.util.List;

@Data
public class SplitOrderVO {

    private String batchRecordId;
    private String batchDetailId;
    private List<BatchOrderSaveReqVO> batchList;
}
