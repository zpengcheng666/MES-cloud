package com.miyu.module.mcc.controller.admin.materialtype.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - 编码类别属性表(树形结构 VO")
@Data
public class MaterialListTreeVO {

    /**
     * ID
     */
    private String id;
    /**
     * 编码
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 父类型id
     */
    private String parentId;
    /**
     * 位数
     */
    private Integer bitNumber;
    /***
     * 层级
     */
    private Integer level;
    /**
     * 总层级
     */
    private Integer levelLimit;

    /***
     * 分类
     */
    private Integer encodingProperty;



    private List<MaterialListTreeVO> children;

}