package cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 评审子表，关联的设备 DO
 *
 * @author 技术部长
 */
@TableName("project_assessment_device")
@KeySequence("project_assessment_device_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssessmentDeviceDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 项目编号
     */
    private String projectCode;
    /**
     * 制造资源类型：1设备 2刀具 3工装
     */
    private Integer resourcesType;
    /**
     * 制造资源id(设备、刀具、工装等)
     */
    private String resourcesTypeId;
    /**
     * 数量,因为查询的数据也叫quantity,两边冲突，所以改名了
     */
//    private Integer quantity;
    private Integer amount;
    /**
     * 采购数量,这个才是采购,就算已经有了,也可能因为数量不够而采购，上面那个数量只是需要多少个，根本不是采购。
     */
    private Integer purchaseAmount;
    /**
     * 估值
     */
    private BigDecimal predictPrice;
    /**
     * 损耗
     */
    private BigDecimal wastage;
    /**
     * 加工时间
     */
    private Long processingTime;
    /**
     * 评审表id
     */
    private String assessmentId;
    /**
     * 零部件图号
     */
    private String partNumber;
    /**
     * 审批建议
     */
    private String suggestion;
    /**
     * 采购类型
     */
    private Integer purchaseType;

}
