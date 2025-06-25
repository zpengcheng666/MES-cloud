package cn.iocoder.yudao.module.pms.service.materialPurchasePlan;

import cn.hutool.core.bean.BeanUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.*;
import cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder.PlanPurchaseOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder.PlanPurchaseOrderItemDO;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.*;
import cn.iocoder.yudao.module.pms.dal.mysql.planPurchaseOrder.PlanPurchaseOrderItemMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.planPurchaseOrder.PlanPurchaseOrderMapper;
import cn.iocoder.yudao.module.system.api.dept.DeptApi;
import cn.iocoder.yudao.module.system.api.dept.dto.DeptRespDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.ppm.api.purchaseRequirement.RequirementApi;
import com.miyu.module.ppm.api.purchaseRequirement.dto.PurchaseRequirementDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.PLAN_ITEM_NOT_EXISTS;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.START_PURCHASE_SAVE_FAILSE;

@Service
@Validated
public class MaterialPurchasePlanServiceImpl implements MaterialPurchasePlanService {

    @Resource
    private PlanPurchaseOrderMapper planPurchaseOrderMapper;

    @Resource
    private PlanPurchaseOrderItemMapper planPurchaseOrderItemMapper;

    @Resource
    private AdminUserApi adminUserApi;

    @Resource
    private DeptApi deptApi;

    @Resource
    private RequirementApi requirementApi;

    @Resource
    private PlanDemandDeviceMapper planDemandDeviceMapper;

    @Resource
    private PlanDemandMaterialMapper planDemandMaterialMapper;

    @Resource
    private PlanCombinationMapper planCombinationMapper;

    @Resource
    private PlanDemandCutterMapper planDemandCutterMapper;

    @Resource
    private PlanDemandHiltMapper planDemandHiltMapper;

    @Resource
    private PlanPurchaseMaterialMapper planPurchaseMaterialMapper;
    /**
     * 发起设备采购
     * 创建一个源单
     * 以及关联的信息,记个id就行
     * 把采购的id标记为已采购
     * @param type
     * @param userId
     * @param list
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startDevicePurchase(Integer type,Long userId, List<PlanDemandDeviceDO> list) {
        //存入源单
        PlanPurchaseOrderDO orderDO = new PlanPurchaseOrderDO();
        orderDO.setType(type);
        planPurchaseOrderMapper.insert(orderDO);

        //源单子信息
        List<PlanPurchaseOrderItemDO> orderItemDOList = new ArrayList<>();
        //合并数据
        Map<String,PlanDemandDeviceDO> resourceMap = new HashMap<>();
        for (PlanDemandDeviceDO item : list) {
            PlanPurchaseOrderItemDO orderItemDO = new PlanPurchaseOrderItemDO();
            orderItemDO.setProjectCode(item.getProjectCode()).setResourceId(item.getId()).setProjectPlanPurchaseOrderId(orderDO.getId());
            orderItemDOList.add(orderItemDO);
            //将采购标记变为1
            item.setPurchaseMark(1);
            //将相同类型的数据合并,通过资源id
            if(resourceMap.containsKey(item.getResourcesTypeId())){
                PlanDemandDeviceDO planDO = resourceMap.get(item.getResourcesTypeId());
                planDO.setPurchaseAmount(planDO.getPurchaseAmount()+item.getPurchaseAmount());
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }else {
                PlanDemandDeviceDO planDO = new PlanDemandDeviceDO();
                BeanUtil.copyProperties(item,planDO);
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }
        }
        //更新资源列表
        planDemandDeviceMapper.updateBatch(list);
        //存入源单子信息
        planPurchaseOrderItemMapper.insertBatch(orderItemDOList);

        //向采购发起采购单
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        PurchaseRequirementDTO requirementDTO = new PurchaseRequirementDTO();
        requirementDTO.setApplicant(userId.toString()).setApplicationDepartment(user.getDeptId().toString()).setApplicationDate(LocalDateTime.now()).setApplicationReason("物资采购").setType(2);

        //合并数据
        List<PlanDemandDeviceDO> mergeList = new ArrayList<>();
        for (Map.Entry<String, PlanDemandDeviceDO> entry : resourceMap.entrySet()) {
            mergeList.add(entry.getValue());
        }
        //也使用合并数据
        Map<String, PlanDemandDeviceDO> map = CollectionUtils.convertMap(mergeList, PlanDemandDeviceDO::getId);
        List<PurchaseRequirementDTO.Detail> details = BeanUtils.toBean(mergeList, PurchaseRequirementDTO.Detail.class, (vo) -> {
            PlanDemandDeviceDO planDemandDeviceDO = map.get(vo.getId());
            vo.setRequiredDate(planDemandDeviceDO.getPurchaseTime())
                    .setEstimatedPrice(planDemandDeviceDO.getPredictPrice())
                    .setRequiredQuantity(new BigDecimal(planDemandDeviceDO.getPurchaseAmount()))
                    .setRequiredMaterial(planDemandDeviceDO.getResourcesTypeId())
                    .setSourceType(2)
//                    .setSourceId(orderDO.getId())
                    .setSourceId(vo.getId())
                    .setId(null);
        });
        requirementDTO.setDetails(details);
        requirementApi.createPurchaseRequirement(requirementDTO).getCheckedData();

        return true;
    }

    /**
     * 工装采购，和物料大体一样
     * @param type
     * @param userId
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startMaterialPurchase(Integer type, Long userId, List<PlanDemandMaterialDO> list) {
        //存入源单
        PlanPurchaseOrderDO orderDO = new PlanPurchaseOrderDO();
        orderDO.setType(type);
        planPurchaseOrderMapper.insert(orderDO);

        List<PlanPurchaseOrderItemDO> orderItemDOList = new ArrayList<>();
        //合并数据
        Map<String,PlanDemandMaterialDO> resourceMap = new HashMap<>();
        for (PlanDemandMaterialDO item : list) {
            PlanPurchaseOrderItemDO orderItemDO = new PlanPurchaseOrderItemDO();
            orderItemDO.setProjectCode(item.getProjectCode()).setResourceId(item.getId()).setProjectPlanPurchaseOrderId(orderDO.getId());
            orderItemDOList.add(orderItemDO);
            //将采购标记变为1
            item.setPurchaseMark(1);

            //将相同类型的数据合并,通过资源id
            if(resourceMap.containsKey(item.getResourcesTypeId())){
                PlanDemandMaterialDO planDO = resourceMap.get(item.getResourcesTypeId());
                planDO.setPurchaseAmount(planDO.getPurchaseAmount()+item.getPurchaseAmount());
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }else {
                PlanDemandMaterialDO planDO = new PlanDemandMaterialDO();
                BeanUtil.copyProperties(item,planDO);
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }
        }
        //更新资源列表
        planDemandMaterialMapper.updateBatch(list);
        //存入源单子信息
        planPurchaseOrderItemMapper.insertBatch(orderItemDOList);

        //向采购发起采购单
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        PurchaseRequirementDTO requirementDTO = new PurchaseRequirementDTO();
        requirementDTO.setApplicant(userId.toString()).setApplicationDepartment(user.getDeptId().toString()).setApplicationDate(LocalDateTime.now()).setApplicationReason("物资采购").setType(4);

        //合并数据
        List<PlanDemandMaterialDO> mergeList = new ArrayList<>();
        for (Map.Entry<String, PlanDemandMaterialDO> entry : resourceMap.entrySet()) {
            mergeList.add(entry.getValue());
        }
        //也使用合并数据
        Map<String, PlanDemandMaterialDO> map = CollectionUtils.convertMap(mergeList, PlanDemandMaterialDO::getId);
        List<PurchaseRequirementDTO.Detail> details = BeanUtils.toBean(mergeList, PurchaseRequirementDTO.Detail.class, (vo) -> {
            PlanDemandMaterialDO planDemandMaterialDO = map.get(vo.getId());
            vo.setRequiredDate(planDemandMaterialDO.getPurchaseTime())
                    .setEstimatedPrice(planDemandMaterialDO.getPredictPrice())
                    .setRequiredQuantity(new BigDecimal(planDemandMaterialDO.getPurchaseAmount()))
                    .setRequiredMaterial(planDemandMaterialDO.getResourcesTypeId())
                    .setSourceType(2)
//                    .setSourceId(orderDO.getId())
                    .setSourceId(vo.getId())
                    .setId(null);
        });
        requirementDTO.setDetails(details);
        requirementApi.createPurchaseRequirement(requirementDTO).getCheckedData();
        return true;
    }

    /**
     * 刀具采购
     * @param type
     * @param userId
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startCombinationPurchase(Integer type, Long userId, List<PlanCombinationDO> list) {
        //存入源单
        PlanPurchaseOrderDO orderDO = new PlanPurchaseOrderDO();
        orderDO.setType(type);
        planPurchaseOrderMapper.insert(orderDO);

        List<PlanPurchaseOrderItemDO> orderItemDOList = new ArrayList<>();
        //合并数据
        Map<String,PlanCombinationDO> resourceMap = new HashMap<>();

        for (PlanCombinationDO item : list) {
            PlanPurchaseOrderItemDO orderItemDO = new PlanPurchaseOrderItemDO();
            orderItemDO.setProjectCode(item.getProjectCode()).setResourceId(item.getId()).setProjectPlanPurchaseOrderId(orderDO.getId());
            orderItemDOList.add(orderItemDO);
            //将采购标记变为1
            item.setPurchaseMark(1);

            //将相同类型的数据合并,通过资源id
            if(resourceMap.containsKey(item.getResourcesTypeId())){
                PlanCombinationDO planDO = resourceMap.get(item.getResourcesTypeId());
                planDO.setPurchaseAmount(planDO.getPurchaseAmount()+item.getPurchaseAmount());
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }else {
                PlanCombinationDO planDO = new PlanCombinationDO();
                BeanUtil.copyProperties(item,planDO);
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }
        }
        //更新资源列表
        planCombinationMapper.updateBatch(list);
        //存入源单子信息
        planPurchaseOrderItemMapper.insertBatch(orderItemDOList);

        //向采购发起采购单
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        PurchaseRequirementDTO requirementDTO = new PurchaseRequirementDTO();
        requirementDTO.setApplicant(userId.toString()).setApplicationDepartment(user.getDeptId().toString()).setApplicationDate(LocalDateTime.now()).setApplicationReason("物资采购").setType(5);

        //合并数据
        List<PlanCombinationDO> mergeList = new ArrayList<>();
        for (Map.Entry<String, PlanCombinationDO> entry : resourceMap.entrySet()) {
            mergeList.add(entry.getValue());
        }
        //也使用合并数据
        Map<String, PlanCombinationDO> map = CollectionUtils.convertMap(mergeList, PlanCombinationDO::getId);
        List<PurchaseRequirementDTO.Detail> details = BeanUtils.toBean(mergeList, PurchaseRequirementDTO.Detail.class, (vo) -> {
            PlanCombinationDO item = map.get(vo.getId());
            vo.setRequiredDate(item.getPurchaseTime())
                    .setEstimatedPrice(item.getPredictPrice())
                    .setRequiredQuantity(new BigDecimal(item.getPurchaseAmount()))
                    .setRequiredMaterial(item.getResourcesTypeId())
                    .setSourceType(2)
//                    .setSourceId(orderDO.getId())
                    .setSourceId(vo.getId())
                    .setId(null);

        });

        requirementDTO.setDetails(details);
        requirementApi.createPurchaseRequirement(requirementDTO).getCheckedData();
        return true;
    }

    /**
     * 刀具采购(分)
     * @param type
     * @param userId
     * @param list
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startCutterPurchase(Integer type, Long userId, List<PlanDemandCutterDO> list) {
        //存入源单
        PlanPurchaseOrderDO orderDO = new PlanPurchaseOrderDO();
        orderDO.setType(type);
        planPurchaseOrderMapper.insert(orderDO);

        List<PlanPurchaseOrderItemDO> orderItemDOList = new ArrayList<>();
        //合并数据
        Map<String,PlanDemandCutterDO> resourceMap = new HashMap<>();
        for (PlanDemandCutterDO item : list) {
            PlanPurchaseOrderItemDO orderItemDO = new PlanPurchaseOrderItemDO();
            orderItemDO.setProjectCode(item.getProjectCode()).setResourceId(item.getId()).setProjectPlanPurchaseOrderId(orderDO.getId());
            orderItemDOList.add(orderItemDO);
            //将采购标记变为1
            item.setPurchaseMark(1);
            //将相同类型的数据合并,通过资源id
            if(resourceMap.containsKey(item.getResourcesTypeId())){
                PlanDemandCutterDO planDO = resourceMap.get(item.getResourcesTypeId());
                planDO.setPurchaseAmount(planDO.getPurchaseAmount()+item.getPurchaseAmount());
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }else {
                PlanDemandCutterDO planDO = new PlanDemandCutterDO();
                BeanUtil.copyProperties(item,planDO);
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }
        }
        //更新资源列表
        planDemandCutterMapper.updateBatch(list);
        //存入源单子信息
        planPurchaseOrderItemMapper.insertBatch(orderItemDOList);

        //向采购发起采购单
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        PurchaseRequirementDTO requirementDTO = new PurchaseRequirementDTO();
        requirementDTO.setApplicant(userId.toString()).setApplicationDepartment(user.getDeptId().toString()).setApplicationDate(LocalDateTime.now()).setApplicationReason("物资采购").setType(5);

        //合并数据
        List<PlanDemandCutterDO> mergeList = new ArrayList<>();
        for (Map.Entry<String, PlanDemandCutterDO> entry : resourceMap.entrySet()) {
            mergeList.add(entry.getValue());
        }
        //也使用合并数据
        Map<String, PlanDemandCutterDO> map = CollectionUtils.convertMap(mergeList, PlanDemandCutterDO::getId);
        List<PurchaseRequirementDTO.Detail> details = BeanUtils.toBean(mergeList, PurchaseRequirementDTO.Detail.class, (vo) -> {
            PlanDemandCutterDO item = map.get(vo.getId());
            vo.setRequiredDate(item.getPurchaseTime())
                    .setEstimatedPrice(item.getPredictPrice())
                    .setRequiredQuantity(new BigDecimal(item.getPurchaseAmount()))
                    .setRequiredMaterial(item.getResourcesTypeId())
                    .setSourceType(2)
//                    .setSourceId(orderDO.getId())
                    .setSourceId(vo.getId())
                    .setId(null);
        });
        requirementDTO.setDetails(details);
        requirementApi.createPurchaseRequirement(requirementDTO).getCheckedData();
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startHiltPurchase(Integer type, Long userId, List<PlanDemandHiltDO> list) {
        //存入源单
        PlanPurchaseOrderDO orderDO = new PlanPurchaseOrderDO();
        orderDO.setType(type);
        planPurchaseOrderMapper.insert(orderDO);

        List<PlanPurchaseOrderItemDO> orderItemDOList = new ArrayList<>();
        //合并数据
        Map<String,PlanDemandHiltDO> resourceMap = new HashMap<>();
        for (PlanDemandHiltDO item : list) {
            PlanPurchaseOrderItemDO orderItemDO = new PlanPurchaseOrderItemDO();
            orderItemDO.setProjectCode(item.getProjectCode()).setResourceId(item.getId()).setProjectPlanPurchaseOrderId(orderDO.getId());
            orderItemDOList.add(orderItemDO);
            //将采购标记变为1
            item.setPurchaseMark(1);
            //将相同类型的数据合并,通过资源id
            if(resourceMap.containsKey(item.getResourcesTypeId())){
                PlanDemandHiltDO planDO = resourceMap.get(item.getResourcesTypeId());
                planDO.setPurchaseAmount(planDO.getPurchaseAmount()+item.getPurchaseAmount());
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }else {
                PlanDemandHiltDO planDO = new PlanDemandHiltDO();
                BeanUtil.copyProperties(item,planDO);
                resourceMap.put(item.getResourcesTypeId(),planDO);
            }
        }
        //更新资源列表
        planDemandHiltMapper.updateBatch(list);
        //存入源单子信息
        planPurchaseOrderItemMapper.insertBatch(orderItemDOList);

        //向采购发起采购单
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        PurchaseRequirementDTO requirementDTO = new PurchaseRequirementDTO();
        requirementDTO.setApplicant(userId.toString()).setApplicationDepartment(user.getDeptId().toString()).setApplicationDate(LocalDateTime.now()).setApplicationReason("物资采购").setType(5);

        //合并数据
        List<PlanDemandHiltDO> mergeList = new ArrayList<>();
        for (Map.Entry<String, PlanDemandHiltDO> entry : resourceMap.entrySet()) {
            mergeList.add(entry.getValue());
        }
        //也使用合并数据
        Map<String, PlanDemandHiltDO> map = CollectionUtils.convertMap(mergeList, PlanDemandHiltDO::getId);
        List<PurchaseRequirementDTO.Detail> details = BeanUtils.toBean(list, PurchaseRequirementDTO.Detail.class, (vo) -> {
            PlanDemandHiltDO item = map.get(vo.getId());
            vo.setRequiredDate(item.getPurchaseTime())
                    .setEstimatedPrice(item.getPredictPrice())
                    .setRequiredQuantity(new BigDecimal(item.getPurchaseAmount()))
                    .setRequiredMaterial(item.getResourcesTypeId())
                    .setSourceType(2)
//                    .setSourceId(orderDO.getId())
                    .setSourceId(vo.getId())
                    .setId(null);
        });
        requirementDTO.setDetails(details);
        requirementApi.createPurchaseRequirement(requirementDTO).getCheckedData();
        return true;
    }

    /**
     * 这个是物料采购,和其他的不一样
     * 现在要多发送字段子计划id,计划类型,订单id,项目id,计划id,
     * @param type
     * @param userId
     * @param list
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startMiterialConfigPurchase(Integer type, Long userId, List<PlanPurchaseMaterialDO> list) {
        //存入源单
        PlanPurchaseOrderDO orderDO = new PlanPurchaseOrderDO();
        orderDO.setType(type);
        planPurchaseOrderMapper.insert(orderDO);

        //源单数据详情，是我这边的记录
        List<PlanPurchaseOrderItemDO> orderItemDOList = new ArrayList<>();
        //合并数据,之前的数据合并了，但是因为要区分订单,合并其实不太合理,我给他分开

//        Map<String,PlanPurchaseMaterialDO> resourceMap = new HashMap<>();
        for (PlanPurchaseMaterialDO item : list) {
            PlanPurchaseOrderItemDO orderItemDO = new PlanPurchaseOrderItemDO();
            orderItemDO.setProjectCode(item.getProjectCode()).setResourceId(item.getId()).setProjectPlanPurchaseOrderId(orderDO.getId());
            orderItemDOList.add(orderItemDO);
            //将采购标记变为1
            item.setPurchaseMark(1);
            //停止合并
            //将相同类型的数据合并,通过资源id,因为数据来源不同，这里用MaterialId,不是resourceTypeId
//            if(resourceMap.containsKey(item.getMaterialId())){
//                PlanPurchaseMaterialDO planDO = resourceMap.get(item.getMaterialId());
//                planDO.setPurchaseAmount(planDO.getPurchaseAmount()+item.getPurchaseAmount());
//                resourceMap.put(item.getMaterialId(),planDO);
//            }else {
//                PlanPurchaseMaterialDO planDO = new PlanPurchaseMaterialDO();
//                BeanUtil.copyProperties(item,planDO);
//                resourceMap.put(item.getMaterialId(),planDO);
//            }
        }
        //更新资源列表
        planPurchaseMaterialMapper.updateBatch(list);
        //存入源单子信息
        planPurchaseOrderItemMapper.insertBatch(orderItemDOList);

        //向采购发起采购单
        AdminUserRespDTO user = adminUserApi.getUser(userId).getCheckedData();
        PurchaseRequirementDTO requirementDTO = new PurchaseRequirementDTO();
        requirementDTO.setApplicant(userId.toString()).setApplicationDepartment(user.getDeptId().toString()).setApplicationDate(LocalDateTime.now()).setApplicationReason("物资采购").setType(4);

        //合并数据
//        List<PlanPurchaseMaterialDO> mergeList = new ArrayList<>();
//        for (Map.Entry<String, PlanPurchaseMaterialDO> entry : resourceMap.entrySet()) {
//            mergeList.add(entry.getValue());
//        }

        Map<String, PlanPurchaseMaterialDO> map = CollectionUtils.convertMap(list, PlanPurchaseMaterialDO::getId);
        List<PurchaseRequirementDTO.Detail> details = BeanUtils.toBean(list, PurchaseRequirementDTO.Detail.class, (vo) -> {
            PlanPurchaseMaterialDO item = map.get(vo.getId());
            vo.setRequiredDate(item.getPurchaseTime())
                    .setEstimatedPrice(item.getPredictPrice())
                    .setRequiredQuantity(new BigDecimal(item.getPurchaseAmount()))
                    .setRequiredMaterial(item.getMaterialId())
                    .setSourceType(3)//用来跳过采购计划明细重复验证
//                    .setSourceId(orderDO.getId())
                    .setSourceId(vo.getId())
                    .setId(null)
                    .setOrderId(item.getProjectOrderId())
                    .setProjectId(item.getProjectId());
            //外协
            if(item.getPlanType()==2){
                vo.setProjectPlanId(item.getProjectPlanId())
                .setProjectPlanItemId(item.getProjectPlanItemId())
                .setPlanType(item.getPlanType());
            }else {
                vo.setPlanType(item.getPlanType());
            }
        });
        requirementDTO.setDetails(details);
        requirementApi.createPurchaseRequirement(requirementDTO).getCheckedData();
        return true;
    }
}
