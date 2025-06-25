package cn.iocoder.yudao.module.pms.api.orderMaterial.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Schema(description = "外协物料选择表")
@Data
public class OutsourcingMaterialSelectDTO {

    //订单id
    private String orderId;
    //子计划id
    private String planItemId;

    //合同id
    private String contractId;
    //外协厂家
    private String aidMill;
    //条码集合(编码集合,逗号分割)
    @NotNull(message = "条码集合不能为空")
    private String MaterialCodeList;
}
