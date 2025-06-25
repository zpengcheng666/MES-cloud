package com.miyu.module.ppm.service.shippingreturn;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailDTO;
import com.miyu.module.ppm.convert.shippingreturn.ShippingReturnConvert;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.enums.shippingreturn.ShippingReturnTypeEnum;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.shippingreturn.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingreturn.ShippingReturnDO;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shippingreturn.ShippingReturnMapper;
import com.miyu.module.ppm.dal.mysql.shippingreturndetail.ShippingReturnDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.RETURN_PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_RETURN_DETAIL_NOT_EXISTS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_RETURN_IN_ERROR;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_RETURN_NOT_EXISTS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_RETURN__SUBMIT_FAIL_NOT_DRAFT;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.SHIPPING_RETURN_CLUE_TYPE;
import static com.miyu.module.ppm.enums.LogRecordConstants.SHIPPING_RETURN_SUBMIT_SUB_TYPE;
import static com.miyu.module.ppm.enums.LogRecordConstants.SHIPPING_RETURN_SUBMIT_SUCCESS;

/**
 * 销售退货单 Service 实现类
 *
 * @author miyudmA
 */
@Service
@Validated
public class ShippingReturnServiceImpl implements ShippingReturnService {

    @Resource
    private ConsignmentMapper consignmentMapper;
    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Resource
    private ContractApi contractApi;
    @Resource
    private CompanyApi companyApi;
    @Resource
    private OrderApi orderApi;
    @Resource
    private ShippingService shippingService;
    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;




    private ConsignmentDO validateShippingReturnExists(String id) {

        ConsignmentDO shippingReturnDO = consignmentMapper.selectById(id);
        if (shippingReturnDO == null) {
            throw exception(SHIPPING_RETURN_NOT_EXISTS);
        }
        return shippingReturnDO;
    }


//    /****
//     * 校验退货详情
//     * @param vo
//     */
//    private void validateShippingReturnDetail(ShippingReturnSaveReqVO vo) {
//
//
//
//        //判断 除仅退款外   其他类型的是否存在详情  如果不存在抛异常
//        if (!ShippingReturnTypeEnum.REFUND.getStatus().equals(vo.getReturnType()) && CollectionUtils.isEmpty(vo.getShippingReturnDetails())) {
//            throw exception(SHIPPING_RETURN_DETAIL_NOT_EXISTS);
//        }
//
//
//        //判断详情的产品 是否有其他的退货单
//        //如果是单件的产品  有其他正在执行的退货单 则抛出异常
//        //如果是批量的产品  计算合同下该产品所有退货单总量是否超过发货单的量    超过则抛出异常
//        if (!ShippingReturnTypeEnum.REFUND.getStatus().equals(vo.getReturnType())) {
//            //查询合同下所有有效的发货单
//            List<ShippingDetailDO> shippingDetailDOList = shippingService.getShippingDetailListByProjectId(vo.getProjectId(),vo.getContractId(),1);
//            Map<String, BigDecimal> shippingMaterialMap = new HashMap<>();
//            for (ShippingDetailDO shippingDetailDO : shippingDetailDOList) {
//                if (shippingMaterialMap.get(shippingDetailDO.getBarCode()) == null) {
//                    shippingMaterialMap.put(shippingDetailDO.getBarCode(), shippingDetailDO.getConsignedAmount());
//                } else {
//                    shippingMaterialMap.get(shippingDetailDO.getBarCode()).add(shippingDetailDO.getConsignedAmount());
//                }
//            }
//
//            //查询项目下所有有效的退货单
//            List<ConsignmentDetailDO> shippingReturnDetailDOS = consignmentDetailMapper.getShippingReturnDetails(vo.getProjectId(), Lists.newArrayList(0, 1, 2, 3, 4));
//            List<ShippingReturnDetailDO> shippingReturnDetailDOList = shippingReturnDetailDOS.stream().filter(shippingReturnDetailDO -> !shippingReturnDetailDO.getShippingReturnId().equals(vo.getId())).collect(Collectors.toList());
//            Map<String, BigDecimal> shippingReturnMap = new HashMap<>();
//            if (!CollectionUtils.isEmpty(shippingReturnDetailDOList)) {
//
//                for (ShippingReturnDetailDO shippingReturnDetailDO : shippingReturnDetailDOList) {
//                    if (shippingReturnMap.get(shippingReturnDetailDO.getBarCode()) == null) {
//                        shippingReturnMap.put(shippingReturnDetailDO.getBarCode(), shippingReturnDetailDO.getConsignedAmount());
//                    } else {
//                        shippingReturnMap.get(shippingReturnDetailDO.getBarCode()).add(shippingReturnDetailDO.getConsignedAmount());
//                    }
//                }
//            }
//
//
//            for (ShippingReturnDetailDO detailDO : vo.getShippingReturnDetails()) {
//
//                BigDecimal outBoundNumber = shippingMaterialMap.get(detailDO.getBarCode());
//                BigDecimal inBoundNumber = shippingReturnMap.get(detailDO.getBarCode()) == null ? new BigDecimal(0) : shippingReturnMap.get(detailDO.getBarCode());
//
//                if (inBoundNumber.add(detailDO.getConsignedAmount()).compareTo(outBoundNumber) > 0) {
//                    throw exception(new ErrorCode(1_010_000_012, detailDO.getBarCode() + "总退货数量超过发货数量"));
//                }
//            }
//
//        }
//
//    }



    @Override
    public List<ShippingReturnDTO> getShippingReturnListByContractIds(Collection<String> ids) {
        ArrayList<Integer> list = Lists.newArrayList(2, 3, 4, 5,6,9,10);
        //查询发货单
        QueryWrapper<ConsignmentDO> wrapper = new QueryWrapper<>();
        wrapper.in("contract_id",ids);
        wrapper.in("consignment_status",list);
        List<ConsignmentDO> shippingReturnList = consignmentMapper.selectList(wrapper);

        //合同集合
        List<ContractRespDTO> contractRespDTOS = contractApi.getContractList(ids).getCheckedData();
        Map<String, ContractRespDTO> contractMap = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap(contractRespDTOS, ContractRespDTO::getId);

        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractRespDTO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(companyIds);

        List<ShippingReturnDTO> ShippingReturnList = BeanUtils.toBean(shippingReturnList, ShippingReturnDTO.class, vo -> {
            //给发货单设置合同方(公司名)
            MapUtils.findAndThen(companyMap, contractMap.get(vo.getContractId()).getParty(), a -> vo.setCompanyName(a.getName()));
            List<ConsignmentDetailDO> shippingReturnDetailDOS = consignmentDetailMapper.queryConsignmentDetailIdByConsignmentId(vo.getId());
            List<ShippingReturnDetailDTO> shippingReturnDetailDTOS = BeanUtils.toBean(shippingReturnDetailDOS, ShippingReturnDetailDTO.class);
            vo.setReturnDetailDTOList(shippingReturnDetailDTOS);
        });
        return ShippingReturnList;
    }

    // ==================== 子表（销售退货单详情） ====================



    @Override
    public void updateShippingProcessInstanceStatus(String id, Integer status) {


        ConsignmentDO shippingReturnDO = validateShippingReturnExists(id);
        shippingReturnDO.setId(id);
        shippingReturnDO.setStatus(status);
        if (DMAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            //审核通过后  如果是仅退款的单子  则不需要签收入库 直接完成  否则更改状态为 待签收
            if (ShippingReturnTypeEnum.REFUND.getStatus().equals(shippingReturnDO.getReturnType())) {

                shippingReturnDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
            } else {
                shippingReturnDO.setConsignmentStatus(ConsignmentStatusEnum.SINGING.getStatus());
            }

        } else if (DMAuditStatusEnum.REJECT.getStatus().equals(status)) {
            //审核不通过则需要更改状态为审核失败
            shippingReturnDO.setConsignmentStatus(ConsignmentStatusEnum.CANCEL.getStatus());
        }
        purchaseConsignmentService.updateConsignmentStatus(shippingReturnDO);

    }



    @Override
        public List<ConsignmentDO> getShippingReturnByContract(String contractId,List<Integer> status) {

        MPJLambdaWrapperX<ConsignmentDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ConsignmentDO ::getConsignmentStatus, status)
                .eq(ConsignmentDO::getContractId, contractId);

        return consignmentMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentDO> getShippingReturnByIds(List<String> ids) {



        return consignmentMapper.selectList(ConsignmentDO::getId, ids);
    }

//
//    private void createShippingReturnDetailList(String shippingReturnId, List<ShippingReturnDetailDO> list) {
//        list.forEach(o -> o.setShippingReturnId(shippingReturnId));
//        shippingReturnDetailMapper.insertBatch(list);
//    }


}
