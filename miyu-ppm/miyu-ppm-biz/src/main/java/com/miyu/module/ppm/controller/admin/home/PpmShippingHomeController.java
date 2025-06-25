package com.miyu.module.ppm.controller.admin.home;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.pms.api.pms.dto.ProductStatusRespDTO;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractPageReqVO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractRespVO;
import com.miyu.module.ppm.controller.admin.home.vo.ContractProgressResp;
import com.miyu.module.ppm.controller.admin.home.vo.PurchaseContractExecutionResp;
import com.miyu.module.ppm.controller.admin.home.vo.ContractAnalysisResp;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingPageReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.convert.purchaseConsignment.PurchaseConsignmentConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.consignmentinfo.ConsignmentInfoService;
import com.miyu.module.ppm.service.contract.ContractOrderService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.inboundexceptionhandling.InboundExceptionHandlingService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaserequirement.PurchaseRequirementService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippinginfo.ShippingInfoService;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;

@Tag(name = "管理后台 - 首页")
@RestController
@RequestMapping("/ppm/shippingHome")
@Validated
@Slf4j
public class PpmShippingHomeController {

    @Resource
    private CompanyService companyService;

    @Resource
    private ContractService contractService;

    @Resource
    private PurchaseRequirementService purchaseRequirementService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    @Resource
    private ConsignmentInfoService consignmentInfoService;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private ShippingInfoService shippingInfoService;
    @Resource
    private ContractOrderService contractOrderService;
    @Resource
    private InspectionSheetApi inspectionSheetApi;
    @Resource
    private ShippingService shippingService;

    @Resource
    private InboundExceptionHandlingService inboundExceptionHandlingService;


    @GetMapping("/getContractDetailPage")
    @Operation(summary = "获得购销合同执行情况分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<PageResult<PurchaseContractExecutionResp>> getContractShippingDetailPage(@Valid ContractPageReqVO pageReqVO) {
        pageReqVO.setContractStatus(1);
        //获取产品订单
        List<ContractOrderDO> orderDOS = contractOrderService.getContractOrderPage(pageReqVO);
        if (CollUtil.isEmpty(orderDOS)) {
            return success(new PageResult<>());
        }


        List<String> orderIds = orderDOS.stream().map(ContractOrderDO::getId).distinct().collect(Collectors.toList());

        //获取物料类型
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(orderDOS, obj -> obj.getMaterialId()));


        //查询采购收货单
        List<ShippingInfoDO> shippingInfoDOS = shippingInfoService.getShippingInfoByOrderIds(orderIds);
        Map<String, BigDecimal> map = new HashMap<>();
        Map<String, List<String>> consignmentIdMap = new HashMap<>();

        for (ShippingInfoDO infoDO : shippingInfoDOS) {

            if (map.get(infoDO.getOrderId()) == null) {
                map.put(infoDO.getOrderId(), infoDO.getOutboundAmount());
            } else {
                BigDecimal s = map.get(infoDO.getOrderId()).add(infoDO.getOutboundAmount());
                map.put(infoDO.getOrderId(), s);
            }

            if (consignmentIdMap.get(infoDO.getOrderId()) == null) {
                consignmentIdMap.put(infoDO.getOrderId(), Lists.newArrayList(infoDO.getShippingId()));
            } else {
                consignmentIdMap.get(infoDO.getOrderId()).add(infoDO.getShippingId());
            }

        }
        List<String> shippingIds = shippingInfoDOS.stream().map(ShippingInfoDO::getShippingId).distinct().collect(Collectors.toList());

        //查询退货单
        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoService.getConsignmentInfoByShippingIds(shippingIds);
        LocalDateTime dateTime = LocalDateTime.now();

        List<String> materialConfigIds = orderDOS.stream().map(ContractOrderDO::getMaterialId).distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> materialConfigMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        List<String> projectIds  = orderDOS.stream().map(ContractOrderDO::getProjectId).distinct().collect(Collectors.toList());
        CommonResult<List<ProductStatusRespDTO>> commonResult = new CommonResult<>();

        //获取项目进度
        try {
            commonResult =  pmsApi.getProductStatusList(projectIds);
        } catch (Exception e) {
            log.error("调用PMS系统失败"+e.getMessage());
        }
        List<ProductStatusRespDTO> respDTOS = commonResult.getCheckedData();




        List<PurchaseContractExecutionResp> resps = new ArrayList<>();



        Map<String,Integer> projectFinishMap = new HashMap<>();
        for (ContractOrderDO orderDO : orderDOS) {

            //获取物料信息
            MaterialConfigRespDTO  materialConfigRespDTO = materialConfigMap.get(orderDO.getMaterialId());
            //获取进度
            List<ProductStatusRespDTO> respDTOList = respDTOS.stream().filter(productStatusRespDTO -> productStatusRespDTO.getPartNumber().equals(materialConfigRespDTO.getMaterialNumber())&& productStatusRespDTO.getId().equals(orderDO.getProjectId())).collect(Collectors.toList());

            ProductStatusRespDTO statusRespDTO = respDTOList.get(0);
            PurchaseContractExecutionResp resp = BeanUtils.toBean(orderDO, PurchaseContractExecutionResp.class);
            BigDecimal finishAmount = new BigDecimal(0);
            BigDecimal s = map.get(orderDO.getId());
            if (s != null) {
                finishAmount = finishAmount.add(s);
            }
            MaterialConfigRespDTO respDTO = materialTypeMap.get(orderDO.getMaterialId());
            resp.setMaterialName(respDTO.getMaterialName());
            resp.setMaterialNumber(respDTO.getMaterialNumber());
            List<String> consignmentIdList = consignmentIdMap.get(orderDO.getId());
            List<ConsignmentInfoDO> consignmentInfoDOList = new ArrayList<>();
            if (!CollectionUtils.isAnyEmpty(consignmentIdList)) {
                consignmentInfoDOList = consignmentInfoDOS.stream().filter(shippingInfoDO -> consignmentIdList.contains(shippingInfoDO.getShippingId()) && shippingInfoDO.getMaterialConfigId().equals(orderDO.getMaterialId())).collect(Collectors.toList());

            }
            if (!CollectionUtils.isAnyEmpty(consignmentInfoDOList)) {
                BigDecimal returnAmount = new BigDecimal(0);
                for (ConsignmentInfoDO shippingInfoDO : consignmentInfoDOList) {
                    returnAmount = returnAmount.add(shippingInfoDO.getSignedAmount());
                }

                finishAmount = finishAmount.subtract(returnAmount);
            }
            resp.setFinishQuantity(finishAmount);
            String key = statusRespDTO.getId()+"_"+statusRespDTO.getPartNumber();

            if (projectFinishMap.get(key) != null){
                Integer finish = projectFinishMap.get(key);
                projectFinishMap.put(key,finish -finishAmount.intValue());
            }else {
                projectFinishMap.put(key,statusRespDTO.getCompleted()-finishAmount.intValue());
            }

            if (finishAmount.compareTo(orderDO.getQuantity()) == 0) {
                //resp.setLeadStatus(4);//完成


                continue;
            } else {

                if (dateTime.isAfter(resp.getLeadDate())) {
                    resp.setLeadStatus(1);//逾期
                } else {
                    long day = Duration.between(dateTime, resp.getLeadDate()).toDays();
                    if (day > 10l) {
                        resp.setLeadStatus(3);//正常
                    } else {
                        resp.setLeadStatus(2);//临期
                    }

                }


            }

            resps.add(resp);
        }


        Collections.sort(resps, new Comparator<PurchaseContractExecutionResp>() {
            @Override
            public int compare(PurchaseContractExecutionResp o1, PurchaseContractExecutionResp o2) {
                return o1.getLeadStatus().compareTo(o2.getLeadStatus());
            }
        });

        for (PurchaseContractExecutionResp resp :resps){
            //获取物料信息
            MaterialConfigRespDTO  materialConfigRespDTO = materialConfigMap.get(resp.getMaterialId());
            //获取进度
            List<ProductStatusRespDTO> respDTOList = respDTOS.stream().filter(productStatusRespDTO -> productStatusRespDTO.getPartNumber().equals(materialConfigRespDTO.getMaterialNumber())&& productStatusRespDTO.getId().equals(resp.getProjectId())).collect(Collectors.toList());

            ProductStatusRespDTO statusRespDTO = respDTOList.get(0);
            String key = statusRespDTO.getId()+"_"+statusRespDTO.getPartNumber();

            Integer number = projectFinishMap.get(key);

            resp.setCompleted(number);
            resp.setUnprocessed(statusRespDTO.getUnprocessed());
            resp.setProcessed(statusRespDTO.getProcessed());
            BigDecimal bigDecimal = new BigDecimal(number);
            BigDecimal bigDecimal1 = new BigDecimal(statusRespDTO.getQuantity()-statusRespDTO.getCompleted() + number);
            resp.setRate(bigDecimal.divide(bigDecimal1).multiply(new BigDecimal(100)));

        }


        PageResult<PurchaseContractExecutionResp> pageResult = new PageResult();
        int count = resps.size(); // 总记录数
        // 计算总页数
        int pages = count % pageReqVO.getPageSize() == 0 ? count / pageReqVO.getPageSize() : count / pageReqVO.getPageSize() + 1;
        // 起始位置
        int start = pageReqVO.getPageNo() <= 0 ? 0 : (pageReqVO.getPageNo() > pages ? (pages - 1) * pageReqVO.getPageSize() : (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize());
        // 终止位置
        int end = pageReqVO.getPageNo() <= 0 ? (pageReqVO.getPageSize() <= count ? pageReqVO.getPageSize() : count) : (pageReqVO.getPageSize() * pageReqVO.getPageNo() <= count ? pageReqVO.getPageSize() * pageReqVO.getPageNo() : count);

        pageResult.setTotal((long) count);
        pageResult.setList(resps.subList(start, end));
        return success(pageResult);

    }


    @GetMapping("/getShippingPage")
    @Operation(summary = "获得销售发货分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<PageResult<ShippingRespVO>> getShippingPage(@Valid ShippingPageReqVO pageReqVO) {
        pageReqVO.setShippingType(1);
        pageReqVO.setShippingStatuss(Lists.newArrayList(0, 1, 2, 3, 4));
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
            log.error("调用PMS系统失败" + e.getMessage());
        }

        // 拼接数据 用户信息

        //合同信息
        List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);
        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractDO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());

        List<CompanyDO> companyList = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyRespDTOMap = CollectionUtils.convertMap(companyList, CompanyDO::getId);


        return success(new PageResult<>(ShippingConvert.INSTANCE.convertList(pageResult.getList(), userMap, contractMap, companyRespDTOMap, pmsApprovalDtoMap),
                pageResult.getTotal()));
    }


    @GetMapping("/getShippingReturnPage")
    @Operation(summary = "获得采购退货单分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<PageResult<PurchaseConsignmentRespVO>> getShippingReturnPage(@Valid PurchaseConsignmentPageReqVO pageReqVO) {
        pageReqVO.setConsignmentType(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus());
        pageReqVO.setConsignmentStatuss(Lists.newArrayList(0, 1, 2, 3, 9, 10));
        PageResult<ConsignmentDO> pageResult = purchaseConsignmentService.getPurchaseConsignmentPage(pageReqVO);
        if (pageResult.getList().size() > 0) {
            //用户
            List<Long> userIdList = com.miyu.module.ppm.controller.admin.contract.util.StringListUtils.stringListToLongList(convertList(pageResult.getList(), ConsignmentDO::getConsignedBy));
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


    @GetMapping("/getContractAnalysis")
    @Operation(summary = "获取销售金额统计")
    public CommonResult<List<ContractAnalysisResp>> getContractAnalysis(@Valid ContractPageReqVO pageReqVO) {
        return success(shippingService.getContractAnalysis(pageReqVO.getType()));
    }

    //合同
    @GetMapping("/contractPage")
    @Operation(summary = "获得购销合同分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping:query')")
    public CommonResult<PageResult<ContractRespVO>> getContractPage(@Valid ContractPageReqVO pageReqVO) {
        pageReqVO.setContractStatus(1);
        PageResult<ContractDO> pageResult = contractService.getContractPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        List<Long> selfContacts = com.miyu.module.ppm.controller.admin.contract.util.StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getSelfContact()));
        List<Long> purchaserIds = com.miyu.module.ppm.controller.admin.contract.util.StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getPurchaser()));
        List<Long> userIdList = new ArrayList<>();
        List<String> contractIds = convertList(pageResult.getList(), obj -> obj.getId());
        //合并用户集合
        if (selfContacts != null) userIdList.addAll(selfContacts);
        if (purchaserIds != null) userIdList.addAll(purchaserIds);

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
            log.error("調用PMS失敗");
        }


        List<ContractOrderDO> contractOrderDOS = contractService.getContractOrderListByContractIds(contractIds);
        Map<String, BigDecimal> priceMap = new HashMap<>();//合同价格
        Map<String, BigDecimal> numberMap = new HashMap<>();//合同产品数量
        for (ContractOrderDO contractOrderDO : contractOrderDOS) {
            BigDecimal price = contractOrderDO.getTaxPrice().multiply(contractOrderDO.getQuantity());
            if (priceMap.get(contractOrderDO.getContractId()) == null) {
                priceMap.put(contractOrderDO.getContractId(), price);
            } else {
                BigDecimal a = priceMap.get(contractOrderDO.getContractId()).add(price);
                priceMap.put(contractOrderDO.getContractId(), a);
            }


            if (numberMap.get(contractOrderDO.getContractId()) == null) {
                numberMap.put(contractOrderDO.getContractId(), contractOrderDO.getQuantity());
            } else {
                BigDecimal a = numberMap.get(contractOrderDO.getContractId()).add(contractOrderDO.getQuantity());
                numberMap.put(contractOrderDO.getContractId(), a);
            }
        }


        //收货单
        List<ShippingInfoDO> shippingInfoDOS = shippingInfoService.getShippingInfoByContractIds(contractIds);
        //退货单
        List<String> shippingIds = shippingInfoDOS.stream().map(ShippingInfoDO::getShippingId).distinct().collect(Collectors.toList());
        List<ConsignmentInfoDO> consignmentInfoDOS = CollUtil.isEmpty(shippingIds) ? new ArrayList<>() : consignmentInfoService.getConsignmentInfoByShippingIds(shippingIds);


        Map<String, BigDecimal> finishNumberMap = new HashMap<>();//合同发货产品数量
        if (!CollUtil.isEmpty(shippingInfoDOS)) {
            for (ShippingInfoDO shippingInfoDO : shippingInfoDOS) {

                BigDecimal cancelNumber = new BigDecimal(0);
                BigDecimal number = shippingInfoDO.getConsignedAmount().subtract(cancelNumber);
                if (finishNumberMap.get(shippingInfoDO.getContractId()) == null) {
                    finishNumberMap.put(shippingInfoDO.getContractId(), number);
                } else {
                    BigDecimal a = numberMap.get(shippingInfoDO.getContractId()).add(number);

                    finishNumberMap.put(shippingInfoDO.getContractId(), a);
                }
            }
        }

        if (!CollUtil.isEmpty(consignmentInfoDOS)) {
            for (ConsignmentInfoDO consignmentInfoDO : consignmentInfoDOS) {
                BigDecimal cancelNumber = consignmentInfoDO.getConsignedAmount();
                if (finishNumberMap.get(consignmentInfoDO.getContractId()) != null) {
                    BigDecimal sNumber = finishNumberMap.get(consignmentInfoDO.getContractId()).subtract(cancelNumber);
                    finishNumberMap.put(consignmentInfoDO.getContractId(), sNumber);
                }

            }
        }

        List<ContractPaymentDO> contractPaymentDOS = contractService.getContractPaymentListByContractIds(contractIds);
        Map<String, BigDecimal> finishPriceMap = new HashMap<>();//合同价格
        if (!CollUtil.isEmpty(contractPaymentDOS)) {
            for (ContractPaymentDO contractPaymentDO : contractPaymentDOS) {
                BigDecimal price = contractPaymentDO.getAmount();

                if (finishPriceMap.get(contractPaymentDO.getContractId()) == null) {
                    finishPriceMap.put(contractPaymentDO.getContractId(), price);
                } else {
                    BigDecimal a = numberMap.get(contractPaymentDO.getContractId()).add(price);

                    finishPriceMap.put(contractPaymentDO.getContractId(), a);
                }
            }
        }


        Map<String, PmsApprovalDto> finalProjectMap = projectMap;
        return success(BeanUtils.toBean(pageResult, ContractRespVO.class, vo -> {
            vo.setSelfContactName(userMap.get(NumberUtils.parseLong(vo.getSelfContact())).getNickname());
            vo.setPurchaserName(userMap.get(NumberUtils.parseLong(vo.getPurchaser())).getNickname());
            vo.setDepartmentName(deptMap.get(NumberUtils.parseLong(vo.getDepartment())).getName());


            BigDecimal finish = finishNumberMap.get(vo.getId()) == null ? new BigDecimal(0) : finishNumberMap.get(vo.getId());
            BigDecimal trueNumber = numberMap.get(vo.getId()) == null ? new BigDecimal(0) : numberMap.get(vo.getId());
            Boolean consignmentFinish = finish.compareTo(trueNumber) == 0;

            BigDecimal priceNumber = finishPriceMap.get(vo.getId()) == null ? new BigDecimal(0) : finishPriceMap.get(vo.getId());
            BigDecimal truePriceNumber = priceMap.get(vo.getId()) == null ? new BigDecimal(0) : priceMap.get(vo.getId());

            Boolean priceFinish = priceNumber.compareTo(truePriceNumber) == 0;

            if (consignmentFinish && priceFinish) {
                vo.setContractProgressStatus(4);
            } else if (consignmentFinish && !priceFinish) {
                vo.setContractProgressStatus(3);
            } else if (!consignmentFinish && priceFinish) {
                vo.setContractProgressStatus(2);
            } else if (!consignmentFinish && !priceFinish) {
                vo.setContractProgressStatus(1);
            }

            if (ObjectUtil.isNotNull(vo.getProjectId())) {
                vo.setProjectName(finalProjectMap.get(vo.getProjectId()).getProjectName());
            }
        }));

    }


}