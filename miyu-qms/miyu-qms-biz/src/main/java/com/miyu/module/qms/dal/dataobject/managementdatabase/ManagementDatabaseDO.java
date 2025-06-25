package com.miyu.module.qms.dal.dataobject.managementdatabase;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 质量管理资料库 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_management_database")
@KeySequence("qms_management_database_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementDatabaseDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 所属质量管理关联ID
     */
    private String treeId;
    /**
     * 类型
     */
    private Integer type;

    /**
     * 备注
     */
    private String remark;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 工作流编号
     */
    private String processInstanceId;

}