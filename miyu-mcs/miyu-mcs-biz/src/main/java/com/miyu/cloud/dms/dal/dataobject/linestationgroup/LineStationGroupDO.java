package com.miyu.cloud.dms.dal.dataobject.linestationgroup;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * 产线/工位组 DO
 *
 * @author 芋道源码
 */
@TableName("dms_line_station_group")
@KeySequence("dms_line_station_group_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LineStationGroupDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 编号
     */
    private String code;
    /**
     * 名称
     */
    private String name;
    /**
     * 产线/工位组
     * <p>
     * 枚举 {@link TODO dms_line_station_group 对应的类}
     */
    private Integer type;
    /**
     * 是否启用
     * <p>
     * 枚举 {@link TODO enableStatus 对应的类}
     */
    private Integer enable;
    /**
     * 所属类型
     */
    private String affiliationDeviceType;
    /**
     * 本机ip
     */
    private String ip;
    /**
     * 位置
     */
    private String location;
    /**
     * 备注
     */
    private String remark;
    /**
     * 参数1 报告文件类型
     */
    private String technicalParameter1;
    /**
     * 参数2 报告路径
     */
    private String technicalParameter2;

}
