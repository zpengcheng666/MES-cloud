package com.miyu.module.wms.controller.admin.warehouselocation;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationPageReqVO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationRespVO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationSaveReqVO;
import com.miyu.module.wms.controller.admin.warehouselocation.vo.WarehouseLocationSimpleRespVO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.warehousearea.WarehouseAreaMapper;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 库位")
@RestController
@RequestMapping("/wms/warehouse-location")
@Validated
public class WarehouseLocationController {

    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private WarehouseAreaMapper warehouseAreaMapper;

    @Resource
    private WarehouseService warehouseService;

    @Resource
    private WarehouseAreaService warehouseAreaService;


    @PostMapping("/create")
    @Operation(summary = "创建库位")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:create')")
    public CommonResult<String> createWarehouseLocation(@Valid @RequestBody WarehouseLocationSaveReqVO createReqVO) {
        return success(warehouseLocationService.createWarehouseLocation(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库位")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:update')")
    public CommonResult<Boolean> updateWarehouseLocation(@Valid @RequestBody WarehouseLocationSaveReqVO updateReqVO) {
        warehouseLocationService.updateWarehouseLocation(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库位")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:delete')")
    public CommonResult<Boolean> deleteWarehouseLocation(@RequestParam("id") String id) {
        warehouseLocationService.deleteWarehouseLocation(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:query')")
    public CommonResult<WarehouseLocationRespVO> getWarehouseLocation(@RequestParam("id") String id) {
        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocation(id);
        return success(BeanUtils.toBean(warehouseLocation, WarehouseLocationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库位分页")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:query')")
    public CommonResult<PageResult<WarehouseLocationRespVO>> getWarehouseLocationPage(@Valid WarehouseLocationPageReqVO pageReqVO) {
        PageResult<WarehouseLocationDO> pageResult = warehouseLocationService.getWarehouseLocationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, WarehouseLocationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库位 Excel")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWarehouseLocationExcel(@Valid WarehouseLocationPageReqVO pageReqVO,
                                             HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WarehouseLocationDO> list = warehouseLocationService.getWarehouseLocationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库位.xls", "数据", WarehouseLocationRespVO.class,
                BeanUtils.toBean(list, WarehouseLocationRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得库位list")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:export')")
    public CommonResult<List<WarehouseLocationSimpleRespVO>> getWarehouseLocationList() {
        List<WarehouseLocationDO> list = warehouseLocationService.getWarehouseLocationList();
        return success(BeanUtils.toBean(list, WarehouseLocationSimpleRespVO.class));
    }

    @GetMapping("/getWarehouseLocation")
    @Parameter(name = "areaCode", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:query')")
    public CommonResult<List<WarehouseLocationRespVO>> getWarehouseLocationByareaCode(@RequestParam("areaCode") String areaCode) {
        WarehouseAreaDO warehouseAreaDO = warehouseAreaMapper.selectOne(new QueryWrapper<WarehouseAreaDO>().eq("area_code", areaCode));
        List<WarehouseLocationDO> list = warehouseLocationService.getWarehouseLocationByAreaCode(warehouseAreaDO);
        return success(BeanUtils.toBean(list, WarehouseLocationRespVO.class));

    }

    @GetMapping("/getWarehouseLocationDetail")
    @Operation(summary = "获得库位详情")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:query')")
    public CommonResult<WarehouseLocationRespVO> getWarehouseAreaDetail(@RequestParam(value = "locationId", required = false) String id) {
        if (StringUtils.isBlank(id)) {
            return success(null);
        }
        // 主键获取库位
        WarehouseLocationDO location = warehouseLocationService.getWarehouseLocation(id);
        // 库位获取库区信息
        WarehouseAreaDO area = warehouseAreaService.getWarehouseArea(location.getWarehouseAreaId());
        // 库区获取仓库信息
        WarehouseDO warehouse = warehouseService.getWarehouse(area.getWarehouseId());
        location.setWarehouseId(warehouse.getId());
        // 仓库编码
        location.setWarehouseCode(warehouse.getWarehouseCode());
        // 库区名称
        location.setAreaName(area.getAreaName());
        // 库区编码
        location.setAreaCode(area.getAreaCode());
        // 库区属性
        location.setAreaProperty(area.getAreaProperty());
        // 库区类型
        location.setAreaType(area.getAreaType());
        return success(BeanUtils.toBean(location, WarehouseLocationRespVO.class));
    }

    @GetMapping("/getLocationListByWarehouseId")
    @Operation(summary = "通过仓库id获得库位")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-location:query')")
    public CommonResult<List<WarehouseLocationDO>> getLocationListByWarehouseId(@RequestParam(value = "warehouseId", required = false) String warehouseId) {
        return success(warehouseLocationService.getLocationListByWarehouseId(warehouseId));
    }
}
