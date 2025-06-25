package com.miyu.module.wms.controller.admin.home;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarryTaskPageReqVO;
import com.miyu.module.wms.controller.admin.home.vo.InOutWarehouseRespVO;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.home.HomeService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Tag(name = "管理后台 -WMS 首页")
@RestController
@RequestMapping("/wms/home")
@Validated
public class HomeController {

    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private HomeService homeService;

    /**
     * 仓库占用率-- 库位占用
     * 库位上存在非托盘的物料  托盘占用
     */
    @GetMapping("/warehouseOccupancyRate")
    @Operation(summary = "仓库占用率-- 库位占用")
    @PreAuthorize("@ss.hasPermission('wms:occupancy:rate')")
    public CommonResult<Map<String, Map<String, Integer>>> warehouseOccupancyRate() {
        List<WarehouseLocationDO> warehouseLocationDOS = warehouseLocationService.getWarehouseLocationByWarehouseType(DictConstants.WMS_WAREHOUSE_TYPE_1);
        return CommonResult.success(homeService.warehouseOccupancyRate(warehouseLocationDOS));
    }

    /**
     * 出入库统计--根据时间段查询
     * @return
     */
    @GetMapping("/warehouseInOutAnalysis")
    @Operation(summary = "出入库统计")
    @PreAuthorize("@ss.hasPermission('wms:warehouseInOut:analysis')")
    public CommonResult<JSONObject> warehouseInOutAnalysis(
            @RequestParam("createTimeRange[0]") String createTimeRange0,
            @RequestParam("createTimeRange[1]") String createTimeRange1) {
        LocalDateTime[] createTimeRange = new LocalDateTime[]{
                LocalDateTime.parse(createTimeRange0, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)),
                LocalDateTime.parse(createTimeRange1, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND))};
        JSONObject jsonObject = homeService.warehouseInOutAnalysis(createTimeRange);
        return CommonResult.success(jsonObject);
    }

    /**
     * 人工待入库列表
     * @return
     */
    @GetMapping("/getManualInList")
    @Operation(summary = "人工待出入库列表")
    @PreAuthorize("@ss.hasPermission('wms:manualIn:list')")
    public CommonResult<List<InOutWarehouseRespVO>> getManualInList() {
        return CommonResult.success(homeService.getManualInList());
    }

    /**
     * 人工待出库列表
     * @return
     */
    @GetMapping("/getManualOutList")
    @Operation(summary = "人工待出入库列表")
    @PreAuthorize("@ss.hasPermission('wms:manualOut:list')")
    public CommonResult<List<InOutWarehouseRespVO>> getManualOutList() {
        return CommonResult.success(homeService.getManualOutList());
    }




}
