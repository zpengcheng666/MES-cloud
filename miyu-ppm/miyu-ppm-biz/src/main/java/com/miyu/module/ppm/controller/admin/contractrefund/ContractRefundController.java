package com.miyu.module.ppm.controller.admin.contractrefund;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.convert.contractrefund.ContractRefundConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.enums.contractrefund.ContractRefundEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.service.shippingreturn.ShippingReturnService;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.contractrefund.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import com.miyu.module.ppm.service.contractrefund.ContractRefundService;

@Tag(name = "管理后台 - 合同退款")
@RestController
@RequestMapping("/ppm/contract-refund")
@Validated
public class ContractRefundController {

    @Resource
    private ContractRefundService contractRefundService;
    @Resource
    private ContractApi contractApi;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private ShippingReturnService shippingReturnService;

    @PostMapping("/create")
    @Operation(summary = "创建合同退款")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:create')")
    public CommonResult<String> createContractRefund(@Valid @RequestBody ContractRefundSaveReqVO createReqVO) {
        return success(contractRefundService.createContractRefund(createReqVO));
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建合同退款并提交审核")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:create')")
    public CommonResult<String> createContractRefundAndSubmit(@Valid @RequestBody ContractRefundSaveReqVO createReqVO) {
        return success(contractRefundService.createContractRefundAndSubmit(createReqVO));
    }


    @PutMapping("/update")
    @Operation(summary = "更新合同退款")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:update')")
    public CommonResult<Boolean> updateContractRefund(@Valid @RequestBody ContractRefundSaveReqVO updateReqVO) {
        contractRefundService.updateContractRefund(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新合同退款并提交审核")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:update')")
    public CommonResult<Boolean> updateContractRefundAndSubmit(@Valid @RequestBody ContractRefundSaveReqVO updateReqVO) {
        contractRefundService.updateContractRefundAndSubmit(updateReqVO);
        return success(true);
    }


    @PutMapping("/submit")
    @Operation(summary = "提交退款审批")
    @PreAuthorize("@ss.hasPermission('crm:contract:update')")
    public CommonResult<Boolean> submitContractRefund(@RequestParam("id") String id) {
        contractRefundService.submitContractRefund(id, getLoginUserId());
        return success(true);
    }


    @PutMapping("/confirm")
    @Operation(summary = "付款确认")
    @PreAuthorize("@ss.hasPermission('dm:shipping:update')")
    public CommonResult<Boolean> confirmRefund(@RequestParam("id") String id) {

        contractRefundService.updateContractRefundStatus(id, ContractRefundEnum.FINISH.getStatus());
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除合同退款")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:delete')")
    public CommonResult<Boolean> deleteContractRefund(@RequestParam("id") String id) {
        contractRefundService.deleteContractRefund(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得合同退款")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:query')")
    public CommonResult<ContractRefundRespVO> getContractRefund(@RequestParam("id") String id) {
        ContractRefundDO contractRefund = contractRefundService.getContractRefund(id);


        Map<String, ContractRespDTO> contractMap = contractApi.getContractMap(Lists.newArrayList(contractRefund.getContractId()));
        ContractRefundRespVO contractRefundRespVO = BeanUtils.toBean(contractRefund, ContractRefundRespVO.class);
        contractRefundRespVO.setContractNo(contractMap.get(contractRefund.getContractId()).getNumber());
        contractRefundRespVO.setContractName(contractMap.get(contractRefund.getContractId()).getName());
        return success(contractRefundRespVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得合同退款分页")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:query')")
    public CommonResult<PageResult<ContractRefundRespVO>> getContractRefundPage(@Valid ContractRefundPageReqVO pageReqVO) {
        PageResult<ContractRefundDO> pageResult = contractRefundService.getContractRefundPage(pageReqVO);


        List<String> contractIdList = convertList(pageResult.getList(), ContractRefundDO::getContractId);
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());

        // 拼接数据 用户信息

        //合同信息
        List<ContractRespDTO> contractRespDTOS = contractApi.getContractList(contractIdList).getCheckedData();
        Map<String, ContractRespDTO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractRespDTO::getId);
        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractRespDTO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyRespDTOMap = companyApi.getCompanyMap(companyIds);

        List<String> returnIds = convertList(pageResult.getList(),ContractRefundDO ::getShippingReturnId);
        List<String> returnIdList = returnIds.stream().distinct().collect(Collectors.toList());
        List<ConsignmentDO> shippingReturnDO = shippingReturnService.getShippingReturnByIds(returnIdList);

        Map<String, ConsignmentDO> shippingReturnMap = CollectionUtils.convertMap(shippingReturnDO, ConsignmentDO::getId);



        return success(new PageResult<>(ContractRefundConvert.INSTANCE.convertList(pageResult.getList(), contractMap, companyRespDTOMap,shippingReturnMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出合同退款 Excel")
    @PreAuthorize("@ss.hasPermission('dm:contract-refund:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractRefundExcel(@Valid ContractRefundPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractRefundDO> list = contractRefundService.getContractRefundPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "合同退款.xls", "数据", ContractRefundRespVO.class,
                BeanUtils.toBean(list, ContractRefundRespVO.class));
    }

}