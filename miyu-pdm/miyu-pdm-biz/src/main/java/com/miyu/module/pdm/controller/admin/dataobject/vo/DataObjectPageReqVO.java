package com.miyu.module.pdm.controller.admin.dataobject.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 产品数据对象分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class DataObjectPageReqVO extends PageParam {

    @Schema(description = "产品表ID", example = "19715")
    private String rootproductId;

    @Schema(description = "标准数据对象")
    private String stdDataObject;

    @Schema(description = "客户化数据对象")
    private String customizedDataObject;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "客户化类型:0固有 1客户化 ", example = "2")
    private Integer customizedType;

    @Schema(description = "数据表表名", example = "王五")
    private String tableName;

    @Schema(description = "客户化数据对象说明", example = "你猜")
    private String description;

    @Schema(description = "数据表实例化状态(1已实例化，不可更改；0未实例化)", example = "2")
    private String status;

    @Schema(description = "属性内容json(固有属性)")
    private String intrinsicAttrs;

    @Schema(description = "属性内容json(客户化属性)")
    private String customizedAttrs;

    @Schema(description = "排序")
    private Integer serialNumber;

    @Schema(description = "创建时间")

    private LocalDateTime[] createTime;

    @TableField(exist = false)
    private List list;

    @TableField(exist = false)
    @Schema(description = "旧客户化标识")
    private String customizedIndexOld;


}