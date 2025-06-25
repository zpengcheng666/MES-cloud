package com.miyu.module.ppm.controller.admin.shippinginstorage;

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
import com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo.ShippingInstorageDetailRespVO;
import com.miyu.module.ppm.convert.purchaseConsignmentDetail.PurchaseConsignmentDetailConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.convert.shippinginstorage.ShippingInstorageConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
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

import com.miyu.module.ppm.controller.admin.shippinginstorage.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginstorage.ShippingInstorageDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import com.miyu.module.ppm.service.shippinginstorage.ShippingInstorageService;

@Tag(name = "管理后台 - 销售订单入库")
@RestController
@RequestMapping("/ppm/shipping-instorage")
@Validated
@Slf4j
public class ShippingInstorageController {

    @Resource
    private ShippingInstorageService shippingInstorageService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private CompanyService companyService;
    @Resource
    private ContractService contractService;
    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private PmsApi pmsApi;

    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;

    @PostMapping("/create")
    @Operation(summary = "创建销售订单入库")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:create')")
    public CommonResult<String> createShippingInstorage(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(ConsignmentTypeEnum.ORDER.getStatus());
        createReqVO.setName(ConsignmentTypeEnum.ORDER.getName());
        return success(purchaseConsignmentService.createPurchaseConsignment(getLoginUserId(),createReqVO));
    }
    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建销售订单入库")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:create')")
    public CommonResult<String> createShippingInstorageAndSubmit(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(ConsignmentTypeEnum.ORDER.getStatus());
        createReqVO.setName(ConsignmentTypeEnum.ORDER.getName());
        return success(purchaseConsignmentService.createPurchaseConsignmentAndSubmit(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售订单入库")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:update')")
    public CommonResult<Boolean> updateShippingInstorage(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(ConsignmentTypeEnum.ORDER.getStatus());
        purchaseConsignmentService.updatePurchaseConsignment(updateReqVO);
        return success(true);
    }
    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新销售订单入库")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:update')")
    public CommonResult<Boolean> updateShippingInstorageAndSubmit(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(ConsignmentTypeEnum.ORDER.getStatus());
        purchaseConsignmentService.updatePurchaseConsignmentSubmit(updateReqVO);
        return success(true);
    }



    @PutMapping("/submit")
    @Operation(summary = "提交采购审批")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:update')")
    public CommonResult<Boolean> submitShippingInstorage(@RequestParam("id") String id) {
        purchaseConsignmentService.submitPurchaseConsignment(id, getLoginUserId());
        return success(true);
    }
    @PutMapping("/cancel")
    @Operation(summary = "作废销售入库单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:delete')")
    public CommonResult<Boolean> cancelShippingInstorage(@RequestParam("id") String id) {
        purchaseConsignmentService.cancelPurchaseConsignment(id);
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除销售订单入库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:delete')")
    public CommonResult<Boolean> deleteShippingInstorage(@RequestParam("id") String id) {
        purchaseConsignmentService.deletePurchaseConsignment(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售订单入库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:query')")
    public CommonResult<PurchaseConsignmentRespVO> getShippingInstorage(@RequestParam("id") String id) {
        ConsignmentDO purchaseConsignment = purchaseConsignmentService.getPurchaseConsignment(id);
        PurchaseConsignmentRespVO respVO = BeanUtils.toBean(purchaseConsignment, PurchaseConsignmentRespVO.class);
        Map<String, PmsApprovalDto> result = pmsApi.getApprovalMap(Lists.newArrayList(purchaseConsignment.getProjectId()));
        PmsApprovalDto dto = result.get(purchaseConsignment.getProjectId());
        respVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");
        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售订单入库分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:query')")
    public CommonResult<PageResult<PurchaseConsignmentRespVO>> getShippingInstoragePage(@Valid PurchaseConsignmentPageReqVO pageReqVO) {

        pageReqVO.setConsignmentTypes(Lists.newArrayList(ConsignmentTypeEnum.ORDER.getStatus()));
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
        Map<String, PmsApprovalDto> map = null;
        try {
            map = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {
            log.error("调用PMS失败"+e.getMessage());
        }


        return success(new PageResult<>(ShippingInstorageConvert.INSTANCE.convertList(pageResult.getList(), userMap, contractMap, companyDOMap,map),
                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售订单入库 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingInstorageExcel(@Valid PurchaseConsignmentPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setConsignmentTypes(Lists.newArrayList(4));
        PageResult<ConsignmentDO> pageResult = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO);

        List<ConsignmentDO> list = pageResult.getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售订单入库.xls", "数据", PurchaseConsignmentRespVO.class,
                        BeanUtils.toBean(list, PurchaseConsignmentRespVO.class));
    }

    // ==================== 子表（销售订单入库明细） ====================

    @GetMapping("/shipping-instorage-detail/list-by-shipping-storage-id")
    @Operation(summary = "获得销售订单入库明细列表")
    @Parameter(name = "shippingStorageId", description = "收货单ID")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getShippingInstorageDetailListByShippingStorageId(@RequestParam("shippingStorageId") String shippingStorageId) {


        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(shippingStorageId);
        List<ConsignmentDetailDO> consignmentDetailDO = purchaseConsignmentService.getPurchaseConsignmentDetailListByConsignmentId(shippingStorageId);
        List<ConsignmentInfoDO> consignmentInfoDO = purchaseConsignmentService.getPurchaseConsignmentInfoListByConsignmentId(shippingStorageId);

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


        //查询合同信息
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(consignmentDO.getContractId());

        Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(list, ContractOrderDO::getId);

        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = purchaseConsignmentService.getOrderMap(consignmentDO.getContractId(), null,Lists.newArrayList(0,1,2,3,4,5));

        //获取订单下每个产品的ID
        List<String> materialIds = convertList(consignmentInfoDO, ConsignmentInfoDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        return success(PurchaseConsignmentDetailConvert.INSTANCE.convertListInfo(consignmentInfoDO, userMap, orderMap, map, numberMap));

        //return success(PurchaseConsignmentDetailConvert.INSTANCE.convertList(consignmentDetailDO, userMap, orderMap, map, numberMap));


    }

}