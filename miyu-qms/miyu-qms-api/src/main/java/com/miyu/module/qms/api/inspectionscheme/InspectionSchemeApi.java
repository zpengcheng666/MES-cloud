package com.miyu.module.qms.api.inspectionscheme;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.api.dto.InspectionSchemeReqDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeRespDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeSaveReqDTO;
import com.miyu.module.qms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 检验方案")
public interface InspectionSchemeApi {

    String PREFIX = ApiConstants.PREFIX + "/inspection-scheme";

    @PostMapping(PREFIX + "/list")
    @Operation(summary = "根据物料和检验类型查询方案")
    CommonResult<List<InspectionSchemeRespDTO>> getInspectionScheme(@RequestBody InspectionSchemeReqDTO reqDTO);


    @PostMapping(PREFIX + "/api/createInspectionScheme")
    @Operation(summary = "创建检验方案")
    CommonResult<String> createInspectionScheme(@Valid @RequestBody InspectionSchemeSaveReqDTO createReqVO);


    /**
     * @param technologyId
     * @return
     */
    @GetMapping(PREFIX + "/api/getInspectionSchemeByProcessId")
    @Operation(summary = "工艺ID查询检验方案")
    CommonResult<InspectionSchemeRespDTO> getInspectionSchemeByProcessId(@RequestParam("technologyId") String technologyId, @RequestParam("processId") String processId);

    /**
     * @param technologyId
     * @param isEffective 1 发布
     * @return
     */
    @PostMapping(PREFIX + "/api/submitEffective")
    @Operation(summary = "更改生效状态")
    CommonResult<Boolean> submitEffective(@RequestParam("technologyId") String technologyId, @RequestParam("isEffective") Integer isEffective);


    /**
     * @param id
     * @return
     */
    @PostMapping(PREFIX + "/api/deleteInspectionSchemeItemById")
    @Operation(summary = "删除检测项")
    CommonResult<Boolean> deleteInspectionSchemeItemById(@RequestParam("id") String id);

    /**
     * @param technologyId
     * @return
     */
    @PostMapping(PREFIX + "/api/deleteInspectionSchemeByTechnologyId")
    @Operation(summary = "删除检测方案")
    CommonResult<Boolean> deleteInspectionSchemeByTechnologyId(@RequestParam("technologyId") String technologyId);
}
