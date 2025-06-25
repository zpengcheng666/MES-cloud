package cn.iocoder.yudao.module.pms.controller.admin.plan.vo;

import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import java.util.List;
import java.util.Map;

@Schema(description = "管理后台 - 项目计划 Request VO")
@Data
@ToString(callSuper = true)
public class PmsPlanHandleVO {
    private List<PmsPlanHandleRespVO> planHandleList;

    //物料编号+库存
    private Map<String,List<MaterialStockRespDTO>> stockMap;
    private List<MaterialStockRespDTO> stockList;
}
