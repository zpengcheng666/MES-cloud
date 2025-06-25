package com.miyu.cloud.mcs.controller.admin.batchrecord.vo;

import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class BatchRecordDeviceTypeReqVO {

    private String id;
    private String code;
    private String name;
    private String type;
    private String status;
    private String enable;
    private List<LedgerDO> children = new ArrayList<>();

    public void setEnable(String enable) {
        this.enable = enable;
        this.status = enable;
    }
}
