package cn.iocoder.yudao.module.pms.service.orderMaterialRelation;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationRespVO;
import cn.iocoder.yudao.module.pms.controller.admin.orderMaterialRelation.vo.OrderMaterialRelationSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PmsPlanMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.outsourcing.McsOutsourcingPlanDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.ProcessPlanDetailApi;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.PurchaseConsignmentApi;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.*;

/**
 * 订单物料关系表 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class OrderMaterialRelationServiceImpl implements OrderMaterialRelationService {

    @Resource
    private OrderMaterialRelationMapper orderMaterialRelationMapper;

    @Resource
    private ProcessPlanDetailApi processPlanDetailApi;

    @Resource
    private PmsOrderMapper orderMapper;

    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @Resource
    private PmsPlanMapper pmsPlanMapper;

    @Resource
    private PmsApprovalMapper approvalMapper;

    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;

    @Resource
    private PurchaseConsignmentApi purchaseConsignmentApi;

    @Resource
    private ShippingApi shippingApi;

    @Override
    public String createOrderMaterialRelation(OrderMaterialRelationSaveReqVO createReqVO) {
        // 插入
        OrderMaterialRelationDO orderMaterialRelation = BeanUtils.toBean(createReqVO, OrderMaterialRelationDO.class);
        orderMaterialRelationMapper.insert(orderMaterialRelation);
        // 返回
        return orderMaterialRelation.getId();
    }

    @Override
    public void updateOrderMaterialRelation(OrderMaterialRelationSaveReqVO updateReqVO) {
        // 校验存在
        validateOrderMaterialRelationExists(updateReqVO.getId());
        // 更新
        OrderMaterialRelationDO updateObj = BeanUtils.toBean(updateReqVO, OrderMaterialRelationDO.class);
        orderMaterialRelationMapper.updateById(updateObj);
    }

    @Override
    public void deleteOrderMaterialRelation(String id) {
        // 校验存在
        validateOrderMaterialRelationExists(id);
        // 删除
        orderMaterialRelationMapper.deleteById(id);
    }

    private void validateOrderMaterialRelationExists(String id) {
        if (orderMaterialRelationMapper.selectById(id) == null) {
            throw exception(ORDER_MATERIAL_RELATION_NOT_EXISTS);
        }
    }

    @Override
    public OrderMaterialRelationDO getOrderMaterialRelation(String id) {
        return orderMaterialRelationMapper.selectById(id);
    }

    @Override
    public List<OrderMaterialRelationDO> getRelationListByOrderId(String id) {
        return orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId,id);
    }

    @Override
    public PageResult<OrderMaterialRelationDO> getOrderMaterialRelationPage(OrderMaterialRelationPageReqVO pageReqVO) {
        return orderMaterialRelationMapper.selectPage(pageReqVO);
    }

    @Override
    public List<OrderMaterialRelationDO> getOrderMaterialRelationWith(OrderMaterialRelationSaveReqVO req) {
        return orderMaterialRelationMapper.getOrderMaterialRelationWith(req);
    }

    @Override
    public void insertOrderMaterialRelation(OrderMaterialRelationDO relationDO) {
        orderMaterialRelationMapper.insert(relationDO);
    }

    @Override
    public void insertOrderMaterialRelationBatch(List<OrderMaterialRelationDO> relationDOList) {
        orderMaterialRelationMapper.insertBatch(relationDOList);
    }

    @Override
    public void updateRelation(OrderMaterialRelationDO relationDO) {
        orderMaterialRelationMapper.updateById(relationDO);
    }

    @Override
    public List<OrderMaterialRelationDO> getRelationByBarCode(List<String> list) {
        return orderMaterialRelationMapper.selectListByBarCode(list);
    }

    /**
     * 备料更新，从空关系更新
     * @param createReqVO
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void prepareUpdate(OrderMaterialRelationSaveReqVO createReqVO) {
        if(CollectionUtils.isAnyEmpty(createReqVO.getMaterialCodeList())){
            return;
        }
        List<MaterialStockRespDTO> materialStockList = materialStockApi.getMaterialsByBarCodes(createReqVO.getMaterialCodeList()).getCheckedData();
        Map<String, MaterialStockRespDTO> stringMaterialStockRespDTOMap = CollectionUtils.convertMap(materialStockList, MaterialStockRespDTO::getBarCode);

        List<OrderMaterialRelationDO> relationDOList = orderMaterialRelationMapper.selectList(OrderMaterialRelationDO::getOrderId, createReqVO.getOrderId());
        List<String> materialCodeList = createReqVO.getMaterialCodeList();
        String step = null;
        PmsOrderDO orderDO = orderMapper.selectById(createReqVO.getOrderId());
        if(orderDO.getOutsource() == 1){
            //整单外协备料,区别是没有工艺,好像没什么要写的
        }else {
            //加工备料，和原来一样
            //工艺
            String processScheme = createReqVO.getProcessScheme();
            List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(processScheme).getCheckedData();
            ProcedureRespDTO procedureRespDTO = procedureList.get(0);
            step = procedureRespDTO.getProcedureNum();
        }
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
                orderMaterialRelationDO.setMaterialCode(materialCode).setVariableCode(materialCode).setPrepare(true).setPlanId(createReqVO.getPlanId()).setStep(step).setMaterialStatus(1);
                orderMaterialRelationMapper.updateById(orderMaterialRelationDO);
                materialCodeList.remove(0);
            }
        }
    }

    @Override
    public List<MaterialStockRespDTO> selectCompleteMaterialCodeByRelationId(String id) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(id);
        String orderId = relationDO.getOrderId();
        PmsOrderDO orderDO = orderMapper.selectById(orderId);

        if(StrUtil.isNotEmpty(relationDO.getMaterialCode())){
            //有码的情况，是带料，只要查这个码的类型是不是图号即可
            List<MaterialStockRespDTO> materialStockRespDTOList = materialStockApi.getMaterialsByBarCode(relationDO.getMaterialCode()).getCheckedData();
            if(materialStockRespDTOList.size()>0){
                MaterialStockRespDTO materialStockRespDTO = materialStockRespDTOList.get(0);
                //得到物料类型
                String materialConfigId = materialStockRespDTO.getMaterialConfigId();
                List<MaterialConfigRespDTO> materialConfigRespDTOList = materialMCCApi.getMaterialConfigList(Arrays.asList(materialConfigId)).getCheckedData();
                MaterialConfigRespDTO materialConfigRespDTO = materialConfigRespDTOList.get(0);
                //判断物料编号和图号是否一致,一致返回库存，不一致返回空集合
                if(materialConfigRespDTO.getMaterialNumber().equals(orderDO.getPartNumber())){
                    return materialStockRespDTOList;
                }
            }

        }else {
            //无码的情况,不带料,要从库存里选一个没被占用的成品
            String partNumber = orderDO.getPartNumber();
            //根据图号查类型id
            MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
            dto.setMaterialNumber(partNumber);
            List<MaterialConfigRespDTO> materialConfigRespDTOList = materialMCCApi.getMaterialConfigListByTypeCode(dto).getCheckedData();
            //根据物料类型id查码
            MaterialConfigRespDTO materialConfigRespDTO = materialConfigRespDTOList.get(0);
            String id1 = materialConfigRespDTO.getId();
            List<MaterialStockRespDTO> materialStockRespDTOListTemp = materialStockApi.getOutOrderMaterialsByConfigIds(Arrays.asList(id1)).getCheckedData();
            List<MaterialStockRespDTO> materialStockRespDTOList = materialStockRespDTOListTemp.stream().filter((item) -> {return ObjectUtil.isNotNull(item.getMaterialStatus())&&item.getMaterialStatus() == 2;}).collect(Collectors.toList());
            if(materialStockRespDTOList.size()>0){
                //虽然查到了码，但是得排除掉已经被其他项目占用的码
                List<String> materialCodeList = materialStockRespDTOList.stream().map(MaterialStockRespDTO::getBarCode).collect(Collectors.toList());
                LambdaQueryWrapperX<OrderMaterialRelationDO> wrapperX = new LambdaQueryWrapperX<>();
                wrapperX.in(OrderMaterialRelationDO::getMaterialCode,materialCodeList);
                List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(wrapperX);
                //能查到关系说明已经被占用了,不能被选取
                List<String> hasCode = relationDOS.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.toList());
                //排除
                List<MaterialStockRespDTO> MaterialStockRespDTOList2 = materialStockRespDTOList.stream().filter((item) -> {
                    return !hasCode.contains(item.getBarCode());
                }).collect(Collectors.toList());
                return  MaterialStockRespDTOList2;
            }
        }

        return Collections.emptyList();
    }

    @Override
    public List<MaterialStockRespDTO> selectCompleteMaterialCodeByRelationId2(String id,String planId) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(id);
        String orderId = relationDO.getOrderId();
        PmsOrderDO orderDO = orderMapper.selectById(orderId);
        if(StrUtil.isNotEmpty(relationDO.getMaterialCode())){
            //有码的情况，是带料，只要查这个码的类型是不是图号即可
            List<MaterialStockRespDTO> materialStockRespDTOList = materialStockApi.getMaterialsByBarCode(relationDO.getMaterialCode()).getCheckedData();
            if(materialStockRespDTOList.size()>0){
                MaterialStockRespDTO materialStockRespDTO = materialStockRespDTOList.get(0);
                //再加个状态判断
                if(ObjectUtil.isNotNull(materialStockRespDTO.getMaterialStatus())&&(materialStockRespDTO.getMaterialStatus()==2)){
                    //得到物料类型
                    String materialConfigId = materialStockRespDTO.getMaterialConfigId();
                    List<MaterialConfigRespDTO> materialConfigRespDTOList = materialMCCApi.getMaterialConfigList(Arrays.asList(materialConfigId)).getCheckedData();
                    MaterialConfigRespDTO materialConfigRespDTO = materialConfigRespDTOList.get(0);
                    //判断物料编号和图号是否一致,一致返回库存，不一致返回空集合
                    if(materialConfigRespDTO.getMaterialNumber().equals(orderDO.getPartNumber())){
                        return materialStockRespDTOList;
                    }
                }
            }
        }else {
            //无码的情况,不带料,改查收货单
            PmsPlanDO pmsPlanDO = pmsPlanMapper.selectById(planId);
            PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(pmsPlanDO.getProjectId());
            //查收货详情，状态6(结束),类型1(整单外协不带料都当采购算,带料才是2)
            List<PurchaseConsignmentDetailDTO> consignmentDetailListTemp = purchaseConsignmentApi.getPurchaseDetailListByProjectIds(Arrays.asList(pmsApprovalDO.getId())).getCheckedData();
            List<PurchaseConsignmentDetailDTO> consignmentDetailList = consignmentDetailListTemp.stream().filter((item) -> {
                return ObjectUtil.isNotNull(item.getConsignmentStatus()) && (item.getConsignmentStatus() == 6) && ("1".equals(item.getConsignmentType()))&&planId.equals(item.getProjectPlanId());
            }).collect(Collectors.toList());
            //收货单,排除退货,排除已选,剩下可选码
            consignmentDetailList = filterConsignmentDetail(consignmentDetailList);
            List<String> barcodeList = consignmentDetailList.stream().map(PurchaseConsignmentDetailDTO::getBarCode).collect(Collectors.toList());
            if(barcodeList.size()>0){
                List<MaterialStockRespDTO> materialStockListTemp = materialStockApi.getMaterialsByBarCodes(barcodeList).getCheckedData();
                List<MaterialStockRespDTO> materialStockList = materialStockListTemp.stream().filter((item) -> {
                    return ObjectUtil.isNotNull(item.getMaterialStatus()) && item.getMaterialStatus() == 2;
                }).collect(Collectors.toList());
                return materialStockList;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 过滤收货明细
     * 有退货的不要
     * 被选中的不要
     * @param list
     * @return
     */
    public List<PurchaseConsignmentDetailDTO> filterConsignmentDetail(List<PurchaseConsignmentDetailDTO> list){
        if(list.size()>0){
            List<String> detailIds = new ArrayList<>();
            List<String> codeList = new ArrayList<>();
            for (PurchaseConsignmentDetailDTO purchaseConsignmentDetailDTO : list) {
                detailIds.add(purchaseConsignmentDetailDTO.getId());
                codeList.add(purchaseConsignmentDetailDTO.getBarCode());
            }
            //查退货code
            List<ShippingDetailDTO> returnDetail = shippingApi.getShippingByConsignmentDetailIds(detailIds).getCheckedData();
            List<String> returnCodeList = returnDetail.stream().filter(item -> ObjectUtil.isNotNull(item.getBarCode())).map(ShippingDetailDTO::getBarCode).collect(Collectors.toList());
            //查关系code
            LambdaQueryWrapper<OrderMaterialRelationDO> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(OrderMaterialRelationDO::getMaterialCode,codeList);
            List<OrderMaterialRelationDO> relationDOS = orderMaterialRelationMapper.selectList(wrapper);
            List<String> relationCode = relationDOS.stream().map(OrderMaterialRelationDO::getMaterialCode).collect(Collectors.toList());
            //被退货码或者关系码包含的都不要了
            list = list.stream().filter((item)->{
                return !(returnCodeList.contains(item.getBarCode())||relationCode.contains(item.getBarCode()));
            }).collect(Collectors.toList());

        }
        return list;
    }

    @Override
    public List<MaterialStockRespDTO> selectStorageMaterialCodeByRelationId(String id) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(id);
//        String orderId = relationDO.getOrderId();
//        PmsOrderDO orderDO = orderMapper.selectById(orderId);
        //查工序
        PmsPlanDO pmsPlanDO = pmsPlanMapper.selectById(relationDO.getPlanId());
        String processVersionId = pmsPlanDO.getProcessVersionId();
        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(processVersionId).getCheckedData();
        String step = relationDO.getStep();
        List<MaterialStockRespDTO> materialStockRespDTOListTemp = materialStockApi.getMaterialsByBarCode(relationDO.getMaterialCode()).getCheckedData();
        List<MaterialStockRespDTO> materialStockRespDTOList = materialStockRespDTOListTemp.stream().filter((item) -> {
            return ObjectUtil.isNotNull(item.getMaterialStatus()) && (item.getMaterialStatus() == 2);
        }).collect(Collectors.toList());
        if(materialStockRespDTOList.size()==0){
            return Collections.emptyList();
        }
        String[] s = materialStockRespDTOList.get(0).getMaterialNumber().split("_");
        int index = 0;
        int index1 = 0;
        //大于1说明加工了，是图号+序号,不然就是是物料编号，返回空就行
        if(s.length>1){
            String s1 = s[1];
            for (int i = 0; i < procedureList.size(); i++) {

                if(procedureList.get(i).getProcedureNum().equals(step)){
                    index = i;
                }
                if(procedureList.get(i).getProcedureNum().equals(s1)){
                    index1 = i;
                }
            }
            //所查物码的物料编号工序等于或在当前工序之后，说明外协入库了。
            if(index1>=index){
                return materialStockRespDTOList;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public void outsourceComplete(OrderMaterialRelationSaveReqVO createReqVO) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(createReqVO.getId());
        OrderMaterialRelationDO relation = new OrderMaterialRelationDO();
        relation.setId(relationDO.getId()).setMaterialStatus(5);
        if(StrUtil.isNotEmpty(relationDO.getMaterialCode())){
            //有码
        }else {
            //无码
            relation.setMaterialCode(createReqVO.getMaterialCode());
        }
        orderMaterialRelationMapper.updateById(relation);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void outsourceInStorage(OrderMaterialRelationSaveReqVO createReqVO) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(createReqVO.getId());
        //查工序
        PmsPlanDO pmsPlanDO = pmsPlanMapper.selectById(relationDO.getPlanId());
        String processVersionId = pmsPlanDO.getProcessVersionId();
        List<ProcedureRespDTO> procedureList = processPlanDetailApi.getProcedureListByProcessVersionId(processVersionId).getCheckedData();
        //预设状态，加工完成
        relationDO.setMaterialStatus(5);
        //relationDO.setStep("-1");
        String step = relationDO.getStep();
        for (int i = 0; i < procedureList.size()-1; i++) {
            //如果委派工序和当前遍历工序相等,判断下一道工序是不是外协
            if(procedureList.get(i).getProcedureNum().equals(step)){
                if(procedureList.get(i+1).getIsOut().equals("1")){
                    //是外协
                    step = procedureList.get(i+1).getProcedureNum();
                }else {
                    //不是外协,设置要通知的工序(当前遍历工序),设置物料状态
                    relationDO.setMaterialStatus(2);
                    //结束外协的工序通知mcs
                    step = procedureList.get(i).getProcedureNum();
                    //要加工的工序是下一道
                    relationDO.setStep(procedureList.get(i+1).getProcedureNum());
                    break;
                }
            }
        }
        //mcs修改，我这不用改了
//        orderMaterialRelationMapper.updateById(relationDO);
        //通知工序外协完成,继续加工,mcs
        McsOutsourcingPlanDTO dto = new McsOutsourcingPlanDTO();
        dto.setOutsourcingId(relationDO.getContractId()).setOrderNumber(relationDO.getOrderNumber()).setMaterialCodeList(relationDO.getMaterialCode()).setProcessNumber(step).setAidMill(relationDO.getAidMill());
        Object checkedData = mcsManufacturingControlApi.outsourcingFinish(dto).getCheckedData();


    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String addRelation(OrderMaterialRelationSaveReqVO createReqVO) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(createReqVO.getId());
        if(StrUtil.isEmpty(relationDO.getPlanItemId())){
            relationDO.setPlanItemId("000000");
        }
        if(StrUtil.isEmpty(relationDO.getMaterialCode())){
            relationDO.setMaterialCode("ABANDON");
        }
        relationDO.setMaterialStatus(8);
        orderMaterialRelationMapper.updateById(relationDO);
        orderMaterialRelationMapper.insert(new OrderMaterialRelationDO().setMaterialStatus(3).setOrderId(relationDO.getOrderId()).setProjectId(relationDO.getProjectId()));
        return "ok";
    }

    /**
     * 释放物料关系码
     * @param id
     * @return
     */
    @Override
    public String releaseCode(String id) {
        OrderMaterialRelationDO relationDO = orderMaterialRelationMapper.selectById(id);
        //如果能查到子计划id,说明已经开工了,开工就不适合释放了
        if(ObjectUtil.isNotNull(relationDO.getPlanItemId())){
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CODE_HAS_WORKING);
        }
        orderMaterialRelationMapper.releaseCode(id);
        return "ok";
    }

}
