package cn.iocoder.yudao.module.pms.service.pmsapproval;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import cn.iocoder.yudao.module.pms.controller.admin.delivery.vo.PmsPlanPurchaseRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.OrderProcessVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.order.vo.PmsOrderSaveReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalProcessVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.OrderListDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanPurchaseMaterialDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.assessment.AssessmentMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.order.OrderListMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PmsPlanMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.pms.enums.ProjectStatusEnum;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderService;
import cn.iocoder.yudao.module.pms.service.order.PmsOrderServiceImpl;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import com.miyu.module.pdm.api.feasibilityDetail.FeasibilityDetailApi;
import com.miyu.module.pdm.api.processPlan.ProcessPlanApi;
import com.miyu.module.pdm.api.projectPlan.PdmProjectPlanApi;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import org.springframework.web.bind.annotation.RequestParam;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.pms.enums.LogRecordConstants.*;

/**
 * pms 立项表,项目立项相关 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PmsApprovalServiceImpl implements PmsApprovalService {

    @Resource
    private PmsApprovalMapper approvalMapper;

    @Resource
    private AssessmentMapper assessmentMapper;

    @Resource
    private PmsOrderMapper orderMapper;

    @Resource
    private PmsPlanMapper pmsPlanMapper;

    @Resource
    private OrderListMapper orderListMapper;

    @Resource
    private FeasibilityDetailApi feasibilityDetailApi;

    @Resource
    private OrderMaterialRelationMapper relationMapper;

    /**
     * 对应的流程定义 KEY
     */
    public static final String PROCESS_KEY = "pms_approval";

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private ProcessPlanApi processPlanApi;

    @Resource
    private PmsOrderService pmsOrderService;

    @Resource
    private PdmProjectPlanApi pdmProjectPlanApi;

    @Resource
    private PmsPlanService pmsPlanService;

    @Resource
    private ShippingApi shippingApi;

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createApproval(PmsApprovalSaveReqVO createReqVO) {
        // 插入
        PmsApprovalDO approval = BeanUtils.toBean(createReqVO, PmsApprovalDO.class).setProjectStatus(ProjectStatusEnum.NotStart.getStatus());
        approvalMapper.insert(approval);

        if(createReqVO.getOrderLists()!=null&&createReqVO.getOrderLists().size()>0){
            List<PmsOrderDO> orderList = createReqVO.getOrderLists();
            for (PmsOrderDO orderDO : orderList) {
                orderDO.setProjectName(approval.getProjectName());
                orderDO.setProjectId(approval.getId());
                orderDO.setProjectCode(approval.getProjectCode());
                PmsOrderSaveReqVO pmsOrderSaveReqVO = BeanUtils.toBean(orderDO, PmsOrderSaveReqVO.class);
                pmsOrderService.createOrder(pmsOrderSaveReqVO);
            }
        }
        // 返回
        return approval.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createApprovalWithOrder(PmsApprovalSaveReqVO createReqVO) {
        // 插入
        PmsApprovalDO approval = BeanUtils.toBean(createReqVO, PmsApprovalDO.class).setProjectStatus(ProjectStatusEnum.NotStart.getStatus());
        approvalMapper.insert(approval);

        //订单，有就更新,没有就新建一个
        PmsOrderDO order = createReqVO.getOrder();
        if(order.getId()!=null){
            PmsOrderDO pmsOrderDO = orderMapper.selectById(order.getId());
            pmsOrderDO.setProjectId(approval.getId());
            pmsOrderDO.setProjectCode(approval.getProjectCode());
            pmsOrderDO.setProjectName(approval.getProjectName());
            //订单状态是1,处理订单后待审核
            pmsOrderDO.setOrderStatus(1);
            orderMapper.updateById(pmsOrderDO);
            //订单物料关系创建
            createRelation(approval.getId(),pmsOrderDO.getId(),pmsOrderDO.getQuantity());
        }else {
            order.setProjectId(approval.getId());
            order.setProjectCode(approval.getProjectCode());
            order.setProjectName(approval.getProjectName());
            //订单状态是1,未开始和待审核分开
            order.setOrderStatus(1);
            //内部自制订单
            order.setOrderType(1);
            orderMapper.insert(order);
            //订单物料关系创建
            createRelation(approval.getId(),order.getId(),order.getQuantity());
        }
        // 返回
        return approval.getId();
    }

    /**
     * 创建空的订单物料关系
     */
    public void createRelation(String projectId,String orderId,Integer quantity){
        //先查找关系
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(OrderMaterialRelationDO::getOrderId,orderId);
        List<OrderMaterialRelationDO> relationDOList1 = relationMapper.selectList(wrapperX);
        //能查到说明这是删除项目的那种情况,关系已经创建,这种比较极端一般不会出现,但还是判断一下
        if(relationDOList1.size()>0){
            for (OrderMaterialRelationDO relationDO : relationDOList1) {
                //设置新的项目id
                relationDO.setProjectId(projectId).setOrderId(orderId).setMaterialStatus(3);
            }
            relationMapper.updateBatch(relationDOList1);
        }else {
            //创建空关系
            List<OrderMaterialRelationDO> relationDOList = new ArrayList<>();
            for (int i = 0; i < quantity; i++) {
                relationDOList.add(new OrderMaterialRelationDO().setProjectId(projectId).setOrderId(orderId).setMaterialStatus(3));
            }
            relationMapper.insertBatch(relationDOList);
        }

    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PMS_PROJECT_TYPE, subType = PMS_PROJECT_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = PMS_PROJECT_UPDATE_SUCCESS)
    public void updateApproval(PmsApprovalSaveReqVO updateReqVO) {
        // 校验存在
        PmsApprovalDO oldProject = validateApprovalExists(updateReqVO.getId());
        // 更新
        PmsApprovalDO updateObj = BeanUtils.toBean(updateReqVO, PmsApprovalDO.class);
        approvalMapper.updateById(updateObj);
        List<PmsOrderDO> orderList = updateReqVO.getOrderLists();

        //原本存在的
        List<PmsOrderDO> orderList2 = getOrderListByProjectId(updateObj.getId());
        //Map<String, PmsOrderDO> orderDOMap = CollectionUtils.convertMap(orderList2, PmsOrderDO::getId);
        List<String> orderIds = orderList2.stream().map(PmsOrderDO::getId).collect(Collectors.toList());

        for (PmsOrderDO order : orderList) {
            if(ObjectUtil.isNotNull(order.getId())){
                orderIds.remove(order.getId());
                //PmsOrderDO oldOrder = orderDOMap.get(order.getId());
                orderMapper.updateById(order);
                //updateOrderLog(order,oldOrder);
            }else {
                order.setProjectName(updateObj.getProjectName());
                order.setProjectId(updateObj.getId());
                order.setProjectCode(updateObj.getProjectCode());
                PmsOrderSaveReqVO pmsOrderSaveReqVO = BeanUtils.toBean(order, PmsOrderSaveReqVO.class);
                pmsOrderService.createOrder(pmsOrderSaveReqVO);
            }
        }
        //被项目删除的订单，解除绑定即可，无需删除
        if(orderIds.size()>0){
            for (String orderId : orderIds) {
                pmsOrderService.unbind(orderId);
            }
        }
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldProject, PmsApprovalSaveReqVO.class));
        LogRecordContext.putVariable("project", oldProject);

//        PmsOrderDO order = updateReqVO.getOrder();
//        if(order!=null){
//            order.setProjectCode(updateObj.getProjectCode());
//            orderMapper.updateById(order);
//        }
    }
//    @LogRecord(type = PMS_PROJECT_ORDER_TYPE, subType = PMS_PROJECT_ORDER_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
//            success = PMS_PROJECT_ORDER_UPDATE_SUCCESS)
//    public void updateOrderLog(PmsOrderDO updateReqVO,PmsOrderDO oldOrder){
//        orderMapper.updateById(updateReqVO);
//        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(oldOrder, PmsOrderDO.class));
//        LogRecordContext.putVariable("order", oldOrder);
//    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void closeApproval(@Valid PmsApprovalSaveReqVO updateReqVO) {
        PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(updateReqVO.getId());
        List<PmsOrderDO> orderList = getOrderListByProjectId(updateReqVO.getId());
        for (PmsOrderDO orderDO : orderList) {
            orderDO.setOrderStatus(6);
        }
        //更新项目
        PmsApprovalDO updateObj = BeanUtils.toBean(updateReqVO, PmsApprovalDO.class);
        approvalMapper.updateById(updateObj);
        orderMapper.updateBatch(orderList);

        //关闭项目关联的工艺
        String checkedData = feasibilityDetailApi.updateProjectstatus(pmsApprovalDO.getProjectCode()).getCheckedData();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PMS_PROJECT_TYPE, subType = PMS_PROJECT_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = PMS_PROJECT_DELETE_SUCCESS)
    public void deleteApproval(String id) {
        // 校验存在
        PmsApprovalDO pmsApprovalDO = validateApprovalExists(id);
        // 删除
        approvalMapper.deleteById(id);
        //还要删除订单,只删自制订单
        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectList(PmsOrderDO::getProjectId, pmsApprovalDO.getId());
        Iterator<PmsOrderDO> iterator = pmsOrderDOS.iterator();
        while (iterator.hasNext()){
            PmsOrderDO orderDO = iterator.next();

            if(orderDO.getOrderType()==1){
                //自制订单
                orderMapper.deleteById(orderDO.getId());
            }else {
                orderDO.setOrderStatus(0).setProjectId(null).setProjectCode(null).setProjectName(null);
                orderMapper.updateOutOrder(orderDO);
            }
        }
        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("project", pmsApprovalDO);

    }

    private PmsApprovalDO validateApprovalExists(String id) {
        PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(id);
        if (approvalMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.APPROVAL_NOT_EXISTS);
        }
        return pmsApprovalDO;
    }

    @Override
    public PmsApprovalDO getApproval(String id) {
        return approvalMapper.selectById(id);
    }

    @Override
    public PageResult<PmsApprovalDO> getApprovalPage(PmsApprovalPageReqVO pageReqVO) {
        return approvalMapper.selectPage(pageReqVO);
    }

    @Override
    public PageResult<PmsApprovalDO> selectPageWithPass(PmsApprovalPageReqVO pageReqVO) {
        return approvalMapper.selectPageWithPass(pageReqVO);
    }

    @Override
    public List<PmsApprovalDO> selectListWithPass(PmsApprovalPageReqVO pageReqVO) {
        return approvalMapper.selectListWithPass(pageReqVO);
    }

    @Override
    public List<PmsApprovalDO> selectListWithPass2(PmsApprovalReqVO reqVO) {
        return approvalMapper.selectListWithPass2(reqVO);
    }

    @Override
    public List<PmsApprovalDO> selectListWithCondition(PmsApprovalReqVO reqVO) {
        return approvalMapper.selectListWithCondition(reqVO);
    }

    @Override
    public PageResult<PmsApprovalDO> selectPageWithStatus(PmsApprovalPageReqVO pageReqVO){
        return approvalMapper.selectPageWithStatus(pageReqVO);
    }

    /**
     * 更新项目审批状态，项目状态，订单状态
     * 发起工艺评估
     * @param id 编号
     * @param status 结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateStatus(String id, Integer status) {
        PmsApprovalDO pmsApprovalDO = validateLeaveExists2(id);
        //这是项目审批没通过的情况
        if(status!=2){
            //异常终止
            pmsApprovalDO.setProjectStatus(7L).setStatus(status);
            approvalMapper.updateById(pmsApprovalDO);
            List<PmsOrderDO> pmsOrderDOList = orderMapper.selectList(PmsOrderDO::getProjectId, id);
            for (PmsOrderDO orderDO : pmsOrderDOList) {
                orderDO.setOrderStatus(7);
                orderMapper.updateById(orderDO);
            }
            return;
        }

        if(pmsApprovalDO.getNeedsAssessment()==1){

            pmsApprovalDO.setProjectStatus(2L).setStatus(status);
        }else {
            //不需要评审，项目状态直接就是准备中,需要评审的,评审后改为准备中
            pmsApprovalDO.setProjectStatus(3L).setStatus(status);
        }

        approvalMapper.updateById(pmsApprovalDO);
        List<PmsOrderDO> pmsOrderList = orderMapper.selectList(PmsOrderDO::getProjectId, id);

        //选评审的才评估
        if(pmsApprovalDO.getNeedsAssessment()==1){
            for (PmsOrderDO orderDO : pmsOrderList) {
                orderDO.setOrderStatus(2);
                //发起工艺评估
                String checkedData = feasibilityDetailApi.pushFeasibility(pmsApprovalDO.getProjectCode(), orderDO.getPartNumber(), orderDO.getPartName(), orderDO.getProcessCondition()).getCheckedData();
            }

//            //待评审
//            PmsOrderDO orderDO = pmsOrderDO.setOrderStatus(2);
//            orderMapper.updateById(orderDO);
//           //发起工艺评估
//            String checkedData = feasibilityDetailApi.pushFeasibility(pmsApprovalDO.getProjectCode(), pmsOrderDO.getPartNumber(), pmsOrderDO.getPartName(), pmsOrderDO.getProcessCondition()).getCheckedData();
        }else {
            //无需审核
            for (PmsOrderDO orderDO : pmsOrderList) {
                orderDO.setOrderStatus(3);
                //非整单外协才通知工艺方案
//                if(orderDO.getOutsource()!=1){
//                    String checkedData = processPlanApi.pushProcess(orderDO.getProjectCode(), orderDO.getPartNumber(), orderDO.getPartName(), orderDO.getProcessCondition()).getCheckedData();
//                }

            }
//            //无需审核
//            PmsOrderDO orderDO = pmsOrderDO.setOrderStatus(3);
//            orderMapper.updateById(orderDO);
//            //发起工艺编制,不需要工艺评估了，直接进行工艺编制
//            //非整单外协才通知工艺方案
//            if(orderDO.getOutsource()!=1){
//                String checkedData = processPlanApi.pushProcess(orderDO.getProjectCode(), orderDO.getPartNumber(), orderDO.getPartName(), orderDO.getProcessCondition()).getCheckedData();
//            }

        }
        if(pmsOrderList.size()>0){
            orderMapper.updateBatch(pmsOrderList);
        }

    }

    /**
     * 更新负责人任命
     * @param map
     */
    @Override
    public String apponit(Map<String,String> map) {
        validateLeaveExists(map.get("approvalId"));
        PmsApprovalDO approvalDO = new PmsApprovalDO().setId(map.get("approvalId"));
        if(map.containsKey("responsiblePerson")){
            approvalDO.setResponsiblePerson(Long.valueOf(map.get("responsiblePerson")));
        }
        if(map.containsKey("projectManager")){
            approvalDO.setProjectManager(Long.valueOf(map.get("projectManager")));
        }
        approvalMapper.updateById(approvalDO);
        return approvalDO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createApprovalBpm(@Valid PmsApprovalSaveReqVO createReqVO) {
//        updateApproval(createReqVO);
        Long userId = getLoginUserId();
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("approvalId",createReqVO.getId());
        processInstanceVariables.put("projectType",createReqVO.getProjectType());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(createReqVO.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();
        // 将工作流的编号，更新到 OA 请假单中
        approvalMapper.updateById(new PmsApprovalDO().setId(createReqVO.getId()).setProcessInstanceId(processInstanceId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus()).setProjectStatus(ProjectStatusEnum.Start.getStatus()));

        //将订单状态从未开始变为待审核
        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectList(PmsOrderDO::getProjectId, createReqVO.getId());
        for (PmsOrderDO pmsOrderDO : pmsOrderDOS) {
            pmsOrderDO.setOrderStatus(1);
        }
        if(pmsOrderDOS.size()>0){
            orderMapper.updateBatch(pmsOrderDOS);
        }
        return createReqVO.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createApprovalBpm2(@Valid PmsApprovalSaveReqVO createReqVO) {

        Long userId = getLoginUserId();
        // 发起 BPM 流程
        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("approvalId",createReqVO.getId());
        processInstanceVariables.put("projectType",createReqVO.getProjectType());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(createReqVO.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();
        // 将工作流的编号，更新到 OA 请假单中
        approvalMapper.updateById(new PmsApprovalDO().setId(createReqVO.getId()).setProcessInstanceId(processInstanceId).setStatus(BpmTaskStatusEnum.RUNNING.getStatus()).setProjectStatus(ProjectStatusEnum.StayAssessment.getStatus()));
        return createReqVO.getId();
    }

    /**
     * 获取全部项目
     * @return
     */
    @Override
    public List<PmsApprovalDO> getApprovalAll() {
        return approvalMapper.selectList();
    }

    @Override
    public PmsApprovalDO getByProjectCode(String code) {
        return approvalMapper.selectOne(PmsApprovalDO::getProjectCode,code);
    }

    @Override
    public List<PmsApprovalDO> getByStatus(List<Integer> status) {
        LambdaQueryWrapperX<PmsApprovalDO> wrapperX = new LambdaQueryWrapperX<>();
        if(CollectionUtils.isAnyEmpty(status)){
            wrapperX.isNull(PmsApprovalDO::getStatus);
        }else {
            wrapperX.in(PmsApprovalDO::getStatus,status);
        }
        List<PmsApprovalDO> pmsApprovalList = approvalMapper.selectList(wrapperX);
        return pmsApprovalList;
    }

    @Override
    public List<PmsApprovalDO> getByProjectStatus(List<Integer> status) {
        if(CollectionUtils.isAnyEmpty(status)){
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<PmsApprovalDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PmsApprovalDO::getProjectStatus, status);
        List<PmsApprovalDO> pmsApprovalList = approvalMapper.selectList(wrapperX);
        return pmsApprovalList;
    }

    @Override
    public List<PmsApprovalDO> getByProjectCodes(List<String> codes) {
        if (codes.size()==0){
            return new ArrayList<PmsApprovalDO>();
        }
        return approvalMapper.selectList(PmsApprovalDO::getProjectCode,codes);
    }

    @Override
    public List<PmsOrderDO> getPmsOrderList(List<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<PmsOrderDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PmsOrderDO::getProjectId,ids);
        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectList(wrapperX);

        return pmsOrderDOS;
    }

    @Override
    public List<PmsPlanDO> getPmsPlanList(List<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return Collections.emptyList();
        }
        LambdaQueryWrapperX<PmsPlanDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(PmsPlanDO::getProjectId,ids);
        List<PmsPlanDO> pmsPlanDOS = pmsPlanMapper.selectList(wrapperX);
        return pmsPlanDOS;
    }

    @Override
    public List<PmsApprovalProcessVO> projectProgress() {
        //查询所有进行中的项目
        LambdaQueryWrapperX<PmsApprovalDO> projectWrapper = new LambdaQueryWrapperX<>();
        projectWrapper.in(PmsApprovalDO::getProjectStatus,Arrays.asList(3,4,5));
        projectWrapper.orderByDesc(PmsApprovalDO::getId);
        List<PmsApprovalDO> pmsApprovalDOS = approvalMapper.selectList(projectWrapper);
        //项目id
        List<String> projectIds = pmsApprovalDOS.stream().map(PmsApprovalDO::getId).collect(Collectors.toList());
        if(CollectionUtils.isAnyEmpty(projectIds)){
            return Collections.emptyList();
        }
        //查询项目下的所有订单
        LambdaQueryWrapperX<PmsOrderDO> orderWrapper = new LambdaQueryWrapperX<>();
        orderWrapper.in(PmsOrderDO::getProjectId,projectIds);
        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectList(orderWrapper);
        Map<String, PmsOrderDO> orderDOMap = CollectionUtils.convertMap(pmsOrderDOS, PmsOrderDO::getId);
        //查询计划
        LambdaQueryWrapperX<PmsPlanDO> planWrapper = new LambdaQueryWrapperX<>();
        planWrapper.in(PmsPlanDO::getProjectId,projectIds);
        List<PmsPlanDO> pmsPlanDOS = pmsPlanMapper.selectList(planWrapper);
        //把计划存储成map
        Map<String,List<PmsPlanDO>> map = new HashMap<>();
        for (PmsPlanDO pmsPlanDO : pmsPlanDOS) {
            if(map.containsKey(pmsPlanDO.getProjectId())){
                map.get(pmsPlanDO.getProjectId()).add(pmsPlanDO);
            }else {
                List<PmsPlanDO> list = new ArrayList<>();
                list.add(pmsPlanDO);
                map.put(pmsPlanDO.getProjectId(),list);
            }
        }

        //计算工艺进度
        for (PmsPlanDO pmsPlanDO : pmsPlanDOS) {
            if(pmsPlanDO.getProcessScheme()!=null){
                //进度为100
                pmsPlanDO.setSchemeProcess(100);
            }else {
                PmsOrderDO orderDO = orderDOMap.get(pmsPlanDO.getProjectOrderId());
                //整单外协没有工艺,直接给100
                if (orderDO.getOutsource()==1){
                    pmsPlanDO.setSchemeProcess(100);
                }else {
                    List<ProjPartBomTreeRespDTO> schemeList = pdmProjectPlanApi.getProjPartBomTreeListNew(pmsPlanDO.getProjectCode(), pmsPlanDO.getPartNumber()).getCheckedData();
                    if(schemeList.size()>0){
                        //进度为100
                        pmsPlanDO.setSchemeProcess(100);
                    }else {
                        //按日期算进度
                        LocalDate create=pmsPlanDO.getCreateTime().toLocalDate();
                        LocalDate preparation = pmsPlanDO.getProcessPreparationTime().toLocalDate();
                        LocalDate now = LocalDate.now();
                        long until = create.until(preparation, ChronoUnit.DAYS);
                        long unti2 = create.until(now, ChronoUnit.DAYS);
                        long schemeProcess = 100 * unti2 / until;

                        pmsPlanDO.setSchemeProcess((int)schemeProcess);
                        if(schemeProcess>100){
                            pmsPlanDO.setSchemeProcess(99);
                        }

                    }
                }
            }
        }
        //计算采购进度
        List<PmsPlanPurchaseRespVO> purchaseList = getPurchaseByProjectId(projectIds);
        Map<String,Integer> totalMap = new HashMap<>();
        Map<String,Integer> purchaseMap = new HashMap<>();
        for (PmsPlanPurchaseRespVO pmsPlanPurchaseRespVO : purchaseList) {
            String projectId = pmsPlanPurchaseRespVO.getProjectId();
            if(totalMap.containsKey(projectId)){
                totalMap.put(projectId,totalMap.get(projectId) + pmsPlanPurchaseRespVO.getPurchaseAmount());
            }else {
                totalMap.put(projectId,pmsPlanPurchaseRespVO.getPurchaseAmount());
            }
            if(purchaseMap.containsKey(projectId)){
                purchaseMap.put(projectId,purchaseMap.get(projectId) + pmsPlanPurchaseRespVO.getAmount()-pmsPlanPurchaseRespVO.getReturnAmount());
            }else {
                purchaseMap.put(projectId,pmsPlanPurchaseRespVO.getAmount()-pmsPlanPurchaseRespVO.getReturnAmount());
            }
        }

        //计算生产进度
        LambdaQueryWrapperX<OrderMaterialRelationDO> relationWrapperX = new LambdaQueryWrapperX<>();
        relationWrapperX.in(OrderMaterialRelationDO::getProjectId,projectIds);
        relationWrapperX.eq(OrderMaterialRelationDO::getMaterialStatus,5);
        List<OrderMaterialRelationDO> relationDOS = relationMapper.selectList(relationWrapperX);
        Map<String,Integer> productionMap = new HashMap<>();
        for (OrderMaterialRelationDO relationDO : relationDOS) {
            if(productionMap.containsKey(relationDO.getProjectId())){
                productionMap.put(relationDO.getProjectId(),productionMap.get(relationDO.getProjectId())+1);
            }else {
                productionMap.put(relationDO.getProjectId(),1);
            }
        }


        List<PmsApprovalProcessVO> pmsApprovalProcessVOS = BeanUtils.toBean(pmsApprovalDOS, PmsApprovalProcessVO.class, vo -> {
            //工艺进度,生产进度加时间
            if (map.containsKey(vo.getId())) {
                List<PmsPlanDO> pmsPlanDOS1 = map.get(vo.getId());
                //轮次,方便比较时间和计算进度
                int count = 0;
                //工艺进度总和
                int schemeProcess = 0;
                //项目生产总和
                int total = 0;
                for (PmsPlanDO pmsPlanDO : pmsPlanDOS1) {
                    total = total + pmsPlanDO.getQuantity();
                    if (count == 0) {
                        vo.setProductionTime(pmsPlanDO.getPlanDeliveryTime());
                        vo.setPurchaseTime(pmsPlanDO.getPurchaseCompletionTime());
                        if(pmsPlanDO.getProcessPreparationTime()!=null){
                            vo.setSchemeTime(pmsPlanDO.getProcessPreparationTime());
                        }
                        schemeProcess = schemeProcess + pmsPlanDO.getSchemeProcess();
                    } else {
                        if (pmsPlanDO.getPlanDeliveryTime().isAfter(vo.getProductionTime())) {
                            vo.setProductionTime(pmsPlanDO.getPlanDeliveryTime());
                        }
                        if(pmsPlanDO.getProcessPreparationTime()!=null){
                            if (pmsPlanDO.getPurchaseCompletionTime().isAfter(vo.getPurchaseTime())) {
                                vo.setPurchaseTime(pmsPlanDO.getPurchaseCompletionTime());
                            }
                        }
                        if (pmsPlanDO.getProcessPreparationTime().isAfter(vo.getSchemeTime())) {
                            vo.setSchemeTime(pmsPlanDO.getPlanDeliveryTime());
                        }
                        schemeProcess = schemeProcess + pmsPlanDO.getSchemeProcess();
                    }
                    count++;
                }
                //生产进度
                if (productionMap.containsKey(vo.getId())) {
                    Integer product = productionMap.get(vo.getId());
                    vo.setProductionProcess(100*product / total);
                } else {
                    vo.setProductionProcess(0);
                }
                //工艺进度
                if(count!=0){
                    vo.setSchemeProcess(schemeProcess / count);
                }else {
                    vo.setSchemeProcess(0);
                }
                //采购进度
                if (totalMap.containsKey(vo.getId())) {
                    vo.setPurchaseProcess(100*purchaseMap.get(vo.getId()) / totalMap.get(vo.getId()));
                } else {
                    vo.setPurchaseProcess(0);
                }
            }
        });

        return pmsApprovalProcessVOS;
    }

    /**
     * 订单进度
     * @return
     */
    @Override
    public List<OrderProcessVO> orderProgress() {
        //查询所有进行中的项目
        LambdaQueryWrapperX<PmsApprovalDO> projectWrapper = new LambdaQueryWrapperX<>();
        projectWrapper.in(PmsApprovalDO::getProjectStatus,Arrays.asList(3,4,5));
        projectWrapper.orderByDesc(PmsApprovalDO::getId);
        List<PmsApprovalDO> pmsApprovalDOS = approvalMapper.selectList(projectWrapper);
        //项目id
        List<String> projectIds = pmsApprovalDOS.stream().map(PmsApprovalDO::getId).collect(Collectors.toList());
        if(CollectionUtils.isAnyEmpty(projectIds)){
            return Collections.emptyList();
        }
        //查询项目下的所有订单
        LambdaQueryWrapperX<PmsOrderDO> orderWrapper = new LambdaQueryWrapperX<>();
        orderWrapper.in(PmsOrderDO::getProjectId,projectIds);
        List<PmsOrderDO> pmsOrderDOS = orderMapper.selectList(orderWrapper);
        Map<String, PmsOrderDO> orderDOMap = CollectionUtils.convertMap(pmsOrderDOS, PmsOrderDO::getId);
        //查询计划
        LambdaQueryWrapperX<PmsPlanDO> planWrapper = new LambdaQueryWrapperX<>();
        planWrapper.in(PmsPlanDO::getProjectId,projectIds);
        planWrapper.orderByDesc(PmsPlanDO::getId);
        List<PmsPlanDO> pmsPlanDOS = pmsPlanMapper.selectList(planWrapper);
        Map<String, PmsPlanDO> planMap = CollectionUtils.convertMap(pmsPlanDOS, PmsPlanDO::getId);
        //把计划存储成map
//        Map<String,List<PmsPlanDO>> map = new HashMap<>();
//        for (PmsPlanDO pmsPlanDO : pmsPlanDOS) {
//            if(map.containsKey(pmsPlanDO.getProjectId())){
//                map.get(pmsPlanDO.getProjectId()).add(pmsPlanDO);
//            }else {
//                List<PmsPlanDO> list = new ArrayList<>();
//                list.add(pmsPlanDO);
//                map.put(pmsPlanDO.getProjectId(),list);
//            }
//        }

        //计算工艺进度
        for (PmsPlanDO pmsPlanDO : pmsPlanDOS) {
            if(pmsPlanDO.getProcessScheme()!=null){
                //进度为100
                pmsPlanDO.setSchemeProcess(100);
            }else {
                PmsOrderDO orderDO = orderDOMap.get(pmsPlanDO.getProjectOrderId());
                //整单外协没有工艺,直接给100
                if (orderDO.getOutsource()==1){
                    pmsPlanDO.setSchemeProcess(100);
                }else {
                    List<ProjPartBomTreeRespDTO> schemeList = pdmProjectPlanApi.getProjPartBomTreeListNew(pmsPlanDO.getProjectCode(), pmsPlanDO.getPartNumber()).getCheckedData();
                    if(schemeList.size()>0){
                        //进度为100
                        pmsPlanDO.setSchemeProcess(100);
                    }else {
                        //按日期算进度
                        LocalDate create=pmsPlanDO.getCreateTime().toLocalDate();
                        LocalDate preparation = pmsPlanDO.getProcessPreparationTime().toLocalDate();
                        LocalDate now = LocalDate.now();
                        long until = create.until(preparation, ChronoUnit.DAYS);
                        long unti2 = create.until(now, ChronoUnit.DAYS);
                        long schemeProcess = 100 * unti2 / until;

                        pmsPlanDO.setSchemeProcess((int)schemeProcess);
                        if(schemeProcess>100){
                            pmsPlanDO.setSchemeProcess(99);
                        }

                    }
                }
            }
        }
        //计算采购进度
        List<PmsPlanPurchaseRespVO> purchaseList = getPurchaseByProjectId(projectIds);
        Map<String,Integer> totalMap = new HashMap<>();
        Map<String,Integer> purchaseMap = new HashMap<>();
        for (PmsPlanPurchaseRespVO pmsPlanPurchaseRespVO : purchaseList) {
            String orderId = pmsPlanPurchaseRespVO.getProjectOrderId();
            if(totalMap.containsKey(orderId)){
                totalMap.put(orderId,totalMap.get(orderId) + pmsPlanPurchaseRespVO.getPurchaseAmount());
            }else {
                totalMap.put(orderId,pmsPlanPurchaseRespVO.getPurchaseAmount());
            }
            if(purchaseMap.containsKey(orderId)){
                purchaseMap.put(orderId,purchaseMap.get(orderId) + pmsPlanPurchaseRespVO.getAmount()-pmsPlanPurchaseRespVO.getReturnAmount());
            }else {
                purchaseMap.put(orderId,pmsPlanPurchaseRespVO.getAmount()-pmsPlanPurchaseRespVO.getReturnAmount());
            }
        }
//        for (PmsPlanPurchaseRespVO pmsPlanPurchaseRespVO : purchaseList) {
//            String projectId = pmsPlanPurchaseRespVO.getProjectId();
//            if(totalMap.containsKey(projectId)){
//                totalMap.put(projectId,totalMap.get(projectId) + pmsPlanPurchaseRespVO.getPurchaseAmount());
//            }else {
//                totalMap.put(projectId,pmsPlanPurchaseRespVO.getPurchaseAmount());
//            }
//            if(purchaseMap.containsKey(projectId)){
//                purchaseMap.put(projectId,purchaseMap.get(projectId) + pmsPlanPurchaseRespVO.getAmount()-pmsPlanPurchaseRespVO.getReturnAmount());
//            }else {
//                purchaseMap.put(projectId,pmsPlanPurchaseRespVO.getAmount()-pmsPlanPurchaseRespVO.getReturnAmount());
//            }
//        }

        //计算生产进度
        LambdaQueryWrapperX<OrderMaterialRelationDO> relationWrapperX = new LambdaQueryWrapperX<>();
        relationWrapperX.in(OrderMaterialRelationDO::getProjectId,projectIds);
        relationWrapperX.eq(OrderMaterialRelationDO::getMaterialStatus,5);
        List<OrderMaterialRelationDO> relationDOS = relationMapper.selectList(relationWrapperX);
        Map<String,Integer> productionMap = new HashMap<>();
        for (OrderMaterialRelationDO relationDO : relationDOS) {
            if(productionMap.containsKey(relationDO.getOrderId())){
                productionMap.put(relationDO.getOrderId(),productionMap.get(relationDO.getOrderId())+1);
            }else {
                productionMap.put(relationDO.getOrderId(),1);
            }
        }
//        for (OrderMaterialRelationDO relationDO : relationDOS) {
//            if(productionMap.containsKey(relationDO.getProjectId())){
//                productionMap.put(relationDO.getProjectId(),productionMap.get(relationDO.getProjectId())+1);
//            }else {
//                productionMap.put(relationDO.getProjectId(),1);
//            }
//        }


        List<OrderProcessVO> orderList = BeanUtils.toBean(pmsPlanDOS, OrderProcessVO.class, vo -> {
            PmsPlanDO pmsPlanDO1 = planMap.get(vo.getId());
            String orderId = pmsPlanDO1.getProjectOrderId();
            //生产进度
            if (productionMap.containsKey(orderId)) {
                Integer product = productionMap.get(orderId);
                vo.setProductionProcess(100*product / pmsPlanDO1.getQuantity());
            } else {
                vo.setProductionProcess(0);
            }

            //采购进度
            if (totalMap.containsKey(orderId)) {
                vo.setPurchaseProcess(100*purchaseMap.get(orderId) / totalMap.get(orderId));
            } else {
                vo.setPurchaseProcess(0);
            }
            vo.setProductionTime(pmsPlanDO1.getPlanDeliveryTime());
            vo.setPurchaseTime(pmsPlanDO1.getPurchaseCompletionTime());
            if(pmsPlanDO1.getProcessPreparationTime()!=null){
                vo.setSchemeTime(pmsPlanDO1.getProcessPreparationTime());
            }
        });

        return orderList;
    }

    /**
     * 采购进度
     * @param projectIds
     * @return
     */
    public List<PmsPlanPurchaseRespVO> getPurchaseByProjectId(List<String> projectIds){
        List<PlanPurchaseMaterialDO> planPurchaseMaterialList = pmsPlanService.getPlanPurchaseMaterialListByProjectIds(projectIds);
        List<String> pids = planPurchaseMaterialList.stream().map(PlanPurchaseMaterialDO::getId).collect(Collectors.toList());
        Map<String,Integer> amountMap = new HashMap<>();
        Map<String,Integer> returnMap = new HashMap<>();
        //根据采购计划id查询收货详情
        List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetailDTOListTemp = purchaseConsignmentApi.getPurchaseDetailListByPurchaseIds(pids).getCheckedData();
        List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetailDTOList = purchaseConsignmentDetailDTOListTemp.stream().filter((item) -> {
            List<String> types = Arrays.asList("1");
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
        List<PmsPlanPurchaseRespVO> purchaseList = pmsPlanPurchaseRespVOS.stream().filter(item -> item.getPlanType() == 1).collect(Collectors.toList());
        return purchaseList;
    }

    private void validateLeaveExists(String id) {
        if (approvalMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.APPROVAL_NOT_EXISTS);
        }
    }
    private PmsApprovalDO validateLeaveExists2(String id) {
        PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(id);
        if (approvalMapper.selectById(id) == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.APPROVAL_NOT_EXISTS);
        }
        return pmsApprovalDO;
    }

    // ==================== 子表（项目评审） ====================

    @Override
    public List<AssessmentDO> getAssessmentListByProjectId(String projectId) {
        return assessmentMapper.selectListByProjectId(projectId);
    }

    private void createAssessmentList(String projectId, List<AssessmentDO> list) {
        list.forEach(o -> o.setProjectId(projectId));
        assessmentMapper.insertBatch(list);
    }

    private void updateAssessmentList(String projectId, List<AssessmentDO> list) {
        deleteAssessmentByProjectId(projectId);
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createAssessmentList(projectId, list);
    }

    private void deleteAssessmentByProjectId(String projectId) {
        assessmentMapper.deleteByProjectId(projectId);
    }

    // ==================== 子表（项目订单） ====================
    @Override
    public List<PmsOrderDO> getOrderListByProjectId(String projectId) {
        return orderMapper.selectListByProjectId(projectId);
    }

    /**
     * 根据项目id查订单
     * @param projectId
     * @return
     */
    @Override
    public PmsOrderDO getOrderByProjectId(String projectId) {
        return orderMapper.selectOne(PmsOrderDO::getProjectId,projectId);
    }

    @Override
    public List<PmsOrderDO> getOrderListByProjectIds(List<String> projectIds) {
        if(projectIds.size()==0){
            return new ArrayList<PmsOrderDO>();
        }
        return orderMapper.selectListByProjectIds(projectIds);
    }
}
