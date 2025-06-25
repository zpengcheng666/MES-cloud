package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 项目计划子表更新")
@Data
public class PmsPlanItemReqVO {

    @Schema(description = "id")
    private String id;

    @Schema(description = "项目id")
    @NotNull(message = "项目id不能为空")
    private String projectId;

    @Schema(description = "项目计划id")
    @NotNull(message = "项目计划id不能为空")
    private String projectPlanId;

    @Schema(description = "订单id")
    @NotNull(message = "订单id不能为空")
    private String projectOrderId;

    @Schema(description = "项目编码")
    private String projectCode;

    @Schema(description = "物料牌号(物料类型)")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工艺方案")
    private String processScheme;

    @Schema(description = "可用数量")
    private BigDecimal available;

    @Schema(description = "计划数量")
    private BigDecimal quantity;

    @Schema(description = "计划类型(加工1,外协2,工序外协3)")
    private Integer planType;

    private String projectName;
//    @NotNull(message = "物料码不能为空")
    private List<String> materialCodeList;

    private String materialCodeListStr;

    @Schema(description = "负责人")
    private Long responsiblePerson;

    @Schema(description = "是否外协")
    private String isOut;

    @Schema(description = "工序")
    private String processStep;

    @Schema(description = "订单编码")
    private String orderNumber;

    @Schema(description = "采购数量,非表中字段")
    private Integer purchaseAmount;

    @Schema(description = "首件标识")
    private Integer firstMark;



}
