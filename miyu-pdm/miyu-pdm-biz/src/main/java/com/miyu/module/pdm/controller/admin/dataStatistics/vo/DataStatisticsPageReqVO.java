package com.miyu.module.pdm.controller.admin.dataStatistics.vo;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 设计数据包统计分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataStatisticsPageReqVO extends PageParam {
    @Schema(description = "数据包结构id", example = "20041")
    private String id;

    @Schema(description = "项目",example = "赵六")
    @TableField(exist = false)
    private String projectName;

    @TableField(exist = false)
    @Schema(description = "创建者")
    private String creator;


    @Schema(description = "数据包名称", example = "张三")
    private String name;

    @Schema(description = "数据包大小")
    private String size;

    @Schema(description = "数据包类型", example = "2")
    private Boolean type;

    @Schema(description = "数据包md5")
    private String md5;

    @Schema(description = "电子仓库地址")
    private String vaultUrl;

    @Schema(description = "数据包编号")
    private String receiveCode;

    @Schema(description = "即处理过程", example = "1")
    private String status;

    @Schema(description = "产品图号")
    private String productNumber;

    @Schema(description = "产品图号")
    private String rootProductId;

    @Schema(description = "拥有者")
    private String updater;

    @TableField(exist = false)
    @Schema(description = "产品")
    private String productName;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "数据包结构id", example = "20041")
    private Long structureId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime createTime;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String useTime;

    @Schema(description = "厂家id")
    private String companyId;

}
