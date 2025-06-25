package cn.iocoder.yudao.module.pms.controller.admin.excute;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.controller.admin.excute.vo.ExcuteOrderListRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.excute.vo.ProductProgressRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderListPurchaseDetailRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderListRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.ProjectPurchaseDetailRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderService;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsProjectOrderDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderDTO;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDetailDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.cloud.mcs.enums.DictConstants.MCS_BATCH_RECORD_STATUS_COMPLETED;

/**
 * TODO 项目执行暂时去掉,前面整改完再改这
 */
@Tag(name = "管理后台 - 项目执行")
@RestController
@RequestMapping("/pms/excute")
@Validated
public class ExcuteController {
    @Resource
    private PmsApprovalService pmsApprovalService;
    @Resource
    private PmsOrderService pmsOrderService;
//    @Resource
//    private MaterialConfigApi materialConfigApi;
    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;
    @Resource
    private ConsignmentReturnApi consignmentReturnApi;
    @Resource
    private ContractApi contractApi;
    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;
    @Resource
    private PmsPlanService pmsPlanService;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private ShippingApi shippingApi;
    /**
     * 通过项目id查询所属产品或物料
     * @param
     */
//    @GetMapping("getMaterialList")
//    @Operation(summary = "根据项目id获取所属产品或物料")
//    public CommonResult<List<ExcuteOrderListRespVO>> getMaterialList(@RequestParam("projectId")String projectId){
//        List<PmsOrderDO> orderListDO = pmsApprovalService.getOrderListByProjectId(projectId);
//        //用来存储所有子订单
//        List<ExcuteOrderListRespVO> allOrderItemList = new ArrayList<>();
//        //用来存储所有材料id
//        List<String> materialList = new ArrayList<>();
//
//        //遍历订单，存储所有材料id
//        for (PmsOrderDO pmsOrderDO : orderListDO) {
//            List<OrderListDO> orderItemList = pmsOrderDO.getOrderItemList();
//            if(orderItemList!=null&&orderItemList.size()>0){
//                List<ExcuteOrderListRespVO> excuteOrderListRespVOS = BeanUtils.toBean(orderItemList, ExcuteOrderListRespVO.class, vo -> {
//                    vo.setContractId(pmsOrderDO.getContractId());
//                });
//                allOrderItemList.addAll(excuteOrderListRespVOS);
//                //查询订单(合同)下的物料(产品)类型
//                List<String> materialIds = orderItemList.stream().filter(item -> item.getMaterialId() != null).map(item -> item.getMaterialId()).collect(Collectors.toList());
//                materialList.addAll(materialIds);
//
//            }
//        }
//
//
//
//        List<String> materialListDistinct = materialList.stream().distinct().collect(Collectors.toList());
//        //拿到所需材料的map
//        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialListDistinct);
//        if(materialConfigMap.size()>0){
//            //补全订单的产品/物料信息
//            for (ExcuteOrderListRespVO excuteOrderListRespVO : allOrderItemList) {
//                MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(excuteOrderListRespVO.getMaterialId());
//                excuteOrderListRespVO.setMaterialName(materialConfigRespDTO.getMaterialName());
//                excuteOrderListRespVO.setMaterialCode(materialConfigRespDTO.getMaterialCode());
//                excuteOrderListRespVO.setMaterialNumber(materialConfigRespDTO.getMaterialNumber());
//                excuteOrderListRespVO.setMaterialUnit(materialConfigRespDTO.getMaterialUnit());
//            }
//        }
//
//        return success(allOrderItemList);
//    }

    @GetMapping("getMaterialListByProjectIds")
    @Operation(summary = "根据项目id获取所属产品")
    public CommonResult<List<ExcuteOrderListRespVO>> getMaterialListByProjectIds(@RequestParam("projectIds")List<String> projectIds){
        if(projectIds==null||projectIds.size()<=0){
            List<ExcuteOrderListRespVO> list = new ArrayList<>();
            return success(list);
        }
        List<PmsOrderDO> orderListDO = pmsApprovalService.getOrderListByProjectIds(projectIds);
        List<String> materialList = getMaterialList(orderListDO);
        //用来存储所有子订单
        List<ExcuteOrderListRespVO> allOrderItemList = BeanUtils.toBean(orderListDO, ExcuteOrderListRespVO.class);

        List<String> materialListDistinct = materialList.stream().distinct().collect(Collectors.toList());
        //拿到所需材料的map
        List<MaterialConfigRespDTO> materialConfigRespDTOList = materialMCCApi.getMaterialConfigList(materialListDistinct).getCheckedData();
        Map<String, MaterialConfigRespDTO> materialConfigMap = CollectionUtils.convertMap(materialConfigRespDTOList, MaterialConfigRespDTO::getMaterialNumber);

//        Map<String, MaterialConfigRespDTO> materialConfigMap = materialMCCApi.getMaterialConfigMap(materialListDistinct);
        if(materialConfigMap.size()>0){
            //补全订单的产品/物料信息
            for (ExcuteOrderListRespVO excuteOrderListRespVO : allOrderItemList) {
                if(materialConfigMap.containsKey(excuteOrderListRespVO.getPartNumber())){
                    MaterialConfigRespDTO materialConfigRespDTO = materialConfigMap.get(excuteOrderListRespVO.getPartNumber());
                    excuteOrderListRespVO.setMaterialName(materialConfigRespDTO.getMaterialName());
                    excuteOrderListRespVO.setMaterialNumber(excuteOrderListRespVO.getPartNumber());
                    excuteOrderListRespVO.setMaterialCode(materialConfigRespDTO.getMaterialTypeCode());
                    excuteOrderListRespVO.setMaterialId(materialConfigRespDTO.getId());
                    excuteOrderListRespVO.setMaterialUnit(materialConfigRespDTO.getMaterialUnit());
                }

            }
        }

        return success(allOrderItemList);
    }

    @GetMapping("getPurchaseProgress")
    @Operation(summary = "根据项目id获取采购进度")
    public CommonResult<List<OrderListPurchaseDetailRespVO>> purchaseProgress(@RequestParam("projectIds")List<String> projectIds){
        if(CollectionUtils.isAnyEmpty(projectIds)){
            return success(Collections.emptyList());
        }
//        List<PmsOrderDO> orderList = pmsApprovalService.getOrderListByProjectIds(projectIds);
//        //根据订单id查询收货和退货的明细
//        List<String> orderIds = orderList.stream().map(PmsOrderDO::getId).collect(Collectors.toList());
        //1.根据项目id查合同订单
        List<ContractOrderDTO> contractOrderList = contractApi.getContractOrderListByProjectIds(projectIds, 1).getCheckedData();
        //2.根据合同订单查采购收货明细
        List<String> contractOrderIds = contractOrderList.stream().filter((val)->{return StrUtil.isNotEmpty(val.getId());}).map(ContractOrderDTO::getId).collect(Collectors.toList());
        if(CollectionUtils.isAnyEmpty(contractOrderIds)){
            return success(Collections.emptyList());
        }
        List<PurchaseConsignmentDetailDTO> totalDetailList = purchaseConsignmentApi.getPurchaseDetailListByContractOrderIds(contractOrderIds).getCheckedData();
        //只要1,2,4
        List<PurchaseConsignmentDetailDTO> detailList = totalDetailList.stream().filter((item) -> {
            return Arrays.asList("1", "2", "4").contains(item.getConsignmentType())&&item.getConsignmentStatus()==6;
        }).collect(Collectors.toList());
        //3.采购详情按订单分类
        Map<String,List<PurchaseConsignmentDetailDTO>> orderMap = new HashMap<>();
        for (PurchaseConsignmentDetailDTO purchaseConsignmentDetailDTO : detailList) {
            if(orderMap.containsKey(purchaseConsignmentDetailDTO.getOrderId())){
                orderMap.get(purchaseConsignmentDetailDTO.getOrderId()).add(purchaseConsignmentDetailDTO);
            }else {
                List<PurchaseConsignmentDetailDTO> list = new ArrayList<>();
                list.add(purchaseConsignmentDetailDTO);
                orderMap.put(purchaseConsignmentDetailDTO.getOrderId(),list);
            }
        }
        //4.查物料名
        List<String> materialIds = contractOrderList.stream().map(ContractOrderDTO::getMaterialId).distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialMCCApi.getMaterialConfigMap(materialIds);
        //5.查合同名
        List<String> contractIds = contractOrderList.stream().filter((val) -> {
            return StrUtil.isNotEmpty(val.getContractId());
        }).map(ContractOrderDTO::getContractId).collect(Collectors.toList());
        Map<String, ContractRespDTO> contractMap = contractApi.getContractMap(contractIds);
        //6.整合数据
        List<OrderListPurchaseDetailRespVO> orderListPurchaseDetailRespVOS = BeanUtils.toBean(contractOrderList, OrderListPurchaseDetailRespVO.class, vo -> {
            if (orderMap.containsKey(vo.getId())) {
                vo.setActualAmount(orderMap.get(vo.getId()).size());
                vo.setRemainAmount((vo.getQuantity() - vo.getActualAmount()) > 0 ? (vo.getQuantity() - vo.getActualAmount()) : 0);
                BigDecimal multiply = new BigDecimal(vo.getActualAmount()).divide(new BigDecimal(vo.getQuantity()), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                if (multiply.compareTo(new BigDecimal(100)) >=0){
                    vo.setProgress(new BigDecimal(100));
                }else {
                    vo.setProgress(multiply);
                }
                //vo.setProgress(new BigDecimal(vo.getActualAmount()).divide(new BigDecimal(vo.getQuantity()),2,3).multiply(new BigDecimal(100)));
            }else {
                vo.setActualAmount(0);
                vo.setRemainAmount(vo.getQuantity() - vo.getActualAmount());
                vo.setProgress(new BigDecimal(0));
            }
            if(materialConfigMap.containsKey(vo.getMaterialId())){
                vo.setMaterialName(materialConfigMap.get(vo.getMaterialId()).getMaterialName());
                vo.setMaterialNumber(materialConfigMap.get(vo.getMaterialId()).getMaterialNumber());
            }
            if(contractMap.containsKey(vo.getContractId())){
                vo.setContractName(contractMap.get(vo.getContractId()).getName());
                vo.setContractNumber(contractMap.get(vo.getContractId()).getNumber());
            }
        });
        return success(orderListPurchaseDetailRespVOS);
    }


    /**
     * 获取详细的采购进度,单个物料的进度
     * @param projectId
     */
    @GetMapping("getPurchaseProgress2")
    @Operation(summary = "根据项目id获取采购进度")
    public CommonResult<List<OrderListPurchaseDetailRespVO>> purchaseProgress(@RequestParam("projectId")String projectId){
        //获取该项目下的订单
        List<PmsOrderDO> pmsOrderDOList = pmsApprovalService.getOrderListByProjectId(projectId);
//        //合同id集合
//        List<String> ids = pmsOrderDOList.stream().filter(item -> ObjectUtil.isNotNull(item.getContractId())).map(item -> item.getContractId()).collect(Collectors.toList());
//        if(ObjectUtil.isNull(ids)||ids.size()==0){
//            List<OrderListPurchaseDetailRespVO> list = new ArrayList<>();
//            return success(list);
//        }
//        //查询项目下的所有合同信息
//        List<ContractRespDTO> contractList = contractApi.getContractList(ids).getCheckedData();
        //现在是根基项目id查询合同
        List<ContractRespDTO> contractRespDTOList = contractApi.getContractListByProjectIds(Arrays.asList(projectId),2).getCheckedData();
        List<String> ids = contractRespDTOList.stream().filter(item -> ObjectUtil.isNotNull(item.getId())).map(item -> item.getId()).collect(Collectors.toList());
        if(ObjectUtil.isNull(ids)||ids.size()==0){
            List<OrderListPurchaseDetailRespVO> list = new ArrayList<>();
            return success(list);
        }
        //收货
        List<PurchaseConsignmentDTO> consignmentList = purchaseConsignmentApi.getConsignmentDetailByContractIds(ids).getCheckedData();
        //存储所有收货信息
        List<PurchaseConsignmentDetailDTO> consignmentDetailList = new ArrayList<>();
        for (PurchaseConsignmentDTO purchaseConsignmentDTO : consignmentList) {
            if(purchaseConsignmentDTO.getDetailDTOList()!=null&&purchaseConsignmentDTO.getDetailDTOList().size()>0){
                consignmentDetailList.addAll(purchaseConsignmentDTO.getDetailDTOList());
            }
        }

        //退货
        List<ConsignmentReturnDTO> returnList = consignmentReturnApi.getConsignmentReturnDetailByContractIds(ids).getCheckedData();
        List<ConsignmentReturnDetailDTO> returnDetailList = new ArrayList<>();
        for (ConsignmentReturnDTO consignmentReturnDTO : returnList) {
            if(consignmentReturnDTO.getReturnDetailDTOList()!=null&&consignmentReturnDTO.getReturnDetailDTOList().size()>0){
                returnDetailList.addAll(consignmentReturnDTO.getReturnDetailDTOList());
            }
        }
        //存储退货单-退货数量(详细)，key用的采购单详细
        Map<String, BigDecimal> returnDetailMap = new HashMap<>();
        //过滤无效退货单
        List<ConsignmentReturnDetailDTO> returnDetailDTOList = returnDetailList.stream().filter(item -> ObjectUtil.isNotNull(item.getSignedAmount())).collect(Collectors.toList());
        //这样每个详细采购单的退货数量就计算出来了
        for (ConsignmentReturnDetailDTO consignmentReturnDetailDTO : returnDetailDTOList) {
            if(returnDetailMap.containsKey(consignmentReturnDetailDTO.getConsignmentDetailId())){
                BigDecimal add = returnDetailMap.get(consignmentReturnDetailDTO.getConsignmentDetailId()).add(consignmentReturnDetailDTO.getSignedAmount());
                returnDetailMap.put(consignmentReturnDetailDTO.getConsignmentDetailId(),add);
            }else {
                returnDetailMap.put(consignmentReturnDetailDTO.getConsignmentDetailId(),consignmentReturnDetailDTO.getSignedAmount());
            }
        }

        //单个订单发货数存储,用单个订单的id作为key
        Map<String,BigDecimal> signAmountMap = new HashMap<>();
        //单个订单退货数存储,用单个订单的id作为key
        Map<String,BigDecimal> returnAmountMap = new HashMap<>();
        //存储每个订单的合同id,key为订单id
        Map<String,String> orderContractIdMap = new HashMap<>();


        //存储所有合同订单
        List<ContractOrderRespDTO> allContractOrder = new ArrayList<>();
        //合同
        for (ContractRespDTO contractRespDTO : contractRespDTOList) {
            List<ContractOrderRespDTO> contractOrderList = contractApi.getOrderList(contractRespDTO.getId()).getCheckedData();
            allContractOrder.addAll(contractOrderList);
            //单个合同订单
            for (ContractOrderRespDTO contractOrderRespDTO : contractOrderList) {
                orderContractIdMap.put(contractOrderRespDTO.getId(),contractRespDTO.getId());
                //发货数量,退货数量
                //拿到合同订单下的有效发货单
                List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetail = consignmentDetailList.stream().filter(item -> item.getOrderId().equals(contractOrderRespDTO.getId())).filter(item -> ObjectUtil.isNotNull(item.getSignedAmount())).collect(Collectors.toList());
                //遍历发货单
                //得到一个订单的发货数
                BigDecimal signAmount = new BigDecimal("0");
                //得到一个订单的退货数
                BigDecimal returnAmount = new BigDecimal("0");
                for (PurchaseConsignmentDetailDTO purchaseConsignmentDetailDTO : purchaseConsignmentDetail) {
                    signAmount =  signAmount.add(purchaseConsignmentDetailDTO.getSignedAmount());

//                    returnAmount = returnAmount.add(returnDetailMap.get(purchaseConsignmentDetailDTO.getId()));
                    returnAmount = returnAmount.add(returnDetailMap.get(purchaseConsignmentDetailDTO.getId())==null? new BigDecimal(0):returnDetailMap.get(purchaseConsignmentDetailDTO.getId()));
                }
                signAmountMap.put(contractOrderRespDTO.getId(),signAmount);
                returnAmountMap.put(contractOrderRespDTO.getId(),returnAmount);
            }
        }
        //获取物料id
        List<String> materialList = getMaterialList(pmsOrderDOList);


        List<String> materialListDistinct = materialList.stream().distinct().collect(Collectors.toList());
        //拿到所需材料的map
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialMCCApi.getMaterialConfigMap(materialListDistinct);

        //封装结果数据
        List<OrderListPurchaseDetailRespVO> orderListPurchaseDetailRespVOS = BeanUtils.toBean(allContractOrder, OrderListPurchaseDetailRespVO.class, vo -> {
            //这个id是合同订单的id
//            vo.setSignAmount(signAmountMap.get(vo.getId()));
//            vo.setReturnAmount(returnAmountMap.get(vo.getId()));
//            vo.setActualAmount(vo.getSignAmount().subtract(vo.getReturnAmount()));
//            vo.setProgress(vo.getActualAmount().divide(new BigDecimal(vo.getQuantity())).multiply(new BigDecimal(100)));
            if(materialConfigMap.containsKey(vo.getMaterialId())){
                vo.setMaterialName(materialConfigMap.get(vo.getMaterialId()).getMaterialName());
                vo.setMaterialNumber(materialConfigMap.get(vo.getMaterialId()).getMaterialNumber());
            }
//            vo.setRemainAmount(new BigDecimal(vo.getQuantity()).subtract(vo.getActualAmount()));
            vo.setContractId(orderContractIdMap.get(vo.getId()));
        });

        return success(orderListPurchaseDetailRespVOS);
    }

    @GetMapping("getProductProgress")
    @Operation(summary = "根据项目编号获取生产进度")
    public CommonResult<List<ProductProgressRespVO>> getProductProgress(@RequestParam("projectCodes") List<String> projectCodes) {
        if(ObjectUtil.isNull(projectCodes)||projectCodes.size()==0){
            return success(new ArrayList<ProductProgressRespVO>());
        }
        //获取项目
        List<PmsApprovalDO> pmsApprovalList = pmsApprovalService.getByProjectCodes(projectCodes);
        //因为我的项目编码独一无二,所以可以做key
        Map<String, PmsApprovalDO> pmsApprovalMap = CollectionUtils.convertMap(pmsApprovalList, PmsApprovalDO::getProjectCode);

        //查子订单是为计划数量赋值
        List<PlanItemDO> planItemList = pmsPlanService.selectByProjectCodes(projectCodes);
        //用项目编码+图号做key就是惟一的
        Map<String, PlanItemDO> planItemMap = CollectionUtils.convertMap(planItemList, (item) -> {
            return item.getProjectCode() + item.getPartNumber();
        });

        List<String> projectNumbers = projectCodes;
        List<McsProjectOrderDTO> McsProjectOrderDTOList = mcsManufacturingControlApi.getPartRecordByProjectNumber(projectNumbers).getCheckedData();

        List<ProductProgressRespVO> progressList = BeanUtils.toBean(McsProjectOrderDTOList, ProductProgressRespVO.class, vo -> {
            //完成数量
            if(vo.getPartList()!=null&&vo.getPartList().size()>0){
                List<McsBatchRecordDTO> collect = vo.getPartList().stream().filter(item -> item.getStatus() == MCS_BATCH_RECORD_STATUS_COMPLETED).collect(Collectors.toList());
                vo.setComplete(collect.size());
            }else {
                vo.setComplete(0);
            }
            //项目名
            vo.setProjectName(pmsApprovalMap.get(vo.getProjectNumber()).getProjectName());
            //计划数量
            if(planItemMap.containsKey(vo.getProjectNumber()+vo.getPartNumber())){
                vo.setQuantity(planItemMap.get(vo.getProjectNumber()+vo.getPartNumber()).getQuantity());
            }

            //进度
            vo.setProgress(new BigDecimal(vo.getComplete()).divide(new BigDecimal(vo.getCount()),2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)));
//            vo.setProgress(new BigDecimal(1).divide(new BigDecimal(vo.getCount())).multiply(new BigDecimal(100)));
        });
      return success(progressList);
    }

    /**
     * 根据订单集合(成品)
     * 获取所有物料id
     */
    public List<String> getMaterialList(List<PmsOrderDO> list){
        //材料
        //用来存储所有材料id
        List<String> materialList = new ArrayList<>();
        //遍历订单，存储所有材料id
        for (PmsOrderDO pmsOrderDO : list) {
//            String materialNumber = pmsOrderDO.getMaterialNumber();
            String materialNumber = pmsOrderDO.getPartNumber();
            //请求mcc
            MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
            dto.setMaterialNumber(materialNumber);
            //物料码唯一,所以只能查询到一条
            List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
            if (materialConfigList.size()>0){
                materialList.add(materialConfigList.get(0).getId());
            }
        }
        return materialList;
    }




}
