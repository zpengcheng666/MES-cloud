package com.miyu.module.pdm.dal.dataobject.attachment;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@TableName("pdm_receive_attachment")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PartAttachmentDO extends BaseDO {

    /**
     * id
     */
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

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 更新者
     */
    private String updater;

    private String fileSource;
    /**
     * 文件名称
     */
    private String fileName;

    /**
     * vault地址
     */
    private String vaultUrl;
    @TableField(exist = false)
    private String fileType;


}
