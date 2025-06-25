package cn.iocoder.yudao.module.pms.controller.admin.pmsapproval;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsOrderApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderListDTO;
import cn.iocoder.yudao.module.pms.api.pms.orderDto.PmsOrderRespDTO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderProcessVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.*;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.pms.enums.ProjectStatusEnum;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.googlecode.aviator.utils.ArrayHashMap;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDetailDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.PaymentApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
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

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;


@Tag(name = "管理后台 - pms 立项表,项目立项相关")
@RestController
@RequestMapping("/pms/approval")
@Validated
public class PmsApprovalController {

    @Resource
    private PmsApprovalService approvalService;

    @Resource
    private PmsOrderService orderService;

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private ContractApi contractApi;

    @Resource
    private CompanyApi companyApi;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    @Resource
    private ConsignmentReturnApi consignmentReturnApi;

    @Resource
    private PmsOrderApi orderApi;

    @Resource
    private PaymentApi paymentApi;

    @PostMapping("/create")
    @Operation(summary = "创建pms 立项表,项目立项相关")
    @PreAuthorize("@ss.hasPermission('pms:approval:create')")
    public CommonResult<String> createApproval(@Valid @RequestBody PmsApprovalSaveReqVO createReqVO) {
        return success(approvalService.createApproval(createReqVO));
    }

    /**
     * 带订单创建,现在项目订单一起创建，一个项目一个订单
     * 自研的订单新建,外部的订单更新
     * @param createReqVO
     * @return
     */
    @PostMapping("/createWithOrder")
    @Operation(summary = "创建pms 立项表,项目立项相关")
    @PreAuthorize("@ss.hasPermission('pms:approval:create')")
    public CommonResult<String> createApprovalWithOrder(@Valid @RequestBody PmsApprovalSaveReqVO createReqVO) {
        return success(approvalService.createApprovalWithOrder(createReqVO));
    }

    @PostMapping("/createBpm")
    @Operation(summary = "创建pms 立项表,项目立项相关")
    @PreAuthorize("@ss.hasPermission('pms:approval:create')")
    public CommonResult<String> createApprovalBpm(@Valid @RequestBody PmsApprovalSaveReqVO createReqVO) {
        return success(approvalService.createApprovalBpm(createReqVO));
    }

    /**
     * 简化版,没有二次填写
     * @param createReqVO
     * @return
     */
    @PostMapping("/createBpm2")
    @Operation(summary = "创建pms 立项表,项目立项相关")
    @PreAuthorize("@ss.hasPermission('pms:approval:create')")
    public CommonResult<String> createApprovalBpm2(@Valid @RequestBody PmsApprovalSaveReqVO createReqVO) {
        return success(approvalService.createApprovalBpm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新pms 立项表,项目立项相关")
    @PreAuthorize("@ss.hasPermission('pms:approval:update')")
    public CommonResult<Boolean> updateApproval(@Valid @RequestBody PmsApprovalSaveReqVO updateReqVO) {
        approvalService.updateApproval(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除pms 立项表,项目立项相关")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:approval:delete')")
    public CommonResult<Boolean> deleteApproval(@RequestParam("id") String id) {
        approvalService.deleteApproval(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得pms 立项表,项目立项相关")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<PmsApprovalRespVO> getApproval(@RequestParam("id") String id) {
        PmsApprovalDO approval = approvalService.getApproval(id);
        List<PmsOrderDO> orderList = approvalService.getOrderListByProjectId(id);
        approval.setOrderList(orderList);
        return success(BeanUtils.toBean(approval, PmsApprovalRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得pms 立项表,项目立项相关分页")
    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<PageResult<PmsApprovalRespVO>> getApprovalPage(@Valid PmsApprovalPageReqVO pageReqVO) {
        PageResult<PmsApprovalDO> pageResult = approvalService.getApprovalPage(pageReqVO);
        List<String> projectIds = pageResult.getList().stream().map(PmsApprovalDO::getId).collect(Collectors.toList());
        List<PmsOrderDO> pmsOrderList = approvalService.getPmsOrderList(projectIds);
        //项目id为key，里面是订单集合
        Map<String,List<PmsOrderDO>> orderMap = new HashMap<>();
        for (PmsOrderDO pmsOrderDO : pmsOrderList) {
            if(orderMap.containsKey(pmsOrderDO.getProjectId())){
                orderMap.get(pmsOrderDO.getProjectId()).add(pmsOrderDO);
            }else {
                List<PmsOrderDO> list = new ArrayList<>();
                list.add(pmsOrderDO);
                orderMap.put(pmsOrderDO.getProjectId(),list);
            }
        }

        List<PmsPlanDO> pmsPlanList = approvalService.getPmsPlanList(projectIds);
        //项目id为key，里面是订单集合
        Map<String,List<PmsPlanDO>> planMap = new HashMap<>();
        for (PmsPlanDO pmsPlanDO : pmsPlanList) {
            if(planMap.containsKey(pmsPlanDO.getProjectId())){
                planMap.get(pmsPlanDO.getProjectId()).add(pmsPlanDO);
            }else {
                List<PmsPlanDO> list = new ArrayList<>();
                list.add(pmsPlanDO);
                planMap.put(pmsPlanDO.getProjectId(),list);
            }
        }

        //这段是用户id回显
        //项目负责人
        List<Long> collect = pageResult.getList().stream().filter(val -> ObjectUtil.isNotNull(val.getResponsiblePerson())).map(val -> {
            return val.getResponsiblePerson();
        }).collect(Collectors.toList());
        //项目经理
        List<Long> collect2 = pageResult.getList().stream().filter(val -> ObjectUtil.isNotNull(val.getProjectManager())).map(val -> {
            return val.getProjectManager();
        }).collect(Collectors.toList());
        List<Long> userIdList = new ArrayList<>();
        //合并用户集合
        if (collect != null){userIdList.addAll(collect);}
        if (collect2!= null){userIdList.addAll(collect2);}
        userIdList = userIdList.stream().distinct().collect(Collectors.toList());
        // 拼接数据
        Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);

        //项目来源
        List<String> clientList = pageResult.getList().stream().filter(val -> ObjectUtil.isNotNull(val.getProjectClient())).map(val -> val.getProjectClient()).collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(clientList);
//        Map<String, CompanyRespDTO> companyMap = new HashMap<>();
//        try {
//            Map<String, CompanyRespDTO> companyMap1 = companyApi.getCompanyMap(clientList);
//            for (Map.Entry<String, CompanyRespDTO> entry : companyMap1.entrySet()) {
//                companyMap.put(entry.getKey(),entry.getValue());
//            }
//
//
//        }catch (Exception e){
//            e.printStackTrace();
//        }

        return success(BeanUtils.toBean(pageResult, PmsApprovalRespVO.class,vo-> {
            if(ObjectUtil.isNotNull(vo.getResponsiblePerson())&&userMap.containsKey(vo.getResponsiblePerson())){
                vo.setResponsiblePersonName(userMap.get(vo.getResponsiblePerson()).getNickname());
            }
            if(ObjectUtil.isNotNull(vo.getProjectManager())&&userMap.containsKey(vo.getProjectManager())){
                vo.setProjectManagerName(userMap.get(vo.getProjectManager()).getNickname());
            }
            if(ObjectUtil.isNotNull(vo.getProjectClient())&&companyMap.containsKey(vo.getProjectClient())){
                //设置公司名(项目来源)
                vo.setProjectClientName(companyMap.get(vo.getProjectClient()).getName());
            }
            if(orderMap.containsKey(vo.getId())){
                vo.setOrderList(orderMap.get(vo.getId()));
            }else {
                vo.setOrderList(new ArrayList<PmsOrderDO>());
            }

            if(planMap.containsKey(vo.getId())){
                vo.setPlanList(planMap.get(vo.getId()));
            }else {
                vo.setPlanList(new ArrayList<PmsPlanDO>());
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出pms 立项表,项目立项相关 Excel")
    @PreAuthorize("@ss.hasPermission('pms:approval:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportApprovalExcel(@Valid PmsApprovalPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<PmsApprovalDO> list = approvalService.getApprovalPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "pms 立项表,项目立项相关.xls", "数据", PmsApprovalRespVO.class,
                        BeanUtils.toBean(list, PmsApprovalRespVO.class));
    }


    @GetMapping("/all")
    @Operation(summary = "获取列表", description = "主要用于前端的下拉选项")
    public CommonResult<List<PmsApprovalRespVO>> getAllList() {
        List<PmsApprovalDO> approvalAll = approvalService.getApprovalAll();
        return success(BeanUtils.toBean(approvalAll, PmsApprovalRespVO.class));
    }

    //我应该不用再获取合同了，我不用选择合同
//    @GetMapping("/getContractListAll")
//    @Operation(summary = "获取合同列表", description = "主要用于前端的下拉选项")
//    public CommonResult<List<ContractRespDTO>> getContractListAll() {
//        List<ContractRespDTO> checkedData = contractApi.getContractListAll().getCheckedData();
//        return success(BeanUtils.toBean(checkedData, ContractRespDTO.class));
//    }

    /**
     * 项目结算关闭
     * @param updateReqVO
     * @return
     */
    @PutMapping("/projectClose")
    @Operation(summary = "更新pms 立项表,项目立项相关")
    @PreAuthorize("@ss.hasPermission('pms:approval:update')")
    public CommonResult<Boolean> projectClose(@Valid @RequestBody PmsApprovalSaveReqVO updateReqVO) {
        updateReqVO.setProjectStatus(ProjectStatusEnum.Finish.getStatus());
        approvalService.closeApproval(updateReqVO);
        return success(true);
    }

    @GetMapping("/getByProjectCode")
    @Operation(summary = "通过项目编码获得项目,校验用")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<PmsApprovalRespVO> getByProjectCode(@RequestParam("code") String code) {
        PmsApprovalDO approval = approvalService.getByProjectCode(code);
        return success(BeanUtils.toBean(approval, PmsApprovalRespVO.class));
    }

    @GetMapping("/getByStatus")
    @Operation(summary = "通过项目编码获得项目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<List<PmsApprovalRespVO>> getByStatus(@RequestParam("status") List<Integer> status) {
        List<PmsApprovalDO> list = approvalService.getByStatus(status);
        return success(BeanUtils.toBean(list, PmsApprovalRespVO.class));
    }

    @GetMapping("/getByProjectStatus")
    @Operation(summary = "通过项目状态获得项目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<List<PmsApprovalRespVO>> getByProjectStatus(@RequestParam("status") List<Integer> status) {
        List<PmsApprovalDO> list = approvalService.getByProjectStatus(status);
        return success(BeanUtils.toBean(list, PmsApprovalRespVO.class));
    }

//    @GetMapping("/getPurchaseDetail")
//    @Operation(summary = "通过项目id获得采购明细,项目执行采购明细用")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    public CommonResult<List<ProjectPurchaseDetailRespVO>> getPurchaseDetail(@RequestParam("id") String id) {
//        List<PmsOrderDO> PmsOrderDOList = approvalService.getOrderListByProjectId(id);
//        PmsOrderDO pmsOrderDO = new PmsOrderDO();
//        if(PmsOrderDOList.size()>0){
//            pmsOrderDO = approvalService.getOrderListByProjectId(id).get(0);
//        }else {
//            return success(null);
//        }
//        String contractId = pmsOrderDO.getContractId();
//        List<String> ids = new ArrayList<>();
//        ids.add(contractId);
//        ContractRespDTO contract = contractApi.getContractMap(ids).get(contractId);
//        List<ContractOrderRespDTO> contractOrder = contractApi.getOrderList(contractId).getCheckedData();
//
//        return success(BeanUtils.toBean(contractOrder,ProjectPurchaseDetailRespVO.class,vo->{
//            //币种
//            vo.setCurrency(contract.getCurrency());
//            //采购日期(用的签约日期)
//            vo.setPurchaseTime(contract.getSigningDate());
//
//        }));
//    }

    /**
     * 返回的是每个合同的明细
     * @param
     * @return
     */
    //整改完将这部分移除
//    @GetMapping("/getPurchaseDetail")
//    @Operation(summary = "通过项目id获得采购明细,项目执行采购明细用")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    public CommonResult<List<ProjectPurchaseDetailRespVO>> getPurchaseDetail(@RequestParam("id") String id) {
//        //获取该项目下的订单
//        List<PmsOrderDO> pmsOrderDOList = approvalService.getOrderListByProjectId(id);
//        //合同id集合
//        List<String> ids = pmsOrderDOList.stream().filter(item -> ObjectUtil.isNotNull(item.getContractId())).map(item -> item.getContractId()).collect(Collectors.toList());
//        if(ObjectUtil.isNull(ids)||ids.size()==0){
//            List<ProjectPurchaseDetailRespVO> list = new ArrayList<>();
//            return success(list);
//        }
//        //合同集合
//        List<ContractRespDTO> contractRespDTO = contractApi.getContractList(ids).getCheckedData();
//        Map<String, ContractRespDTO> contractMap = contractApi.getContractMap(ids);
//
//        //合同付款
//        Map<String, ContractPaymentDTO> paymentMap = paymentApi.getPaymentMapByContractIds(ids);
//
//
//        //收货详情
//        List<PurchaseConsignmentDTO> purchaseConsignmentDTO = purchaseConsignmentApi.getConsignmentDetailByContractIds(ids).getCheckedData();
//        //key是合同id,值是采购明细
//        Map<String, List<PurchaseConsignmentDetailDTO>> consignmentMap = new HashMap<>();
//        //收货发货单遍历,以合同为key,存储发货详情
//        for (PurchaseConsignmentDTO consignmentDTO : purchaseConsignmentDTO) {
//            if(consignmentMap.containsKey(consignmentDTO.getContractId())){
//                consignmentMap.get(consignmentDTO.getContractId()).addAll(consignmentDTO.getDetailDTOList());
//            }else {
//                consignmentMap.put(consignmentDTO.getContractId(),consignmentDTO.getDetailDTOList());
//            }
//
//        }
//
////        Map<String, PurchaseConsignmentDTO> consignmentMap = purchaseConsignmentApi.getConsignmentMapByContractIds(ids);
////        System.out.println(consignmentMap);
//        //退货详情
//        //退货
//        List<ConsignmentReturnDTO> returnList = consignmentReturnApi.getConsignmentReturnDetailByContractIds(ids).getCheckedData();
//        List<ConsignmentReturnDetailDTO> returnDetailList = new ArrayList<>();
//        for (ConsignmentReturnDTO consignmentReturnDTO : returnList) {
//            if(consignmentReturnDTO.getReturnDetailDTOList()!=null&&consignmentReturnDTO.getReturnDetailDTOList().size()>0){
//                returnDetailList.addAll(consignmentReturnDTO.getReturnDetailDTOList());
//            }
//        }
//        //存储退货单-退货数量(详细)，key用的采购单详细
//        Map<String, BigDecimal> returnDetailMap = new HashMap<>();
//        Map<String, BigDecimal> returnDetailDTOMapBigDecimal = new HashMap<>();
//        //过滤无效退货单
//        List<ConsignmentReturnDetailDTO> returnDetailDTOList = returnDetailList.stream().filter(item -> ObjectUtil.isNotNull(item.getSignedAmount())).collect(Collectors.toList());
//        //这样每个详细采购单的退货数量就计算出来了
//        for (ConsignmentReturnDetailDTO consignmentReturnDetailDTO : returnDetailDTOList) {
//            if(returnDetailMap.containsKey(consignmentReturnDetailDTO.getConsignmentDetailId())){
//                BigDecimal add = returnDetailMap.get(consignmentReturnDetailDTO.getConsignmentDetailId()).add(consignmentReturnDetailDTO.getSignedAmount());
//                returnDetailMap.put(consignmentReturnDetailDTO.getConsignmentDetailId(),add);
//            }else {
//                returnDetailMap.put(consignmentReturnDetailDTO.getConsignmentDetailId(),consignmentReturnDetailDTO.getSignedAmount());
//            }
//        }
//
//
//
//        //采购详情,要返回的值
//        List<ProjectPurchaseDetailRespVO> purchaseDetail = BeanUtils.toBean(pmsOrderDOList, ProjectPurchaseDetailRespVO.class);
//        //设置各种属性
//        for (ProjectPurchaseDetailRespVO purchase : purchaseDetail) {
//            List<ContractOrderRespDTO> orderList = contractApi.getOrderList(purchase.getContractId()).getCheckedData();
//            //这个key用的是合同订单表的id
//            Map<String, ContractOrderRespDTO> orderMap = CollectionUtils.convertMap(orderList, ContractOrderRespDTO::getId);
//            //签收(入库)
//            BigDecimal signAmount = new BigDecimal("0");
//            //入库总额
//            BigDecimal signTotalPrice = new BigDecimal("0");
//
//            //收货详情
//            //PurchaseConsignmentDTO purchaseConsignmentDTO = consignmentMap.get(purchase.getContractId());
//            if(ObjectUtil.isNotNull(purchaseConsignmentDTO)){
//                //采购单每条具体记录
//                //List<PurchaseConsignmentDetailDTO> detailList = purchaseConsignmentDTO.getDetailDTOList().stream().filter(item -> ObjectUtil.isNotNull(item.getSignedAmount())).collect(Collectors.toList());
//                List<PurchaseConsignmentDetailDTO> detailList = new ArrayList<>();
//                if(consignmentMap.containsKey(purchase.getContractId())){
//                    detailList = consignmentMap.get(purchase.getContractId()).stream().filter(item -> ObjectUtil.isNotNull(item.getSignedAmount())).collect(Collectors.toList());
//                }
////                List<PurchaseConsignmentDetailDTO> detailList = consignmentMap.get(purchase.getContractId()).stream().filter(item -> ObjectUtil.isNotNull(item.getSignedAmount())).collect(Collectors.toList());
//                for (PurchaseConsignmentDetailDTO purchaseConsignmentDetailDTO : detailList) {
//                    //拿到退货单退货数量
//                    BigDecimal returnAmount = new BigDecimal("0");
//                    if(returnDetailMap.containsKey(purchaseConsignmentDetailDTO.getId())){
//                        returnAmount = returnDetailMap.get(purchaseConsignmentDetailDTO.getId());
//                    }
//
////                    signAmount =  signAmount.add(purchaseConsignmentDetailDTO.getSignedAmount());
//                    //每个发货单详情减去所有对应的退货单详情，发货单详情只有一个，但是对应的退货单详情可能有多个
//                    signAmount =  signAmount.add(purchaseConsignmentDetailDTO.getSignedAmount()).subtract(returnAmount);
//                    //每个收货详情的收货额，加起来就是收货总额,之后还要减去退货总额
////                    BigDecimal signPrice = orderMap.get(purchaseConsignmentDetailDTO.getOrderId()).getTaxPrice().multiply(purchaseConsignmentDetailDTO.getSignedAmount());
//                    BigDecimal signPrice = orderMap.get(purchaseConsignmentDetailDTO.getOrderId()).getTaxPrice().multiply(purchaseConsignmentDetailDTO.getSignedAmount().subtract(returnAmount));
//                    signTotalPrice = signTotalPrice.add(signPrice);
//
//                }
//            }
//
////            List<ContractOrderRespDTO> orderList = contractApi.getOrderList(purchase.getContractId()).getCheckedData();
//            //数量
//            BigDecimal quantity = new BigDecimal("0");
//            //总额
//            BigDecimal total = new BigDecimal("0");
//            //含税总额
//            BigDecimal taxtotal = new BigDecimal("0");
//            for (ContractOrderRespDTO order : orderList) {
//                quantity = quantity.add(order.getQuantity());
//                total = total.add(order.getPrice().multiply(order.getQuantity()));
//                taxtotal = taxtotal.add(order.getTaxPrice().multiply(order.getQuantity()));
//            }
//            //每个合同的付款
//            ContractPaymentDTO contractPaymentDTO = paymentMap.get(purchase.getContractId());
//            BigDecimal payment = new BigDecimal("0");
//            if(ObjectUtil.isNotNull(contractPaymentDTO)){
//                payment = contractPaymentDTO.getPaymentDetailDTOList().stream().filter(item -> ObjectUtil.isNotNull(item.getAmount())).map(item -> item.getAmount()).reduce(payment, (a, b) -> a.add(b));
//            }
//
//            purchase.setQuantity(quantity);
//            purchase.setTotalPrice(total);
//            purchase.setTaxTotalPrice(taxtotal);
//            purchase.setSignAmount(signAmount);
//            purchase.setRemainAmount(quantity.subtract(signAmount));
//            purchase.setSignTotalPrice(signTotalPrice);
//            purchase.setUnSignTotalPrice(taxtotal.subtract(signTotalPrice));
//            purchase.setPayment(payment);
//            purchase.setRemainPayment(taxtotal.subtract(payment));
//            if(ObjectUtil.isNotNull(contractMap.get(purchase.getContractId()))){
//                //采购日期,签约日期
//                purchase.setPurchaseTime(contractMap.get(purchase.getContractId()).getSigningDate());
//                //币种
//                purchase.setCurrency(contractMap.get(purchase.getContractId()).getCurrency());
//            }
//
//        }
//
////        //通过合同id拿到合同
////        for (ContractRespDTO respDTO : contractRespDTO) {
////            List<ContractOrderRespDTO> contractOrder = contractApi.getOrderList(respDTO.getId()).getCheckedData();
////
////        }
//
//        return success(purchaseDetail);
//    }

    /**
     * 工艺进度(完成就是100%,没完成按天加)
     * 采购进读(按采购计划来,项目下的所有采购计划)
     * 生产进度(一起算)
     */
    @GetMapping("/projectProgress")
    @Operation(summary = "项目总体进度")
    public CommonResult<List<PmsApprovalProcessVO>> projectProgress(){
        List<PmsApprovalProcessVO> pmsApprovalProcessVOS = approvalService.projectProgress();
        return success(pmsApprovalProcessVOS);
    }

    /**
     * 工艺进度(完成就是100%,没完成按天加)
     * 采购进读
     * 生产进度
     */
    @GetMapping("/orderProgress")
    @Operation(summary = "项目总体进度")
    public CommonResult<List<OrderProcessVO>> orderProgress(){
        List<OrderProcessVO> orderProcessVOS = approvalService.orderProgress();
        return success(orderProcessVOS);
    }



    // ==================== 子表（项目评审） ====================

    @GetMapping("/assessment/list-by-project-id")
    @Operation(summary = "获得项目评审列表")
    @Parameter(name = "projectId", description = "项目id")
    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<List<AssessmentDO>> getAssessmentListByProjectId(@RequestParam("projectId") String projectId) {
        return success(approvalService.getAssessmentListByProjectId(projectId));
    }

    // ==================== 子表（项目订单） ====================

    @GetMapping("/order/list-by-project-id")
    @Operation(summary = "获得项目订单列表")
    @Parameter(name = "projectId", description = "项目id")
    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<List<PmsOrderDO>> getOrderListByProjectId(@RequestParam("projectId") String projectId) {
        return success(approvalService.getOrderListByProjectId(projectId));
    }

}
