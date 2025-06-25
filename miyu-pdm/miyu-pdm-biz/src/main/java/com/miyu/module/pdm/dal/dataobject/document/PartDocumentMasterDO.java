package com.miyu.module.pdm.dal.dataobject.document;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("pdm_document_master")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartDocumentMasterDO extends BaseDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 文档名称
     */
    private String documentName;

    /**
     * 域id
     */
    private String domainId;

    /**
     * 文档类型
     */
    private String documentType;

    /**
     * 描述
     */
    private String description;

    /**
     * 产品id
     */
    private String rootProductId;

    /**
     * 关键字
     */
    private String keywords;

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
}
