package com.miyu.module.qms.dal.dataobject.managementtree;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 质量管理关联树 DO
 *
 * @author Zhangyunfei
 */
@TableName("qms_management_tree")
@KeySequence("qms_management_tree_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ManagementTreeDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 父节点
     */
    private String parent;

    /**
     * 节点关联字段ID
     */
    private String nodeId;

    /**
     * 节点名
     */
    private String nodeName;

    /**
     * 节点类型
     */
    private Integer nodeType;
    /**
     * 备注
     */
    private String remark;

}