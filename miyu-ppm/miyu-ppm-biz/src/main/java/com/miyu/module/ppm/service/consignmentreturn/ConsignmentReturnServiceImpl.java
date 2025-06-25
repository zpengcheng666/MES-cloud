package com.miyu.module.ppm.service.consignmentreturn;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCancelReqDto;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDetailDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.dal.mysql.consignmentreturndetail.ConsignmentReturnDetailMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.dal.mysql.shipping.ShippingMapper;
import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;
import com.miyu.module.ppm.dal.mysql.shippinginfo.ShippingInfoMapper;
import com.miyu.module.ppm.dal.mysql.warehousedetail.WarehouseDetailMapper;
import com.miyu.module.ppm.enums.consignmentReturn.ConsignmentReturnTypeEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.PpmAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import com.mzt.logapi.context.LogRecordContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

import org.springframework.util.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.consignmentreturn.ConsignmentReturnMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.OUT_WARHOUSE;
import static com.miyu.module.ppm.enums.ApiConstants.PM_RETURN_AUDIT_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 销售退货单 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ConsignmentReturnServiceImpl implements ConsignmentReturnService {

    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private ShippingService shippingService;
    @Resource
    private ShippingInfoMapper shippingInfoMapper;

    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;
    @Autowired
    private ShippingDetailMapper shippingDetailMapper;

    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ContractApi contractApi;

    @Resource
    private CompanyApi companyApi;

    @Resource
    private AdminUserApi userApi;


    @Resource
    private OrderApi orderApi;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;

    private ShippingDO validateConsignmentReturnExists(String id) {

        ShippingDO consignmentReturn = shippingMapper.selectById(id);

        if (consignmentReturn == null) {
            throw exception(CONSIGNMENT_RETURN_NOT_EXISTS);
        }

        return consignmentReturn;
    }


    @Override
    public void updateConsignmentProcessInstanceStatus(String id, Integer status) {

        ShippingDO shippingReturnDO = validateConsignmentReturnExists(id);
        shippingReturnDO.setId(id);
        shippingReturnDO.setStatus(status);
        List<ShippingDetailDO> detailDOS = shippingDetailMapper.selectListByShippingId(id);
        if (PpmAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后  如果是仅退款的单子  则不需要签收入库 直接完成  否则更改状态为 待出库
            if (ConsignmentReturnTypeEnum.REFUND.getStatus().equals(shippingReturnDO.getReturnType())) {

                shippingReturnDO.setShippingStatus(ShippingStatusEnum.FINISH.getStatus());
            } else {
                shippingReturnDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());

            }
//            //审核通过后  退货需要把PMS码还原掉
//            List<String> barCodes = detailDOS.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
//            CommonResult<String> result = pmsOrderMaterialRelationApi.resetRelations(barCodes);
//            if (!result.isSuccess()) {
//                throw exception(PMS_OUTBOUND_CANCEL_ERROR);
//            }
        } else if (PpmAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过则需要更改状态为审核失败
            shippingReturnDO.setShippingStatus(ShippingStatusEnum.REJECT.getStatus());

            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
            for (ShippingDetailDO detailDO : detailDOS) {

                OrderReqDTO orderReqDTO = new OrderReqDTO();
                orderReqDTO.setOrderNumber(shippingReturnDO.getNo());
                orderReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT);
                orderReqDTO.setChooseStockId(detailDO.getMaterialStockId());
                orderReqDTO.setOrderStatus(5);
                orderReqDTO.setQuantity(detailDO.getConsignedAmount().intValue());
                orderReqDTOList.add(orderReqDTO);
            }
            CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
            if (!commonResult.isSuccess()) {
                throw exception(SHIPPING_OUTBOUND_CANCEL_ERROR);
            }
        }
        shippingService.updateShippingStatus(shippingReturnDO);

    }


    @Override
    public List<ConsignmentReturnDTO> getConsignmentReturnDetailByContractIds(Collection<String> ids) {
        ArrayList<Integer> list = Lists.newArrayList(2, 3, 4, 5);
        //通过合同id查询收货
        QueryWrapper<ShippingDO> wrapper = new QueryWrapper<>();
        wrapper.in("contract_id", ids);
        wrapper.in("consignment_status", list);
        List<ShippingDO> consignmentReturnDOS = shippingMapper.selectList(wrapper);

        //合同集合
        List<ContractRespDTO> contractRespDTOS = contractApi.getContractList(ids).getCheckedData();
        Map<String, ContractRespDTO> contractMap = convertMap(contractRespDTOS, ContractRespDTO::getId);

        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractRespDTO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(companyIds);

        List<ConsignmentReturnDTO> consignmentReturnDTOList = BeanUtils.toBean(consignmentReturnDOS, ConsignmentReturnDTO.class, vo -> {
            //给退货单设置合同方(公司名)
            MapUtils.findAndThen(companyMap, contractMap.get(vo.getContractId()).getParty(), a -> vo.setCompanyName(a.getName()));
            List<ShippingDetailDO> consignmentReturnDetailDOS = queryConsignmentReturnDetailById(vo.getId());
            List<ConsignmentReturnDetailDTO> consignmentReturnDetailDTOS = BeanUtils.toBean(consignmentReturnDetailDOS, ConsignmentReturnDetailDTO.class);
            vo.setReturnDetailDTOList(consignmentReturnDetailDTOS);
        });
        return consignmentReturnDTOList;
    }

    /**
     * 查询退货单详细信息
     *
     * @param consignmentReturnId
     * @return
     */
    @Override
    public List<ShippingDetailDO> queryConsignmentReturnDetailById(String consignmentReturnId) {
        return shippingDetailMapper.selectListByShippingId(consignmentReturnId);
    }

    /**
     * 获取合同下的退货单
     *
     * @param contractId
     * @param statusList
     * @return
     */
    @Override
    public List<ShippingDO> getConsignmentReturnByContract(String contractId, List<Integer> statusList,Integer shippingType) {
        return shippingMapper.getShippingByContract(contractId, statusList,shippingType);
    }

    /**
     * 合同主键查询退货单集合
     *
     * @param id
     * @return
     */
    @Override
    public List<ConsignmentReturnDTO> getConsignmentReturnListByContractId(String id) {
        // 获取退货单集合
        List<ShippingDO> consignmentReturnDOS = shippingMapper.getShippingByContract(id, Lists.newArrayList(2, 3, 4, 5),3);
        //用户
        List<Long> userIdList = StringListUtils.stringListToLongList(convertList(consignmentReturnDOS, ShippingDO::getConsigner));
        // 拼接数据 用户信息
        Map<Long, AdminUserRespDTO> userMap;
        if (!org.springframework.util.CollectionUtils.isEmpty(userIdList)) {
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            userMap = userApi.getUserMap(userIdList);
        } else {
            userMap = new HashMap<>();
        }

        // 存在退货单
        if (consignmentReturnDOS.size() > 0) {
            // 获取合同信息
            ContractDO contract = contractMapper.getContractById(id);
            List<ConsignmentReturnDTO> consignmentReturnDTOs = BeanUtils.toBean(consignmentReturnDOS, ConsignmentReturnDTO.class, vo -> {
                //给退货单设置合同方(公司名)
                vo.setPartyName(contract.getPartyName());
                if (StringUtils.isNotBlank(vo.getConsigner())) {
                    MapUtils.findAndThen(userMap, Long.valueOf(vo.getConsigner()), a -> vo.setConsigner(a.getNickname()));
                }
                //通过收货id查收货详情
                List<ShippingDetailDO> returnDetailDOS = shippingDetailMapper.selectListByShippingId(vo.getId());
                vo.setReturnDetailDTOList(BeanUtils.toBean(returnDetailDOS, ConsignmentReturnDetailDTO.class));
            });

            return consignmentReturnDTOs;
        }

        return Collections.emptyList();
    }

    @Override
    public void checkOutBoundInfo() {
        //查询正在出库的发货单信息
//        List<ConsignmentReturnDO> returnDTOS = consignmentReturnMapper.selectList(ConsignmentReturnDO::getConsignmentStatus, ShippingStatusEnum.OUTBOUNDING.getStatus());
//
//        if (!org.springframework.util.CollectionUtils.isEmpty(returnDTOS)) {
//            List<OrderReqDTO> list = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList(returnDTOS, returnDO -> {
//                OrderReqDTO dto = new OrderReqDTO();
//                dto.setOrderNumber(returnDO.getConsignmentReturnNo());
//                dto.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT);
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
//                for (ConsignmentReturnDO returnDO : returnDTOS) {
//                    List<OrderReqDTO> orderReqDTOS = orderMap.get(returnDO.getConsignmentReturnNo());
//                    Boolean finish = true;
//                    List<ConsignmentReturnDetailDO> returnDetailDOS = consignmentReturnDetailMapper.selectList(ConsignmentReturnDetailDO::getConsignmentReturnId,returnDO.getId());
//
//                    if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOS)) {
//
//                        Map<String, OrderReqDTO> orderReqDTOMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(orderReqDTOS, OrderReqDTO::getMaterialStockId);
//
//                        for (ConsignmentReturnDetailDO detailDO : returnDetailDOS) {
//                            OrderReqDTO dto = orderReqDTOMap.get(detailDO.getMaterialStockId());
//                            if (dto == null) {//找不到完成的出库信息  则视为没完成
//                                finish = false;
//                            }else if (dto.getQuantity().intValue() !=detailDO.getConsignedAmount().intValue()){
//                                finish = false; //数量对应不上则视为没完成
//                            }else {
//                                detailDO.setInboundAmount(detailDO.getConsignedAmount());
//                                detailDO.setInboundBy(dto.getOperator());
//                                detailDO.setInboundTime(dto.getOperateTime());
//                                //TODO  后续增加操作人
//                            }
//                        }
//                    } else {
//                        finish = false;
//                    }
//
//                    if (finish){//更新状态为待发货  并且更新出库数量
//                        returnDO.setConsignmentStatus(ShippingStatusEnum.TRANSIT.getStatus());
//
//                        consignmentReturnMapper.updateById(returnDO);
//
//                        consignmentReturnDetailMapper.updateBatch(returnDetailDOS);
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
    public Boolean outBound(ConsignmentReturnDO consignmentReturnDO, List<ConsignmentReturnDetailDO> detailDOS) {


//        List<OrderReqDTO> orderReqDTOList = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList(detailDOS, detailDO ->
//        {
//            OrderReqDTO orderRespVO = new OrderReqDTO();
//            orderRespVO.setOrderNumber(consignmentReturnDO.getConsignmentReturnNo());
//            orderRespVO.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT);
//            orderRespVO.setChooseStockId(detailDO.getMaterialStockId());
//            orderRespVO.setQuantity(detailDO.getConsignedAmount().intValue());
//            //目标仓库
//            orderRespVO.setTargetWarehouseId(OUT_WARHOUSE);
//            return orderRespVO;
//        });
//
//        //TODO 提交审批时调用出库
//        CommonResult<List<String>> result = orderApi.orderDistribute(orderReqDTOList);
//
//        if (result.isSuccess()) {
//            return  true;
//        } else {
//            throw exception(SHIPPING_OUTBOUND_ERROR);
//        }
    return  true;
}

    @Override
    public List<ShippingDetailDO> getReturnByBarCodes(Collection<String> barCodes) {
        return shippingDetailMapper.selectShippingByBarCodes(barCodes);
    }


    /****
     * 校验退货详情
     * @param vo
     */
    private void validateConsignmentReturnDetail(ConsignmentReturnSaveReqVO vo) {

//        //判断 除仅退款外   其他类型的是否存在详情  如果不存在抛异常
//        if (!ConsignmentReturnTypeEnum.REFUND.getStatus().equals(vo.getReturnType()) && CollectionUtils.isEmpty(vo.getConsignmentReturnDetails())) {
//            throw exception(CONSIGNMENT_RETURN_DETAIL_NOT_EXISTS);
//        }
//
//        vo.getConsignmentReturnDetails().forEach(i->{
//
//            //校验退货数量是否超出库存数量
//            if(i.getConsignedAmount()!=null && i.getQuantity()!=null ) {
//                int result = i.getConsignedAmount().compareTo(i.getQuantity());
//                if(result > 0) {
//                    throw exception(CONSIGNMENT_RETURN_DETAIL_QUANTITY);
//                }
//            }
//
//        });



//        //获取合同下已经存在的退货数量信息
//        BigDecimal count = queryConsignmentReturnAmount(vo.getContractId());
//        //查询合同下 所有有效的收货单
//        vo.getConsignmentReturnDetails().forEach(i->{
//            if(i.getConsignedAmount()!=null && !i.getConsignedAmount().equals(BigDecimal.ZERO) && i.getSignedAmount()!=null && !i.getSignedAmount().equals(BigDecimal.ZERO)) {
//                int result = i.getConsignedAmount().compareTo(i.getSignedAmount().subtract(count));
//                if(result > 0 ) {
//                    throw exception(CONSIGNMENT_RETURN_DETAIL_NOT_EXCEEES);
//                }
//            }
//        });

    }

//    /**
//     * 查询所有该合同的退货数量
//     */
//    private BigDecimal queryConsignmentReturnAmount(String contractId){
//        return consignmentReturnDetailMapper.queryConsignmentReturnAmount(contractId) ;
//    }




}
