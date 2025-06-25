package com.miyu.module.pdm.dal.dataobject.dataStatistics;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.*;
import lombok.*;
import java.time.LocalDateTime;

@TableName("pdm_receive_info")
@KeySequence("pdm_receive_info_seq")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
//@Builder生成一个私有的构造函数
@Builder
//生成一个无参的构造方法
@NoArgsConstructor
//生成一个包含全部字段的构造方法
@AllArgsConstructor
public class DataStatisticsDO extends BaseDO {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;

    /**
     * 数据包名称
     */
    private String name;

    /**
     * 数据包大小
     */
    private String size;

    /**
     * 数据包类型 0 首发包 1 更改包 2 ECO贯彻包
     */
    private Boolean type;

    /**
     * 数据包MD5
     */
    private String md5;

    /**
     * 电子仓库地址
     */
    private String vaultUrl;

    /**
     * 数据包接收编号-日期+流水
     */
    private String receiveCode;

    /**
     * 即处理过程：解压数据包、数据包校验、装配结构解析、设计数模轻量化转换、成功/失败
     */
    private String status;

    /**
     * 产品图号
     */
    private String productNumber;

    /**
     * 项目编号
     */
    private String projectCode;

    /**
     * 项目名称
     */
    @TableField(exist = false)
    private String projectName;

    /**
     * 厂家id
     */
    private String companyId;

    /**
     * 厂家名称
     */
    private String companyName;

    /**
     * 数据包结构id
     */
    private String structureId;

    /**
     * 拥有者
     */
    private String updater;

    /**
     * 处理时间
     */
    private LocalDateTime createTime;

    /**
     * 数据包接收编号
     */
    @TableField(exist = false)
    private String receiveInfoId;

    /**
     * 零件数量
     */
    @TableField(exist = false)
    private String partCount;

    /**
     * 耗时
     */
    @TableField(exist = false)
    private String useTime;
}
