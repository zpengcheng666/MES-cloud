package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;
@Schema(description = "管理后台 - 产品数据对象 Response VO")
@Data

public class PartBomRespVO {
    /**
     * id
     */
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12076")
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 零部件版本id
     */
    @Schema(description = "零部件版本id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String partVersionId;

    /**
     * 数量
     */
    @Schema(description = "数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private int quantity;

    /**
     * 交付日期
     */
    @Schema(description = "交付日期", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private LocalDateTime deliverTime;

    /**
     * 正式接收记录id
     */
    @Schema(description = "正式接收记录id", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String receiveInfoId;

    /**
     * 数据表表名
     */
    @Schema(description = "数据表表名", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String tableName;

    /**
     * 创建人
     */
    @Schema(description = "创建人", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String creator;

    /**
     * 创建时间
     */
    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String createTime;

    /**
     * 更新人
     */
    @Schema(description = "更新人", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String updater;

    /**
     * 更新时间
     */
    @Schema(description = "更新时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    private String updateTime;

    @TableField(exist = false)
    private String datapackageBomId;

    @TableField(exist = false)
    private String fileName;

    @TableField(exist = false)
    private String fileType;

    @TableField(exist = false)
    private String vaultUrl;

    @TableField(exist = false)
    private String fileId;
}
