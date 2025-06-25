package com.miyu.module.wms.api.mateiral.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 库区 DO
 *
 * @author QianJy
 */
@Schema(description = "RPC 服务 - 获取库区")
@Data
public class WarehouseAreaRespDTO {

    /**
     * ID
     */
    private String id;
    /**
     * 仓库id
     */
    private String warehouseId;
    /**
     * 库区名称
     */
    private String areaName;
    /**
     * 库区编码
     */
    private String areaCode;
    /**
     * 库区属性
     */
    private Integer areaProperty;
    /**
     * 库区长
     */
    private Integer areaLength;
    /**
     * 库区宽
     */
    private Integer areaWidth;
    /**
     * 库区高
     */
    private Integer areaHeight;
    /**
     * 库区承重
     */
    private Integer areaBearing;
    /**
     * 通道
     */
    private Integer areaChannels;
    /**
     * 组
     */
    private Integer areaGroup;
    /**
     * 层
     */
    private Integer areaLayer;
    /**
     * 位
     */
    private Integer areaSite;
    /**
     * 库区类型
     */
    private Integer areaType;
    /**
     * 刀具配送顺序
     */
    private Integer deliverySequence;

    // 库位id
    private String joinLocationId;
}
