package com.miyu.module.qms.dal.dataobject.managementdatabasefile;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 质量管理资料库附件 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_management_database_file")
@KeySequence("qms_management_database_file_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementDatabaseFileDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 质料库ID
     */
    private String databaseId;
    /**
     * 附件
     */
    private String fileUrl;

}