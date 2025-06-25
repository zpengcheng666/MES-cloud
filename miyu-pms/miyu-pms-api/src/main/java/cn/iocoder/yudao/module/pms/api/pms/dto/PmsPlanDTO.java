package cn.iocoder.yudao.module.pms.api.pms.dto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

/**
 * 项目计划 DO
 *
 * @author 芋道源码
 */
@Data
@Schema(description = "项目计划")
public class PmsPlanDTO{

    /**
     * 项目计划id
     */
    private String id;
    /**
     * 项目订单编号(项目订单ID)
     */
    private String projectOrderId;
    /**
     * 备注
     */
    private String remark;

    private List<PlanItemDTO> itemDTOList;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 项目编号
     */
    private String projectCode;

}
