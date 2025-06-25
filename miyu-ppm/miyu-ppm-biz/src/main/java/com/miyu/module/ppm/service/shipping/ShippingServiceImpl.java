package com.miyu.module.ppm.service.shipping;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCancelReqDto;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationRespDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailRetraceDTO;
import com.miyu.module.ppm.controller.admin.home.vo.ContractAnalysisResp;
import com.miyu.module.ppm.convert.shippingdetail.ShippingDetailConvert;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.dal.mysql.shippinginfo.ShippingInfoMapper;
import com.miyu.module.ppm.enums.shipping.DMAuditStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.enums.shipping.ShippingTypeEnum;
import com.miyu.module.ppm.service.company.CompanyService;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.ppm.service.shippingreturndetail.ShippingReturnDetailService;
import com.miyu.module.ppm.strategy.IShippingFactory;
import com.miyu.module.ppm.strategy.ShippingFactory;
import com.miyu.module.ppm.utils.StringListUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.shipping.vo.*;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.shipping.ShippingMapper;
import com.miyu.module.ppm.dal.mysql.shippingdetail.ShippingDetailMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertMap;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.*;
import static com.miyu.module.ppm.enums.ApiConstants.PROCESS_KEY;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_NOT_EXISTS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_OUTBOUND_ERROR;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_SUBMIT_FAIL_NOT_DRAFT;
import static com.miyu.module.ppm.enums.LogRecordConstants.SHIPPING_CLUE_TYPE;
import static com.miyu.module.ppm.enums.LogRecordConstants.SHIPPING_SUBMIT_SUB_TYPE;
import static com.miyu.module.ppm.enums.LogRecordConstants.SHIPPING_SUBMIT_SUCCESS;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_OUTBOUND_CANCEL_ERROR;

/**
 * 销售发货 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class ShippingServiceImpl implements ShippingService {
    @Resource
    private ShippingMapper shippingMapper;
    @Resource
    private ShippingDetailMapper shippingDetailMapper;
    @Resource
    private ShippingInfoMapper shippingInfoMapper;
    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;
    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private ContractService contractService;
    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private ShippingReturnDetailService shippingReturnDetailService;
    @Resource
    private CompanyService companyService;

    @Resource
    private AdminUserApi userApi;
    @Resource
    private OrderApi orderApi;
    @Resource
    private CompanyProductService companyProductService;
    @Resource
    private PmsApi pmsApi;
    @Resource
    private PmsOrderMaterialRelationApi pmsOrderMaterialRelationApi;
    @Resource
    private ContractMapper contractMapper;
    @Resource
    private ShippingFactory shippingFactory;
    @Resource
    private MaterialStockApi materialStockApi;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createShipping(ShippingSaveReqVO createReqVO) {
        //校验发货单详细
        //validateShippingOrder(createReqVO);
        IShippingFactory strategy = shippingFactory.generatorStrategy(createReqVO.getShippingType());
        strategy.validateCreate(createReqVO);

        // 插入
        ShippingDO shipping = BeanUtils.toBean(createReqVO, ShippingDO.class);
        shipping.setStatus(DMAuditStatusEnum.DRAFT.getStatus());
        shippingMapper.insert(shipping);

        // 插入子表
        createShippingDetailList(shipping, createReqVO.getShippingDetails());

        // 返回
        return shipping.getId();
    }

    @Override
    public String createShippingAndSubmit(@Valid ShippingSaveReqVO createReqVO) {
        //校验发货单详细
        //validateShippingOrder(createReqVO);

        IShippingFactory strategy = shippingFactory.generatorStrategy(createReqVO.getShippingType());
        strategy.validateCreate(createReqVO);
        // 插入
        ShippingDO shipping = BeanUtils.toBean(createReqVO, ShippingDO.class);
        shipping.setStatus(DMAuditStatusEnum.DRAFT.getStatus()).setShippingStatus(ShippingStatusEnum.PROCESS.getStatus());
        shippingMapper.insert(shipping);

        // 插入子表
        createShippingDetailList(shipping, createReqVO.getShippingDetails());
        Boolean canOutBound = outBoundShipping(shipping, createReqVO.getShippingDetails());


        String key = PROCESS_KEY;
        if (ShippingTypeEnum.OUTSOURCING.getStatus().equals(createReqVO.getShippingType())){
            key = CONTRACT_CONSIGNMENT_PROCESS_KEY;
        }else if (ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus().equals(createReqVO.getShippingType())){
            key = PM_RETURN_AUDIT_KEY;
        }else if (ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus().equals(createReqVO.getShippingType())){
            key = SHIPPING_INSTORAGE_RETURN_PROCESS_KEY;
        }

        if (canOutBound) {
            // 2. 创建发货审批流程实例
            Map<String, Object> variables = new HashMap<>();
            String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                    .setProcessDefinitionKey(key).setBusinessKey(shipping.getId()).setVariables(variables)).getCheckedData();

            // 3. 更新发货工作流编号
            shippingMapper.updateById(shipping.setProcessInstanceId(processInstanceId)
                    .setStatus(DMAuditStatusEnum.PROCESS.getStatus()).setShippingStatus(ShippingStatusEnum.PROCESS.getStatus()));
            // 返回
        }

        return shipping.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateShipping(ShippingSaveReqVO updateReqVO) {
        Long userId = getLoginUserId();
        // 校验存在
        validateShippingExists(updateReqVO.getId());
        if (updateReqVO.getShippingStatus().equals(ShippingStatusEnum.CREATE.getStatus())) {
            //validateShippingOrder(updateReqVO);
            IShippingFactory strategy = shippingFactory.generatorStrategy(updateReqVO.getShippingType());
            strategy.validateCreate(updateReqVO);
        }

        // 更新
        ShippingDO updateObj = BeanUtils.toBean(updateReqVO, ShippingDO.class);
        Boolean isFinish = true;
        if (ShippingStatusEnum.SEND.getStatus().equals(updateReqVO.getShippingStatus())) {

            for (ShippingDetailDO shippingDetailDO : updateReqVO.getShippingDetails()) {
                shippingDetailDO.setOutboundBy(null);
                shippingDetailDO.setShippingStatus(ShippingStatusEnum.FINISH.getStatus());
            }

            updateObj.setShippingStatus(ShippingStatusEnum.FINISH.getStatus());

        }
        shippingMapper.updateById(updateObj);

        // 更新子表
        updateShippingDetailList(updateObj, updateReqVO.getShippingDetails());

        if (updateObj.getShippingStatus().equals(ShippingStatusEnum.FINISH.getStatus())){
            //删码
            List<String> barcodes = updateReqVO.getShippingDetails().stream().map(ShippingDetailDO::getBarCode).collect(Collectors.toList());
            CommonResult<Boolean> result =   materialStockApi.sendMaterial(barcodes);
            if (!result.isSuccess()){
                throw exception(SHIPPING_OUTBOUND_SUBMIT_ERROR);
            }
        }
    }

    @Override
    public void outBoundShipping(@Valid ShippingSaveReqVO updateReqVO) {

        ShippingDO shippingDO = validateShippingExists(updateReqVO.getId());

        List<OrderReqDTO> orderReqDTOList = CollectionUtils.convertList(updateReqVO.getShippingDetails(), detailDO ->
        {
            OrderReqDTO orderRespVO = new OrderReqDTO();
            orderRespVO.setOrderNumber(shippingDO.getNo());
            orderRespVO.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
            orderRespVO.setChooseStockId(detailDO.getMaterialStockId());
            orderRespVO.setQuantity(detailDO.getOutboundAmount().intValue());
            //目标仓库
            orderRespVO.setTargetWarehouseId(updateReqVO.getWarehouseId());
            return orderRespVO;
        });

        //TODO 审批通过后调用出库
        CommonResult<List<String>> result = orderApi.orderDistribute(orderReqDTOList);

        if (result.isSuccess()) {
            shippingDO.setShippingStatus(ShippingStatusEnum.OUTBOUNDING.getStatus());
            shippingMapper.updateById(shippingDO);
        } else {
            throw exception(SHIPPING_OUTBOUND_ERROR);
        }

    }

    @Override
    public Boolean outBoundShipping(ShippingDO shippingDO, List<ShippingDetailDO> detailDOS) {
        //TODO 不再调用出库单

        return  true;
//        Integer orderValue = DictConstants.WMS_ORDER_TYPE_SALE_OUT;
//        if (ShippingTypeEnum.OUTSOURCING.getStatus().equals(shippingDO.getShippingType())){
//            orderValue = DictConstants.WMS_ORDER_TYPE_OUTSOURCE_OUT;
//        }else if (ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus().equals(shippingDO.getShippingType())){
//            orderValue = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT;
//        }else if (ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus().equals(shippingDO.getShippingType())){
//            orderValue = DictConstants.WMS_ORDER_TYPE_PURCHASE_RETURN_OUT;
//        }
//
//        Integer finalOrderValue = orderValue;
//        List<OrderReqDTO> orderReqDTOList = CollectionUtils.convertList(detailDOS, detailDO ->
//        {
//            OrderReqDTO orderRespVO = new OrderReqDTO();
//            orderRespVO.setOrderNumber(shippingDO.getNo());
//            orderRespVO.setOrderType(finalOrderValue);
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
//            return true;
//        } else {
//            throw exception(SHIPPING_OUTBOUND_ERROR);
//        }

    }

    @Override
    public void updateShippingSubmit(@Valid ShippingSaveReqVO updateReqVO) {


        Long userId = getLoginUserId();
        // 校验存在
        validateShippingExists(updateReqVO.getId());
        //validateShippingOrder(updateReqVO);
        IShippingFactory strategy = shippingFactory.generatorStrategy(updateReqVO.getShippingType());
        strategy.validateCreate(updateReqVO);
        // 更新
        ShippingDO updateObj = BeanUtils.toBean(updateReqVO, ShippingDO.class);


        Boolean canOutBound = outBoundShipping(updateObj, updateReqVO.getShippingDetails());

        String key = PROCESS_KEY;
        if (ShippingTypeEnum.OUTSOURCING.getStatus().equals(updateReqVO.getShippingType())){
            key = CONTRACT_CONSIGNMENT_PROCESS_KEY;
        }else if (ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus().equals(updateReqVO.getShippingType())){
            key = PM_RETURN_AUDIT_KEY;
        }else if (ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus().equals(updateReqVO.getShippingType())){
            key = SHIPPING_INSTORAGE_RETURN_PROCESS_KEY;
        }

        // 更新子表
        updateShippingDetailList(updateObj, updateReqVO.getShippingDetails());
        if (canOutBound) {
            // 2. 创建发货审批流程实例
            Map<String, Object> variables = new HashMap<>();
            String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                    .setProcessDefinitionKey(key).setBusinessKey(updateObj.getId()).setVariables(variables)).getCheckedData();

            // 3. 更新发货工作流编号
            updateObj.setProcessInstanceId(processInstanceId)
                    .setStatus(DMAuditStatusEnum.PROCESS.getStatus()).setShippingStatus(ShippingStatusEnum.PROCESS.getStatus());

            shippingMapper.updateById(updateObj);

        }

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteShipping(String id) {
        // 校验存在
        validateShippingExists(id);
        // 删除
        shippingMapper.deleteById(id);

        // 删除子表
        deleteShippingDetailByShippingId(id);
    }

    @Override
    public void cancelShipping(String id) {
        // 校验存在
        validateShippingExists(id);
        ShippingDO shippingDO = shippingMapper.selectById(id);

        //如果正在审批  需要取消审批
        if (ShippingStatusEnum.PROCESS.getStatus().equals(shippingDO.getShippingStatus())) {
            bpmProcessInstanceApi.cancelProcessInstanceByStartUser(getLoginUserId(), new BpmProcessInstanceCancelReqDto().setId(shippingDO.getProcessInstanceId()).setReason("发货单已作废"));
        }
        //TODO  如果状态是待出库   需要确认WMS出库单是否正在执行
        if (ShippingStatusEnum.OUTBOUND.getStatus().equals(shippingDO.getShippingStatus())) {

        }
        shippingDO.setShippingStatus(ShippingStatusEnum.CANCEL.getStatus());
        List<ShippingInfoDO> shippingInfoDOS = shippingInfoMapper.selectList(ShippingInfoDO::getShippingId,id);
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectListByShippingId(id);
        shippingInfoDOS.forEach(shippingInfoDO -> {
            shippingInfoDO.setShippingStatus(ShippingStatusEnum.CANCEL.getStatus());
        });
        shippingDetailDOS.forEach(shippingDetailDO -> {
            shippingDetailDO.setShippingStatus(ShippingStatusEnum.CANCEL.getStatus());
        });
        shippingMapper.updateById(shippingDO);
        shippingDetailMapper.updateBatch(shippingDetailDOS);
        shippingInfoMapper.updateBatch(shippingInfoDOS);
    }

    @Override
    public void confirmOut(String id) {
       String userId = getLoginUserId().toString();
        ShippingDetailDO shippingDetailDO = shippingDetailMapper.selectById(id);

        shippingDetailDO.setOutboundAmount(new BigDecimal(1));
        shippingDetailDO.setOutboundBy(userId);
        shippingDetailDO.setOutboundTime(LocalDateTime.now());
//        shippingDetailDO.setSignedAmount(new BigDecimal(1));
//        shippingDetailDO.setSignedBy(userId);
//        shippingDetailDO.setSignedTime(LocalDateTime.now());
        shippingDetailDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
        shippingDetailMapper.updateById(shippingDetailDO);

        List<ShippingDetailDO> detailDOS = shippingDetailMapper.selectListByShippingId(shippingDetailDO.getShippingId());

        Map<String,List<ShippingDetailDO>> map = new HashMap<>();
        Boolean isFinish =  true;
        for (ShippingDetailDO detailDO : detailDOS) {

            if (org.springframework.util.CollectionUtils.isEmpty(map.get(detailDO.getInfoId()))) {
                map.put(detailDO.getInfoId(), Lists.newArrayList(detailDO));
            } else {
                map.get(detailDO.getInfoId()).add(detailDO);
            }

            if (shippingDetailDO.getShippingStatus().equals(ShippingStatusEnum.OUTBOUNDING.getStatus())){
                isFinish =false;
            }
        }
        for (Map.Entry<String, List<ShippingDetailDO>> entry : map.entrySet()) {

            BigDecimal outNumber = new BigDecimal(0);
            for (ShippingDetailDO detailDO : entry.getValue()){
                if (detailDO.getShippingStatus().equals(ShippingStatusEnum.SEND.getStatus())){
                    outNumber = outNumber.add(detailDO.getOutboundAmount());
                }
            }
            ShippingInfoDO shippingInfoDO = shippingInfoMapper.selectById(entry.getKey());
            shippingInfoDO.setOutboundAmount(outNumber);
            if (outNumber.compareTo(shippingInfoDO.getConsignedAmount()) == 0){
                shippingInfoDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
            }
            shippingInfoMapper.updateById(shippingInfoDO);
        }

        if (isFinish){
            ShippingDO shippingDO = new ShippingDO().setId(shippingDetailDO.getShippingId()).setShippingStatus(ShippingStatusEnum.SEND.getStatus());
            shippingMapper.updateById(shippingDO);
        }

    }

    private ShippingDO validateShippingExists(String id) {
        ShippingDO shippingDO = shippingMapper.selectById(id);
        if (shippingDO == null) {
            throw exception(SHIPPING_NOT_EXISTS);
        }

        return shippingDO;
    }

    /**
     * 验证发货单订单信息
     *
     * @param vo
     */
    private void validateShippingOrder(ShippingSaveReqVO vo) {


        List<ShippingDetailDO> shippingDetailDOList = this.getShippingDetailListByProjectId(vo.getProjectId(), vo.getContractId(), vo.getShippingType());

        if (vo.getShippingType().intValue() == 1) {
            List<String> detailIds = new ArrayList<>();
            if (!StringUtils.isEmpty(vo.getId())) {
                detailIds.addAll(convertList(vo.getShippingDetails(), ShippingDetailDO::getId));
            }
            //筛选出有效的出库单
            List<ShippingDetailDO> list = shippingDetailDOList.stream().filter(shippingDetailDO -> !shippingDetailDO.getShippingId().equals(vo.getId())).collect(Collectors.toList());
            //出库单的数量
            BigDecimal outBoundNumber = new BigDecimal(list.size());

            //查询项目总数
            CommonResult<PmsPlanDTO> orderResult = pmsApi.getPlanByProjectId(vo.getProjectId());

            OrderMaterialRelationUpdateDTO dto = new OrderMaterialRelationUpdateDTO();
            dto.setOrderId(orderResult.getCheckedData().getProjectOrderId());

            CommonResult<List<OrderMaterialRelationRespDTO>> result = pmsOrderMaterialRelationApi.getRelationByPlanOrOrder(dto);

            BigDecimal totalNumber = new BigDecimal(result.getCheckedData().size());

            //TODO 查询项目下所有有效的退货单
            BigDecimal returnNumber = new BigDecimal(0);


            BigDecimal nowNumber = new BigDecimal(vo.getShippingDetails().size());
            if (outBoundNumber.add(nowNumber).subtract(returnNumber).compareTo(totalNumber) > 0) {
                throw exception(new ErrorCode(1_010_000_012, "发货数量超过项目的总限制数量"));
            }
        }




    }


    @Override
    public ShippingDO getShipping(String id) {
        return shippingMapper.selectById(id);
    }

    @Override
    public PageResult<ShippingDO> getShippingPage(ShippingPageReqVO pageReqVO) {
        return shippingMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ShippingDTO> getShippingListByContractIds(Collection<String> ids) {
        ArrayList<Integer> list = Lists.newArrayList(2, 3, 4, 5);
        //查询发货单
        QueryWrapper<ShippingDO> wrapper = new QueryWrapper<>();
        wrapper.in("contract_id", ids);
        wrapper.in("shipping_status", list);
        List<ShippingDO> shippingList = shippingMapper.selectList(wrapper);
        //合同集合
        List<ContractDO> contractRespDTOS = contractService.getContractListByIds(ids);
        Map<String, ContractDO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractDO::getId);

        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractDO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());
        List<CompanyDO> companyDOS = companyService.getCompanyListByIds(companyIds);
        Map<String, CompanyDO> companyMap =  CollectionUtils.convertMap(companyDOS, CompanyDO::getId);

        List<ShippingDTO> shippingDTOS = BeanUtils.toBean(shippingList, ShippingDTO.class, vo -> {
            //给发货单设置合同方(公司名)
            MapUtils.findAndThen(companyMap, contractMap.get(vo.getContractId()).getParty(), a -> vo.setCompanyName(a.getName()));
            List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectListByShippingId(vo.getId());
            List<ShippingDetailDTO> shippingDetailDTOS = BeanUtils.toBean(shippingDetailDOS, ShippingDetailDTO.class);
            vo.setShippingDetailDTOList(shippingDetailDTOS);
        });

        System.out.println(shippingDTOS);
        return shippingDTOS;
    }

    @Override
    public List<ShippingDetailRetraceDTO> getShippingListByBarcode(String barCode) {
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectListByBarCode(barCode);
        List<ShippingDetailRetraceDTO> shippingDetailRetraceDTOS = new ArrayList<>();
        if (!org.springframework.util.CollectionUtils.isEmpty(shippingDetailDOS)) {


            List<Long> userIdList = new ArrayList<>();
            //主管
            List<Long> outboundIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingDetailDO::getOutboundBy));
//            List<Long> singedIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingDetailDO::getSignedBy));
//            //创建人
            List<Long> creatorIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingDetailDO::getCreator));
            //更新人
            List<Long> updaterIds = StringListUtils.stringListToLongList(convertList(shippingDetailDOS, ShippingDetailDO::getUpdater));

            //合并用户集合
            if (outboundIds != null) userIdList.addAll(outboundIds);
            if (creatorIds != null) userIdList.addAll(creatorIds);
            if (updaterIds != null) userIdList.addAll(updaterIds);
//            if (singedIds != null) userIdList.addAll(singedIds);

            userIdList = userIdList.stream().distinct().collect(Collectors.toList());

            // 拼接数据
            Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);


            //合同集合
            //TODO
            // List<ContractRespDTO> contractRespDTOS = contractApi.getContractList(Lists.newArrayList(shippingDetailDOS.get(0).ge())).getCheckedData();
            List<ContractRespDTO> contractRespDTOS = new ArrayList<>();
            Map<String, ContractRespDTO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractRespDTO::getId);

            //合同供应商信息
            List<String> companyIds = convertList(contractRespDTOS, ContractRespDTO::getParty);
            companyIds = companyIds.stream().distinct().collect(Collectors.toList());
            List<CompanyDO> companyDOS = companyService.getCompanyListByIds(companyIds);
            Map<String, CompanyDO> companyMap =  CollectionUtils.convertMap(companyDOS, CompanyDO::getId);


            shippingDetailRetraceDTOS = ShippingDetailConvert.INSTANCE.convertList(shippingDetailDOS, userMap, contractMap, companyMap);

        }


        return shippingDetailRetraceDTOS;
    }



    // ==================== 子表（销售发货明细） ====================
    @Override
    public List<ShippingDTO> getShippingListByProjectIds(Collection<String> ids) {
        //分成两类,一种直接绑项目上发货,一种绑合同上发货(合同绑项目)
        //1.直接通过项目查
//        LambdaQueryWrapperX<ShippingDO> wrapperX = new LambdaQueryWrapperX<>();
//        wrapperX.in(ShippingDO::getProjectId,ids);
//        List<ShippingDO> shippingDOList = shippingMapper.selectList(wrapperX);

        LambdaQueryWrapperX<ShippingDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(ShippingDetailDO::getProjectId,ids);
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectList(wrapperX);
        if(CollectionUtils.isAnyEmpty(shippingDetailDOS)){
            return Collections.emptyList();
        }
        List<String> shippingIds = shippingDetailDOS.stream().map(ShippingDetailDO::getShippingId).distinct().collect(Collectors.toList());
        List<ShippingDO> shippingDOList = shippingMapper.selectBatchIds(shippingIds);

        //2.查项目下的合同，再用合同查
        LambdaQueryWrapperX<ContractDO> contractWrapper = new LambdaQueryWrapperX<>();
        contractWrapper.in(ContractDO::getProjectId,ids);
        List<ContractDO> contractDOList = contractMapper.selectList(contractWrapper);
        if (contractDOList.size()>0){
            List<String> contractIds = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
            LambdaQueryWrapperX<ShippingDO> wrapperX2 = new LambdaQueryWrapperX<>();
            wrapperX2.in(ShippingDO::getContractId,contractIds);
            List<ShippingDO> shippingDOList2 = shippingMapper.selectList(wrapperX2);
            shippingDOList.addAll(shippingDOList2);
            shippingDOList = shippingDOList.stream().distinct().collect(Collectors.toList());
        }


        return BeanUtils.toBean(shippingDOList,ShippingDTO.class);
    }

    @Override
    public List<ShippingDetailDTO> getShippingDetailListByProjectIds(Collection<String> ids) {
        LambdaQueryWrapperX<ShippingDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(ShippingDetailDO::getProjectId,ids);
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectList(wrapperX);
        if(CollectionUtils.isAnyEmpty(shippingDetailDOS)){
            return Collections.emptyList();
        }
        return BeanUtils.toBean(shippingDetailDOS,ShippingDetailDTO.class);
    }

    @Override
    public List<ShippingDetailDO> getShippingDetailListByShippingId(String shippingId) {
        return shippingDetailMapper.selectListByShippingId(shippingId);
    }

    @Override
    public List<ShippingDetailDTO> getShippingByConsignmentDetailIds(Collection<String> ids) {
        LambdaQueryWrapperX<ShippingDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(ShippingDetailDO::getConsignmentDetailId, ids);
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectList(wrapperX);
        List<ShippingDetailDTO> shippingDetailDTOS = BeanUtils.toBean(shippingDetailDOS, ShippingDetailDTO.class);
        return shippingDetailDTOS;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SHIPPING_CLUE_TYPE, subType = SHIPPING_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = SHIPPING_SUBMIT_SUCCESS)
    public void submitShipping(String id, Long userId) {
        // 1. 校验发货单是否在审批
        ShippingDO shippingDO = validateShippingExists(id);



        String key = PROCESS_KEY;
        if (ShippingTypeEnum.OUTSOURCING.getStatus().equals(shippingDO.getShippingType())){
            key = CONTRACT_CONSIGNMENT_PROCESS_KEY;
        }else if (ShippingTypeEnum.CONSIGNMENT_RETURN.getStatus().equals(shippingDO.getShippingType())){
            key = PM_RETURN_AUDIT_KEY;
        }else if (ShippingTypeEnum.COMMISSIONEDPROCESSING.getStatus().equals(shippingDO.getShippingType())){
            key = SHIPPING_INSTORAGE_RETURN_PROCESS_KEY;
        }

        List<ShippingDetailDO> shippingDetailDOList = shippingDetailMapper.selectListByShippingId(id);

        Boolean canOutBound = outBoundShipping(shippingDO, shippingDetailDOList);

        if (!(ObjUtil.equals(shippingDO.getStatus(), DMAuditStatusEnum.DRAFT.getStatus()) || ObjUtil.equals(shippingDO.getStatus(), DMAuditStatusEnum.REJECT.getStatus()))) {
            throw exception(SHIPPING_SUBMIT_FAIL_NOT_DRAFT);
        }

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(key).setBusinessKey(String.valueOf(id)).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号
        shippingMapper.updateById(new ShippingDO().setId(id).setProcessInstanceId(processInstanceId)
                .setStatus(DMAuditStatusEnum.PROCESS.getStatus()).setShippingStatus(ShippingStatusEnum.PROCESS.getStatus()));

        // 4. 记录日志
        LogRecordContext.putVariable("shippingNo", shippingDO.getNo());
    }

    @Override
    public void updateShippingProcessInstanceStatus(String id, Integer status) {
        ShippingDO shippingDO = validateShippingExists(id);
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
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
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
                orderReqDTO.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
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

    @Override
    public void updateShippingStatus(String id, Integer shippingStatus) {
        ShippingDetailDO detailDO = shippingDetailMapper.selectById(id);

        ShippingInfoDO shippingInfoDO = shippingInfoMapper.selectById(detailDO.getInfoId());
        detailDO.setShippingStatus(shippingStatus);
        detailDO.setSignedTime(LocalDateTime.now());
        detailDO.setSignedBy(getLoginUserId().toString());
        shippingDetailMapper.updateById(detailDO);

        //更新数量
        List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectList(ShippingDetailDO::getInfoId, shippingInfoDO.getId());
        BigDecimal a = new BigDecimal(0);
        for (ShippingDetailDO shippingDetailDO: shippingDetailDOS){

            if (shippingDetailDO.getShippingStatus().equals(shippingStatus)){
                a.add(new BigDecimal(1));
            }
        }
        shippingInfoDO.setSignedAmount(a);
        if (shippingInfoDO.getOutboundAmount().compareTo(shippingInfoDO.getSignedAmount()) == 0){
            shippingInfoDO.setShippingStatus(shippingStatus);
            shippingInfoDO.setSignedTime(LocalDateTime.now());
            shippingInfoDO.setSignedBy(getLoginUserId().toString());
        }
        shippingInfoMapper.updateById(shippingInfoDO);

        if (shippingInfoDO.getShippingStatus().equals(shippingStatus)){

            List<ShippingInfoDO> shippingInfoDOS = shippingInfoMapper.selectList(ShippingInfoDO::getShippingId,shippingInfoDO.getShippingId());

            Boolean signed = true;
            for (ShippingInfoDO infoDO: shippingInfoDOS){

                if (!infoDO.getShippingStatus().equals(shippingStatus)){
                    signed = false;
                }
            }
            if (signed){
                ShippingDO shippingDO = shippingMapper.selectById(shippingInfoDO.getShippingId());

                shippingDO.setShippingStatus(shippingStatus);
                shippingMapper.updateById(shippingDO);
            }

        }




    }

    @Override
    public Map<String, BigDecimal> getOrderMap(String contractId, List<String> detailIds, List<Integer> status) {


        //查询合同下  所有已发货的发货单(如果detailIds 不为空 则需要排除掉)
        List<ShippingDetailDO> detailDOS = shippingDetailMapper.getOutboundOderByContractId(contractId, detailIds, status);
        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = new HashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            detailDOS.forEach(shippingDetailDO -> {

                if (numberMap.get(shippingDetailDO.getOrderId()) == null) {
                    numberMap.put(shippingDetailDO.getOrderId(), shippingDetailDO.getConsignedAmount());
                } else {
                    numberMap.get(shippingDetailDO.getOrderId()).add(shippingDetailDO.getConsignedAmount());
                }
            });
        }

        return numberMap;
    }

    @Override
    public List<ShippingDO> getShippingList(List<Integer> status) {
        QueryWrapper queryWrapper = new QueryWrapper<ShippingDO>();
        if (!org.springframework.util.CollectionUtils.isEmpty(status)) {
            queryWrapper.in("shipping_status", status);
        }

        return shippingMapper.selectList(queryWrapper);
    }

    @Override
    public List<ShippingDO> getShippingByContract(String contractId) {
        return shippingMapper.getShippingByContract(contractId,Lists.newArrayList(0,1,2,3,4,5),1);
    }

    @Override
    public ShippingDO getShippingByNo(String shippingNo) {
        return shippingMapper.selectOne("shipping_no", shippingNo);
    }

    @Override
    public void checkOutBoundInfo() {

        //查询正在出库的发货单信息
        List<ShippingDO> shippingDOS = shippingMapper.selectList(ShippingDO::getShippingStatus, ShippingStatusEnum.OUTBOUNDING.getStatus());

        if (!org.springframework.util.CollectionUtils.isEmpty(shippingDOS)) {
            List<OrderReqDTO> list = CollectionUtils.convertList(shippingDOS, shippingDO -> {
                OrderReqDTO dto = new OrderReqDTO();
                dto.setOrderNumber(shippingDO.getNo());
                dto.setOrderType(DictConstants.WMS_ORDER_TYPE_SALE_OUT);
                return dto;
            });
            //查询发货单对应的出库单信息
            CommonResult<List<OrderReqDTO>> result = orderApi.orderList(list);


            //筛选出出库完成的单子
            List<OrderReqDTO> orderReqDTOList = result.getCheckedData().stream().filter(orderReqDTO -> orderReqDTO.getOrderStatus().equals(DictConstants.WMS_ORDER_DETAIL_STATUS_4)).collect(Collectors.toList());

            if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOList)) {
                Map<String, List<OrderReqDTO>> orderMap = result.getCheckedData().stream().collect(Collectors.groupingBy(OrderReqDTO::getOrderNumber));


                for (ShippingDO shippingDO : shippingDOS) {
                    List<OrderReqDTO> orderReqDTOS = orderMap.get(shippingDO.getNo());
                    Boolean finish = true;
                    List<ShippingDetailDO> shippingDetailDOS = shippingDetailMapper.selectListByShippingId(shippingDO.getId());
                    List<ShippingInfoDO> shippingInfoDOS = shippingInfoMapper.selectList(ShippingInfoDO::getShippingId,shippingDO.getId());

                    if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOS)) {

                        Map<String, OrderReqDTO> orderReqDTOMap = CollectionUtils.convertMap(orderReqDTOS, OrderReqDTO::getMaterialStockId);

                        for (ShippingDetailDO shippingDetailDO : shippingDetailDOS) {
                            OrderReqDTO dto = orderReqDTOMap.get(shippingDetailDO.getMaterialStockId());
                            if (dto == null) {//找不到完成的出库信息  则视为没完成
                                finish = false;
                            } else if (dto.getQuantity().intValue() != shippingDetailDO.getConsignedAmount().intValue()) {
                                finish = false; //数量对应不上则视为没完成
                            } else {
                                shippingDetailDO.setOutboundAmount(shippingDetailDO.getConsignedAmount());
                                shippingDetailDO.setOutboundBy(dto.getOperator());
                                shippingDetailDO.setOutboundTime(dto.getOperateTime());
                                shippingDetailDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
                                //TODO  后续增加操作人
                            }
                        }

                        for (ShippingInfoDO shippingInfoDO :shippingInfoDOS){
                            shippingInfoDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());
                        }
                    } else {
                        finish = false;
                    }

                    if (finish) {//更新状态为待发货  并且更新出库数量
                        shippingDO.setShippingStatus(ShippingStatusEnum.SEND.getStatus());

                        shippingMapper.updateById(shippingDO);
                        shippingInfoMapper.updateBatch(shippingInfoDOS);
                        shippingDetailMapper.updateBatch(shippingDetailDOS);

                    }

                }

            }


        }


    }

    @Override
    public List<ShippingDetailDO> getShippingDetailListByProjectId(String projectId, String contractId, Integer shippingType) {
        MPJLambdaWrapperX<ShippingDetailDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.leftJoin(ShippingDO.class, ShippingDO::getId, ShippingDetailDO::getShippingId)
                .selectAs(ShippingDO::getNo, ShippingDetailDO::getNo)
                .selectAs(ShippingDO::getName, ShippingDetailDO::getName)
                .selectAs(ShippingDO::getShippingStatus, ShippingDetailDO::getShippingStatus)
                .selectAll(ShippingDetailDO.class);
        wrapperX.eqIfPresent(ShippingDetailDO::getProjectId, projectId)
                .eqIfPresent(ShippingDetailDO::getShippingType, shippingType)
                .neIfPresent(ShippingDetailDO::getShippingStatus,ShippingStatusEnum.CANCEL.getStatus())
                .eqIfPresent(ShippingDetailDO::getContractId, contractId);

        return shippingDetailMapper.selectList(wrapperX);
    }

    @Override
    public List<ShippingDO> getShippings(List<String> ids) {
        return shippingMapper.selectBatchIds(ids);
    }

    @Override
    public void updateShippingStatus(ShippingDO shippingDO) {

        shippingMapper.updateById(shippingDO);
        List<ShippingDetailDO> detailDOS = shippingDetailMapper.selectList(ShippingDetailDO::getShippingId,shippingDO.getId());
        detailDOS.forEach(shippingDetailDO -> {
            shippingDetailDO.setShippingStatus(shippingDO.getShippingStatus());

        });
        List<ShippingInfoDO> shippingInfoDOS = shippingInfoMapper.selectList(ShippingInfoDO::getShippingId,shippingDO.getId());
        shippingInfoDOS.forEach(shippingDetailDO -> {
            shippingDetailDO.setShippingStatus(shippingDO.getShippingStatus());

        });
        shippingDetailMapper.updateBatch(detailDOS);
        shippingInfoMapper.updateBatch(shippingInfoDOS);

    }

    @Override
    public List<ContractAnalysisResp> getContractAnalysis(Integer type) {
        return shippingMapper.getContractAnalysis(type);
    }

    private void createShippingDetailList(ShippingDO shippingDO, List<ShippingDetailDO> list) {
        Map<String, List<ShippingDetailDO>> map = new HashMap<>();
        for (ShippingDetailDO shippingDetailDO : list) {
            shippingDetailDO.setShippingId(shippingDO.getId());
            shippingDetailDO.setContractId(shippingDO.getContractId());
            shippingDetailDO.setShippingType(shippingDO.getShippingType());
            if (org.springframework.util.CollectionUtils.isEmpty(map.get(shippingDetailDO.getMaterialConfigId()))) {
                map.put(shippingDetailDO.getMaterialConfigId(), Lists.newArrayList(shippingDetailDO));
            } else {
                map.get(shippingDetailDO.getMaterialConfigId()).add(shippingDetailDO);
            }
        }
        for (Map.Entry<String, List<ShippingDetailDO>> entry : map.entrySet()) {
            List<ShippingDetailDO> detailDOS = entry.getValue();
            BigDecimal number = new BigDecimal(0);
            BigDecimal outNumber = new BigDecimal(0);
            BigDecimal signedNumber = new BigDecimal(0);
            for (ShippingDetailDO detailDO : detailDOS) {
                number = number.add(detailDO.getConsignedAmount());
                if (detailDO.getOutboundAmount() != null){
                    outNumber = outNumber.add(detailDO.getOutboundAmount());
                }
                if (detailDO.getSignedAmount() != null){
                    signedNumber = signedNumber.add(detailDO.getSignedAmount());
                }
            }

            ShippingInfoDO shippingInfoDO = BeanUtils.toBean(detailDOS.get(0), ShippingInfoDO.class);
            shippingInfoDO.setConsignedAmount(number);
            shippingInfoDO.setSignedAmount(signedNumber);
            shippingInfoDO.setOutboundAmount(outNumber);
            shippingInfoMapper.insert(shippingInfoDO);
            detailDOS.forEach(o -> o.setInfoId(shippingInfoDO.getId()));
            shippingDetailMapper.insertBatch(detailDOS);
        }

    }

    private void updateShippingDetailList(ShippingDO shippingDO, List<ShippingDetailDO> list) {
        shippingDetailMapper.deleteAllByShippingId(shippingDO.getId());
        shippingInfoMapper.deleteAllByShippingId(shippingDO.getId());
        list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createShippingDetailList(shippingDO, list);
    }

    private void deleteShippingDetailByShippingId(String shippingId) {
        shippingDetailMapper.deleteAllByShippingId(shippingId);
        shippingInfoMapper.deleteAllByShippingId(shippingId);
    }

}
