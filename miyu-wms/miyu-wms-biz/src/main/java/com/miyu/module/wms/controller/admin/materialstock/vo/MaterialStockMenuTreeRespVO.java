package com.miyu.module.wms.controller.admin.materialstock.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;


@Schema(description = "管理后台 -  库存树 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialStockMenuTreeRespVO {


    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private Long id;

    /**
     * 类型：0根节点，1仓库，2库区，3库位
     */
    @Schema(description = "类型")
    @ExcelProperty("类型")
    private Integer type = 0;

    /**
     * 节点名字
     */
    @Schema(description = "节点名字", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("节点名字")
    private String name;

    /**
     * 库存信息
     */
    private List<MaterialStockDO> childrens;
}
