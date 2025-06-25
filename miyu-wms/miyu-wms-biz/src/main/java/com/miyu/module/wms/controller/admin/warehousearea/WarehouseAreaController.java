package com.miyu.module.wms.controller.admin.warehousearea;

import com.miyu.module.wms.convert.warehousearea.WarehouseAreaConvert;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static com.miyu.module.wms.enums.ErrorCodeConstants.WAREHOUSE_AREA_ALL_NULL_CGLS;

import com.miyu.module.wms.controller.admin.warehousearea.vo.*;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;

@Tag(name = "管理后台 - 库区")
@RestController
@RequestMapping("/wms/warehouse-area")
@Validated
public class WarehouseAreaController {

    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private WarehouseLocationService warehouseLocationService;

    @PostMapping("/create")
    @Operation(summary = "创建库区")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:create')")
    public CommonResult<String> createWarehouseArea(@Valid @RequestBody WarehouseAreaSaveReqVO createReqVO) {
        if(createReqVO.getAreaChannels() == null
                && createReqVO.getAreaGroup() == null
                && createReqVO.getAreaLayer() == null
                && createReqVO.getAreaSite() == null){
            throw exception(WAREHOUSE_AREA_ALL_NULL_CGLS);
        }
        return success(warehouseAreaService.createWarehouseArea(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库区")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:update')")
    public CommonResult<Boolean> updateWarehouseArea(@Valid @RequestBody WarehouseAreaSaveReqVO updateReqVO) {
        warehouseAreaService.updateWarehouseArea(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库区")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:delete')")
    public CommonResult<Boolean> deleteWarehouseArea(@RequestParam("id") String id) {
        warehouseAreaService.deleteWarehouseArea(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库区")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:query')")
    public CommonResult<WarehouseAreaRespVO> getWarehouseArea(@RequestParam("id") String id) {
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseArea(id);
        return success(BeanUtils.toBean(warehouseArea, WarehouseAreaRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库区分页")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:query')")
    public CommonResult<PageResult<WarehouseAreaRespVO>> getWarehouseAreaPage(@Valid WarehouseAreaPageReqVO pageReqVO) {
        PageResult<WarehouseAreaDO> pageResult = warehouseAreaService.getWarehouseAreaPage(pageReqVO);
        // 拼接数据
        List<String> warehouseIds = convertList(pageResult.getList(), WarehouseAreaDO::getWarehouseId);
        Map<String, WarehouseDO> warehouseMap = warehouseService.getWarehouseMap(warehouseIds);
        return success(new PageResult<>(WarehouseAreaConvert.INSTANCE.convertList(pageResult.getList(), warehouseMap),
                pageResult.getTotal()));
//        return success(BeanUtils.toBean(pageResult, WarehouseAreaRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得库区信息列表", description = "主要用于前端的下拉选项")
    public CommonResult<List<WarehouseAreaRespVO>> getWarehouseAreaList() {
        List<WarehouseAreaDO> warehouseAreaList = warehouseAreaService.getWarehouseAreaList();
        return success(BeanUtils.toBean(warehouseAreaList, WarehouseAreaRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库区 Excel")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWarehouseAreaExcel(@Valid WarehouseAreaPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WarehouseAreaDO> list = warehouseAreaService.getWarehouseAreaPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库区.xls", "数据", WarehouseAreaRespVO.class,
                        BeanUtils.toBean(list, WarehouseAreaRespVO.class));
    }

    @GetMapping("/getwarehouseareadetail")
    @Operation(summary = "获得库区详情")
    @PreAuthorize("@ss.hasPermission('wms:warehouse-area:query')")
    public CommonResult<WarehouseAreaDO> getWarehouseAreaDetail(@RequestParam(value = "areaId", required = false) String id) {
        if(StringUtils.isBlank(id)){
            return success(null);
        }
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseArea(id);
        WarehouseDO warehouse = warehouseService.getWarehouse(warehouseArea.getWarehouseId());
        warehouseArea.setWarehouseCode(warehouse.getWarehouseCode());
        List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationByAreaId(id);
        // 根据 通道 组 层 位  创建一个四维数组
        int channelNum = warehouseArea.getAreaChannels()==null?1:warehouseArea.getAreaChannels();
        int groupNum = warehouseArea.getAreaGroup()==null?1:warehouseArea.getAreaGroup();
        int layerNum = warehouseArea.getAreaLayer()==null?1:warehouseArea.getAreaLayer();
        int siteNum = warehouseArea.getAreaSite()==null?1:warehouseArea.getAreaSite();
        WarehouseLocationDO[][][][] location = new WarehouseLocationDO[channelNum][groupNum][layerNum][siteNum];
        for (WarehouseLocationDO warehouseLocation : warehouseLocationList) {
            int channel = warehouseLocation.getChannel()==null?1:warehouseLocation.getChannel();
            int group = warehouseLocation.getGroupp()==null?1:warehouseLocation.getGroupp();
            int layer = warehouseLocation.getLayer()==null?1:warehouseLocation.getLayer();
            int site = warehouseLocation.getSite()==null?1:warehouseLocation.getSite();
            location[channel-1][group-1][layer-1][site-1] = warehouseLocation;
        }
        warehouseArea.setChildrens(location);
        warehouseArea.setLocationCount(warehouseLocationList.size());

        return success(warehouseArea);
    }

}
