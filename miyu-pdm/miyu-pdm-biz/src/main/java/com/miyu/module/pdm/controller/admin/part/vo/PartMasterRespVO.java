package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品数据对象 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PartMasterRespVO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 零件图号
     */
    private String partNumber;

    /**
     * 零件名称
     */
    private String partName;

    /**
     * 根节点id
     */
    private String rootProductId;


    /**
     * 域id
     */
    private String domainId;

    /**
     * 零部件类型
     */
    private Integer partType;

    /**
     * 加工类型
     */
    private Integer processType;

    /**
     * 材料代码
     */
    private String materialCode;

    /**
     * 材料分类
     */
    private String materialClassIf;

    /**
     * 材料牌号
     */
    private String materialDesg;

    /**
     * 材料状态
     */
    private String materialCondition;
    /**
     * 材料标准
     */
    private String materialSpec;

    /**
     * 材料规格
     */
    private String materialDimension;

    /**
     * 材料id
     */
    private String materialId;
    /**
     * 创建者
     */
    private String creator;
    /**
     * 创建时间
     */
    private LocalDateTime createTime;
    /**
     * 更新者
     */
    private String updater;
    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}