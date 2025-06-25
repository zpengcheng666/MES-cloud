package cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo.MaterialPurchasePlanRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo.ResourceRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo.StartPurchaseReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderSaveReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.PmsPlanItemReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.service.materialPurchasePlan.MaterialPurchasePlanService;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import com.miyu.cloud.dms.api.devicetype.DeviceTypeApi;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.module.pdm.api.projectAssessment.dto.CombinationRespDTO;
import com.miyu.module.pdm.api.projectPlan.PdmProjectPlanApi;
import com.miyu.module.pdm.api.projectPlan.dto.ProcedureDetailRespDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDetailDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.PLAN_ITEM_NOT_EXISTS;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.UN_CHOOSE_PROCESS_SCHEME;

@Tag(name = "管理后台 - 物料采购计划")
@RestController
@RequestMapping("/pms/materialPurchasePlan/")
@Validated
public class MaterialPurchasePlanController {

    @Resource
    private PmsPlanService pmsPlanService;

    @Resource
    private ContractApi contractApi;

    @Resource
    private ConsignmentReturnApi consignmentReturnApi;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private PmsApprovalService pmsApprovalService;

    @Resource
    private PdmProjectPlanApi projectPlanApi;

    @Resource
    private DeviceTypeApi deviceTypeApi;

    @Resource
    private MaterialConfigApi materialConfigApi;

    @Resource
    private MaterialPurchasePlanService materialPurchasePlanService;

    /**
     * 因为中间产品部分被移除,这个方法也弃用了
     * @param projectIds
     * @return
     */
    @GetMapping("getMaterial")
    @Operation(summary ="获取项目所属物料采购计划")
    public CommonResult<List<MaterialPurchasePlanRespVO>> getMaterial(@RequestParam("projectIds") Collection<String> projectIds){
        if(projectIds.size()==0){
            List<MaterialPurchasePlanRespVO> list = new ArrayList<>();
            return success(list);
        }
        List<PlanItemDO> planItemDOS = pmsPlanService.selectListMaterialUse(projectIds);

        //取出物料id集合
        //先注掉
//        List<String> materialIds = planItemDOS.stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialId())).distinct().map(item -> item.getMaterialId()).collect(Collectors.toList());
        List<MaterialStockRespDTO> materialStockList = new ArrayList<>();
//        if(materialIds.size()>0){
//            materialStockList = materialStockApi.getMaterialsByConfigIds(materialIds).getCheckedData();
//        }
        //拿到物料库存，之后就是计算每个物料的库存总数并存起来
        Map<String, BigDecimal> materailInventoryAmountMap = new HashMap<>();
        for (MaterialStockRespDTO materialStockRespDTO : materialStockList) {
            if(materailInventoryAmountMap.containsKey(materialStockRespDTO.getMaterialConfigId())){
                BigDecimal add = materailInventoryAmountMap.get(materialStockRespDTO.getMaterialConfigId()).add(new BigDecimal(materialStockRespDTO.getTotality()));
                materailInventoryAmountMap.put(materialStockRespDTO.getMaterialConfigId(),add);
            }else {
                materailInventoryAmountMap.put(materialStockRespDTO.getMaterialConfigId(),new BigDecimal(materialStockRespDTO.getTotality()));
            }
        }
        //获取退货单,通过合同id集合查,要查的是采购退货单,所有项目的采购退货单都要
        List<ContractRespDTO> contractRespList = contractApi.getContractListAll().getCheckedData();
        //拿到所有合同后,只留下采购合同和已经审批通过的
        List<ContractRespDTO> contractList = contractRespList.stream().filter(item -> item.getType() == 1 && item.getStatus() == 2).collect(Collectors.toList());
       //取出合同id查询退货详情,只要状态为1和2的(审批和待出库)
        List<String> contractIds = contractList.stream().map(item -> item.getId()).collect(Collectors.toList());
        List<ConsignmentReturnDTO> consignmentReturnList = consignmentReturnApi.getConsignmentReturnDetailByContractIds(contractIds).getCheckedData();
        List<ConsignmentReturnDetailDTO> returnList = new ArrayList<>();
        if(consignmentReturnList.size()>0){
            for (ConsignmentReturnDTO consignmentReturnDTO : consignmentReturnList) {
                returnList.addAll(consignmentReturnDTO.getReturnDetailDTOList());
            }
        }
        //拿到详细退货单，之后就是计算每个物料的退货总数并存起来
        Map<String, BigDecimal> materailReturnAmountMap = new HashMap<>();
        for (ConsignmentReturnDetailDTO consignmentReturnDetailDTO : returnList) {
            if(materailReturnAmountMap.containsKey(consignmentReturnDetailDTO.getMaterialConfigId())){
                BigDecimal add = materailReturnAmountMap.get(consignmentReturnDetailDTO.getMaterialConfigId()).add(consignmentReturnDetailDTO.getConsignedAmount());
                materailReturnAmountMap.put(consignmentReturnDetailDTO.getMaterialConfigId(),add);
            }else {
                materailReturnAmountMap.put(consignmentReturnDetailDTO.getMaterialConfigId(),consignmentReturnDetailDTO.getConsignedAmount());
            }
        }
        //获取每个零件使用的物料,计算总物料使用,用于计算可用库存
        Map<String, BigDecimal> userMaterailMap = new HashMap<>();
        //TODO 先注掉
//        for (PlanItemDO planItemDO : planItemDOS) {
//            if (ObjectUtil.isNull(planItemDO.getMaterialId())){
//                continue;
//            }
//            if(userMaterailMap.containsKey(planItemDO.getMaterialId())){
//                BigDecimal add = userMaterailMap.get(planItemDO.getMaterialId()).add(planItemDO.getUseInventory());
//                userMaterailMap.put(planItemDO.getMaterialId(),add);
//            }else {
//                userMaterailMap.put(planItemDO.getMaterialId(),planItemDO.getUseInventory());
//            }
//        }


        List<MaterialPurchasePlanRespVO> materialPurchasePlanRespVOS = BeanUtils.toBean(planItemDOS, MaterialPurchasePlanRespVO.class,vo->{
            //设置退货数量
            vo.setMaterialRerurnAmount(materailReturnAmountMap.get(vo.getMaterialId()));
            //设置物料库存
            vo.setInventory(materailInventoryAmountMap.get(vo.getMaterialId()));
            //设置可用库存,总库存-退货-使用库存
            if(materailInventoryAmountMap.containsKey(vo.getMaterialId())){
                //这里退货单是所有的退货单，所以要区分有退货和无退货的物料
                if(materailReturnAmountMap.containsKey(vo.getMaterialId())){
                    BigDecimal subtract = materailInventoryAmountMap.get(vo.getMaterialId()).subtract(materailReturnAmountMap.get(vo.getMaterialId())).subtract(userMaterailMap.get(vo.getMaterialId()));
                    vo.setInventoryAvailable(subtract);
                }else {
                    BigDecimal subtract = materailInventoryAmountMap.get(vo.getMaterialId()).subtract(userMaterailMap.get(vo.getMaterialId()));
                    vo.setInventoryAvailable(subtract);
                }
            }
        });

        materialPurchasePlanRespVOS.sort((a,b)->{return a.getId().compareTo(b.getId());});

        return success(materialPurchasePlanRespVOS);
    }

    @GetMapping("getApprovalPageWithPass")
    @Operation(summary ="获取项目所属物料采购计划")
    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<PageResult<PmsApprovalRespVO>> getApprovalPageWithPass(@Valid PmsApprovalPageReqVO pageReqVO){
        PageResult<PmsApprovalDO> pageResult = pmsApprovalService.selectPageWithPass(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PmsApprovalRespVO.class));
    }

    @GetMapping("getApprovalListWithPass")
    @Operation(summary ="获取项目列表,通过审核的")
    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<List<PmsApprovalRespVO>> getApprovalListWithPass(@Valid PmsApprovalPageReqVO pageReqVO){
        List<PmsApprovalDO> pmsApprovalDOS = pmsApprovalService.selectListWithPass(pageReqVO);
        return success(BeanUtils.toBean(pmsApprovalDOS, PmsApprovalRespVO.class));
    }

    @GetMapping("getApprovalPageByStatus")
    @Operation(summary ="通过项目状态获取项目列表,1待审核，2待评审，3准备中，4生产中，5出库完成，6项目关闭，7异常终止。评审通过之后才能生产")
    @PreAuthorize("@ss.hasPermission('pms:approval:query')")
    public CommonResult<PageResult<PmsApprovalRespVO>> getApprovalListByStatus(@Valid PmsApprovalPageReqVO pageReqVO){
        PageResult<PmsApprovalDO> pageResult = pmsApprovalService.selectPageWithStatus(pageReqVO);
        return success(BeanUtils.toBean(pageResult,PmsApprovalRespVO.class));
    }


    //有变动,这个方法弃用
    @PutMapping("/updatePlanItem")
    @Operation(summary = "更新项目订单")
    public CommonResult<Boolean> updatePlanItem(@Valid @RequestBody PmsPlanItemReqVO req) {
        //没选工艺编程就当普通更新处理
        if(ObjectUtil.isNull(req.getProcessScheme())){
            pmsPlanService.updatePlanItem2(req);
            return success(true);
        }
        //TODO 库存这块暂时先注掉,逻辑有变
//        //库存
//        BigDecimal materialStock = getMaterialStock(req.getMaterialId());
//        //退货
//        BigDecimal materialReturn = getMaterialReturn(req.getMaterialId());
//        //已使用(不能查planItem了,得查planPurchaseMaterial)
//        BigDecimal useAmount = getAllPlanItemAmountByMaterialId(req.getMaterialId());
//        //这里多减了一份自己的，实际上只需要计算其他零件用的物料数量就好，一会还得加回来
//        BigDecimal subtract = materialStock.subtract(materialReturn).subtract(useAmount);
//        req.setAvailable(subtract.compareTo(new BigDecimal(0))>=0? subtract:new BigDecimal(0));
        pmsPlanService.updatePlanItem(req);
        return success(true);
    }

    /**
     * 获取单个物料库存
     * @param materialId
     * @return
     */
//    public BigDecimal getMaterialStock(String materialId){
//
////        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByConfigIds(Arrays.asList(materialId)).getCheckedData();
//        List<MaterialStockRespDTO> materialStockList = materialStockApi.getOutOrderMaterialsByConfigIds(Arrays.asList(materialId)).getCheckedData();
//        //因为我只传了一个id,所以直接加就可以
//        BigDecimal decimal = new BigDecimal("0");
//        for (MaterialStockRespDTO materialStockRespDTO : materialStockList) {
//            decimal = decimal.add(new BigDecimal(materialStockRespDTO.getTotality()));
//        }
//        return decimal;
//    }

    /**
     * 获取单个物料退货
     * @param materialId
     * @return
     */
    public BigDecimal getMaterialReturn(String materialId){
        //获取退货单,通过合同id集合查,要查的是采购退货单,所有项目的采购退货单都要
        List<ContractRespDTO> contractRespList = contractApi.getContractListAll().getCheckedData();
        //拿到所有合同后,只留下采购合同和已经审批通过的
        List<ContractRespDTO> contractList = contractRespList.stream().filter(item -> item.getType() == 1 && item.getStatus() == 2).collect(Collectors.toList());
        //取出合同id查询退货详情,只要状态为1和2的(审批和待出库)
        List<String> contractIds = contractList.stream().map(item -> item.getId()).collect(Collectors.toList());
        List<ConsignmentReturnDTO> consignmentReturnList = consignmentReturnApi.getConsignmentReturnDetailByContractIds(contractIds).getCheckedData();
        List<ConsignmentReturnDetailDTO> returnList = new ArrayList<>();
        if(consignmentReturnList.size()>0){
            for (ConsignmentReturnDTO consignmentReturnDTO : consignmentReturnList) {
                returnList.addAll(consignmentReturnDTO.getReturnDetailDTOList());
            }
        }
        BigDecimal decimal = new BigDecimal("0");
        for (ConsignmentReturnDetailDTO consignmentReturnDetailDTO : returnList) {
            //只要和传入的物料相等的
            if(materialId.equals(consignmentReturnDetailDTO.getMaterialConfigId())){
                decimal = decimal.add(consignmentReturnDetailDTO.getConsignedAmount());
            }
        }
        return decimal;
    }

    /**
     * 获取单个物料的使用数量
     * @param materialId
     * @return
     */
//    public BigDecimal getAllPlanItemAmountByMaterialId(String materialId){
//        List<PlanItemDO> planItemDOS = pmsPlanService.selectByMaterialId(materialId);
//        BigDecimal decimal = new BigDecimal("0");
//        for (PlanItemDO planItemDO : planItemDOS) {
//            if(planItemDO.getUseInventory()!=null){
//                decimal = decimal.add(planItemDO.getUseInventory());
//            }
//
//        }
//        return decimal;
//    }
    /**
     * 获取单个物料的使用数量
     * @param materialId
     * @return
     */
    public BigDecimal getAllPlanPurchaseMaterialAmountByMaterialId(String materialId){
        List<PlanPurchaseMaterialDO> planPurchaseMaterialDOS = pmsPlanService.selectPurchaseMaterialByMaterialId(materialId);
        BigDecimal decimal = new BigDecimal("0");
        for (PlanPurchaseMaterialDO planDO : planPurchaseMaterialDOS) {
            if(planDO.getUseInventory()!=null){
                decimal = decimal.add(planDO.getUseInventory());
            }
        }
        return decimal;
    }

    //TODO 这个好像没有使用,确认就删掉
    @GetMapping("/getProjPartBomProcessDetailDesignList")
    @Operation(summary = "获取工艺详细设计结构树")
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomProcessDetailDesignList(@RequestParam("projectCode") String projectCode,@RequestParam(name="partNumber",defaultValue = "") String partNumber,@RequestParam("viewSelf") boolean viewSelf,@RequestParam("processScheme") String processScheme){
        //获取工艺详细设计结构树
        List<ProjPartBomTreeRespDTO> checkedData = projectPlanApi.getProjPartBomProcessDetailDesignList(projectCode, viewSelf).getCheckedData();
        //过滤子版本,此时留下type3
        List<ProjPartBomTreeRespDTO> childList = checkedData.stream().filter(item -> processScheme.equals(item.getParentId())).collect(Collectors.toList());
        //再次用id过滤,留下type4
        List<String> ids = childList.stream().map(ProjPartBomTreeRespDTO::getId).collect(Collectors.toList());
        //这些是符合条件的type,子任务的子任务,通过这些id去查设备,刀具,工装
        List<String> collect = checkedData.stream().filter((item) -> {
            return ids.contains(item.getId());
        }).map(ProjPartBomTreeRespDTO::getId).collect(Collectors.toList());
        return success(childList);
    }


//    @GetMapping("/getProcessDetailDesign")
//    @Operation(summary = "获取工艺编程type3,目前用不到这个方法")
//    public CommonResult<List<ProjPartBomTreeRespDTO>> getProcessDetailDesign(@RequestParam("projectCode") String projectCode,
//                                                                             @RequestParam(name="partNumber",defaultValue = "") String partNumber,
//                                                                             @RequestParam("viewSelf") boolean viewSelf,
//                                                                             @RequestParam("processScheme") String processScheme,
//                                                                             @RequestParam("projectPlanId") String projectPlanId,
//                                                                             @RequestParam("projectPlanItemId") String projectPlanItemId){
//        //获取工艺详细设计结构树
//        List<ProjPartBomTreeRespDTO> checkedData = projectPlanApi.getProjPartBomPlanList(projectCode, viewSelf).getCheckedData();
//        //过滤子版本,此时留下type3,type3就够了
//        List<ProjPartBomTreeRespDTO> childList = checkedData.stream().filter(item -> processScheme.equals(item.getParentId())).collect(Collectors.toList());
//        savePlanResource(childList,processScheme,projectPlanId,projectCode,partNumber,projectPlanItemId);
//
//        return success(childList);
//    }

    @GetMapping("getPurchaseResource")
    @Operation(summary ="获取项目所属物料采购计划")
    public CommonResult<ResourceRespVO> getPurchaseResource(@RequestParam("projectPlanId") String projectPlanId){
        ResourceRespVO resource = pmsPlanService.getResource(projectPlanId);
        return success(resource);
    }

    /**
     * 上面那种只获取一个项目计划的采购列表,这个可以获取多个项目的
     * @param projectPlanIds
     * @return
     */
    @GetMapping("getPurchaseResourceByProjectIds")
    @Operation(summary ="获取项目所属物料采购计划")
    public CommonResult<ResourceRespVO> getPurchaseResourceByProjectId(@RequestParam("projectIds") List<String> projectPlanIds){
        ResourceRespVO resource = pmsPlanService.getResourceByProjectIds(projectPlanIds);
        return success(resource);
    }

    /**
     * 获取采购计划,项目状态为还在进行中的
     * @return
     */
    @GetMapping("getPurchaseResourceAll")
    @Operation(summary ="获取项目所属物料采购计划")
    public CommonResult<ResourceRespVO> getPurchaseResourceAll(){
        ResourceRespVO resource = pmsPlanService.getResourceAll();
        return success(resource);
    }


    /**
     * 存储物料采购计划的设备,刀具,工装
     * 原本是获取工艺下的设备等，然后存起来，现在是用评审数据，所以不会调用这个方法了。
     * 目前用不到这个方法
     * @param childList
     * @param processScheme
     * @param projectPlanId
     * @param projectCode
     * @param partNumber
     * @param projectPlanItemId
     */
    @Transactional(rollbackFor = Exception.class)
    public void savePlanResource(List<ProjPartBomTreeRespDTO> childList,String processScheme,String projectPlanId,String projectCode,String partNumber,String projectPlanItemId){
        //传入的是type3节点,通过这个节点的数据查询使用的设备,工装,刀具
        System.out.println(childList);
        List<PlanDeviceDO> planDeviceDOList = new ArrayList<>();
        List<PlanMaterialDO> planMaterialDOList = new ArrayList<>();
        List<PlanCombinationDO> planCombinationDOList = new ArrayList<>();

        for (ProjPartBomTreeRespDTO projPartBomTreeRespDTO : childList) {
            //获取节点下设备刀具工装id
            List<ProcedureDetailRespDTO> resourceList = projectPlanApi.getResourceListByProcedure(processScheme, projPartBomTreeRespDTO.getId(), projPartBomTreeRespDTO.getPartVersionId()).getCheckedData();
            //设备
            List<String> deviceIds = resourceList.stream().filter(item -> item.getResourcesType() == 1).map(ProcedureDetailRespDTO::getResourcesTypeId).collect(Collectors.toList());
            //刀具
            List<String> combinationIds = resourceList.stream().filter(item -> item.getResourcesType() == 2).map(ProcedureDetailRespDTO::getResourcesTypeId).collect(Collectors.toList());
            //工装
            List<String> materialIds = resourceList.stream().filter(item -> item.getResourcesType() == 3).map(ProcedureDetailRespDTO::getResourcesTypeId).collect(Collectors.toList());
            //设备
            if(deviceIds.size()>0){
                //获取设备
                List<DeviceTypeDataRespDTO> deviceType = deviceTypeApi.getDeviceTypeListByIds(deviceIds).getCheckedData();
                List<PlanDeviceDO> planDeviceList = BeanUtils.toBean(deviceType, PlanDeviceDO.class, vo -> {
                    vo.setProcedureName(projPartBomTreeRespDTO.getName()).setType(vo.getId()).setId(null).setProjectPlanId(projectPlanId).setProjectPlanItemId(projectPlanItemId).setProjectCode(projectCode).setPartNumber(partNumber);
                });
                planDeviceDOList.addAll(planDeviceList);
            }
            System.out.println(resourceList);

            if(combinationIds.size()>0){
                //TODO 暂时用pdm的数据
                List<CombinationRespDTO> combinationDTO = projectPlanApi.getResourceCombinationListByIds(combinationIds).getCheckedData();
                List<PlanCombinationDO> planCombination = BeanUtils.toBean(combinationDTO, PlanCombinationDO.class, vo -> {
                    vo.setProcedureName(projPartBomTreeRespDTO.getName()).setId(null).setProjectPlanId(projectPlanId).setProjectPlanItemId(projectPlanItemId).setProjectCode(projectCode).setPartNumber(partNumber);
                });
                planCombinationDOList.addAll(planCombination);
                //步骤同上
                //                BeanUtils.toBean(combinationDTO,)
            }
            if(materialIds.size()>0){
                List<MaterialConfigRespDTO> materialConfig = materialConfigApi.getMaterialConfigList(materialIds).getCheckedData();
                List<PlanMaterialDO> planMaterialDOS = BeanUtils.toBean(materialConfig, PlanMaterialDO.class, vo -> {
                    vo.setProcedureName(projPartBomTreeRespDTO.getName()).setId(null).setProjectPlanId(projectPlanId).setProjectPlanItemId(projectPlanItemId).setProjectCode(projectCode).setPartNumber(partNumber);
                });
                planMaterialDOList.addAll(planMaterialDOS);
            }
        }
        pmsPlanService.deleteDevice(projectPlanItemId);
        pmsPlanService.saveDevice(projectPlanItemId,planDeviceDOList);
        pmsPlanService.deleteMaterial(projectPlanItemId);
        pmsPlanService.saveMaterial(projectPlanItemId,planMaterialDOList);
        pmsPlanService.deleteCombination(projectPlanItemId);
        pmsPlanService.saveCombination(projectPlanItemId,planCombinationDOList);
    }

    /**
     * 发起采购
     * 物料采购计划右侧
     * @param reqVO
     * @return
     */
    @PostMapping("/startPurchase")
    @Operation(summary = "发起采购")
    public CommonResult<Boolean> startPurchase(@RequestBody StartPurchaseReqVO reqVO) {
        Integer type = reqVO.getType();
        Long userId = reqVO.getUserId();
        switch (type){
            case 0:
                List<PlanPurchaseMaterialDO> purchaseMaterialDOList = reqVO.getPurchaseMaterialDOList();
                materialPurchasePlanService.startMiterialConfigPurchase(type, userId,purchaseMaterialDOList);
                break;
            case 1:
                List<PlanDemandDeviceDO> demandDeviceDOList = reqVO.getDemandDeviceDOList();
                materialPurchasePlanService.startDevicePurchase(type, userId, demandDeviceDOList);
                break;
            case 2:
                List<PlanCombinationDO> combinationDOList = reqVO.getCombinationDOList();
                materialPurchasePlanService.startCombinationPurchase(type, userId,combinationDOList);
                break;
            case 3:
                List<PlanDemandMaterialDO> demandMaterialDOList = reqVO.getDemandMaterialDOList();
                materialPurchasePlanService.startMaterialPurchase(type, userId, demandMaterialDOList);
                break;
            case 4:
                List<PlanDemandCutterDO> demandCutterDOList = reqVO.getDemandCutterDOList();
                materialPurchasePlanService.startCutterPurchase(type, userId,demandCutterDOList);
                break;
            case 5:
                List<PlanDemandHiltDO> demandHiltDOList = reqVO.getDemandHiltDOList();
                materialPurchasePlanService.startHiltPurchase(type, userId, demandHiltDOList);
                break;
            default:
                break;
        }
        return success(true);
    }




}
