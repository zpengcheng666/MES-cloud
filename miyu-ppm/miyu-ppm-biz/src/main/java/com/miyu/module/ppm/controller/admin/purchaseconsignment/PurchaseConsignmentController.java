package com.miyu.module.ppm.controller.admin.purchaseconsignment;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractOrderRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.convert.purchaseConsignment.PurchaseConsignmentConvert;
import com.miyu.module.ppm.convert.purchaseConsignmentDetail.PurchaseConsignmentDetailConvert;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.contract.ContractOrderService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.contractconsignment.ContractConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
import com.miyu.module.ppm.service.warehousedetail.WarehouseDetailService;
import com.miyu.module.qms.api.dto.UnqualifiedMaterialRespDTO;
import com.miyu.module.qms.api.unqualifiedmaterial.UnqualifiedMaterialApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.math.BigDecimal;
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

import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 采购收货")
@RestController
@RequestMapping("/ppm/purchase-consignment")
@Validated
public class PurchaseConsignmentController {
    private static Logger logger = LoggerFactory.getLogger(PurchaseConsignmentController.class);

    @Resource
    WarehouseDetailService warehouseDetailService;

    @Resource
    ContractOrderService contractOrderService;

    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private ContractService contractService;

    @Resource
    private CompanyApi companyApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;
    @Resource
    private CompanyService companyService;
    @Resource
    private CompanyProductService companyProductService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private UnqualifiedMaterialApi unqualifiedMaterialApi;


    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建采购收货并提交审批")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:create')")
    public CommonResult<String> createPurchaseConsignmentAndSubmit(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(createReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getStatus() : ConsignmentTypeEnum.PURCHASE.getStatus());
        createReqVO.setName(createReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getName() : ConsignmentTypeEnum.PURCHASE.getName());

        return success(purchaseConsignmentService.createPurchaseConsignmentAndSubmit(getLoginUserId(), createReqVO));
    }

    @PostMapping("/create")
    @Operation(summary = "创建采购收货")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:create')")
    public CommonResult<String> createPurchaseConsignment(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(createReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getStatus() : ConsignmentTypeEnum.PURCHASE.getStatus());
        createReqVO.setName(createReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getName() : ConsignmentTypeEnum.PURCHASE.getName());
        return success(purchaseConsignmentService.createPurchaseConsignment(getLoginUserId(), createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购收货")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:update')")
    public CommonResult<Boolean> updatePurchaseConsignment(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(updateReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getStatus() : ConsignmentTypeEnum.PURCHASE.getStatus());
        updateReqVO.setName(updateReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getName() : ConsignmentTypeEnum.PURCHASE.getName());

        purchaseConsignmentService.updatePurchaseConsignment(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新采购信息并提交审核")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:update')")
    public CommonResult<Boolean> updatePurchaseConsignmentSubmit(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(updateReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getStatus() : ConsignmentTypeEnum.PURCHASE.getStatus());
        updateReqVO.setName(updateReqVO.getContractType().intValue() == 2 ? ConsignmentTypeEnum.OUT.getName() : ConsignmentTypeEnum.PURCHASE.getName());
        purchaseConsignmentService.updatePurchaseConsignmentSubmit(updateReqVO);
        return success(true);
    }


    @PutMapping("/submit")
    @Operation(summary = "提交采购审批")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:update')")
    public CommonResult<Boolean> submitPurchaseConsignment(@RequestParam("id") String id) {
        purchaseConsignmentService.submitPurchaseConsignment(id, getLoginUserId());
        return success(true);
    }

//    @PutMapping("/confirm")
//    @Operation(summary = "确认收货")
//    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:update')")
//    public CommonResult<Boolean> confirmPurchaseConsignment(@RequestParam("id") String id) {
//
//        purchaseConsignmentService.updateConsignmentStatus(id, ConsignmentStatusEnum.INBOUND.getStatus());
//        return success(true);
//    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采购收货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:delete')")
    public CommonResult<Boolean> deletePurchaseConsignment(@RequestParam("id") String id) {
        purchaseConsignmentService.deletePurchaseConsignment(id);
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "作废采购收货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:delete')")
    public CommonResult<Boolean> cancelPurchaseConsignment(@RequestParam("id") String id) {
        purchaseConsignmentService.cancelPurchaseConsignment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购收货")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<PurchaseConsignmentRespVO> getPurchaseConsignment(@RequestParam("id") String id) {
        ConsignmentDO purchaseConsignment = purchaseConsignmentService.getPurchaseConsignment(id);
        PurchaseConsignmentRespVO respVO = BeanUtils.toBean(purchaseConsignment, PurchaseConsignmentRespVO.class);
//        Map<String, PmsApprovalDto> result = pmsApi.getApprovalMap(Lists.newArrayList(purchaseConsignment.getProjectId()));
//        PmsApprovalDto dto = result.get(purchaseConsignment.getProjectId());
//        respVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");

        List<ContractDO> contractDOS = contractService.getContractListByIds(Lists.newArrayList(purchaseConsignment.getContractId()));
        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        respVO.setContractName(contractDOMap.get(purchaseConsignment.getContractId()).getName());
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得采购收货分页")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<PageResult<PurchaseConsignmentRespVO>> getPurchaseConsignmentPage(@Valid PurchaseConsignmentPageReqVO pageReqVO) {
        pageReqVO.setConsignmentTypes(Lists.newArrayList(1, 2));
        PageResult<ConsignmentDO> pageResult = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO);

        if (pageResult.getList().size() > 0) {
            //用户
            List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), ConsignmentDO::getConsignedBy));
            //合同
            List<String> contractIdList = convertList(pageResult.getList(), ConsignmentDO::getContractId);

            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());

            // 拼接数据 用户信息
            Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);
            //合同信息
            List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
            Map<String, ContractDO> contractRespDTOMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);
            //合同供应商信息
            List<String> companyIds = convertList(contractRespDTOS, ContractDO::getParty);
            companyIds = companyIds.stream().distinct().collect(Collectors.toList());
            Map<String, CompanyRespDTO> companyRespDTOMap = companyApi.getCompanyMap(companyIds);


            return success(new PageResult<>(PurchaseConsignmentConvert.INSTANCE.convertList(pageResult.getList(), userMap, contractRespDTOMap, companyRespDTOMap),
                    pageResult.getTotal()));
        } else {
            return success(new PageResult<>(PurchaseConsignmentConvert.INSTANCE.convertList(pageResult.getList(), null, null, null),
                    pageResult.getTotal()
            ));
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购收货 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPurchaseConsignmentExcel(@Valid PurchaseConsignmentPageReqVO pageReqVO,
                                               HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setConsignmentTypes(Lists.newArrayList(1, 2));
        List<ConsignmentDO> list = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采购收货.xls", "数据", PurchaseConsignmentRespVO.class,
                BeanUtils.toBean(list, PurchaseConsignmentRespVO.class));
    }

    // ==================== 子表（收货明细） ====================

    @GetMapping("/purchase-consignment-detail/list-by-consignment-id")
    @Operation(summary = "获得收货明细列表")
    @Parameter(name = "consignmentId", description = "收货单ID")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getPurchaseConsignmentDetailListByConsignmentId(@RequestParam("consignmentId") String consignmentId) {
        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(consignmentId);
        List<ConsignmentDetailDO> consignmentDetailDO = purchaseConsignmentService.getPurchaseConsignmentDetailListByConsignmentId(consignmentId);
        List<ConsignmentInfoDO> consignmentInfoDO = purchaseConsignmentService.getPurchaseConsignmentInfoListByConsignmentId(consignmentId);

        List<Long> userIdList = new ArrayList<>();
        //签收人
        List<Long> singedIds = StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getSignedBy));
        //创建人
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getCreator));
        //更新人
        List<Long> updaterIds = StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getUpdater));

        //合并用户集合
        //if (outboundIds != null) userIdList.addAll(outboundIds);
        if (creatorIds != null) userIdList.addAll(creatorIds);
        if (updaterIds != null) userIdList.addAll(updaterIds);
        if (singedIds != null) userIdList.addAll(singedIds);

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());

        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);


        //查询合同信息
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(consignmentDO.getContractId());

        Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(list, ContractOrderDO::getId);

        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = purchaseConsignmentService.getOrderMap(consignmentDO.getContractId(), null, Lists.newArrayList(0, 1, 2, 3, 4, 5));

        //获取订单下每个产品的ID
        List<String> materialIds = convertList(list, ContractOrderDO::getMaterialId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);

        if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.PURCHASE.getStatus())) {
            return success(PurchaseConsignmentDetailConvert.INSTANCE.convertListInfo(consignmentInfoDO, userMap, orderMap, map, numberMap));

        } else {
            return success(PurchaseConsignmentDetailConvert.INSTANCE.convertList(consignmentDetailDO, userMap, orderMap, map, numberMap));

        }

    }


    @GetMapping("/purchase-consignment-detail/list-by-project-id")
    @Operation(summary = "获得收货明细列表")
    @Parameter(name = "consignmentId", description = "收货单ID")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getPurchaseConsignmentDetailListByProjectId(@RequestParam("projectId") String projectId,
                                                                                                           @RequestParam("contractId") String contractId,
                                                                                                           @RequestParam("consignmentType") Integer consignmentType) {
        if (contractId.equals("0")) {
            contractId = null;
        }
        //获取所有的收货单
        List<ConsignmentDetailDO> detailDOS = purchaseConsignmentDetailService.getDetailListByProjectId(projectId, contractId, consignmentType);
        if (org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            return success(new ArrayList<>());
        }

        List<String> detailIds = detailDOS.stream().map(ConsignmentDetailDO::getId).collect(Collectors.toList());
        //找到收货单对应的退货单
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailService.getDetailByConsignmentDetailIds(detailIds);

        List<String> useDetailIds = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(shippingDetailDOS)) {
            useDetailIds.addAll(shippingDetailDOS.stream().map(ShippingDetailDO::getConsignmentDetailId).collect(Collectors.toList()));
        }
        //过滤掉退货单
        List<ConsignmentDetailDO> consignmentDetailDOS = detailDOS.stream().filter(consignmentDetailDO -> !useDetailIds.contains(consignmentDetailDO.getId())).collect(Collectors.toList());

        if (org.springframework.util.CollectionUtils.isEmpty(consignmentDetailDOS)) {
            return success(new ArrayList<>());
        }
        Map<String, ConsignmentDetailDO> consignmentDetailDOMap = CollectionUtils.convertMap(consignmentDetailDOS, ConsignmentDetailDO::getBarCode);

        List<String> barcodes = consignmentDetailDOS.stream().map(ConsignmentDetailDO::getBarCode).collect(Collectors.toList());

        CommonResult<List<MaterialStockRespDTO>> commonResult = materialStockApi.getMaterialsByBarCodes(barcodes);

        List<String> materialIds = convertList(consignmentDetailDOS, ConsignmentDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        CommonResult<List<UnqualifiedMaterialRespDTO>> unqualifiedMaterialListByBarCodes = null;
        try {
            unqualifiedMaterialListByBarCodes  = unqualifiedMaterialApi.getUnqualifiedMaterialListByBarCodes(barcodes);
        } catch (Exception e) {
            logger.error("调用质检失败失败---" + e.getMessage());
        }
        Map<String, UnqualifiedMaterialRespDTO> respDTOMap = new HashMap<>();
        if (unqualifiedMaterialListByBarCodes != null){
            respDTOMap = CollectionUtils.convertMap(unqualifiedMaterialListByBarCodes.getCheckedData(), UnqualifiedMaterialRespDTO::getBarCode);
        }


        return success(PurchaseConsignmentDetailConvert.INSTANCE.convertList(consignmentDetailDOMap, map, commonResult.getCheckedData(),respDTOMap));


    }


    @GetMapping("/getOrder")
    @Operation(summary = "根据合同获取订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<ContractOrderRespVO>> getOrderByContract(@RequestParam("id") String id) {

        //获取合同
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(id);

        //查询合同下  所有收货单
        List<ConsignmentDetailDO> detailDOS = purchaseConsignmentDetailService.getInboundOderByContractId(id, Lists.newArrayList(0, 1, 2, 3, 4, 5));

//        if (org.springframework.util.CollectionUtils.isEmpty(detailDOS)){
//            return success(new ArrayList<>());
//        }
        //计算每个订单已经收货的数量
        Map<String, BigDecimal> numberMap = new HashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            detailDOS.forEach(purchaseConsignmentDetailDO -> {

                if (numberMap.get(purchaseConsignmentDetailDO.getOrderId()) == null) {
                    numberMap.put(purchaseConsignmentDetailDO.getOrderId(), purchaseConsignmentDetailDO.getConsignedAmount());
                } else {
                    numberMap.get(purchaseConsignmentDetailDO.getOrderId()).add(purchaseConsignmentDetailDO.getConsignedAmount());
                }
            });
        }

        List<String> materialIds = convertList(list, ContractOrderDO::getMaterialId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        return success(PurchaseConsignmentConvert.INSTANCE.convertOrderList(list, map, numberMap));
    }

    @GetMapping("/getConsignmentDetail")
    @Operation(summary = "根据合同获取收货单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getConsignmentDetailByContract(@RequestParam("id") String id) {

        List<ConsignmentDetailDO> detailDOS = purchaseConsignmentDetailService.getDetailListByProjectId(null, id, null);

        if (org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            return success(new ArrayList<>());
        }

        List<String> detailIds = detailDOS.stream().map(ConsignmentDetailDO::getId).collect(Collectors.toList());

        List<ShippingDetailDO> shippingDetailDOS = shippingDetailService.getDetailByConsignmentDetailIds(detailIds);

        List<String> useDetailIds = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(shippingDetailDOS)) {
            useDetailIds.addAll(shippingDetailDOS.stream().map(ShippingDetailDO::getConsignmentDetailId).collect(Collectors.toList()));
        }

        List<ConsignmentDetailDO> consignmentDetailDOS = detailDOS.stream().filter(consignmentDetailDO -> !useDetailIds.contains(consignmentDetailDO.getId())).collect(Collectors.toList());


        if (org.springframework.util.CollectionUtils.isEmpty(consignmentDetailDOS)) {
            return success(new ArrayList<>());
        }
        Map<String, ConsignmentDetailDO> consignmentDetailDOMap = CollectionUtils.convertMap(consignmentDetailDOS, ConsignmentDetailDO::getBarCode);

        List<String> barcodes = consignmentDetailDOS.stream().map(ConsignmentDetailDO::getBarCode).collect(Collectors.toList());

        CommonResult<List<MaterialStockRespDTO>> commonResult = null;
        try {
            commonResult = materialStockApi.getMaterialsByBarCodes(barcodes);
        } catch (Exception e) {
            logger.error("调用WMS失败---" + e.getMessage());
        }
        List<String> materialIds = convertList(consignmentDetailDOS, ConsignmentDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);
        Map<String, UnqualifiedMaterialRespDTO> respDTOMap = new HashMap<>();
        if (commonResult != null && commonResult.isSuccess() && commonResult.getCheckedData().size() > 0 ){
            CommonResult<List<UnqualifiedMaterialRespDTO>> unqualifiedMaterialListByBarCodes = null;
            try {
                unqualifiedMaterialListByBarCodes  = unqualifiedMaterialApi.getUnqualifiedMaterialListByBarCodes(barcodes);
            } catch (Exception e) {
                logger.error("调用质检失败失败---" + e.getMessage());
            }

            if (unqualifiedMaterialListByBarCodes != null && unqualifiedMaterialListByBarCodes.isSuccess()){
                respDTOMap = CollectionUtils.convertMap(unqualifiedMaterialListByBarCodes.getCheckedData(), UnqualifiedMaterialRespDTO::getBarCode);
            }

        }

        return success(PurchaseConsignmentDetailConvert.INSTANCE.convertList(consignmentDetailDOMap, map, commonResult != null ? commonResult.getCheckedData() : null,respDTOMap));


    }


    @GetMapping("/purchase-consignment-detail/list-by-consignment-id-qms")
    @Operation(summary = "获取需要质检的收货单")
    @Parameter(name = "consignmentId", description = "收货单ID")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getPurchaseConsignmentDetailListByConsignmentIdForQms(@RequestParam("consignmentId") String consignmentId) {

        List<ConsignmentDetailDO> consignmentDetailDO = purchaseConsignmentService.getPurchaseConsignmentDetailListByConsignmentId(consignmentId);
        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(consignmentId);
        List<Long> userIdList = new ArrayList<>();
        //签收人
        List<Long> singedIds = StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getSignedBy));
        //创建人
        List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getCreator));
        //更新人
        List<Long> updaterIds = StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getUpdater));
        //合并用户集合
        //if (outboundIds != null) userIdList.addAll(outboundIds);
        if (creatorIds != null) userIdList.addAll(creatorIds);
        if (updaterIds != null) userIdList.addAll(updaterIds);
        if (singedIds != null) userIdList.addAll(singedIds);

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());

        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);


        ContractDO contractDO = contractService.getContractById(consignmentDO.getContractId());


        List<CompanyProductDO> productDOList = companyProductService.getProductListByCompanyId(contractDO.getParty());
        Map<String, CompanyProductDO> productDOMap = CollectionUtils.convertMap(productDOList, CompanyProductDO::getMaterialId);

        List<ContractOrderRespDTO> list = contractOrderService.getContractOrderByContractId(consignmentDO.getContractId());

        Map<String, ContractOrderRespDTO> orderMap = CollectionUtils.convertMap(list, ContractOrderRespDTO::getId);


        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = purchaseConsignmentService.getOrderMap(consignmentDO.getContractId(), null, Lists.newArrayList(0, 1, 2, 3, 4, 5));

        //获取订单下每个产品的ID
        List<String> materialIds = convertList(list, ContractOrderRespDTO::getMaterialId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);

        List<PurchaseConsignmentDetailRespVO> result = PurchaseConsignmentDetailConvert.INSTANCE.convertListForQms(consignmentDetailDO, userMap, map, numberMap, productDOMap, orderMap);

        return success(result.stream().filter(vo -> vo.getQualityCheck().intValue() == 2).collect(Collectors.toList()));

    }


    @PutMapping("/updateForQms")
    @Operation(summary = "提交质检单")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:update')")
    public CommonResult<Boolean> purchaseConsignmentQms(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        purchaseConsignmentService.updatePurchaseConsignmentQms(updateReqVO);
        return success(true);
    }


}