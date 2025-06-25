package com.miyu.cloud.es.api.brake.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrakeRes {

    private Integer resultCode;

    private String Message;

    private Integer pageNum;

    private Integer pageSize;

    private Integer totalSize;

    private Integer totalPage;

    private List<BrakeDTO> data;
}
