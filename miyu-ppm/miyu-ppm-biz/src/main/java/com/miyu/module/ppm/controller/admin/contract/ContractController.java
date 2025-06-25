package com.miyu.module.ppm.controller.admin.contract;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.controller.admin.contractpaymentscheme.vo.ContractPaymentSchemeRespVO;
import com.miyu.module.ppm.dal.dataobject.consignmentrefund.ConsignmentRefundDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contractinvoice.ContractInvoiceDO;
import com.miyu.module.ppm.dal.dataobject.contractpaymentscheme.ContractPaymentSchemeDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.service.consignmentrefund.ConsignmentRefundService;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnService;
import com.miyu.module.ppm.service.contractconsignmentdetail.ContractConsignmentDetailService;
import com.miyu.module.ppm.service.contractinvoice.ContractInvoiceService;
import com.miyu.module.ppm.service.contractpayment.ContractPaymentService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentServiceImpl;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;
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

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.framework.common.util.collection.MapUtils.findAndThen;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.PURCHASE_CONTRACT_ERROR;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_OUTBOUND_ERROR;

import com.miyu.module.ppm.controller.admin.contract.vo.*;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.service.contract.ContractService;

@Tag(name = "管理后台 - 购销合同")
@RestController
@RequestMapping("/ppm/contract")
@Validated
public class ContractController {
    private static Logger logger = LoggerFactory.getLogger(ContractController.class);

    @Resource
    private ContractService contractService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private MaterialMCCApi materialMCCApi;


    @Resource
    private ContractPaymentService contractPaymentService;

    @Resource
    private ContractInvoiceService contractInvoiceService;

    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;

    @Resource
    private ConsignmentReturnService consignmentReturnService;

    @Resource
    private ConsignmentRefundService consignmentRefundService;

    @Resource
    private PmsApi pmsApi;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private ShippingDetailService shippingDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建购销合同")
    @PreAuthorize("@ss.hasPermission('ppm:contract:create')")
    public CommonResult<String> createContract(@Valid @RequestBody ContractSaveReqVO createReqVO) {
        return success(contractService.createContract(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新购销合同")
    @PreAuthorize("@ss.hasPermission('ppm:contract:update')")
    public CommonResult<Boolean> updateContract(@Valid @RequestBody ContractSaveReqVO updateReqVO) {
        contractService.updateContract(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除购销合同")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:contract:delete')")
    public CommonResult<Boolean> deleteContract(@RequestParam("id") String id) {
        contractService.deleteContract(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得购销合同")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<ContractRespVO> getContract(@RequestParam("id") String id) {
        ContractDO contract = contractService.getContract(id);

        // 获取合同关联订单集合
        List<ContractOrderDO> orderList = contractService.getContractOrderListByContractId(id);
        // 获取付款计划集合
        List<ContractPaymentSchemeDO> schemeList = contractService.getContractPaymentSchemeListByContractId(id);

        // Map<String, ContractOrderProductDO> orderMap = contractService.getContractProductPriceHis(convertSet(orderList, obj -> obj.getMaterialId()));
        // 产品主键获取产品物料属性
        Map<String, MaterialConfigRespDTO> productMap = materialMCCApi.getMaterialConfigMap(
                convertSet(orderList, obj -> obj.getMaterialId()));
        // 封装产品属性
        List<ContractRespVO.Product> productList = BeanUtils.toBean(orderList, ContractRespVO.Product.class, vo -> {
            // 设置单位
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setMaterialUnit(product.getMaterialUnit()));
            // 设置价格
//            findAndThen(orderMap, vo.getMaterialId(), product -> {
//                vo.setMaxPrice(orderMap.get(vo.getMaterialId()) == null ? "" : orderMap.get(vo.getMaterialId()).getMaxPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//                vo.setMinPrice(orderMap.get(vo.getMaterialId()) == null ? "" : orderMap.get(vo.getMaterialId()).getMinPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//                vo.setAvgPrice(orderMap.get(vo.getMaterialId()) == null ? "" : (orderMap.get(vo.getMaterialId()).getAvgPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString()));
//                vo.setLatestPrice(orderMap.get(vo.getMaterialId()) == null ? "" : orderMap.get(vo.getMaterialId()).getLatestPrice().setScale(2, BigDecimal.ROUND_HALF_UP).toString());
//            });
        });

        Map<String, PmsApprovalDto> map = new HashMap<>();
        if (StringUtils.isNotBlank(contract.getProjectId())){
            map = pmsApi.getApprovalMap(Lists.newArrayList(contract.getProjectId()));
        }



        // 获取采购人
        List<ContractDO> list = new ArrayList();
        list.add(contract);
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(
                convertSet(list, obj -> Long.parseLong(obj.getPurchaser())));

        Map<String, PmsApprovalDto> finalMap = map;
        return success(BeanUtils.toBean(contract, ContractRespVO.class, vo -> {
            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
            vo.setProducts(productList);
            vo.setProjectName(finalMap.get(vo.getProjectId()) !=null?finalMap.get(vo.getProjectId()).getProjectName():"");
            vo.setPaymentSchemes(BeanUtils.toBean(schemeList, ContractRespVO.PaymentScheme.class));
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得购销合同分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<PageResult<ContractRespVO>> getContractPage(@Valid ContractPageReqVO pageReqVO) {
        PageResult<ContractDO> pageResult = contractService.getContractPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        List<Long> selfContacts = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getSelfContact()));
        List<Long> purchaserIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getPurchaser()));
        List<Long> userIdList = new ArrayList<>();
        //合并用户集合
        if (selfContacts != null)userIdList.addAll(selfContacts);
        if (purchaserIds!= null)userIdList.addAll(purchaserIds);

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(pageResult.getList(), obj -> Long.parseLong(obj.getDepartment())));

        // 拼接项目名称
        List<String> projects = convertList(pageResult.getList(), obj -> obj.getProjectId());
        Map<String, PmsApprovalDto> projectMap = new HashMap<>();
        try {
            projectMap = pmsApi.getApprovalMap(projects);
        } catch (Exception e) {
            logger.error("調用PMS失敗");
        }

        Map<String, PmsApprovalDto> finalProjectMap = projectMap;
        return success(BeanUtils.toBean(pageResult, ContractRespVO.class, vo -> {
            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());
            if(ObjectUtil.isNotNull(vo.getProjectId())) {
                vo.setProjectName(finalProjectMap.get(vo.getProjectId()).getProjectName());
            }
        }));

//        return success(new PageResult<>(ContractConvert.INSTANCE.convertList(pageResult.getList(), userMap),
//                pageResult.getTotal()));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出购销合同 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:contract:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportContractExcel(@Valid ContractPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ContractDO> list = contractService.getContractPage(pageReqVO).getList();

        List<Long> selfContacts = StringListUtils.stringListToLongList(convertList(list, obj -> obj.getSelfContact()));
        List<Long> purchaserIds = StringListUtils.stringListToLongList(convertList(list, obj -> obj.getPurchaser()));
        List<Long> userIdList = new ArrayList<>();
        //合并用户集合
        if (selfContacts != null)userIdList.addAll(selfContacts);
        if (purchaserIds!= null)userIdList.addAll(purchaserIds);

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(list, obj -> Long.parseLong(obj.getDepartment())));

        // 拼接项目名称
        List<String> projects = convertList(list, obj -> obj.getProjectId());
        Map<String, PmsApprovalDto> projectMap = null;
        try {
            projectMap = pmsApi.getApprovalMap(projects);
        } catch (Exception e) {
            logger.error("調用PMS失敗");
        }

        // 导出 Excel
        Map<String, PmsApprovalDto> finalProjectMap = projectMap;
        ExcelUtils.write(response, "购销合同.xls", "数据", ContractRespVO.class,
                        BeanUtils.toBean(list, ContractRespVO.class, vo -> {
                            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
                            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
                            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());
                            if(ObjectUtil.isNotNull(vo.getProjectId())) {
                                vo.setProjectName(finalProjectMap.get(vo.getProjectId()).getProjectName());
                            }
                        }));
    }

    @GetMapping("/list-by-type")
    @Operation(summary = "获得购销合同")
    @Parameter(name = "type", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ContractRespVO>> getContractListByType(@RequestParam("type") Collection<String> types) {
        List<ContractDO> contractList = contractService.getContractListByType(types);
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(
                convertSet(contractList, obj -> Long.parseLong(obj.getSelfContact())));

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(contractList, obj -> Long.parseLong(obj.getDepartment())));

        return success(BeanUtils.toBean(contractList, ContractRespVO.class, vo -> {
            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());
        }));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交合同审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract:update')")
    public CommonResult<Boolean> submitContract(@RequestParam("id") String id,  @RequestParam("processKey") String processKey) {
        contractService.submitContract(id, processKey, getLoginUserId());
        return success(true);
    }

    @PostMapping("/createAndSubmit")
    @Operation(summary = "创建并提交合同审批")
    @PreAuthorize("@ss.hasPermission('ppm:contract:create')")
    public CommonResult<Boolean> createAndsubmit(@Valid @RequestBody ContractSaveReqVO createReqVO) {
        contractService.createAndSubmitContract(createReqVO);
        return success(true);
    }

    @PutMapping("/status")
    @Operation(summary = "更新购销合同状态")
    @PreAuthorize("@ss.hasPermission('ppm:contract:update')")
    public CommonResult<Boolean> updateContractStatus(@Valid @RequestBody ContractUpdateReqVO updateReqVO) {
        contractService.updateContractStatus(updateReqVO);
        return success(true);
    }

    @GetMapping("/get-contract-by-id")
    @Operation(summary = "获得购销合同")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<ContractRespVO> selectContractById(@RequestParam("id") String id) {
        ContractDO contract = contractService.getContractById(id);

        return success(BeanUtils.toBean(contract, ContractRespVO.class));
    }

    @GetMapping("/get-contract-detail-info-by-id")
    @Operation(summary = "获得购销合同")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<ContractRespVO> getContractDetailInfoById(@RequestParam("id") String id) {
        // 合同信息
        ContractDO contractDO = contractService.getContractById(id);

        if (contractDO == null) {
            return success(new ContractRespVO());
        }
        // 获取合同关联订单集合
        List<ContractOrderDO> orderList = contractService.getContractOrderListByContractId(contractDO.getId());
        // 获取付款计划集合
        List<ContractPaymentSchemeDO> schemeList = contractService.getContractPaymentSchemeListByContractId(contractDO.getId());

        List<ContractPaymentDTO> paymentList = contractPaymentService.getContractPaymentByContractId(contractDO.getId());
        // 发票集合
        List<ContractInvoiceDO> invoiceList = contractInvoiceService.getContractInvoiceByContractId(contractDO.getId());
        // 收货单集合
        List<PurchaseConsignmentDTO> consignmentList = purchaseConsignmentService.getConsignmentDetailByContractId(contractDO.getId());
        // 退货集合
        List<ConsignmentReturnDTO> consignmentReturnList = consignmentReturnService.getConsignmentReturnListByContractId(contractDO.getId());
        // 退款集合
        List<ConsignmentRefundDO> consignmentRefundList = consignmentRefundService.getConsignmentRefundListByContractId(contractDO.getId());

        // 产品主键获取产品物料属性
        Map<String, MaterialConfigRespDTO> productMap = materialMCCApi.getMaterialConfigMap(
                convertSet(orderList, obj -> obj.getMaterialId()));
        // 封装产品属性
        List<ContractRespVO.Product> productList = BeanUtils.toBean(orderList, ContractRespVO.Product.class, vo -> {
            // 设置单位
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setMaterialUnit(product.getMaterialUnit()));
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setProductName(product.getMaterialName()));
            findAndThen(productMap, vo.getMaterialId(), product -> vo.setMaterialName(product.getMaterialName()));
        });

        List<Long> userIdList = new ArrayList<>();
        //合并用户集合
        if (contractDO.getSelfContact() != null)userIdList.add(NumberUtils.parseLong(contractDO.getSelfContact()));
        if (contractDO.getPurchaser() != null)userIdList.add(NumberUtils.parseLong(contractDO.getPurchaser()));

        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);
        HashSet<Long> deptSet= new HashSet<>();
        if (contractDO.getDepartment() != null)deptSet.add(Long.parseLong(contractDO.getDepartment()));
        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(deptSet);

        return success(BeanUtils.toBean(contractDO, ContractRespVO.class, vo -> {
            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());
            vo.setProducts(productList);
            vo.setPaymentSchemes(BeanUtils.toBean(schemeList, ContractRespVO.PaymentScheme.class));
            vo.setPayments(paymentList);
            vo.setInvoices(invoiceList);
            vo.setConsignmentList(consignmentList);
            vo.setConsignmentReturnList(consignmentReturnList);
            vo.setConsignmentRefundList(consignmentRefundList);
        }));
    }


    // ==================== 子表（合同订单） ====================

    @GetMapping("/contract-order/list-by-contract-id")
    @Operation(summary = "获得合同订单列表")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ContractOrderRespVO>> getContractOrderListByContractId(@RequestParam("contractId") String contractId) {

        List<ContractOrderDO> orderList = contractService.getContractOrderListByContractId(contractId);

        // Map<String, ContractOrderProductDO> orderMap = contractService.getContractProductPriceHis(convertSet(orderList, obj -> obj.getMaterialId()));

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(orderList, obj -> obj.getMaterialId()));

        return success(convertList(orderList, order -> new ContractOrderRespVO()
                .setId(order.getId())
                .setMaterialConfigId(order.getMaterialId())
                .setMaterialNumber(materialTypeMap.get(order.getMaterialId()).getMaterialNumber())
                .setProductName(materialTypeMap.get(order.getMaterialId()).getMaterialName())
                .setPrice(order.getPrice())
                .setQuantity(order.getQuantity())
                .setTaxRate(order.getTaxRate())
                .setProjectId(order.getProjectId())
                .setProjectPlanId(order.getProjectPlanId())
                .setProjectPlanItemId(order.getProjectPlanItemId())
                .setOrderId(order.getOrderId())
                .setTaxRate(order.getTaxRate())
                .setTaxPrice(order.getTaxPrice())
                .setLeadDate(order.getLeadDate())
                .setMaterialUnit(materialTypeMap.get(order.getMaterialId()).getMaterialUnit())
                .setMaterialName(materialTypeMap.get(order.getMaterialId()).getMaterialName())
                .setInitTax(order.getInitTax())));
//                .setMaxPrice(CollUtil.isEmpty(orderMap) ? "" : orderMap.get(order.getMaterialId()).getMaxPrice().setScale(2).toString())
//                .setMinPrice(CollUtil.isEmpty(orderMap)? "" : orderMap.get(order.getMaterialId()).getMinPrice().setScale(2).toString())
//                .setAvgPrice(CollUtil.isEmpty(orderMap) ? "" : orderMap.get(order.getMaterialId()).getAvgPrice().setScale(2).toString())
//                .setLatestPrice(CollUtil.isEmpty(orderMap) ? "" : orderMap.get(order.getMaterialId()).getLatestPrice().setScale(2).toString())));
    }


    @GetMapping("/contract-payment-scheme/list-by-contract-id")
    @Operation(summary = "获得购销合同")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ContractPaymentSchemeRespVO>> getContractPaymentSchemeListByContractId(@RequestParam("contractId") String contractId) {
        List<ContractPaymentSchemeDO> schemeList = contractService.getContractPaymentSchemeListByContractId(contractId);
        return success(BeanUtils.toBean(schemeList, ContractPaymentSchemeRespVO.class));
    }



    @GetMapping("/project-order/list-by-contract-id")
    @Operation(summary = "获得合同订单列表")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ProjectOrderDetailRespVO>> getProjectOrderListByContractId(@RequestParam("contractId") String contractId) {

        ContractDO contractDO = contractService.getContractById(contractId);
        if (contractDO.getContractType().intValue() !=2){
            //正常 和不带料外协  都不查询
            return success(null);
        }
        List<ContractOrderDO> orderList = contractService.getContractOrderListByContractId(contractId);

        Map<String,ContractOrderDO> orderDOMap = new HashMap<>();
        for (ContractOrderDO contractOrderDO : orderList){
            orderDOMap.put(contractOrderDO.getOrderId(),contractOrderDO);
        }

        List<ProjectOrderDetailRespVO> list = new ArrayList<>();
        Set<String> projectIds = new HashSet<>();

        for (ContractOrderDO contractOrderDO :orderList){
            if (StringUtils.isBlank(contractOrderDO.getOrderId())){
                continue;
            }
            OrderMaterialRelationUpdateDTO dto = new OrderMaterialRelationUpdateDTO();
            dto.setOrderId(contractOrderDO.getOrderId());
            dto.setProjectId(contractOrderDO.getProjectId());
             dto.setPlanId(contractOrderDO.getProjectPlanId());
            dto.setPlanItemId(contractOrderDO.getProjectPlanItemId());
            CommonResult<List<OrderMaterialRelationRespDTO>>  result = pmsOrderMaterialRelationApi.getRelationByPlanOrOrder(dto);

            List<OrderMaterialRelationRespDTO> finalList = result.getCheckedData().stream().filter(orderMaterialRelationRespDTO ->orderMaterialRelationRespDTO.getMaterialStatus().intValue()==4).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(finalList)){
                continue;
            }

            List<ProjectOrderDetailRespVO> respVOList = BeanUtils.toBean(finalList,ProjectOrderDetailRespVO.class);

            respVOList.forEach(projectOrderDetailRespVO -> {
                projectOrderDetailRespVO.setId(contractOrderDO.getId());
            });
            //状态为4 6的可外协
            List<Integer> status = Lists.newArrayList(4,6);
            list.addAll(respVOList.stream().filter(projectOrderDetailRespVO -> status.contains(projectOrderDetailRespVO.getMaterialStatus())).collect(Collectors.toList()));
            projectIds.add(contractOrderDO.getProjectId());
        }
        if (CollectionUtils.isEmpty(list)){
            throw exception(PURCHASE_CONTRACT_ERROR);
        }




        Set<String> barcode = convertSet(list, obj -> obj.getVariableCode());
        CommonResult<List<MaterialStockRespDTO>> commonResult = materialStockApi.getMaterialsByBarCodes(barcode);
        List<MaterialStockRespDTO> respDTOS = commonResult.getCheckedData();

        Set<String> configIds = convertSet(respDTOS, obj -> obj.getMaterialConfigId());
        Map<String, MaterialConfigRespDTO>  materialConfigMap = materialMCCApi.getMaterialConfigMap(configIds);

        //key  barcode  value 物料属性
        Map<String,MaterialConfigRespDTO> map  =new HashMap<>();
        Map<String,MaterialStockRespDTO> stockRespDTOMap  =new HashMap<>();

        for (MaterialStockRespDTO respDTO :respDTOS){
            MaterialConfigRespDTO configRespDTO = materialConfigMap.get(respDTO.getMaterialConfigId());
            map.put(respDTO.getBarCode(),configRespDTO);
            stockRespDTOMap.put(respDTO.getBarCode(),respDTO);
        }

//        List<ShippingDetailDO> contractConsignmentDetailDOS = shippingDetailService.getDetailByProjectIds(projectIds);
//        List<String> barcodes = convertList(contractConsignmentDetailDOS,contractConsignmentDetailDO -> contractConsignmentDetailDO.getBarCode());


        List<ProjectOrderDetailRespVO> result = new ArrayList<>();
        for (ProjectOrderDetailRespVO respVO :list){

            ProjectOrderDetailRespVO detailRespVO = respVO;
            detailRespVO.setContractOrderId(orderDOMap.get(respVO.getOrderId()).getId());
            MaterialConfigRespDTO a = map.get(detailRespVO.getVariableCode());
            if(a !=null){
                detailRespVO.setMaterialConfigId(a.getId()).setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                        .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                        .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setQuantity(1);

            }
            MaterialStockRespDTO respDTO = stockRespDTOMap.get(detailRespVO.getVariableCode());
            if (respDTO !=null){
                detailRespVO.setMaterialStockId(respDTO.getId());
                detailRespVO.setBatchNumber(respDTO.getBatchNumber());
            }else {
                continue;
            }
            detailRespVO.setQuantity(1);
            result.add(detailRespVO);
        }

        return success(result);
    }




    @GetMapping("/project-order-in/list-by-contract-id")
    @Operation(summary = "获得合同订单列表（外协收货用）")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ProjectOrderDetailRespVO>> getProjectOrderListByContractIdForIn(@RequestParam("contractId") String contractId) {

        ContractDO contractDO = contractService.getContractById(contractId);
        if (contractDO.getContractType().intValue() !=2){
            //正常 和不带料外协  都不查询
            return success(null);
        }
        List<ContractOrderDO> orderList = contractService.getContractOrderListByContractId(contractId);

        Map<String,ContractOrderDO> orderDOMap = new HashMap<>();
        for (ContractOrderDO contractOrderDO : orderList){
            orderDOMap.put(contractOrderDO.getOrderId(),contractOrderDO);
        }

        List<ProjectOrderDetailRespVO> list = new ArrayList<>();
        Set<String> projectIds = new HashSet<>();

        for (ContractOrderDO contractOrderDO :orderList){
            if (StringUtils.isBlank(contractOrderDO.getOrderId())){
                continue;
            }
            OrderMaterialRelationUpdateDTO dto = new OrderMaterialRelationUpdateDTO();
            dto.setOrderId(contractOrderDO.getOrderId());
            dto.setProjectId(contractOrderDO.getProjectId());
            dto.setPlanId(contractOrderDO.getProjectPlanId());
            dto.setPlanItemId(contractOrderDO.getProjectPlanItemId());
            CommonResult<List<OrderMaterialRelationRespDTO>>  result = pmsOrderMaterialRelationApi.getRelationByPlanOrOrder(dto);

            List<OrderMaterialRelationRespDTO> finalList = result.getCheckedData().stream().filter(orderMaterialRelationRespDTO ->orderMaterialRelationRespDTO.getMaterialStatus().intValue()==4).collect(Collectors.toList());
            if (CollectionUtils.isEmpty(finalList)){
                continue;
            }

            List<ProjectOrderDetailRespVO> respVOList = BeanUtils.toBean(finalList,ProjectOrderDetailRespVO.class);

            respVOList.forEach(projectOrderDetailRespVO -> {
                projectOrderDetailRespVO.setId(contractOrderDO.getId());
                projectOrderDetailRespVO.setMaterialConfigId(contractOrderDO.getMaterialId());
            });
            //状态为4 6的可外协
            List<Integer> status = Lists.newArrayList(4,6);
            list.addAll(respVOList.stream().filter(projectOrderDetailRespVO -> status.contains(projectOrderDetailRespVO.getMaterialStatus())).collect(Collectors.toList()));
            projectIds.add(contractOrderDO.getProjectId());
        }
        if (CollectionUtils.isEmpty(list)){
            throw exception(PURCHASE_CONTRACT_ERROR);
        }
        Set<String> ids = convertSet(orderList, obj -> obj.getMaterialId());
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialMCCApi.getMaterialConfigMap(ids);


        Set<String> barcode = convertSet(list, obj -> obj.getVariableCode());
        CommonResult<List<MaterialStockRespDTO>> commonResult = materialStockApi.getMaterialsByBarCodes(barcode);
        List<MaterialStockRespDTO> respDTOS = commonResult.getCheckedData();
        //key  barcode  value 物料属性
        Map<String,MaterialStockRespDTO> stockRespDTOMap  =convertMap(respDTOS,MaterialStockRespDTO::getBarCode);

        List<ProjectOrderDetailRespVO> result = new ArrayList<>();
        for (ProjectOrderDetailRespVO respVO :list){

            ProjectOrderDetailRespVO detailRespVO = respVO;
            detailRespVO.setContractOrderId(orderDOMap.get(respVO.getOrderId()).getId());
            MaterialConfigRespDTO a = materialConfigMap.get(detailRespVO.getMaterialConfigId());
            if(a !=null){
                detailRespVO.setMaterialConfigId(a.getId()).setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                        .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                        .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setQuantity(1);

            }
            MaterialStockRespDTO respDTO = stockRespDTOMap.get(detailRespVO.getVariableCode());
            if (respDTO !=null) {
                detailRespVO.setMaterialStockId(respDTO.getId());
                detailRespVO.setBatchNumber(respDTO.getBatchNumber());
            }
            detailRespVO.setQuantity(1);
            result.add(detailRespVO);
        }

        return success(result);
    }



    /**
     * 采购申请详情ID获取采购合同集合
     * @param requirementDetailId
     * @return
     */
    @GetMapping("/contract-order/list-by-requirement-detail-id")
    @Operation(summary = "获得合同订单列表")
    @Parameter(name = "contractId", description = "合同ID")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<List<ContractOrderRespVO>> getContractOrderListByRequirementDetailId(@RequestParam("requirementDetailId") String requirementDetailId) {
        List<ContractOrderDO> orderList = contractService.getContractOrderListByRequirementDetailId(requirementDetailId);
        return success(BeanUtils.toBean(orderList, ContractOrderRespVO.class));
    }

}
