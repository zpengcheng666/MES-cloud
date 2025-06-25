package com.miyu.module.wms.controller.admin.home.vo;

import lombok.Data;


/**
 * 入库出库统计VO
 */
@Data
public class InOutWarehouseStatisticsVO {

    /**
     * 仓库ID
     */
    private String warehouseId;
    private String warehouseName;

    /**
     * 物料批次号(冗余方便查询)
     */
    private String batchNumber;
    /**
     * 物料类型id(冗余方便查询)
     */
    private String materialNumber;

    /**
     * 出库总数
     */
    private Integer inTotalCount = 0;
    /**
     * 入库总数
     */
    private Integer outTotalCount = 0;

    /**
     * 库存总数
     */
    private Integer totalStockCount = 0;
}
