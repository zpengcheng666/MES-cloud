package com.miyu.module.ppm.controller.admin.consignmentreturn.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 销售入库单详细 Resp VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ConsignmentMaterialRespVO extends PageParam {


    private String no;

    /**
     * 物料库存ID
     */
    private String  materialStockId;

    /***
     * 物料类型Id
     */
    private String  materialConfigId;

    /**
     * 物料条码
     */
    private String barCode;

    /**
     * 物料批次号
     */
    private String batchNumber;

    /**
     * 数量
     */
    private Integer quantity;


    private String materialTypeName;


    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "王五")
    private String materialName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    private String materialParentTypeName;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    /***
     * 是否合格
     */
    private Integer inspectionResult;
}