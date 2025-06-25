package com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 入库异常处理 DO
 *
 * @author 上海弥彧
 */
@TableName("ppm_inbound_exception_handling")
@KeySequence("ppm_inbound_exception_handling_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InboundExceptionHandlingDO extends BaseDO {

    /**
     * ID
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 产品表ID
     */
    private String infoId;
    /**
     * 入库单ID
     */
    private String consignmentId;
    /**
     * 入库单号
     */
    private String no;
    /**
     * 应收数量
     */
    private BigDecimal consignedAmount;
    /**
     * 实收数量
     */
    private BigDecimal signedAmount;
    /**
     * 处理人
     */
    private String handleBy;
    /**
     * 处理日期
     */
    private LocalDateTime handleTime;
    /**
     * 物料类型ID
     */
    private String materialConfigId;
    /**
     * 状态  0待处理  1已处理
     *
     * 枚举 {@link TODO ppm_exception_handle_status 对应的类}
     */
    private Integer status;
    /**
     * 类型 1采购收货 2外协退货 3原材料入库 4 销售退货
     *
     * 枚举 {@link TODO consignment_type 对应的类}
     */
    private Integer consignmentType;
    /**
     * 处理结果  1入库 2退货
     *
     * 枚举 {@link TODO ppm_exception_handle_result 对应的类}
     */
    private Integer rusultType;
    /**
     * 异常类型 1来货不足 2收货收多 
     */
    private Integer exceptionType;
    /**
     * 合同ID
     */
    private String contractId;
    /**
     * 项目ID
     */
    private String projectId;
    /**
     * 公司ID
     */
    private String companyId;

}