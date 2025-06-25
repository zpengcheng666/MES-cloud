package com.miyu.module.pdm.controller.admin.dataStatistics.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设计数据包接收记录 Response VO")
@Data
//该注解的属性不会被导入到Excel中
@ExcelIgnoreUnannotated
public class DataStatisticsRespVO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "产品图号")
    //该注解用于和excel中的列一一对应
//    @ExcelProperty("产品图号")
    private String productNumber;

    @Schema(description = "项目编号")
//    @ExcelProperty("项目编号")
    private String projectCode;

    @Schema(description = "厂家名称")
    @ExcelProperty("厂家名称")
    private String companyName;

    @Schema(description = "数据包接收编号-日期+流水")
    @ExcelProperty("数据包编号")
    private String receiveCode;

    @Schema(description = "数据包名称", example = "张三")
    @ExcelProperty("数据包名称")
    private String name;

    @Schema(description = "数据包大小")
    @ExcelProperty("数据包大小")
    private String size;

    @TableField(exist = false)
    @Schema(description = "零件数量")
    @ExcelProperty("零件数量")
    private String partCount;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    @Schema(description = "耗时")
    @ExcelProperty("耗时")
    private String useTime;

    @Schema(description = "即处理过程：解压数据包、数据包校验、装配结构解析、设计数模轻量化转换、成功/失败", example = "1")
    @ExcelProperty("处理结果")
    private String status;

    @Schema(description = "创建者")
    @ExcelProperty("创建者")
    private String creator;

    @Schema(description = "数据包类型：0首发包、1更改包、2ECO贯彻包", example = "2")
    private Boolean type;

    @Schema(description = "数据包md5")
    private String md5;

    @Schema(description = "电子仓库地址", example = "https://www.iocoder.cn")
    private String vaultUrl;

    @Schema(description = "项目名称", example = "赵六")
    private String projectName;

    @Schema(description = "数据包结构id", example = "20041")
    private Long structureId;

}