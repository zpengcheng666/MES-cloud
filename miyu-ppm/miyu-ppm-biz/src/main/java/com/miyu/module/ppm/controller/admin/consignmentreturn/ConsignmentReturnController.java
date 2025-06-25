package com.miyu.module.ppm.controller.admin.consignmentreturn;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.controller.admin.consignmentreturndetail.vo.ConsignmentReturnDetailSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingPageReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingSaveReqVO;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.ShippingDetailRespVO;
import com.miyu.module.ppm.convert.purchaseConsignmentDetail.PurchaseConsignmentDetailConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.convert.shippingdetail.ShippingDetailConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.consignmentreturndetail.ConsignmentReturnDetailService;
import com.miyu.module.ppm.service.contract.ContractOrderService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.contract.ContractServiceImpl;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.warehousedetail.WarehouseDetailService;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import org.springframework.beans.factory.annotation.Autowired;
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

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnService;

@Tag(name = "管理后台 - 采购退货单")
@RestController
@RequestMapping("/ppm/consignment-return")
@Validated
public class ConsignmentReturnController {

    @Resource
    private ContractService contractService;

    @Resource
    ContractOrderService contractOrderService;


    @Resource
    private ContractApi contractApi;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private ConsignmentReturnService consignmentReturnService;

    @Resource
    private ConsignmentReturnDetailService consignmentReturnDetailService;

    @Resource
    private WarehouseDetailService warehouseDetailService;
    @Autowired
    private ContractServiceImpl contractServiceImpl;

    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    @Resource
    private OrderApi orderApi;
    @Resource
    private InspectionSheetApi inspectionSheetApi;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private CompanyService companyService;

    @Resource
    private ShippingService shippingService;
    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建采购退货单")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:create')")
    public CommonResult<String> createConsignmentReturn(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
        createReqVO.setName(ShippingTypeEnum.CONSIGNMENT_RETURN.getName());
        return success(shippingService.createShipping(createReqVO));
    }

    @PostMapping("/createAndAudit")
    @Operation(summary = "创建采购退货单并提交审核")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:create')")
    public CommonResult<String> createConsignmentReturnAndAudit(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
        createReqVO.setName(ShippingTypeEnum.CONSIGNMENT_RETURN.getName());
        return success(shippingService.createShippingAndSubmit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购退货单")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:update')")
    public CommonResult<Boolean> updateConsignmentReturn(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
        updateReqVO.setName(ShippingTypeEnum.CONSIGNMENT_RETURN.getName());
        shippingService.updateShipping(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateAndAudit")
    @Operation(summary = "更新采购退货单并提交审核")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:update')")
    public CommonResult<Boolean> updateConsignmentReturnAndAudit(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
        updateReqVO.setName(ShippingTypeEnum.CONSIGNMENT_RETURN.getName());
        shippingService.updateShippingSubmit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采购退货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:delete')")
    public CommonResult<Boolean> deleteConsignmentReturn(@RequestParam("id") String id) {
        shippingService.deleteShipping(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购退货单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:query')")
    public CommonResult<ShippingRespVO> getConsignmentReturn(@RequestParam("id") String id) {
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
    @Operation(summary = "获得采购退货单分页")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:query')")
    public CommonResult<PageResult<ShippingRespVO>> getConsignmentReturnPage(@Valid ShippingPageReqVO pageReqVO) {
        pageReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
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

        List<CompanyDO> companyList = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyRespDTOMap =CollectionUtils.convertMap(companyList, CompanyDO::getId);



        return success(new PageResult<>(ShippingConvert.INSTANCE.convertList(pageResult.getList(), userMap, contractMap, companyRespDTOMap,pmsApprovalDtoMap),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购退货单 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportConsignmentReturnExcel(@Valid ShippingPageReqVO pageReqVO,
                                             HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
        List<ShippingDO> list = shippingService.getShippingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售发货.xls", "数据", ShippingRespVO.class,
                BeanUtils.toBean(list, ShippingRespVO.class));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交退货审批")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:update')")
    public CommonResult<Boolean> submitConsignmentReturn(@RequestParam("id") String id) {
        shippingService.submitShipping(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "作废采购退货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:delete')")
    public CommonResult<Boolean> cancelConsignmentReturn(@RequestParam("id") String id) {
        shippingService.cancelShipping(id);
        return success(true);
    }

    @PutMapping("/confirmShipping")
    @Operation(summary = "出库确认")
    @Parameter(name = "id", description = "详情ID", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping:update')")
    public CommonResult<Boolean> confirmOut(@RequestParam("id") String id) {
        shippingService.confirmOut(id);
        return success(true);
    }

    // =======================子表（采购退货单详情）======================
    @GetMapping("/consignment-return-detail/list-by-consignment-return-id")
    @Operation(summary = "获得采购退货单详情列表")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:query')")
    public CommonResult<List<ShippingDetailRespVO>> getShippingReturnDetailListByConsignmentReturnId(@RequestParam("consignmentReturnId") String consignmentReturnId) {

        List<ShippingDetailDO> shippingDetailDOList = shippingService.getShippingDetailListByShippingId(consignmentReturnId);
        ShippingDO shippingDO = shippingService.getShipping(consignmentReturnId);
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


        return success(ShippingDetailConvert.INSTANCE.convertList(shippingDetailDOList, userMap, map));    }

    @GetMapping("/list-by-contract")
    @Operation(summary = "获取合同下的退货单")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasAnyPermissions('ppm:consignment-return:query')")
    public CommonResult<List<ConsignmentReturnRespVO>> getReturnByContract(@RequestParam("contractId") String contractId){
        //根据合同获取采购退货单
        List<ShippingDO> consignmentReturnDOS = consignmentReturnService.getConsignmentReturnByContract(contractId, Lists.newArrayList(2,3,4,5),ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());

        if (org.springframework.util.CollectionUtils.isEmpty(consignmentReturnDOS)) {
            return success(null);
        }

        //转换为采购退货单ID集合
        List<String> ids = convertList(consignmentReturnDOS, ShippingDO::getId);

        //查询采购退货单明细信息
        List<ShippingDetailDO> consignmentReturnDetailDOS = shippingService.getShippingDetailListByProjectId(null,contractId,null);

        //退货单金额计算
        return success(consignmentReturnDetailService.queryConsignmentReturnPrice(consignmentReturnDOS,consignmentReturnDetailDOS));

    }




    @GetMapping("/purchases-by-contract")
    @Operation(summary = "获取合同下的收货单明细")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasAnyPermissions('ppm:consignment-return:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getPurchaseByContract(@RequestParam("contractId") String contractId){
        List<ConsignmentDetailDO> detailDOS = purchaseConsignmentDetailService.getDetailListByProjectId(null,contractId,null);

        if (org.springframework.util.CollectionUtils.isEmpty(detailDOS)){
            return success(new ArrayList<>());
        }
        List<String> materialIds = convertList(detailDOS, ConsignmentDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        return success(PurchaseConsignmentDetailConvert.INSTANCE.convertList(detailDOS, map));
    }

}