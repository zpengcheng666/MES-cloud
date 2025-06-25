package com.miyu.module.ppm.controller.admin.contractconsignment;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.controller.admin.contractconsignmentdetail.vo.ContractConsignmentDetailRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingPageReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingSaveReqVO;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.ShippingDetailRespVO;
import com.miyu.module.ppm.convert.contractconsignment.ContractConsignmentConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.convert.shippingdetail.ShippingDetailConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.utils.StringListUtils;
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
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.contractconsignment.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import com.miyu.module.ppm.service.contractconsignment.ContractConsignmentService;

@Tag(name = "管理后台 - 外协发货")
@RestController
@RequestMapping("/ppm/contract-consignment")
@Validated
public class ContractConsignmentController {

    @Resource
    private ContractConsignmentService contractConsignmentService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractService contractService;
    @Resource
    private CompanyService companyService;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private ShippingService shippingService;
    @Resource
    private PmsApi pmsApi;
    @PostMapping("/create")
    @Operation(summary = "创建外协发货")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:create')")
    public CommonResult<String> createContractConsignment(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.OUTSOURCING.getStatus());
        createReqVO.setName(ShippingTypeEnum.OUTSOURCING.getName());
        return success(shippingService.createShipping(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新外协发货")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:update')")
    public CommonResult<Boolean> updateContractConsignment(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.OUTSOURCING.getStatus());
        updateReqVO.setName(ShippingTypeEnum.OUTSOURCING.getName());
        shippingService.updateShipping(updateReqVO);
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建外协发货并提交审核")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:create')")
    public CommonResult<String> createContractConsignmentAndSubmit(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.OUTSOURCING.getStatus());
        createReqVO.setName(ShippingTypeEnum.OUTSOURCING.getName());
        return success(shippingService.createShippingAndSubmit(createReqVO));
    }

    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新外协发货并提交审核")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:update')")
    public CommonResult<Boolean> updateContractConsignmentAndSubmit(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.OUTSOURCING.getStatus());
        updateReqVO.setName(ShippingTypeEnum.OUTSOURCING.getName());
        shippingService.updateShippingSubmit(updateReqVO);
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除外协发货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:delete')")
    public CommonResult<Boolean> deleteContractConsignment(@RequestParam("id") String id) {
        shippingService.deleteShipping(id);
        return success(true);
    }


    @PutMapping("/confirmShipping")
    @Operation(summary = "出库确认")
    @Parameter(name = "id", description = "详情ID", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:update')")
    public CommonResult<Boolean> confirmOut(@RequestParam("id") String id) {
        shippingService.confirmOut(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得外协发货")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:query')")
    public CommonResult<ShippingRespVO> getContractConsignment(@RequestParam("id") String id) {
        ShippingDO shipping = shippingService.getShipping(id);
        ShippingRespVO shippingRespVO = BeanUtils.toBean(shipping, ShippingRespVO.class);

//        Map<String, PmsApprovalDto> result =   pmsApi.getApprovalMap(Lists.newArrayList(shippingRespVO.getProjectId()));
//        PmsApprovalDto dto = result.get(shippingRespVO.getProjectId());
//        shippingRespVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");

        List<ContractDO> contractDOS =  contractService.getContractListByIds(Lists.newArrayList(shipping.getContractId()));
        Map<String, ContractDO>  contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        shippingRespVO.setContractName(contractDOMap.get(shipping.getContractId()).getName());
        return success(shippingRespVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得外协发货分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:query')")
    public CommonResult<PageResult<ShippingRespVO>> getContractConsignmentPage(@Valid ShippingPageReqVO pageReqVO) {
        pageReqVO.setShippingType(ShippingTypeEnum.OUTSOURCING.getStatus());
        PageResult<ShippingDO> pageResult = shippingService.getShippingPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        //用户
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), ShippingDO::getConsigner));
        //合同

        Map<Long, AdminUserRespDTO> userMap = new HashMap<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(userIdList);
        }
        List<String> contractIdList = convertList(pageResult.getList(), ShippingDO::getContractId);
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());



        List<String> projectIds = pageResult.getList().stream().map(ShippingDO::getProjectId).distinct().collect(Collectors.toList());
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = pmsApi.getApprovalMap(projectIds);

        // 拼接数据 用户信息

        //合同信息
        List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);
        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractDO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());

        List< CompanyDO > companyList = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyRespDTOMap =CollectionUtils.convertMap(companyList, CompanyDO::getId);



        return success(new PageResult<>(ShippingConvert.INSTANCE.convertList(pageResult.getList(), userMap, contractMap, companyRespDTOMap,pmsApprovalDtoMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出外协发货 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractConsignmentExcel(@Valid ShippingPageReqVO pageReqVO,
                                               HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setShippingType(ShippingTypeEnum.OUTSOURCING.getStatus());
        List<ShippingDO> list = shippingService.getShippingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售发货.xls", "数据", ShippingRespVO.class,
                BeanUtils.toBean(list, ShippingRespVO.class));
    }

    // ==================== 子表（外协发货单详情） ====================

    @GetMapping("/contract-consignment-detail/list-by-consignment-id")
    @Operation(summary = "获得外协发货单详情列表")
    @Parameter(name = "consignmentId", description = "发货单ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:query')")
    public CommonResult<List<ShippingDetailRespVO>> getContractConsignmentDetailListByConsignmentId(@RequestParam("consignmentId") String consignmentId) {
        List<ShippingDetailDO> shippingDetailDOList = shippingService.getShippingDetailListByShippingId(consignmentId);
        ShippingDO shippingDO = shippingService.getShipping(consignmentId);
        List<Long> userIdList = new ArrayList<>();
        //主管
        List<Long> outboundIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOList, ShippingDetailDO::getOutboundBy));
//        List<Long> singedIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOList, ShippingDetailDO::getSignedBy));
//        //创建人
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOList, ShippingDetailDO::getCreator));
        //更新人
        List<Long> updaterIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOList, ShippingDetailDO::getUpdater));

        //合并用户集合
        if (outboundIds != null) userIdList.addAll(outboundIds);
        if (creatorIds != null) userIdList.addAll(creatorIds);
        if (updaterIds != null) userIdList.addAll(updaterIds);
//        if (singedIds != null) userIdList.addAll(singedIds);

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());

        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);


        List<String> materialIds = convertList(shippingDetailDOList, ShippingDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        return success(ShippingDetailConvert.INSTANCE.convertList(shippingDetailDOList, userMap, map));
    }

    @PutMapping("/cancel")
    @Operation(summary = "作废销售发货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping:delete')")
    public CommonResult<Boolean> cancelShipping(@RequestParam("id") String id) {
        shippingService.cancelShipping(id);
        return success(true);
    }



//    @PutMapping("/outBound")
//    @Operation(summary = "通知WMS出库")
//    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment:update')")
//    public CommonResult<Boolean> outBoundContractConsignment(@Valid @RequestBody ContractConsignmentSaveReqVO updateReqVO) {
//        contractConsignmentService.outBoundContractConsignment(updateReqVO);
//        return success(true);
//    }


    @PutMapping("/submit")
    @Operation(summary = "提交发货审批")
    @PreAuthorize("@ss.hasPermission('crm:contract:update')")
    public CommonResult<Boolean> submitContractConsignment(@RequestParam("id") String id) {
        shippingService.submitShipping(id, getLoginUserId());
        return success(true);
    }
}