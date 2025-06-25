package com.miyu.cloud.es.api.brake.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrakeData {

    private Integer resultCode;

    private String Message;

    private Integer pageNum;

    private Integer pageSize;

    private Integer total;

    private Integer totalPage;

    private List<BrakeDTO> data;
}
