package com.miyu.module.ppm.api.shipping;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentInfoDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.MaterialStockInRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.*;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.dal.mysql.consignmentinfo.ConsignmentInfoMapper;
import com.miyu.module.ppm.dal.mysql.shipping.ShippingMapper;
import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;
import com.miyu.module.ppm.dal.mysql.shippinginfo.ShippingInfoMapper;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.WarehouseApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.mateiral.dto.WarehouseAreaRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_NOT_EXISTS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_OUTBOUND_ERROR;


@RestController
@Validated
@Slf4j
public class ShippingApiImpl implements ShippingApi {

    @Resource
    private ShippingService shippingService;
    @Resource
    private ShippingInfoMapper shippingInfoMapper;
    @Resource
    private ShippingDetailService shippingDetailService;
    @Resource
    private ContractService contractService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private CompanyService companyService;
    @Resource
    private ShippingDetailMapper shippingDetailMapper;
    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private OrderApi orderApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private WarehouseApi warehouseApi;

    @Override
    public CommonResult<String> updateShippingStatus(String businessKey, Integer status) {

        shippingService.updateShippingProcessInstanceStatus(businessKey, status);

        return CommonResult.success("ok");
    }

    @Override
    public void outBoundSubmit(Long userId, @Valid ShippingOutboundReqDTO outboundReqDTO) {


        ShippingDO shippingDO = shippingService.getShippingByNo(outboundReqDTO.getShippingNo());

        //todo 出库反馈
        if (outboundReqDTO.getStatus().intValue() == 1) {
            shippingDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
        }


    }


    private ShippingDO validateShippingExists(String id) {
        ShippingDO shippingDO = shippingService.getShipping(id);
        if (shippingDO == null) {
            throw exception(SHIPPING_NOT_EXISTS);
        }

        return shippingDO;
    }

    @Override
    public CommonResult<List<ShippingDTO>> getShippingListByContractIds(Collection<String> ids) {
        return CommonResult.success(shippingService.getShippingListByContractIds(ids));
    }

    @Override
    public CommonResult<List<ShippingDetailRetraceDTO>> getShippingListByBarcode(String barCode) {
        return CommonResult.success(shippingService.getShippingListByBarcode(barCode));
    }

    @Override
    public CommonResult<List<ShippingDTO>> getShippingListByProjectIds(Collection<String> ids) {
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        List<ShippingDTO> shippingList = shippingService.getShippingListByProjectIds(ids);
        return CommonResult.success(shippingList);
    }

    @Override
    public CommonResult<List<ShippingDetailDTO>> getShippingDetailListByProjectIds(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return CommonResult.success(Collections.emptyList());
        }
        List<ShippingDetailDTO> shippingDetailList = shippingService.getShippingDetailListByProjectIds(ids);
        return CommonResult.success(shippingDetailList);
    }

    @Override
    public CommonResult<List<ShippingDetailDTO>> getShippingByConsignmentDetailIds(Collection<String> ids) {
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        List<ShippingDetailDTO> ShippingDetailDOList = shippingService.getShippingByConsignmentDetailIds(ids);
        return CommonResult.success(ShippingDetailDOList);
    }

    @Override
    public CommonResult<List<ShippingInfoDTO>> getOutboundingShippingInfo() {

        MPJLambdaWrapperX<ShippingInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ShippingInfoDO::getShippingStatus, Lists.newArrayList(2, 3));
        List<ShippingInfoDO> list = shippingInfoMapper.selectList(wrapperX);
        if (list.isEmpty()) {
            return CommonResult.success(Collections.emptyList());
        }
        //主单信息
        List<String> shippingIds = list.stream().map(ShippingInfoDO::getShippingId).distinct().collect(Collectors.toList());

        List<ShippingDO> shippingDOS = shippingService.getShippings(shippingIds);

        Map<String, ShippingDO> shippingDOMap = CollectionUtils.convertMap(shippingDOS, ShippingDO::getId);
        //合同信息
        List<String> contractIds = list.stream().map(ShippingInfoDO::getContractId).distinct().collect(Collectors.toList());

        List<ContractDO> contractDOS = contractService.getContractListByIds(contractIds);
        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);

        List<String> companyIds = contractDOS.stream().map(ContractDO::getParty).distinct().collect(Collectors.toList());

        List<CompanyDO> companyDOS = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyDOMap = CollectionUtils.convertMap(companyDOS, CompanyDO::getId);
        //项目信息
        List<String> projectIds = list.stream().map(ShippingInfoDO::getProjectId).distinct().collect(Collectors.toList());
        Map<String, PmsApprovalDto> map = new HashMap<>();
        try {
            map = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {

        }
        //物料信息
        List<String> materialConfigIds = list.stream().map(ShippingInfoDO::getMaterialConfigId).distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> configRespDTOMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        List<ShippingInfoDTO> respVOS = new ArrayList<>();
        List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
        for (ShippingInfoDO infoDO : list) {
            ShippingDO shippingDO = shippingDOMap.get(infoDO.getShippingId());
            OrderReqDTO reqDTO = new OrderReqDTO();
            reqDTO.setOrderNumber(shippingDO.getNo());
            Integer type = DictConstants.WMS_ORDER_TYPE_SALE_OUT;
            if (shippingDO.getShippingType().equals(ShippingTypeEnum.SHIPPING.getStatus())) {
                type = DictConstants.WMS_ORDER_TYPE_SALE_OUT;
            } else if (shippingDO.getShippingType().equals(ShippingTypeEnum.OUTSOURCING.getStatus())) {
                type = DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT;
            } else if (shippingDO.getShippingType().equals(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus())) {
                type = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT;
            } else if (shippingDO.getShippingType().equals(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus())) {
                type = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT;
            }
            reqDTO.setOrderType(type);
            orderReqDTOList.add(reqDTO);
        }
        CommonResult<List<OrderReqDTO>> commonResult = orderApi.orderList(orderReqDTOList);

        List<OrderReqDTO> resps = new ArrayList<>();

        if (commonResult.isSuccess()) {
            resps = commonResult.getCheckedData();
        }

        Map<String, List<OrderReqDTO>> listMap = new HashMap<>();
        if (!CollectionUtils.isAnyEmpty(resps)) {
            for (OrderReqDTO dto : resps) {
                if (CollectionUtils.isAnyEmpty(listMap.get(dto.getOrderNumber()))) {
                    listMap.put(dto.getOrderNumber(), Lists.newArrayList(dto));
                } else {
                    listMap.get(dto.getOrderNumber()).add(dto);
                }
            }
        }

        for (ShippingInfoDO infoDO : list) {

            ShippingInfoDTO respVO = BeanUtils.toBean(infoDO, ShippingInfoDTO.class);
            ShippingDO shippingDO = shippingDOMap.get(infoDO.getShippingId());
            ContractDO contractDO = contractDOMap.get(infoDO.getContractId());
            PmsApprovalDto pmsApprovalDto = map.get(infoDO.getProjectId());
            MaterialConfigRespDTO a = configRespDTOMap.get(infoDO.getMaterialConfigId());

            if (shippingDO != null) {
                respVO.setNo(shippingDO.getNo()).setName(shippingDO.getName());
                List<OrderReqDTO> reqDTOList = listMap.get(shippingDO.getNo());
                if (!CollectionUtils.isAnyEmpty(reqDTOList)) {
                    List<OrderReqDTO> reqDTOS = reqDTOList.stream().filter(orderReqDTO -> orderReqDTO.getMaterialConfigId().equals(infoDO.getMaterialConfigId())).collect(Collectors.toList());
                    if (!CollectionUtils.isAnyEmpty(reqDTOS)) {
                        List<Integer> integers = Lists.newArrayList(4, 5);
                        List<OrderReqDTO> finalDTOS = reqDTOS.stream().filter(orderReqDTO -> !integers.contains(orderReqDTO.getOrderStatus())).collect(Collectors.toList());
                        if (!CollectionUtils.isAnyEmpty(finalDTOS)) {
                            respVO.setOutStatus(2);
                        }
                    }
                }
            }
            respVO.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialConfigId(a.getId());

            if (contractDO != null) {
                respVO.setContractCode(contractDO.getNumber()).setContractName(contractDO.getName());

                CompanyDO companyDO = companyDOMap.get(contractDO.getParty());
                respVO.setCompanyName(companyDO.getName());
                respVO.setCompanyId(companyDO.getId());
            }
            if (pmsApprovalDto != null) {
                respVO.setProjectName(pmsApprovalDto.getProjectName()).setProjectCode(pmsApprovalDto.getProjectCode());
            }
            respVOS.add(respVO);
        }

        return CommonResult.success(respVOS);
    }

    @Override
    public CommonResult<List<ShippingDetailDTO>> getShippingDetailByShippingInfoId(String shippingInfoId) {

        List<ShippingDetailDO> detailDOS = shippingDetailService.getDetailsById(null, shippingInfoId);

        List<String> materialConfigIds = detailDOS.stream().map(ShippingDetailDO::getMaterialConfigId).distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> configRespDTOMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);


        List<ShippingDetailDTO> respVOS = new ArrayList<>();

        for (ShippingDetailDO detailDO : detailDOS) {
            ShippingDetailDTO respVO = BeanUtils.toBean(detailDO, ShippingDetailDTO.class);

            MaterialConfigRespDTO a = configRespDTOMap.get(detailDO.getMaterialConfigId());
            respVO.setMaterialName(a.getMaterialName())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialUnit(a.getMaterialUnit());
            respVOS.add(respVO);
        }
        return CommonResult.success(respVOS);
    }

    @Override
    public CommonResult<String> signMaterial(ShippingOutDTO shippingOutDTO) {


        String userId = getLoginUserId().toString();
        //获取所有条码
        List<ShippingDetailDO> detailDOS = shippingDetailService.getDetailsById(null, shippingOutDTO.getShippingInfoId());
        Boolean isFinish = true;
        BigDecimal number = new BigDecimal(0);
        for (ShippingDetailDO detailDO : detailDOS) {
            if (shippingOutDTO.getBarCodes().contains(detailDO.getBarCode())) {
                detailDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
                number = number.add(new BigDecimal(1));

                detailDO.setOutboundAmount(new BigDecimal(1));
                detailDO.setOutboundBy(userId);
                detailDO.setOutboundTime(LocalDateTime.now());

            } else {
                if (!detailDO.getShippingStatus().equals(ShippingStatusEnum.SEND.getStatus())) {
                    isFinish = false;
                } else {
                    number = number.add(new BigDecimal(1));
                }
            }
        }
        shippingDetailMapper.updateBatch(detailDOS);
        ShippingInfoDO shippingInfoDO = shippingInfoMapper.selectById(shippingOutDTO.getShippingInfoId());
        if (isFinish) {
            shippingInfoDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
        } else {
            shippingInfoDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());
        }
        shippingInfoDO.setOutboundAmount(number);
        shippingInfoDO.setOutboundBy(userId);
        shippingInfoDO.setOutboundTime(LocalDateTime.now());
        shippingInfoMapper.updateById(shippingInfoDO);


        List<ShippingInfoDO> shippingInfoDOS = shippingInfoMapper.selectList(ShippingInfoDO::getShippingId, shippingInfoDO.getShippingId());
        ShippingDO shippingDO = shippingService.getShipping(shippingInfoDO.getShippingId());
        Boolean flag = true;
        for (ShippingInfoDO infoDO : shippingInfoDOS) {
            if (infoDO.getShippingStatus().equals(ShippingStatusEnum.OUTBOUNDING.getStatus())) {
                shippingDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());
                flag = false;
            }

            if (flag) {
                if (infoDO.getShippingStatus().equals(ShippingStatusEnum.SEND.getStatus())) {
                    shippingDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
                }
            }
        }
        shippingMapper.updateById(shippingDO);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult<String> generatorOutBound(ShippingOutDTO shippingOutDTO) {

        if (StringUtils.isEmpty(shippingOutDTO.getLocationId())){
            return CommonResult.error(1_010_002_013,"位置信息不能为空");
        }
        if (StringUtils.isEmpty(shippingOutDTO.getShippingInfoId())){
            return CommonResult.error(1_010_002_014,"出库单不能为空");
        }
        ShippingInfoDO shippingInfoDO = shippingInfoMapper.selectById(shippingOutDTO.getShippingInfoId());
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectList(ShippingDetailDO::getInfoId, shippingOutDTO.getShippingInfoId());

        List<ShippingDO> shippingDOS = shippingMapper.getShippingByShippingInfoId(shippingOutDTO.getShippingInfoId());
        ShippingDO shippingDO = shippingDOS.get(0);
        List<String> barCodes = shippingDetailDOS.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
        CommonResult<List<MaterialStockRespDTO>> commonResult = null;
        try {
            commonResult = materialStockApi.getMaterialAtLocationByBarCodes(barCodes);
        } catch (Exception e) {
            log.error("调用MES失败");
        }
        List<MaterialStockRespDTO> materialStockRespDTOS = new ArrayList<>();

        if (commonResult.isSuccess()) {
            materialStockRespDTOS = commonResult.getCheckedData();
        }
        Map<String, MaterialStockRespDTO> stockRespDTOMap = CollectionUtils.convertMap(materialStockRespDTOS, MaterialStockRespDTO::getBarCode);
        List<String> locationIds = materialStockRespDTOS.stream().map(MaterialStockRespDTO::getAtLocationId).distinct().collect(Collectors.toList());
        locationIds.add(shippingOutDTO.getLocationId());
        CommonResult<List<WarehouseAreaRespDTO>> commonResult1 = null;
        try {
            commonResult1 = warehouseApi.getWarehouseAreaByLocationIds(locationIds);
        } catch (Exception e) {
            log.error("调用MES失败");
        }
        Map<String, WarehouseAreaRespDTO> map = new HashMap<>();
        if (commonResult1.isSuccess()) {
            map = CollectionUtils.convertMap(commonResult1.getCheckedData(), WarehouseAreaRespDTO::getJoinLocationId);
        }

        Integer type = DictConstants.WMS_ORDER_TYPE_SALE_OUT;
        if (shippingDO.getShippingType().equals(ShippingTypeEnum.SHIPPING.getStatus())) {
            type = DictConstants.WMS_ORDER_TYPE_SALE_OUT;
        } else if (shippingDO.getShippingType().equals(ShippingTypeEnum.OUTSOURCING.getStatus())) {
            type = DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT;
        } else if (shippingDO.getShippingType().equals(ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus())) {
            type = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT;
        } else if (shippingDO.getShippingType().equals(ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus())) {
            type = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT;
        }
        Map<String, WarehouseAreaRespDTO> finalMap = map;
        WarehouseAreaRespDTO areaRespDTO = finalMap.get(shippingOutDTO.getLocationId());
        Integer finalType = type;
        List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
        for (ShippingDetailDO detailDO : shippingDetailDOS) {

            MaterialStockRespDTO respDTO = stockRespDTOMap.get(detailDO.getBarCode());
            //判断  物料所在位置 是否和出库位置在同一仓库   如果是  不需要调用WMS 生成出库单
            if (respDTO != null && areaRespDTO !=null) {
                WarehouseAreaRespDTO respDTO1 = finalMap.get(respDTO.getAtLocationId());
                if (respDTO1.getWarehouseId().equals(areaRespDTO.getWarehouseId())) {
                    continue;
                }
            }
            OrderReqDTO orderRespVO = new OrderReqDTO();
            orderRespVO.setOrderNumber(shippingDO.getNo());
            orderRespVO.setOrderType(finalType);
            orderRespVO.setOrderStatus(1);
            orderRespVO.setChooseStockId(detailDO.getMaterialStockId());
            orderRespVO.setQuantity(detailDO.getConsignedAmount().intValue());
            //目标仓库
            orderRespVO.setTargetWarehouseId(areaRespDTO.getWarehouseId());
            orderReqDTOList.add(orderRespVO);
        }
        if(!CollectionUtils.isAnyEmpty(orderReqDTOList)){
            CommonResult<List<String>> result = orderApi.orderDistribute(orderReqDTOList);

            if (result.isSuccess()) {
                shippingInfoDO.setOutStatus(2);
            } else {
                throw exception(SHIPPING_OUTBOUND_ERROR);
            }
        }else {
            shippingInfoDO.setOutStatus(2);

        }
        shippingInfoMapper.updateById(shippingInfoDO);
        return CommonResult.success("出库单生成成功");
    }
}
