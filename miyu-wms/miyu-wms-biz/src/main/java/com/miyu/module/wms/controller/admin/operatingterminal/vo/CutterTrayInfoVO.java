package com.miyu.module.wms.controller.admin.operatingterminal.vo;

import lombok.Data;

import java.util.List;

@Data
public class CutterTrayInfoVO {

    private String locationId;
    private String locationCode;
    // 刀具托盘id
    private String cutterTrayId;
    // 刀具托盘编码
    private String cutterTrayCode;

    // 托盘上储位信息
    private List<CutterTrayDetailInfoVO> cutterTrayDetailInfoS;
}
