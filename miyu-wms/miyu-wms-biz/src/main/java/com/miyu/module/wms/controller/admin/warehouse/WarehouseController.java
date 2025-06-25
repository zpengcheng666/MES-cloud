package com.miyu.module.wms.controller.admin.warehouse;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockMenuTreeRespVO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.warehouselocation.WarehouseLocationMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import com.miyu.module.wms.convert.warehouse.WarehouseConvert;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

import com.miyu.module.wms.controller.admin.warehouse.vo.*;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.service.warehouse.WarehouseService;

@Tag(name = "管理后台 - 仓库表")
@RestController
@RequestMapping("/wms/warehouse")
@Validated
public class WarehouseController {

    @Resource
    private WarehouseService warehouseService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialStorageService materialStorageService;

    @PostMapping("/create")
    @Operation(summary = "创建仓库表")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:create')")
    public CommonResult<String> createWarehouse(@Valid @RequestBody WarehouseSaveReqVO createReqVO) {
        return success(warehouseService.createWarehouse(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新仓库表")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:update')")
    public CommonResult<Boolean> updateWarehouse(@Valid @RequestBody WarehouseSaveReqVO updateReqVO) {
        warehouseService.updateWarehouse(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除仓库表")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:warehouse:delete')")
    public CommonResult<Boolean> deleteWarehouse(@RequestParam("id") String id) {
        warehouseService.deleteWarehouse(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得仓库表")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<WarehouseRespVO> getWarehouse(@RequestParam("id") String id) {
        WarehouseDO warehouse = warehouseService.getWarehouse(id);
        return success(BeanUtils.toBean(warehouse, WarehouseRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得仓库表分页")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<PageResult<WarehouseRespVO>> getWarehousePage(@Valid WarehousePageReqVO pageReqVO) {
        PageResult<WarehouseDO> pageResult = warehouseService.getWarehousePage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        List<Long> userIdList = new ArrayList<>();
        //主管
        List<Long> userIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), WarehouseDO::getUserId));
        //创建人
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), WarehouseDO::getCreator));
        //更新人
        List<Long> updaterIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), WarehouseDO::getUpdater));

        //合并用户集合
        if (userIds!= null)userIdList.addAll(userIds);
        if (creatorIds != null)userIdList.addAll(creatorIds);
        if (updaterIds != null)userIdList.addAll(updaterIds);

        Map<Long, AdminUserRespDTO> userMap = null;
        if(CollectionUtils.isNotEmpty(userIdList)){
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            // 拼接数据
           userMap = userApi.getUserMap(userIdList);
        }

        return success(new PageResult<>(WarehouseConvert.INSTANCE.convertList(pageResult.getList(), userMap),
                pageResult.getTotal()));
//        return success(BeanUtils.toBean(pageResult, WarehouseRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得仓库信息列表", description = "主要用于前端的下拉选项")
    public CommonResult<List<WarehouseRespVO>> getWarehouseList() {
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseList();
        return success(BeanUtils.toBean(warehouseList, WarehouseRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出仓库表 Excel")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportWarehouseExcel(@Valid WarehousePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<WarehouseDO> list = warehouseService.getWarehousePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "仓库表.xls", "数据", WarehouseRespVO.class,
                        BeanUtils.toBean(list, WarehouseRespVO.class));
    }


    @GetMapping("/warehouseMenuTree")
    @Operation(summary = "仓库管理 - 左边树形根展示")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<WarehouseMenuTreeRespVO> warehouseMenuTree(
            @RequestParam(name = "type",required = false) Integer type) {
        long l1 = System.currentTimeMillis();
        System.err.println("================" + type);
        WarehouseMenuTreeRespVO warehouseMenuTree = new WarehouseMenuTreeRespVO();
        warehouseMenuTree.setName("三角防务厂区");
        // 获得仓库列表
        List<WarehouseDO> warehouseList = warehouseService.getWarehouseList();
        // 获得库区列表
        List<WarehouseAreaDO> warehouseAreaList = warehouseAreaService.getWarehouseAreaList();
        // 获得库位列表
        List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationList();

        // 组装库位map 库区id 作为key
        Map<String, List<WarehouseLocationSimpleVO>> warehouseLocationMap =warehouseLocationList.stream().map(a->{
            WarehouseLocationSimpleVO warehouseLocationSimpleVO = new WarehouseLocationSimpleVO();
            StringBuilder name = new StringBuilder();
            int c = a.getChannel() == null ? 1 : a.getChannel();
            int g = a.getGroupp() == null ? 1 : a.getGroupp();
            int l = a.getLayer() == null ? 1 : a.getLayer();
            int s = a.getSite() == null ? 1 : a.getSite();
            name.append("通道").append(c).append("-");
            name.append("组").append(g).append("-");
            name.append("层").append(l).append("-");
            name.append("位").append(s).append("-");
//            a.setName(name.substring(0,name.length()-1));
            warehouseLocationSimpleVO.setName(name.substring(0,name.length()-1));
            warehouseLocationSimpleVO.setWarehouseAreaId(a.getWarehouseAreaId());
            warehouseLocationSimpleVO.setId(a.getId());
            return warehouseLocationSimpleVO;
        }).collect(Collectors.groupingBy(WarehouseLocationSimpleVO::getWarehouseAreaId));

        // 组装库区map 仓库id 作为key
        Map<String, List<WarehouseAreaSimpleVO>> warehouseAreaMap =warehouseAreaList.stream().map(a->{
            WarehouseAreaSimpleVO warehouseAreaSimpleVO = new WarehouseAreaSimpleVO();
            /*a.setName(a.getAreaName());
            a.setChildrens(warehouseLocationMap.get(a.getId()));*/
            warehouseAreaSimpleVO.setId(a.getId());
            warehouseAreaSimpleVO.setWarehouseId(a.getWarehouseId());
            warehouseAreaSimpleVO.setName(a.getAreaName());
            warehouseAreaSimpleVO.setChildrens(warehouseLocationMap.get(a.getId()));
            return warehouseAreaSimpleVO;
        }).collect(Collectors.groupingBy(WarehouseAreaSimpleVO::getWarehouseId));

        // 组装仓库map 将库区数据塞入仓库
        List<WarehouseSimpleVO> warehouseSimpleList = warehouseList.stream().map(w->{
            /*w.setName(w.getWarehouseName());
            w.setChildrens(warehouseAreaMap.get(w.getId()));*/
            WarehouseSimpleVO warehouseSimpleVO = new WarehouseSimpleVO();
            warehouseSimpleVO.setId(w.getId());
            warehouseSimpleVO.setName(w.getWarehouseName());
            warehouseSimpleVO.setChildrens(warehouseAreaMap.get(w.getId()));
            return warehouseSimpleVO;
        }).collect(Collectors.toList());
        // 组装树形结构
        warehouseMenuTree.setChildrens(warehouseSimpleList);
        System.err.println("=" + (System.currentTimeMillis() - l1));
        return success(warehouseMenuTree);
    }



    @GetMapping("/warehouseLocationMenuTree")
    @Operation(summary = "仓库管理 - 左边树形根展示")
    @PreAuthorize("@ss.hasPermission('wms:warehouse:query')")
    public CommonResult<List<WarehouseMenuTreeRespVO>> warehouseLocationMenuTree(
            HttpServletRequest request,
            @RequestParam(name = "type",required = false) Integer type,
            @RequestParam(name = "warehouseId",required = false) String warehouseId,
            @RequestParam(name = "areaTypes",required = false) Collection<String> areaTypes) {
        long l1 = System.currentTimeMillis();
        System.err.println("================" + type);
        ArrayList<WarehouseMenuTreeRespVO> result = new ArrayList<>();
        if(type == null || type == 1){

            WarehouseMenuTreeRespVO warehouseMenuTree = new WarehouseMenuTreeRespVO();
            result.add(warehouseMenuTree);
            warehouseMenuTree.setName("选择库位");

            List<WarehouseDO> warehouseList = new ArrayList<>();
            // 获得仓库列表
            if(StringUtils.isNotBlank(warehouseId)){
                warehouseList.add(warehouseService.getWarehouse(warehouseId));
            }else {
                warehouseList = warehouseService.getWarehouseList();
            }

            // 获得库区列表
            List<WarehouseAreaDO> warehouseAreaList = warehouseAreaService.getWarehouseAreaByWarehouseIdAndAreaTypes(warehouseId, areaTypes);

            // 获得库位列表
            List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationList();

            // 组装库位map 库区id 作为key
            Map<String, List<WarehouseLocationSimpleVO>> warehouseLocationMap =warehouseLocationList.stream().map(a->{
                WarehouseLocationSimpleVO warehouseLocationSimpleVO = new WarehouseLocationSimpleVO();
                StringBuilder name = new StringBuilder();
                int c = a.getChannel() == null ? 1 : a.getChannel();
                int g = a.getGroupp() == null ? 1 : a.getGroupp();
                int l = a.getLayer() == null ? 1 : a.getLayer();
                int s = a.getSite() == null ? 1 : a.getSite();
                name.append("通道").append(c).append("-");
                name.append("组").append(g).append("-");
                name.append("层").append(l).append("-");
                name.append("位").append(s).append("-");
    //            a.setName(name.substring(0,name.length()-1));
                warehouseLocationSimpleVO.setName(name.substring(0,name.length()-1));
                warehouseLocationSimpleVO.setWarehouseAreaId(a.getWarehouseAreaId());
                warehouseLocationSimpleVO.setId(a.getId());
                return warehouseLocationSimpleVO;
            }).collect(Collectors.groupingBy(WarehouseLocationSimpleVO::getWarehouseAreaId));

            // 组装库区map 仓库id 作为key
            Map<String, List<WarehouseAreaSimpleVO>> warehouseAreaMap =warehouseAreaList.stream().map(a->{
                WarehouseAreaSimpleVO warehouseAreaSimpleVO = new WarehouseAreaSimpleVO();
                /*a.setName(a.getAreaName());
                a.setChildrens(warehouseLocationMap.get(a.getId()));*/
                warehouseAreaSimpleVO.setId(a.getId());
                warehouseAreaSimpleVO.setWarehouseId(a.getWarehouseId());
                warehouseAreaSimpleVO.setName(a.getAreaName());
                warehouseAreaSimpleVO.setChildrens(warehouseLocationMap.get(a.getId()));
                return warehouseAreaSimpleVO;
            }).collect(Collectors.groupingBy(WarehouseAreaSimpleVO::getWarehouseId));

            // 组装仓库map 将库区数据塞入仓库
            List<WarehouseSimpleVO> warehouseSimpleList = warehouseList.stream().map(w->{
                /*w.setName(w.getWarehouseName());
                w.setChildrens(warehouseAreaMap.get(w.getId()));*/
                WarehouseSimpleVO warehouseSimpleVO = new WarehouseSimpleVO();
                warehouseSimpleVO.setId(w.getId());
                warehouseSimpleVO.setName(w.getWarehouseName());
                warehouseSimpleVO.setChildrens(warehouseAreaMap.get(w.getId()));
                return warehouseSimpleVO;
            }).collect(Collectors.toList());
            // 组装树形结构
            warehouseMenuTree.setChildrens(warehouseSimpleList);
            System.err.println("=" + (System.currentTimeMillis() - l1));
        }


        if(type == null || type == 2){

            /**
             * 封装容器库存
             */
            WarehouseMenuTreeRespVO stockMenuTree = new WarehouseMenuTreeRespVO();
            result.add(stockMenuTree);
            stockMenuTree.setName("选择储位");
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
            stockMenuTree.setChildrens(BeanUtils.toBean(containerStockList, WarehouseSimpleVO.class));

        }

        return success(result);
    }

}