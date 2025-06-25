package com.miyu.module.ppm.controller.admin.shipping;

import cn.hutool.core.collection.CollUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.PurchaseConsignmentController;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.ShippingDetailRespVO;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.convert.shippingdetail.ShippingDetailConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
import com.miyu.module.ppm.service.shippingreturndetail.ShippingReturnDetailService;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.ppm.controller.admin.shipping.vo.*;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.service.shipping.ShippingService;

@Tag(name = "管理后台 - 销售发货")
@RestController
@RequestMapping("/ppm/shipping")
@Validated
public class ShippingController {
    private static Logger logger = LoggerFactory.getLogger(ShippingController.class);

    @Resource
    private ShippingService shippingService;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Resource
    private ShippingReturnDetailService shippingReturnDetailService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private CompanyService companyService;

    @Resource
    private ContractService contractService;

    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;



    @PostMapping("/create")
    @Operation(summary = "创建销售发货")
    @PreAuthorize("@ss.hasPermission('dm:shipping:create')")
    public CommonResult<String> createShipping(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.SHIPPING.getStatus());
        createReqVO.setName(ShippingTypeEnum.SHIPPING.getName());
        return success(shippingService.createShipping(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售发货")
    @PreAuthorize("@ss.hasPermission('dm:shipping:update')")
    public CommonResult<Boolean> updateShipping(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.SHIPPING.getStatus());
        updateReqVO.setName(ShippingTypeEnum.SHIPPING.getName());
        shippingService.updateShipping(updateReqVO);
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建销售发货并提交审批")
    @PreAuthorize("@ss.hasPermission('dm:shipping:create')")
    public CommonResult<String> createShippingAndSubmit(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.SHIPPING.getStatus());
        createReqVO.setName(ShippingTypeEnum.SHIPPING.getName());
        return success(shippingService.createShippingAndSubmit(createReqVO));
    }

    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新销售发货并提交审核")
    @PreAuthorize("@ss.hasPermission('dm:shipping:update')")
    public CommonResult<Boolean> updateShippingSubmit(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.SHIPPING.getStatus());
        updateReqVO.setName(ShippingTypeEnum.SHIPPING.getName());
        shippingService.updateShippingSubmit(updateReqVO);
        return success(true);
    }


    @PutMapping("/submit")
    @Operation(summary = "提交发货审批")
    @PreAuthorize("@ss.hasPermission('crm:contract:update')")
    public CommonResult<Boolean> submitShipping(@RequestParam("id") String id) {
        shippingService.submitShipping(id, getLoginUserId());
        return success(true);
    }





    @PutMapping("/outBound")
    @Operation(summary = "通知WMS出库")
    @PreAuthorize("@ss.hasPermission('dm:shipping:update')")
    public CommonResult<Boolean> outBoundShipping(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        shippingService.outBoundShipping(updateReqVO);
        return success(true);
    }



    @PutMapping("/confirm")
    @Operation(summary = "收货确认")
    @PreAuthorize("@ss.hasPermission('dm:shipping:update')")
    public CommonResult<Boolean> confirmShipping(@RequestParam("id") String id) {

        shippingService.updateShippingStatus(id, ShippingStatusEnum.SEND.getStatus());
        return success(true);
    }


    @DeleteMapping("/delete")
    @Operation(summary = "删除销售发货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping:delete')")
    public CommonResult<Boolean> deleteShipping(@RequestParam("id") String id) {
        shippingService.deleteShipping(id);
        return success(true);
    }

    @PutMapping("/cancel")
    @Operation(summary = "作废销售发货单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping:delete')")
    public CommonResult<Boolean> cancelShipping(@RequestParam("id") String id) {
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

    @GetMapping("/get")
    @Operation(summary = "获得销售发货")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<ShippingRespVO> getShipping(@RequestParam("id") String id) {
        ShippingDO shipping = shippingService.getShipping(id);
        ShippingRespVO shippingRespVO = BeanUtils.toBean(shipping, ShippingRespVO.class);

        Map<String, PmsApprovalDto> result =   pmsApi.getApprovalMap(Lists.newArrayList(shippingRespVO.getProjectId()));
        PmsApprovalDto dto = result.get(shippingRespVO.getProjectId());
        shippingRespVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");

//        List<ContractDO> contractDOS =  contractService.getContractListByIds(Lists.newArrayList(shipping.getContractId()));
//        Map<String, ContractDO>  contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);
//        shippingRespVO.setContractName(contractDOMap.get(shipping.getContractId()).getName());
        return success(shippingRespVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售发货分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<PageResult<ShippingRespVO>> getShippingPage(@Valid ShippingPageReqVO pageReqVO) {

        pageReqVO.setShippingType(ShippingTypeEnum.SHIPPING.getStatus());
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
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = null;
        try {
            pmsApprovalDtoMap = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {
            logger.error("调用PMS系统失败"+e.getMessage());
        }

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
    @Operation(summary = "导出销售发货 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingExcel(@Valid ShippingPageReqVO pageReqVO,
                                    HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        pageReqVO.setShippingType(ShippingTypeEnum.SHIPPING.getStatus());
        List<ShippingDO> list = shippingService.getShippingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售发货.xls", "数据", ShippingRespVO.class,
                BeanUtils.toBean(list, ShippingRespVO.class));
    }

    // ==================== 子表（销售发货明细） ====================

    @GetMapping("/shipping-detail/list-by-shipping-id")
    @Operation(summary = "获得销售发货明细列表")
    @Parameter(name = "shippingId", description = "发货单ID")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<List<ShippingDetailRespVO>> getShippingDetailListByShippingId(@RequestParam("shippingId") String shippingId) {

        List<ShippingDetailDO> shippingDetailDOList = shippingService.getShippingDetailListByShippingId(shippingId);
        ShippingDO shippingDO = shippingService.getShipping(shippingId);
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


    @GetMapping("/getOrder")
    @Operation(summary = "根据合同获取订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<List<ContractOrderRespVO>> getOrderByContract(@RequestParam("id") String id) {
        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(id);


        //查询合同下  所有已发货的发货单
        List<ShippingDetailDO> detailDOS = shippingDetailService.getOutboundOderByContractId(id,Lists.newArrayList(0,1,2,3,4,5));
        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = new HashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            detailDOS.forEach(shippingDetailDO -> {

                if (numberMap.get(shippingDetailDO.getOrderId()) == null) {
                    numberMap.put(shippingDetailDO.getOrderId(), shippingDetailDO.getConsignedAmount());
                } else {
                    numberMap.get(shippingDetailDO.getOrderId()).add(shippingDetailDO.getConsignedAmount());
                }
            });
        }

        List<String> materialIds = convertList(list, ContractOrderDO::getMaterialId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        List<ShippingReturnDetailDO> shippingReturnDetailDOS = shippingReturnDetailService.getShippingReturnDetails(id, Lists.newArrayList(0,1,2,3,4));
        Map<String, BigDecimal> returnNumberMap = new HashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(shippingReturnDetailDOS)) {
            shippingReturnDetailDOS.forEach(shippingReturnDetailDO -> {

                if (returnNumberMap.get(shippingReturnDetailDO.getOrderId()) == null) {
                    returnNumberMap.put(shippingReturnDetailDO.getOrderId(), shippingReturnDetailDO.getConsignedAmount());
                } else {
                    returnNumberMap.get(shippingReturnDetailDO.getOrderId()).add(shippingReturnDetailDO.getConsignedAmount());
                }
            });
        }

        return success(ShippingConvert.INSTANCE.convertOrderList(list, map, numberMap,returnNumberMap));
    }


    @GetMapping("/getShippingDetail")
    @Operation(summary = "根据合同获取发货单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<List<ShippingDetailRespVO>> getShippingDetailContract(@RequestParam("id") String id) {


        List<ContractOrderDO> list = contractService.getContractOrderListByContractId(id);

        Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(list, ContractOrderDO::getId);

        //查询合同下  所有已发货的发货单
        List<ShippingDetailDO> detailDOS = shippingDetailService.getOutboundOderByContractId(id,Lists.newArrayList(4,5));

        List<String> materialIds = convertList(list, ContractOrderDO::getMaterialId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        return success(ShippingDetailConvert.INSTANCE.convertDetailList(detailDOS, map,orderMap));
    }



    @GetMapping("/list")
    @Operation(summary = "获得销售发货单集合")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<List<ShippingRespVO>> getShippingList(@RequestParam("status") String status) {

        List<ShippingDO> shippingDOS = shippingService.getShippingList(StringListUtils.stringListToIntegerList(Arrays.asList(status.split(","))));

        return success(BeanUtils.toBean(shippingDOS,ShippingRespVO.class));
    }




    @GetMapping("/getShippingDetailByProject")
    @Operation(summary = "根据项目获取发货单详细")
    @Parameter(name = "projectId", description = "项目ID", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ShippingDetailRespVO>> getShippingDetailByProject(@RequestParam("projectId") String projectId,
                                                                               @RequestParam("contractId") String contractId,
                                                                               @RequestParam("shippingType") Integer shippingType) {


        if (contractId.equals("0")){
            contractId = null;
        }
        if (projectId.equals("0")){
            projectId = null;
        }
        // 合同信息
        List<ShippingDetailDO> shippingDetailDOList = shippingService.getShippingDetailListByProjectId(projectId,contractId,shippingType);

        List<String> detailIds = shippingDetailDOList.stream().map(ShippingDetailDO::getId).collect(Collectors.toList());

        List<ConsignmentDetailDO> detailDOS = purchaseConsignmentDetailService.getDetailListByShippingDetailIds(detailIds);


        List<String> useDetailIds = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)){
            useDetailIds.addAll(detailDOS.stream().map(ConsignmentDetailDO::getShippingDetailId).collect(Collectors.toList()));
        }
        List<ShippingDetailDO>  shippingDetailDOs = shippingDetailDOList.stream().filter(shippingDetailDO -> !useDetailIds.contains(shippingDetailDO.getId())).collect(Collectors.toList());


        List<String> materialIds = convertList(shippingDetailDOs, ShippingDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);

        return success(ShippingDetailConvert.INSTANCE.convertList(shippingDetailDOs, map));

    }


}