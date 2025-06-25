package cn.iocoder.yudao.module.pms.dal.dataobject.order;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * 项目订单表子 DO
 *
 * @author 芋道源码
 */
@TableName("project_order_list")
@KeySequence("project_order_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListDO extends BaseDO {

    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private String id;
    /**
     * 产品编号(产品ID(与工艺内产品版本ID对应))
     */
    private String materialId;

    /**
     * 产品图号(由于订单都是销售订单,全是产品，所以上面那个materialId暂时用不到了)
     */
    private String partNumber;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 销售订单编号
     */
    //    private String orderId;
    /**
     * 带料加工(是/否)
     *
     * 枚举 {@link TODO pms_process_type 对应的类}
     */
    private Integer processType;
    /**
     * 原料交付时间
     */
    private LocalDateTime materialDeliveryTime;
    /**
     * 成品交付时间
     */
    private LocalDateTime fproDeliveryTime;
    /**
     * 项目订单id
     */
    private String projectOrderId;

}
