package com.miyu.module.tms.controller.admin.toolinfo.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import lombok.Data;
@Data
@ExcelIgnoreUnannotated
public class AssembleRecordVO {



    private String id;
    /***
     * 条码  查询用
     */
    private String appendageBarCode;
    /**
     * 物料类型码 查询用
     */
    private String appendageMaterialNumber;
    /**
     * 物料类型码 查询用
     */
    private String appendageMaterialCode;
    /**
     * 物料类型名 查询用
     */
    private String appendageMaterialName;
    /**
     * 刀位
     */
    private Integer site;
    /**
     * 数量
     */
    private Integer count;
    /***
     * 库存ID
     */
    private String materialStockId;
    /***
     * 储位
     */
    private String storageId;

    /***
     * 类型   1 装 2 卸刀  3 当前装  4 报废
     */
    private Integer type;

}
