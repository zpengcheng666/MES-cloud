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
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.ConsignmentInfoQueryReqVO;
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractPageReqVO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractRespVO;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentContractQmsNumberRespVO;
import com.miyu.module.ppm.controller.admin.home.vo.PurchaseContractExecutionResp;
import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.InboundExceptionHandlingPageReqVO;
import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.InboundExceptionHandlingRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentPageReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.PurchaseRequirementDetailRespVO;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.PurchaseRequirementPageReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingPageReqVO;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.convert.inboundexceptionhandling.ExceptionHandlingConvert;
import com.miyu.module.ppm.convert.purchaseConsignment.PurchaseConsignmentConvert;
import com.miyu.module.ppm.convert.shipping.ShippingConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.contractpayment.ContractPaymentDO;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.consignmentinfo.ConsignmentInfoService;
import com.miyu.module.ppm.service.contract.ContractOrderService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.inboundexceptionhandling.InboundExceptionHandlingService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaserequirement.PurchaseRequirementService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippinginfo.ShippingInfoService;
import com.miyu.module.qms.api.dto.InspectionSheetSchemeMaterialRespDTO;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

@Tag(name = "管理后台 - 首页")
@RestController
@RequestMapping("/ppm/home")
@Validated
@Slf4j
public class PpmConsignmentHomeController {

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

    @GetMapping("/getPurchaseRequirementPage")
    @Operation(summary = "获取需要的采购需求")
    @PreAuthorize("@ss.hasPermission('ppm:company:create')")
    public CommonResult<PageResult<PurchaseRequirementDetailRespVO>> getPurchaseRequirementDetailPage(@Valid PurchaseRequirementPageReqVO pageReqVO) {
        PageResult<PurchaseRequirementDetailDO> pageResult = purchaseRequirementService.getPurchaseRequirementDetailPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getApplicant()));
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        Map<Long, DeptRespDTO> deptMap = deptApi.getDeptMap(
                convertSet(pageResult.getList(), obj -> Long.parseLong(obj.getApplicationDepartment())));

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(pageResult.getList(), obj -> obj.getRequiredMaterial()));
        List<PurchaseRequirementDetailRespVO> respVOList = new ArrayList<>();
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = new HashMap<>();
        try {
            pmsApprovalDtoMap = pmsApi.getApprovalMap(convertSet(pageResult.getList(), obj -> obj.getProjectId()));
        } catch (Exception e) {
            log.error("PMS项目调用失败");
        }
        for (PurchaseRequirementDetailDO detailDO : pageResult.getList()) {
            PurchaseRequirementDetailRespVO respVO = BeanUtils.toBean(detailDO, PurchaseRequirementDetailRespVO.class);
            MaterialConfigRespDTO a = materialTypeMap.get(detailDO.getRequiredMaterial());
            if (a != null) {
                respVO.setRequiredMaterialName(a.getMaterialName())
                        .setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                        .setMaterialNumber(a.getMaterialNumber()).setMaterialTypeId(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                        .setMaterialSpecification(a.getMaterialSpecification())
                        .setMaterialBrand(a.getMaterialBrand())
                        .setMaterialTypeName(a.getMaterialTypeName())
                        .setMaterialParentTypeName(a.getMaterialParentTypeName());
            }

            PmsApprovalDto dto = pmsApprovalDtoMap.get(detailDO.getProjectId());
            if (dto != null) {
                respVO.setProjectName(dto.getProjectName());
                respVO.setProjectCode(dto.getProjectCode());
            }
            if (respVO.getPurchasedQuantity() == null) {
                respVO.setPurchasedQuantity(new BigDecimal(0));
            }
            respVO.setApplicant(userMap.get(NumberUtils.parseLong(respVO.getApplicant())).getNickname());
            respVO.setApplicationDepartment(deptMap.get(NumberUtils.parseLong(respVO.getApplicationDepartment())).getName());
            respVOList.add(respVO);
        }
        PageResult<PurchaseRequirementDetailRespVO> pageResults = new PageResult<PurchaseRequirementDetailRespVO>();
        pageResults.setList(respVOList);
        pageResults.setTotal(pageResult.getTotal());

        return success(pageResults);
    }

    @GetMapping("/contractPage")
    @Operation(summary = "获得购销合同分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<PageResult<ContractRespVO>> getContractPage(@Valid ContractPageReqVO pageReqVO) {
        pageReqVO.setContractStatus(1);
        PageResult<ContractDO> pageResult = contractService.getContractPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }

        List<Long> selfContacts = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getSelfContact()));
        List<Long> purchaserIds = StringListUtils.stringListToLongList(convertList(pageResult.getList(), obj -> obj.getPurchaser()));
        List<String> contractIds = convertList(pageResult.getList(), obj -> obj.getId());
        List<Long> userIdList = new ArrayList<>();
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
        Map<String,BigDecimal> priceMap = new HashMap<>();//合同价格
        Map<String,BigDecimal> numberMap = new HashMap<>();//合同产品数量
        for (ContractOrderDO contractOrderDO : contractOrderDOS){
            BigDecimal price = contractOrderDO.getTaxPrice().multiply(contractOrderDO.getQuantity());
            if (priceMap.get(contractOrderDO.getContractId()) == null){
                priceMap.put(contractOrderDO.getContractId(), price);
            }else {
                BigDecimal a = priceMap.get(contractOrderDO.getContractId()).add(price);
                priceMap.put(contractOrderDO.getContractId(), a);
            }


            if (numberMap.get(contractOrderDO.getContractId()) == null){
                numberMap.put(contractOrderDO.getContractId(), contractOrderDO.getQuantity());
            }else {
                BigDecimal a = numberMap.get(contractOrderDO.getContractId()).add(contractOrderDO.getQuantity());
                numberMap.put(contractOrderDO.getContractId(), a);
            }
        }
        //收货单
        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoService.getConsignmentInfoByContractIds(contractIds);
        //退货单
        List<String> consignmentIds = consignmentInfoDOS.stream().map(ConsignmentInfoDO::getConsignmentId).distinct().collect(Collectors.toList());
        List<ShippingInfoDO> shippingInfoDOS = CollUtil.isEmpty(consignmentIds)?new ArrayList<>():shippingInfoService.getShippingInfoByConsignmentIds(consignmentIds);

        Map<String,BigDecimal> finishNumberMap = new HashMap<>();//合同收货产品数量
        if (!CollUtil.isEmpty(consignmentInfoDOS)){

            for (ConsignmentInfoDO consignmentInfoDO :consignmentInfoDOS){

                 BigDecimal cancelNumber = new BigDecimal(0);

                BigDecimal number = consignmentInfoDO.getConsignedAmount().subtract(cancelNumber);
                if (finishNumberMap.get(consignmentInfoDO.getContractId()) == null){
                    finishNumberMap.put(consignmentInfoDO.getContractId(), number);
                }else {
                    BigDecimal a = numberMap.get(consignmentInfoDO.getContractId()).add(number);

                    finishNumberMap.put(consignmentInfoDO.getContractId(), a);
                }
            }
        }

        if (!CollUtil.isEmpty(shippingInfoDOS)){
            for (ShippingInfoDO shippingInfoDO :shippingInfoDOS){
                BigDecimal cancelNumber = shippingInfoDO.getConsignedAmount();
                if (finishNumberMap.get(shippingInfoDO.getContractId()) != null){
                    BigDecimal sNumber = finishNumberMap.get(shippingInfoDO.getContractId()).subtract(cancelNumber);
                    finishNumberMap.put(shippingInfoDO.getContractId(), sNumber);
                }

            }
        }

        List<ContractPaymentDO> contractPaymentDOS = contractService.getContractPaymentListByContractIds(contractIds);
        Map<String,BigDecimal> finishPriceMap = new HashMap<>();//合同价格
        if (!CollUtil.isEmpty(contractPaymentDOS)){
            for (ContractPaymentDO contractPaymentDO : contractPaymentDOS){
                BigDecimal price = contractPaymentDO.getAmount();

                if (finishPriceMap.get(contractPaymentDO.getContractId()) == null){
                    finishPriceMap.put(contractPaymentDO.getContractId(), price);
                }else {
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

            BigDecimal finish = finishNumberMap.get(vo.getId()) == null?new BigDecimal(0):finishNumberMap.get(vo.getId());
            BigDecimal trueNumber = numberMap.get(vo.getId()) == null?new BigDecimal(0):numberMap.get(vo.getId());
            Boolean consignmentFinish = finish.compareTo(trueNumber) == 0;

            BigDecimal priceNumber = finishPriceMap.get(vo.getId()) == null?new BigDecimal(0):finishPriceMap.get(vo.getId());
            BigDecimal truePriceNumber = priceMap.get(vo.getId()) == null?new BigDecimal(0):priceMap.get(vo.getId());

            Boolean priceFinish = priceNumber.compareTo(truePriceNumber) == 0;

            if (consignmentFinish &&priceFinish){
                vo.setContractProgressStatus(4);
            }else if (consignmentFinish && !priceFinish){
                vo.setContractProgressStatus(3);
            }else if (!consignmentFinish && priceFinish){
                vo.setContractProgressStatus(2);
            }else if (!consignmentFinish && !priceFinish){
                vo.setContractProgressStatus(1);
            }


            if (ObjectUtil.isNotNull(vo.getProjectId())) {
                vo.setProjectName(finalProjectMap.get(vo.getProjectId()).getProjectName());
            }
        }));

//        return success(new PageResult<>(ContractConvert.INSTANCE.convertList(pageResult.getList(), userMap),
//                pageResult.getTotal()));
    }


    @GetMapping("/getContractDetailPage")
    @Operation(summary = "获得购销合同执行情况分页")
    @PreAuthorize("@ss.hasPermission('ppm:contract:query')")
    public CommonResult<PageResult<PurchaseContractExecutionResp>> getContractDetailPage(@Valid ContractPageReqVO pageReqVO) {
        pageReqVO.setContractStatus(1);
        List<ContractOrderDO> orderDOS = contractOrderService.getContractOrderPage(pageReqVO);
        if (CollUtil.isEmpty(orderDOS)) {
            return success(new PageResult<PurchaseContractExecutionResp>());
        }



        List<String> orderIds = orderDOS.stream().map(ContractOrderDO::getId).distinct().collect(Collectors.toList());

        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(
                convertSet(orderDOS, obj -> obj.getMaterialId()));


        //查询采购收货单
        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoService.getConsignmentInfoByOrderIds(orderIds);
        Map<String, BigDecimal> map = new HashMap<>();
        Map<String, List<String>> consignmentIdMap = new HashMap<>();

        for (ConsignmentInfoDO infoDO : consignmentInfoDOS) {

            if (map.get(infoDO.getOrderId()) == null) {
                map.put(infoDO.getOrderId(), infoDO.getSignedAmount());
            } else {
                BigDecimal s = map.get(infoDO.getOrderId()).add(infoDO.getSignedAmount());
                map.put(infoDO.getOrderId(), s);
            }

            if (consignmentIdMap.get(infoDO.getOrderId()) == null) {
                consignmentIdMap.put(infoDO.getOrderId(), Lists.newArrayList(infoDO.getConsignmentId()));
            } else {
                consignmentIdMap.get(infoDO.getOrderId()).add(infoDO.getConsignmentId());
            }

        }
        //退货单
        List<String> consignmentIds = consignmentInfoDOS.stream().map(ConsignmentInfoDO::getConsignmentId).distinct().collect(Collectors.toList());

        List<ShippingInfoDO> shippingInfoDOS = shippingInfoService.getShippingInfoByConsignmentIds(consignmentIds);
        LocalDateTime dateTime = LocalDateTime.now();

        List<PurchaseContractExecutionResp> resps = new ArrayList<>();
        for (ContractOrderDO orderDO : orderDOS) {

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

            List<ShippingInfoDO> shippingInfoDOList = shippingInfoDOS.stream().filter(shippingInfoDO -> consignmentIdList.contains(shippingInfoDO.getConsignmentId()) && shippingInfoDO.getMaterialConfigId().equals(orderDO.getMaterialId())).collect(Collectors.toList());

            if (!CollectionUtils.isAnyEmpty(shippingInfoDOList)) {
                BigDecimal returnAmount = new BigDecimal(0);
                for (ShippingInfoDO shippingInfoDO : shippingInfoDOList) {
                    returnAmount = returnAmount.add(shippingInfoDO.getConsignedAmount());
                }

                finishAmount = finishAmount.subtract(returnAmount);
            }
            resp.setFinishQuantity(finishAmount);
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

        PageResult<PurchaseContractExecutionResp> pageResult = new PageResult();
        int count = resps.size(); // 总记录数
        // 计算总页数
        int pages = count % pageReqVO.getPageSize() == 0 ? count / pageReqVO.getPageSize() : count / pageReqVO.getPageSize() + 1;
        // 起始位置
        int start = pageReqVO.getPageNo() <= 0 ? 0 : (pageReqVO.getPageNo() > pages ? (pages - 1) * pageReqVO.getPageSize() : (pageReqVO.getPageNo() - 1) * pageReqVO.getPageSize());
        // 终止位置
        int end = pageReqVO.getPageNo() <= 0 ? (pageReqVO.getPageSize() <= count ? pageReqVO.getPageSize() : count) : (pageReqVO.getPageSize() * pageReqVO.getPageNo() <= count ? pageReqVO.getPageSize() * pageReqVO.getPageNo() : count);

        pageResult.setTotal((long)count);
        pageResult.setList(resps.subList(start, end));


        return success(pageResult);

    }


    @GetMapping("/purchaseConsignmentPage")
    @Operation(summary = "获得采购收货分页")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<PageResult<PurchaseConsignmentRespVO>> getPurchaseConsignmentPage(@Valid PurchaseConsignmentPageReqVO pageReqVO) {
        pageReqVO.setConsignmentTypes(Lists.newArrayList(1, 2));
        pageReqVO.setConsignmentStatuss(Lists.newArrayList(0, 1, 2, 3, 9, 10));
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


    @GetMapping("/getCompanyReturnRate")
    @Operation(summary = "获取客户退货率")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<ConsignmentCompanyNumberRespVO>> getCompanyReturnRate(@RequestParam("createTimeRange[0]") String createTimeRange0,
                                                                                   @RequestParam("createTimeRange[1]") String createTimeRange1) {


        LocalDateTime[] createTimeRange = new LocalDateTime[]{
                LocalDateTime.parse(createTimeRange0, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)),
                LocalDateTime.parse(createTimeRange1, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND))};

        List<ConsignmentCompanyNumberRespVO> finalList = new ArrayList<>();

        //获取所有完成的收货单
        List<ConsignmentCompanyNumberRespVO> respVOList = consignmentInfoService.getCompanyConsignmentAmount(createTimeRange);

        //获取退货单
        List<ConsignmentCompanyNumberRespVO> returnList = shippingInfoService.getConsignmentCompanyReturnNumber(createTimeRange);

        Map<String, ConsignmentCompanyNumberRespVO> companyNumberRespVOMap = convertMap(returnList, ConsignmentCompanyNumberRespVO::getCompanyId);

        if (!CollectionUtils.isAnyEmpty(respVOList)) {

            for (ConsignmentCompanyNumberRespVO respVO : respVOList) {

                ConsignmentCompanyNumberRespVO returnResp = companyNumberRespVOMap.get(respVO.getCompanyId());
                if (returnResp != null) {

                    respVO.setReturnAmount(returnResp.getSignedAmount());

                    respVO.setRate(returnResp.getSignedAmount().divide(respVO.getSignedAmount()));
                } else {
                    respVO.setReturnAmount(new BigDecimal(0));
                    respVO.setRate(new BigDecimal(0));
                }

                finalList.add(respVO);
            }
        }

        return success(finalList);
    }

    @GetMapping("/getConsignmentQmsRate")
    @Operation(summary = "获取采购质检合格率")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment:query')")
    public CommonResult<List<ConsignmentContractQmsNumberRespVO>> getConsignmentQmsRate(
            @RequestParam("createTimeRange[0]") String createTimeRange0,
            @RequestParam("createTimeRange[1]") String createTimeRange1
    ) {
        //获取创建的采购单
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime before = now.minus(30, ChronoUnit.DAYS);

        LocalDateTime[] createTimeRange = new LocalDateTime[]{
                LocalDateTime.parse(createTimeRange0, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)),
                LocalDateTime.parse(createTimeRange1, DateTimeFormatter.ofPattern(FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND))};



        //获取采购数量
        ConsignmentInfoQueryReqVO reqVO = new ConsignmentInfoQueryReqVO();
        reqVO.setBeginTime(createTimeRange[0]);
        reqVO.setEndTime(createTimeRange[1]);
        reqVO.setCreator(getLoginUserId().toString());
        reqVO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
        reqVO.setConsignmentType(Lists.newArrayList(ConsignmentTypeEnum.OUT.getStatus(), ConsignmentTypeEnum.PURCHASE.getStatus()));
        List<ConsignmentDO> consignmentInfoDOS = purchaseConsignmentService.getConsignments(reqVO);

        List<String> nos = consignmentInfoDOS.stream().map(ConsignmentDO::getNo).distinct().collect(Collectors.toList());

        CommonResult<List<InspectionSheetSchemeMaterialRespDTO>> commonResult = null;
        try {
            commonResult = inspectionSheetApi.getInspectionMaterialListByRecordNumber(nos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<String, Integer> allMap = new HashMap<>();//总数量
        Map<String, Integer> unQuantityMap = new HashMap<>();//不合格数量

        if (commonResult !=null &&commonResult.isSuccess() && commonResult.getCheckedData().size()>0){

            for (InspectionSheetSchemeMaterialRespDTO respDTO :commonResult.getCheckedData()){
                String key = respDTO.getRecordNumber();
                if (allMap.get(key) == null){
                    allMap.put(key,1);
                }else {
                    allMap.put(key, allMap.get(key)+1);
                }
                if (respDTO.getInspectionResult().intValue() ==2){
                    if (unQuantityMap.get(key) == null){
                        unQuantityMap.put(key,1);
                    }else {
                        unQuantityMap.put(key, unQuantityMap.get(key)+1);
                    }
                }

            }

        }
        List<ConsignmentContractQmsNumberRespVO> respVOList = new ArrayList<>();
        for (ConsignmentDO infoDO : consignmentInfoDOS) {
            ConsignmentContractQmsNumberRespVO consignmentContractQmsNumberRespVO = new ConsignmentContractQmsNumberRespVO();
            String key = infoDO.getNo();
            if (allMap.get(key) != null){
                consignmentContractQmsNumberRespVO.setCheckAmount(new BigDecimal(allMap.get(key)));
            }else {
                consignmentContractQmsNumberRespVO.setCheckAmount(new BigDecimal(0));
                consignmentContractQmsNumberRespVO.setRate(new BigDecimal(0));
            }
            if (unQuantityMap.get(key) != null){
                consignmentContractQmsNumberRespVO.setUnQuantityAmount(new BigDecimal(unQuantityMap.get(key)));
            }else {
                consignmentContractQmsNumberRespVO.setUnQuantityAmount(new BigDecimal(0));

            }

            if (consignmentContractQmsNumberRespVO.getCheckAmount().compareTo(new BigDecimal(0)) != 0){
                consignmentContractQmsNumberRespVO.setRate(consignmentContractQmsNumberRespVO.getUnQuantityAmount().divide(consignmentContractQmsNumberRespVO.getCheckAmount()));
            }
            consignmentContractQmsNumberRespVO.setNo(infoDO.getNo());
            respVOList.add(consignmentContractQmsNumberRespVO);
        }

        return success(respVOList);
    }


    @GetMapping("/getConsignmentReturnPage")
    @Operation(summary = "获得采购退货单分页")
    @PreAuthorize("@ss.hasPermission('ppm:consignment-return:query')")
    public CommonResult<PageResult<ShippingRespVO>> getConsignmentReturnPage(@Valid ShippingPageReqVO pageReqVO) {
        pageReqVO.setShippingType(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus());
        pageReqVO.setShippingStatuss(Lists.newArrayList(0,1,2,3,4,8));
        PageResult<ShippingDO> pageResult = shippingService.getShippingPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        //用户
        List<Long> userIdList = com.miyu.module.ppm.utils.StringListUtils.stringListToLongList(convertList(pageResult.getList(), ShippingDO::getConsigner));
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



    @GetMapping("/getInboundExceptionHandlingPage")
    @Operation(summary = "获得入库异常处理分页")
    @PreAuthorize("@ss.hasPermission('ppm:inbound-exception-handling:query')")
    public CommonResult<PageResult<InboundExceptionHandlingRespVO>> getInboundExceptionHandlingPage(@Valid InboundExceptionHandlingPageReqVO pageReqVO) {
        pageReqVO.setStatus(0);
        PageResult<InboundExceptionHandlingDO> pageResult = inboundExceptionHandlingService.getInboundExceptionHandlingPage(pageReqVO);
        if (CollUtil.isEmpty(pageResult.getList())) {
            return success(new PageResult<>(pageResult.getTotal()));
        }
        List<InboundExceptionHandlingDO> dos = pageResult.getList();
        List<String> contractIdList = convertList(pageResult.getList(), InboundExceptionHandlingDO::getContractId);
        contractIdList = contractIdList.stream().distinct().collect(Collectors.toList());



        List<String> projectIds = pageResult.getList().stream().map(InboundExceptionHandlingDO::getProjectId).distinct().collect(Collectors.toList());
        Map<String, PmsApprovalDto> pmsApprovalDtoMap = null;
        try {
            pmsApprovalDtoMap = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {
            log.error("调用PMS失败");
        }
        // 拼接数据 用户信息
        //合同信息
        List<ContractDO> contractRespDTOS = contractService.getContractListByIds(contractIdList);
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);

        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractDO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());

        List<CompanyDO> companyList = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyRespDTOMap =CollectionUtils.convertMap(companyList, CompanyDO::getId);


        return success(new PageResult<>(ExceptionHandlingConvert.INSTANCE.convertList(pageResult.getList(), contractMap, companyRespDTOMap,pmsApprovalDtoMap),
                pageResult.getTotal()));
    }


}