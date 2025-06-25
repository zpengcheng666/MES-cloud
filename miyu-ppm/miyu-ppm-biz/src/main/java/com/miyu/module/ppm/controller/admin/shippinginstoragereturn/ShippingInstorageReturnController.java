package com.miyu.module.ppm.controller.admin.shippinginstoragereturn;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingPageReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingSaveReqVO;
import com.miyu.module.ppm.controller.admin.shippingdetail.vo.ShippingDetailRespVO;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.convert.shippingdetail.ShippingDetailConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
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
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;


@Tag(name = "管理后台 - 委托加工退货(发货)")
@RestController
@RequestMapping("/ppm/shipping-instorage-return")
@Validated
public class ShippingInstorageReturnController {

    @Resource
    private ShippingService shippingService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private AdminUserApi userApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Resource
    private CompanyService companyService;

    @Resource
    private ContractService contractService;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;

    @PostMapping("/create")
    @Operation(summary = "创建委托加工退货")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:create')")
    public CommonResult<String> createShippingInstorageReturn(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus());
        createReqVO.setName(ShippingTypeEnum.COMMISSIONEDPROCESSING.getName());
        return success(shippingService.createShipping(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新委托加工退货")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:update')")
    public CommonResult<Boolean> updateShippingInstorageReturn(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus());
        shippingService.updateShipping(updateReqVO);
        return success(true);
    }


    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建委托加工退货")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:create')")
    public CommonResult<String> createShippingInstorageReturnAndSubmit(@Valid @RequestBody ShippingSaveReqVO createReqVO) {
        createReqVO.setShippingType(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus());
        createReqVO.setName(ShippingTypeEnum.COMMISSIONEDPROCESSING.getName());
        return success(shippingService.createShipping(createReqVO));
    }

    @PutMapping("/updateAndSubmit")
    @Operation(summary = "更新委托加工退货")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:update')")
    public CommonResult<Boolean> updateShippingInstorageReturnAndSubmit(@Valid @RequestBody ShippingSaveReqVO updateReqVO) {
        updateReqVO.setShippingType(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus());
        shippingService.updateShippingSubmit(updateReqVO);
        return success(true);
    }


    @PutMapping("/submit")
    @Operation(summary = "提交审批")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:update')")
    public CommonResult<Boolean> submitShippingInstorageReturn(@RequestParam("id") String id) {
        shippingService.submitShipping(id, getLoginUserId());
        return success(true);
    }




    @DeleteMapping("/delete")
    @Operation(summary = "删除委托加工退货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:delete')")
    public CommonResult<Boolean> deleteShippingInstorageReturn(@RequestParam("id") String id) {
        shippingService.deleteShipping(id);
        return success(true);
    }


    @PutMapping("/cancel")
    @Operation(summary = "作废委托加工退货")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping:delete')")
    public CommonResult<Boolean> cancelShipping(@RequestParam("id") String id) {
        shippingService.cancelShipping(id);
        return success(true);
    }
    
    
    @GetMapping("/get")
    @Operation(summary = "获得委托加工退货")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:query')")
    public CommonResult<ShippingRespVO> getShippingInstorageReturn(@RequestParam("id") String id) {
        ShippingDO shipping = shippingService.getShipping(id);
        ShippingRespVO shippingRespVO = BeanUtils.toBean(shipping, ShippingRespVO.class);

        Map<String, PmsApprovalDto> result =  pmsApi.getApprovalMap(Lists.newArrayList(shippingRespVO.getProjectId()));
        PmsApprovalDto dto = result.get(shippingRespVO.getProjectId());
        shippingRespVO.setProjectName(dto.getProjectCode()+"("+dto.getProjectName()+")");
        return success(shippingRespVO);
    }

    @GetMapping("/page")
    @Operation(summary = "获得委托加工退货分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:query')")
    public CommonResult<PageResult<ShippingRespVO>> getShippingInstorageReturnPage(@Valid ShippingPageReqVO pageReqVO) {
        pageReqVO.setShippingType(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus());
        PageResult<ShippingDO> pageResult = shippingService.getShippingPage(pageReqVO);

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
    @Operation(summary = "导出委托加工退货 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingInstorageReturnExcel(@Valid ShippingPageReqVO pageReqVO,
                                                   HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShippingDO> list = shippingService.getShippingPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "委托加工退货.xls", "数据", ShippingRespVO.class,
                BeanUtils.toBean(list, ShippingRespVO.class));
    }

    // ==================== 子表（销售订单退货明细） ====================

    @GetMapping("/shipping-instorage-return-detail/list-by-shipping-storage-return-id")
    @Operation(summary = "获得销售订单退货明细列表")
    @Parameter(name = "shippingStorageReturnId", description = "收货单ID")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-return:query')")
    public CommonResult<List<ShippingDetailRespVO>> getShippingInstorageReturnDetailListByShippingStorageReturnId(@RequestParam("shippingStorageReturnId") String shippingStorageReturnId) {

        List<ShippingDetailDO> shippingDetailDOList = shippingService.getShippingDetailListByShippingId(shippingStorageReturnId);
        ShippingDO shippingDO = shippingService.getShipping(shippingStorageReturnId);
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



        Set<String> consignmentIds = shippingDetailDOList.stream().map(ShippingDetailDO::getConsignmentId).distinct().collect(Collectors.toSet());

        List<ConsignmentDO> consignmentDOS = purchaseConsignmentService.getConsignmentDetailByIds(consignmentIds);
        Map<String, ConsignmentDO> consignmentDOMap= CollectionUtils.convertMap(consignmentDOS, ConsignmentDO::getId);
        //查询合同信息

//        CommonResult<List<ContractOrderRespDTO>> list = contractApi.getOrderList(shippingDO.getContractId());
//
//         Map<String, ContractOrderRespDTO> orderMap= CollectionUtils.convertMap(list.getCheckedData(), ContractOrderRespDTO::getId);
//
//        //计算每个订单已经发货的数量
//        Map<String, BigDecimal> numberMap = shippingService.getOrderMap(shippingDO.getContractId(), null,Lists.newArrayList(0,1,2,3,4,5));


        List<String> materialIds = convertList(shippingDetailDOList, ShippingDetailDO::getMaterialConfigId);

        materialIds = materialIds.stream().distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> map = materialMCCApi.getMaterialConfigMap(materialIds);


        return success(ShippingDetailConvert.INSTANCE.convertListForReturn(shippingDetailDOList, userMap, map,consignmentDOMap));
    }


    @PutMapping("/confirmShipping")
    @Operation(summary = "出库确认")
    @Parameter(name = "id", description = "详情ID", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping:delete')")
    public CommonResult<Boolean> confirmOut(@RequestParam("id") String id) {
        shippingService.confirmOut(id);
        return success(true);
    }

}