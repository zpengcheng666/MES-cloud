package com.miyu.module.es.controller.admin.brakeN;


import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNDTO;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNData;
import com.miyu.cloud.es.api.brakeN.dto.BrakeNRest;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNDataVO;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNPageReqVO;
import com.miyu.module.es.controller.admin.brakeN.vo.BrakeNVO;
import com.miyu.module.es.service.brakeN.BrakeNService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

@Tag(name= "管理后台 - 新厂车牌数据")
@RestController
@RequestMapping("/es/brakeN")
@Validated
public class BrakeNController {

    @Resource
    private BrakeNService brakeNService;

    @GetMapping("/page")
    @Operation(summary = "获得新厂车牌数据分页")
    @PreAuthorize("@ss.hasPermission('es:brakeN:query')")
    public CommonResult<BrakeNPageReqVO> getBrakeNPage(@Valid BrakeNVO brakeNVO){
        BrakeNData brakeNData = brakeNService.getBrakeNPage(brakeNVO);
        BrakeNPageReqVO brakeNPageReqVO = new BrakeNPageReqVO();
        brakeNPageReqVO.setTotal(brakeNData.getTotalCount()).setPageNum(brakeNData.getPageIndex()).setPageSize(brakeNData.getPageSize());
        brakeNPageReqVO.setData(brakeNData.getResults());
        return CommonResult.success(brakeNPageReqVO);
    }

    @GetMapping("/get")
    @Operation(summary = "获得新厂车牌数据")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('es:brakeN:query')")
    public CommonResult<BrakeNDTO> getBrakeN(@RequestParam("id") String id){
        BrakeNData brakeNData = brakeNService.getBrakeN(id);
        return CommonResult.success(brakeNData.getResults().get(0));
    }


}
