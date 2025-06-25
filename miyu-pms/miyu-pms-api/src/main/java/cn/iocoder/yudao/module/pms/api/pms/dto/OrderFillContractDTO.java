package cn.iocoder.yudao.module.pms.api.pms.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 项目计划子表，产品计划完善 DO
 *
 * @author 芋道源码
 */

@Data
@Schema(description = "项目计划子表")
public class OrderFillContractDTO {

    /**
     * 项目id
     */
    private String projectId;

    /**
     * 合同id
     */
    private String contractId;


}
