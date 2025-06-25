package cn.iocoder.yudao.module.pms.controller.admin.excute.vo;

import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "项目执行,生产进度")
@Data
public class ProductProgressRespVO {

    /**
     * 项目编码，对应我这边projectCode
     */
    private String projectNumber;
    /**
     * 图号
     */
    private String partNumber;
    /**
     * 订单编码
     */
    private String orderNumber;

    /**
     * 项目名称
     */
    private String projectName;
    /**
     * 生产计划数量
     */
    private Integer count;

    /**
     * 计划成品数量
     */
    private Integer quantity;

    /**
     * 成品完成数量
     */
    private Integer complete;
    /**
     * 计划完工日期
     */
    private LocalDateTime deliveryTime;

    private List<McsBatchRecordDTO> partList;

    /**
     * 进度
     */
    private BigDecimal progress;

}
