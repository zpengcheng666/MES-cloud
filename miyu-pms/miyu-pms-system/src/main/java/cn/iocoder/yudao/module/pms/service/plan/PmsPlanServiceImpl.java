package cn.iocoder.yudao.module.pms.service.plan;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import cn.iocoder.yudao.module.pms.controller.admin.materialPurchasePlan.vo.ResourceRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageSaveReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.*;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessmentdevice.AssessmentDeviceDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.assessment.AssessmentMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.assessmentdevice.AssessmentDeviceMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.*;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.service.notifymessage.NotifyMessageService;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.orderForm.McsOrderFormCreateDTO;
import com.miyu.cloud.mcs.dto.outsourcing.McsOutsourcingPlanDTO;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.encodingrule.dto.DemoDTO;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.api.encodingrule.dto.UpdateCodeReqDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.mcc.api.materialtype.MaterialTypeApi;
import com.miyu.module.mcc.api.materialtype.dto.MaterialTypeRespDTO;
import com.miyu.module.pdm.api.processPlan.ProcessPlanApi;
import com.miyu.module.pdm.api.processPlan.dto.ProcessRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.pdm.api.projectAssessment.TechnologyAssessmentApi;
import com.miyu.module.pdm.api.projectAssessment.dto.*;
import com.miyu.module.pdm.api.projectPlan.PdmProjectPlanApi;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomReqDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ReturnMaterialDTO;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import org.springframework.web.bind.annotation.RequestParam;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.module.pms.enums.LogRecordConstants.*;
//import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;


/**
 * 项目计划 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PmsPlanServiceImpl implements PmsPlanService {

    @Resource
    private PmsPlanMapper planMapper;
    @Resource
    private PlanItemMapper planItemMapper;
    @Resource
    private PlanDeviceMapper planDeviceMapper;
    @Resource
    private PlanMaterialMapper planMaterialMapper;
    @Resource
    private PlanCombinationMapper planCombinationMapper;
    @Resource
    private TechnologyAssessmentApi technologyAssessmentApi;

    @Resource
    private PlanDemandHiltMapper planDemandHiltMapper;
    @Resource
    private PlanDemandMaterialMapper planDemandMaterialMapper;
    @Resource
    private PlanDemandDeviceMapper planDemandDeviceMapper;
    @Resource
    private PlanDemandCutterMapper planDemandCutterMapper;
    @Resource
    private PlanPurchaseMaterialMapper planPurchaseMaterialMapper;

    @Resource
    private AssessmentMapper assessmentMapper;
    @Resource
    private AssessmentDeviceMapper assessmentDeviceMapper;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private PdmProjectPlanApi projectPlanApi;
    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;
    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;
    @Resource
    private EncodingRuleApi encodingRuleApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private PmsApprovalMapper approvalMapper;

    /**
     * 对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "pms_plan";
    //生产订单生码
    public static final String GENERATE_CODE = "OR";//PMS_OR

    @Resource
    private BpmProcessInstanceApi processInstanceApi;
    @Resource
    private PmsOrderMapper orderMapper;

    @Resource
    private OrderMaterialRelationMapper orderMaterialRelationMapper;

    @Resource
    private PmsApprovalMapper pmsApprovalMapper;

    @Resource
    private PPOMapper ppoMapper;

    @Resource
    private ProcessPlanApi processPlanApi;

    @Resource
    private ConsignmentReturnApi consignmentReturnApi;

    @Resource
    private NotifyMessageService messageService;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    @Resource
    private ShippingApi shippingApi;

    @Resource
    private MaterialTypeApi materialTypeApi;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPlan(PmsPlanSaveReqVO createReqVO) {
        //查询订单
        final PmsOrderDO pmsOrderDO = orderMapper.selectById(createReqVO.getProjectOrderId());
        if(ObjectUtil.isNull(pmsOrderDO)){
            throw exception(ORDER_NOT_EXISTS);
        }
        // 插入
        PmsPlanDO plan = BeanUtils.toBean(createReqVO, PmsPlanDO.class).setProjectId(pmsOrderDO.getProjectId()).setProjectCode(pmsOrderDO.getProjectCode()).setQuantity(pmsOrderDO.getQuantity()).setProcessCondition(pmsOrderDO.getProcessCondition());
        //整单外协，没有工艺和生产准备
        if(createReqVO.getProcessPreparationTime()==null){
            plan.setProcessPreparationTime(LocalDateTime.now());
            plan.setProductionPreparationTime(LocalDateTime.now());
        }
        planMapper.insert(plan);

        createRelation(pmsOrderDO.getProjectId(),pmsOrderDO.getId(),pmsOrderDO.getQuantity());

        // 插入子表
//        createPlanItemList(plan, createReqVO.getPlanItems());
        //把评审通过的采购数据存储到计划中,如果项目不需要评审，下面的就省了
        PmsApprovalDO pmsApprovalDO = pmsApprovalMapper.selectById(pmsOrderDO.getProjectId());
        //1是需要评审，2是不需要
        if(pmsApprovalDO.getNeedsAssessment()==1){
            createPlanPurchase(pmsOrderDO.getProjectId(),pmsOrderDO.getProjectCode(),plan.getId(),pmsOrderDO.getPartNumber());
        }

        //发起工艺编制
        if(pmsOrderDO.getOutsource()!=1){
            //工艺完成时间
            String checkedData = processPlanApi.pushProcess(pmsOrderDO.getProjectCode(), pmsOrderDO.getPartNumber(), pmsOrderDO.getPartName(), pmsOrderDO.getProcessCondition(),plan.getProcessPreparationTime()).getCheckedData();
        }

        // 返回
        return plan.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPlanBpm(PmsPlanSaveReqVO createReqVO) {
        updatePlan(createReqVO);

        Long userId = getLoginUserId();
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("planId",createReqVO.getId());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(createReqVO.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();
        // 将工作流的编号，更新到 OA 请假单中
        planMapper.updateById(new PmsPlanDO().setId(createReqVO.getId()).setProcessInstanceId(processInstanceId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus()));
        // 返回
        return createReqVO.getId();
    }

    /**
     * 上面的简化版，无需跳转，直接审批
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPlanBpm2(String id) {
        Long userId = getLoginUserId();
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("planId",id);
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(id))
                        .setStartUserSelectAssignees(null)).getCheckedData();
        // 将工作流的编号，更新到 OA 请假单中
        planMapper.updateById(new PmsPlanDO().setId(id).setProcessInstanceId(processInstanceId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus()));
        // 返回
        return id;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PMS_PROJECT_PLAN_TYPE, subType = PMS_PROJECT_PLAN_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PMS_PROJECT_PLAN_UPDATE_SUCCESS)
    public void updatePlan(PmsPlanSaveReqVO updateReqVO) {
        // 校验存在
        PmsPlanDO oldPlan = validatePlanExists(updateReqVO.getId());
        //查询订单
        final PmsOrderDO pmsOrderDO = orderMapper.selectById(updateReqVO.getProjectOrderId());
        // 更新
        PmsPlanDO updateObj = BeanUtils.toBean(updateReqVO, PmsPlanDO.class).setProjectId(pmsOrderDO.getProjectId()).setProjectCode(pmsOrderDO.getProjectCode());
        planMapper.updateById(updateObj);

        // 更新子表(因为子计划现在根本不会改，这个其实没有意义)
        //updatePlanItemList2(updateObj, updateReqVO.getPlanItems());
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldPlan, PmsPlanSaveReqVO.class));
        LogRecordContext.putVariable("plan", oldPlan);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PMS_PROJECT_PLAN_TYPE, subType = PMS_PROJECT_PLAN_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PMS_PROJECT_PLAN_DELETE_SUCCESS)
    public void deletePlan(String id) {
        // 校验存在
        PmsPlanDO pmsPlanDO = validatePlanExists(id);
        orderMaterialRelationMapper.delete(OrderMaterialRelationDO::getOrderId,pmsPlanDO.getProjectOrderId());
        // 删除
        planMapper.deleteById(id);


        // 删除子表
        deletePlanItemByProjectPlanId(id);
        deletePlanDeviceByProjectPlanId(id);
        deletePlanMaterialByProjectPlanId(id);
        deletePlanCombinationByProjectPlanId(id);
        deletePlanDemandDeviceByProjectPlanId(id);
        deletePlanDemandCutterByProjectPlanId(id);
        deletePlanDemandHiltByProjectPlanId(id);
        deletePlanDemandMaterialByProjectPlanId(id);
        deletePlanPurchaseMaterialDOByProjectPlanId(id);
        //记录操作日志上下文
        LogRecordContext.putVariable("plan", pmsPlanDO);
    }

    @Override
    public void updatePlanStatus(String id, Integer status) {
        validatePlanExists(id);
        planMapper.updateById(new PmsPlanDO().setId(id).setStatus(status));
    }

    private PmsPlanDO validatePlanExists(String id) {
        PmsPlanDO pmsPlanDO = planMapper.selectById(id);
        if (pmsPlanDO == null) {
            throw exception(PLAN_NOT_EXISTS);
        }
        return pmsPlanDO;
    }

    @Override
    public PmsPlanDO getPlan(String id) {
        return planMapper.selectById(id);
    }

    @Override
    public PageResult<PmsPlanDO> getPlanPage(PmsPlanPageReqVO pageReqVO) {
        return planMapper.selectPage(pageReqVO);
    }

    @Override
    public List<PmsPlanDO> selectListWith(PmsPlanSaveReqVO req) {
        return planMapper.selectListWith(req);
    }

    /**
     * 批量查询
     * 选料,下单用
     * @param data
     * @return
     */
    @Override
    public PmsPlanHandleVO batchSelect(List<PmsPlanHandleReqVO> data) {
        PmsPlanHandleVO handleVO = new PmsPlanHandleVO();

        //查工艺
        List<String> processSchemes = data.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getProcessScheme());
        }).map(PmsPlanHandleReqVO::getProcessScheme).distinct().collect(Collectors.toList());
        List<ProcessRespDTO> processRespDTOList = new ArrayList<>();
        if(processSchemes.size()>0){
            processRespDTOList = processPlanApi.getInformationList(processSchemes).getCheckedData();
        }
        Map<String, ProcessRespDTO> processRespDTOMap = CollectionUtils.convertMap(processRespDTOList, ProcessRespDTO::getId);
        //根据工艺查物料编码,物料比
        for (PmsPlanHandleReqVO vo : data) {
            vo.setGroupSise(1);
            vo.setProjectId(vo.getProjectId());
            if(processRespDTOMap.containsKey(vo.getProcessScheme())){
                vo.setGroupSise(Integer.valueOf(processRespDTOMap.get(vo.getProcessScheme()).getGroupSize()));
                vo.setMaterialNumber(processRespDTOMap.get(vo.getProcessScheme()).getMaterialNumber());
            }
        }
        //查物料编号
        List<String> materialNumbers = data.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getMaterialNumber());
        }).map(PmsPlanHandleReqVO::getMaterialNumber).distinct().collect(Collectors.toList());
        //根据物料编码查可用库存
        List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByCode(materialNumbers).getCheckedData();
        List<String> materialIds = materialConfigList.stream().map(MaterialConfigRespDTO::getId).collect(Collectors.toList());
        //查每条需要的库存
        List<MaterialStockRespDTO> materialStockListTemp = materialStockApi.getOutOrderMaterialsByConfigIds(materialIds).getCheckedData();
        List<MaterialStockRespDTO> materialStockList = materialStockListTemp.stream().filter((item) -> {return ObjectUtil.isNotNull(item.getMaterialStatus())&&item.getMaterialStatus() == 2;}).collect(Collectors.toList());
        //排除被占用的库存,barCodeList是全部条码
        List<String> barCodeList = materialStockList.stream().map(MaterialStockRespDTO::getBarCode).collect(Collectors.toList());
        List<OrderMaterialRelationDO> relationByBarCodeList = orderMaterialRelationMapper.selectListByBarCode(barCodeList);
        //已存在关系的条码
        List<String> materialCodeList = relationByBarCodeList.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.toList());
        //已退货的条码
        List<ReturnMaterialDTO> ReturnMaterialList = consignmentReturnApi.getReturnByCodes(barCodeList).getCheckedData();
        List<String> returnCodeList = ReturnMaterialList.stream().filter(item -> ObjectUtil.isNotNull(item.getBarCode())).map(ReturnMaterialDTO::getBarCode).collect(Collectors.toList());
        materialCodeList.addAll(returnCodeList);
        //排除
        List<MaterialStockRespDTO> stockList = materialStockList.stream().filter((item) -> {
            return !(materialCodeList.contains(item.getBarCode()));
        }).collect(Collectors.toList());

        //测试假数据
//        List<MaterialStockRespDTO>  testStock= getTestStock();
//        stockList.addAll(testStock);

        //将库存按物料编号分类
        Map<String,List<MaterialStockRespDTO>> stockMap = new HashMap<>();
        for (MaterialStockRespDTO materialStockRespDTO : stockList) {
            if(stockMap.containsKey(materialStockRespDTO.getMaterialNumber())){
                stockMap.get(materialStockRespDTO.getMaterialNumber()).add(materialStockRespDTO);
            }else {
                List<MaterialStockRespDTO> list = new ArrayList<>();
                list.add(materialStockRespDTO);
                stockMap.put(materialStockRespDTO.getMaterialNumber(),list);
            }
        }

        //计算每条数据还需要的物料
        List<PmsPlanHandleReqVO> pmsPlanHandleReqVOS = computeRequire(data,stockMap);
        //查看是否已发起采购
        //List<PmsPlanHandleReqVO> pmsPlanHandleReqVOS = computeHasPurchase(pmsPlanHandleReqVO1);
        //赋值
        List<PmsPlanHandleRespVO> pmsPlanHandleRespVOS = BeanUtils.toBean(pmsPlanHandleReqVOS, PmsPlanHandleRespVO.class);
        handleVO.setPlanHandleList(pmsPlanHandleRespVOS);
        handleVO.setStockMap(stockMap);
        handleVO.setStockList(stockList);
        return handleVO;
    }

//    @SneakyThrows
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String batchPurchaseAndOrder(List<PmsPlanHandleReqVO> data) {
//        //每个计划都要判定采购和下单
//        for (PmsPlanHandleReqVO val : data) {
//            //下单
//            if(val.getMaterialCodeList().size()>0){
//                List<PlanMaterialStock> materialCodeList = val.getMaterialCodeList();
//                List<String> list = materialCodeList.stream().map(PlanMaterialStock::getBarCode).collect(Collectors.toList());
//
//                //有码，先要初始化码
//                orderMaterialInitBatch(list,val.getProjectOrderId(),val.getPlanId());
//                //初始化后下单
//                PmsPlanItemReqVO pmsPlanItemReqVO = BeanUtils.toBean(val, PmsPlanItemReqVO.class);
//                pmsPlanItemReqVO.setProjectCode(val.getProjectCode());
//                pmsPlanItemReqVO.setProjectPlanId(val.getPlanId());
//                pmsPlanItemReqVO.setProjectId(val.getId());
//                createPlanItem(pmsPlanItemReqVO);
//            }
//            //采购,大于0的才采购
//            if(val.getPurchaseAmount()>0&&StrUtil.isNotEmpty(val.getMaterialNumber())){
//                List<PlanPurchaseMaterialDO> planPurchaseMaterialDOS = planPurchaseMaterialMapper.selectListByProjectPlanId(val.getPlanId());
//                //没采购过的才采购
//                if(planPurchaseMaterialDOS.size()==0){
//                    MaterialPurchsePlanReqVO materialPurchsePlanReqVO = BeanUtils.toBean(val, MaterialPurchsePlanReqVO.class);
//                    materialPurchsePlanReqVO.setProjectId(val.getId());
//                    materialPurchsePlanReqVO.setProjectPlanId(val.getPlanId());
//                    createMaterialPurchasePlan(materialPurchsePlanReqVO);
//                }
//
//            }
//        }
//        return "ok";
//    }

    /**
     * 批量采购,只需要计算出采购数量即可
     * 先查库存,不用管是否被占用,再查采购记录,加起来和需求比，少的要买,得计算是哪个少了
     * @param data
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchPurchase(List<PmsPlanHandleReqVO> data) {
        //先把选中的码存上
        for (PmsPlanHandleReqVO  val: data) {
            //下单
            if(val.getMaterialCodeList().size()>0){
                List<PlanMaterialStock> materialCodeList = val.getMaterialCodeList();
                List<String> list = materialCodeList.stream().map(PlanMaterialStock::getBarCode).collect(Collectors.toList());
                //有码，先要初始化码
                orderMaterialInitBatch(list,val.getProjectOrderId(),val.getPlanId());
            }
        }

        //只保留带有毛坯的数据
        List<PmsPlanHandleReqVO> list = data.stream().filter((item)->{
            return StrUtil.isNotEmpty(item.getMaterialNumber());
        }).collect(Collectors.toList());
        if (list.size()==0){
            return "false";
        }
        //毛坯物料编号集合
        //List<String> materialNumberList = list.stream().map(PmsPlanHandleReqVO::getMaterialNumber).distinct().collect(Collectors.toList());
        List<String> planIds = list.stream().map(PmsPlanHandleReqVO::getPlanId).collect(Collectors.toList());
        List<String> projectIds = list.stream().map(PmsPlanHandleReqVO::getProjectId).distinct().collect(Collectors.toList());

        //物料类型存储(key为MaterialNumber)
//        Map<String,MaterialConfigRespDTO> materialConfigMap = new HashMap<>();
//        List<String> materialConfigIds  = new ArrayList<>();
//        for (String materialNumber : materialNumberList) {
//            //用物料牌号,查询物料类型
//            MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
//            dto.setMaterialNumber(materialNumber);
//            List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
//            MaterialConfigRespDTO materialConfigRespDTO = materialConfigList.get(0);
//            materialConfigMap.put(materialNumber,materialConfigRespDTO);
//            materialConfigIds.add(materialConfigRespDTO.getId());
//        }

        //物码存储
        //List<MaterialStockRespDTO> stockCodeList = materialStockApi.getOutOrderMaterialsByConfigIds(materialConfigIds).getCheckedData();

        //根据计划ids查询关系
        //只需要查关系上的码+采购计划就能判断出哪个计划缺物料,还得减去已经到货的数量,不然数量就超了
        //如果上面加起来发现比需求多了怎么办，那种说明用了别人料,别人再买物料少买即可,确保总数正常。
        List<OrderMaterialRelationDO> relationList = getRelationByPlanIds(planIds);
        //这部分确认选用的数量
        Map<String,List<OrderMaterialRelationDO>> orderMaterialMap = new HashMap<>();
        for (OrderMaterialRelationDO relationDO : relationList) {
            if(orderMaterialMap.containsKey(relationDO.getPlanId())){
                orderMaterialMap.get(relationDO.getPlanId()).add(relationDO);
            }else {
                List<OrderMaterialRelationDO> relationDOS = new ArrayList<>();
                relationDOS.add(relationDO);
                orderMaterialMap.put(relationDO.getPlanId(),relationDOS);
            }

        }

        //这部分查询采购的数量,计算每个计划采购量
        List<PlanPurchaseMaterialDO> purchaseList = getPurchaseByPlanIds(planIds);
        Map<String,Integer> purchaseAmountMap = new HashMap<>();
        for (PlanPurchaseMaterialDO planPurchaseMaterialDO : purchaseList) {
            if(purchaseAmountMap.containsKey(planPurchaseMaterialDO.getProjectPlanId())){
                if(ObjectUtil.isNotNull(planPurchaseMaterialDO.getPurchaseAmount())){
                    Integer integer = purchaseAmountMap.get(planPurchaseMaterialDO.getProjectPlanId());
                    purchaseAmountMap.put(planPurchaseMaterialDO.getProjectPlanId(),integer + planPurchaseMaterialDO.getPurchaseAmount());
                }
            }else {
                if(ObjectUtil.isNotNull(planPurchaseMaterialDO.getPurchaseAmount())){
                    purchaseAmountMap.put(planPurchaseMaterialDO.getProjectPlanId(),planPurchaseMaterialDO.getPurchaseAmount());
                }else {
                    purchaseAmountMap.put(planPurchaseMaterialDO.getProjectPlanId(),0);
                }
            }
        }

        //这部分到货的数量,去查采购收货明细
        List<PurchaseConsignmentDetailDTO> consignmentDetailListTemp = purchaseConsignmentApi.getPurchaseDetailListByProjectIds(projectIds).getCheckedData();
        List<PurchaseConsignmentDetailDTO> consignmentDetailList = consignmentDetailListTemp.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getConsignmentStatus()) && (item.getConsignmentStatus() == 6);
        }).collect(Collectors.toList());
        //这部分查退货，根据到货的id查
        List<String> consignmentIds = consignmentDetailList.stream().map(PurchaseConsignmentDetailDTO::getId).collect(Collectors.toList());
        List<String>  consignmentReturnIds = new ArrayList<>();
        if(consignmentIds.size()>0){
            List<ShippingDetailDTO> ShippingDetailList = shippingApi.getShippingByConsignmentDetailIds(consignmentIds).getCheckedData();
            for (ShippingDetailDTO shippingDetailDTO : ShippingDetailList) {
                consignmentReturnIds.add(shippingDetailDTO.getConsignmentDetailId());
            }
        }

        Map<String,List<PurchaseConsignmentDetailDTO>> consignmentMap = new HashMap<>();
        for (PurchaseConsignmentDetailDTO purchaseConsignmentDetailDTO : consignmentDetailList) {
            //如果包含在退货中,这个收货就不能算,直接跳过
            if(consignmentReturnIds.contains(purchaseConsignmentDetailDTO.getId())){
                continue;
            }
            if(consignmentMap.containsKey(purchaseConsignmentDetailDTO.getProjectPlanId())){
                consignmentMap.get(purchaseConsignmentDetailDTO.getProjectPlanId()).add(purchaseConsignmentDetailDTO);
            }else {
                List<PurchaseConsignmentDetailDTO> purchseList = new ArrayList<>();
                purchseList.add(purchaseConsignmentDetailDTO);
                consignmentMap.put(purchaseConsignmentDetailDTO.getProjectPlanId(),purchseList);
            }
        }



        //计算每个计划缺少的物料
        List<PmsPlanHandleReqVO> finalPurchseList = computePurchase(list, orderMaterialMap, purchaseAmountMap, consignmentMap);
        for (PmsPlanHandleReqVO pmsPlanHandleReqVO : finalPurchseList) {
            MaterialPurchsePlanReqVO materialPurchsePlanReqVO = BeanUtils.toBean(pmsPlanHandleReqVO, MaterialPurchsePlanReqVO.class);
            materialPurchsePlanReqVO.setProjectId(pmsPlanHandleReqVO.getId());
            materialPurchsePlanReqVO.setProjectPlanId(pmsPlanHandleReqVO.getPlanId());
            createMaterialPurchasePlan(materialPurchsePlanReqVO);
        }
        return "ok";
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String batchPurchase2(List<PmsPlanHandleReqVO> data) {
        //先把选中的码存上
        for (PmsPlanHandleReqVO  val: data) {
            if(val.getMaterialCodeList().size()>0){
                List<PlanMaterialStock> materialCodeList = val.getMaterialCodeList();
                List<String> list = materialCodeList.stream().map(PlanMaterialStock::getBarCode).collect(Collectors.toList());
                //有码，先要初始化码
                orderMaterialInitBatch(list,val.getProjectOrderId(),val.getPlanId());
            }
        }

        //只保留带有毛坯的数据
        List<PmsPlanHandleReqVO> list = data.stream().filter((item)->{
            return StrUtil.isNotEmpty(item.getMaterialNumber());
        }).collect(Collectors.toList());
        if (list.size()==0){
            return "false";
        }

        List<String> materialNumberList = list.stream().map(PmsPlanHandleReqVO::getMaterialNumber).distinct().collect(Collectors.toList());
        List<String> planIds = list.stream().map(PmsPlanHandleReqVO::getPlanId).collect(Collectors.toList());
        List<String> projectIds = list.stream().map(PmsPlanHandleReqVO::getProjectId).distinct().collect(Collectors.toList());
        //物料类型存储(key为MaterialId,value为MaterialNumber)
        //Map<String,MaterialConfigRespDTO> materialConfigMap = new HashMap<>();
        Map<String,String> materialConfigMap = new HashMap<>();
        List<String> materialConfigIds  = new ArrayList<>();
        for (String materialNumber : materialNumberList) {
            //用物料牌号,查询物料类型
            MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
            dto.setMaterialNumber(materialNumber);
            List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
            MaterialConfigRespDTO materialConfigRespDTO = materialConfigList.get(0);
            materialConfigMap.put(materialConfigRespDTO.getId(),materialNumber);
            materialConfigIds.add(materialConfigRespDTO.getId());
        }
        //库存(未过滤)
        List<MaterialStockRespDTO> materialStockListTepm = materialStockApi.getOutOrderMaterialsByConfigIds(materialConfigIds).getCheckedData();
        //占用关系
        List<OrderMaterialRelationDO> relationList = getRelationByPlanIds(planIds);

        //查库存占用
        List<String> relationCodeList = new ArrayList<>();
        if(materialStockListTepm.size()>0){
            List<String> stockCodeList = materialStockListTepm.stream().map(MaterialStockRespDTO::getBarCode).collect(Collectors.toList());
            LambdaQueryWrapper<OrderMaterialRelationDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(OrderMaterialRelationDO::getMaterialCode, stockCodeList);
            List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(wrapper);
            for (OrderMaterialRelationDO relationDO : relationDOS) {
                relationCodeList.add(relationDO.getMaterialCode());
            }

        }
        //未被占用的库存
        List<MaterialStockRespDTO> materialStockList = materialStockListTepm.stream().filter((item) -> {
            return (!relationCodeList.contains(item.getBarCode()))&&ObjectUtil.isNotNull(item.getMaterialStatus())&&(item.getMaterialStatus()==2);
        }).collect(Collectors.toList());

        //组装对于物料map (物料编号,数量) ,剩余的库存
        Map<String,Integer> remainingMap = new HashMap<>();
        for (MaterialStockRespDTO dto : materialStockList) {
            if(materialConfigMap.containsKey(dto.getMaterialConfigId())){
                String materialNumber = materialConfigMap.get(dto.getMaterialConfigId());
                if(remainingMap.containsKey(materialNumber)){
                    Integer remain = remainingMap.get(materialNumber);
                    remainingMap.put(materialNumber,(remain + 1));
                }else {
                    remainingMap.put(materialNumber,1);
                }
            }
        }


        //这部分确认选用的数量
        Map<String,List<OrderMaterialRelationDO>> orderMaterialMap = new HashMap<>();
        for (OrderMaterialRelationDO relationDO : relationList) {
            if(orderMaterialMap.containsKey(relationDO.getPlanId())){
                orderMaterialMap.get(relationDO.getPlanId()).add(relationDO);
            }else {
                List<OrderMaterialRelationDO> relationDOS = new ArrayList<>();
                relationDOS.add(relationDO);
                orderMaterialMap.put(relationDO.getPlanId(),relationDOS);
            }

        }

        //这部分查询采购的数量,计算每个计划采购量
        List<PlanPurchaseMaterialDO> purchaseList = getPurchaseByPlanIds(planIds);
        Map<String,Integer> purchaseAmountMap = new HashMap<>();
        for (PlanPurchaseMaterialDO planPurchaseMaterialDO : purchaseList) {
            if(purchaseAmountMap.containsKey(planPurchaseMaterialDO.getProjectPlanId())){
                if(ObjectUtil.isNotNull(planPurchaseMaterialDO.getPurchaseAmount())){
                    Integer integer = purchaseAmountMap.get(planPurchaseMaterialDO.getProjectPlanId());
                    purchaseAmountMap.put(planPurchaseMaterialDO.getProjectPlanId(),integer + planPurchaseMaterialDO.getPurchaseAmount());
                }
            }else {
                if(ObjectUtil.isNotNull(planPurchaseMaterialDO.getPurchaseAmount())){
                    purchaseAmountMap.put(planPurchaseMaterialDO.getProjectPlanId(),planPurchaseMaterialDO.getPurchaseAmount());
                }else {
                    purchaseAmountMap.put(planPurchaseMaterialDO.getProjectPlanId(),0);
                }
            }
        }

        //这部分到货的数量,去查采购收货明细
        List<PurchaseConsignmentDetailDTO> consignmentDetailListTemp = purchaseConsignmentApi.getPurchaseDetailListByProjectIds(projectIds).getCheckedData();
        List<PurchaseConsignmentDetailDTO> consignmentDetailList = consignmentDetailListTemp.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getConsignmentStatus()) && (item.getConsignmentStatus() == 6);
        }).collect(Collectors.toList());
        //这部分查退货，根据到货的id查
        List<String> consignmentIds = consignmentDetailList.stream().map(PurchaseConsignmentDetailDTO::getId).collect(Collectors.toList());
        List<String>  consignmentReturnIds = new ArrayList<>();
        if(consignmentIds.size()>0){
            List<ShippingDetailDTO> ShippingDetailList = shippingApi.getShippingByConsignmentDetailIds(consignmentIds).getCheckedData();
            for (ShippingDetailDTO shippingDetailDTO : ShippingDetailList) {
                consignmentReturnIds.add(shippingDetailDTO.getConsignmentDetailId());
            }
        }

        Map<String,List<PurchaseConsignmentDetailDTO>> consignmentMap = new HashMap<>();
        for (PurchaseConsignmentDetailDTO purchaseConsignmentDetailDTO : consignmentDetailList) {
            //如果包含在退货中,这个收货就不能算,直接跳过
            if(consignmentReturnIds.contains(purchaseConsignmentDetailDTO.getId())){
                continue;
            }
            if(consignmentMap.containsKey(purchaseConsignmentDetailDTO.getProjectPlanId())){
                consignmentMap.get(purchaseConsignmentDetailDTO.getProjectPlanId()).add(purchaseConsignmentDetailDTO);
            }else {
                List<PurchaseConsignmentDetailDTO> purchseList = new ArrayList<>();
                purchseList.add(purchaseConsignmentDetailDTO);
                consignmentMap.put(purchaseConsignmentDetailDTO.getProjectPlanId(),purchseList);
            }
        }

        //按计划来,采购的量 - 到货的量 + 占用的量 = totalRequire,等于说明不用采,
        //采购的量 - 到货的量 = 还未到的数量 占用的量+还未到的量 应该>= totalRequire ,小了说明没够还得采
        //但是有可能只是还在库存中,所以可以用库存顶采购


        //计算每个计划缺少的物料
        List<PmsPlanHandleReqVO> finalPurchseList = computePurchase2(list, orderMaterialMap, purchaseAmountMap, consignmentMap,remainingMap);
        for (PmsPlanHandleReqVO pmsPlanHandleReqVO : finalPurchseList) {
            MaterialPurchsePlanReqVO materialPurchsePlanReqVO = BeanUtils.toBean(pmsPlanHandleReqVO, MaterialPurchsePlanReqVO.class);
            materialPurchsePlanReqVO.setProjectId(pmsPlanHandleReqVO.getId());
            materialPurchsePlanReqVO.setProjectPlanId(pmsPlanHandleReqVO.getPlanId());
            createMaterialPurchasePlan(materialPurchsePlanReqVO);
        }
        return "ok";
    }

    /**
     * 计算采购数量2
     * @param list
     * @return
     */
    public List<PmsPlanHandleReqVO> computePurchase2(List<PmsPlanHandleReqVO> list,
                                                    Map<String,List<OrderMaterialRelationDO>> relationMap,
                                                    Map<String,Integer> purchaseAmountMap,
                                                    Map<String,List<PurchaseConsignmentDetailDTO>> consignmentMap,
                                                    Map<String,Integer> remainMap
    ){

        List<PmsPlanHandleReqVO> purchaseList = new ArrayList<>();
        List<PmsPlanHandleReqVO> finalPurchaseList = new ArrayList<>();
        //物料编号,数量 如果使用了别人的料，总体会多，反之会少,用这个平衡一下
        for (PmsPlanHandleReqVO pmsPlanHandleReqVO : list) {
            //需求量
            int require = pmsPlanHandleReqVO.getTotalRequire();
            //已选数量
            int selectNum = 0;
            //采购单数量
            int purchaseNum = 0;
            //到货数量
            int consignNum = 0;
            if(relationMap.containsKey(pmsPlanHandleReqVO.getPlanId())){
                selectNum = relationMap.get(pmsPlanHandleReqVO.getPlanId()).size();
            }
            if(purchaseAmountMap.containsKey(pmsPlanHandleReqVO.getPlanId())){
                purchaseNum = purchaseAmountMap.get(pmsPlanHandleReqVO.getPlanId());
            }
            if(consignmentMap.containsKey(pmsPlanHandleReqVO.getPlanId())){
                consignNum = consignmentMap.get(pmsPlanHandleReqVO.getPlanId()).size();
            }
            int sum = selectNum + purchaseNum - consignNum;
            //大于等于都无需采购

            if(sum >= require){
                //这个物料在此主计划多了几个，其他主计划采购就可以少才几个
                if(remainMap.containsKey(pmsPlanHandleReqVO.getMaterialNumber())){
                    Integer surplus = remainMap.get(pmsPlanHandleReqVO.getMaterialNumber());
                    remainMap.put(pmsPlanHandleReqVO.getMaterialNumber(),surplus + sum - require);
                }else {
                    remainMap.put(pmsPlanHandleReqVO.getMaterialNumber(),sum - require);
                }

            }else {
                purchaseList.add(pmsPlanHandleReqVO);
                //这种需要采购
                int amount = 0;
                amount = require - sum;
                //初步采购数量
                pmsPlanHandleReqVO.setPurchaseAmount(amount);
            }
        }
        //在这里计算采购数量
        for (PmsPlanHandleReqVO planHandleReqVO : purchaseList) {
            //采购前看看别的计划是不是多了,多了就少采购
            if(remainMap.containsKey(planHandleReqVO.getMaterialNumber())){
                Integer surplus = remainMap.get(planHandleReqVO.getMaterialNumber());
                int amount = planHandleReqVO.getPurchaseAmount();
                if(surplus >= amount){
                    surplus = surplus - amount;
                    amount = 0;
                }else {
                    amount = amount - surplus;
                    surplus = 0;
                    //这里就是最终要采购的数量了
                    planHandleReqVO.setPurchaseAmount(amount);
                    finalPurchaseList.add(planHandleReqVO);
                }
                remainMap.put(planHandleReqVO.getMaterialNumber(),surplus);
            }else {
                //之前没有这方面的剩余，那该多少就是多少,用之前的初步采购就行
                finalPurchaseList.add(planHandleReqVO);
            }
        }
        return finalPurchaseList;
    }

    /**
     * 计算采购数量
     * @param list
     * @return
     */
    public List<PmsPlanHandleReqVO> computePurchase(List<PmsPlanHandleReqVO> list,
                                                    Map<String,List<OrderMaterialRelationDO>> relationMap,
                                                    Map<String,Integer> purchaseAmountMap,
                                                    Map<String,List<PurchaseConsignmentDetailDTO>> consignmentMap
                                                    ){

        List<PmsPlanHandleReqVO> purchaseList = new ArrayList<>();
        List<PmsPlanHandleReqVO> finalPurchaseList = new ArrayList<>();
        //物料编号,数量 如果使用了别人的料，总体会多，反之会少,用这个平衡一下
        Map<String,Integer> map = new HashMap<>();
        for (PmsPlanHandleReqVO pmsPlanHandleReqVO : list) {
            //需求量
            //int require = pmsPlanHandleReqVO.getRequire();
            int require = pmsPlanHandleReqVO.getTotalRequire();
            //已选数量
            int selectNum = 0;
            //采购单数量
            int purchaseNum = 0;
            //到货数量
            int consignNum = 0;
            if(relationMap.containsKey(pmsPlanHandleReqVO.getPlanId())){
                selectNum = relationMap.get(pmsPlanHandleReqVO.getPlanId()).size();
            }
            if(purchaseAmountMap.containsKey(pmsPlanHandleReqVO.getPlanId())){
                purchaseNum = purchaseAmountMap.get(pmsPlanHandleReqVO.getPlanId());
            }
            if(consignmentMap.containsKey(pmsPlanHandleReqVO.getPlanId())){
                consignNum = consignmentMap.get(pmsPlanHandleReqVO.getPlanId()).size();
            }
            int sum = selectNum + purchaseNum - consignNum;
            //大于等于都无需采购

            if(sum >= require){
                //这个物料在此主计划多了几个，其他主计划采购就可以少才几个
                if(map.containsKey(pmsPlanHandleReqVO.getMaterialNumber())){
                    Integer surplus = map.get(pmsPlanHandleReqVO.getMaterialNumber());
                    map.put(pmsPlanHandleReqVO.getMaterialNumber(),surplus + sum - require);
                }else {
                    map.put(pmsPlanHandleReqVO.getMaterialNumber(),sum - require);
                }

            }else {
                purchaseList.add(pmsPlanHandleReqVO);
                //这种需要采购
                int amount = 0;
                amount = require - sum;
                //初步采购数量
                pmsPlanHandleReqVO.setPurchaseAmount(amount);
                //采购前看看别的计划是不是多了,多了就少采购
//                if(map.containsKey(pmsPlanHandleReqVO.getMaterialNumber())){
//                    Integer surplus = map.get(pmsPlanHandleReqVO.getMaterialNumber());
//                    if(surplus >= amount){
//                        surplus = surplus - amount;
//                        amount = 0;
//                    }else {
//                        amount = amount - surplus;
//                        surplus = 0;
//                    }
//                    map.put(pmsPlanHandleReqVO.getMaterialNumber(),surplus);
//                }
//                if(amount>0){
//                    pmsPlanHandleReqVO.setPurchaseAmount(amount);
//                    purchaseList.add(pmsPlanHandleReqVO);
//                }

            }
        }
        //在这里计算采购数量
        for (PmsPlanHandleReqVO planHandleReqVO : purchaseList) {
            //采购前看看别的计划是不是多了,多了就少采购
            if(map.containsKey(planHandleReqVO.getMaterialNumber())){
                Integer surplus = map.get(planHandleReqVO.getMaterialNumber());
                int amount = planHandleReqVO.getPurchaseAmount();
                if(surplus >= amount){
                    surplus = surplus - amount;
                    amount = 0;
                }else {
                    amount = amount - surplus;
                    surplus = 0;
                    //这里就是最终要采购的数量了
                    planHandleReqVO.setPurchaseAmount(amount);
                    finalPurchaseList.add(planHandleReqVO);
                }
                map.put(planHandleReqVO.getMaterialNumber(),surplus);
            }else {
                //之前没有这方面的剩余，那该多少就是多少,用之前的初步采购就行
                finalPurchaseList.add(planHandleReqVO);
            }
        }
        return finalPurchaseList;
    }

    /**
     * 根据计划ids查询关系
     * @param ids
     * @return
     */
    public List<PlanPurchaseMaterialDO> getPurchaseByPlanIds(List<String> ids){
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<PlanPurchaseMaterialDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanPurchaseMaterialDO::getProjectPlanId,ids);
        //类型1是物料采购，其余不是
        wrapperX.eq(PlanPurchaseMaterialDO::getPlanType,1);
        return planPurchaseMaterialMapper.selectList(wrapperX);
    }
    /**
     * 根据计划ids查询采购数量
     * @param ids
     * @return
     */
    public List<OrderMaterialRelationDO> getRelationByPlanIds(List<String> ids){
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(OrderMaterialRelationDO::getPlanId,ids);
        return orderMaterialRelationMapper.selectList(wrapperX);
    }

    //初始化码
    public String orderMaterialInit(List<String> list,String orderId) {
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, orderId);
        //存储空关系
        for (OrderMaterialRelationDO orderMaterialRelationDO : relationDOList) {
            //没有物料码就结束循环
            if(list.size()==0){
                break;
            }
            //遍历关系找到物料码为空的
            if(ObjectUtil.isEmpty(orderMaterialRelationDO.getMaterialCode())){
                String materialCode = list.get(0);
                orderMaterialRelationDO.setMaterialCode(materialCode).setVariableCode(materialCode).setMaterialStatus(1);
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                list.remove(0);
            }
        }
        return "ok";
    }
    //批量下单用初始化码,得查工序
    public String orderMaterialInitBatch(List<String> list,String orderId,String planId) {
        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCodes(list).getCheckedData();
        Map<String, MaterialStockRespDTO> stringMaterialStockRespDTOMap = CollectionUtils.convertMap(materialStockList, MaterialStockRespDTO::getBarCode);

        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, orderId);
        PmsPlanDO pmsPlanDO = planMapper.selectById(planId);
        String step = null;
        if(pmsPlanDO.getProcessScheme()!=null){
            List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(pmsPlanDO.getProcessScheme()).getCheckedData();
            ProcedureRespDTO procedureRespDTO = procedureList.get(0);
            step = procedureRespDTO.getProcedureNum();
        }
        //存储空关系
        for (OrderMaterialRelationDO orderMaterialRelationDO : relationDOList) {
            //没有物料码就结束循环
            if(list.size()==0){
                break;
            }
            //遍历关系找到物料码为空的
            if(ObjectUtil.isEmpty(orderMaterialRelationDO.getMaterialCode())){
                String materialCode = list.get(0);
                if(stringMaterialStockRespDTOMap.containsKey(materialCode)){
                    MaterialStockRespDTO dto = stringMaterialStockRespDTOMap.get(materialCode);
                    orderMaterialRelationDO.setMaterialTypeId(dto.getMaterialConfigId());
                }
                orderMaterialRelationDO.setMaterialCode(materialCode).setVariableCode(materialCode).setMaterialStatus(1).setStep(step).setPlanId(planId);
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                list.remove(0);
            }
        }
        return "ok";
    }

    public List<PmsPlanHandleReqVO> computeRequire(List<PmsPlanHandleReqVO> list,Map<String,List<MaterialStockRespDTO>> map){
        List<String> orderIds = list.stream().map(PmsPlanHandleReqVO::getProjectOrderId).collect(Collectors.toList());
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(OrderMaterialRelationDO::getOrderId,orderIds);
        List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(wrapperX);
        for (PmsPlanHandleReqVO pmsPlanHandleReqVO : list) {
            //整单外协且不带料,需求为0
            if(pmsPlanHandleReqVO.getOutsource()==1&&pmsPlanHandleReqVO.getProcessType()==2){
                pmsPlanHandleReqVO.setRequire(0);
                pmsPlanHandleReqVO.setTotalRequire(0);
            }else {
                int require = pmsPlanHandleReqVO.getQuantity()/pmsPlanHandleReqVO.getGroupSise();
                int totalRequire = pmsPlanHandleReqVO.getQuantity()/pmsPlanHandleReqVO.getGroupSise();
                //这些是使用的物料
                List<OrderMaterialRelationDO> relationList = relationDOS.stream().filter((item) -> {
                    return item.getOrderId().equals(pmsPlanHandleReqVO.getProjectOrderId())&& StrUtil.isNotEmpty(item.getMaterialCode())&&item.getMaterialStatus()!=7&&item.getMaterialStatus()!=8;
                }).collect(Collectors.toList());
                require = require - relationList.size();
                if(require<0){
                    require = 0;
                }
                pmsPlanHandleReqVO.setRequire(require);
                pmsPlanHandleReqVO.setTotalRequire(totalRequire);
            }
            if(ObjectUtil.isNotNull(pmsPlanHandleReqVO.getMaterialNumber())&&map.containsKey(pmsPlanHandleReqVO.getMaterialNumber())){
                pmsPlanHandleReqVO.setInventory(map.get(pmsPlanHandleReqVO.getMaterialNumber()).size());
            }else {
                pmsPlanHandleReqVO.setInventory(0);
            }
            //这个不展示了,不设置也没问题,采购数量不从前端传，而是后端计算
            pmsPlanHandleReqVO.setPurchaseAmount(pmsPlanHandleReqVO.getRequire());
            if(ObjectUtil.isNotNull(pmsPlanHandleReqVO.getMaterialNumber())){
                pmsPlanHandleReqVO.setPurchaseAmount(pmsPlanHandleReqVO.getRequire());
            }else {
                pmsPlanHandleReqVO.setPurchaseAmount(0);
            }

        }

        return list;
    }

    public List<PmsPlanHandleReqVO> computeHasPurchase(List<PmsPlanHandleReqVO> list){
        List<String> planIds = list.stream().filter(item -> ObjectUtil.isNotNull(item.getPlanId())).map(PmsPlanHandleReqVO::getPlanId).distinct().collect(Collectors.toList());
        if(planIds.size()>0){
            LambdaQueryWrapperX<PlanPurchaseMaterialDO> wrapperX = new LambdaQueryWrapperX<>();
            wrapperX.in(PlanPurchaseMaterialDO::getProjectPlanId,planIds);
            List<PlanPurchaseMaterialDO> planPurchaseMaterialDOS = planPurchaseMaterialMapper.selectList(wrapperX);
            Map<String,Integer> map = new HashMap<>();
            for (PlanPurchaseMaterialDO planPurchaseMaterialDO : planPurchaseMaterialDOS) {
                if(map.containsKey(planPurchaseMaterialDO.getProjectPlanId())){
                    map.put(planPurchaseMaterialDO.getProjectPlanId(),map.get(planPurchaseMaterialDO.getProjectPlanId())+1);
                }else {
                    map.put(planPurchaseMaterialDO.getProjectPlanId(),1);
                }
            }
            for (PmsPlanHandleReqVO pmsPlanHandleReqVO : list) {
                if(map.containsKey(pmsPlanHandleReqVO.getPlanId())){
                    pmsPlanHandleReqVO.setHasPurchase(map.get(pmsPlanHandleReqVO.getPlanId()));
                }else {
                    pmsPlanHandleReqVO.setHasPurchase(0);
                }
            }
        }
        return list;
    }

    //测试数据
//    public List<MaterialStockRespDTO> getTestStock(){
//        List<MaterialStockRespDTO> list = new ArrayList<>();
//        for (int i = 0; i < 10; i++) {
//            MaterialStockRespDTO dto = new MaterialStockRespDTO();
//            dto.setMaterialNumber("SAMCMTGJMP0003");
//            dto.setId("A"+i);
//            dto.setBarCode(dto.getMaterialNumber()+"-"+i);
//            list.add(dto);
//        }
//
//        for (int i = 0; i < 10; i++) {
//            MaterialStockRespDTO dto = new MaterialStockRespDTO();
//            dto.setMaterialNumber("SAMCMTGJMP0001");
//            dto.setId("B"+i);
//            dto.setBarCode(dto.getMaterialNumber()+"-"+i);
//            list.add(dto);
//        }
//        return list;
//    }



    @Override
    public PageResult<OrderWithPlan> getPPOPage(PmsPlanPageReqVO pageReqVO) {
        return ppoMapper.selectPagePPO(pageReqVO);
    }

    @Override
    public List<OrderWithPlan> getPlanToDo() {
        return ppoMapper.selectPlanToDo();
    }

    /**
     * 获取项目计划,物料采购计划中资源列表(设备那些)
     * @param projectPlanId
     * @return
     */
    @Override
    public ResourceRespVO getResource(String projectPlanId) {
        ResourceRespVO respVO = new ResourceRespVO();
//        List<PlanDeviceDO> planDeviceDOList = getPlanDeviceListByProjectPlanItemId(projectPlanItemId);
//        respVO.setDeviceDOList(planDeviceDOList);
//        List<PlanMaterialDO> planMaterialList = getPlanMaterialListByProjectPlanItemId(projectPlanItemId);
//        respVO.setMaterialDOList(planMaterialList);
//        List<PlanCombinationDO> planCombinationList = getPlanCombinationListByProjectPlanItemId(projectPlanItemId);
//        respVO.setCombinationDOList(planCombinationList);
        List<PlanDemandDeviceDO> planDemandDeviceList = getPlanDemandDeviceListByProjectPlanId(projectPlanId);
        respVO.setDemandDeviceDOList(planDemandDeviceList);
        List<PlanDemandCutterDO> planDemandCutterList = getPlanDemandCutterListByProjectPlanId(projectPlanId);
        respVO.setDemandCutterDOList(planDemandCutterList);
        List<PlanDemandHiltDO> planDemandHiltList = getPlanDemandHiltListByProjectPlanId(projectPlanId);
        respVO.setDemandHiltDOList(planDemandHiltList);
        List<PlanDemandMaterialDO> planDemandMaterialList = getPlanDemandMaterialListByProjectPlanId(projectPlanId);
        respVO.setDemandMaterialDOList(planDemandMaterialList);
        List<PlanPurchaseMaterialDO> planPurchaseMaterialList = getPlanPurchaseMaterialListByProjectPlanId(projectPlanId);
        respVO.setPurchaseMaterialDOList(planPurchaseMaterialList);
        return respVO;
    }

    /**
     * 获取项目计划,物料采购计划中资源列表(设备那些)
     * @param projectIds
     * @return
     */
    @Override
    public ResourceRespVO getResourceByProjectIds(List<String> projectIds) {
        ResourceRespVO respVO = new ResourceRespVO();
        if (ObjectUtil.isNull(projectIds)||projectIds.size()==0){
            return respVO;
        }
        //先通过项目id查找到项目计划id
        LambdaQueryWrapperX<PmsPlanDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PmsPlanDO::getProjectId,projectIds);
        List<PmsPlanDO> pmsPlanDOList = planMapper.selectList(wrapperX);
        List<String> planIds = pmsPlanDOList.stream().map(PmsPlanDO::getId).collect(Collectors.toList());

        if(planIds.size()==0){
            return respVO;
        }

        //存储的时候,设备和工装和采购的合并了,所以不用查询
//        List<PlanDeviceDO> planDeviceDOList = getPlanDeviceDOListByProjectPlanIds(planIds);
        List<PlanCombinationDO> planCombinationDOList = getPlanCombinationDOListByProjectPlanIds(planIds);
//        List<PlanMaterialDO> planMaterialDOList = getPlanMaterialDOListByProjectPlanIds(planIds);
        respVO.setCombinationDOList(planCombinationDOList);


        List<PlanDemandDeviceDO> planDemandDeviceDOList = getPlanDemandDeviceDOListByProjectPlanIds(planIds);
        List<PlanDemandCutterDO> planDemandCutterDOList = getPlanDemandCutterDOListByProjectPlanIds(planIds);
        List<PlanDemandHiltDO> planDemandHiltDOList = getPlanDemandHiltDOListByProjectPlanIds(planIds);
        List<PlanDemandMaterialDO> planDemandMaterialDOList = getPlanDemandMaterialDOListByProjectPlanIds(planIds);
        List<PlanPurchaseMaterialDO> planPurchaseMaterialList = getPlanPurchaseMaterialListByProjectPlanIds(planIds);
        respVO.setPurchaseMaterialDOList(planPurchaseMaterialList);
        respVO.setDemandDeviceDOList(planDemandDeviceDOList);
        respVO.setDemandCutterDOList(planDemandCutterDOList);
        respVO.setDemandHiltDOList(planDemandHiltDOList);
        respVO.setDemandMaterialDOList(planDemandMaterialDOList);
        return respVO;
    }

    @Override
    public ResourceRespVO getResourceAll() {
        //查询所有进行中的项目
        LambdaQueryWrapperX<PmsApprovalDO> projectWrapper = new LambdaQueryWrapperX<>();
        projectWrapper.in(PmsApprovalDO::getProjectStatus,Arrays.asList(3,4,5));
        projectWrapper.orderByDesc(PmsApprovalDO::getId);
        List<PmsApprovalDO> pmsApprovalDOS = approvalMapper.selectList(projectWrapper);
        //项目id
        List<String> projectIds = pmsApprovalDOS.stream().map(PmsApprovalDO::getId).collect(Collectors.toList());
        if(CollectionUtils.isAnyEmpty(projectIds)){
            return new ResourceRespVO();
        }
        ResourceRespVO resource = getResourceByProjectIds(projectIds);
        return resource;
    }

    /**
     * 通过项目id获得项目
     * @param id 编号
     * @return
     */
    @Override
    public PmsPlanDO getByProjectId(String id) {
        return planMapper.selectList(PmsPlanDO::getProjectId,id).get(0);
    }

    /**
     * 选择工艺方案
     * @param createReqVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void selectProcessScheme(PmsPlanSaveReqVO createReqVO) {
        //选择工艺的时候,主计划还没创建,要先创建
        if(ObjectUtil.isNull(createReqVO.getId())){
            PmsPlanSaveReqVO req = new PmsPlanSaveReqVO();
            PmsPlanSaveReqVO pmsPlanSaveReqVO = BeanUtils.toBean(createReqVO, PmsPlanSaveReqVO.class).setStatus(null);
            String planId = createPlan(pmsPlanSaveReqVO);
            createReqVO.setId(planId);
        }

        //1.工艺方案只能选一次,选过就不能再选了
        PmsPlanDO pmsPlanDO = planMapper.selectById(createReqVO.getId());
        if(ObjectUtil.isNotEmpty(pmsPlanDO.getProcessScheme())){
            return;
        }
        //查询订单,查看是否整单外协
        PmsOrderDO orderDO = orderMapper.selectById(pmsPlanDO.getProjectOrderId());
        Integer outsource = orderDO.getOutsource();
        planMapper.updateById(new PmsPlanDO().setId(pmsPlanDO.getId()).setProcessScheme(createReqVO.getProcessScheme()).setProcessVersionId(createReqVO.getProcessVersionId()));
    }

    @Override
    public List<PmsPlanDO> showProcessScheme() {
        //查询所有进行中的项目
        LambdaQueryWrapperX<PmsApprovalDO> projectWrapper = new LambdaQueryWrapperX<>();
        projectWrapper.in(PmsApprovalDO::getProjectStatus,Arrays.asList(3,4,5));
        projectWrapper.orderByDesc(PmsApprovalDO::getId);
        List<PmsApprovalDO> pmsApprovalDOS = approvalMapper.selectList(projectWrapper);

        List<String> projectIds = new ArrayList<>();
        List<String> codeList = new ArrayList<>();
        for (PmsApprovalDO pmsApprovalDO : pmsApprovalDOS) {
            projectIds.add(pmsApprovalDO.getId());
            codeList.add(pmsApprovalDO.getProjectCode());
        }
        if (codeList.size()==0){
            return Collections.emptyList();
        }
//        LambdaQueryWrapperX<PmsPlanDO> planWrapper = new LambdaQueryWrapperX<>();
//        planWrapper.in(PmsPlanDO::getProjectId,projectIds);
//        planWrapper.isNull(PmsPlanDO::getProcessScheme);

        //List<PmsPlanDO> pmsPlanDOS = planMapper.selectList(planWrapper);

        List<PmsPlanDO> pmsPlanDOS = planMapper.showProcessScheme(projectIds);

        List<PmsPlanDO> list = new ArrayList<>();
        for (PmsPlanDO pmsPlanDO : pmsPlanDOS) {
            List<ProjPartBomTreeRespDTO> schemeList = projectPlanApi.getProjPartBomTreeListNew(pmsPlanDO.getProjectCode(), pmsPlanDO.getPartNumber()).getCheckedData();
            if (schemeList.size()>0){
                list.add(pmsPlanDO);
                pmsPlanDO.setSchemeOrderBy(1);
            }else {
                pmsPlanDO.setSchemeOrderBy(0);
            }
        }
        List<PmsPlanDO> sortList = pmsPlanDOS.stream().sorted((a, b) -> {
            return b.getSchemeOrderBy() - a.getSchemeOrderBy();
        }).collect(Collectors.toList());
        //现在返回全部
        return sortList;
    }

    @Override
    public List<ProjPartBomTreeRespDTO> selectProcessSchemeList(List<String> projectCodes) {
        if (CollectionUtils.isAnyEmpty(projectCodes)){
            return Collections.emptyList();
        }
        List<ProjPartBomTreeRespDTO> processSchemeList = projectPlanApi.getProcessListByProjectCodes(projectCodes).getCheckedData();
        return processSchemeList;
    }

    //创建项目计划时,把评审需要采购的数据存到计划里,因为现在是一对多,一个项目多个主计划,现在根据图号分开创建采购计划
   public void createPlanPurchase(String projectId,String projectCode,String planId,String partNumber){
       //创建项目计划后,要把评审表里的设备,工装,刀具存起来，变成项目计划子表，给物料采购计划使用
       //TechnologyAssessmentRespDTO technologyAssessment = technologyAssessmentApi.getTechnologyAssement(projectCode).getCheckedData();
       //数据处理
       TechnologyAssessmentRespDTO technologyAssessment = handleTechnology(projectId, projectCode, planId, partNumber);


       //主计划和订单绑定,要根据图号区分创建采购计划
       //只需要demand(采购就行)
       List<DemandDeviceRespDTO> demandDeviceList = technologyAssessment.getDemandDeviceList();
       List<DemandCutterRespDTO> demandCutterList = technologyAssessment.getDemandCutterList();
       List<DemandHiltRespDTO> demandHiltList = technologyAssessment.getDemandHiltList();
       List<DemandMaterialRespDTO> demandMaterialList = technologyAssessment.getDemandMaterialList();
       //计划有变,其他的也需要
       List<DeviceRespDTO> deviceList = ObjectUtil.isNull(technologyAssessment.getDeviceList())? Collections.emptyList():technologyAssessment.getDeviceList();
       List<CombinationRespDTO> combinationList = ObjectUtil.isNull(technologyAssessment.getCombinationList())? Collections.emptyList():technologyAssessment.getCombinationList();
       List<MaterialRespDTO> materialList = ObjectUtil.isNull(technologyAssessment.getMaterialList())?Collections.emptyList():technologyAssessment.getMaterialList();


       //获取评审
       LambdaQueryWrapperX<AssessmentDO> wrapperX = new LambdaQueryWrapperX<>();
       wrapperX.eq(AssessmentDO::getProjectId,projectId);
       wrapperX.eq(AssessmentDO::getProjectCode,projectCode);
       AssessmentDO assessmentDO = assessmentMapper.selectOne(wrapperX);
       //获取采购数量
       LambdaQueryWrapperX<AssessmentDeviceDO> wrapperX2 = new LambdaQueryWrapperX<>();
       wrapperX2.eq(AssessmentDeviceDO::getAssessmentId,assessmentDO.getId());
       List<AssessmentDeviceDO> assessmentDeviceDOList = assessmentDeviceMapper.selectList(wrapperX2);
       //将采购数量存入map,key为图号+类型+资源id
       Map<String,AssessmentDeviceDO> map = new HashMap<>();
       for (AssessmentDeviceDO assessmentDeviceDO : assessmentDeviceDOList) {
           map.put(assessmentDeviceDO.getPartNumber()+assessmentDeviceDO.getResourcesType()+assessmentDeviceDO.getResourcesTypeId(),assessmentDeviceDO);
       }
       System.out.println(assessmentDeviceDOList);

       //处理采购设备(采购建议)
       List<PlanDemandDeviceDO> planDemandDeviceDOList = BeanUtils.toBean(demandDeviceList, PlanDemandDeviceDO.class,vo->{
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if(ObjectUtil.isNotNull(assessmentDeviceDO)){
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });
       //处理采购刀具(采购建议)
       List<PlanDemandCutterDO> planDemandCutterDOList = BeanUtils.toBean(demandCutterList, PlanDemandCutterDO.class,vo->{
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if(ObjectUtil.isNotNull(assessmentDeviceDO)){
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });
       //处理采购刀柄(采购建议)
       List<PlanDemandHiltDO> planDemandHiltDOList = BeanUtils.toBean(demandHiltList, PlanDemandHiltDO.class,vo->{
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if(ObjectUtil.isNotNull(assessmentDeviceDO)){
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });
       //处理采购工装(采购建议)
       List<PlanDemandMaterialDO> planDemandMaterialList = BeanUtils.toBean(demandMaterialList, PlanDemandMaterialDO.class,vo->{
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if(ObjectUtil.isNotNull(assessmentDeviceDO)){
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });
       //处理采购设备
       List<PlanDeviceDO> planDeviceDOList = BeanUtils.toBean(deviceList, PlanDeviceDO.class, vo -> {
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if (ObjectUtil.isNotNull(assessmentDeviceDO)) {
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });
       //处理采购刀具
       List<PlanCombinationDO> planCombinationDOList = BeanUtils.toBean(combinationList, PlanCombinationDO.class, vo -> {
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if (ObjectUtil.isNotNull(assessmentDeviceDO)) {
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });
       //处理采购工装
       List<PlanMaterialDO> planMaterialDOList = BeanUtils.toBean(materialList, PlanMaterialDO.class, vo -> {
           AssessmentDeviceDO assessmentDeviceDO = map.get(vo.getPartNumber() +vo.getResourcesType()+ vo.getResourcesTypeId());
           if (ObjectUtil.isNotNull(assessmentDeviceDO)) {
               vo.setPurchaseAmount(assessmentDeviceDO.getPurchaseAmount());
               vo.setPredictPrice(assessmentDeviceDO.getPredictPrice());
           }
           vo.setProjectPlanId(planId).setId(null).setProjectId(projectId);
       });

       //设备合并到采购设备,工装合并到采购工装,设备和工装表就没用了
       for (PlanDeviceDO planDeviceDO : planDeviceDOList) {
           PlanDemandDeviceDO planDemandDeviceDO = BeanUtils.toBean(planDeviceDO, PlanDemandDeviceDO.class, vo -> {
               vo.setDeviceCode(planDeviceDO.getCode()).setDeviceName(planDeviceDO.getName()).setDeviceSpecification(planDeviceDO.getSpecification());
           });
           planDemandDeviceDOList.add(planDemandDeviceDO);

       }
       for (PlanMaterialDO planMaterialDO : planMaterialDOList) {
           PlanDemandMaterialDO planDemandMaterialDO = BeanUtil.copyProperties(planMaterialDO, PlanDemandMaterialDO.class);
           planDemandMaterialList.add(planDemandMaterialDO);
       }
       //刀具也合并
       for (PlanCombinationDO planCombinationDO : planCombinationDOList) {
           PlanDemandCutterDO planDemandCutterDO = BeanUtil.copyProperties(planCombinationDO, PlanDemandCutterDO.class);
           planDemandCutterDOList.add(planDemandCutterDO);
       }




       //过滤采购数量大于0的然后存起来
       List<PlanDemandDeviceDO> planDemandDeviceDOListPurchase = planDemandDeviceDOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getPurchaseAmount())).filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
       planDemandDeviceMapper.insertBatch(planDemandDeviceDOListPurchase);
       List<PlanDemandCutterDO> planDemandCutterDOListPurchase = planDemandCutterDOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getPurchaseAmount())).filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
       planDemandCutterMapper.insertBatch(planDemandCutterDOListPurchase);
       List<PlanDemandHiltDO> planDemandHiltDOListPurchase = planDemandHiltDOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getPurchaseAmount())).filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
       planDemandHiltMapper.insertBatch(planDemandHiltDOListPurchase);
       List<PlanDemandMaterialDO> planDemandMaterialListPurchase = planDemandMaterialList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getPurchaseAmount())).filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
       planDemandMaterialMapper.insertBatch(planDemandMaterialListPurchase);

//       List<PlanDeviceDO> planDeviceDOListPurchase = planDeviceDOList.stream().filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
//       planDeviceMapper.insertBatch(planDeviceDOListPurchase);
//       List<PlanCombinationDO> planCombinationDOListPurchase = planCombinationDOList.stream().filter(item -> ObjectUtil.isNotEmpty(item.getPurchaseAmount())).filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
//       planCombinationMapper.insertBatch(planCombinationDOListPurchase);
//       List<PlanMaterialDO> planMaterialDOListPurchase = planMaterialDOList.stream().filter(item -> item.getPurchaseAmount() > 0).collect(Collectors.toList());
//       planMaterialMapper.insertBatch(planMaterialDOList);

//       System.out.println(123456);


   }

    /**
     * 获取技术评估中可能要采购的物料
     * @param projectId
     * @param projectCode
     * @param planId
     * @param partNumber
     * @return
     */
    public TechnologyAssessmentRespDTO handleTechnology(String projectId,String projectCode,String planId,String partNumber){
        //创建项目计划后,要把评审表里的设备,工装,刀具存起来，变成项目计划子表，给物料采购计划使用
        TechnologyAssessmentRespDTO technologyAssessment = technologyAssessmentApi.getTechnologyAssement(projectCode).getCheckedData();
        TechnologyAssessmentRespDTO technology = new TechnologyAssessmentRespDTO();

        List<DemandDeviceRespDTO> demandDeviceList = technologyAssessment.getDemandDeviceList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());
        List<DemandCutterRespDTO> demandCutterList = technologyAssessment.getDemandCutterList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());
        List<DemandHiltRespDTO> demandHiltList = technologyAssessment.getDemandHiltList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());
        List<DemandMaterialRespDTO> demandMaterialList = technologyAssessment.getDemandMaterialList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());
        //计划有变,其他的也需要
        List<DeviceRespDTO> deviceList = ObjectUtil.isNull(technologyAssessment.getDeviceList())? Collections.emptyList():technologyAssessment.getDeviceList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());
        List<CombinationRespDTO> combinationList = ObjectUtil.isNull(technologyAssessment.getCombinationList())? Collections.emptyList():technologyAssessment.getCombinationList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());
        List<MaterialRespDTO> materialList = ObjectUtil.isNull(technologyAssessment.getMaterialList())?Collections.emptyList():technologyAssessment.getMaterialList().stream().filter((item)->{return partNumber.equals(item.getPartNumber());}).collect(Collectors.toList());

        technology.setDemandDeviceList(demandDeviceList).setDemandCutterList(demandCutterList).setDemandHiltList(demandHiltList)
                .setDemandMaterialList(demandMaterialList).setDeviceList(deviceList).setCombinationList(combinationList).setMaterialList(materialList);
        return technology;
    }

    // ==================== 子表（项目计划子表，产品计划完善） ====================
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public String createPlanItem(@Valid PmsPlanItemReqVO req) throws InterruptedException {
//        List<String> materialCodeList = req.getMaterialCodeList();
//        //预设物料状态为加工中
//        int materialStatus = 2;
//        PmsOrderDO orderDO = orderMapper.selectById(req.getProjectOrderId());
//        orderDO.setOrderStatus(4);
//        orderMapper.updateById(orderDO);
//        PmsApprovalDO pmsApprovalDO = pmsApprovalMapper.selectById(orderDO.getProjectId());
//        pmsApprovalDO.setProjectStatus(4L);
//        pmsApprovalMapper.updateById(pmsApprovalDO);
//
//        //生码，代替订单编码
//        GeneratorCodeReqDTO gDto = new GeneratorCodeReqDTO();
//        gDto.setEncodingRuleType(1);
//        gDto.setClassificationCode(GENERATE_CODE);
//        String generateCode = encodingRuleApi.generatorCode(gDto).getCheckedData();
//        UpdateCodeReqDTO updateDto = new UpdateCodeReqDTO();
//        updateDto.setCode(generateCode);
//        String checkedData1 = encodingRuleApi.updateCodeStatus(updateDto).getCheckedData();
//        PlanItemDO planItemDO = BeanUtils.toBean(req, PlanItemDO.class).setId(null).setOrderNumber(generateCode);
//
//        //判断路线,决定处理逻辑
//        if(orderDO.getOutsource()==1){
//            //1整单外协
//            //1.1带料
//            if(orderDO.getProcessType()==1){
//                materialStatus = 6;
//                planItemDO.setPlanType(2);
//                if(materialCodeList.size()<=0){
//                    throw exception(MATERIAL_UNSELECTED);
//                }
//                planItemMapper.insert(planItemDO);
//                //更新订单物料关系
//                List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getId, materialCodeList);
//                for (OrderMaterialRelationDO relationDO : relationDOList) {
//                    relationDO.setPlanId(planItemDO.getProjectPlanId());
//                    relationDO.setPlanItemId(planItemDO.getId());
//                    relationDO.setPlanType(planItemDO.getPlanType());
//                    relationDO.setMaterialStatus(materialStatus);
//                    relationDO.setOrderNumber(generateCode);
//                    relationDO.setProductCode(orderDO.getPartNumber());
//                }
//                orderMaterialRelationMapper.updateBatch(relationDOList);
//                //生成采购申请
//                createPurchase(req,planItemDO);
//
//            }else {
//                //1.2不带料
//                //状态直接就是外协中,因为没码，没人会通知我选了什么
//                materialStatus = 4;
//                planItemDO.setPlanType(3);
//                planItemMapper.insert(planItemDO);
//                //更新订单物料关系,这个没码,所以去找planItemId为空的更新，这种都是没分配任务的
//                List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, orderDO.getId());
//                List<OrderMaterialRelationDO> relationDOS = relationDOList.stream().filter((item) -> {
//                    return ObjectUtil.isNull(item.getPlanItemId());
//                }).limit(req.getPurchaseAmount()).collect(Collectors.toList());
//
//                if(relationDOS.size()==req.getPurchaseAmount()){
//                    for (OrderMaterialRelationDO relationDO : relationDOS) {
//                        relationDO.setPlanId(planItemDO.getProjectPlanId());
//                        relationDO.setPlanItemId(planItemDO.getId());
//                        relationDO.setPlanType(planItemDO.getPlanType());
//                        relationDO.setMaterialStatus(materialStatus);
//                        relationDO.setOrderNumber(generateCode);
//                        relationDO.setProductCode(orderDO.getPartNumber());
//                    }
//                    orderMaterialRelationMapper.updateBatch(relationDOList);
//                }else {
//                    //采购数量和剩余的关系不匹配
//                    throw exception(PURCHASE_AMOUNT_INCORRECT);
//                }
//                //生成采购申请
//                createPurchase(req,planItemDO);
//            }
//        }else {
//            //2正常加工(工序外协也是正常加工)
//            if(materialCodeList.size()<=0){
//                throw exception(MATERIAL_UNSELECTED);
//            }
//            planItemDO.setPlanType(1);
//            planItemMapper.insert(planItemDO);
//            //更新订单物料关系
//            List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getId, materialCodeList);
//
//            for (OrderMaterialRelationDO relationDO : relationDOList) {
//                relationDO.setPlanId(planItemDO.getProjectPlanId());
//                relationDO.setPlanItemId(planItemDO.getId());
//                relationDO.setPlanType(planItemDO.getPlanType());
//                relationDO.setMaterialStatus(materialStatus);
//                relationDO.setOrderNumber(generateCode);
//
//            }
//            orderMaterialRelationMapper.updateBatch(relationDOList);
//            //请求生产任务,mcs接口,只有正常加工用这个
//            McsOrderFormCreateDTO dto = new McsOrderFormCreateDTO();
//            dto.setIsBatch(false).setOrderType(1).setPriority(1).setDeliveryTime(orderDO.getFproDeliveryTime()).setReceptionTime(orderDO.getCreateTime());
//            dto.setPartNumber(req.getPartNumber()).setBeginProcessNumber(req.getProcessStep()).setProjectNumber(req.getProjectCode()).setTechnologyId(req.getProcessScheme()).setCount(req.getQuantity().intValue());
//            dto.setResponsiblePerson(req.getResponsiblePerson().toString()).setMaterialCode(req.getMaterialCodeListStr()).setOrderNumber(generateCode).setProcesStatus(planItemDO.getPlanType());
//            Object checkedData = mcsManufacturingControlApi.orderFormCreate(dto).getCheckedData();
//        }
//        return planItemDO.getId();
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPlanItem(@Valid PmsPlanItemReqVO req) throws InterruptedException {
        //首件订单,每个批次都有首件订单,首件通过才能继续下单,通过计划查询子计划
        //处理订单和项目状态
        PmsOrderDO orderDO = orderMapper.selectById(req.getProjectOrderId());
        orderDO.setOrderStatus(4);
        orderMapper.updateById(orderDO);
        PmsApprovalDO pmsApprovalDO = pmsApprovalMapper.selectById(orderDO.getProjectId());
        pmsApprovalDO.setProjectStatus(4L);
        pmsApprovalMapper.updateById(pmsApprovalDO);
        //查询可用物码
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        if(orderDO.getOutsource()==1&&orderDO.getProcessType()==2){
            wrapperX.eq(OrderMaterialRelationDO::getOrderId,orderDO.getId())
                    .isNull(OrderMaterialRelationDO::getPlanItemId);
        }else {
            wrapperX.eq(OrderMaterialRelationDO::getMaterialStatus,1)
                    .eq(OrderMaterialRelationDO::getOrderId,orderDO.getId())
                    .isNull(OrderMaterialRelationDO::getPlanItemId);
        }

        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
        //查不到说明都分配了
        if(relationDOList.size()==0){
            return "false";
        }
        //要查询条码是否在退货
        relationDOList = clearRelation(relationDOList);
        if(orderDO.getOutsource()==1){
            //处理整单外协
            handleCreatePlanItemOutsource(orderDO,req,relationDOList);
        }else {
            //首件订单,每个批次都有首件订单,首件通过才能继续下单,通过计划查询子计划
            //在这里处理首件,通过计划id查询首件,查不到，创建首件，查到了,加工完成正常下单,加工未完成,不许下单
            LambdaQueryWrapper<PlanItemDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(PlanItemDO::getProjectPlanId,req.getProjectPlanId());
            wrapper.eq(PlanItemDO::getFirstMark,1);
            List<PlanItemDO> planItemDOS = planItemMapper.selectList(wrapper);
            if(planItemDOS.size()>0){
                //大于0,说明有首件
                PlanItemDO planItemDO = planItemDOS.get(0);
                LambdaQueryWrapper<OrderMaterialRelationDO> reWrapper = new LambdaQueryWrapper<>();
                reWrapper.eq(OrderMaterialRelationDO::getPlanItemId, planItemDO.getId());
                reWrapper.eq(OrderMaterialRelationDO::getMaterialStatus,5);
                List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(reWrapper);
                if(relationDOS.size()>0){
                    handleCreatePlanItemProcess(orderDO,req,relationDOList,0);

//                    //首件完成，判断质检是否合格,没完成什么都不用做
//                    String materialCode = relationDOS.get(0).getMaterialCode();
//                    List<MaterialStockRespDTO> materialStockList= materialStockApi.getMaterialsByBarCode(materialCode).getCheckedData();
//                    if(materialStockList.size()>0){
//                        MaterialStockRespDTO materialStock = materialStockList.get(0);
//                        if(materialStock.getMaterialStatus()==2){
//                            //生产完成且质检合格，可以继续下单
//                            //处理加工
//                            handleCreatePlanItemProcess(orderDO,req,relationDOList,0);
//                        }
//                    }
                }
            }else {
                //没有首件,只传一个并且标记为1
                //处理加工
                handleCreatePlanItemProcess(orderDO,req,Arrays.asList(relationDOList.get(0)),1);
            }
        }

        return "ok";
    }

    @Transactional
    @Override
    public String createPlanItemByIds(List<String> ids) throws Exception {
        if(CollectionUtils.isAnyEmpty(ids)){
            return "false";
        }
        //查询计划
        List<PmsPlanDO> pmsPlanDOS = planMapper.selectBatchIds(ids);
        //查询子计划
        LambdaQueryWrapperX<PlanItemDO> itemWrapper = new LambdaQueryWrapperX<>();
        itemWrapper.in(PlanItemDO::getProjectPlanId,ids);
        List<PlanItemDO> planItemDOS = planItemMapper.selectList(itemWrapper);
        //封装子计划
        Map<String,List<PlanItemDO>> itemMap = new HashMap<>();
        //记录首单
        List<PlanItemDO> firstItemList = new ArrayList<>();
        for (PlanItemDO planItemDO : planItemDOS) {
            String projectPlanId = planItemDO.getProjectPlanId();
            if(itemMap.containsKey(projectPlanId)){
                itemMap.get(projectPlanId).add(planItemDO);
            }else {
                List<PlanItemDO> list = new ArrayList<>();
                list.add(planItemDO);
                itemMap.put(projectPlanId,list);
            }
            if(planItemDO.getFirstMark()!=null&&planItemDO.getFirstMark()==1){
                firstItemList.add(planItemDO);
            }
        }
        //查询首单完成情况
        Map<String,String> completeMap = new HashMap<>();
        if(firstItemList.size()>0){
            List<String> itemList = firstItemList.stream().map(PlanItemDO::getId).collect(Collectors.toList());
            LambdaQueryWrapperX<OrderMaterialRelationDO> relationWrapperX = new LambdaQueryWrapperX<>();
            relationWrapperX.in(OrderMaterialRelationDO::getPlanItemId,itemList);
            relationWrapperX.eq(OrderMaterialRelationDO::getMaterialStatus,5);
            //查询完成的
            List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(relationWrapperX);
            for (OrderMaterialRelationDO relationDO : relationDOS) {
                completeMap.put(relationDO.getPlanId(),"ok");
            }
        }



        //查询项目
        List<String> projectIds = pmsPlanDOS.stream().map(PmsPlanDO::getProjectId).distinct().collect(Collectors.toList());
        LambdaQueryWrapperX<PmsApprovalDO> proWrapper = new LambdaQueryWrapperX<>();
        proWrapper.in(PmsApprovalDO::getId,projectIds);
        List<PmsApprovalDO> pmsApprovalDOS = pmsApprovalMapper.selectList(proWrapper);
        Map<String, Long> personMap = CollectionUtils.convertMap(pmsApprovalDOS, PmsApprovalDO::getId, PmsApprovalDO::getResponsiblePerson);

        //下单
        for (PmsPlanDO pmsPlanDO : pmsPlanDOS) {

            if(itemMap.containsKey(pmsPlanDO.getId())){
                //包含,说明下单过
                List<PlanItemDO> planItemDOS1 = itemMap.get(pmsPlanDO.getId());
                //只有一单，说明是首单,检查首单是否完成,完成才能继续下单
                if(planItemDOS1.size()==1&&completeMap.containsKey(pmsPlanDO.getId())){
                    //生码，订单编码
                    String generateCode = getgenerateCode();
                    Long person = personMap.get(pmsPlanDO.getProjectId());
                    //不包含子单那就是首单
                    PlanItemDO planItemDO = BeanUtils.toBean(pmsPlanDO, PlanItemDO.class);
                    planItemDO.setId(null).setFirstMark(0).setPlanType(1).setProjectPlanId(pmsPlanDO.getId())
                            .setOrderNumber(generateCode).setResponsiblePerson(person).setCount(pmsPlanDO.getQuantity()-1);
                    //创建订单
                    PmsOrderDO orderDO = orderMapper.selectById(pmsPlanDO.getProjectOrderId());
                    createProductOrder(planItemDO,orderDO,pmsPlanDO);
                }

            }else {
                //生码，订单编码
                String generateCode = getgenerateCode();
                Long person = personMap.get(pmsPlanDO.getProjectId());
                //不包含子单那就是首单
                PlanItemDO planItemDO = BeanUtils.toBean(pmsPlanDO, PlanItemDO.class);
                planItemDO.setId(null).setFirstMark(1).setPlanType(1).setProjectPlanId(pmsPlanDO.getId())
                        .setOrderNumber(generateCode).setResponsiblePerson(person).setCount(1);
                System.out.println(planItemDO);
                //首单更改项目状态,订单状态
                PmsOrderDO orderDO = orderMapper.selectById(pmsPlanDO.getProjectOrderId());
                orderDO.setOrderStatus(4);
                orderMapper.updateById(orderDO);
                pmsApprovalMapper.updateById(new PmsApprovalDO().setId(pmsPlanDO.getProjectId()).setProjectStatus(4L));
                //创建订单
                createProductOrder(planItemDO,orderDO,pmsPlanDO);
            }
        }

        return "ok";
    }

    @Override
    public String createPlanItemWithAmount(String id, Integer amount) throws Exception {
        //查询计划
        PmsPlanDO pmsPlanDO = planMapper.selectById(id);
        //查询子计划
        LambdaQueryWrapperX<PlanItemDO> itemWrapper = new LambdaQueryWrapperX<>();
        itemWrapper.eq(PlanItemDO::getProjectPlanId,id);
        List<PlanItemDO> planItemDOS = planItemMapper.selectList(itemWrapper);
        //封装子计划
        Map<String,List<PlanItemDO>> itemMap = new HashMap<>();
        //记录首单
        List<PlanItemDO> firstItemList = new ArrayList<>();
        for (PlanItemDO planItemDO : planItemDOS) {
            String projectPlanId = planItemDO.getProjectPlanId();
            if(itemMap.containsKey(projectPlanId)){
                itemMap.get(projectPlanId).add(planItemDO);
            }else {
                List<PlanItemDO> list = new ArrayList<>();
                list.add(planItemDO);
                itemMap.put(projectPlanId,list);
            }
            if(planItemDO.getFirstMark()==1){
                firstItemList.add(planItemDO);
            }
        }
        //查询首单完成情况
        Map<String,String> completeMap = new HashMap<>();
        if(firstItemList.size()>0){
            List<String> itemList = firstItemList.stream().map(PlanItemDO::getId).collect(Collectors.toList());
            LambdaQueryWrapperX<OrderMaterialRelationDO> relationWrapperX = new LambdaQueryWrapperX<>();
            relationWrapperX.in(OrderMaterialRelationDO::getPlanItemId,itemList);
            relationWrapperX.eq(OrderMaterialRelationDO::getMaterialStatus,5);
            //查询完成的
            List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(relationWrapperX);
            for (OrderMaterialRelationDO relationDO : relationDOS) {
                completeMap.put(relationDO.getPlanId(),"ok");
            }
        }



        //查询项目
        LambdaQueryWrapperX<PmsApprovalDO> proWrapper = new LambdaQueryWrapperX<>();
        proWrapper.eq(PmsApprovalDO::getId,pmsPlanDO.getId());
        List<PmsApprovalDO> pmsApprovalDOS = pmsApprovalMapper.selectList(proWrapper);
        Map<String, Long> personMap = CollectionUtils.convertMap(pmsApprovalDOS, PmsApprovalDO::getId, PmsApprovalDO::getResponsiblePerson);

        //下单
        if(itemMap.containsKey(pmsPlanDO.getId())){
            //包含,说明下单过
            List<PlanItemDO> planItemDOS1 = itemMap.get(pmsPlanDO.getId());
            //只有一单，说明是首单,检查首单是否完成,完成才能继续下单
            if(planItemDOS1.size()==1&&completeMap.containsKey(pmsPlanDO.getId())){
                //生码，订单编码
                String generateCode = getgenerateCode();
                Long person = personMap.get(pmsPlanDO.getProjectId());
                //不包含子单那就是首单
                PlanItemDO planItemDO = BeanUtils.toBean(pmsPlanDO, PlanItemDO.class);
                planItemDO.setId(null).setFirstMark(0).setPlanType(1).setProjectPlanId(pmsPlanDO.getId())
                        .setOrderNumber(generateCode).setResponsiblePerson(person).setCount(pmsPlanDO.getQuantity()-1);
                //创建订单
                PmsOrderDO orderDO = orderMapper.selectById(pmsPlanDO.getProjectOrderId());
                createProductOrder(planItemDO,orderDO,pmsPlanDO);
            }

        }else {
            //生码，订单编码
            String generateCode = getgenerateCode();
            Long person = personMap.get(pmsPlanDO.getProjectId());
            //不包含子单那就是首单
            PlanItemDO planItemDO = BeanUtils.toBean(pmsPlanDO, PlanItemDO.class);
            planItemDO.setId(null).setFirstMark(1).setPlanType(1).setProjectPlanId(pmsPlanDO.getId())
                    .setOrderNumber(generateCode).setResponsiblePerson(person).setCount(1);
            System.out.println(planItemDO);
            //首单更改项目状态,订单状态
            PmsOrderDO orderDO = orderMapper.selectById(pmsPlanDO.getProjectOrderId());
            orderDO.setOrderStatus(4);
            orderMapper.updateById(orderDO);
            pmsApprovalMapper.updateById(new PmsApprovalDO().setId(pmsPlanDO.getProjectId()).setProjectStatus(4L));
            //创建订单
            createProductOrder(planItemDO,orderDO,pmsPlanDO);
        }
        return "ok";
    }

    public String getgenerateCode () throws Exception {
        GeneratorCodeReqDTO gDto = new GeneratorCodeReqDTO();
        gDto.setEncodingRuleType(1);
        gDto.setClassificationCode(GENERATE_CODE);
        String generateCode = encodingRuleApi.generatorCode(gDto).getCheckedData();
        return generateCode;
    }

    /**
     * 创建生产订单，和原createPlanItem一样
     */
    public void createProductOrder(PlanItemDO planItemDO,PmsOrderDO orderDO,PmsPlanDO pmsPlanDO){
        planItemMapper.insert(planItemDO);
        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(pmsPlanDO.getProcessScheme()).getCheckedData();
        ProcedureRespDTO procedureRespDTO = procedureList.get(0);
        String step = procedureRespDTO.getProcedureNum();

        //请求生产任务,mcs接口,只有正常加工用这个
        McsOrderFormCreateDTO dto = new McsOrderFormCreateDTO();
        dto.setIsBatch(false).setOrderType(1).setPriority(1).setDeliveryTime(orderDO.getFproDeliveryTime()).setReceptionTime(orderDO.getCreateTime());
        dto.setPartNumber(orderDO.getPartNumber()).setBeginProcessNumber(step).setProjectNumber(orderDO.getProjectCode()).setTechnologyId(pmsPlanDO.getProcessScheme()).setCount(planItemDO.getCount());
        dto.setResponsiblePerson(planItemDO.getResponsiblePerson().toString()).setOrderNumber(planItemDO.getOrderNumber()).setProcesStatus(planItemDO.getPlanType());
        dto.setProjectPlanId(planItemDO.getProjectPlanId());
        if(planItemDO.getFirstMark()==1){
            dto.setFirst(true);
        }else {
            dto.setFirst(false);
        }

        Object checkedData = mcsManufacturingControlApi.orderFormCreate(dto).getCheckedData();

    }

    /**
     * 排除正在退货的关系
     * @param relationDOList
     * @return
     */
    public List<OrderMaterialRelationDO> clearRelation(List<OrderMaterialRelationDO> relationDOList){
        List<String> barCodeList = relationDOList.stream().filter(item -> ObjectUtil.isNotNull(item.getMaterialCode())).map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.toList());
        if(barCodeList.size()>0){
            List<ReturnMaterialDTO> ReturnMaterialList = consignmentReturnApi.getReturnByCodes(barCodeList).getCheckedData();
            if(ReturnMaterialList.size()>0){
                List<OrderMaterialRelationDO> newList = new ArrayList<>();
                for (OrderMaterialRelationDO relationDO : relationDOList) {
                    if(ReturnMaterialList.contains(relationDO.getMaterialCode())){
                        //如果包含，说明被排除了，对这个码进行清空(没有必要清空，选不到就可以了，之后回来了继续用)
//                        LambdaUpdateWrapper<OrderMaterialRelationDO> updateWrapper = new LambdaUpdateWrapper<>();
//                        updateWrapper.set(OrderMaterialRelationDO::getMaterialCode,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getVariableCode,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getProductCode,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getPlanItemId,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getPlanId,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getMaterialStatus,3);
//                        updateWrapper.set(OrderMaterialRelationDO::getPlanType,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getStep,null);
//                        updateWrapper.set(OrderMaterialRelationDO::getOrderNumber,null);
//                        updateWrapper.eq(OrderMaterialRelationDO::getMaterialCode,relationDO.getMaterialCode());
                    }else {
                        newList.add(relationDO);
                    }
                }
                relationDOList = newList;
            }
        }

        return relationDOList;
    }

    /**
     * 生产加工处理,根据工序分类，有可能有多个订单
     * @param orderDO
     * @param req
     * @throws InterruptedException
     */
    public void handleCreatePlanItemProcess(PmsOrderDO orderDO,PmsPlanItemReqVO req,List<OrderMaterialRelationDO> relationDOList,Integer firstMark) throws InterruptedException {
        //根据工序不同，分类,多种工序就得下多个单(只有料被切开,追加加工才会这样,大多数情况都是一种工序)
        Map<String,List<OrderMaterialRelationDO>> map = new HashMap<>();
        for (OrderMaterialRelationDO relationDO : relationDOList) {
            if(map.containsKey(relationDO.getStep())){
                map.get(relationDO.getStep()).add(relationDO);
            }else {
                List<OrderMaterialRelationDO> list = new ArrayList<>();
                list.add(relationDO);
                map.put(relationDO.getStep(),list);
            }
        }
        for (Map.Entry<String, List<OrderMaterialRelationDO>> entry : map.entrySet()) {
            List<OrderMaterialRelationDO> relationValues = entry.getValue();
            //生码，代替订单编码
            GeneratorCodeReqDTO gDto = new GeneratorCodeReqDTO();
            gDto.setEncodingRuleType(1);
            gDto.setClassificationCode(GENERATE_CODE);
            String generateCode = encodingRuleApi.generatorCode(gDto).getCheckedData();
            UpdateCodeReqDTO updateDto = new UpdateCodeReqDTO();
            updateDto.setCode(generateCode);
            PlanItemDO planItemDO = BeanUtils.toBean(req, PlanItemDO.class).setId(null).setOrderNumber(generateCode);
            int materialStatus = 2;
            planItemDO.setPlanType(1);
            planItemDO.setFirstMark(firstMark);
            planItemMapper.insert(planItemDO);
            for (OrderMaterialRelationDO relationDO : relationValues) {
                relationDO.setPlanId(planItemDO.getProjectPlanId());
                relationDO.setPlanItemId(planItemDO.getId());
                relationDO.setPlanType(planItemDO.getPlanType());
                relationDO.setMaterialStatus(materialStatus);
                relationDO.setOrderNumber(generateCode);
                relationDO.setProductCode(orderDO.getPartNumber());
            }
            orderMaterialRelationMapper.updateBatch(relationDOList);
            String materialCodeListStr = relationValues.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.joining(","));
            //请求生产任务,mcs接口,只有正常加工用这个
            McsOrderFormCreateDTO dto = new McsOrderFormCreateDTO();
            dto.setIsBatch(false).setOrderType(1).setPriority(1).setDeliveryTime(orderDO.getFproDeliveryTime()).setReceptionTime(orderDO.getCreateTime());
            dto.setPartNumber(req.getPartNumber()).setBeginProcessNumber(entry.getKey()).setProjectNumber(req.getProjectCode()).setTechnologyId(req.getProcessScheme()).setCount(relationValues.size());
            dto.setResponsiblePerson(req.getResponsiblePerson().toString()).setMaterialCode(materialCodeListStr).setOrderNumber(generateCode).setProcesStatus(planItemDO.getPlanType());
            dto.setProjectPlanId(req.getProjectPlanId());
            if(firstMark==1){
                dto.setFirst(true);
            }else {
                dto.setFirst(false);
            }

            Object checkedData = mcsManufacturingControlApi.orderFormCreate(dto).getCheckedData();
        }
    }

    /**
     * 整单外协处理(分带料和不带料两种)
     * 整单外协肯定是一个订单
     * @param orderDO
     * @param req
     * @throws InterruptedException
     */
    public void handleCreatePlanItemOutsource(PmsOrderDO orderDO,PmsPlanItemReqVO req,List<OrderMaterialRelationDO> relationDOList) throws InterruptedException {
        //生码，代替订单编码
        GeneratorCodeReqDTO gDto = new GeneratorCodeReqDTO();
        gDto.setEncodingRuleType(1);
        gDto.setClassificationCode(GENERATE_CODE);
        String generateCode = encodingRuleApi.generatorCode(gDto).getCheckedData();
        UpdateCodeReqDTO updateDto = new UpdateCodeReqDTO();
        updateDto.setCode(generateCode);
        PlanItemDO planItemDO = BeanUtils.toBean(req, PlanItemDO.class).setId(null).setOrderNumber(generateCode);

        if(orderDO.getProcessType()==1){
            //带料的情况
            int materialStatus = 4;
            planItemDO.setPlanType(2);
            planItemMapper.insert(planItemDO);
            for (OrderMaterialRelationDO relationDO : relationDOList) {
                relationDO.setPlanId(planItemDO.getProjectPlanId());
                relationDO.setPlanItemId(planItemDO.getId());
                relationDO.setPlanType(planItemDO.getPlanType());
                relationDO.setMaterialStatus(materialStatus);
                relationDO.setOrderNumber(generateCode);
                relationDO.setProductCode(orderDO.getPartNumber());
            }
        }else {
            //不带料的情况
            int materialStatus = 4;
            planItemDO.setPlanType(3);
            planItemMapper.insert(planItemDO);
            for (OrderMaterialRelationDO relationDO : relationDOList) {
                relationDO.setPlanId(planItemDO.getProjectPlanId());
                relationDO.setPlanItemId(planItemDO.getId());
                relationDO.setPlanType(planItemDO.getPlanType());
                relationDO.setMaterialStatus(materialStatus);
                relationDO.setOrderNumber(generateCode);
                relationDO.setProductCode(orderDO.getPartNumber());
            }
        }
        orderMaterialRelationMapper.updateBatch(relationDOList);
        req.setPurchaseAmount(relationDOList.size());
        //生成采购申请
        createPurchase(req,planItemDO);
    }


    //创建物料采购计划(这个是外协采购成品的,采购毛坯不能用这个)
    public void createPurchase(PmsPlanItemReqVO req,PlanItemDO planItemDO){
        PmsPlanDO pmsPlanDO = planMapper.selectById(req.getProjectPlanId());
        PlanPurchaseMaterialDO planPurchaseMaterialDO = BeanUtils.toBean(req, PlanPurchaseMaterialDO.class)
                .setProjectPlanItemId(planItemDO.getId()).setProjectPlanId(planItemDO.getProjectPlanId())
                .setId(null).setProjectCode(planItemDO.getProjectCode()).setPartNumber(planItemDO.getPartNumber()).setPlanType(planItemDO.getPlanType())
                .setProjectId(planItemDO.getProjectId());
        planPurchaseMaterialDO.setPurchaseAmount(planPurchaseMaterialDO.getQuantity());
        planPurchaseMaterialDO.setPurchaseTime(pmsPlanDO.getPurchaseCompletionTime());
        //用物料牌号,查询物料类型,外协采购是成品,得用partNumber
        MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
        dto.setMaterialNumber(req.getPartNumber());
        List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
        if(materialConfigList.size()>0){
            MaterialConfigRespDTO materialConfigRespDTO = materialConfigList.get(0);
            planPurchaseMaterialDO.setMaterialId(materialConfigRespDTO.getId());
            planPurchaseMaterialDO.setMaterialName(materialConfigRespDTO.getMaterialName());
            planPurchaseMaterialDO.setMaterialSpecification(materialConfigRespDTO.getMaterialSpecification());
        }
        planPurchaseMaterialMapper.insert(planPurchaseMaterialDO);
    }

    /**
     * 物料采购计划,毛坯的(外协的虽然数据差不多，但是是和子计划同步创建,采购毛坯的时候还没有子计划)
     */
    @Override
    public void createMaterialPurchasePlan(MaterialPurchsePlanReqVO req){
        //用物料牌号,查询物料类型
        MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
        dto.setMaterialNumber(req.getMaterialNumber());
        List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
        MaterialConfigRespDTO materialConfigRespDTO = materialConfigList.get(0);

        //采购合并
        LambdaQueryWrapperX<PlanPurchaseMaterialDO> wrapperX = new LambdaQueryWrapperX<>();
        //未采购,物料类型一致,planType为1
        wrapperX.eq(PlanPurchaseMaterialDO::getPurchaseMark,0)
                //.eq(PlanPurchaseMaterialDO::getProjectOrderId,req.getProjectOrderId())
                .eq(PlanPurchaseMaterialDO::getProjectPlanId,req.getProjectPlanId())
                .eq(PlanPurchaseMaterialDO::getMaterialId,materialConfigRespDTO.getId())
                .eq(PlanPurchaseMaterialDO::getPlanType,1);
        List<PlanPurchaseMaterialDO> planPurchaseMaterialDOS = planPurchaseMaterialMapper.selectList(wrapperX);
        if(planPurchaseMaterialDOS.size()>0){
            //加上要采购的数量就好
            PlanPurchaseMaterialDO planPurchaseMaterialDO = planPurchaseMaterialDOS.get(0);
            planPurchaseMaterialDO.setPurchaseAmount(planPurchaseMaterialDO.getPurchaseAmount()+req.getPurchaseAmount());
            planPurchaseMaterialMapper.updateById(planPurchaseMaterialDO);
            //如果合并了就不用往下走了
            return;
        }
        PmsPlanDO pmsPlanDO = planMapper.selectById(req.getProjectPlanId());

        PlanPurchaseMaterialDO planPurchaseMaterialDO = BeanUtils.toBean(req, PlanPurchaseMaterialDO.class).setPlanType(1);
        planPurchaseMaterialDO.setPurchaseTime(pmsPlanDO.getPurchaseCompletionTime());
        if(materialConfigList.size()>0){
            planPurchaseMaterialDO.setMaterialId(materialConfigRespDTO.getId());
            planPurchaseMaterialDO.setMaterialName(materialConfigRespDTO.getMaterialName());
            planPurchaseMaterialDO.setMaterialSpecification(materialConfigRespDTO.getMaterialSpecification());
        }
        planPurchaseMaterialMapper.insert(planPurchaseMaterialDO);
    }

    /**
     * 除了计划类型,好像和上面的一样
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void creatStepOutsourcePurchsePlan(StepOutsourcePurchseReqVO req){
        if(CollectionUtils.isAnyEmpty(req.getMaterialCodeList())){
            return;
        }
        List<MaterialStockRespDTO> stockList = materialStockApi.getMaterialsByBarCodes(req.getMaterialCodeList()).getCheckedData();
        Map<String, MaterialStockRespDTO> materialStockMap = CollectionUtils.convertMap(stockList, MaterialStockRespDTO::getBarCode);

        PlanPurchaseMaterialDO planPurchaseMaterialDO = BeanUtils.toBean(req, PlanPurchaseMaterialDO.class).setPlanType(2);
        //用物料牌号,查询物料类型
        MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
        dto.setMaterialNumber(planPurchaseMaterialDO.getMaterialNumber());
        List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
        //更新订单物料关系
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getId, req.getMaterialCodeList());
        //排除一下正要退货的
        relationDOList = clearRelation(relationDOList);

        for (OrderMaterialRelationDO relationDO : relationDOList) {
            //这部分是新加的原料id,外协发货的时候原料也得知道
            if(materialStockMap.containsKey(relationDO.getMaterialCode())){
                MaterialStockRespDTO dto1 = materialStockMap.get(relationDO.getMaterialCode());
                relationDO.setMaterialTypeId(dto1.getMaterialConfigId());
            }
            //现在是待外协(直接外协中)
            //relationDO.setMaterialStatus(6);
            relationDO.setMaterialStatus(4);
        }
        //查工序
        PmsPlanDO pmsPlanDO = planMapper.selectById(relationDOList.get(0).getPlanId());
        String processVersionId = pmsPlanDO.getProcessVersionId();
        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(processVersionId).getCheckedData();

        if(materialConfigList.size()>0){
            //码已经生了,直接去查
            String step = relationDOList.get(0).getStep();

            //获得委派时的工序,需要的是外协结束时的工序
//            ProcedureRespDTO procedure = new ProcedureRespDTO();
//            for (ProcedureRespDTO procedureRespDTO : procedureList) {
//                if(procedureRespDTO.getProcedureNum().equals(step)){
//                    procedure = procedureRespDTO;
//                    break;
//                }
//            }
//            //获得委派时的索引
//            int index = procedureList.indexOf(procedure);
            int index = 0;
            //判断要买的物料是不是成品
            boolean isFinish = false;
            for (int i = 0; i < procedureList.size()-1; i++) {
                //如果委派工序和当前遍历工序相等,判断下一道工序是不是外协
                if(procedureList.get(i).getProcedureNum().equals(step)){
                    if(procedureList.get(i+1).getIsOut().equals("1")){
                        //是外协
                        index = i+1;
                        step = procedureList.get(i+1).getProcedureNum();
                        //是外协并且是最后一步
                        if(index == (procedureList.size()-1)){
                            //最后一步都是外协，说明买的成品
                            isFinish = true;
                        }
                    }else {
                        //不是外协
                        index = i;
                        //不是外协并且是最后一步
                        if(index == (procedureList.size()-1)){
                            String procedureName = procedureList.get(index).getProcedureName();
                            if("入库".equals(procedureName)){
                                //最后一步是入库，根本不会加工，买的还是成品
                                isFinish = true;
                            }
                        }
                        break;
                    }
                }
            }


            //查找物料类型，一定有一个
            MaterialConfigReqDTO materialConfigReqDTO = new MaterialConfigReqDTO();
            if(isFinish){
                //买的成品
                materialConfigReqDTO.setMaterialNumber(req.getPartNumber());
            }else {
                //说明已经加工过了，得去查找,要买外协结束的图号+工序
                materialConfigReqDTO.setMaterialNumber(req.getPartNumber()+"_"+procedureList.get(index).getProcedureNum());
            }
//            //说明已经加工过了，得去查找,要买外协结束的图号+工序
//            materialConfigReqDTO.setMaterialNumber(req.getPartNumber()+"_"+procedureList.get(index).getProcedureNum());

//            if(index>0){
//                materialConfigReqDTO.setMaterialNumber(req.getPartNumber()+"_"+procedureList.get(index));
//            }else {
//                //等于0说明还没加工,用毛坯类型就可以
//                materialConfigReqDTO.setMaterialNumber(req.getMaterialNumber());
//            }
            List<MaterialConfigRespDTO> materialConfigRespDTOList = materialMCCApi.getMaterialConfigListByTypeCode(materialConfigReqDTO).getCheckedData();
            MaterialConfigRespDTO materialConfigRespDTO = new MaterialConfigRespDTO();
            if(materialConfigRespDTOList.size()>0){
                materialConfigRespDTO = materialConfigRespDTOList.get(0);
            }else {
                //else里的一定是半成品,不用担心生成新类型问题
                //根本查不到这个类型，要新生成一个
                //materialConfigReqDTO.setMaterialNumber(req.getPartNumber());
                materialConfigReqDTO.setMaterialNumber(req.getPartNumber());
                List<MaterialConfigRespDTO> partNumberList = materialMCCApi.getMaterialConfigListByTypeCode(materialConfigReqDTO).getCheckedData();
                MaterialConfigRespDTO materialConfigRespDTO1 = partNumberList.get(0);
                // 保存物料
                MaterialConfigReqDTO materialType = BeanUtils.toBean(materialConfigRespDTO1, MaterialConfigReqDTO.class);
                //类型是半成品
                MaterialTypeRespDTO bp = materialTypeApi.getByCode("BP").getCheckedData();
                if(bp!=null){
                    materialType.setMaterialTypeId(bp.getId());
                }
                materialType.setMaterialSourceId(materialConfigRespDTO1.getId()).setMaterialTypeCode("BP");
                materialType.setMaterialNumber(req.getPartNumber()+"_"+procedureList.get(index).getProcedureNum());
                String materialId = materialMCCApi.createMaterialConfig(materialType).getCheckedData();
                BeanUtil.copyProperties(materialConfigRespDTO1,materialConfigRespDTO);
                materialConfigRespDTO.setMaterialNumber(req.getPartNumber()+"_"+procedureList.get(index).getProcedureNum());
                materialConfigRespDTO.setId(materialId);
            }

            //下面的要被替换
            planPurchaseMaterialDO.setMaterialNumber(materialConfigRespDTO.getMaterialNumber());
            planPurchaseMaterialDO.setMaterialId(materialConfigRespDTO.getId());
            planPurchaseMaterialDO.setMaterialName(materialConfigRespDTO.getMaterialName());
            planPurchaseMaterialDO.setMaterialSpecification(materialConfigRespDTO.getMaterialSpecification());
        }
        orderMaterialRelationMapper.updateBatch(relationDOList);
        planPurchaseMaterialDO.setPurchaseAmount(req.getMaterialCodeList().size());
        planPurchaseMaterialDO.setProjectPlanItemId(req.getPlanItemId());
        planPurchaseMaterialMapper.insert(planPurchaseMaterialDO);
    }

    @Override
    public List<PlanItemDO> getPlanItemListByProjectPlanId(String projectPlanId) {
        return planItemMapper.selectListByProjectPlanId(projectPlanId);
    }

    @Override
    public List<PlanItemDO> getPlanItemListByProjectId(String projectId) {
        return planItemMapper.selectListByProjectId(projectId);
    }

    private void createPlanItemList(PmsPlanDO plan, List<PlanItemDO> list) {
        list.forEach(o -> {
            o.setProjectPlanId(plan.getId());
            o.setProjectCode(plan.getProjectCode());
            o.setProjectId(plan.getProjectId());
        });
        planItemMapper.insertBatch(list);
    }

    private void updatePlanItemList(PmsPlanDO plan, List<PlanItemDO> list) {
        deletePlanItemByProjectPlanId(plan.getId());
//		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
//        createPlanItemList(plan, list);
    }

    /**
     * 原本是先删后增,本来没问题,但因为我的代码逻辑,不能随意删除子计划，否则关联的id就找不到了
     * @param plan
     * @param list
     */
    private void updatePlanItemList2(PmsPlanDO plan, List<PlanItemDO> list) {
        if(ObjectUtil.isNull(list)||list.size()==0){
            return;
        }
        //先查看旧数据
        List<PlanDeviceDO> oldData = getPlanDeviceListByProjectPlanId(plan.getId());
        List<String> oldIds = oldData.stream().map(PlanDeviceDO::getId).collect(Collectors.toList());
        for (PlanItemDO planItemDO : list) {
            if (planItemDO.getId()!=null){
                //按Id更新
                updatePlanItemById(planItemDO);
                oldIds.remove(planItemDO.getId());
            }else {
                //这种就是新增了(也没有新增按钮)
                planItemDO.setProjectPlanId(plan.getId());
                planItemMapper.insert(planItemDO);
            }
        }
        //直到最后,oldIds任然有值,说明已经被删除了(前端其实没有删除按钮,这个基本上用不到)
        if(oldIds.size()>0){
            planItemMapper.deleteBatchIds(oldIds);
            //子表下面的资源表也跟着删掉
            for (String oldId : oldIds) {
                deleteDevice(oldId);
                deleteCombination(oldId);
                deleteMaterial(oldId);
            }
        }


    }

    private void deletePlanItemByProjectPlanId(String projectPlanId) {
        planItemMapper.deleteByProjectPlanId(projectPlanId);
    }

    /**
     * 根据id,删除子计划
     * 清除关系表中子计划id
     * @param id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePlanItem(String id){
        LambdaQueryWrapperX<OrderMaterialRelationDO> relationDOLambdaQueryWrapperX = new LambdaQueryWrapperX<>();
        relationDOLambdaQueryWrapperX.eq(OrderMaterialRelationDO::getPlanItemId,id);
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(relationDOLambdaQueryWrapperX);
        //重置关系
        if(relationDOList.size()>0){
            LambdaUpdateWrapper<OrderMaterialRelationDO> relationDOLambdaUpdateWrapper = new LambdaUpdateWrapper<>();
            relationDOLambdaUpdateWrapper.set(OrderMaterialRelationDO::getPlanItemId,null);
            relationDOLambdaUpdateWrapper.set(OrderMaterialRelationDO::getMaterialStatus,1);
            relationDOLambdaUpdateWrapper.set(OrderMaterialRelationDO::getOrderNumber,null);
            relationDOLambdaUpdateWrapper.set(OrderMaterialRelationDO::getPlanId,null);
            relationDOLambdaUpdateWrapper.eq(OrderMaterialRelationDO::getPlanItemId,id);
            orderMaterialRelationMapper.update(relationDOLambdaUpdateWrapper);
        }
        PlanItemDO planItemDO = planItemMapper.selectById(id);
        Long userId = getLoginUserId();
        AdminUserRespDTO user = userApi.getUser(userId).getCheckedData();

        NotifyMessageSaveReqVO notifyReq = new NotifyMessageSaveReqVO();
        //type目前没有作用,是预留字段
        notifyReq.setReadStatus(false).setTemplateNickname("系统通知").setType(4).setUserId(userId)
                .setTemplateContent("订单编号:"+planItemDO.getOrderNumber()+"已被用户"+user.getNickname()+"删除");
        messageService.createNotifyMessage(notifyReq);
        planItemMapper.deleteById(id);
        Object checkedData = mcsManufacturingControlApi.orderFormCancel(planItemDO.getOrderNumber()).getCheckedData();
    }

    @Override
    public List<PlanItemDO> selectListMaterialUse(Collection<String> projectIds){
      return planItemMapper.selectListMaterialUse(projectIds);
    }

    /**
     * 更新子项目计划,和下面那个不一样，这个单纯的是更新子项目
     * 这个id是子计划自己的id
     * @param planItemDO
     */
    public void updatePlanItemById(PlanItemDO planItemDO){
        planItemMapper.updateById(planItemDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePlanItem(@Valid PmsPlanItemReqVO updateReqVO) {

        PlanItemDO planItemDO = planItemMapper.selectById(updateReqVO.getId());
        if(ObjectUtil.isNotNull(planItemDO)){
            //TODO 先注掉,这个地方有变化
//            //实际库存,可用加自身使用
//            //特殊情况,我重新选了一次工艺方案,这个时候实际库存不应该加自身使用，因为不是一种物料
//            BigDecimal actually = new BigDecimal(0);
//            if(updateReqVO.getMaterialId().equals(planItemDO.getMaterialId())){
//                actually = updateReqVO.getAvailable().add(planItemDO.getUseInventory());
//            }else {
//                actually = updateReqVO.getAvailable();
//            }
//            //计划数量
//            BigDecimal quantity = updateReqVO.getQuantity();
//            if(actually.compareTo(quantity)>=0){
//                //实际可用数量多于计划数量,使用数量和计划数量一致
//                updateReqVO.setUseInventory(quantity);
//            }else {
//                //实际可用数量小于计划数量,能用多少用多少
//                updateReqVO.setUseInventory(actually);
//            }
            PlanItemDO planItemDO1 = BeanUtils.toBean(updateReqVO, PlanItemDO.class);
            planItemMapper.updateById(planItemDO1);


        }else {
            throw exception(PLAN_ITEM_NOT_EXISTS);
        }
        //现在物料不和产品一栏了，物料单独存,目前是一个
        planPurchaseMaterialMapper.deleteByProjectPlanItemId(planItemDO.getId());
        PlanPurchaseMaterialDO planPurchaseMaterialDO = BeanUtils.toBean(updateReqVO, PlanPurchaseMaterialDO.class)
                .setProjectPlanItemId(planItemDO.getId()).setProjectPlanId(planItemDO.getProjectPlanId())
                .setId(null).setProjectCode(planItemDO.getProjectCode()).setPartNumber(planItemDO.getPartNumber());
        planPurchaseMaterialDO.setPurchaseAmount(planPurchaseMaterialDO.getQuantity()-planPurchaseMaterialDO.getUseInventory().intValue());
        planPurchaseMaterialMapper.insert(planPurchaseMaterialDO);

    }
    @Override
    public void updatePlanItem2(@Valid PmsPlanItemReqVO updateReqVO) {
        PlanItemDO planItemDO1 = BeanUtils.toBean(updateReqVO, PlanItemDO.class);
        planItemMapper.updateById(planItemDO1);
    }

    @Override
    public List<PlanItemDO> selectByMaterialId(String materialId) {
        return planItemMapper.selectListByMaterialId(materialId);
    }

    @Override
    public List<PlanItemDO> selectByProjectCodes(List<String> projectCodes) {
        if(projectCodes.size()==0){
            return new ArrayList<PlanItemDO>();
        }
        return planItemMapper.selectList(PlanItemDO::getProjectCode,projectCodes);
    }

    /**
     * 创建空的订单物料关系
     */
    public void createRelation(String projectId,String orderId,Integer quantity){
        //创建空关系
        List<OrderMaterialRelationDO> relationDOList = new ArrayList<>();
        for (int i = 0; i < quantity; i++) {
            relationDOList.add(new OrderMaterialRelationDO().setOrderId(orderId).setMaterialStatus(3).setProjectId(projectId));
        }
        orderMaterialRelationMapper.insertBatch(relationDOList);

    }

    // ==================== 子表（项目计划子表，物料采购计划中的设备采购） ====================

    @Override
    public List<PlanDeviceDO> getPlanDeviceListByProjectPlanId(String projectPlanId) {
        return planDeviceMapper.selectListByProjectPlanId(projectPlanId);
    }

    /**
     * 通过项目集合查询设备采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanDeviceDO> getPlanDeviceDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanDeviceDO>();
        }
        LambdaQueryWrapperX<PlanDeviceDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanDeviceDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanDeviceDO::getPartNumber);
        return planDeviceMapper.selectList(wrapperX);
    }

    @Override
    public List<PlanDeviceDO> getPlanDeviceListByProjectPlanItemId(String projectPlanItemId) {
        return planDeviceMapper.selectListByProjectPlanItemId(projectPlanItemId);
    }

    public void createPlanDeviceList(String projectPlanId, List<PlanDeviceDO> list) {
        list.forEach(o -> o.setProjectPlanId(projectPlanId));
        planDeviceMapper.insertBatch(list);
    }

    @Override
    public void saveDevice(String projectPlanItemId, List<PlanDeviceDO> list) {
//        list.forEach(o -> o.setProjectPlanId(projectPlanItemId));
        planDeviceMapper.insertBatch(list);
    }
    @Override
    public void deleteDevice(String projectPlanItemId) {
        deletePlanDeviceByProjectPlanItemId(projectPlanItemId);
    }

    //    private void updatePlanDeviceList(String projectPlanId, List<PlanDeviceDO> list) {
//        deletePlanDeviceByProjectPlanItemId(projectPlanId);
//        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
//        createPlanDeviceList(projectPlanId, list);
//    }
    private void deletePlanDeviceByProjectPlanId(String projectPlanId) {
        planDeviceMapper.deleteByProjectPlanId(projectPlanId);
    }

    private void deletePlanDeviceByProjectPlanItemId(String projectPlanItemId) {
        planDeviceMapper.deleteByProjectPlanItemId(projectPlanItemId);
    }

    // ==================== 子表（项目计划子表，物料采购计划中的工装采购） ====================
    @Override
    public List<PlanMaterialDO> getPlanMaterialListByProjectPlanItemId(String projectPlanItemId) {
        return planMaterialMapper.selectListByProjectPlanItemId(projectPlanItemId);
    }

    /**
     * 通过项目集合查询工装采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanMaterialDO> getPlanMaterialDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanMaterialDO>();
        }
        LambdaQueryWrapperX<PlanMaterialDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanMaterialDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanMaterialDO::getPartNumber);
        return planMaterialMapper.selectList(wrapperX);
    }

    @Override
    public void deleteMaterial(String projectPlanItemId) {
        planMaterialMapper.deleteByProjectPlanItemId(projectPlanItemId);
    }

    //删除子表,在项目计划更新和删除的时候使用
    private void deletePlanMaterialByProjectPlanId(String projectPlanId) {
        planMaterialMapper.deleteByProjectPlanId(projectPlanId);
    }

    @Override
    public void saveMaterial(String projectPlanItemId, List<PlanMaterialDO> list) {
//        list.forEach(o -> o.setProjectPlanItemId(projectPlanItemId));
        planMaterialMapper.insertBatch(list);
    }
    // ==================== 子表（项目计划子表，物料采购计划中的刀具采购） ====================
    @Override
    public List<PlanCombinationDO> getPlanCombinationListByProjectPlanItemId(String projectPlanItemId) {
        return planCombinationMapper.selectListByProjectPlanItemId(projectPlanItemId);
    }

    /**
     * 通过项目集合查询刀具采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanCombinationDO> getPlanCombinationDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanCombinationDO>();
        }
        LambdaQueryWrapperX<PlanCombinationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanCombinationDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanCombinationDO::getPartNumber);
        return planCombinationMapper.selectList(wrapperX);
    }

    @Override
    public void deleteCombination(String projectPlanItemId) {
        planCombinationMapper.deleteByProjectPlanItemId(projectPlanItemId);
    }

    //删除子表,在项目计划更新和删除的时候使用
    private void deletePlanCombinationByProjectPlanId(String projectPlanId) {
        planCombinationMapper.deleteByProjectPlanId(projectPlanId);
    }

    @Override
    public void saveCombination(String projectPlanItemId, List<PlanCombinationDO> list) {
        planCombinationMapper.insertBatch(list);
    }


    // ==================== 子表（项目计划子表，物料采购计划中的刀柄采购） ====================

    @Override
    public List<PlanDemandHiltDO> getPlanDemandHiltListByProjectPlanId(String projectPlanId) {
        return planDemandHiltMapper.selectListByProjectPlanId(projectPlanId);
    }

    /**
     * 通过项目集合查询刀柄采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanDemandHiltDO> getPlanDemandHiltDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanDemandHiltDO>();
        }
        LambdaQueryWrapperX<PlanDemandHiltDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanDemandHiltDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanDemandHiltDO::getPartNumber);
        return planDemandHiltMapper.selectList(wrapperX);
    }

    private void createPlanDemandHiltList(String projectPlanId, List<PlanDemandHiltDO> list) {
        list.forEach(o -> o.setProjectPlanId(projectPlanId));
        planDemandHiltMapper.insertBatch(list);
    }

    private void updatePlanDemandHiltList(String projectPlanId, List<PlanDemandHiltDO> list) {
        deletePlanDemandHiltByProjectPlanId(projectPlanId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createPlanDemandHiltList(projectPlanId, list);
    }

    private void deletePlanDemandHiltByProjectPlanId(String projectPlanId) {
        planDemandHiltMapper.deleteByProjectPlanId(projectPlanId);
    }

    // ==================== 子表（项目计划子表，物料采购计划中的工装采购） ====================

    @Override
    public List<PlanDemandMaterialDO> getPlanDemandMaterialListByProjectPlanId(String projectPlanId) {
        return planDemandMaterialMapper.selectListByProjectPlanId(projectPlanId);
    }

    /**
     * 通过项目集合查询工装采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanDemandMaterialDO> getPlanDemandMaterialDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanDemandMaterialDO>();
        }
        LambdaQueryWrapperX<PlanDemandMaterialDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanDemandMaterialDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanDemandMaterialDO::getPartNumber);
        return planDemandMaterialMapper.selectList(wrapperX);
    }

    private void createPlanDemandMaterialList(String projectPlanId, List<PlanDemandMaterialDO> list) {
        list.forEach(o -> o.setProjectPlanId(projectPlanId));
        planDemandMaterialMapper.insertBatch(list);
    }

    private void updatePlanDemandMaterialList(String projectPlanId, List<PlanDemandMaterialDO> list) {
        deletePlanDemandMaterialByProjectPlanId(projectPlanId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createPlanDemandMaterialList(projectPlanId, list);
    }

    private void deletePlanDemandMaterialByProjectPlanId(String projectPlanId) {
        planDemandMaterialMapper.deleteByProjectPlanId(projectPlanId);
    }

    // ==================== 子表（项目计划子表，物料采购计划中的设备采购） ====================

    @Override
    public List<PlanDemandDeviceDO> getPlanDemandDeviceListByProjectPlanId(String projectPlanId) {
        return planDemandDeviceMapper.selectListByProjectPlanId(projectPlanId);
    }
    /**
     * 通过项目集合查询刀具采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanDemandDeviceDO> getPlanDemandDeviceDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanDemandDeviceDO>();
        }
        LambdaQueryWrapperX<PlanDemandDeviceDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanDemandDeviceDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanDemandDeviceDO::getPartNumber);
        return planDemandDeviceMapper.selectList(wrapperX);
    }

    private void createPlanDemandDeviceList(String projectPlanId, List<PlanDemandDeviceDO> list) {
        list.forEach(o -> o.setProjectPlanId(projectPlanId));
        planDemandDeviceMapper.insertBatch(list);
    }

    private void updatePlanDemandDeviceList(String projectPlanId, List<PlanDemandDeviceDO> list) {
        deletePlanDemandDeviceByProjectPlanId(projectPlanId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createPlanDemandDeviceList(projectPlanId, list);
    }

    private void deletePlanDemandDeviceByProjectPlanId(String projectPlanId) {
        planDemandDeviceMapper.deleteByProjectPlanId(projectPlanId);
    }

    // ==================== 子表（项目计划子表，物料采购计划中的刀具采购） ====================

    @Override
    public List<PlanDemandCutterDO> getPlanDemandCutterListByProjectPlanId(String projectPlanId) {
        return planDemandCutterMapper.selectListByProjectPlanId(projectPlanId);
    }

    /**
     * 通过项目集合查询刀具采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanDemandCutterDO> getPlanDemandCutterDOListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanDemandCutterDO>();
        }
        LambdaQueryWrapperX<PlanDemandCutterDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanDemandCutterDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanDemandCutterDO::getPartNumber);
        return planDemandCutterMapper.selectList(wrapperX);
    }

    private void createPlanDemandCutterList(String projectPlanId, List<PlanDemandCutterDO> list) {
        list.forEach(o -> o.setProjectPlanId(projectPlanId));
        planDemandCutterMapper.insertBatch(list);
    }

    private void updatePlanDemandCutterList(String projectPlanId, List<PlanDemandCutterDO> list) {
        deletePlanDemandCutterByProjectPlanId(projectPlanId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createPlanDemandCutterList(projectPlanId, list);
    }

    private void deletePlanDemandCutterByProjectPlanId(String projectPlanId) {
        planDemandCutterMapper.deleteByProjectPlanId(projectPlanId);
    }

    // ==================== 子表（项目计划子表，物料采购计划中的物料采购） ====================
    @Override
    public List<PlanPurchaseMaterialDO> getPlanPurchaseMaterialListByProjectPlanId(String projectPlanId) {
        return planPurchaseMaterialMapper.selectListByProjectPlanId(projectPlanId);
    }

    @Override
    public List<PlanPurchaseMaterialDO> selectPurchaseMaterialByMaterialId(String materialId) {
        return planPurchaseMaterialMapper.selectPurchaseMaterialByMaterialId(materialId);
    }

    /**
     * 单获取物料采购计划
     * @param ids
     * @return
     */
    @Override
    public List<PlanPurchaseMaterialDO> getPlanPurchaseMaterialListByProjectIds(List<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        //先通过项目id查找到项目计划id
        LambdaQueryWrapperX<PmsPlanDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PmsPlanDO::getProjectId,ids);
        List<PmsPlanDO> pmsPlanDOList = planMapper.selectList(wrapperX);
        List<String> planIds = pmsPlanDOList.stream().map(PmsPlanDO::getId).collect(Collectors.toList());
        List<PlanPurchaseMaterialDO> planPurchaseMaterialList = getPlanPurchaseMaterialListByProjectPlanIds(planIds);
        return planPurchaseMaterialList;
    }

    /**
     * 通过项目集合查询物料采购,
     * 物料采购计划,左侧选项目的时候用
     * @param projectPlanIds
     * @return
     */
    public List<PlanPurchaseMaterialDO> getPlanPurchaseMaterialListByProjectPlanIds(List<String> projectPlanIds){
        if(projectPlanIds.size()==0){
            return new ArrayList<PlanPurchaseMaterialDO>();
        }
        LambdaQueryWrapperX<PlanPurchaseMaterialDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PlanPurchaseMaterialDO::getProjectPlanId,projectPlanIds).orderByDesc(PlanPurchaseMaterialDO::getPartNumber);
        return planPurchaseMaterialMapper.selectList(wrapperX);
    }

    private void deletePlanPurchaseMaterialDOByProjectPlanId(String projectPlanId) {
        planPurchaseMaterialMapper.deleteByProjectPlanId(projectPlanId);
    }

    private void deletePlanPurchaseMaterialDOByProjectPlanItemId(String projectPlanItemId) {
        planPurchaseMaterialMapper.deleteByProjectPlanItemId(projectPlanItemId);
    }

}
