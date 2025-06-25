package com.miyu.module.wms.controller.admin.movewarehousedetail;

import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.miyu.module.wms.convert.movewarehousedetail.MoveWarehouseDetailConvert;
import com.miyu.module.wms.core.carrytask.service.impl.MoveMaterialServiceImpl;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
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
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

import com.miyu.module.wms.controller.admin.movewarehousedetail.vo.*;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;

@Tag(name = "管理后台 - 库存移动详情")
@RestController
@RequestMapping("/wms/move-warehouse-detail")
@Validated
public class MoveWarehouseDetailController {

    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private MoveMaterialServiceImpl moveMaterialService;
    @Resource
    private WarehouseService warehouseService;

    @PostMapping("/create")
    @Operation(summary = "创建库存移动详情")
    @PreAuthorize("@ss.hasPermission('wms:move-warehousedetail:create')")
    public CommonResult<String> createMoveWarehouseDetail(@Valid @RequestBody MoveWarehouseDetailSaveReqVO createReqVO) {
        if(StringUtils.isNotBlank(createReqVO.getChooseStockId())){
            MaterialStockDO materialStock = materialStockService.getMaterialStock(createReqVO.getChooseStockId());
            createReqVO.setBatchNumber(materialStock.getBatchNumber());
            createReqVO.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(materialStock.getTotality() == createReqVO.getQuantity()){
                createReqVO.setMaterialStockId(materialStock.getId());
                if(StringUtils.isBlank(createReqVO.getStartWarehouseId())){
                    WarehouseDO warehouse = warehouseService.getWarehouseByMaterialStockId(materialStock.getId());
                    createReqVO.setStartWarehouseId(warehouse.getId());
                }
            }
            if(materialStock.getTotality() < createReqVO.getQuantity()){
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }
        };
        return success(moveWarehouseDetailService.createMoveWarehouseDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存移动详情")
    @PreAuthorize("@ss.hasPermission('wms:move-warehousedetail:update')")
    public CommonResult<Boolean> updateMoveWarehouseDetail(@Valid @RequestBody MoveWarehouseDetailSaveReqVO updateReqVO) {
        if(StringUtils.isNotBlank(updateReqVO.getChooseStockId())){
            MaterialStockDO materialStock = materialStockService.getMaterialStock(updateReqVO.getChooseStockId());
            updateReqVO.setBatchNumber(materialStock.getBatchNumber());
            updateReqVO.setMaterialConfigId(materialStock.getMaterialConfigId());
            if(materialStock.getTotality() == updateReqVO.getQuantity()){
                updateReqVO.setMaterialStockId(materialStock.getId());
                if(StringUtils.isBlank(updateReqVO.getStartWarehouseId())){
                    WarehouseDO warehouse = warehouseService.getWarehouseByMaterialStockId(materialStock.getId());
                    updateReqVO.setStartWarehouseId(warehouse.getId());
                }
            }
            if(materialStock.getTotality() < updateReqVO.getQuantity()){
                throw exception(MATERIAL_STOCK_NOT_ENOUGH_STOCK);
            }
        };
        moveWarehouseDetailService.updateMoveWarehouseDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存移动详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:move-warehousedetail:delete')")
    public CommonResult<Boolean> deleteMoveWarehouseDetail(@RequestParam("id") String id) {
        moveWarehouseDetailService.deleteMoveWarehouseDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存移动详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:move-warehousedetail:query')")
    public CommonResult<MoveWarehouseDetailRespVO> getMoveWarehouseDetail(@RequestParam("id") String id) {
        MoveWarehouseDetailDO MoveWarehouseDetail = moveWarehouseDetailService.getMoveWarehouseDetail(id);
        return success(BeanUtils.toBean(MoveWarehouseDetail, MoveWarehouseDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存移动详情分页")
    @PreAuthorize("@ss.hasPermission('wms:move-warehousedetail:query')")
    public CommonResult<PageResult<MoveWarehouseDetailRespVO>> getMoveWarehouseDetailPage(@Valid MoveWarehouseDetailPageReqVO pageReqVO) {
        PageResult<MoveWarehouseDetailDO> pageResult = moveWarehouseDetailService.getMoveWarehouseDetailPage(pageReqVO);
        //创建者
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), MoveWarehouseDetailDO::getCreator));
        Map<Long, AdminUserRespDTO> userMap = null;
        if(CollectionUtils.isNotEmpty(creatorIds)){
            creatorIds = creatorIds.stream().distinct().collect(Collectors.toList());
            // 拼接数据
            userMap = userApi.getUserMap(creatorIds);
        }

        return success(new PageResult<>(MoveWarehouseDetailConvert.INSTANCE.convertList(pageResult.getList(), userMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存移动详情 Excel")
    @PreAuthorize("@ss.hasPermission('wms:move-warehousedetail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMoveWarehouseDetailExcel(@Valid MoveWarehouseDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MoveWarehouseDetailDO> list = moveWarehouseDetailService.getMoveWarehouseDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存移动详情.xls", "数据", MoveWarehouseDetailRespVO.class,
                        BeanUtils.toBean(list, MoveWarehouseDetailRespVO.class));
    }



    @PostMapping("/move-warehouse-action")
    @Operation(summary = "移库操作")
    public CommonResult<Boolean> inWarehouseAction(String locationId, String warehouseId) {
        moveWarehouseDetailService.moveWarehouseAction(locationId, warehouseId,null);
        return success(true);
    }



}