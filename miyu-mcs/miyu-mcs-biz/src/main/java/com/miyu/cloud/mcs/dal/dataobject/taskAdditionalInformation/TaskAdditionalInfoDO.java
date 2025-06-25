package com.miyu.cloud.mcs.dal.dataobject.taskAdditionalInformation;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 任务附加信息 DO
 *
 * @author miyu
 */
@TableName("mcs_task_additional_information")
@KeySequence("mcs_task_additional_information") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskAdditionalInfoDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 任务id
     */
    private String recordId;
    /**
     * 类别编码
     */
    private String typeCode;
    /**
     * 存储方式
     */
    private Integer storageMode;
    /**
     * 路径
     */
    private String path;
    /**
     * 内容
     */
    private String content;

}
