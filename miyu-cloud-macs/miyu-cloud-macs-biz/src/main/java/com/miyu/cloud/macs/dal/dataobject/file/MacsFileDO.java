package com.miyu.cloud.macs.dal.dataobject.file;

import lombok.*;
import com.baomidou.mybatisplus.annotation.*;

/**
 * 文件 DO
 *
 * @author miyu
 */
@TableName("macs_file")
@KeySequence("macs_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MacsFileDO {

    /**
     * 主键id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 文件名
     */
    private String name;
    /**
     * 文件路径
     */
    private String path;
    /**
     * 文件类型
     */
    private String type;
    /**
     * 文件大小
     */
    private Long size;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 访客id
     */
    private String visitorId;

    /**
     * 加密盐
     */
    private String salt;

    /**
     * 文件信息类别 1.人脸 2.指纹
     */
    private Integer infoType;

}
