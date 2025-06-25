package com.miyu.module.ppm.controller.admin.inboundexceptionhandling;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import com.miyu.module.ppm.convert.inboundexceptionhandling.ExceptionHandlingConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.contract.ContractService;
import lombok.extern.slf4j.Slf4j;
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
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.*;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import com.miyu.module.ppm.service.inboundexceptionhandling.InboundExceptionHandlingService;

@Tag(name = "管理后台 - 入库异常处理")
@RestController
@RequestMapping("/ppm/inbound-exception-handling")
@Validated
@Slf4j
public class InboundExceptionHandlingController {

    @Resource
    private InboundExceptionHandlingService inboundExceptionHandlingService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private ContractService contractService;
    @Resource
    private CompanyService companyService;

    @PostMapping("/create")
    @Operation(summary = "创建入库异常处理")
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:create')")
    public CommonResult<String> createInboundExceptionHandling(@Valid @RequestBody InboundExceptionHandlingSaveReqVO createReqVO) {
        return success(inboundExceptionHandlingService.createInboundExceptionHandling(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新入库异常处理")
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:update')")
    public CommonResult<Boolean> updateInboundExceptionHandling(@Valid @RequestBody InboundExceptionHandlingSaveReqVO updateReqVO) {
        inboundExceptionHandlingService.updateInboundExceptionHandling(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除入库异常处理")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:delete')")
    public CommonResult<Boolean> deleteInboundExceptionHandling(@RequestParam("id") String id) {
        inboundExceptionHandlingService.deleteInboundExceptionHandling(id);
        return success(true);
    }



    @PutMapping("/inbound")
    @Operation(summary = "入库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:delete')")
    public CommonResult<Boolean> inbound(@RequestParam("id") String id) {
        inboundExceptionHandlingService.handleInboundException(id,1);
        return success(true);
    }

    @PutMapping("/returnSheet")
    @Operation(summary = "退货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:delete')")
    public CommonResult<Boolean> returnSheet(@RequestParam("id") String id) {
        inboundExceptionHandlingService.handleInboundException(id,2);
        return success(true);
    }






    @GetMapping("/get")
    @Operation(summary = "获得入库异常处理")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:query')")
    public CommonResult<InboundExceptionHandlingRespVO> getInboundExceptionHandling(@RequestParam("id") String id) {
        InboundExceptionHandlingDO inboundExceptionHandling = inboundExceptionHandlingService.getInboundExceptionHandling(id);
        return success(BeanUtils.toBean(inboundExceptionHandling, InboundExceptionHandlingRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得入库异常处理分页")
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:query')")
    public CommonResult<PageResult<InboundExceptionHandlingRespVO>> getInboundExceptionHandlingPage(@Valid InboundExceptionHandlingPageReqVO pageReqVO) {
        PageResult<InboundExceptionHandlingDO> pageResult = inboundExceptionHandlingService.getInboundExceptionHandlingPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        List<InboundExceptionHandlingDO> dos = pageResult.getList();
        List<String> contractIdList = convertList(pageResult.getList(), InboundExceptionHandlingDO::getContractId);
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());



        List<String> projectIds = pageResult.getList().stream().map(InboundExceptionHandlingDO::getProjectId).distinct().collect(Collectors.toList());
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = null;
        try {
            pmsApprovalDtoMap = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {
           log.error("调用PMS失败");
        }

        // 拼接数据 用户信息

        //合同信息
        List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);

        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractDO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());

        List<CompanyDO> companyList = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyRespDTOMap =CollectionUtils.convertMap(companyList, CompanyDO::getId);



        return success(new PageResult<>(ExceptionHandlingConvert.INSTANCE.convertList(pageResult.getList(), contractMap, companyRespDTOMap,pmsApprovalDtoMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出入库异常处理 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInboundExceptionHandlingExcel(@Valid InboundExceptionHandlingPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InboundExceptionHandlingDO> list = inboundExceptionHandlingService.getInboundExceptionHandlingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "入库异常处理.xls", "数据", InboundExceptionHandlingRespVO.class,
                        BeanUtils.toBean(list, InboundExceptionHandlingRespVO.class));
    }

}