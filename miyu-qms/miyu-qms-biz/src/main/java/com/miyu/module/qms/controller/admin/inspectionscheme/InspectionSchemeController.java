package com.miyu.module.qms.controller.admin.inspectionscheme;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailRetraceDTO;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailRetraceDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcessPlanDetailRespDTO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetMaterialReqVO;
import com.miyu.module.qms.convert.InspectionSchemeConvert;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.service.inspectionsheet.InspectionSheetService;
import com.miyu.module.qms.utils.StringListUtils;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import io.swagger.v3.oas.annotations.Parameters;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.time.LocalDateTime;
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
import static com.miyu.module.qms.enums.ErrorCodeConstants.*;

import com.miyu.module.qms.controller.admin.inspectionscheme.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionscheme.InspectionSchemeDO;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import com.miyu.module.qms.service.inspectionscheme.InspectionSchemeService;

@Tag(name = "管理后台 - 检验方案")
@RestController
@RequestMapping("/qms/inspection-scheme")
@Validated
@Slf4j
public class InspectionSchemeController {

    @Resource
    private InspectionSchemeService inspectionSchemeService;
    @Resource
    private ShippingApi shippingApi;
    @Resource
    private ShippingReturnApi shippingReturnApi;
    @Resource
    private InspectionSheetService inspectionSheetService;
    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private MaterialMCCApi materialMCCApi;

    @PostMapping("/create")
    @Operation(summary = "创建检验方案")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:create')")
    public CommonResult<String> createInspectionScheme(@Valid @RequestBody InspectionSchemeSaveReqVO createReqVO) {
        return success(inspectionSchemeService.createInspectionScheme(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验方案")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:update')")
    public CommonResult<Boolean> updateInspectionScheme(@Valid @RequestBody InspectionSchemeSaveReqVO updateReqVO) {
        inspectionSchemeService.updateInspectionScheme(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验方案")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:delete')")
    public CommonResult<Boolean> deleteInspectionScheme(@RequestParam("id") String id) {
        inspectionSchemeService.deleteInspectionScheme(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验方案")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:query')")
    public CommonResult<InspectionSchemeRespVO> getInspectionScheme(@RequestParam("id") String id) {
        InspectionSchemeDO inspectionScheme = inspectionSchemeService.getInspectionScheme(id);

        Map<String,ProcedureRespDTO> map;
        if (!StringUtils.isEmpty(inspectionScheme.getProcessId())){
            List<String> produceIds = new ArrayList<>();
            produceIds.add(inspectionScheme.getProcessId());
            map = processPlanDetailApi.getProcedureMapByIds(produceIds);
        }
        else {
            map = new HashMap<>();
        }

        Map<String, MaterialConfigRespDTO> materialConfigMap;
        if (!StringUtils.isEmpty(inspectionScheme.getMaterialConfigId())){
            List<String> configIds = new ArrayList<>();
            configIds.add(inspectionScheme.getMaterialConfigId());
            materialConfigMap =  materialMCCApi.getMaterialConfigMap(configIds);
        }
        else {
            materialConfigMap = new HashMap<>();
        }

        return success(BeanUtils.toBean(inspectionScheme, InspectionSchemeRespVO.class, vo -> {
            if (!StringUtils.isEmpty(inspectionScheme.getProcessId())){
                MapUtils.findAndThen(map, vo.getProcessId(), a -> vo.setTechnologyName(a.getProcessName() + '('+ a.getProcessCode() + ')').setProcedureName(a.getProcedureName()));
            }
            if (!StringUtils.isEmpty(inspectionScheme.getMaterialConfigId())){
                MapUtils.findAndThen(materialConfigMap, vo.getMaterialConfigId(), a -> vo.setMaterialTypeName(a.getMaterialTypeName()).setMaterialSpecification(a.getMaterialSpecification()).setMaterialUnit(a.getMaterialUnit()));
            }
        }));
    }


    @PutMapping("/submitEffective")
    @Operation(summary = "更改生效状态")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:update')")
    public CommonResult<Boolean> submitEffective(@RequestParam("id") String id, @RequestParam("isEffective") Integer isEffective) {
        inspectionSchemeService.submitEffective(id, isEffective);
        return success(true);
    }


    @GetMapping("/page")
    @Operation(summary = "获得检验方案分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:query')")
    public CommonResult<PageResult<InspectionSchemeRespVO>> getInspectionSchemePage(@Valid InspectionSchemePageReqVO pageReqVO) {
        PageResult<InspectionSchemeDO> pageResult = inspectionSchemeService.getInspectionSchemePage(pageReqVO);
        // 查询物料类型
        List<String> configIds = convertList(pageResult.getList(),InspectionSchemeDO::getMaterialConfigId);
        Map<String, MaterialConfigRespDTO> materialConfigMap =  materialMCCApi.getMaterialConfigMap(configIds);

        // 查询工序集合
        List<String> produceIds = convertList(pageResult.getList(),InspectionSchemeDO::getProcessId);
        Map<String,ProcedureRespDTO> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(produceIds)){
            map = processPlanDetailApi.getProcedureMapByIds(produceIds);
        }

        return success(new PageResult<>(InspectionSchemeConvert.INSTANCE.convertList(pageResult.getList(), map, materialConfigMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验方案 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSchemeExcel(@Valid InspectionSchemePageReqVO pageReqVO,
                                            HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSchemeDO> list = inspectionSchemeService.getInspectionSchemePage(pageReqVO).getList();

        // 查询物料类型
        List<String> configIds = convertList(list,InspectionSchemeDO::getMaterialConfigId);
        Map<String, MaterialConfigRespDTO> materialConfigMap =  materialMCCApi.getMaterialConfigMap(configIds);

        // 查询工序集合
        List<String> produceIds = convertList(list,InspectionSchemeDO::getProcessId);
        Map<String,ProcedureRespDTO> map = new HashMap<>();
        if (!CollectionUtils.isEmpty(produceIds)){
            map = processPlanDetailApi.getProcedureMapByIds(produceIds);
        }
        // 导出 Excel
        ExcelUtils.write(response, "检验方案.xls", "数据", InspectionSchemeRespVO.class,
                InspectionSchemeConvert.INSTANCE.convertList(list, map, materialConfigMap));
    }


    @GetMapping("/list-4-inspection-sheet")
    @Operation(summary = "获得检验方案集合")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:query')")
    public CommonResult<List<InspectionSchemeRespVO>> getInspectionSchemeList4InspectionSheet(InspectionSchemeReqVO reqVO) {
        List<InspectionSchemeDO> list = inspectionSchemeService.getInspectionSchemeList4InspectionSheet(reqVO);
        return success(BeanUtils.toBean(list, InspectionSchemeRespVO.class));
    }

    // ==================== 子表（检验方案检测项目详情） ====================

    @GetMapping("/inspection-scheme-item/list-by-inspection-scheme-id")
    @Operation(summary = "获得检验方案检测项目详情列表")
    @Parameter(name = "inspectionSchemeId", description = "方案ID")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:query')")
    public CommonResult<List<InspectionSchemeItemDO>> getInspectionSchemeItemListByInspectionSchemeId(@RequestParam("inspectionSchemeId") String inspectionSchemeId) {
        return success(inspectionSchemeService.getInspectionSchemeItemListByInspectionSchemeId(inspectionSchemeId));
    }


    /***
     * 先查询物料基本信息
     * 1.1成品
     * 查找
     *
     * 1.2 非成品
     *
     *
     * @param no
     * @param batchNo
     * @return
     */
    @GetMapping("/getRetrace")
    @Operation(summary = "追溯")
    @Parameters({
            @Parameter(name = "no", description = "条码", required = true, example = "1024"),
            @Parameter(name = "batchNo", description = "批次码", required = true, example = "1024"),
    })
    public CommonResult<RetraceRespVO> getRetrace(@RequestParam("no") String no, @RequestParam("batchNo") String batchNo) {


        if (StringUtils.isEmpty(no)){
            throw exception(MATERIAL_NOT_EXISTS);
        }

        CommonResult<List<MaterialStockRespDTO>> commonResult= materialStockApi.getMaterialsByBarCode(no);
        if (CollectionUtils.isEmpty(commonResult.getCheckedData())){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }

        CommonResult<List<MaterialConfigRespDTO>> configList =  materialMCCApi.getMaterialConfigList(Lists.newArrayList(commonResult.getCheckedData().get(0).getMaterialConfigId()));


        RetraceRespVO vo = new RetraceRespVO();
        vo = BeanUtils.toBean(configList.getCheckedData().get(0),RetraceRespVO.class);
        vo.setBarCode(no);

        String id = null;
        //==========================出入库追溯==============================
        //先查物料信息  如果是成品（查找生产入库单找到生产批次）  根据生产批次查出所有记录
        //如果是零件  先查出入库单（采购的）   找到对应的生产批次  然后查出所有的出入库记录
        OrderReqDTO dto = new OrderReqDTO().setMaterialStockId(commonResult.getCheckedData().get(0).getId());



        //==========================销售追溯==============================
        List<Map<String, Object>> dmInfos = new ArrayList<>();
        //发货单追溯
        try {
            CommonResult<List<ShippingDetailRetraceDTO>> result = shippingApi.getShippingListByBarcode(no);
            List<ShippingDetailRetraceDTO> detailRetraceDTOS = result.getCheckedData();


            if (!CollectionUtils.isEmpty(detailRetraceDTOS)) {
                List<Map<String, Object>> results = detailRetraceDTOS.stream().map(shippingDetailRetraceDTO -> {
                    Map<String, Object> map = new LinkedHashMap<>();

                    map.put("retraceType", "发货单");
                    map.putAll(BeanUtil.beanToMap(shippingDetailRetraceDTO));
                    return map;
                }).collect(Collectors.toList());

                dmInfos.addAll(results);
            }


            //退货单追溯
            CommonResult<List<ShippingReturnDetailRetraceDTO>> results = shippingReturnApi.getShippingReturnListByBarcode(no);

            List<ShippingReturnDetailRetraceDTO> returnDetailRetraceDTOS = results.getCheckedData();

            if (!CollectionUtils.isEmpty(returnDetailRetraceDTOS)) {
                List<Map<String, Object>> resultMap = returnDetailRetraceDTOS.stream().map(shippingDetailRetraceDTO -> {
                    Map<String, Object> map = new LinkedHashMap<>();

                    map.put("retraceType", "退货单");
                    map.putAll(BeanUtil.beanToMap(shippingDetailRetraceDTO));
                    return map;
                }).collect(Collectors.toList());

                dmInfos.addAll(resultMap);
            }
        }catch (Exception e){
            log.error("查询销售系统失败");
        }





        List<Map<String, Object>> finalResults = StringListUtils.sortMapListByFeild(dmInfos, "createTime", "asc");

        vo.setDmInfos(finalResults);

        //==========================质量追溯==============================
        InspectionSheetMaterialReqVO reqVO = new InspectionSheetMaterialReqVO().setBarCode(no);
        if (!StringUtils.isEmpty(batchNo)){
            reqVO.setBatchNumber(batchNo);
        }




        List<InspectionSheetSchemeMaterialDO> inspectionSheetSchemeMaterialDOS = inspectionSheetService.getInspectionSheetInfoMaterial(reqVO);

        List<Map<String, Object>> inspections = new ArrayList<>();

        if (!CollectionUtils.isEmpty(inspectionSheetSchemeMaterialDOS)) {
            List<Map<String, Object>> resultMap = inspectionSheetSchemeMaterialDOS.stream().map(shippingDetailRetraceDTO -> {
                Map<String, Object> map = new LinkedHashMap<>();

                map.put("retraceType", "质检单");
                map.putAll(BeanUtil.beanToMap(shippingDetailRetraceDTO));
                return map;
            }).collect(Collectors.toList());

            inspections.addAll(resultMap);
        }

        List<Map<String, Object>> inspectionsResults = StringListUtils.sortMapListByFeild(inspections, "createTime", "asc");

        vo.setInspectionInfos(inspectionsResults);


        //==========================生产追溯==============================
        List<Map<String, Object>> value = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        map.put("orderNumber", "T00001");
        map.put("retraceType", "生产单");
        map.put("name", "工序");
        map.put("progess", "工序111");
        LocalDateTime localDateTime = LocalDateTime.now();
        map.put("createTime", localDateTime);
        value.add(map);
        Map<String, Object> map1 = new HashMap<>();
        map1.put("orderNumber", "T00001");
        map1.put("retraceType", "生产单");
        map1.put("name", "工序");
        value.add(map1);
        vo.setProductsInfos(value);
        return success(vo);
    }





    @GetMapping("/getProcessPage")
    @Operation(summary = "获得工艺规程分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:query')")
    public CommonResult<PageResult<ProcessPlanDetailRespDTO>> getProcessPage(@Valid PageParam pageReqVO) {

        CommonResult<List<ProcessPlanDetailRespDTO>> result =  processPlanDetailApi.getProcessPlanList("");
        List<ProcessPlanDetailRespDTO> list = result.getCheckedData();
        PageResult<ProcessPlanDetailRespDTO> pageResult = new PageResult();
        if (!CollectionUtils.isEmpty(list)){
            int count = list.size(); // 总记录数
            // 计算总页数
            int pages = count % pageReqVO.getPageSize() == 0 ? count / pageReqVO.getPageSize() : count / pageReqVO.getPageSize() + 1;
            // 起始位置
            int start = pageReqVO.getPageNo() <= 0 ? 0 : (pageReqVO.getPageNo() > pages ? (pages - 1) * pageReqVO.getPageSize() : (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize());
            // 终止位置
            int end = pageReqVO.getPageNo() <= 0 ? (pageReqVO.getPageSize() <= count ? pageReqVO.getPageSize() : count) : (pageReqVO.getPageSize() * pageReqVO.getPageNo() <= count ? pageReqVO.getPageSize() * pageReqVO.getPageNo() : count);

            pageResult.setTotal((long)count);
            pageResult.setList(list.subList(start, end));
        }
        return success(pageResult);
    }




    @GetMapping("/getProcedure")
    @Operation(summary = "获得工艺规程下的工序")
    @Parameter(name = "id", description = "工艺规程ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme:query')")
    public CommonResult<List<ProcedureRespDTO>> getProcedure(@RequestParam("id") String id) {
        return processPlanDetailApi.getProcedureListByProcessVersionId(id);
    }




}
