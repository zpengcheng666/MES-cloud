package com.miyu.module.ppm.api.purchaseConsignment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.*;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.ConsignmentInfoRespVO;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.ConsignmentInfoSaveReqVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.PurchaseConsignmentController;
import com.miyu.module.ppm.controller.admin.warehousedetail.vo.WarehouseDetailRespVO;
import com.miyu.module.ppm.convert.purchaseConsignment.PurchaseConsignmentConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.dal.mysql.consignmentinfo.ConsignmentInfoMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;
import com.miyu.module.ppm.dal.mysql.purchaserequirement.PurchaseRequirementDetailMapper;
import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.consignmentinfo.ConsignmentInfoService;
import com.miyu.module.ppm.service.contract.ContractOrderService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentService;
import com.miyu.module.ppm.service.purchaseconsignment.PurchaseConsignmentServiceImpl;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;
import com.miyu.module.ppm.service.warehousedetail.WarehouseDetailService;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.CONSIGNMENT_DETAIL_NOT_EXISTS;


@RestController
@Validated
public class PurchaseConsignmentApiImpl implements PurchaseConsignmentApi {
    private static Logger logger = LoggerFactory.getLogger(PurchaseConsignmentApi.class);

    @Resource
    private PurchaseConsignmentService purchaseConsignmentService;
    @Autowired
    private ConsignmentInfoService consignmentInfoService;
    @Autowired
    private PurchaseConsignmentServiceImpl purchaseConsignmentServiceImpl;
    @Resource
    private WarehouseDetailService warehouseDetailService;
    @Autowired
    private ContractOrderService contractOrderService;
    @Autowired
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;
    @Resource
    private ContractService contractService;
    @Resource
    private CompanyProductService companyProductService;
    @Resource
    private ConsignmentInfoMapper consignmentInfoMapper;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private CompanyService companyService;

    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;
    @Resource
    private ConsignmentMapper consignmentMapper;
    @Resource
    private ShippingDetailMapper shippingDetailMapper;
    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private PurchaseRequirementDetailMapper requirementDetailMapper;
    @Resource
    private ContractOrderMapper orderMapper;

    @Resource
    private OrderApi orderApi;
    /**
     * 更新审批状态
     *
     * @param businessKey
     * @param status
     * @return
     */
    @Override
    public CommonResult<String> updatePurchaseConsignmentStatus(String businessKey, Integer status) {

        purchaseConsignmentService.updatePurchaseConsignmentStatus(businessKey, status);
        return CommonResult.success("ok");

    }

    /**
     * 入库反馈
     *
     * @param id
     */
    @Override
    public void InBoundSubmit(String id) {
//        PurchaseConsignmentDO purchaseConsignmentDO = purchaseConsignmentService.getPurchaseConsignment(id);
//        if (purchaseConsignmentDO.getStatus().intValue() == 1 && purchaseConsignmentDO.getConsignmentStatus().equals(ConsignmentStatusEnum.SETBOUND.getStatus())){
//            purchaseConsignmentDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
//            purchaseConsignmentServiceImpl.updatePurchaseConsignment(BeanUtils.toBean(purchaseConsignmentDO, PurchaseConsignmentSaveReqVO.class));
//        }

    }

    /**
     * 根据采购单号ID筛选免检产品
     *
     * @param id
     * @return
     */
    @Override
    public CommonResult<ConsignmentWarehouseDTO> queryConsignmentWarehouseById(String id) {
        //获取收货单信息
        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(id);
        ConsignmentWarehouseDTO consignmentWarehouseDTO = BeanUtils.toBean(consignmentDO, ConsignmentWarehouseDTO.class);
        //获取收货单明细信息
        List<ConsignmentDetailDO> consignmentDetailDO = purchaseConsignmentDetailService.queryConsignmentDetailIdByConsignmentId(id);
        //合同订单Id集合
        List<String> orderIds = consignmentDetailDO.stream().map(ConsignmentDetailDO::getOrderId).collect(Collectors.toList());
        //查询合同订单下相关产品
        List<ContractOrderRespDTO> contractOrderDO = contractOrderService.queryContractOrderByIds(orderIds);
        //获取产品信息
        List<String> MaterialIds = contractOrderDO.stream().map(ContractOrderRespDTO::getMaterialId).collect(Collectors.toList());
        //查询合同对应供应商
        ContractDO contractDO = contractService.getContract(consignmentDO.getContractId());
        //查询产品是否免检
        List<CompanyProductDO> companyProductDOS = companyProductService.queryCompanyProductByParty(contractDO.getParty(), MaterialIds);
        Map<String, CompanyProductDO> productMap = CollectionUtils.convertMap(companyProductDOS, CompanyProductDO::getMaterialId);
        //查询收货单对应的一物一码信息
        List<WarehouseDetailDO> warehouseDetailDOS = warehouseDetailService.queryWareHouseListByConsignmentReturnNo(consignmentDO.getNo());

        List<WarehouseDetailRespVO> list = PurchaseConsignmentConvert.INSTANCE.queryWarehouseCheck(warehouseDetailDOS, productMap);
        List<WarehouseRespDTO> dtoList = BeanUtils.toBean(list, WarehouseRespDTO.class);
        consignmentWarehouseDTO.setPurchaseConsignmentDetails(dtoList);

        return CommonResult.success(consignmentWarehouseDTO);

    }


    /**
     * 校验采购明细是否存在
     *
     * @param id
     * @return
     */
    private ConsignmentDO validatePurchaseConsignmentExists(String id) {
        ConsignmentDO consignmentDO = purchaseConsignmentService.getPurchaseConsignment(id);
        if (consignmentDO == null) {
            throw exception(CONSIGNMENT_DETAIL_NOT_EXISTS);
        }

        return consignmentDO;
    }

    /**
     * 查询采购详情
     *
     * @param ids
     * @return
     */
    @Override
    public CommonResult<List<PurchaseConsignmentDTO>> getConsignmentDetailByContractIds(Collection<String> ids) {
        return CommonResult.success(purchaseConsignmentService.getConsignmentDetailByContractIds(ids));
    }

    /**
     * 同步入库明细表至PPM
     *
     * @param warehouseRespDTO
     */
    @Override
    public void addWarehouseDetail(List<WarehouseRespDTO> warehouseRespDTO) {
        warehouseDetailService.addWarehouseDetail(BeanUtils.toBean(warehouseRespDTO, WarehouseDetailDO.class));
    }

    @Override
    public CommonResult<List<PurchaseConsignmentDTO>> getPurchaseListByProjectIds(Collection<String> ids) {
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        List<PurchaseConsignmentDTO> consignmentList = purchaseConsignmentService.getConsignmentListByProjectIds(ids);
        return CommonResult.success(consignmentList);
    }

    @Override
    public CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByProjectIds(Collection<String> ids) {
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        List<ConsignmentDetailDO> consignmentDetailList = purchaseConsignmentService.getConsignmentDetailListByProjectIds(ids);
        List<PurchaseConsignmentDetailDTO> consignmentDetailDTOS = BeanUtils.toBean(consignmentDetailList, PurchaseConsignmentDetailDTO.class);
        return CommonResult.success(consignmentDetailDTOS);
    }

    @Override
    public CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByShippingIds(Collection<String> ids) {
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        List<PurchaseConsignmentDetailDTO> purchaseDetailList = purchaseConsignmentService.getPurchaseDetailListByShippingIds(ids);
        return CommonResult.success(purchaseDetailList);
    }

    @Override
    public CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByContractOrderIds(Collection<String> ids) {
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        List<ConsignmentDetailDO> purchaseDetailList = purchaseConsignmentService.getPurchaseDetailListByContractOrderIds(ids);
        List<PurchaseConsignmentDetailDTO> consignmentDetailDTOS = BeanUtils.toBean(purchaseDetailList, PurchaseConsignmentDetailDTO.class);
        return CommonResult.success(consignmentDetailDTOS);
    }

    @Override
    public CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByPurchaseIds(Collection<String> ids) {
        //采购计划id,采购明细
        //Map<String,List<PurchaseConsignmentDetailDTO>> map = new HashMap<>();
        if (CollectionUtils.isAnyEmpty(ids)) {
            return CommonResult.success(Collections.emptyList());
        }
        //先查询需求计划id
        LambdaQueryWrapper<PurchaseRequirementDetailDO> requireWrapper = new LambdaQueryWrapper<>();
        requireWrapper.in(PurchaseRequirementDetailDO::getSourceId,ids);
        List<PurchaseRequirementDetailDO> purchaseRequirementDetailList = requirementDetailMapper.selectList(requireWrapper);
        if(purchaseRequirementDetailList.size()==0){
            return CommonResult.success(Collections.emptyList());
        }
        //需求id,采购id
        Map<String,String> rpMap = new HashMap<>();
        for (PurchaseRequirementDetailDO purchaseRequirementDetailDO : purchaseRequirementDetailList) {
            rpMap.put(purchaseRequirementDetailDO.getId(),purchaseRequirementDetailDO.getSourceId());
        }
        //根据需求计划查合同id
        List<String> requireIds = purchaseRequirementDetailList.stream().map(PurchaseRequirementDetailDO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<ContractOrderDO> orderWrapper = new LambdaQueryWrapper<>();
        orderWrapper.in(ContractOrderDO::getRequirementDetailId,requireIds);
        List<ContractOrderDO> contractOrderDOS = orderMapper.selectList(orderWrapper);
        if(contractOrderDOS.size()==0){
            return CommonResult.success(Collections.emptyList());
        }
        //合同id,需求id
        Map<String,String> orMap = new HashMap<>();
        for (ContractOrderDO contractOrderDO : contractOrderDOS) {
            orMap.put(contractOrderDO.getId(),contractOrderDO.getRequirementDetailId());
        }
        //根据合同id查收货,
        List<String> orderIds = contractOrderDOS.stream().map(ContractOrderDO::getId).collect(Collectors.toList());
        LambdaQueryWrapper<ConsignmentDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(ConsignmentDetailDO::getOrderId,orderIds);
        List<ConsignmentDetailDO> consignmentDetailList = consignmentDetailMapper.selectList(wrapper);
        List<PurchaseConsignmentDetailDTO> consignmentDetailDTOS = BeanUtils.toBean(consignmentDetailList, PurchaseConsignmentDetailDTO.class);
        for (PurchaseConsignmentDetailDTO consignmentDetailDO : consignmentDetailDTOS) {
            //合同订单id
            String orderId = consignmentDetailDO.getOrderId();
            //不为空，就获取需求id
            if(orMap.containsKey(orderId)){
                String requireId = orMap.get(orderId);
                //不为空，就获取采购计划id
                if(rpMap.containsKey(requireId)){
                    String purchaseId = rpMap.get(requireId);
                    consignmentDetailDO.setPurchaseId(purchaseId);
                }
            }
        }
        return CommonResult.success(consignmentDetailDTOS);
    }

    @Override
    public CommonResult<List<ConsignmentInfoDTO>> getSignConsignmentInfo() {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.in(ConsignmentInfoDO::getConsignmentStatus, Lists.newArrayList(2, 9,10));
        List<ConsignmentInfoDO> list = consignmentInfoMapper.selectList(wrapperX);
        //主单信息
        List<String> consignmentIds = list.stream().map(ConsignmentInfoDO::getConsignmentId).distinct().collect(Collectors.toList());

        if(consignmentIds.isEmpty()){
            return CommonResult.success(Collections.emptyList());
        }

        List<ConsignmentDO> consignmentDOS = purchaseConsignmentService.getConsignmentDetailByIds(consignmentIds);

        Map<String, ConsignmentDO> consignmentDOMap = CollectionUtils.convertMap(consignmentDOS, ConsignmentDO::getId);
        //合同信息
        List<String> contractIds = list.stream().map(ConsignmentInfoDO::getContractId).distinct().collect(Collectors.toList());

        List<ContractDO> contractDOS = contractService.getContractListByIds(contractIds);
        Map<String, ContractDO> contractDOMap = CollectionUtils.convertMap(contractDOS, ContractDO::getId);

        List<String> companyIds = contractDOS.stream().map(ContractDO::getParty).distinct().collect(Collectors.toList());

        List<CompanyDO> companyDOS = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyDOMap = CollectionUtils.convertMap(companyDOS, CompanyDO::getId);
        //项目信息
        List<String> projectIds = list.stream().map(ConsignmentInfoDO::getProjectId).distinct().collect(Collectors.toList());
        Map<String, PmsApprovalDto> map = new HashMap<>();
        try {
            map = pmsApi.getApprovalMap(projectIds);
        } catch (Exception e) {

        }
        //物料信息
        List<String> materialConfigIds = list.stream().map(ConsignmentInfoDO::getMaterialConfigId).distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> configRespDTOMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        List<ConsignmentInfoDTO> respVOS = new ArrayList<>();

        for (ConsignmentInfoDO infoDO : list) {
            ConsignmentInfoDTO respVO = BeanUtils.toBean(infoDO, ConsignmentInfoDTO.class);
            ConsignmentDO consignmentDO = consignmentDOMap.get(infoDO.getConsignmentId());
            ContractDO contractDO = contractDOMap.get(infoDO.getContractId());
            PmsApprovalDto pmsApprovalDto = map.get(infoDO.getProjectId());
            MaterialConfigRespDTO a = configRespDTOMap.get(infoDO.getMaterialConfigId());

            respVO.setNo(consignmentDO.getNo()).setName(consignmentDO.getName());
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
    public CommonResult<List<PurchaseConsignmentDetailDTO>> getSignConsignmentDetail(String consignmentInfoId) {

        List<ConsignmentDetailDO> detailDOS =  purchaseConsignmentDetailService.getDetailListByInfoId(consignmentInfoId);

        List<String> materialConfigIds = detailDOS.stream().map(ConsignmentDetailDO::getMaterialConfigId).distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> configRespDTOMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);


        List<PurchaseConsignmentDetailDTO> respVOS = new ArrayList<>();

        for (ConsignmentDetailDO detailDO : detailDOS) {
            PurchaseConsignmentDetailDTO respVO = BeanUtils.toBean(detailDO, PurchaseConsignmentDetailDTO.class);

            MaterialConfigRespDTO a = configRespDTOMap.get(detailDO.getMaterialConfigId());
            respVO.setMaterialName(a.getMaterialName())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialUnit(a.getMaterialUnit());

            respVOS.add(respVO);
        }
        return CommonResult.success(respVOS);
    }

    @Override
    public CommonResult<String> signNumber(ConsignmentSignDTO consignmentSignDTO) {
        if(consignmentSignDTO.getNumber() == null || consignmentSignDTO.getNumber().intValue()==0){
            return CommonResult.error(1_010_002_010,"数量不能为空");
        }
        if (StringUtils.isEmpty(consignmentSignDTO.getLocationId())){
            return CommonResult.error(1_010_002_011,"位置信息不能为空");
        }
        ConsignmentInfoSaveReqVO consignmentInfoSaveReqVO = new ConsignmentInfoSaveReqVO();
        consignmentInfoSaveReqVO.setId(consignmentSignDTO.getConsignmentInfoId());
        consignmentInfoSaveReqVO.setSignedAmount(new BigDecimal(consignmentSignDTO.getNumber()));
        consignmentInfoSaveReqVO.setLocationId(consignmentSignDTO.getLocationId());
        consignmentInfoService.signInfo(consignmentInfoSaveReqVO);
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult<String> signMaterial(ConsignmentSignDTO consignmentSignDTO) {
        if(CollectionUtils.isAnyEmpty(consignmentSignDTO.getBarCodes())){
            return CommonResult.error(1_010_002_011,"请选择条码");
        }
        if (StringUtils.isEmpty(consignmentSignDTO.getLocationId())){
            return CommonResult.error(1_010_002_011,"位置信息不能为空");
        }
        List<ConsignmentDetailDO> detailDOS =  purchaseConsignmentDetailService.getDetailListByInfoId(consignmentSignDTO.getConsignmentInfoId());

        List<ConsignmentDetailDO> finalDetails = detailDOS.stream().filter(consignmentDetailDO -> consignmentSignDTO.getBarCodes().contains(consignmentDetailDO.getBarCode())).collect(Collectors.toList());

        List<String> ids = finalDetails.stream().map(ConsignmentDetailDO::getId).collect(Collectors.toList());
        ConsignmentInfoSaveReqVO consignmentInfoSaveReqVO = new ConsignmentInfoSaveReqVO();
        consignmentInfoSaveReqVO.setId(consignmentSignDTO.getConsignmentInfoId());
        consignmentInfoSaveReqVO.setIds(ids);
        consignmentInfoSaveReqVO.setLocationId(consignmentSignDTO.getLocationId());
        consignmentInfoService.signMaterial(consignmentInfoSaveReqVO);

        return CommonResult.success("成功");
    }

    @Override
    public CommonResult<String> returnConsignment(ConsignmentSignDTO consignmentSignDTO) {

        ConsignmentInfoDO consignmentInfoDO = consignmentInfoMapper.selectById(consignmentSignDTO.getConsignmentInfoId());

        List<ConsignmentDetailDO> detailDOS =  purchaseConsignmentDetailService.getDetailListByInfoId(consignmentSignDTO.getConsignmentInfoId());

        consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());

        detailDOS.forEach(consignmentDetailDO -> {
            if(ConsignmentStatusEnum.RETURN.getStatus().equals(consignmentDetailDO.getConsignmentStatus())){
                consignmentDetailDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
            }
        });
        consignmentInfoMapper.updateById(consignmentInfoDO);
        consignmentDetailMapper.updateBatch(detailDOS);

        ConsignmentDO consignmentDO = consignmentMapper.selectById(consignmentInfoDO.getConsignmentId());


        //3.查看整单状态
        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoMapper.selectListByConsignmentId(consignmentInfoDO.getConsignmentId());

        Boolean isFinish = true;
        Boolean isCancel = true;
        for (ConsignmentInfoDO infoDO : consignmentInfoDOS) {
            //如果完成 更新主单状态
            if (!infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.FINISH.getStatus()) && !infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.REJECT.getStatus())) {
                isFinish = false;
            }

            if (!infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.REJECT.getStatus())) {
                isCancel = false;
            }
        }
        if (isFinish && !isCancel) {  //如果都是作废 不更状态
            consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
            consignmentMapper.updateById(consignmentDO);
        }
        if (isCancel){
            consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
            consignmentMapper.updateById(consignmentDO);
        }
        return CommonResult.success("成功");
    }

    @Override
    public CommonResult<List<MaterialStockInRespDTO>> getStockForIn(String locationId) {

        //todo
        CommonResult<List<MaterialStockRespDTO>> commonResult = materialStockApi.getMaterialsByLocationId(locationId);
        if (!commonResult.isSuccess()){
            logger.error("调用WMS失败");
            return  CommonResult.success(null);
        }
        List<MaterialStockRespDTO> stockRespDTOS = commonResult.getCheckedData();
        if (CollectionUtils.isAnyEmpty(stockRespDTOS)){
            return  CommonResult.success(null);
        }

        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingDO.class,ShippingDO::getId,ShippingDetailDO::getShippingId)
                .selectAs(ShippingDO ::getNo,ShippingDetailDO ::getNo)
                .selectAs(ShippingDO ::getName,ShippingDetailDO ::getName)
                .selectAll(ShippingDetailDO.class);
        wrapperX.in(ShippingDetailDO::getShippingStatus, Lists.newArrayList(2, 3,4));
        List<ShippingDetailDO> shippingDetailDOS =shippingDetailMapper.selectList(wrapperX);
        Map<String,ShippingDetailDO> shippingDetailDOMap = convertMap(shippingDetailDOS,ShippingDetailDO::getBarCode);
        //List<String> barcodes = shippingDetailDOS.stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
        List<MaterialStockInRespDTO> respDTOS = new ArrayList<>();
        List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
        for (MaterialStockRespDTO stockRespDTO :stockRespDTOS){

            ShippingDetailDO detailDO =  shippingDetailDOMap.get(stockRespDTO.getBarCode());
            if (detailDO == null){
                MaterialStockInRespDTO respDTO = BeanUtils.toBean(stockRespDTO,MaterialStockInRespDTO.class);
                List<ConsignmentDO> consignmentDOS = consignmentDetailMapper.getConsignmentByBarCode(stockRespDTO.getBarCode());
                respDTO.setStatus(0);
                if (!CollectionUtils.isAnyEmpty(consignmentDOS)){
                    ConsignmentDO consignmentDO = consignmentDOS.get(0);
                    respDTO.setConsignmentType(consignmentDO.getConsignmentType());
                    respDTO.setNo(consignmentDO.getNo());
                    OrderReqDTO reqDTO  = new OrderReqDTO();
                    reqDTO.setOrderNumber(consignmentDO.getNo());

                    Integer type= DictConstants.WMS_ORDER_TYPE_PURCHASE_IN;
                    if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.PURCHASE.getStatus())){
                        type = DictConstants.WMS_ORDER_TYPE_PURCHASE_IN;
                    }else if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.OUT.getStatus())){
                        type = DictConstants.WMS_ORDER_TYPE_OUTSOURCE_IN;
                    }else if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.OUT_MATERIAL.getStatus())){
                        type = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_MATERIAL_IN;
                    }else if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.ORDER.getStatus())){
                        type = DictConstants.WMS_ORDER_TYPE_MATERIAL_IN;
                    } else if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.SHIPPING_RETURN.getStatus())){
                        type = DictConstants.WMS_ORDER_TYPE_OTHER_IN;
                    }
                    reqDTO.setOrderType(type);
                    respDTO.setOrderType(type);
                    orderReqDTOList.add(reqDTO);
                }
                respDTOS.add(respDTO);
            }else {
                MaterialStockInRespDTO respDTO = BeanUtils.toBean(stockRespDTO,MaterialStockInRespDTO.class);
                respDTO.setStatus(9);//待出库
                respDTO.setConsignmentType(detailDO.getShippingType());
                respDTO.setNo(detailDO.getNo());
            }

        }




        if (!CollectionUtils.isAnyEmpty(orderReqDTOList)){
            CommonResult<List<OrderReqDTO>> result =  orderApi.orderList(orderReqDTOList);
            List<OrderReqDTO> resps = new ArrayList<>();

            if (result.isSuccess()){
                resps = result.getCheckedData();
            }
            //List<String> stockIds = resps.stream().filter(orderReqDTO -> orderReqDTO.getOrderStatus().intValue() != DictConstants.WMS_ORDER_DETAIL_STATUS_5 && orderReqDTO.getOrderStatus().intValue() != DictConstants.WMS_ORDER_DETAIL_STATUS_4).map(OrderReqDTO::getMaterialStockId).collect(Collectors.toList());
            List<OrderReqDTO>  orderReqDTOS = resps.stream().filter(orderReqDTO -> orderReqDTO.getOrderStatus().intValue() != DictConstants.WMS_ORDER_DETAIL_STATUS_5 && orderReqDTO.getOrderStatus().intValue() != DictConstants.WMS_ORDER_DETAIL_STATUS_4).collect(Collectors.toList());
            Map<String,OrderReqDTO> orderReqDTOMap = convertMap(orderReqDTOS,OrderReqDTO::getMaterialStockId);

            //respDTOS = respDTOS.stream().filter(materialStockInRespDTO -> !stockIds.contains(materialStockInRespDTO.getId())).collect(Collectors.toList());
            respDTOS.forEach(materialStockInRespDTO -> {
                OrderReqDTO reqDTO = orderReqDTOMap.get(materialStockInRespDTO.getId());
                if (reqDTO !=null){
                    materialStockInRespDTO.setStatus(reqDTO.getOrderStatus());
                }
            });


        }

        //TODO   查询目标位置是该位置的  所有出库单



        return CommonResult.success(respDTOS);
    }


}
