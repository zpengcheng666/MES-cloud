package com.miyu.module.ppm.service.contractconsignment;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OutsourcingMaterialSelectDTO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.mysql.shipping.ShippingMapper;
import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.PpmAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.shipping.ShippingService;
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

import com.miyu.module.ppm.controller.admin.contractconsignment.vo.*;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignmentdetail.ContractConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.contractconsignment.ContractConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.contractconsignmentdetail.ContractConsignmentDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.CONTRACT_CONSIGNMENT_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ApiConstants.OUT_WARHOUSE;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 外协发货 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ContractConsignmentServiceImpl implements ContractConsignmentService {
    private static Logger logger = LoggerFactory.getLogger(ContractConsignmentService.class);

    @Resource
    private ShippingDetailMapper shippingDetailMapper;
    @Resource
    private OrderApi orderApi;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private ContractService contractService;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;

    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private ShippingService shippingService;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;





    @Override
    public Boolean outBoundContractConsignment(ContractConsignmentDO consignmentDO, List<ContractConsignmentDetailDO> detailDOS) {
        List<OrderReqDTO> orderReqDTOList = CollectionUtils.convertList(detailDOS, detailDO ->
        {
            OrderReqDTO orderRespVO = new OrderReqDTO();
            orderRespVO.setOrderNumber(consignmentDO.getConsignmentNo());
            orderRespVO.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT);
            orderRespVO.setChooseStockId(detailDO.getMaterialStockId());
            orderRespVO.setQuantity(detailDO.getConsignedAmount().intValue());
            //目标仓库
            orderRespVO.setTargetWarehouseId(OUT_WARHOUSE);
            return orderRespVO;
        });

        CommonResult<List<String>> result = orderApi.orderDistribute(orderReqDTOList);

        if (result.isSuccess()) {
           return  true;
        } else {
            logger.error(result.getMsg());
            throw exception(SHIPPING_OUTBOUND_ERROR);
        }
    }



    private ShippingDO validateContractConsignmentExists(String id) {
        ShippingDO contractConsignmentDO = shippingMapper.selectById(id);
        if (contractConsignmentDO == null) {
            throw exception(CONTRACT_CONSIGNMENT_NOT_EXISTS);
        }

        return contractConsignmentDO;
    }


    // ==================== 子表（外协发货单详情） ====================


    @Override
    public void checkOutBoundInfo() {

//        //查询正在出库的发货单信息
//        List<ContractConsignmentDO> returnDTOS = contractConsignmentMapper.selectList(ContractConsignmentDO::getConsignmentStatus, ContractConsignmentStatusEnum.OUTBOUNDING.getStatus());
//
//        if (!org.springframework.util.CollectionUtils.isEmpty(returnDTOS)) {
//            List<OrderReqDTO> list = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList(returnDTOS, returnDO -> {
//                OrderReqDTO dto = new OrderReqDTO();
//                dto.setOrderNumber(returnDO.getConsignmentNo());
//                dto.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT);
//                return dto;
//            });
//            //查询发货单对应的出库单信息
//            CommonResult<List<OrderReqDTO>> result = orderApi.orderList(list);
//
//
//            //筛选出出库完成的单子
//            List<OrderReqDTO> orderReqDTOList = result.getCheckedData().stream().filter(orderReqDTO -> orderReqDTO.getOrderStatus().equals(DictConstants.WMS_ORDER_DETAIL_STATUS_4)).collect(Collectors.toList());
//
//            if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOList)) {
//                Map<String, List<OrderReqDTO>> orderMap = result.getCheckedData().stream().collect(Collectors.groupingBy(OrderReqDTO::getOrderNumber));
//
//
//                for (ContractConsignmentDO returnDO : returnDTOS) {
//                    List<OrderReqDTO> orderReqDTOS = orderMap.get(returnDO.getConsignmentNo());
//                    Boolean finish = true;
//                    List<ContractConsignmentDetailDO> returnDetailDOS = contractConsignmentDetailMapper.selectList(ContractConsignmentDetailDO::getConsignmentId,returnDO.getId());
//
//                    if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOS)) {
//
//                        Map<String, OrderReqDTO> orderReqDTOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(orderReqDTOS, OrderReqDTO::getMaterialStockId);
//
//                        for (ContractConsignmentDetailDO detailDO : returnDetailDOS) {
//                            OrderReqDTO dto = orderReqDTOMap.get(detailDO.getMaterialStockId());
//                            if (dto == null) {//找不到完成的出库信息  则视为没完成
//                                finish = false;
//                            }else if (dto.getQuantity().intValue() !=detailDO.getConsignedAmount().intValue()){
//                                finish = false; //数量对应不上则视为没完成
//                            }else {
//                                detailDO.setInboundAmount(detailDO.getConsignedAmount());
//                                detailDO.setInboundBy(dto.getOperator());
//                                detailDO.setInboundTime(dto.getOperateTime());
//                            }
//                        }
//                    } else {
//                        finish = false;
//                    }
//
//                    if (finish){//更新状态为待发货  并且更新出库数量
//                        returnDO.setConsignmentStatus(ContractConsignmentStatusEnum.SEND.getStatus());
//
//                        contractConsignmentMapper.updateById(returnDO);
//
//                        contractConsignmentDetailMapper.updateBatch(returnDetailDOS);
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
    public void updateContractConsignmentStatus(String businessKey, Integer status) {
        ShippingDO contractConsignmentDO = validateContractConsignmentExists(businessKey);
        contractConsignmentDO.setId(businessKey);
        contractConsignmentDO.setStatus(status);
        List<ShippingDetailDO> detailDOS = shippingDetailMapper.selectListByShippingId(businessKey);

        List<String> barCodes = detailDOS.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
        ContractDO contractDO = contractService.getContractById(contractConsignmentDO.getContractId());
        if (PpmAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            contractConsignmentDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());
            //调用pms 出库码
            OutsourcingMaterialSelectDTO dto = new OutsourcingMaterialSelectDTO();
            dto.setContractId(contractConsignmentDO.getContractId());
            dto.setAidMill(contractDO.getPartyName());
            dto.setMaterialCodeList(String.join(",", barCodes));

            CommonResult<String> result=  pmsOrderMaterialRelationApi.outSourceSelectMaterial(dto);
            if (!result.isSuccess()){
                throw exception(CONTRACT_OUT_RELATION_ERROR);
            }
            //调用wms 更新状态
            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
            for (ShippingDetailDO detailDO :detailDOS){

                OrderReqDTO orderReqDTO = new OrderReqDTO();
                orderReqDTO.setOrderNumber(contractConsignmentDO.getNo());
                orderReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT);
                orderReqDTO.setChooseStockId(detailDO.getMaterialStockId());
                orderReqDTO.setOrderStatus(1);
                orderReqDTO.setQuantity(detailDO.getConsignedAmount().intValue());
                orderReqDTOList.add(orderReqDTO);
            }
            CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
            if (!commonResult.isSuccess()){
                throw exception(new ErrorCode(1_010_000_010, "WMS更新状态失败"));
            }

        }


        if (PpmAuditStatusEnum.REJECT.getStatus().equals(status)) {
            contractConsignmentDO.setShippingStatus(ShippingStatusEnum.REJECT.getStatus());
            //TODO  调用WMS作废出 库单
            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
            for (ShippingDetailDO detailDO : detailDOS){

                OrderReqDTO orderReqDTO = new OrderReqDTO();
                orderReqDTO.setOrderNumber(contractConsignmentDO.getNo());
                orderReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT);
                orderReqDTO.setChooseStockId(detailDO.getMaterialStockId());
                orderReqDTO.setOrderStatus(5);
                orderReqDTO.setQuantity(detailDO.getConsignedAmount().intValue());
                orderReqDTOList.add(orderReqDTO);
            }
            CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
            if (!commonResult.isSuccess()){
                throw exception(CONTRACT_CONSIGNMENT_RELATION_ERROR);
            }

        }
        shippingService.updateShippingStatus(contractConsignmentDO);
    }

    @Override
    public void updateContractConsignmentReturnStatus(String businessKey, Integer status) {
        purchaseConsignmentService.updatePurchaseConsignmentStatus(businessKey,status);
    }

    @Override
    public void validateContractConsignment(ContractConsignmentSaveReqVO reqVO) {

//        List<ContractConsignmentDetailDO> detailDOS = contractConsignmentDetailMapper.getContractConsignmentDetailsByContractId(reqVO.getContractId());
//
//        List<String> beforeDetailIds = new ArrayList<>();
//        if (StringUtils.isNotBlank(reqVO.getId())){
//            List<ContractConsignmentDetailDO>   beforeDetails = contractConsignmentDetailMapper.selectListByConsignmentId(reqVO.getId());
//            beforeDetailIds.addAll(beforeDetails.stream().map(ContractConsignmentDetailDO::getId).collect(Collectors.toList()));
//
//        }
//        List<ContractConsignmentDetailDO> chargeDetails = detailDOS.stream().filter(detailDO -> !beforeDetailIds.contains(detailDO.getId())).collect(Collectors.toList());
//
//
//        //  key  条码  value   数量
//        //查找已经发货的条码以及数量
//        Map<String, BigDecimal> barCodeMap = new HashMap<>();
//        if (!org.springframework.util.CollectionUtils.isEmpty(chargeDetails)){
//
//            for (ContractConsignmentDetailDO detailDO :chargeDetails){
//
//                barCodeMap.put(detailDO.getBarCode(), detailDO.getConsignedAmount());
//            }
//        }
//
//        //TODO  退货入库的也加上
//
//        for (ContractConsignmentDetailDO  detailDO: reqVO.getContractConsignmentDetails()){
//            //判断条码数量是否超过限制
//            if (barCodeMap.get(detailDO.getBarCode()) != null){
//                throw exception(new ErrorCode(1_031_000_006, detailDO.getBarCode()+"数量超过限制"));
//            }
//        }


    }

}