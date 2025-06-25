package com.miyu.cloud.dc.api.devicedate.dto;

import lombok.Data;

import java.util.List;

@Data
public class DeviceDateRespDTO {

    private String id;

    private List<Object> items;
}
