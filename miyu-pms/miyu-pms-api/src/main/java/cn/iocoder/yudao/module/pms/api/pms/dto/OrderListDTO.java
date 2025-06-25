package cn.iocoder.yudao.module.pms.api.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Schema(description = "订单表子表")
public class OrderListDTO{

    /**
     * id
     */
    private String id;
    /**
     * 项目编号
     */
    private String projectId;

    /**
     * 项目编码
     */
    private String projectCode;
    /**
     * 物料编码
     */
    private String materialNumber;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 带料加工(是/否)
     */
    private Integer processType;
    /**
     * 订单id
     */
    private Integer orderStatus;
    /**
     * 原料交付时间
     */
    private LocalDateTime materialDeliveryTime;
    /**
     * 成品交付时间
     */
    private LocalDateTime fproDeliveryTime;
    /**
     * 项目名
     */
    private String projectName;

}
