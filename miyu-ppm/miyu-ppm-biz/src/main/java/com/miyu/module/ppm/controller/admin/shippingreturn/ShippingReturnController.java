package com.miyu.module.ppm.controller.admin.shippingreturn;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.convert.shippingreturn.ShippingReturnConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contractrefund.ContractRefundDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.contractrefund.ContractRefundService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.shippingreturn.vo.*;
import com.miyu.module.ppm.service.shippingreturn.ShippingReturnService;

@Tag(name = "管理后台 - 销售退货单")
@RestController
@RequestMapping("/ppm/shipping-return")
@Validated
public class ShippingReturnController {

    @Resource
    private ShippingReturnService shippingReturnService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private ContractService  contractService;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private CompanyService companyService;
    @Resource
    private ContractRefundService contractRefundService;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;

    @Resource
    private ShippingService shippingService;

    @PostMapping("/create")
    @Operation(summary = "创建销售退货单")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:create')")
    public CommonResult<String> createShippingReturn(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        createReqVO.setName(ConsignmentTypeEnum.SHIPPING_RETURN.getName());
        return success(purchaseConsignmentService.createPurchaseConsignment(getLoginUserId(),createReqVO));
    }

    @PostMapping("/createAndAudit")
    @Operation(summary = "创建销售退货单并提交审核")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:create')")
    public CommonResult<String> createShippingReturnAndAudit(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        createReqVO.setName(ConsignmentTypeEnum.SHIPPING_RETURN.getName());
        return success(purchaseConsignmentService.createPurchaseConsignmentAndSubmit(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售退货单")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:update')")
    public CommonResult<Boolean> updateShippingReturn(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        purchaseConsignmentService.updatePurchaseConsignment(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateAndAudit")
    @Operation(summary = "更新销售退货单并提交审核")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:update')")
    public CommonResult<Boolean> updateShippingReturnAndAudit(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        purchaseConsignmentService.updatePurchaseConsignmentSubmit(updateReqVO);
        return success(true);
    }


    @PutMapping("/submit")
    @Operation(summary = "提交退货审批")
    @PreAuthorize("@ss.hasPermission('crm:contract:update')")
    public CommonResult<Boolean> submitShippingReturn(@RequestParam("id") String id) {
        purchaseConsignmentService.submitPurchaseConsignment(id, getLoginUserId());
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "作废销售退货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:delete')")
    public CommonResult<Boolean> cancelShippingReturn(@RequestParam("id") String id) {
        purchaseConsignmentService.cancelPurchaseConsignment(id);
        return success(true);
    }



    @DeleteMapping("/delete")
    @Operation(summary = "删除销售退货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:delete')")
    public CommonResult<Boolean> deleteShippingReturn(@RequestParam("id") String id) {
        purchaseConsignmentService.deletePurchaseConsignment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售退货单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:query')")
    public CommonResult<PurchaseConsignmentRespVO> getShippingReturn(@RequestParam("id") String id) {
        ConsignmentDO purchaseConsignment = purchaseConsignmentService.getPurchaseConsignment(id);
        PurchaseConsignmentRespVO respVO = BeanUtils.toBean(purchaseConsignment, PurchaseConsignmentRespVO.class);
        Map<String, PmsApprovalDto> result = pmsApi.getApprovalMap(Lists.newArrayList(purchaseConsignment.getProjectId()));
        PmsApprovalDto dto = result.get(purchaseConsignment.getProjectId());
        respVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售退货单分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:query')")
    public CommonResult<PageResult<PurchaseConsignmentRespVO>> getShippingReturnPage(@Valid PurchaseConsignmentPageReqVO pageReqVO) {
        pageReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        PageResult<ConsignmentDO> pageResult = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO);


        if (org.springframework.util.CollectionUtils.isEmpty(pageResult.getList())){
            return success(new PageResult<>());
        }

        //用户
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), ConsignmentDO::getConsignedBy));

        Map<Long, AdminUserRespDTO> userMap = new HashMap<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(userIdList);
        }
        List<String> contractIdList = convertList(pageResult.getList(), ConsignmentDO::getContractId);
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());
        //合同信息
        List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);

        List<String> companyIds = convertList(pageResult.getList(), ConsignmentDO::getCompanyId);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());

        List<CompanyDO> companyDOS= companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyDOMap = CollectionUtils.convertMap(companyDOS, CompanyDO::getId);





        List<String> projectIds = convertList(pageResult.getList(), ConsignmentDO::getProjectId);
        Map<String, PmsApprovalDto> map = pmsApi.getApprovalMap(projectIds);


        return success(new PageResult<>(ShippingReturnConvert.INSTANCE.convertList(pageResult.getList(), userMap, contractMap, companyDOMap,map),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售退货单 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingReturnExcel(@Valid PurchaseConsignmentPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        List<ConsignmentDO> list =  purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售退货单.xls", "数据", PurchaseConsignmentRespVO.class,
                BeanUtils.toBean(list, PurchaseConsignmentRespVO.class));
    }

    // ==================== 子表（销售退货单详情） ====================

    @GetMapping("/shipping-return-detail/list-by-shipping-return-id")
    @Operation(summary = "获得销售退货单详情列表")
    @Parameter(name = "shippingReturnId", description = "退货单ID")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getShippingReturnDetailListByShippingReturnId(@RequestParam("shippingReturnId") String shippingReturnId) {

        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(shippingReturnId);
        List<ConsignmentDetailDO> consignmentDetailDO = purchaseConsignmentService.getPurchaseConsignmentDetailListByConsignmentId(shippingReturnId);
        List<ConsignmentInfoDO> consignmentInfoDO = purchaseConsignmentService.getPurchaseConsignmentInfoListByConsignmentId(shippingReturnId);

        List<Long> userIdList = new ArrayList<>();
        //签收人
        List<Long> singedIds = com.miyu.module.ppm.controller.admin.contract.util.StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getSignedBy));
        //创建人
        List<Long> creatorIds = com.miyu.module.ppm.controller.admin.contract.util.StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getCreator));
        //更新人
        List<Long> updaterIds = com.miyu.module.ppm.controller.admin.contract.util.StringListUtils.stringListToLongList(convertList(consignmentDetailDO, ConsignmentDetailDO::getUpdater));

        //合并用户集合
        //if (outboundIds != null) userIdList.addAll(outboundIds);
        if (creatorIds != null) userIdList.addAll(creatorIds);
        if (updaterIds != null) userIdList.addAll(updaterIds);
        if (singedIds != null) userIdList.addAll(singedIds);

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());

        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);


        List<String> shippingIds = consignmentInfoDO.stream().map(ConsignmentInfoDO::getShippingId).collect(Collectors.toList());

        List<ShippingDO> shippingDOS = shippingService.getShippings(shippingIds);
        Map<String, ShippingDO> shippingDOMap = CollectionUtils.convertMap(shippingDOS, ShippingDO::getId);

        //查询合同信息
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(consignmentDO.getContractId());

        Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(list, ContractOrderDO::getId);

        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = purchaseConsignmentService.getOrderMap(consignmentDO.getContractId(), null,Lists.newArrayList(0,1,2,3,4,5));

        //获取订单下每个产品的ID
        List<String> materialIds = convertList(consignmentInfoDO, ConsignmentInfoDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        //return success(PurchaseConsignmentDetailConvert.INSTANCE.convertListInfo(consignmentInfoDO, userMap, orderMap, map, numberMap));

        return success(ShippingReturnConvert.INSTANCE.convertList(consignmentDetailDO, userMap, orderMap, map, numberMap,shippingDOMap));


    }


    @GetMapping("/list-by-contract")
    @Operation(summary = "获取合同下的退货单")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return:query')")
    public CommonResult<List<ShippingReturnRespVO>> getShippingReturnByContract(@RequestParam("contractId") String contractId, @RequestParam("contractRefundId") String contractRefundId) {

        List<ConsignmentDO> shippingReturnDOS = shippingReturnService.getShippingReturnByContract(contractId, Lists.newArrayList(2, 3, 4));

        if (org.springframework.util.CollectionUtils.isEmpty(shippingReturnDOS)) {
            return success(null);
        }
        List<String> ids = convertList(shippingReturnDOS, ConsignmentDO::getId);

        List<ConsignmentDetailDO> shippingReturnDetailDOS = purchaseConsignmentDetailService.getDetailListByIds(ids);

        //查询合同订单信息
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(contractId);

        Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(list, ContractOrderDO::getId);

        Map<String, BigDecimal> priceMap = new HashMap<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(shippingReturnDetailDOS)) {
            for (ConsignmentDetailDO shippingReturnDetailDO : shippingReturnDetailDOS) {
                if (priceMap.get(shippingReturnDetailDO.getConsignmentId()) == null) {

                    priceMap.put(shippingReturnDetailDO.getConsignmentId(), orderMap.get(shippingReturnDetailDO.getOrderId()).getTaxPrice());
                } else {
                    priceMap.put(shippingReturnDetailDO.getConsignmentId(), priceMap.get(shippingReturnDetailDO.getConsignmentId()).add(orderMap.get(shippingReturnDetailDO.getOrderId()).getTaxPrice()));
                }

            }
        }

        //查询合同下 退款单的金额
        List<ContractRefundDO> contractRefundDOS = contractRefundService.getContractRefundByShippingReturn(ids);

        if (!org.springframework.util.CollectionUtils.isEmpty(contractRefundDOS)) {
            List<ContractRefundDO> dos = contractRefundDOS.stream().filter(contractRefundDO -> (!contractRefundDO.getId().equals(contractRefundId))).collect(Collectors.toList());


            if (!org.springframework.util.CollectionUtils.isEmpty(dos)) {
                for (ContractRefundDO contractRefundDO : dos) {
                    if (priceMap.get(contractRefundDO.getShippingReturnId()) == null) {

                        priceMap.put(contractRefundDO.getShippingReturnId(), contractRefundDO.getRefundPrice());
                    } else {
                        priceMap.put(contractRefundDO.getShippingReturnId(),priceMap.get(contractRefundDO.getShippingReturnId()).subtract(contractRefundDO.getRefundPrice()));
                    }

                }
            }

        }

        return success(ShippingReturnConvert.INSTANCE.convertList(shippingReturnDOS, priceMap));
    }
}