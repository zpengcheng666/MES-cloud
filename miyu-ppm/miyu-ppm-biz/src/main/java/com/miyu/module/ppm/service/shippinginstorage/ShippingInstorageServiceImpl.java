package com.miyu.module.ppm.service.shippinginstorage;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCancelReqDto;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationReqListDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationSaveReqDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.PmsOrderApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.OrderListDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.controller.admin.shipping.vo.ShippingRespVO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.dal.mysql.shipping.ShippingMapper;
import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;
import com.miyu.module.ppm.dal.mysql.shippinginfo.ShippingInfoMapper;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.qms.api.dto.InspectionSchemeReqDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetReqDTO;
import com.miyu.module.qms.api.dto.InspectionSheetRespDTO;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.qms.enums.InspectionSchemeTypeEnum;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import com.mzt.logapi.context.LogRecordContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.shippinginstorage.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginstorage.ShippingInstorageDO;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shippinginstorage.ShippingInstorageMapper;
import com.miyu.module.ppm.dal.mysql.shippinginstoragedetail.ShippingInstorageDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.ApiConstants.SHIPPING_INSTORAGE_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_INSTORAGE_NOT_EXISTS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_INSTORAGE_NUMBER_OUT_LIMIT;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_PMS_NOT_SUCESS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 销售订单入库 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ShippingInstorageServiceImpl implements ShippingInstorageService {
    private static Logger logger = LoggerFactory.getLogger(ShippingInstorageService.class);

    @Resource
    private ConsignmentMapper consignmentMapper;
    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Resource
    private InspectionSchemeApi inspectionSchemeApi;
    @Resource
    private InspectionSheetApi inspectionSheetApi;
    @Resource
    private OrderApi orderApi;
    @Resource
    private PmsOrderApi pmsOrderApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;

    @Resource
    private ShippingMapper shippingMapper;

    @Resource
    private ShippingInfoMapper shippingInfoMapper;

    @Resource
    private ShippingDetailMapper shippingDetailMapper;
    private ConsignmentDO validateShippingInstorageExists(String id) {
        ConsignmentDO shippingInstorageDO = consignmentMapper.selectById(id);
        if (shippingInstorageDO == null) {
            throw exception(SHIPPING_INSTORAGE_NOT_EXISTS);
        }
        return shippingInstorageDO;
    }


    @Override
    public void checkInBoundInfo() {
        //获取所有入库中的入库单
//        List<ShippingInstorageDO> shippingInstorageDOS = shippingInstorageMapper.selectList(ShippingInstorageDO::getShippingInstorageStatus, ConsignmentStatusEnum.INBOUND.getStatus());
//        if (!org.springframework.util.CollectionUtils.isEmpty(shippingInstorageDOS)) {
//            List<OrderReqDTO> list = CollectionUtils.convertList(shippingInstorageDOS, shippingInstorageDO -> {
//                OrderReqDTO dto = new OrderReqDTO();
//                dto.setOrderNumber(shippingInstorageDO.getNo());
//                //TODO  原材料入库
//                dto.setOrderType(DictConstants.WMS_ORDER_TYPE_MATERIAL_IN);
//                return dto;
//            });
//            //查询发货单对应的出库单信息
//            CommonResult<List<OrderReqDTO>> result = orderApi.orderList(list);
//            //筛选出出库完成的单子
//            List<OrderReqDTO> orderReqDTOList = result.getCheckedData().stream().filter(orderReqDTO -> orderReqDTO.getOrderStatus().equals(DictConstants.WMS_ORDER_DETAIL_STATUS_4)).collect(Collectors.toList());
//
//            if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOList)) {
//                Map<String, List<OrderReqDTO>> orderMap = result.getCheckedData().stream().collect(Collectors.groupingBy(OrderReqDTO::getOrderNumber));
//
//
//                for (ShippingInstorageDO shippingInstorageDO : shippingInstorageDOS) {
//                    List<OrderReqDTO> orderReqDTOS = orderMap.get(shippingInstorageDO.getNo());
//                    Boolean finish = true;
//                    if (orderReqDTOS.size() != orderReqDTOList.size()) {
//                        finish = false;
//                        continue;
//                    }
//                    List<OrderMaterialRelationUpdateDTO> relationSaveReqDTOS = new ArrayList<>();
//                    List<ShippingInstorageDetailDO> detailDOS = shippingInstorageDetailMapper.selectListByShippingStorageId(shippingInstorageDO.getId());
//
//                    if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOS)) {
//
//                        Map<String, List<OrderReqDTO>> orderReqDTOMap = new HashMap<>();
//
//                        for (OrderReqDTO reqDTO : orderReqDTOS) {
//                            if (org.springframework.util.CollectionUtils.isEmpty(orderReqDTOMap.get(reqDTO.getMaterialConfigId()))) {
//                                orderReqDTOMap.put(reqDTO.getMaterialConfigId(), Lists.newArrayList(reqDTO));
//                            } else {
//                                orderReqDTOMap.get(reqDTO.getMaterialConfigId()).add(reqDTO);
//                            }
//
//                        }
//
//                        for (ShippingInstorageDetailDO detailDO : detailDOS) {
//                            List<OrderReqDTO> dtos = orderReqDTOMap.get(detailDO.getMaterialId());
//                            if (org.springframework.util.CollectionUtils.isEmpty(dtos)) {//找不到完成的出库信息  则视为没完成
//                                finish = false;
//                            } else if (dtos.size() != detailDO.getSignedAmount().intValue()) {
//                                finish = false; //数量对应不上则视为没完成
//                            } else {
//                                //
//                                if (StringUtils.isNotBlank(detailDO.getOrderId())) {
//                                    for (OrderReqDTO dto : dtos) {
//                                        OrderMaterialRelationUpdateDTO reqDTO = new OrderMaterialRelationUpdateDTO();
//                                        reqDTO.setOrderId(detailDO.getOrderId());
//                                        reqDTO.setProjectId(detailDO.getProjectId());
//                                        CommonResult<MaterialStockRespDTO> respDTOCommonResult = materialStockApi.getById(dto.getMaterialStockId());
//                                        reqDTO.setMaterialCode(respDTOCommonResult.getCheckedData().getBarCode());
//                                        reqDTO.setVariableCode(respDTOCommonResult.getCheckedData().getBarCode());
//                                        relationSaveReqDTOS.add(reqDTO);
//                                    }
//                                }
//                            }
//                        }
//                    } else {
//                        finish = false;
//                    }
//
//                    if (finish) {//更新状态为完成  并
//                        if (!org.springframework.util.CollectionUtils.isEmpty(relationSaveReqDTOS)) {
//
//                            //区分普通采购 和毛坯物料采购
//                            CommonResult<String> commonResult = pmsOrderMaterialRelationApi.orderMaterialInitBatch(relationSaveReqDTOS);
//                            if (!commonResult.isSuccess()) {
//                                logger.error("调用PMS更新条码失败");
//                                throw exception(SHIPPING_PMS_NOT_SUCESS);
//                            }
//                        }
//
//                        shippingInstorageDO.setShippingInstorageStatus(ConsignmentStatusEnum.FINISH.getStatus());
//
//                        shippingInstorageMapper.updateById(shippingInstorageDO);
//
//                    }
//
//                }
//
//            }
//
//
//        }
    }


    @Override
    public void checkSchemeSheetResult() {

//
//        //获取所有检验中的入库单
//        List<ShippingInstorageDO> shippingInstorageDOS = shippingInstorageMapper.selectList(ShippingInstorageDO::getShippingInstorageStatus, ConsignmentStatusEnum.ANALYSISING.getStatus());
//
//
//        if (!org.springframework.util.CollectionUtils.isEmpty(shippingInstorageDOS)) {
//
//            List<String> consignmentNo = CollectionUtils.convertList(shippingInstorageDOS, ShippingInstorageDO::getNo);
//            //查询对应的质检单
//
//            List<OrderReqDTO> reqDTOS = CollectionUtils.convertList(shippingInstorageDOS, shippingDO ->
//            {
//                OrderReqDTO orderReqDTO = new OrderReqDTO().setOrderNumber(shippingDO.getNo()).setOrderType(DictConstants.WMS_ORDER_TYPE_MATERIAL_IN);
//                return orderReqDTO;
//            });
//            CommonResult<List<OrderReqDTO>> orderList = orderApi.orderList(reqDTOS);
//            Map<String, List<OrderReqDTO>> listMap = new HashMap<>();
//
//            for (OrderReqDTO dto : orderList.getCheckedData()) {
//                if (com.alibaba.nacos.common.utils.CollectionUtils.isEmpty(listMap.get(dto.getOrderNumber()))) {
//                    listMap.put(dto.getOrderNumber(), Lists.newArrayList(dto));
//                } else {
//                    listMap.get(dto.getOrderNumber()).add(dto);
//                }
//
//            }
//            CommonResult<List<InspectionSheetRespDTO>> result = inspectionSheetApi.getInspectionSheetListByRecordNumber(consignmentNo);
//
//            Map<String, InspectionSheetRespDTO> map = new HashMap<>();
//            for (InspectionSheetRespDTO dto : result.getCheckedData()) {
//                map.put(dto.getRecordNumber() + "_" + dto.getSchemes().get(0).getMaterialConfigId(), dto);
//            }
//
//            //如果质检单完成
//            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
//            List<ShippingInstorageDO> finishList = new ArrayList<>();
//            for (ShippingInstorageDO shippingInstorageDO : shippingInstorageDOS) {
//
//                List<ShippingInstorageDetailDO> detailDOS = shippingInstorageDetailMapper.selectListByShippingStorageId(shippingInstorageDO.getId());
//                Map<String, MaterialConfigRespDTO> materialConfigRespDTOMap = materialMCCApi.getMaterialConfigMap(detailDOS.stream().map(ShippingInstorageDetailDO::getMaterialId).collect(Collectors.toList()));
//
//                Boolean finish = true;
//                List<String> materialIds = new ArrayList<>();
//                for (ShippingInstorageDetailDO detailDO : detailDOS) {
//
//
//                    InspectionSheetRespDTO dto = map.get(shippingInstorageDO.getNo() + "_" + detailDO.getMaterialId());
//
//                    if (dto != null) {
//                        if (dto.getStatus().intValue() != 3) {//如果质检单状态不为3 则视为没完成
//                            finish = false;
//                        } else {
//                            materialIds.add(detailDO.getMaterialId());
//                        }
//                    }
//
//                }
//
//                if (finish) {
//                    //如果质检单完成  则调用WMS  通知状态
//                    if (!org.springframework.util.CollectionUtils.isEmpty(materialIds)) {
//
////                        for (ShippingInstorageDetailDO detailDO : detailDOS) {
////
////                            MaterialConfigRespDTO configRespDTO = materialConfigRespDTOMap.get(detailDO.getMaterialId());
////                            //如果是单件需要拆成单个的WMS入库订单
////                            if (configRespDTO.getMaterialManage().intValue() ==1){
////
////                                for (int i=0;i<detailDO.getSignedAmount().intValue();i++){
////                                    OrderReqDTO dto = new OrderReqDTO();
////                                    dto.setOrderStatus(1);//待质检
////                                    dto.setOrderNumber(shippingInstorageDO.getNo());
////                                    dto.setOrderType(DictConstants.WMS_ORDER_TYPE_MATERIAL_IN);
////                                    dto.setQuantity(1);
////                                    dto.setMaterialConfigId(detailDO.getMaterialId());
////                                    orderReqDTOList.add(dto);
////                                }
////                            }else {
////                                OrderReqDTO dto = new OrderReqDTO();
////                                dto.setOrderStatus(1);//待质检
////                                dto.setOrderNumber(shippingInstorageDO.getNo());
////                                dto.setOrderType(DictConstants.WMS_ORDER_TYPE_MATERIAL_IN);
////                                dto.setQuantity(detailDO.getSignedAmount().intValue());
////                                dto.setMaterialConfigId(detailDO.getMaterialId());
////                                orderReqDTOList.add(dto);
////                            }
////
////
//////
////                        }
//
//                        List<OrderReqDTO> dtos = listMap.get(shippingInstorageDO.getNo());
//
//                        for (OrderReqDTO dto : dtos) {
//                            dto.setOrderStatus(1);//待质检
//                            dto.setChooseStockId(dto.getMaterialStockId());
//                            orderReqDTOList.add(dto);
//                        }
//                    }
//                    shippingInstorageDO.setShippingInstorageStatus(ConsignmentStatusEnum.INBOUND.getStatus());
//                    finishList.add(shippingInstorageDO);
//                }
//
//
//            }
//
//            if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOList)) {
//                //更新入库状态为待入库
//                CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
//                //更新收货单状态完成
//                if (commonResult.isSuccess()) {
//                    shippingInstorageMapper.updateBatch(finishList);
//                }
//
//            }
//
//
//        }


        //
    }

    @Override
    public void updateShippingInstorgerProcessInstanceStatus(String id, Integer status) {
        ConsignmentDO shippingInstorageDO = validateShippingInstorageExists(id);
        shippingInstorageDO.setId(id);
        shippingInstorageDO.setStatus(status);
        if (DMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            shippingInstorageDO.setConsignmentStatus(ConsignmentStatusEnum.SINGING.getStatus());
        }


        if (DMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            shippingInstorageDO.setConsignmentStatus(ConsignmentStatusEnum.CANCEL.getStatus());
        }
        purchaseConsignmentService.updateConsignmentStatus(shippingInstorageDO);
    }
    @Override
    public void updateShippingProcessInstanceStatus(String id, Integer status) {
        ShippingDO shippingDO = shippingMapper.selectById(id);
        shippingDO.setId(id);
        shippingDO.setStatus(status);
        List<ShippingDetailDO> shippingDetailDOList = shippingDetailMapper.selectListByShippingId(id);

        List<ShippingInfoDO> shippingInfoDOS = shippingInfoMapper.selectList(ShippingInfoDO::getShippingId,id);

        if (DMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            shippingDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());
            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();

            for (ShippingInfoDO shippingInfoDO :shippingInfoDOS){
                shippingInfoDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());

            }
            for (ShippingDetailDO detailDO : shippingDetailDOList) {

                OrderReqDTO orderReqDTO = new OrderReqDTO();
                orderReqDTO.setOrderNumber(shippingDO.getNo());
                orderReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT);
                orderReqDTO.setChooseStockId(detailDO.getMaterialStockId());
                orderReqDTO.setOrderStatus(1);
                orderReqDTO.setQuantity(detailDO.getConsignedAmount().intValue());
                orderReqDTOList.add(orderReqDTO);

                detailDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());
            }
            CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
            if (!commonResult.isSuccess()) {
                throw exception(SHIPPING_OUTBOUND_CANCEL_ERROR);
            }
            //
        }

        if (DMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            shippingDO.setShippingStatus(ShippingStatusEnum.REJECT.getStatus());

            for (ShippingInfoDO shippingInfoDO :shippingInfoDOS){
                shippingInfoDO.setShippingStatus(ShippingStatusEnum.REJECT.getStatus());

            }

            //TODO  调用WMS作废出 库单
            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
            for (ShippingDetailDO detailDO : shippingDetailDOList) {

                OrderReqDTO orderReqDTO = new OrderReqDTO();
                orderReqDTO.setOrderNumber(shippingDO.getNo());
                orderReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT);
                orderReqDTO.setChooseStockId(detailDO.getMaterialStockId());
                orderReqDTO.setOrderStatus(5);
                orderReqDTO.setQuantity(detailDO.getConsignedAmount().intValue());
                orderReqDTOList.add(orderReqDTO);
                detailDO.setShippingStatus(ShippingStatusEnum.REJECT.getStatus());
            }
            CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
            if (!commonResult.isSuccess()) {
                throw exception(SHIPPING_OUTBOUND_CANCEL_ERROR);
            }
        }

        shippingDetailMapper.updateBatch(shippingDetailDOList);
        shippingInfoMapper.updateBatch(shippingInfoDOS);
        shippingMapper.updateById(shippingDO);

    }




    void validQuantity(ShippingInstorageSaveReqVO reqVO) {
//        CommonResult<List<OrderListDTO>> orderResult = pmsOrderApi.getOrderItemList(reqVO.getProjectId());
//
//
//        List<ShippingInstorageDetailDO> detailDOS = this.getDetailsByProjectId(reqVO.getProjectId());
//
//
//        List<ShippingInstorageDetailDO> instorageDetailDOS = detailDOS.stream().filter(shippingInstorageDetailDO -> (!shippingInstorageDetailDO.getShippingStorageId().equals(reqVO.getId()))).collect(Collectors.toList());
//
//
//        Integer useNumber = 0;
//        if (!org.springframework.util.CollectionUtils.isEmpty(instorageDetailDOS)){
//
//            for (ShippingInstorageDetailDO detailDO : instorageDetailDOS){
//                useNumber = useNumber+detailDO.getConsignedAmount().intValue();
//            }
//        }
//        Integer number = 0;
//        for (ShippingInstorageDetailDO detailDO : reqVO.getShippingInstorageDetails()){
//            number = number+detailDO.getConsignedAmount().intValue();
//        }
//        Integer totalNumber = 0;
//        for (OrderListDTO detailDO : orderResult.getCheckedData()){
//            totalNumber = totalNumber+detailDO.getQuantity();
//        }
//
//
//        if (totalNumber <(number+useNumber)){
//            logger.error("选择数量超过订单限制");
//            throw exception(SHIPPING_INSTORAGE_NUMBER_OUT_LIMIT);
//        }

    }
}