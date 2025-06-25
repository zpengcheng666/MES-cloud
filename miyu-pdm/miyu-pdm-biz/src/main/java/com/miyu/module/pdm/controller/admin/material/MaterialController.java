package com.miyu.module.pdm.controller.admin.material;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.controller.admin.material.vo.MaterialListReqVO;
import com.miyu.module.pdm.controller.admin.material.vo.MaterialRespVO;
import com.miyu.module.pdm.dal.dataobject.material.MaterialDO;
import com.miyu.module.pdm.service.material.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - PDM 工装-临时")
@RestController
@RequestMapping("/pdm/material")
@Validated
public class MaterialController {

    @Resource
    private MaterialService materialService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @GetMapping("/getMaterialList1")
    @Operation(summary = "获得工装列表")
    public CommonResult<List<MaterialRespVO>> getMaterialList1(@Valid MaterialListReqVO listReqVO) {
        List<MaterialDO> list = materialService.getMaterialList(listReqVO);
        return success(BeanUtils.toBean(list, MaterialRespVO.class));
    }

    @PostMapping("/getMaterialList")
    @Operation(summary = "获得工装列表")
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialList(@Valid MaterialConfigReqDTO reqDTO) {
        CommonResult<List<MaterialConfigRespDTO>> list = materialMCCApi.getMaterialConfigListByTypeCode(reqDTO);
        return list;
    }

    @GetMapping("/getMaterialListByMaterialIds1")
    @Operation(summary = "根据工装id数组获得工装列表")
    public CommonResult<List<MaterialRespVO>> getDeviceListByDeviceIds1(@RequestParam("ids") List<String> materialIds) {
        List<MaterialDO> list = materialService.getMaterialListByMaterialIds(materialIds);
        return success(BeanUtils.toBean(list, MaterialRespVO.class));
    }

    @GetMapping("/getMaterialListByMaterialIds")
    @Operation(summary = "根据工装id数组获得工装列表")
    public CommonResult<List<MaterialConfigRespDTO>> getDeviceListByDeviceIds(@RequestParam("ids") List<String> materialIds) {
        CommonResult<List<MaterialConfigRespDTO>> list = materialMCCApi.getMaterialConfigList(materialIds);
        return list;
    }

    @PostMapping("/getMaterialList2")
    @Operation(summary = "获得工装列表")
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialList2(@Valid MaterialConfigReqDTO reqDTO) {
        CommonResult<List<MaterialConfigRespDTO>> list = materialMCCApi.getMaterialConfigListByTypeCode(reqDTO);
        return list;
    }
    @GetMapping("/getMaterialListByMaterialIds2")
    @Operation(summary = "根据工装id数组获得工装列表")
    public CommonResult<List<MaterialConfigRespDTO>> getDeviceListByDeviceIds2(@RequestParam("ids") List<String> materialIds) {
        CommonResult<List<MaterialConfigRespDTO>> list = materialMCCApi.getMaterialConfigList(materialIds);
        return list;
    }
}
