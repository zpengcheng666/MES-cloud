package com.miyu.module.qms.controller.admin.samplingruleconfig;

import com.miyu.module.qms.controller.admin.samplingrule.vo.SamplingRuleInfoRespVO;
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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.qms.controller.admin.samplingruleconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingruleconfig.SamplingRuleConfigDO;
import com.miyu.module.qms.service.samplingruleconfig.SamplingRuleConfigService;

@Tag(name = "管理后台 - 抽样规则（检验抽样方案）")
@RestController
@RequestMapping("/qms/sampling-rule-config")
@Validated
public class SamplingRuleConfigController {

    @Resource
    private SamplingRuleConfigService samplingRuleConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建抽样规则（检验抽样方案）")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:create')")
    public CommonResult<String> createSamplingRuleConfig(@Valid @RequestBody SamplingRuleConfigSaveBatchReqVO createReqVO) {
        return success(samplingRuleConfigService.createSamplingRuleConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新抽样规则（检验抽样方案）")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:update')")
    public CommonResult<Boolean> updateSamplingRuleConfig(@Valid @RequestBody SamplingRuleConfigSaveReqVO updateReqVO) {
        samplingRuleConfigService.updateSamplingRuleConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除抽样规则（检验抽样方案）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:delete')")
    public CommonResult<Boolean> deleteSamplingRuleConfig(@RequestParam("id") String id) {
        samplingRuleConfigService.deleteSamplingRuleConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得抽样规则（检验抽样方案）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:query')")
    public CommonResult<SamplingRuleConfigRespVO> getSamplingRuleConfig(@RequestParam("id") String id) {
        SamplingRuleConfigDO samplingRuleConfig = samplingRuleConfigService.getSamplingRuleConfig(id);
        return success(BeanUtils.toBean(samplingRuleConfig, SamplingRuleConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得抽样规则（检验抽样方案）分页")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:query')")
    public CommonResult<PageResult<SamplingRuleConfigRespVO>> getSamplingRuleConfigPage(@Valid SamplingRuleConfigPageReqVO pageReqVO) {
        PageResult<SamplingRuleConfigDO> pageResult = samplingRuleConfigService.getSamplingRuleConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SamplingRuleConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出抽样规则（检验抽样方案） Excel")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSamplingRuleConfigExcel(@Valid SamplingRuleConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SamplingRuleConfigDO> list = samplingRuleConfigService.getSamplingRuleConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "抽样规则（检验抽样方案）.xls", "数据", SamplingRuleConfigRespVO.class,
                        BeanUtils.toBean(list, SamplingRuleConfigRespVO.class));
    }



    @PostMapping("/createTest")
    @Operation(summary = "创建抽样规则（检验抽样方案）")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:create')")
    public CommonResult<String> createTest(@Valid @RequestBody SamplingRuleConfigSaveBatchReqVO createReqVO) {


        List<SamplingRuleConfigDO>  list = samplingRuleConfigService.getSamplingRuleConfigList(null,createReqVO.getSamplingStandardId(),1);
//        List<SamplingAQLReqVo> newList = new ArrayList<>();
//        for (SamplingRuleConfigDO samplingRuleConfigDO : list){
//            SamplingAQLReqVo vo = BeanUtils.toBean(samplingRuleConfigDO,SamplingAQLReqVo.class);
//            newList.add(vo);
//        }
//        createReqVO.setAqlReqVoList(new ArrayList<>());
//        createReqVO.setAqlReqVoList(newList);
        List<SamplingRuleConfigDO> dos = new ArrayList<>();

        for (SamplingRuleConfigDO samplingRuleConfigDO : list){
            SamplingRuleConfigDO vo = BeanUtils.toBean(samplingRuleConfigDO,SamplingRuleConfigDO.class);
            vo.setId(null);
            vo.setSamplingRuleType(createReqVO.getSamplingRuleType());
            dos.add(vo);
        }

        return success(samplingRuleConfigService.createSamplingRuleConfigs(dos));
    }




    @GetMapping("/getInfo")
    @Operation(summary = "获得抽样规则")
    @Parameter(name = "samplingStandardId", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:query')")
    public CommonResult<Map<String,Object>> getSamplingRuleConfigInfo(@RequestParam("samplingStandardId") String samplingStandardId) {
        return success(samplingRuleConfigService.getSamplingRuleConfigInfo(samplingStandardId));
    }

}