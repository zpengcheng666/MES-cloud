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
@ExcelIgnoreUnannotated
public class PartAttachmentRespVO {
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 正式接收数据包产品目录id
     */
    private String datapackageBomId;

    /**
     * 文件表id
     */
    private String fileId;

    /**
     * 创建者
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private String fileSource;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    private String updater;

    @TableField(exist = false)
    private String fileName;

    @TableField(exist = false)
    private String fileType;

    @TableField(exist = false)
    private String vaultUrl;

}
