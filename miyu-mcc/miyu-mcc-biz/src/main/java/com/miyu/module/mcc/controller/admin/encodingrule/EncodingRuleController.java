package com.miyu.module.mcc.controller.admin.encodingrule;

import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApiImpl;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.controller.admin.coderecord.vo.CodeRecordSaveReqVO;
import com.miyu.module.mcc.strategy.EncodingRuleFactory;
import com.miyu.module.mcc.strategy.IEncodingRuleStrategy;
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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.mcc.controller.admin.encodingrule.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import com.miyu.module.mcc.service.encodingrule.EncodingRuleService;

@Tag(name = "管理后台 - 编码规则配置")
@RestController
@RequestMapping("/mcc/encoding-rule")
@Validated
public class EncodingRuleController {

    @Resource
    private EncodingRuleService encodingRuleService;

    @Resource
    private EncodingRuleFactory encodingRuleFactory;


    @PostMapping("/create")
    @Operation(summary = "创建编码规则配置")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:create')")
    public CommonResult<String> createEncodingRule(@Valid @RequestBody EncodingRuleSaveReqVO createReqVO) {
        return success(encodingRuleService.createEncodingRule(createReqVO));
    }



    @PutMapping("/update")
    @Operation(summary = "更新编码规则配置")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:update')")
    public CommonResult<Boolean> updateEncodingRule(@Valid @RequestBody EncodingRuleSaveReqVO updateReqVO) {
        encodingRuleService.updateEncodingRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码规则配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:delete')")
    public CommonResult<Boolean> deleteEncodingRule(@RequestParam("id") String id) {
        encodingRuleService.deleteEncodingRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码规则配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:query')")
    public CommonResult<EncodingRuleRespVO> getEncodingRule(@RequestParam("id") String id) {
        EncodingRuleDO encodingRule = encodingRuleService.getEncodingRule(id);
        return success(BeanUtils.toBean(encodingRule, EncodingRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得编码规则配置分页")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:query')")
    public CommonResult<PageResult<EncodingRuleRespVO>> getEncodingRulePage(@Valid EncodingRulePageReqVO pageReqVO) {
        PageResult<EncodingRuleDO> pageResult = encodingRuleService.getEncodingRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EncodingRuleRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码规则配置 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEncodingRuleExcel(@Valid EncodingRulePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EncodingRuleDO> list = encodingRuleService.getEncodingRulePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "编码规则配置.xls", "数据", EncodingRuleRespVO.class,
                        BeanUtils.toBean(list, EncodingRuleRespVO.class));
    }

    // ==================== 子表（编码规则配置详情） ====================

    @GetMapping("/encoding-rule-detail/list-by-encoding-rule-id")
    @Operation(summary = "获得编码规则配置详情列表")
    @Parameter(name = "encodingRuleId", description = "编码规则表ID")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:query')")
    public CommonResult<List<EncodingRuleDetailDO>> getEncodingRuleDetailListByEncodingRuleId(@RequestParam("encodingRuleId") String encodingRuleId) {
        return success(encodingRuleService.getEncodingRuleDetailListByEncodingRuleId(encodingRuleId));
    }



    @GetMapping("/getRuleDetail")
    @Operation(summary = "获得编码规则配置详情列表")
    @Parameter(name = "encodingRuleId", description = "编码规则表ID")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:query')")
    public CommonResult<Map<String,List<Map<String,String>>>> getRuleDetail(@RequestParam("encodingRuleId") String encodingRuleId) {
        Map<String,List<Map<String,String>>> map = new HashMap<>();
        List<EncodingRuleDetailDO> detailDOS = encodingRuleService.getEncodingRuleDetailListByEncodingRuleId(encodingRuleId);
        EncodingRuleDO encodingRuleDO = encodingRuleService.getEncodingRule(encodingRuleId);
        List<Map<String,String>> list = new ArrayList<>();
        List<Map<String,String>> list2 = new ArrayList<>();
        String codeName="column";
        Integer number = 0;
        Map<String,String> map2 = new HashMap<>();
        for (EncodingRuleDetailDO detailDO: detailDOS){
            Map<String,String> map1 = new HashMap<>();

            map1.put("columnLabel", detailDO.getName()+"("+detailDO.getBitNumber()+"位)");
            map1.put("columnType", detailDO.getType().toString());
            if (StringUtils.isNotBlank(detailDO.getEncodingAttribute())){
                map1.put("columnName", detailDO.getEncodingAttribute());
            }else {
                map1.put("columnName", codeName+"_"+number);
                number++;
            }

            if (StringUtils.isNotBlank(detailDO.getDefalutValue())){
                map2.put(map1.get("columnName"),detailDO.getDefalutValue());
            }else {
                if (StringUtils.isBlank(detailDO.getEncodingAttribute())){
                    IEncodingRuleStrategy strategy = encodingRuleFactory.generatorStrategy(detailDO.getType());
                    String value = strategy.getRuleValue(detailDO,null,detailDOS,encodingRuleDO);
                    map2.put(map1.get("columnName"),value);
                }else {
                    map2.put(map1.get("columnName"),null);
                }

            }
            list.add(map1);
        }
        list2.add(map2);
        map.put("headTableAtts",list);
        map.put("dataList",list2);
        return success(map);
    }



    @GetMapping("/list")
    @Operation(summary = "获得编码规则配置列表")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:query')")
    public CommonResult<List<EncodingRuleRespVO>> getEncodingRuleList(@Valid EncodingRulePageReqVO pageReqVO) {
        List<EncodingRuleDO> result = encodingRuleService.getEncodingRuleList();
        return success(BeanUtils.toBean(result, EncodingRuleRespVO.class));
    }





    @PutMapping("/createCode")
    @Operation(summary = "创建编码")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:create')")
    public CommonResult<String> createCode(@Valid @RequestBody GeneratorCodeReqDTO createReqVO) throws InterruptedException {


//        for (int i =0;i<10;i++){
//            EncodingRuleDO ruleDO = encodingRuleService.generatorCode1(createReqVO);
//           // encodingRuleService.saveCode(createReqVO,ruleDO,null,1);
//        }
        EncodingRuleDO ruleDO = encodingRuleService.generatorCode1(createReqVO);
        //encodingRuleService.saveCode(createReqVO,ruleDO,null,1);
        return success(ruleDO.getGeneratorCode());
    }



    @PutMapping("/updateCode")
    @Operation(summary = "更新编码规则配置")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule:update')")
    public CommonResult<Boolean> updateEncodingStatus(@Valid @RequestBody CodeRecordStatusReqVO updateReqVO) {
        encodingRuleService.updateEncodingStatus(updateReqVO);
        return success(true);
    }
}