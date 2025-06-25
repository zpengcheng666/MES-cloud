package com.miyu.module.tms.controller.admin.toolconfig;

import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import com.miyu.module.tms.dal.mysql.fitconfig.FitConfigMapper;
import com.miyu.module.tms.dal.mysql.toolconfigparameter.ToolConfigParameterMapper;
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
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.tms.controller.admin.toolconfig.vo.*;
import com.miyu.module.tms.dal.dataobject.toolconfig.ToolConfigDO;
import com.miyu.module.tms.service.toolconfig.ToolConfigService;

@Tag(name = "管理后台 - 刀具类型")
@RestController
@RequestMapping("/tms/tool-config")
@Validated
public class ToolConfigController {

    @Resource
    private ToolConfigService toolConfigService;

    @Resource
    private ToolConfigParameterMapper toolConfigParameterMapper;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private FitConfigMapper fitConfigMapper;

    @PostMapping("/create")
    @Operation(summary = "创建刀具类型")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:create')")
    public CommonResult<String> createToolConfig(@Valid @RequestBody ToolConfigSaveReqVO createReqVO) {
        return success(toolConfigService.createToolConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具类型")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:update')")
    public CommonResult<Boolean> updateToolConfig(@Valid @RequestBody ToolConfigSaveReqVO updateReqVO) {
        toolConfigService.updateToolConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具类型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-config:delete')")
    public CommonResult<Boolean> deleteToolConfig(@RequestParam("id") String id) {
        toolConfigService.deleteToolConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:query')")
    public CommonResult<ToolConfigRespVO> getToolConfig(@RequestParam("id") String id) {
        ToolConfigDO toolConfig = toolConfigService.getToolConfig(id);
        // 物料类型
        List<String> materialConfigIds = new ArrayList<>(Arrays.asList(toolConfig.getMaterialConfigId()));
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);

        // 获取物料类型参数集合
        List<ToolConfigParameterDO> configParameterList = toolConfigParameterMapper.selectList(ToolConfigParameterDO::getToolConfigId, id);

        // 刀具适配集合
        List<FitConfigDO> fitConfigList = fitConfigMapper.selectFitConfigList(id);

        return success(BeanUtils.toBean(toolConfig, ToolConfigRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialConfigName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
            }

            vo.setTemplateParamList(configParameterList);
            vo.setGeoParamList(new ArrayList<>());
            vo.setCutParamList(new ArrayList<>());
            vo.setFitConfigList(fitConfigList);
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具类型分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:query')")
    public CommonResult<PageResult<ToolConfigRespVO>> getToolConfigPage(@Valid ToolConfigPageReqVO pageReqVO) {
        PageResult<ToolConfigDO> pageResult = toolConfigService.getToolConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具类型 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolConfigExcel(@Valid ToolConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolConfigDO> list = toolConfigService.getToolConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具类型.xls", "数据", ToolConfigRespVO.class,
                        BeanUtils.toBean(list, ToolConfigRespVO.class));
    }


    @GetMapping("/getByMaterialConfigId")
    @Operation(summary = "物料类型ID获得刀具类型")
    @Parameter(name = "materialConfigId", description = "物料类型ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:query')")
    public CommonResult<ToolConfigRespVO> getToolConfigByMaterialConfigId(@RequestParam("materialConfigId") String materialConfigId) {
        ToolConfigDO toolConfig = toolConfigService.getToolConfigByMaterialConfigId(materialConfigId);
        return success(BeanUtils.toBean(toolConfig, ToolConfigRespVO.class));
    }

    /**
     * 分页查询刀具
     *
     * @param pageReqVO 分页请求
     * @return 刀具信息分页列表
     */
    @GetMapping("/getFitToolConfigPageByType")
    @Operation(summary = "刀具信息分页列表")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:query')")
    public CommonResult<PageResult<ToolConfigRespVO>> getFitToolConfigPageByType(@Valid ToolConfigPageReqVO pageReqVO) {
        PageResult<ToolConfigDO> pageResult = toolConfigService.getFitToolConfigPageByType(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolConfigRespVO.class));
    }


    @GetMapping("/getToolConfigPageByType")
    @Operation(summary = "刀具信息分页列表")
    @PreAuthorize("@ss.hasPermission('tms:tool-config:query')")
    public CommonResult<PageResult<ToolConfigRespVO>> getToolConfigPageByType(@Valid ToolConfigPageReqVO pageReqVO) {
        PageResult<ToolConfigDO> pageResult = toolConfigService.getToolConfigPageByType(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolConfigRespVO.class));
    }
}
