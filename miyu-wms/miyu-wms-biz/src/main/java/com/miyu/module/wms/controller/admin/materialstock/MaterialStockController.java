package com.miyu.module.wms.controller.admin.materialstock;

import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;

import com.miyu.module.wms.controller.admin.materialstorage.vo.MaterialStorageRespVO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;

import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.mzt.logapi.starter.annotation.LogRecord;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;


import com.miyu.module.wms.controller.admin.materialstock.vo.*;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.service.materialstock.MaterialStockService;

@Tag(name = "管理后台 - 物料库存")
@RestController
@RequestMapping("/wms/material-stock")
@Validated
public class MaterialStockController {

    @Resource
    private MaterialStockService materialStockService;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private MaterialStorageService materialStorageService;

    @Resource
    private WarehouseLocationService warehouseLocationService;

    @Resource
    private MaterialConfigService materialConfigService;

    /**
     * 物料的创建规则：
     * 1. 物料条码唯一
     * 2. 只有单件管理的物料 才能赋予容器属性（否则无法自动生成）
     * 3. 物料只能 有库位或者储位其一，不能同时有两个
     * 4. 单储位物料生成的储位编码为物料条码
     * 5. 非容器类物料 总库存仅为1
     * @param createReqVO
     * @return
     */
    @PostMapping("/create")
    @Operation(summary = "创建物料库存")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:create')")
    public CommonResult<String> createMaterialStock(@Valid @RequestBody MaterialStockSaveReqVO createReqVO) {
        return success(materialStockService.createMaterialStock(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料库存")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:update')")
    public CommonResult<Boolean> updateMaterialStock(@Valid @RequestBody MaterialStockSaveReqVO updateReqVO) {
        materialStockService.updateMaterialStock(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料库存")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:material-stock:delete')")
    public CommonResult<Boolean> deleteMaterialStock(@RequestParam("id") String id) {
        materialStockService.deleteMaterialStock(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料库存")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:query')")
    public CommonResult<MaterialStockRespVO> getMaterialStock(@RequestParam("id") String id) {
        MaterialStockDO materialStock = materialStockService.getMaterialStock(id);
        return success(BeanUtils.toBean(materialStock, MaterialStockRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料库存分页")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:query')")
    public CommonResult<PageResult<MaterialStockRespVO>> getMaterialStockPage(@Valid MaterialStockPageReqVO pageReqVO) {
        PageResult<MaterialStockDO> pageResult = materialStockService.getMaterialStockPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialStockRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得物料库存列表", description = "主要用于前端的下拉选项")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockList() {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockList();
        return success(BeanUtils.toBean(materialStockList, MaterialStockRespVO.class));
    }

    @GetMapping("/container-list")
    @Operation(summary = "获得物料库存列表", description = "主要用于前端的下拉选项")
    public CommonResult<List<MaterialStockRespVO>> getMaterialStockContainerList() {
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockContainerList(DictConstants.INFRA_BOOLEAN_TINYINT_YES);
        return success(BeanUtils.toBean(materialStockList, MaterialStockRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料库存 Excel")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialStockExcel(@Valid MaterialStockPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialStockDO> list = materialStockService.getMaterialStockPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料库存.xls", "数据", MaterialStockRespVO.class,
                        BeanUtils.toBean(list, MaterialStockRespVO.class));
    }

    @GetMapping( "/getMaterialsByBarCode")
    @Operation(summary = "根据物料编码获取物料库存")
    public CommonResult<MaterialStockRespVO> getMaterialsByBarCode(@RequestParam("barCode") String barCode){
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByBarCodes(Collections.singletonList(barCode));
        if(materialStockList != null && !materialStockList.isEmpty()){
            return success(BeanUtils.toBean(materialStockList.get(0), MaterialStockRespVO.class));
        }
        return success(null);
    }


    /***************************************************其他服务调用接口*************************************************************/

    /**
     * 出库订单创建时，获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping("/outOrder/getMaterialsByConfigIds")
    @Operation(summary = "根据物料类型ids获取可出库的物料库存列表")
    CommonResult<List<MaterialStockRespDTO>> getOutOrderMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds){
        return materialStockApi.getOutOrderMaterialsByConfigIds(materialConfigIds);
    }


    /**
     * 库存移交订单创建时，获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping("/moveOrder/getMaterialsByConfigIds")
    @Operation(summary = "根据物料类型ids获取可移交的物料库存列表")
    CommonResult<List<MaterialStockRespDTO>> getMoveOrderMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds){
        return materialStockApi.getMoveOrderMaterialsByConfigIds(materialConfigIds);
    }

    /**
     * 入库订单创建时，获取物料库存列表
     * @param materialConfigIds
     * @return
     */
    @GetMapping("/inOrder/getMaterialsByConfigIds")
    @Operation(summary = "根据物料类型ids获取可入库的物料库存列表")
    CommonResult<List<MaterialStockRespDTO>> getInOrderMaterialsByConfigIds(@RequestParam("materialConfigIds") Collection<String> materialConfigIds){
        return materialStockApi.getInOrderMaterialsByConfigIds(materialConfigIds);
    }


    @GetMapping("/StockMenuTree")
    @Operation(summary = "库存管理 - 左边树形根展示")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:query')")
    public CommonResult<List<MaterialStockMenuTreeRespVO>> StockMenuTree() {

        List<MaterialStockMenuTreeRespVO> listTree = new ArrayList<>();
        /**
         * 封装容器库存
         */
        MaterialStockMenuTreeRespVO stockMenuTree = new MaterialStockMenuTreeRespVO();
        stockMenuTree.setName("容器库存");
        // 物料类型是容器
        List<MaterialStockDO> containerStockList = materialStockService.getMaterialStockContainerList(DictConstants.INFRA_BOOLEAN_TINYINT_YES);
        // 获得库位列表
        List<MaterialStorageDO> materialStorageList = materialStorageService.getMaterialStorageList();
        // 组装库位map 库区id 作为key
        Map<String, List<MaterialStorageDO>> materialStorageMap = materialStorageList.stream().map(a->{
            StringBuffer s = new StringBuffer();
//            if(a.getChannel()!=null)s.append("通道"+a.getChannel() + "-");
//            if(a.getGroupp()!=null)s.append("组"+a.getGroupp() + "-");
            int layer = a.getLayer() == null ? 1 : a.getLayer();
            int row = a.getRow() == null ? 1 : a.getRow();
            int col = a.getCol() == null ? 1 : a.getCol();
            s.append("层").append(layer).append("-");
            s.append("排").append(row).append("-");
            s.append("列").append(col).append("-");
            a.setName(s.substring(0,s.length()-1));
            return a;
        }).collect(Collectors.groupingBy(MaterialStorageDO::getMaterialStockId));

        // 组装库区map 仓库id 作为key
        containerStockList = containerStockList.stream().peek(a->{
//            a.setName(a.getMaterialNumber()+ "-" + a.getBarCode());
            a.setName(a.getBarCode());
            a.setChildrens(materialStorageMap.get(a.getId()));
        }).collect(Collectors.toList());

        // 组装树形结构
        stockMenuTree.setChildrens(containerStockList);
        listTree.add(stockMenuTree);

        /**
         * 封装非容器库存tree
         */
        stockMenuTree = new MaterialStockMenuTreeRespVO();
        stockMenuTree.setName("非容器库存");
        // 非容器库存
        List<MaterialStockDO> stockList = materialStockService.getMaterialStockContainerList(DictConstants.INFRA_BOOLEAN_TINYINT_NO);
        // 组装库区map 仓库id 作为key
        stockList = stockList.stream().map(a->{
            a.setType(1);
            a.setName(a.getMaterialNumber()+ "-" + a.getBarCode());
            a.setChildrens(new ArrayList<>());
            return a;
        }).collect(Collectors.toList());
        stockMenuTree.setChildrens(stockList);
        listTree.add(stockMenuTree);

        return success(listTree);
    }



    @GetMapping("/getStorageListByStockId")
    @Operation(summary = "库位主键获得储位集合")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:query')")
    public CommonResult<MaterialStockRespVO> getStorageListByStockId(@RequestParam(value = "stockId", required = false) String id) {
        if(StringUtils.isBlank(id)){
            return success(null);
        }
        // 获取库存
        MaterialStockDO stock = materialStockService.getMaterialStock(id);
        // 库位
        WarehouseLocationDO location = warehouseLocationService.getWarehouseLocation(stock.getLocationId());
        // 储位
        MaterialStorageDO storage = materialStorageService.getMaterialStorage(stock.getStorageId());
        // 物料类型
        MaterialConfigDO config = materialConfigService.getMaterialConfig(stock.getMaterialConfigId());
        // 储位集合
        List<MaterialStorageDO> storageList = materialStorageService.getAllMaterialStorageListByContainerStockId(id);
        // 创建一个三维数组
        int layer = config.getMaterialLayer() == null ? 1 : config.getMaterialLayer();
        int row = config.getMaterialRow() == null ? 1 : config.getMaterialRow();
        int col = config.getMaterialCol() == null ? 1 : config.getMaterialCol();
        MaterialStorageDO[][][] storages = new MaterialStorageDO[layer][row][col];
        // 填充储位信息
        for(MaterialStorageDO s : storageList){
            int layerIndex = s.getLayer() == null ? 1 : s.getLayer();
            int rowIndex = s.getRow() == null ? 1 : s.getRow();
            int colIndex = s.getCol() == null ? 1 : s.getCol();
            storages[layerIndex-1][rowIndex-1][colIndex-1] = s;
        }
        // 封装返回值
       return success(BeanUtils.toBean(stock, MaterialStockRespVO.class, vo -> {
            if(location != null)vo.setLocationName(location.getLocationCode());
            if(storage != null)vo.setStorageName(storage.getStorageName());
            vo.setMaterialNumber(config.getMaterialNumber());
            vo.setChildrens(storages);
        }));
        /*return success(BeanUtils.toBean(stock, MaterialStockRespVO.class, vo -> {
            if(location != null){
                vo.setLocationName(location.getLocationCode());
            }
            if(storage != null){
                vo.setStorageName(storage.getStorageName());
            }

            vo.setMaterialNumber(config.getMaterialNumber());
            vo.setLayer(config.getMaterialLayer());
            vo.setRow(config.getMaterialRow());
            vo.setCol(config.getMaterialCol());
            vo.setChildrens(storageList);
        }));*/
    }

    @GetMapping("/getStorageStockDetail")
    @Operation(summary = "获得库位详情")
    @PreAuthorize("@ss.hasPermission('wms:material-stock:query')")
    public CommonResult<MaterialStorageRespVO> getStorageStockDetail(@RequestParam(value = "storageId", required = false) String id) {
        if(StringUtils.isBlank(id)){
            return success(null);
        }
        // 储位
        MaterialStorageDO storage = materialStorageService.getMaterialStorage(id);
        // 库存
        MaterialStockDO stock = materialStockService.getMaterialStock(storage.getMaterialStockId());
        return success(BeanUtils.toBean(storage, MaterialStorageRespVO.class, vo -> {
            vo.setBarCode(stock.getBarCode());
        }));
    }


    /**
     * 根据物料编号获取物料库存
     */
    @GetMapping("/getMaterialStockByMaterialNumber")
    @Operation(summary = "根据物料编号获取物料库存")
    public CommonResult<PageResult<MaterialStockDO>> getMaterialStockByMaterialNumber(@Valid MaterialStockPageReqVO pageReqVO) {
        return CommonResult.success(materialStockService.getMaterialStockAtStorageAreaByMaterialNumber(pageReqVO));
    }
}
