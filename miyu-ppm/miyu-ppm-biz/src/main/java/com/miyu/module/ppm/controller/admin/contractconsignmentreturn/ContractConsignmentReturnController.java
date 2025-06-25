package com.miyu.module.ppm.controller.admin.contractconsignmentreturn;

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
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.convert.contractconsignmentreturn.ContractConsignmentReturnConvert;
import com.miyu.module.ppm.convert.purchaseConsignment.PurchaseConsignmentConvert;
import com.miyu.module.ppm.convert.purchaseConsignmentDetail.PurchaseConsignmentDetailConvert;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shipping.ShippingService;
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


@Tag(name = "管理后台 - 外协退货")
@RestController
@RequestMapping("/ppm/contract-consignment-return")
@Validated
public class ContractConsignmentReturnController {

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
    private ShippingService shippingService;
    @Resource
    private PmsApi pmsApi;
    @PostMapping("/create")
    @Operation(summary = "创建外协退货")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:create')")
    public CommonResult<String> createContractConsignmentReturn(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(ConsignmentTypeEnum.OUT_MATERIAL.getStatus());
        createReqVO.setName(ConsignmentTypeEnum.OUT_MATERIAL.getName());

        return success(purchaseConsignmentService.createPurchaseConsignment(getLoginUserId(),createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新外协退货")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:update')")
    public CommonResult<Boolean> updateContractConsignmentReturn(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(ConsignmentTypeEnum.OUT_MATERIAL.getStatus());
        updateReqVO.setName(ConsignmentTypeEnum.OUT_MATERIAL.getName());
        purchaseConsignmentService.updatePurchaseConsignment(updateReqVO);
        return success(true);
    }



    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建采购收货并提交审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:create')")
    public CommonResult<String> createContractConsignmentReturnAndSubmit(@Valid @RequestBody PurchaseConsignmentSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(ConsignmentTypeEnum.OUT_MATERIAL.getStatus());
        createReqVO.setName(ConsignmentTypeEnum.OUT_MATERIAL.getName());
        return success(purchaseConsignmentService.createPurchaseConsignmentAndSubmit(getLoginUserId(),createReqVO));
    }



    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新采购信息并提交审核")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:update')")
    public CommonResult<Boolean> updateContractConsignmentReturnSubmit(@Valid @RequestBody PurchaseConsignmentSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(ConsignmentTypeEnum.OUT_MATERIAL.getStatus());
        updateReqVO.setName(ConsignmentTypeEnum.OUT_MATERIAL.getName());
        purchaseConsignmentService.updatePurchaseConsignmentSubmit(updateReqVO);
        return success(true);
    }

    @PutMapping("/submit")
    @Operation(summary = "提交外协退货审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:update')")
    public CommonResult<Boolean> submitContractConsignmentReturn(@RequestParam("id") String id) {
        purchaseConsignmentService.submitPurchaseConsignment(id, getLoginUserId());
        return success(true);
    }
    

    @DeleteMapping("/delete")
    @Operation(summary = "删除外协退货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:delete')")
    public CommonResult<Boolean> deleteContractConsignmentReturn(@RequestParam("id") String id) {
        purchaseConsignmentService.deletePurchaseConsignment(id);
        return success(true);
    }


    @PutMapping("/cancel")
    @Operation(summary = "作废外协退货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:delete')")
    public CommonResult<Boolean> cancelContractConsignmentReturn(@RequestParam("id") String id) {
        purchaseConsignmentService.cancelPurchaseConsignment(id);
        return success(true);
    }



    @GetMapping("/get")
    @Operation(summary = "获得外协退货")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:query')")
    public CommonResult<PurchaseConsignmentRespVO> getContractConsignmentReturn(@RequestParam("id") String id) {
        ConsignmentDO purchaseConsignment = purchaseConsignmentService.getPurchaseConsignment(id);
        PurchaseConsignmentRespVO respVO = BeanUtils.toBean(purchaseConsignment, PurchaseConsignmentRespVO.class);
        Map<String, PmsApprovalDto> result = pmsApi.getApprovalMap(Lists.newArrayList(purchaseConsignment.getProjectId()));
        PmsApprovalDto dto = result.get(purchaseConsignment.getProjectId());
        if (dto != null){
            respVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");

        }

        List<ContractDO> contractDOS =  contractService.getContractListByIds(Lists.newArrayList(purchaseConsignment.getContractId()));
        Map<String, ContractDO>  contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
        ContractDO contractDO = contractDOMap.get(purchaseConsignment.getContractId());
        if ( contractDO!= null){

            respVO.setContractName(contractDO.getNumber()+"("+contractDO.getName()+")");
        }

        return success(respVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得外协退货分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:query')")
    public CommonResult<PageResult<PurchaseConsignmentRespVO>> getContractConsignmentReturnPage(@Valid PurchaseConsignmentPageReqVO pageReqVO) {
        pageReqVO.setConsignmentType(ConsignmentTypeEnum.OUT_MATERIAL.getStatus());
        PageResult<ConsignmentDO> pageResult = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO);

        if(pageResult.getList().size()>0) {
            //用户
            List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), ConsignmentDO::getConsignedBy));
            //合同
            List<String> contractIdList = convertList(pageResult.getList(), ConsignmentDO::getContractId);

            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());

            // 拼接数据 用户信息
            Map<Long , AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);
            //合同信息
            List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
            Map<String , ContractDO> contractRespDTOMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);
            //合同供应商信息
            List<String> companyIds = convertList(contractRespDTOS,ContractDO::getParty);
            companyIds = companyIds.stream().distinct().collect(Collectors.toList());
            Map<String, CompanyRespDTO> companyRespDTOMap = companyApi.getCompanyMap(companyIds);


            return success(new PageResult<>(PurchaseConsignmentConvert.INSTANCE.convertList(pageResult.getList(),userMap,contractRespDTOMap,companyRespDTOMap),
                    pageResult.getTotal()));
        }
        else {
            return success(new PageResult<>(PurchaseConsignmentConvert.INSTANCE.convertList(pageResult.getList(),null,null,null),
                    pageResult.getTotal()
            ));
        }
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出外协退货 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractConsignmentReturnExcel(@Valid PurchaseConsignmentPageReqVO pageReqVO,
                                                     HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setConsignmentType(ConsignmentTypeEnum.OUT_MATERIAL.getStatus());
        List<ConsignmentDO> list = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "外协退货.xls", "数据", PurchaseConsignmentRespVO.class,
                BeanUtils.toBean(list, PurchaseConsignmentRespVO.class));
    }

    // ==================== 子表（外协退货单详情） ====================

    @GetMapping("/contract-consignment-return-detail/list-by-consignment-return-id")
    @Operation(summary = "获得外协退货单详情列表")
    @Parameter(name = "consignmentReturnId", description = "退货单ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract-consignment-return:query')")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getContractConsignmentReturnDetailListByConsignmentReturnId(@RequestParam("consignmentReturnId") String consignmentReturnId) {


        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(consignmentReturnId);
        List<ConsignmentDetailDO> consignmentDetailDO = purchaseConsignmentService.getPurchaseConsignmentDetailListByConsignmentId(consignmentReturnId);
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


        List<String> shippingIds = consignmentDetailDO.stream().map(ConsignmentDetailDO::getShippingId).distinct().collect(Collectors.toList());

        List<ShippingDO> shippingDOS = shippingService.getShippings(shippingIds);
        Map<String, ShippingDO> shippingDOMap = CollectionUtils.convertMap(shippingDOS, ShippingDO::getId);



        //查询合同信息
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(consignmentDO.getContractId());

        Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(list, ContractOrderDO::getId);

        //获取订单下每个产品的ID
        List<String> materialIds = convertList(list, ContractOrderDO::getMaterialId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);

        return success(ContractConsignmentReturnConvert.INSTANCE.convertList(consignmentDetailDO, userMap, orderMap, map, shippingDOMap));

    }

}