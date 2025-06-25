package cn.iocoder.yudao.module.pms.controller.admin.order.vo;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 项目订单表子 DO
 *
 * @author 芋道源码
 */
@Data
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderListRespVO{
    /**
     * 产品编号(产品ID(与工艺内产品版本ID对应))
     */
    private String materialId;
    /**
     * 产品图号
     */
    private String partNumber;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 销售订单编号
     */
    private String orderId;
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
