package com.miyu.module.wms.controller.admin.checkplan;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.convert.checkplan.CheckPlanConvert;
import com.miyu.module.wms.dal.dataobject.checkcontainer.CheckContainerDO;
import com.miyu.module.wms.dal.dataobject.checkdetail.CheckDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.checkcontainer.CheckContainerService;
import com.miyu.module.wms.service.checkdetail.CheckDetailService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
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
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.wms.controller.admin.checkplan.vo.*;
import com.miyu.module.wms.dal.dataobject.checkplan.CheckPlanDO;
import com.miyu.module.wms.service.checkplan.CheckPlanService;

@Tag(name = "管理后台 - 库存盘点计划")
@RestController
@RequestMapping("/wms/check-plan")
@Validated
public class CheckPlanController {

    @Resource
    private CheckPlanService checkPlanService;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private CheckContainerService checkContainerService;
    @Resource
    private CheckDetailService checkDetailService;
    @Resource
    private MaterialStorageService materialStorageService;

    @PostMapping("/create")
    @Operation(summary = "创建库存盘点计划")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:create')")
    public CommonResult<String> createCheckPlan(@Valid @RequestBody CheckPlanSaveReqVO createReqVO) {
        return success(checkPlanService.createCheckPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新库存盘点计划")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:update')")
    public CommonResult<Boolean> updateCheckPlan(@Valid @RequestBody CheckPlanSaveReqVO updateReqVO) {
        checkPlanService.updateCheckPlan(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除库存盘点计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:check-plan:delete')")
    public CommonResult<Boolean> deleteCheckPlan(@RequestParam("id") String id) {
        checkPlanService.deleteCheckPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得库存盘点计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:query')")
    public CommonResult<CheckPlanRespVO> getCheckPlan(@RequestParam("id") String id) {
        CheckPlanDO checkPlan = checkPlanService.getCheckPlan(id);
        return success(BeanUtils.toBean(checkPlan, CheckPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得库存盘点计划分页")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:query')")
    public CommonResult<PageResult<CheckPlanRespVO>> getCheckPlanPage(@Valid CheckPlanPageReqVO pageReqVO) {
        PageResult<CheckPlanDO> pageResult = checkPlanService.getCheckPlanPage(pageReqVO);
        List<String> containerConfigListByIds = new ArrayList<>();
        pageResult.getList().forEach(checkPlanDO -> {
            if (StringUtils.isNotBlank(checkPlanDO.getMaterialConfigIds())) {
                List<String> ids = StringListUtils.stringToArrayList(checkPlanDO.getMaterialConfigIds());
                containerConfigListByIds.addAll(ids);
            }
        });

        List<MaterialConfigDO> materialConfigListByIds = materialConfigService.getMaterialConfigListByIds(containerConfigListByIds.stream().distinct().collect(Collectors.toList()));

        Map<String, MaterialConfigDO> stringMaterialConfigDOMap = CollectionUtils.convertMap(materialConfigListByIds, MaterialConfigDO::getId);

        return success(new PageResult<>(CheckPlanConvert.INSTANCE.convertList(pageResult.getList(), stringMaterialConfigDOMap),
                pageResult.getTotal()));
    }

    @GetMapping("/taskPage")
    @Operation(summary = "获得库存盘点计划分页")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:query')")
    public CommonResult<PageResult<CheckPlanRespVO>> getCheckTaskPage(@Valid CheckPlanPageReqVO pageReqVO) {
        PageResult<CheckPlanDO> pageResult = checkPlanService.getCheckTaskPage(pageReqVO);
        List<String> containerConfigListByIds = new ArrayList<>();
        pageResult.getList().forEach(checkPlanDO -> {
            if (StringUtils.isNotBlank(checkPlanDO.getMaterialConfigIds())) {
                List<String> ids = StringListUtils.stringToArrayList(checkPlanDO.getMaterialConfigIds());
                containerConfigListByIds.addAll(ids);
            }
        });

        List<MaterialConfigDO> materialConfigListByIds = materialConfigService.getMaterialConfigListByIds(containerConfigListByIds.stream().distinct().collect(Collectors.toList()));

        Map<String, MaterialConfigDO> stringMaterialConfigDOMap = CollectionUtils.convertMap(materialConfigListByIds, MaterialConfigDO::getId);

        return success(new PageResult<>(CheckPlanConvert.INSTANCE.convertList(pageResult.getList(), stringMaterialConfigDOMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出库存盘点计划 Excel")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCheckPlanExcel(@Valid CheckPlanPageReqVO pageReqVO,
                                     HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CheckPlanDO> list = checkPlanService.getCheckPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "库存盘点计划.xls", "数据", CheckPlanRespVO.class,
                BeanUtils.toBean(list, CheckPlanRespVO.class));
    }


    @GetMapping("/getWarehouseLocationDetail")
    @Operation(summary = "获得库区详情")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:query')")
    public CommonResult<WarehouseLocationDO[][][][]> getWarehouseLocationDetail(@RequestParam(value = "checkPlanId", required = true) String checkPlanId) {
        if (StringUtils.isBlank(checkPlanId)) {
            return success(null);
        }
        CheckPlanDO checkPlan = checkPlanService.getCheckPlan(checkPlanId);
        // 获得库区详情
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseArea(checkPlan.getCheckAreaId());
        // 获得库位列表
        List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationByAreaId(checkPlan.getCheckAreaId());
//        warehouseArea.setChildrens(warehouseLocationList);
        // 获得库位上的托盘列表
        List<CheckContainerDO> containerStockList = checkContainerService.getCheckContainerAndLocationIdByCheckPlanId(checkPlanId);
        Map<String, CheckContainerDO> locationIdcheckContainerMap = CollectionUtils.convertMap(containerStockList, CheckContainerDO::getLocationId);

        // 获取盘点详情
        List<CheckDetailDO> checkDetailList = checkDetailService.getCheckDetailByCheckContainerIds(containerStockList.stream().map(CheckContainerDO::getId).collect(Collectors.toList()));
        Map<String, List<CheckDetailDO>> containerIdCheckDetailMap = CollectionUtils.convertMap(
                checkDetailList,
                CheckDetailDO::getCheckContainerId,
                c -> {
                    List<CheckDetailDO> checkDetails = new ArrayList<>();
                    checkDetails.add(c);
                    return checkDetails;
                },
                (v1, v2) -> {
                    v1.addAll(v2);
                    return v1;
                });

        // 将盘点容器信息加入库位
        warehouseLocationList.forEach(location -> {
            if (locationIdcheckContainerMap.containsKey(location.getId())) {
                CheckContainerDO checkContainer = locationIdcheckContainerMap.get(location.getId());
                if (containerIdCheckDetailMap.containsKey(checkContainer.getId())) {
                    List<CheckDetailDO> checkDetailDOS = containerIdCheckDetailMap.get(checkContainer.getId());
                    AtomicInteger totalCount = checkContainer.getTotalCount() == null ? new AtomicInteger() : new AtomicInteger(checkContainer.getTotalCount());
                    checkDetailDOS.forEach(checkDetailDO -> {
                        // 计算已盘点数量
                        totalCount.addAndGet(checkDetailDO.getCheckTotality());
                    });
                    //  已盘点数量 假如盘点容器
                    checkContainer.setTotalCount(totalCount.get());
                }
                // 盘点容器信息加入库位
                location.setCheckContainer(checkContainer);
            }

        });

        // 弄一个四维数组，第一维是通道，第二维是组，第三维是层，第四维是库位和其上的托盘
//        List<List<List<List<WarehouseLocationDO>>>> resultList = new ArrayList<>();
        // 根据 通道 组 层 位  创建一个四维数组
        int channelNum = warehouseArea.getAreaChannels()==null?1:warehouseArea.getAreaChannels();
        int groupNum = warehouseArea.getAreaGroup()==null?1:warehouseArea.getAreaGroup();
        int layerNum = warehouseArea.getAreaLayer()==null?1:warehouseArea.getAreaLayer();
        int siteNum = warehouseArea.getAreaSite()==null?1:warehouseArea.getAreaSite();

        WarehouseLocationDO[][][][] resultList = new WarehouseLocationDO[channelNum][groupNum][layerNum][siteNum];

        warehouseLocationList.forEach(location -> {
            int channel = location.getChannel()==null?1:location.getChannel();
            int group = location.getGroupp()==null?1:location.getGroupp();
            int layer = location.getLayer()==null?1:location.getLayer();
            int site = location.getSite()==null?1:location.getSite();
            resultList[channel-1][group-1][layer-1][site-1] = location;
//            // 初始化 通道列表
//            List<List<List<WarehouseLocationDO>>> channelList = null;
//            if (resultList.size() < channel) {
//                channelList = new ArrayList<>();
//                resultList.add(channelList);
//            } else {
//                channelList = resultList.get(channel - 1);
//            }
//
//            // 初始化 组列表
//            List<List<WarehouseLocationDO>> groupList = null;
//            if (channelList.size() < group) {
//                groupList = new ArrayList<>();
//                channelList.add(groupList);
//            } else {
//                groupList = channelList.get(group - 1);
//            }
//
//            // 初始化 层列表
//            List<WarehouseLocationDO> layerList = null;
//            if (groupList.size() < layer) {
//                layerList = new ArrayList<>();
//                groupList.add(layerList);
//            } else {
//                layerList = groupList.get(layer - 1);
//            }
//            // 加入库位
//            layerList.add(location);
        });

        return success(resultList);
    }


    @PostMapping("/startCheckTask")
    @Operation(summary = "开始盘点任务")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:create')")
    public CommonResult<CheckContainerDO> startCheckTask(@RequestBody CheckContainerDO checkContainer) {
        List<CheckDetailDO> checkDetailList = checkDetailService.getCheckDetailByCheckContainerIds(Collections.singletonList(checkContainer.getId()));
        if(checkDetailList == null || checkDetailList.isEmpty()){
            checkDetailList =checkDetailService.createCheckDetailByCheckContainerId(checkContainer.getId(),checkContainer.getContainerStockId());
        }
        String containerStockId = checkContainer.getContainerStockId();
        checkContainer.setContainerStoragelist(materialStorageService.getMaterialStorageListByContainerStockId(containerStockId));
        checkContainer.setCheckDetailList(checkDetailList);
        return success(checkContainer);
    }

    @PostMapping("/saveCheckPlanItem")
    @Operation(summary = "保存盘点")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:create')")
    public CommonResult<Boolean> saveCheckPlanItem(@RequestBody List<CheckDetailDO> checkDetailList) {
        if(checkDetailList == null || checkDetailList.isEmpty()){
            return success(true);
        }
        checkPlanService.updateCheckPlanStatusByCheckContainerId(checkDetailList.get(0).getCheckContainerId());
        return success(checkDetailService.saveCheckDetailItem(checkDetailList));
    }

    @PostMapping("/submitCheckPlanItem")
    @Operation(summary = "提交盘点")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:create')")
    public CommonResult<Boolean> submitCheckPlanItem(@RequestBody List<CheckDetailDO> checkDetailList) {
        if(checkDetailList == null || checkDetailList.isEmpty()){
            return success(true);
        }
        checkDetailService.submitCheckPlanItem(checkDetailList);
        return success(true);
    }

    @PostMapping("/submitCheckPlan")
    @Operation(summary = "提交盘点")
    @PreAuthorize("@ss.hasPermission('wms:check-plan:create')")
    public CommonResult<Boolean> submitCheckPlan(String checkPlanId) {
        checkPlanService.submitCheckPlan(checkPlanId);
        return success(true);
    }


}