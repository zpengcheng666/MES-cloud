package com.miyu.module.pdm.dal.dataobject.toolingDeatail;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@TableName("pdm_ofeu_part_instance")
@KeySequence("pdm_ofeu_part_instance_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToolingDetailDO extends BaseDO {



    @TableField(exist = false)
    private String piid;

    @TableField(exist = false)
    private String instanceNumber;

    @TableField(exist = false)
    private String rootProductId;

    @TableField(exist = false)
    private String partVersionId;

    @TableField(exist = false)
    private String parentPiid;


    @TableField(exist = false)
    private String serialNumber;

    @TableField(exist = false)
    private String vmatrix01;

    @TableField(exist = false)
    private String vmatrix02;

    @TableField(exist = false)
    private String vmatrix03;

    @TableField(exist = false)
    private String vmatrix04;

    @TableField(exist = false)
    private String vmatrix05;

    @TableField(exist = false)
    private String vmatrix06;

    @TableField(exist = false)
    private String vmatrix07;

    @TableField(exist = false)
    private String vmatrix08;

    @TableField(exist = false)
    private String vmatrix09;

    @TableField(exist = false)
    private String vmatrix10;

    @TableField(exist = false)
    private String vmatrix11;

    @TableField(exist = false)
    private String vmatrix12;

    @TableField(exist = false)
    private String type;

    @TableField(exist = false)
    private String partName;

    @TableField(exist = false)
    private String partNumber;

    @TableField(exist = false)
    private String partVersion;

    @TableField(exist = false)
    private String documentVersionId;

    @TableField(exist = false)
    private String vaultUrl;
}
