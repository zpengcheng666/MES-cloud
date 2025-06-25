package com.miyu.module.tms.controller.admin.toolinfo;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleRecordRespVO;
import com.miyu.module.tms.controller.admin.assembletask.vo.AssembleTaskPageReqVO;
import com.miyu.module.tms.convert.assemblerecord.AssembleRecordConvert;
import com.miyu.module.tms.strategy.AssembleRecordFactory;
import com.miyu.module.tms.strategy.IAssembleRecordStrategy;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import lombok.extern.slf4j.Slf4j;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.tms.controller.admin.toolinfo.vo.*;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBalanceDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolBaseDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.service.toolinfo.ToolInfoService;

@Tag(name = "管理后台 - 刀组信息")
@RestController
@RequestMapping("/tms/tool-info")
@Validated
@Slf4j
public class ToolInfoController {

    @Resource
    private ToolInfoService toolInfoService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private MaterialConfigApi materialConfigApi;
    @Resource
    private AssembleRecordFactory assembleRecordFactory;

    @PostMapping("/create")
    @Operation(summary = "创建刀组信息")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:create')")
    public CommonResult<String> createToolInfo(@Valid @RequestBody ToolInfoSaveReqVO createReqVO) {
        return success(toolInfoService.createToolInfo(createReqVO));
    }



    @PutMapping("/update")
    @Operation(summary = "更新刀组信息")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:update')")
    public CommonResult<Boolean> updateToolInfo(@Valid @RequestBody ToolInfoSaveReqVO updateReqVO) {
        toolInfoService.updateToolInfo(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀组信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-info:delete')")
    public CommonResult<Boolean> deleteToolInfo(@RequestParam("id") String id) {
        toolInfoService.deleteToolInfo(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀组信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
    public CommonResult<ToolInfoRespVO> getToolInfo(@RequestParam("id") String id) {
        List<ToolInfoDO> toolInfos = toolInfoService.getToolInfoById(id);
        ToolInfoDO toolInfo = toolInfos.get(0);
        CommonResult<MaterialConfigRespDTO> commonResult = null;
        try {
           commonResult = materialConfigApi.getMaterialConfigById(toolInfo.getMaterialConfigId());
        } catch (Exception e) {
           log.error("调用WMS系统失败");
        }
        toolInfo.setMaterialNumber(commonResult.getData().getMaterialNumber());


        List<AssembleRecordDO> assembleRecordDOS = toolInfoService.getAssembleRecordListByToolInfoId(id,3);


        Set<String> stockCollect = assembleRecordDOS.stream().map(AssembleRecordDO::getMaterialStockId).collect(Collectors.toSet());
        CommonResult<List<MaterialStockRespDTO>> materialStockRespDTOS = materialStockApi.getMaterialsByIds(stockCollect);
        Map<String, MaterialStockRespDTO>  stockRespDTOMap =   CollectionUtils.convertMap(materialStockRespDTOS.getCheckedData(), MaterialStockRespDTO::getId);

        Set<String> collect = materialStockRespDTOS.getCheckedData().stream().map(MaterialStockRespDTO::getMaterialConfigId).collect(Collectors.toSet());
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(collect);


        return success(AssembleRecordConvert.INSTANCE.toToolInfoAndRecord(toolInfo,stockRespDTOMap,materialConfigMap,assembleRecordDOS));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀组信息分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
    public CommonResult<PageResult<ToolInfoRespVO>> getToolInfoPage(@Valid ToolInfoPageReqVO pageReqVO) {
        PageResult<ToolInfoDO> pageResult = toolInfoService.getToolInfoPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolInfoRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀组信息 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolInfoExcel(@Valid ToolInfoPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolInfoDO> list = toolInfoService.getToolInfoPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀组信息.xls", "数据", ToolInfoRespVO.class,
                        BeanUtils.toBean(list, ToolInfoRespVO.class));
    }

    // ==================== 子表（刀具动平衡） ====================

    @GetMapping("/tool-balance/page")
    @Operation(summary = "获得刀具动平衡分页")
    @Parameter(name = "toolInfoId", description = "成品刀具id")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
    public CommonResult<PageResult<ToolBalanceDO>> getToolBalancePage(PageParam pageReqVO,
                                                                                        @RequestParam("toolInfoId") String toolInfoId) {
        return success(toolInfoService.getToolBalancePage(pageReqVO, toolInfoId));
    }

    @PostMapping("/tool-balance/create")
    @Operation(summary = "创建刀具动平衡")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:create')")
    public CommonResult<String> createToolBalance(@Valid @RequestBody ToolBalanceDO toolBalance) {
        return success(toolInfoService.createToolBalance(toolBalance));
    }

    @PutMapping("/tool-balance/update")
    @Operation(summary = "更新刀具动平衡")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:update')")
    public CommonResult<Boolean> updateToolBalance(@Valid @RequestBody ToolBalanceDO toolBalance) {
        toolInfoService.updateToolBalance(toolBalance);
        return success(true);
    }

    @DeleteMapping("/tool-balance/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除刀具动平衡")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:delete')")
    public CommonResult<Boolean> deleteToolBalance(@RequestParam("id") String id) {
        toolInfoService.deleteToolBalance(id);
        return success(true);
    }

	@GetMapping("/tool-balance/get")
	@Operation(summary = "获得刀具动平衡")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
	public CommonResult<ToolBalanceDO> getToolBalance(@RequestParam("id") String id) {
	    return success(toolInfoService.getToolBalance(id));
	}

    // ==================== 子表（对刀数据） ====================

    @GetMapping("/tool-base/page")
    @Operation(summary = "获得对刀数据分页")
    @Parameter(name = "toolInfoId", description = "成品刀具id")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
    public CommonResult<PageResult<ToolBaseDO>> getToolBasePage(PageParam pageReqVO,@RequestParam("toolInfoId") String toolInfoId) {
        return success(toolInfoService.getToolBasePage(pageReqVO, toolInfoId));
    }

    @PostMapping("/tool-base/create")
    @Operation(summary = "创建对刀数据")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:create')")
    public CommonResult<String> createToolBase(@Valid @RequestBody ToolBaseDO toolBase) {
        return success(toolInfoService.createToolBase(toolBase));
    }

    @PutMapping("/tool-base/update")
    @Operation(summary = "更新对刀数据")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:update')")
    public CommonResult<Boolean> updateToolBase(@Valid @RequestBody ToolBaseDO toolBase) {
        toolInfoService.updateToolBase(toolBase);
        return success(true);
    }

    @DeleteMapping("/tool-base/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除对刀数据")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:delete')")
    public CommonResult<Boolean> deleteToolBase(@RequestParam("id") String id) {
        toolInfoService.deleteToolBase(id);
        return success(true);
    }

	@GetMapping("/tool-base/get")
	@Operation(summary = "获得对刀数据")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
	public CommonResult<ToolBaseDO> getToolBase(@RequestParam("id") String id) {
	    return success(toolInfoService.getToolBase(id));
	}

    // ==================== 子表（刀具装配记录） ====================

    @GetMapping("/assemble-record/page")
    @Operation(summary = "获得刀具装配记录分页")
    @Parameter(name = "toolInfoId", description = "成品刀具id")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
    public CommonResult<PageResult<AssembleRecordRespVO>> getAssembleRecordPage(AssembleTaskPageReqVO pageReqVO, @RequestParam("toolInfoId") String toolInfoId) {
        PageResult<AssembleRecordDO> assembleRecordResult = toolInfoService.getAssembleRecordPage(pageReqVO, toolInfoId);
        PageResult<AssembleRecordRespVO> pageResult = new PageResult<>();
        if (assembleRecordResult.getList() != null && !assembleRecordResult.getList().isEmpty()) {
            List<AssembleRecordDO> assembleRecordList = assembleRecordResult.getList();

            HashSet<String> materialConfigIds = new HashSet<>();
            HashSet<String> materialStockIds = new HashSet<>();
            assembleRecordList.forEach(item -> {
                materialStockIds.add(item.getMaterialStockId());
            });
            //获取物料库存信息
            CommonResult<List<MaterialStockRespDTO>> materialResult = materialStockApi.getMaterialsByIds(materialStockIds);
            Map<String, MaterialStockRespDTO> materialMap;
            if (materialResult.isSuccess() && materialResult.getData() != null) {
                materialMap = new HashMap<>();
                materialResult.getData().forEach(item -> {
                    materialMap.put(item.getId(), item);
                    materialConfigIds.add(item.getMaterialConfigId());
                });
            } else {
                materialMap = null;
            }
            //获取物料类型信息
            Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);
            List<AssembleRecordRespVO> assembleRecordRespVO = AssembleRecordConvert.INSTANCE.toAssembleRecordRespVO(assembleRecordList, materialMap, materialConfigMap);

            pageResult.setTotal((long) assembleRecordRespVO.size()).setList(assembleRecordRespVO);
        }
        return success(pageResult);
    }

    @PostMapping("/assemble-record/create")
    @Operation(summary = "创建刀具装配记录")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:create')")
    public CommonResult<String> createAssembleRecord(@Valid @RequestBody AssembleRecordDO assembleRecord) {
        return success(toolInfoService.createAssembleRecord(assembleRecord));
    }





    @PostMapping("/assemble-record/saveAssembleRecord")
    @Operation(summary = "创建刀组装配记录信息")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:create')")
    public CommonResult<String> saveAssembleRecord(@Valid @RequestBody ToolInfoSaveReqVO createReqVO) {

        IAssembleRecordStrategy strategy = assembleRecordFactory.generatorStrategy(createReqVO.getType());
        strategy.saveRecord(createReqVO);
        //toolInfoService.saveUpdateAssembleRecord(createReqVO);

        return success("成功");
    }

    @PutMapping("/assemble-record/update")
    @Operation(summary = "更新刀具装配记录")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:update')")
    public CommonResult<Boolean> updateAssembleRecord(@Valid @RequestBody AssembleRecordDO assembleRecord) {
        toolInfoService.updateAssembleRecord(assembleRecord);
        return success(true);
    }

    @DeleteMapping("/assemble-record/delete")
    @Parameter(name = "id", description = "编号", required = true)
    @Operation(summary = "删除刀具装配记录")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:delete')")
    public CommonResult<Boolean> deleteAssembleRecord(@RequestParam("id") String id) {
        toolInfoService.deleteAssembleRecord(id);
        return success(true);
    }

	@GetMapping("/assemble-record/get")
	@Operation(summary = "获得刀具装配记录")
	@Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
	public CommonResult<AssembleRecordDO> getAssembleRecord(@RequestParam("id") String id) {
	    return success(toolInfoService.getAssembleRecord(id));
	}
	@GetMapping("/assemble-record/getCurrentAssembleRecord")
	@Operation(summary = "获得当前已装刀具装配记录")
	@Parameter(name = "id", description = "刀组id", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-info:query')")
	public CommonResult<List<AssembleRecordVO>> getCurrentAssembleRecord(@RequestParam("id") String id) {
        List<AssembleRecordDO> currentAssembleRecordList = toolInfoService.getCurrentAssembleRecordByToolInfoId(id);
        if (!CollectionUtils.isAnyEmpty(currentAssembleRecordList)) {
            HashSet<String> materialConfigIds = new HashSet<>();
            HashSet<String> materialStockIds = new HashSet<>();
            currentAssembleRecordList.forEach(item -> {
                materialStockIds.add(item.getMaterialStockId());
            });
            //获取物料库存信息
            CommonResult<List<MaterialStockRespDTO>> materialResult = materialStockApi.getMaterialsByIds(materialStockIds);
            Map<String, MaterialStockRespDTO> materialMap;
            if (materialResult.isSuccess() && materialResult.getData() != null) {
                materialMap = new HashMap<>();
                materialResult.getData().forEach(item -> {
                    materialMap.put(item.getId(), item);
                    materialConfigIds.add(item.getMaterialConfigId());
                });
            } else {
                materialMap = null;
            }
            //获取物料类型信息
            Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);
            List<AssembleRecordVO> assembleRecordVO = AssembleRecordConvert.INSTANCE.toAssembleRecordVO(currentAssembleRecordList, materialMap, materialConfigMap);
            return success(assembleRecordVO);
        }
        return success(null);
	}


    @PostMapping("/assemble-record/tool-disassembly")
    @Operation(summary = "刀具分解")
    @PreAuthorize("@ss.hasPermission('tms:tool-info:delete')")
    public CommonResult<Boolean> toolDisassembly(@RequestBody ToolInfoSaveReqVO saveReqVO) {
        // 卸刀
        IAssembleRecordStrategy strategy = assembleRecordFactory.generatorStrategy(2);
        strategy.saveRecord(saveReqVO);
        return success(true);
    }

}
