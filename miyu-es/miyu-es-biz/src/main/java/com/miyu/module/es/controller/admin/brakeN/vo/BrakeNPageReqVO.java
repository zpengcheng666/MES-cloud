package com.miyu.module.es.controller.admin.brakeN.vo;

import com.miyu.cloud.es.api.brake.dto.BrakeDTO;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNDTO;
import lombok.Data;

import java.util.List;

@Data
public class BrakeNPageReqVO {

    private Integer resultCode;

    private String Message;

    private Integer pageNum;

    private Integer pageSize;

    private Integer total;

    private Integer totalPage;

    private List<BrakeNDTO> data;

}
