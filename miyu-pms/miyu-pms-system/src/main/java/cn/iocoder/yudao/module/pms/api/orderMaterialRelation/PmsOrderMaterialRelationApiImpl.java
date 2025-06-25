package cn.iocoder.yudao.module.pms.api.orderMaterialRelation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.*;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PlanItemMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PmsPlanMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import cn.iocoder.yudao.module.pms.service.orderMaterialRelation.OrderMaterialRelationService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.outsourcing.McsOutsourcingPlanDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController
public class PmsOrderMaterialRelationApiImpl implements PmsOrderMaterialRelationApi {
    @Resource
    private OrderMaterialRelationService orderMaterialRelationService;

    @Resource
    private OrderMaterialRelationMapper orderMaterialRelationMapper;

    @Resource
    private PmsPlanMapper pmsPlanMapper;

    @Resource
    private PlanItemMapper planItemMapper;

    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;

    @Resource
    private PmsOrderMapper pmsOrderMapper;

    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;

    @Resource
    private PmsApprovalMapper pmsApprovalMapper;

    @Resource
    private MaterialStockApi materialStockApi;

    @Override
    public CommonResult<String> createOrderMaterial(OrderMaterialRelationSaveReqDTO req) {
        OrderMaterialRelationDO orderMaterialRelationDO = BeanUtils.toBean(req, OrderMaterialRelationDO.class);
        orderMaterialRelationService.insertOrderMaterialRelation(orderMaterialRelationDO);
        return success("ok");
    }

    @Override
    public CommonResult<String> createOrderMaterialBatch(List<OrderMaterialRelationSaveReqDTO> req) {
        List<OrderMaterialRelationDO> list = BeanUtils.toBean(req, OrderMaterialRelationDO.class);
        orderMaterialRelationService.insertOrderMaterialRelationBatch(list);
        return success("ok");
    }

    @Override
    public CommonResult<String> orderMaterialUpdate(OrderMaterialRelationUpdateDTO req) {
        //OrderMaterialRelationDO orderMaterialRelationDO = BeanUtils.toBean(req, OrderMaterialRelationDO.class);
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX();
//        wrapperX.eqIfPresent(OrderMaterialRelationDO::getPlanItemId,req.getPlanItemId())
//                .eq(OrderMaterialRelationDO::getOrderNumber,req.getOrderNumber())
        wrapperX.eq(OrderMaterialRelationDO::getMaterialCode,req.getMaterialCode());
        List<OrderMaterialRelationDO> list = orderMaterialRelationMapper.selectList(wrapperX);
        if(list.size()>0){
            //通过上面的查询一定只会查出一条(变码唯一)
            OrderMaterialRelationDO orderMaterialRelationDO1 = list.get(0);
            orderMaterialRelationService.updateRelation(new OrderMaterialRelationDO().setId(orderMaterialRelationDO1.getId()).setVariableCode(req.getUpdateCode()).setProductCode(req.getProductCode()).setMaterialStatus(req.getMaterialStatus()).setStep(req.getStep()));
        }
        return success("ok");
    }

    @Override
    public CommonResult<String> orderMaterialSlice(@Valid OrderMaterialRelationUpdateDTO req) {
        //根据订单编号查找订单id
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX();
        wrapperX.eq(OrderMaterialRelationDO::getOrderNumber,req.getOrderNumber());
        OrderMaterialRelationDO relationDO1 = orderMaterialRelationMapper.selectList(wrapperX).get(0);
        //根据订单id查找空码并更新
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, relationDO1.getOrderId());
        for (OrderMaterialRelationDO orderMaterialRelationDO : relationDOList) {
            if(ObjectUtil.isEmpty(orderMaterialRelationDO.getMaterialCode())){
                orderMaterialRelationDO.setMaterialCode(req.getMaterialCode()).setMaterialStatus(1).setStep(req.getStep());
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                break;
            }
        }
        //orderMaterialRelationMapper.insert(relationDO);

        return success("ok");
    }

    @Override
    public CommonResult<List<OrderMaterialRelationRespDTO>> getRelationByPlanOrOrder(OrderMaterialRelationUpdateDTO dto) {
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eqIfPresent(OrderMaterialRelationDO::getPlanItemId,dto.getPlanItemId())
                .eqIfPresent(OrderMaterialRelationDO::getOrderId,dto.getOrderId())
                .eqIfPresent(OrderMaterialRelationDO::getMaterialStatus,dto.getMaterialStatus());
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
        return success(BeanUtils.toBean(relationDOList,OrderMaterialRelationRespDTO.class));
    }

    /**
     * 批量查询关系
     *
     * @param req
     * @return
     */
    @Override
    public CommonResult<List<OrderMaterialRelationRespDTO>> getRelationByPlanOrOrderIds(OrderMaterialRelationReqListDTO req) {
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.inIfPresent(OrderMaterialRelationDO::getPlanItemId,req.getPlanItemIds())
                .inIfPresent(OrderMaterialRelationDO::getOrderId,req.getOrderIds())
                .inIfPresent(OrderMaterialRelationDO::getMaterialStatus,req.getMaterialStatusList())
                .inIfPresent(OrderMaterialRelationDO::getProjectId,req.getProjectIds())
                .inIfPresent(OrderMaterialRelationDO::getPlanId,req.getPlanIds())
                .orderByDesc(OrderMaterialRelationDO::getId);
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
        return success(BeanUtils.toBean(relationDOList,OrderMaterialRelationRespDTO.class));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> orderMaterialInit(@Valid OrderMaterialRelationUpdateDTO req) {
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, req.getOrderId());
        for (OrderMaterialRelationDO orderMaterialRelationDO : relationDOList) {
            if(ObjectUtil.isEmpty(orderMaterialRelationDO.getMaterialCode())){
                orderMaterialRelationDO.setMaterialCode(req.getMaterialCode()).setVariableCode(req.getVariableCode()).setMaterialStatus(1);
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                break;
            }
        }
        return success("ok");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> orderMaterialInitBatch(List<OrderMaterialRelationUpdateDTO> list) {
        //key订单id,value List<OrderMaterialRelationUpdateDTO>
        HashMap<String,List<OrderMaterialRelationUpdateDTO>> map = new HashMap<>();
        //把各自订单的数据区分开
        for (OrderMaterialRelationUpdateDTO orderMaterialRelationUpdateDTO : list) {
            if (map.containsKey(orderMaterialRelationUpdateDTO.getOrderId())){
                map.get(orderMaterialRelationUpdateDTO.getOrderId()).add(orderMaterialRelationUpdateDTO);
            }else {
                List<OrderMaterialRelationUpdateDTO> newList = new ArrayList<>();
                newList.add(orderMaterialRelationUpdateDTO);
                map.put(orderMaterialRelationUpdateDTO.getOrderId(),newList);
            }
        }

        for (Map.Entry<String, List<OrderMaterialRelationUpdateDTO>> entry : map.entrySet()) {
            updateRelationInit(entry.getKey(),entry.getValue());
        }
        return success("ok");
    }

    /**
     * 外协入库
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> outSourceUpdate(List<OrderMaterialRelationUpdateDTO> list) {
        //订单集合,用于判断整单外协
        List<String> orderIds = list.stream().map(OrderMaterialRelationUpdateDTO::getOrderId).distinct().collect(Collectors.toList());
        List<PmsOrderDO> pmsOrderList = pmsOrderMapper.selectBatchIds(orderIds);
        Map<String, PmsOrderDO> orderDOMap = CollectionUtils.convertMap(pmsOrderList, PmsOrderDO::getId);

        //key子计划id,value List<OrderMaterialRelationUpdateDTO>
        HashMap<String,List<OrderMaterialRelationUpdateDTO>> map = new HashMap<>();
        //区分到子计划，防止更新状态时出错
        for (OrderMaterialRelationUpdateDTO orderMaterialRelationUpdateDTO : list) {
            if (map.containsKey(orderMaterialRelationUpdateDTO.getPlanItemId())){
                map.get(orderMaterialRelationUpdateDTO.getPlanItemId()).add(orderMaterialRelationUpdateDTO);
            }else {
                List<OrderMaterialRelationUpdateDTO> newList = new ArrayList<>();
                newList.add(orderMaterialRelationUpdateDTO);
                map.put(orderMaterialRelationUpdateDTO.getPlanItemId(),newList);
            }
        }
        //更新
        for (Map.Entry<String, List<OrderMaterialRelationUpdateDTO>> entry : map.entrySet()) {
            //子计划的订单id一定是相同的
            PmsOrderDO orderDO = orderDOMap.get(entry.getValue().get(0).getOrderId());
            String s = updateRelationOutsource(entry.getKey(), entry.getValue(), orderDO.getOutsource());
        }
        return success("ok");
    }

    /**
     * 外协选料
     * 确定物料被选中后，通知生产,并将物料状态改为外协中
     * @param dto
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> outSourceSelectMaterial(OutsourcingMaterialSelectDTO dto) {
        if(StrUtil.isEmpty(dto.getMaterialCodeList())){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.MATERIAL_UNSELECTED);
        }
        String[] materialCodeList = dto.getMaterialCodeList().split(",");
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
//        wrapperX.eqIfPresent(OrderMaterialRelationDO::getOrderId,dto.getOrderId())
//                .eq(OrderMaterialRelationDO::getPlanItemId,dto.getPlanItemId())
        wrapperX.in(OrderMaterialRelationDO::getMaterialCode,materialCodeList);
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
        for (OrderMaterialRelationDO relationDO : relationDOList) {
            relationDO.setMaterialStatus(4);
            relationDO.setContractId(dto.getContractId());
            relationDO.setAidMill(dto.getAidMill());
        }
        orderMaterialRelationMapper.updateBatch(relationDOList);
        //外协开始通知mcc,订单,子计划可能不唯一,得分开通知,按子计划分
        List<String> planItemList = relationDOList.stream().map(OrderMaterialRelationDO::getPlanItemId).distinct().collect(Collectors.toList());
        McsOutsourcingPlanDTO mcsOutsourcingPlanDTO = BeanUtils.toBean(dto, McsOutsourcingPlanDTO.class);
        mcsOutsourcingPlanDTO.setOutsourcingId(dto.getContractId());
        for (String planItemId : planItemList) {
            //物料码集合转字符串
            List<OrderMaterialRelationDO> collect = relationDOList.stream().filter((item) -> {
                return planItemId.equals(item.getPlanItemId());
            }).collect(Collectors.toList());

            String materialStr = collect.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.joining(","));
            //相同子计划工序一致,开始传开始外协的工序，结束传结束外协的工序
            OrderMaterialRelationDO relationDO = collect.get(0);
            String step= relationDO.getStep();

            PmsOrderDO orderDO = pmsOrderMapper.selectById(relationDO.getOrderId());
            //非整单外协才通知生产
            if(orderDO.getOutsource()!=1){
                mcsOutsourcingPlanDTO.setOrderNumber(relationDO.getOrderNumber());
                mcsOutsourcingPlanDTO.setMaterialCodeList(materialStr);
                mcsOutsourcingPlanDTO.setProcessNumber(step);
                Object checkedData = mcsManufacturingControlApi.outsourcingStart(mcsOutsourcingPlanDTO).getCheckedData();
            }
        }

        return success("ok");
    }

    /**
     * 通过物码集合查询关系
     * @param codes
     * @return
     */
    @Override
    public CommonResult<List<OrderMaterialRelationRespDTO>> selectByMaterialCodes(Collection<String> codes) {
        if(CollectionUtils.isAnyEmpty(codes)) {
            return success(Collections.emptyList());
        }
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapper = new LambdaQueryWrapperX<>();
        wrapper.in(OrderMaterialRelationDO::getMaterialCode,codes);
        wrapper.orderByAsc(OrderMaterialRelationDO::getId);
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapper);
        List<String> projectIds = relationDOList.stream().map(OrderMaterialRelationDO::getProjectId).distinct().collect(Collectors.toList());
        if(CollectionUtils.isAnyEmpty(projectIds)) {
            return success(Collections.emptyList());
        }
        List<PmsApprovalDO> pmsApprovalList = pmsApprovalMapper.selectBatchIds(projectIds);
        Map<String, PmsApprovalDO> map = CollectionUtils.convertMap(pmsApprovalList, PmsApprovalDO::getId);

        return success(BeanUtils.toBean(relationDOList,OrderMaterialRelationRespDTO.class,vo->{
            if(map.containsKey(vo.getProjectId())){
                vo.setProjectCode(map.get(vo.getProjectId()).getProjectCode());
                vo.setProjectName(map.get(vo.getProjectId()).getProjectName());
            }
        }));
    }

    /**
     * 重置关系到未入库状态
     * @param codes
     * @return
     */
    @Override
    public CommonResult<String> resetRelations(Collection<String> codes) {
        if(CollectionUtils.isAnyEmpty(codes)) {
            return success("false");
        }
        LambdaUpdateWrapper<OrderMaterialRelationDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderMaterialRelationDO::getPlanId,null);
        updateWrapper.set(OrderMaterialRelationDO::getPlanItemId,null);
        updateWrapper.set(OrderMaterialRelationDO::getMaterialCode,null);
        updateWrapper.set(OrderMaterialRelationDO::getVariableCode,null);
        updateWrapper.set(OrderMaterialRelationDO::getContractId,null);
        updateWrapper.set(OrderMaterialRelationDO::getAidMill,null);
        updateWrapper.set(OrderMaterialRelationDO::getProductCode,null);
        updateWrapper.set(OrderMaterialRelationDO::getPlanType,null);
        updateWrapper.set(OrderMaterialRelationDO::getOrderNumber,null);
        updateWrapper.set(OrderMaterialRelationDO::getStep,null);
        updateWrapper.set(OrderMaterialRelationDO::getMaterialTypeId,null);
        updateWrapper.set(OrderMaterialRelationDO::getMaterialStatus,3);
        updateWrapper.in(OrderMaterialRelationDO::getMaterialCode,codes);
        orderMaterialRelationMapper.update(updateWrapper);
        return success("ok");
    }

    /**
     * 报废物料
     * @param codes
     * @return
     */
    @Override
    public CommonResult<String> scrapMaterialCodes(Collection<String> codes) {
        if(CollectionUtils.isAnyEmpty(codes)){
            return success("false");
        }
        LambdaUpdateWrapper<OrderMaterialRelationDO> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.set(OrderMaterialRelationDO::getMaterialStatus,7);
        updateWrapper.in(OrderMaterialRelationDO::getMaterialCode,codes);
        return success("ok");
    }

    /**
     * 生产选好料后通知项目选了哪些料
     * @param orderNumber       订单编号
     * @param materialCodeList  物料码
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> orderMaterialFill(String orderNumber, List<String> materialCodeList) {
        LambdaQueryWrapperX<PlanItemDO> planItemWrapper = new LambdaQueryWrapperX<>();
        planItemWrapper.eq(PlanItemDO::getOrderNumber, orderNumber);
        PlanItemDO planItemDO = planItemMapper.selectOne(planItemWrapper);
        PmsPlanDO pmsPlanDO = pmsPlanMapper.selectById(planItemDO.getProjectPlanId());

        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(pmsPlanDO.getProcessScheme()).getCheckedData();
        ProcedureRespDTO procedureRespDTO = procedureList.get(0);
        String step = procedureRespDTO.getProcedureNum();

        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, planItemDO.getProjectOrderId());
        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCodes(materialCodeList).getCheckedData();
        Map<String, MaterialStockRespDTO> stringMaterialStockRespDTOMap = CollectionUtils.convertMap(materialStockList, MaterialStockRespDTO::getBarCode);
        //存储空关系
        for (OrderMaterialRelationDO orderMaterialRelationDO : relationDOList) {
            //没有物料码就结束循环
            if(materialCodeList.size()==0){
                break;
            }
            //遍历关系找到物料码为空的
            if(ObjectUtil.isEmpty(orderMaterialRelationDO.getMaterialCode())){
                String materialCode = materialCodeList.get(0);
                if(stringMaterialStockRespDTOMap.containsKey(materialCode)){
                    MaterialStockRespDTO dto = stringMaterialStockRespDTOMap.get(materialCode);
                    orderMaterialRelationDO.setMaterialTypeId(dto.getMaterialConfigId());
                }
                orderMaterialRelationDO.setMaterialCode(materialCode).setVariableCode(materialCode).setPrepare(true).setPlanId(pmsPlanDO.getId()).setStep(step).setMaterialStatus(2).setPlanItemId(planItemDO.getId());
                orderMaterialRelationDO.setOrderNumber(orderNumber);
                //和之前不一样，填码说明加工了
                orderMaterialRelationDO.setPlanType(1);
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                materialCodeList.remove(0);
            }
        }
        return success("ok");
    }

    /**
     * 订单物料关系更新,只用于刚入库
     *
     * @param orderId
     * @param list
     */
    public void updateRelationInit(String orderId,List<OrderMaterialRelationUpdateDTO> list){
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, orderId);
        //存储空关系
        for (OrderMaterialRelationDO orderMaterialRelationDO : relationDOList) {
            //没有物料码就结束循环
            if(list.size()==0){
                break;
            }
            //遍历关系找到物料码为空的
            if(ObjectUtil.isEmpty(orderMaterialRelationDO.getMaterialCode())){
                OrderMaterialRelationUpdateDTO update = list.get(0);
                orderMaterialRelationDO.setMaterialCode(update.getMaterialCode()).setVariableCode(update.getVariableCode()).setMaterialStatus(1);
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                list.remove(0);
            }
        }
    }
    @Transactional(rollbackFor = Exception.class)
    public String updateRelationOutsource(String planItemId,List<OrderMaterialRelationUpdateDTO> list,Integer outsource){

        //外协入库用的是子计划id，不是订单id
        //List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getPlanItemId, planItemId);
        if(list.size()==0){
            return "ok";
        }

        //查需要更改的关系条码
        List<String> updaterelationDOList = list.stream().map(OrderMaterialRelationUpdateDTO::getMaterialCode).collect(Collectors.toList());
        //需要更改的码的字符串
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(OrderMaterialRelationDO::getMaterialCode,updaterelationDOList);
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
        //能查到的更改关系条码
        String materialCodeStr = relationDOList.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.joining(","));

        //查工序
        PmsPlanDO pmsPlanDO = pmsPlanMapper.selectById(relationDOList.get(0).getPlanId());
        String processVersionId = pmsPlanDO.getProcessVersionId();
        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(processVersionId).getCheckedData();

        //整单外协
        if(outsource==1){
            for (OrderMaterialRelationDO relationDO : relationDOList) {
                relationDO.setMaterialStatus(5).setStep("-1");
            }
            orderMaterialRelationMapper.updateBatch(relationDOList);
//            OrderMaterialRelationDO relationDO = relationDOList.get(0);
            //通知工序外协完成,继续加工,mcs,整单外协不用通知
//            McsOutsourcingPlanDTO dto = new McsOutsourcingPlanDTO();
//            dto.setOutsourcingId(relationDO.getContractId()).setOrderNumber(relationDO.getOrderNumber()).setMaterialCodeList(materialCodeStr).setProcessNumber(String.valueOf(procedureList.size())).setAidMill(relationDO.getAidMill());
//            Object checkedData = mcsManufacturingControlApi.outsourcingFinish(dto).getCheckedData();
        }else {
            for (OrderMaterialRelationDO relationDO : relationDOList) {
                //根据工序判断,物料状态是待分配还是完成加工
                int size = procedureList.size();
                //委派时的工序
                String step = relationDO.getStep();

                //预设状态，工序完成,物料状态为加工完成,如果之后都是外协,此状态成立。
                relationDO.setStep("-1");
                relationDO.setMaterialStatus(5);

                //获得委派时的工序
                ProcedureRespDTO procedure = new ProcedureRespDTO();
                for (ProcedureRespDTO procedureRespDTO : procedureList) {
                    if(procedureRespDTO.getProcedureNum().equals(step)){
                        procedure = procedureRespDTO;
                        break;
                    }
                }
                //获得委派时的索引
                int index = procedureList.indexOf(procedure);
                //工序外协结束时的索引
                int outsourcefinish = index;

                //判断下一道工序是不是外协
                for (int i = index+1; i < size; i++) {
                    ProcedureRespDTO procedureRespDTO = procedureList.get(i);
                    //下一道工序不是外协
                    if(procedureRespDTO.getIsOut().equals("0")){
                        //设置下一道工序
                        relationDO.setStep(procedureRespDTO.getProcedureNum());
                        //物料状态为加工中
                        relationDO.setMaterialStatus(2);
                        break;
                    }
                    //到这里说明这道工序是外协
                    outsourcefinish = i;
                }

                //通知工序外协完成,继续加工,mcs
                McsOutsourcingPlanDTO dto = new McsOutsourcingPlanDTO();
                dto.setOutsourcingId(relationDO.getContractId()).setOrderNumber(relationDO.getOrderNumber()).setMaterialCodeList(relationDO.getMaterialCode()).setProcessNumber(procedureList.get(outsourcefinish).getProcedureNum()).setAidMill(relationDO.getAidMill());
                Object checkedData = mcsManufacturingControlApi.outsourcingFinish(dto).getCheckedData();
            }
            orderMaterialRelationMapper.updateBatch(relationDOList);
//            OrderMaterialRelationDO relationDO = relationDOList.get(0);
            //通知工序外协完成,继续加工,mcs
//            McsOutsourcingPlanDTO dto = new McsOutsourcingPlanDTO();
//            dto.setOutsourcingId(relationDO.getContractId()).setOrderNumber(relationDO.getOrderNumber()).setMaterialCodeList(materialCodeStr).setProcessNumber(procedureList.get(outsourcefinish).getProcedureNum()).setAidMill(relationDO.getAidMill());
//            Object checkedData = mcsManufacturingControlApi.outsourcingFinish(dto).getCheckedData();
        }
        return "ok";
    }
//    @Transactional(rollbackFor = Exception.class)
//    public String updateRelationOutsource(String planItemId,List<OrderMaterialRelationUpdateDTO> list,Integer outsource){
//
//        //外协入库用的是子计划id，不是订单id
//        //List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getPlanItemId, planItemId);
//
//        //查需要更改的关系条码
//        List<String> updaterelationDOList = list.stream().map(OrderMaterialRelationUpdateDTO::getMaterialCode).collect(Collectors.toList());
//        //需要更改的码的字符串
//        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
//        wrapperX.in(OrderMaterialRelationDO::getMaterialCode,updaterelationDOList);
//        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
//        //能查到的更改关系条码
//        String materialCodeStr = relationDOList.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.joining(","));
//
//        //查工序
//        PmsPlanDO pmsPlanDO = pmsPlanMapper.selectById(relationDOList.get(0).getPlanId());
//        String processVersionId = pmsPlanDO.getProcessVersionId();
//        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(processVersionId).getCheckedData();
//
//        //整单外协
//        if(outsource==1){
//            for (OrderMaterialRelationDO relationDO : relationDOList) {
//                relationDO.setMaterialStatus(5).setStep("-1");
//            }
//            orderMaterialRelationMapper.updateBatch(relationDOList);
////            OrderMaterialRelationDO relationDO = relationDOList.get(0);
//            //通知工序外协完成,继续加工,mcs,整单外协不用通知
////            McsOutsourcingPlanDTO dto = new McsOutsourcingPlanDTO();
////            dto.setOutsourcingId(relationDO.getContractId()).setOrderNumber(relationDO.getOrderNumber()).setMaterialCodeList(materialCodeStr).setProcessNumber(String.valueOf(procedureList.size())).setAidMill(relationDO.getAidMill());
////            Object checkedData = mcsManufacturingControlApi.outsourcingFinish(dto).getCheckedData();
//        }else {
//            //工序外协结束时的索引
//            int outsourcefinish = 0;
//            for (OrderMaterialRelationDO relationDO : relationDOList) {
//                //根据工序判断,物料状态是待分配还是完成加工
//                int size = procedureList.size();
//                //委派时的工序
//                String step = relationDO.getStep();
//
//                //预设状态，工序完成,物料状态为加工完成,如果之后都是外协,此状态成立。
//                relationDO.setStep("-1");
//                relationDO.setMaterialStatus(5);
//
//                //获得委派时的工序
//                ProcedureRespDTO procedure = new ProcedureRespDTO();
//                for (ProcedureRespDTO procedureRespDTO : procedureList) {
//                    if(procedureRespDTO.getProcedureNum().equals(step)){
//                        procedure = procedureRespDTO;
//                        break;
//                    }
//                }
//                //获得委派时的索引
//                int index = procedureList.indexOf(procedure);
//
//                //判断下一道工序是不是外协
//                for (int i = index+1; i < size; i++) {
//                    ProcedureRespDTO procedureRespDTO = procedureList.get(i);
//                    //下一道工序不是外协
//                    if(procedureRespDTO.getIsOut().equals("0")){
//                        //设置下一道工序
//                        relationDO.setStep(procedureRespDTO.getProcedureNum());
//                        //物料状态为加工中
//                        relationDO.setMaterialStatus(2);
//                        break;
//                    }
//                    //到这里说明这道工序是外协
//                    outsourcefinish = i;
//                }
//            }
//            orderMaterialRelationMapper.updateBatch(relationDOList);
//            OrderMaterialRelationDO relationDO = relationDOList.get(0);
//            //通知工序外协完成,继续加工,mcs
//            McsOutsourcingPlanDTO dto = new McsOutsourcingPlanDTO();
//            dto.setOutsourcingId(relationDO.getContractId()).setOrderNumber(relationDO.getOrderNumber()).setMaterialCodeList(materialCodeStr).setProcessNumber(procedureList.get(outsourcefinish).getProcedureNum()).setAidMill(relationDO.getAidMill());
//            Object checkedData = mcsManufacturingControlApi.outsourcingFinish(dto).getCheckedData();
//        }
//        Boolean finishWork = isFinishWork(relationDOList.get(0).getOrderId());
//        return "ok";
//    }

    /**
     * 判断加工是否完成
     * @param orderId
     * @return
     */
    public Boolean isFinishWork(String orderId){
        LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.eq(OrderMaterialRelationDO::getPlanItemId,orderId);
        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(wrapperX);
        for (OrderMaterialRelationDO relationDO : relationDOList) {
            //物料状态5为加工完成
            if(relationDO.getMaterialStatus()!=5){
                return false;
            }
        }
        //TODO 到这说明加工完成了,得改变项目和订单的状态
        return true;
    }


}
