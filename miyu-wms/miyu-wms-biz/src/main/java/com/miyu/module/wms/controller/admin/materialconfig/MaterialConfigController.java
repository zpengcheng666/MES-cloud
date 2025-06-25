package com.miyu.module.wms.controller.admin.materialconfig;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.convert.materialconfig.MaterialConfigConvert;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.util.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

import com.miyu.module.wms.controller.admin.materialconfig.vo.*;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;

@Tag(name = "管理后台 - 物料类型")
@RestController
@RequestMapping("/wms/material-config")
@Validated
public class MaterialConfigController {

    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    private MaterialStockService materialStockService;

    @PostMapping("/create")
    @Operation(summary = "创建物料类型")
    @PreAuthorize("@ss.hasPermission('wms:material-config:create')")
    public CommonResult<String> createMaterialConfig(@Valid @RequestBody MaterialConfigSaveReqVO createReqVO) {
        return success(materialConfigService.createMaterialConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料类型")
    @PreAuthorize("@ss.hasPermission('wms:material-config:update')")
    public CommonResult<Boolean> updateMaterialConfig(@Valid @RequestBody MaterialConfigSaveReqVO updateReqVO) {
        materialConfigService.updateMaterialConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料类型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:material-config:delete')")
    public CommonResult<Boolean> deleteMaterialConfig(@RequestParam("id") String id) {
        materialConfigService.deleteMaterialConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:material-config:query')")
    public CommonResult<MaterialConfigRespVO> getMaterialConfig(@RequestParam("id") String id) {
        MaterialConfigDO materialConfig = materialConfigService.getMaterialConfig(id);
        return success(BeanUtils.toBean(materialConfig, MaterialConfigRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得物料类型列表")
    @PreAuthorize("@ss.hasPermission('wms:material-config:query')")
    public CommonResult<List<MaterialConfigRespVO>> getMaterialConfig() {
        List<MaterialConfigDO> list = materialConfigService.getMaterialConfigList();
        return success(BeanUtils.toBean(list, MaterialConfigRespVO.class));
    }
    @GetMapping("/page")
    @Operation(summary = "获得物料类型列表")
    @PreAuthorize("@ss.hasPermission('wms:material-config:query')")
    public CommonResult<PageResult<MaterialConfigRespVO>> getMaterialConfigPage(@Valid MaterialConfigPageReqVO listReqVO) {
        PageResult<MaterialConfigDO> pageResult = materialConfigService.getMaterialConfigPage(listReqVO);
        List<String> containerConfigListByIds = new ArrayList<>();
        pageResult.getList().forEach(materialConfigDO -> {
            if(StringUtils.isNotBlank(materialConfigDO.getContainerConfigIds())){
                List<String> ids = StringListUtils.stringToArrayList(materialConfigDO.getContainerConfigIds());
                containerConfigListByIds.addAll(ids);
            }
        });

        List<MaterialConfigDO> materialConfigListByIds = materialConfigService.getMaterialConfigListByIds(containerConfigListByIds.stream().distinct().collect(Collectors.toList()));

        Map<String, MaterialConfigDO> stringMaterialConfigDOMap = CollectionUtils.convertMap(materialConfigListByIds, MaterialConfigDO::getId);

        return success(new PageResult<>(MaterialConfigConvert.INSTANCE.convertList(pageResult.getList(), stringMaterialConfigDOMap),
                pageResult.getTotal()));
    /*    return success(new PageResult<>(WarehouseAreaConvert.INSTANCE.convertList(pageResult.getList(), warehouseMap),
                pageResult.getTotal()));*/
    }
    @GetMapping("/export-excel")
    @Operation(summary = "导出物料类型 Excel")
    @PreAuthorize("@ss.hasPermission('wms:material-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialConfigExcel(@Valid MaterialConfigPageReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<MaterialConfigDO> list = materialConfigService.getMaterialConfigPage(listReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料类型.xls", "数据", MaterialConfigRespVO.class,
                        BeanUtils.toBean(list, MaterialConfigRespVO.class));
    }



    @GetMapping("/getDefalutWarehouse")
    @Operation(summary = "根据库位获取库位上托盘的默认存放仓库")
    @Parameter(name = "locationId", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:material-config:query')")
    public CommonResult<MaterialConfigRespVO> getDefalutWarehouse(@RequestParam("locationId") String locationId) {

        // 查询库位上绑定的物料库存
        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockByLocationId(locationId);
        if(containerStockList.size() != 1){
            // 此物料所在库位绑定多个物料 不能呼叫
            throw exception(CARRYING_TASK_LOCATION_HAS_MULTIPLE_MATERIAL_CALL);
        }
        MaterialStockDO containerStock = containerStockList.get(0);

        MaterialConfigDO materialConfig = materialConfigService.getMaterialConfig(containerStock.getMaterialConfigId());

        return success(BeanUtils.toBean(materialConfig, MaterialConfigRespVO.class));
    }

}
