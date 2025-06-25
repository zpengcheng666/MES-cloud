package com.miyu.module.pdm.dal.dataobject.document;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("pdm_document_version")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDocumentVersionDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     *DMID-文档id
     */
    private String documentMasterId;

    /**
     *版本号
     */
    private String documentVersion;

    /**
     *域id
     */
    private String domainId;

    /**
     *描述
     */
    private String description;

    /**
     *关键字
     */
    private String keywords;

    /**
     *成熟度 目前是M1-M5
     */
    private String maturity;

    /**
     * 状态
     */
    private String status;

    /**
     * 是否锁定
     */
    private String isLocked;

    /**
     * 创建人
     */
    private String creator;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private String partVersionId;

    @TableField(exist = false)
    private String documentName;

    @TableField(exist = false)
    private String documentType;


}
