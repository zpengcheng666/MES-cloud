package cn.iocoder.yudao.module.pms.controller.admin.delivery;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.controller.admin.delivery.vo.PmsOrderShippingRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.delivery.vo.PmsPlanPurchaseRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.delivery.vo.PmsShippingRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.delivery.vo.PurchaseConsignmentRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanPurchaseMaterialDO;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.checkerframework.checker.units.qual.A;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 项目交付")
@RestController
@RequestMapping("/pms/delivery")
@Validated
public class DeliveryController {

    @Resource
    private ShippingApi shippingApi;

    @Resource
    private ShippingReturnApi shippingReturnApi;

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    @Resource
    private ConsignmentReturnApi consignmentReturnApi;

    @Resource
    private PmsApprovalService approvalService;

    @Resource
    private ContractApi contractApi;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private DeptApi deptApi;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private PmsPlanService pmsPlanService;

    /** 即将不使用的 */
    @GetMapping("getShippingDetailByProjectId")
    @Operation(summary = "根据项目id获取销售发货单及明细")
    public CommonResult<List<PmsShippingRespVO>> getShippingDetailByProjectId(@RequestParam("projectIds")List<String> projectIds){
        List<ShippingDTO> shippingList = shippingApi.getShippingListByProjectIds(projectIds).getCheckedData();

        List<Long> userIds = shippingList.stream().filter(item -> ObjectUtil.isNotNull(item.getConsigner())).map(item->Long.valueOf(item.getConsigner())).collect(Collectors.toList());
        List<AdminUserRespDTO> adminList = adminUserApi.getUserList(userIds).getCheckedData();
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(adminList, AdminUserRespDTO::getId);

        List<Long> deptIds = adminList.stream().filter(item -> ObjectUtil.isNotNull(item.getDeptId())).map(AdminUserRespDTO::getDeptId).collect(Collectors.toList());
        List<DeptRespDTO> deptList = deptApi.getDeptList(deptIds).getCheckedData();
        Map<Long, DeptRespDTO> deptMap = CollectionUtils.convertMap(deptList, DeptRespDTO::getId);

        List<String> companyIds = shippingList.stream().filter(item -> ObjectUtil.isNotNull(item.getCompanyId())).map(ShippingDTO::getCompanyId).distinct().collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(companyIds);


        List<PmsShippingRespVO> pmsShippingRespVOS = BeanUtils.toBean(shippingList, PmsShippingRespVO.class,vo->{
            if(ObjectUtil.isNotNull(vo.getConsigner())&&userMap.containsKey(Long.valueOf(vo.getConsigner()))){
                vo.setConsignerName(userMap.get(Long.valueOf(vo.getConsigner())).getNickname());
            }
            if(ObjectUtil.isNotNull(vo.getConsigner())&&userMap.containsKey(Long.valueOf(vo.getConsigner()))){
                vo.setConsignerDeptId(userMap.get(Long.valueOf(vo.getConsigner())).getDeptId());
            }
            if(ObjectUtil.isNotNull(vo.getConsignerDeptId())&&deptMap.containsKey(vo.getConsignerDeptId())){
                vo.setConsignerDeptName(deptMap.get(vo.getConsignerDeptId()).getName());
            }
            if(ObjectUtil.isNotNull(vo.getCompanyId())&&companyMap.containsKey(vo.getCompanyId())){
                vo.setCompanyName(companyMap.get(vo.getCompanyId()).getName());
            }
        });
        return success(pmsShippingRespVOS);
    }

    /**
     * 获取订单发货数量，主体是订单,根据项目id查发货
     * @param projectIds
     */
    @GetMapping("getShippingOrderByProjectId")
    @Operation(summary = "根据项目id获取订单发货数量")
    public CommonResult<List<PmsOrderShippingRespVO>> getShippingOrderByProjectId(@RequestParam("projectIds")List<String> projectIds){
        List<PmsOrderDO> pmsOrderList = approvalService.getPmsOrderList(projectIds);
        List<ShippingDetailDTO> shippingDetailListTemp = shippingApi.getShippingDetailListByProjectIds(projectIds).getCheckedData();
        //发货明细
        List<ShippingDetailDTO> shippingDetailList = shippingDetailListTemp.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getShippingStatus()) && (item.getShippingStatus() == 5)&&(item.getShippingType()==1);
        }).collect(Collectors.toList());
        //退货明细
        List<String> shippingDetailIds = shippingDetailList.stream().map(ShippingDetailDTO::getId).collect(Collectors.toList());
        //用发货明细可以查到退货(退货理论上会携带订单id,确认了一定会带)
        List<PurchaseConsignmentDetailDTO> returnList = purchaseConsignmentApi.getPurchaseDetailListByShippingIds(shippingDetailIds).getCheckedData();
        Map<String, PurchaseConsignmentDetailDTO> consignMap = CollectionUtils.convertMap(returnList, PurchaseConsignmentDetailDTO::getShippingDetailId);

        //订单id,发货数量
        Map<String,Integer> map = new HashMap<>();
        //订单id,退货数量
        Map<String,Integer> returnMap = new HashMap<>();
        for (ShippingDetailDTO shippingDetailDTO : shippingDetailList) {
            //处理发货
            String orderId = shippingDetailDTO.getProjectOrderId();
            if(ObjectUtil.isNotNull(orderId)){
                if(map.containsKey(orderId)){
                    Integer amount = map.get(orderId);
                    amount = amount + 1;
                    map.put(orderId,amount);
                }else {
                    map.put(orderId,1);
                }
            }
            //处理退货,包含在内说明这个被退货了
            if(consignMap.containsKey(shippingDetailDTO.getId())){
                if(returnMap.containsKey(orderId)){
                    Integer returnAmount = returnMap.get(orderId);
                    returnAmount = returnAmount + 1;
                    returnMap.put(orderId,returnAmount);
                }else {
                    returnMap.put(orderId,1);
                }
            }
        }
        List<PmsOrderShippingRespVO> pmsOrderShippingRespVOList = BeanUtils.toBean(pmsOrderList, PmsOrderShippingRespVO.class,vo->{
            if(map.containsKey(vo.getId())){
                Integer amount = map.get(vo.getId());
                vo.setAmount(amount);
            }else {
                vo.setAmount(0);
            }
            if(returnMap.containsKey(vo.getId())){
                Integer returnAmount = returnMap.get(vo.getId());
                vo.setReturnAmount(returnAmount);
            }else {
                vo.setReturnAmount(0);
            }
        });
        return success(pmsOrderShippingRespVOList);
    }

    /**
     * 主体是采购计划
     * 关联,采购计划id关联需求id,需求id关联合同id,发货详情上有合同id和计划id
     */
    @GetMapping("getPurchaseByProjectId")
    @Operation(summary = "根据项目id获取订单发货数量")
    public CommonResult<List<PmsPlanPurchaseRespVO>> getPurchaseByProjectId(@RequestParam("projectIds")List<String> projectIds){
        List<PlanPurchaseMaterialDO> planPurchaseMaterialList = pmsPlanService.getPlanPurchaseMaterialListByProjectIds(projectIds);
        List<String> pids = planPurchaseMaterialList.stream().map(PlanPurchaseMaterialDO::getId).collect(Collectors.toList());
        Map<String,Integer> amountMap = new HashMap<>();
        Map<String,Integer> returnMap = new HashMap<>();
        //根据采购计划id查询收货详情
        List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetailDTOListTemp = purchaseConsignmentApi.getPurchaseDetailListByPurchaseIds(pids).getCheckedData();
        List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetailDTOList = purchaseConsignmentDetailDTOListTemp.stream().filter((item) -> {
            List<String> types = Arrays.asList("1", "2");
//            return ObjectUtil.isNotNull(item.getConsignmentStatus()) && (item.getConsignmentStatus() == 6) && ("1".equals(item.getConsignmentType()));
            return ObjectUtil.isNotNull(item.getConsignmentStatus()) && (item.getConsignmentStatus() == 6) && (types.contains(item.getConsignmentType()));
        }).collect(Collectors.toList());
        //再查退货
        List<String> consignmentDetailIds = purchaseConsignmentDetailDTOList.stream().map(PurchaseConsignmentDetailDTO::getId).collect(Collectors.toList());
        List<ShippingDetailDTO> shippingDetailDTOList = shippingApi.getShippingByConsignmentDetailIds(consignmentDetailIds).getCheckedData();
        Map<String, ShippingDetailDTO> stringShippingDetailDTOMap = CollectionUtils.convertMap(shippingDetailDTOList, ShippingDetailDTO::getConsignmentDetailId);

        for (PurchaseConsignmentDetailDTO dto : purchaseConsignmentDetailDTOList) {
            if(dto.getPurchaseId()!=null){
                String purchaseId = dto.getPurchaseId();
                //收货
                if(amountMap.containsKey(purchaseId)){
                    Integer amout = amountMap.get(purchaseId);
                    amout = amout + 1;
                    amountMap.put(purchaseId,amout);
                }else {
                    amountMap.put(purchaseId,1);
                }
                //退货,包含说明被退货了
                if(stringShippingDetailDTOMap.containsKey(dto.getId())){
                    if(returnMap.containsKey(purchaseId)){
                        Integer returnAmount = returnMap.get(purchaseId);
                        returnAmount = returnAmount + 1;
                        returnMap.put(purchaseId,returnAmount);
                    }else {
                        returnMap.put(purchaseId,1);
                    }
                }
            }
        }
        List<PmsPlanPurchaseRespVO> pmsPlanPurchaseRespVOS = BeanUtils.toBean(planPurchaseMaterialList, PmsPlanPurchaseRespVO.class, vo -> {
            if (amountMap.containsKey(vo.getId())) {
                Integer amount = amountMap.get(vo.getId());
                vo.setAmount(amount);
            } else {
                vo.setAmount(0);
            }
            if (returnMap.containsKey(vo.getId())) {
                Integer returnAmount = returnMap.get(vo.getId());
                vo.setReturnAmount(returnAmount);
            } else {
                vo.setReturnAmount(0);
            }
        });
        return success(pmsPlanPurchaseRespVOS);
    }

    /**
     * 已经不使用的
     * @param projectId
     * @return
     */
    @GetMapping("getShippingReturnDetailByProjectId")
    @Operation(summary = "根据项目id获取销售退货单及明细")
    public CommonResult<List<ShippingReturnDTO>> getShippingReturnDetailByProjectId(@RequestParam("projectId")String projectId){
        List<String> ids = getContractIds(projectId);
        if(ObjectUtil.isNull(ids)||ids.size()==0){
            List<ShippingReturnDTO> list = new ArrayList<>();
            return success(list);
        }
        List<ShippingReturnDTO> shippingReturnList = shippingReturnApi.getShippingReturnListByContractIds(ids).getCheckedData();
        return success(shippingReturnList);
    }

    /** 即将不使用的 */
    @GetMapping("getPurchaseConsignmentByProjectId")
    @Operation(summary = "根据项目id获取采购发货单及明细")
    public CommonResult<List<PurchaseConsignmentRespVO>> getPurchaseConsignmentByProjectId(@RequestParam("projectIds")List<String> projectIds){
//        List<String> ids = getContractIds(projectId);
//        if(ObjectUtil.isNull(ids)||ids.size()==0){
//            List<PurchaseConsignmentRespVO> list = new ArrayList<>();
//            return success(list);
//        }
//        List<PurchaseConsignmentDTO> purchaseConsignmentDTOList = purchaseConsignmentApi.getConsignmentDetailByContractIds(ids).getCheckedData();
        List<PurchaseConsignmentDTO> purchaseConsignmentDTOList = purchaseConsignmentApi.getPurchaseListByProjectIds(projectIds).getCheckedData();


        List<Long> userIds = purchaseConsignmentDTOList.stream().filter(item -> ObjectUtil.isNotNull(item.getConsignedBy())).map(item->Long.valueOf(item.getConsignedBy())).collect(Collectors.toList());
        List<AdminUserRespDTO> adminList = adminUserApi.getUserList(userIds).getCheckedData();
        Map<Long, AdminUserRespDTO> userMap = CollectionUtils.convertMap(adminList, AdminUserRespDTO::getId);

        List<Long> deptIds = adminList.stream().filter(item -> ObjectUtil.isNotNull(item.getDeptId())).map(AdminUserRespDTO::getDeptId).collect(Collectors.toList());
        List<DeptRespDTO> deptList = deptApi.getDeptList(deptIds).getCheckedData();
        Map<Long, DeptRespDTO> deptMap = CollectionUtils.convertMap(deptList, DeptRespDTO::getId);

        List<String> companyIds = purchaseConsignmentDTOList.stream().filter(item -> ObjectUtil.isNotNull(item.getCompanyId())).map(PurchaseConsignmentDTO::getCompanyId).distinct().collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(companyIds);

        List<PurchaseConsignmentRespVO> purchaseConsignmentRespVOS = BeanUtils.toBean(purchaseConsignmentDTOList, PurchaseConsignmentRespVO.class, vo -> {
            if(ObjectUtil.isNotNull(vo.getConsignedBy())&&userMap.containsKey(Long.valueOf(vo.getConsignedBy()))){
                vo.setConsignedByName(userMap.get(Long.valueOf(vo.getConsignedBy())).getNickname());
            }
            if(ObjectUtil.isNotNull(vo.getConsignedBy())&&userMap.containsKey(Long.valueOf(vo.getConsignedBy()))){
                vo.setConsignedByDeptId(userMap.get(Long.valueOf(vo.getConsignedBy())).getDeptId());
            }
            if(ObjectUtil.isNotNull(vo.getConsignedByDeptId())&&deptMap.containsKey(vo.getConsignedByDeptId())){
                vo.setConsignedByDeptName(deptMap.get(vo.getConsignedByDeptId()).getName());
            }
            if(ObjectUtil.isNotNull(vo.getCompanyId())&&companyMap.containsKey(vo.getCompanyId())){
                vo.setCompanyName(companyMap.get(vo.getCompanyId()).getName());
            }


        });
        return success(purchaseConsignmentRespVOS);
    }

    /**
     * 已经不使用的
     * @param projectId
     * @return
     */
    @GetMapping("getConsignmentReturnByProjectId")
    @Operation(summary = "根据项目id获取采购退货单及明细")
    public CommonResult<List<ConsignmentReturnDTO>> getConsignmentReturnByProjectId(@RequestParam("projectId")String projectId){
         List<String> ids = getContractIds(projectId);
        if(ObjectUtil.isNull(ids)||ids.size()==0){
            List<ConsignmentReturnDTO> list = new ArrayList<>();
            return success(list);
        }
        List<ConsignmentReturnDTO> consignmentReturnDTOList = consignmentReturnApi.getConsignmentReturnDetailByContractIds(ids).getCheckedData();
        return success(consignmentReturnDTOList);
    }


    /**
     * 根据项目名获取合同ids
     * @param id
     * @return
     */
    public List<String> getContractIds(String id){
        //合同那边的数据
        List<ContractRespDTO> contractList = contractApi.getContractListByProjectIds(Arrays.asList(id),2).getCheckedData();
        List<String> ids = contractList.stream().map(ContractRespDTO::getId).collect(Collectors.toList());
//        //项目这边的数据,项目这边没有数据了,都是合同挂到项目上
//        List<PmsOrderDO> pmsOrderDOList = approvalService.getOrderListByProjectId(id);
//        List<String> contractIds2 = pmsOrderDOList.stream().filter(item -> ObjectUtil.isNotNull(item.getContractId())).map(item -> item.getContractId()).collect(Collectors.toList());
//        contractIds.addAll(contractIds2);
//        List<String> ids = contractIds.stream().filter(item -> ObjectUtil.isNotNull(item)).filter(item-> ObjectUtil.isNotEmpty(item)).distinct().collect(Collectors.toList());

        //这块不从合同来了，都是项目id，暂时没空管这，先给假数据
//        List<String> ids = new ArrayList<>();
//        ids.add("1");
        return ids;
    }
}
