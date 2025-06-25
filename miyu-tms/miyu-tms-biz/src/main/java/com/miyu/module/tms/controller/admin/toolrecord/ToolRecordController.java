package com.miyu.module.tms.controller.admin.toolrecord;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
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

import com.miyu.module.tms.controller.admin.toolrecord.vo.*;
import com.miyu.module.tms.dal.dataobject.toolrecord.ToolRecordDO;
import com.miyu.module.tms.service.toolrecord.ToolRecordService;

@Tag(name = "管理后台 - 刀具使用记录")
@RestController
@RequestMapping("/tms/tool-record")
@Validated
public class ToolRecordController {

    @Resource
    private ToolRecordService toolRecordService;
    @Resource
    private MaterialStockApi materialStockApi;

    @PostMapping("/create")
    @Operation(summary = "创建刀具使用记录")
    @PreAuthorize("@ss.hasPermission('tms:tool-record:create')")
    public CommonResult<String> createToolRecord(@Valid @RequestBody ToolRecordSaveReqVO createReqVO) {
        return success(toolRecordService.createToolRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具使用记录")
    @PreAuthorize("@ss.hasPermission('tms:tool-record:update')")
    public CommonResult<Boolean> updateToolRecord(@Valid @RequestBody ToolRecordSaveReqVO updateReqVO) {
        toolRecordService.updateToolRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具使用记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-record:delete')")
    public CommonResult<Boolean> deleteToolRecord(@RequestParam("id") String id) {
        toolRecordService.deleteToolRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具使用记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-record:query')")
    public CommonResult<ToolRecordRespVO> getToolRecord(@RequestParam("id") String id) {
        ToolRecordDO toolRecord = toolRecordService.getToolRecord(id);
        return success(BeanUtils.toBean(toolRecord, ToolRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具使用记录分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-record:query')")
    public CommonResult<PageResult<ToolRecordRespVO>> getToolRecordPage(@Valid ToolRecordPageReqVO pageReqVO) {
        PageResult<ToolRecordDO> pageResult = toolRecordService.getToolRecordPage(pageReqVO);
        if(pageResult.getTotal() > 0){
            Set<String> materialStockIds = pageResult.getList().stream().map(ToolRecordDO::getToolInfoId).collect(Collectors.toSet());
            //获取物料库存信息
            CommonResult<List<MaterialStockRespDTO>> materialResult = materialStockApi.getMaterialsAndConfigByIds(materialStockIds);

            if(materialResult.isSuccess() && materialResult.getData() != null){
                Map<String, MaterialStockRespDTO> materialMap = CollectionUtils.convertMap(materialResult.getData(), MaterialStockRespDTO::getId);
                for(ToolRecordDO item : pageResult.getList()) {
                    if(materialMap.containsKey(item.getMainStockId())){
                        MaterialStockRespDTO materialStockRespDTO = materialMap.get(item.getMainStockId());
                        item.setBarCode(materialStockRespDTO.getBarCode());
                        item.setMaterialNumber(materialStockRespDTO.getMaterialNumber());
                        item.setMaterialName(materialStockRespDTO.getMaterialName());
                    }
                }
            }
        }

        return success(BeanUtils.toBean(pageResult, ToolRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具使用记录 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolRecordExcel(@Valid ToolRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolRecordDO> list = toolRecordService.getToolRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具使用记录.xls", "数据", ToolRecordRespVO.class,
                        BeanUtils.toBean(list, ToolRecordRespVO.class));
    }

}