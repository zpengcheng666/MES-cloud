package com.miyu.cloud.es.api.brakeN.dto;

import lombok.Data;

import java.util.List;

@Data
public class BrakeNData {

    private Integer PageIndex;

    private Integer PageSize;

    private Integer TotalCount;

    private List<BrakeNDTO> Results;
}
