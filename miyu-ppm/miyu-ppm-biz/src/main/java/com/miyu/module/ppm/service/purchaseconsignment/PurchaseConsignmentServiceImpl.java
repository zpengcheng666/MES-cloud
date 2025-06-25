package com.miyu.module.ppm.service.purchaseconsignment;

import cn.hutool.core.util.ObjUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.number.NumberUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCancelReqDto;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.pms.api.orderMaterial.PmsOrderMaterialRelationApi;
import cn.iocoder.yudao.module.pms.api.orderMaterial.dto.OrderMaterialRelationUpdateDTO;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.collect.Lists;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.CompanyApi;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.ContractApi;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.ConsignmentInfoQueryReqVO;
import com.miyu.module.ppm.controller.admin.contract.util.StringListUtils;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailSaveReqVO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.dal.mysql.companyproduct.CompanyProductMapper;
import com.miyu.module.ppm.dal.mysql.consignmentinfo.ConsignmentInfoMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.PpmAuditStatusEnum;
import com.miyu.module.ppm.strategy.ConsignmentFactory;
import com.miyu.module.ppm.strategy.IConsignmentFactory;
import com.miyu.module.ppm.strategy.IShippingFactory;
import com.miyu.module.ppm.strategy.ShippingFactory;
import com.miyu.module.qms.api.dto.InspectionSchemeReqDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeRespDTO;
import com.miyu.module.qms.api.dto.InspectionSheetReqDTO;
import com.miyu.module.qms.api.dto.InspectionSheetRespDTO;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import com.miyu.module.qms.api.inspectionsheet.InspectionSheetApi;
import com.miyu.module.qms.api.inspectionsheetcreatetask.InspectionSheetTaskApi;
import com.miyu.module.qms.api.inspectionsheetcreatetask.dto.TaskDTO;
import com.miyu.module.qms.enums.InspectionSchemeTypeEnum;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import com.miyu.module.wms.enums.DictConstants;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import org.apache.poi.ss.formula.functions.T;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.dal.mysql.contract.ContractOrderMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;
import static cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ApiConstants.*;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;
import static com.miyu.module.ppm.enums.LogRecordConstants.*;

/**
 * 采购收货 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class PurchaseConsignmentServiceImpl implements PurchaseConsignmentService {
    private static Logger logger = LoggerFactory.getLogger(PurchaseConsignmentServiceImpl.class);

    @Resource
    private ConsignmentMapper consignmentMapper;
    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;
    @Resource
    private ConsignmentInfoMapper consignmentInfoMapper;
    @Resource
    private ContractMapper contractMapper;

    @Resource
    private ContractOrderMapper contractOrderMapper;

    @Resource
    private BpmProcessInstanceApi bpmProcessInstanceApi;

    @Resource
    private ContractApi contractApi;

    @Resource
    private CompanyApi companyApi;

    @Resource
    private AdminUserApi userApi;

    @Resource
    private MaterialMCCApi materialMCCApi;
    @Resource
    private CompanyProductMapper companyProductMapper;
    @Resource
    private OrderApi orderApi;
    @Resource
    private InspectionSchemeApi inspectionSchemeApi;
    @Resource
    private InspectionSheetApi inspectionSheetApi;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private PmsOrderMaterialRelationApi  pmsOrderMaterialRelationApi;
    @Resource
    private InspectionSheetTaskApi inspectionSheetTaskApi;

    @Resource
    private ConsignmentFactory consignmentFactory;
    @Resource
    private EncodingRuleApi encodingRuleApi;

    @Override
    public String createPurchaseConsignment(Long userId, PurchaseConsignmentSaveReqVO createReqVO) {
        //校验发货单详细
        //validatePurchaseConsignmentOrder(createReqVO);
        IConsignmentFactory strategy = consignmentFactory.generatorStrategy(createReqVO.getConsignmentType());
        strategy.validateCreate(createReqVO);


        // 插入
        ConsignmentDO purchaseConsignment = BeanUtils.toBean(createReqVO, ConsignmentDO.class)
                .setStatus(PpmAuditStatusEnum.DRAFT.getStatus());
        consignmentMapper.insert(purchaseConsignment);

        // 插入子表
        createReqVO.getPurchaseConsignmentDetails().forEach(i -> {
            i.setContractId(createReqVO.getContractId());
        });
        createPurchaseConsignmentDetailList(purchaseConsignment, createReqVO.getPurchaseConsignmentDetails());

        return purchaseConsignment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createPurchaseConsignmentAndSubmit(Long userId, PurchaseConsignmentSaveReqVO createReqVO) {
        //校验发货单详细
        //validatePurchaseConsignmentOrder(createReqVO);

        IConsignmentFactory strategy = consignmentFactory.generatorStrategy(createReqVO.getConsignmentType());
        strategy.validateCreate(createReqVO);

        // 插入
        ConsignmentDO purchaseConsignment = BeanUtils.toBean(createReqVO, ConsignmentDO.class)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus())
                .setConsignmentStatus(ConsignmentStatusEnum.PROCESS.getStatus());
        consignmentMapper.insert(purchaseConsignment);

        // 插入子表
        createReqVO.getPurchaseConsignmentDetails().forEach(i -> {
            i.setContractId(createReqVO.getContractId());
        });
        createPurchaseConsignmentDetailList(purchaseConsignment, createReqVO.getPurchaseConsignmentDetails());

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(getProcessKey(createReqVO.getConsignmentType())).setBusinessKey(purchaseConsignment.getId()).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号
        consignmentMapper.updateById(purchaseConsignment.setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus()).setConsignmentStatus(ConsignmentStatusEnum.PROCESS.getStatus()));
        // 返回
        return purchaseConsignment.getId();
    }

    /**
     * 更新采购收货
     *
     * @param updateReqVO 更新信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePurchaseConsignment(PurchaseConsignmentSaveReqVO updateReqVO) {
        //校验发货单详细
        //validatePurchaseConsignmentOrder(updateReqVO);

        Long userId = getLoginUserId();
        // 校验存在
        validatePurchaseConsignmentExists(updateReqVO.getId());
        if (updateReqVO.getConsignmentStatus().equals(ConsignmentStatusEnum.CREATE.getStatus())) {
            IConsignmentFactory strategy = consignmentFactory.generatorStrategy(updateReqVO.getConsignmentType());
            strategy.validateCreate(updateReqVO);
        }

        // 更新
        ConsignmentDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentDO.class);
        if (updateReqVO.getConsignmentStatus().equals(ConsignmentStatusEnum.SINGING.getStatus())) {
            //校验采购数量与签收数量是否一致
            updateReqVO.getPurchaseConsignmentDetails().forEach(i -> {
                if (!i.getConsignedAmount().equals(i.getSignedAmount())) {
                    throw exception(CONTRACT_CONSIGNMENT_SIGNEDAMOUNT_ERROR);
                }
            });



            ContractDO contractDO = contractMapper.getContractById(updateReqVO.getContractId());

            //List<ContractOrderDO> orderDOList = contractOrderMapper.selectBatchIds(orderIds);

            List<CompanyProductDO> productDOList = companyProductMapper.getProductListByCompanyId(contractDO.getParty());

            Map<String, CompanyProductDO> productDOMap = CollectionUtils.convertMap(productDOList, CompanyProductDO::getMaterialId);

            Boolean check = false;

            List<OrderReqDTO> list = new ArrayList<>();
            Map<String, BigDecimal> checkMaterialMap = new HashMap<>();
            Map<String, InspectionSchemeRespDTO> schemeMap = new HashMap<>();
            Integer checkNum = 0;//需要检验的物料类型数量
            Integer effectiveNum = 0;//有效的检验单的数量

            Map<String, MaterialConfigRespDTO> materialConfigRespDTOMap =  materialMCCApi.getMaterialConfigMap(updateReqVO.getPurchaseConsignmentDetails().stream().map(PurchaseConsignmentDetailSaveReqVO::getMaterialConfigId).collect(Collectors.toList()));

            for (PurchaseConsignmentDetailSaveReqVO detailDO : updateReqVO.getPurchaseConsignmentDetails()) {

                CompanyProductDO companyProductDO = productDOMap.get(detailDO.getMaterialConfigId());
                if (companyProductDO == null) {
                    throw exception(CONSIGNMENT_ORDER_NOT_EXISTS);
                }
                detailDO.setSignedBy(getLoginUserId().toString());
                detailDO.setSignedTime(LocalDateTime.now());
                //如果产品非免检  或者 外协合同 都需要质检
                if (companyProductDO.getQualityCheck().intValue() == 2 ||contractDO.getContractType().intValue()==2 || contractDO.getContractType().intValue()==3) {
                    check = true;
                    checkNum++;
                    checkMaterialMap.put(detailDO.getMaterialConfigId(), detailDO.getSignedAmount());
                    InspectionSchemeReqDTO schemeReqDTO = new InspectionSchemeReqDTO();
                    schemeReqDTO.setSchemeType(InspectionSchemeTypeEnum.PURCHASETYPE.getStatus());
                    schemeReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                    CommonResult<List<InspectionSchemeRespDTO>> commonResult = inspectionSchemeApi.getInspectionScheme(schemeReqDTO);
                    List<InspectionSchemeRespDTO> schemeRespDTOS = commonResult.getCheckedData();
                    if (!org.springframework.util.CollectionUtils.isEmpty(schemeRespDTOS)) {
                        //如果检验方案只有一个 则可以直接生成检验任务
                        if (schemeRespDTOS.size() == 1) {
                            schemeMap.put(detailDO.getMaterialConfigId(), schemeRespDTOS.get(0));
                            effectiveNum++;
                        }
                    }else {
                        throw exception(INSPECTION_SCHEME_ERROR);
                    }


                }

                MaterialConfigRespDTO configRespDTO = materialConfigRespDTOMap.get(detailDO.getMaterialConfigId());

                //如果是单件需要拆成单个的WMS入库订单
                if (configRespDTO.getMaterialManage().intValue() ==1){

                    for (int i=0;i<detailDO.getSignedAmount().intValue();i++){
                        OrderReqDTO dto = new OrderReqDTO();
                        dto.setOrderStatus(0);//待质检
                        dto.setOrderNumber(updateReqVO.getNo());
                        if (StringUtils.isNotBlank(detailDO.getBarCode())){
                            dto.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_IN);
                            dto.setChooseStockId(detailDO.getMaterialStockId());
                        }else {
                            dto.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_IN);
                        }
                        dto.setQuantity(1);
                        dto.setMaterialConfigId(detailDO.getMaterialConfigId());
                        list.add(dto);
                    }
                }else {
                    OrderReqDTO dto = new OrderReqDTO();
                    dto.setOrderStatus(0);//待质检
                    dto.setOrderNumber(updateReqVO.getNo());
                    if (StringUtils.isNotBlank(detailDO.getBarCode())){
                        dto.setOrderType(DictConstants.WMS_ORDER_TYPE_OUTSOURCE_IN);
                        dto.setChooseStockId(detailDO.getMaterialStockId());
                    }else {
                        dto.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_IN);
                    }
                    dto.setQuantity(detailDO.getSignedAmount().intValue());
                    dto.setMaterialConfigId(detailDO.getMaterialConfigId());
                    list.add(dto);
                }

            }

            CommonResult<List<String>> result = orderApi.orderPurchaseInDistribute(list);

            if (!result.isSuccess()){
                throw exception(PURCHASE_CONSIGNMENT_INBOUND_ERROR);
            }

            if (check) {

                if (checkNum.equals(effectiveNum)) {//如果需要质检的产品  都有唯一的质检单  则直接生成质检单 状态更改为质检中 否则需要人工选择质检单
                    for (Map.Entry<String, BigDecimal> entry : checkMaterialMap.entrySet()) {
                        //todo
                        InspectionSheetReqDTO dto = new InspectionSheetReqDTO();
                        dto.setRecordNumber(updateObj.getNo());
                        dto.setMaterialConfigId(entry.getKey());
                        dto.setQuantity(entry.getValue().intValue());
                        dto.setSchemeType(InspectionSchemeTypeEnum.PURCHASETYPE.getStatus());
                        dto.setSchemeId(schemeMap.get(entry.getKey()).getId());
                        CommonResult<String> sheetResult = inspectionSheetApi.createInspectionSheet(dto);

                        if (!sheetResult.isSuccess()){
                            throw exception(PURCHASE_CONSIGNMENT_SHEET_ERROR);
                        }
                    }
                    updateObj.setConsignmentStatus(ConsignmentStatusEnum.ANALYSISING.getStatus());
                } else {
                    updateObj.setConsignmentStatus(ConsignmentStatusEnum.ANALYSIS.getStatus());
                }


            } else {//如果没有质检 调用改成入库状态
                updateObj.setConsignmentStatus(ConsignmentStatusEnum.INBOUND.getStatus());
            }
            if (result.isSuccess()) {
                consignmentMapper.updateById(updateObj);
            } else {
                throw exception(PURCHASE_CONSIGNMENT_INBOUND_ERROR);
            }


        } else {
            consignmentMapper.updateById(updateObj);
        }

        // 更新子表·
        updatePurchaseConsignmentDetailList(updateObj, updateReqVO.getPurchaseConsignmentDetails());
    }

    /**
     * 更新采购收货并审核
     *
     * @param updateReqVO
     */
    @Override
    public void updatePurchaseConsignmentSubmit(@Valid PurchaseConsignmentSaveReqVO updateReqVO) {

        //校验发货单详细
        //validatePurchaseConsignmentOrder(updateReqVO);

        IConsignmentFactory strategy = consignmentFactory.generatorStrategy(updateReqVO.getConsignmentType());
        strategy.validateCreate(updateReqVO);

        Long userId = getLoginUserId();
        // 校验存在
        validatePurchaseConsignmentExists(updateReqVO.getId());
        if (updateReqVO.getConsignmentStatus().equals(ConsignmentStatusEnum.CREATE.getStatus())) {
            validatePurchaseConsignmentOrder(updateReqVO);
        }

        // 更新
        ConsignmentDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentDO.class);
        consignmentMapper.updateById(updateObj);

        // 2. 创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(getLoginUserId(), new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(getProcessKey(updateObj.getConsignmentType())).setBusinessKey(updateObj.getId()).setVariables(variables)).getCheckedData();

        // 3. 更新发货工作流编号
        updateObj.setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus()).setConsignmentStatus(ConsignmentStatusEnum.PROCESS.getStatus());

        consignmentMapper.updateById(updateObj);

        updateReqVO.getPurchaseConsignmentDetails().forEach(i -> {
            i.setSignedBy(updateReqVO.getConsignedBy()).setSignedTime(updateReqVO.getConsignerDate());
        });

        // 更新子表
        updatePurchaseConsignmentDetailList(updateObj, updateReqVO.getPurchaseConsignmentDetails());


    }

    /**
     * 删除采购收货
     *
     * @param id 编号
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePurchaseConsignment(String id) {
        // 校验存在
        validatePurchaseConsignmentExists(id);
        // 删除
        consignmentMapper.deleteById(id);

        // 删除子表
        deletePurchaseConsignmentDetailByConsignmentId(id);
    }

    /**
     * 发货单作废
     *
     * @param id
     */
    @Override
    public void cancelPurchaseConsignment(String id) {
        // 校验存在
        validatePurchaseConsignmentExists(id);
        ConsignmentDO consignmentDO = consignmentMapper.selectById(id);

        //如果正在审批 需要取消审批
        if (ConsignmentStatusEnum.PROCESS.getStatus().equals(consignmentDO.getConsignmentStatus())) {
            bpmProcessInstanceApi.cancelProcessInstanceByStartUser(getLoginUserId(), new BpmProcessInstanceCancelReqDto().setId(consignmentDO.getProcessInstanceId()).setReason("发货单已作废"));
        }
        consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
        consignmentMapper.updateById(consignmentDO);

        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoMapper.selectListByConsignmentId(id);
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectListByConsignmentId(id);
        consignmentInfoDOS.forEach(consignmentInfoDO -> {
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
        });
        consignmentDetailDOS.forEach(consignmentDetailDO -> {
            consignmentDetailDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
        });
        consignmentInfoMapper.updateBatch(consignmentInfoDOS);
        consignmentDetailMapper.updateBatch(consignmentDetailDOS);
    }


    /**
     * 校验是否存在采购数据
     *
     * @param id
     * @return
     */
    private ConsignmentDO validatePurchaseConsignmentExists(String id) {
        ConsignmentDO purchaseConsignment = consignmentMapper.selectById(id);
        if (purchaseConsignment == null) {
            throw exception(PURCHASE_CONSIGNMENT_NOT_EXISTS);
        }
        return purchaseConsignment;
    }

    /**
     * 获得采购收货
     *
     * @param id 编号
     * @return
     */
    @Override
    public ConsignmentDO getPurchaseConsignment(String id) {
        return consignmentMapper.getPurchaseConsignment(id);
    }

    /**
     * 获得采购收货分页
     *
     * @param pageReqVO 分页查询
     * @return
     */
    @Override
    public PageResult<ConsignmentDO> getPurchaseConsignmentPage(PurchaseConsignmentPageReqVO pageReqVO) {
        return consignmentMapper.selectPage(pageReqVO);
    }

    /**
     * 通过合同id查询签收明细
     *
     * @param ids
     */
    @Override
    public List<PurchaseConsignmentDTO> getConsignmentDetailByContractIds(Collection<String> ids) {
        ArrayList<Integer> list = Lists.newArrayList(2, 3, 4, 5);
        //通过合同id查询收货
        QueryWrapper<ConsignmentDO> wrapper = new QueryWrapper<>();
        wrapper.in("contract_id", ids);
        wrapper.in("consignment_status", list);
        List<ConsignmentDO> consignmentDOS = consignmentMapper.selectList(wrapper);

        //合同集合
        List<ContractRespDTO> contractRespDTOS = contractApi.getContractList(ids).getCheckedData();
        Map<String, ContractRespDTO> contractMap = CollectionUtils.convertMap(contractRespDTOS, ContractRespDTO::getId);

        //合同供应商信息
        List<String> companyIds = convertList(contractRespDTOS, ContractRespDTO::getParty);
        companyIds = companyIds.stream().distinct().collect(Collectors.toList());
        Map<String, CompanyRespDTO> companyMap = companyApi.getCompanyMap(companyIds);

        List<PurchaseConsignmentDTO> purchaseConsignmentDTOS = BeanUtils.toBean(consignmentDOS, PurchaseConsignmentDTO.class, vo -> {
            //给发货单设置合同方(公司名)
            MapUtils.findAndThen(companyMap, contractMap.get(vo.getContractId()).getParty(), a -> vo.setCompanyName(a.getName()));
            //通过收货id查收货详情
            List<ConsignmentDetailDO> purchaseConsignmentDetailListByConsignmentId = getPurchaseConsignmentDetailListByConsignmentId(vo.getId());
            List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetailDTOS = BeanUtils.toBean(purchaseConsignmentDetailListByConsignmentId, PurchaseConsignmentDetailDTO.class);
            vo.setDetailDTOList(purchaseConsignmentDetailDTOS);
        });

        return purchaseConsignmentDTOS;
    }

    /**
     * 通过合同id查询收货单明细
     *
     * @param id
     */
    @Override
    public List<PurchaseConsignmentDTO> getConsignmentDetailByContractId(String id) {
        // 合同主键获取收货单集合
        List<ConsignmentDO> consignmentDOS = consignmentMapper.selectList(ConsignmentDO::getContractId, id);
        // 存在已收货单据
        if (consignmentDOS.size() > 0) {
            // 获取合同下的所有订单
            List<ContractOrderDO> orderDOList = contractOrderMapper.selectList(ContractOrderDO::getContractId, id);
            Map<String, ContractOrderDO> orderMap = CollectionUtils.convertMap(orderDOList, ContractOrderDO::getId);
            // 物料id集合
            List<String> materialIds = convertList(orderDOList, ContractOrderDO::getMaterialId);
            materialIds = materialIds.stream().distinct().collect(Collectors.toList());
            Map<String, MaterialConfigRespDTO> materialMap = materialMCCApi.getMaterialConfigMap(materialIds);
            //用户
            List<Long> userIdList = StringListUtils.stringListToLongList(convertList(consignmentDOS, ConsignmentDO::getConsignedBy));
            userIdList = userIdList.stream().distinct().collect(Collectors.toList());
            // 收货单主键集合
            List<Long> consignmentIds = StringListUtils.stringListToLongList(convertList(consignmentDOS, ConsignmentDO::getId));
            // 获取收货单明细集合
            List<ConsignmentDetailDO> consignmentDetailList = consignmentDetailMapper.selectList(ConsignmentDetailDO::getConsignmentId, consignmentIds);
            List<Long> singedIds = StringListUtils.stringListToLongList(convertList(consignmentDetailList, ConsignmentDetailDO::getSignedBy));
            if (singedIds != null) userIdList.addAll(singedIds);
            // 拼接数据 用户信息
            Map<Long, AdminUserRespDTO> userMap = userApi.getUserMap(userIdList);
            // 获取合同信息
            ContractDO contract = contractMapper.getContractById(id);
            List<PurchaseConsignmentDTO> purchaseConsignmentDTOS = BeanUtils.toBean(consignmentDOS, PurchaseConsignmentDTO.class, vo -> {
                //给发货单设置合同方(公司名)
                vo.setCompanyName(contract.getPartyName());
                vo.setConsignedBy(userMap.get(NumberUtils.parseLong(vo.getConsignedBy())).getNickname());
                //通过收货id查收货详情
                List<ConsignmentDetailDO> purchaseConsignmentDetailList = consignmentDetailMapper.selectListByConsignmentId(vo.getId());
                List<PurchaseConsignmentDetailDTO> purchaseConsignmentDetailDTOS = BeanUtils.toBean(purchaseConsignmentDetailList, PurchaseConsignmentDetailDTO.class, o -> {
                    if (StringUtils.isNotBlank(o.getSignedBy())) {
                        o.setSignedBy(userMap.get(NumberUtils.parseLong(o.getSignedBy())).getNickname());
                    }
                    MapUtils.findAndThen(orderMap, o.getOrderId(), a -> o.setMaterialId(a.getMaterialId()));
                    MapUtils.findAndThen(materialMap, o.getMaterialId(), a -> o.setMaterialName(a.getMaterialName()).setMaterialNumber(a.getMaterialNumber()).setMaterialUnit(a.getMaterialUnit()));
                });
                vo.setDetailDTOList(purchaseConsignmentDetailDTOS);
            });

            return purchaseConsignmentDTOS;
        }
        return Collections.emptyList();
    }

    @Override
    public List<PurchaseConsignmentDTO> getConsignmentListByProjectIds(Collection<String> ids) {
        //分成两类,一种直接绑项目上发货,一种绑合同上发货(合同绑项目)
        //1.直接通过项目查
//        LambdaQueryWrapperX<ConsignmentDO> wrapperX = new LambdaQueryWrapperX<>();
//        wrapperX.in(ConsignmentDO::getProjectId,ids);
//        List<ConsignmentDO> consignmentDOList = consignmentMapper.selectList(wrapperX);
        //项目id大概率绑在下一层
        LambdaQueryWrapperX<ConsignmentDetailDO> detailWrapperX = new LambdaQueryWrapperX<>();
        detailWrapperX.in(ConsignmentDetailDO::getProjectId,ids);
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(detailWrapperX);
        if(CollectionUtils.isAnyEmpty(consignmentDetailDOS)){
            return Collections.emptyList();
        }
        List<String> consignmentIds = consignmentDetailDOS.stream().map(ConsignmentDetailDO::getConsignmentId).distinct().collect(Collectors.toList());
        List<ConsignmentDO> consignmentDOList = consignmentMapper.selectBatchIds(consignmentIds);

        //2.查项目下的合同，再用合同查
        LambdaQueryWrapperX<ContractDO> contractWrapper = new LambdaQueryWrapperX<>();
        contractWrapper.in(ContractDO::getProjectId,ids);
        List<ContractDO> contractDOList = contractMapper.selectList(contractWrapper);
        if (contractDOList.size()>0){
            List<String> contractIds = contractDOList.stream().map(ContractDO::getId).collect(Collectors.toList());
            LambdaQueryWrapperX<ConsignmentDO> wrapperX2 = new LambdaQueryWrapperX<>();
            wrapperX2.in(ConsignmentDO::getContractId,contractIds);
            List<ConsignmentDO> consignmentDOList2 = consignmentMapper.selectList(wrapperX2);
            consignmentDOList.addAll(consignmentDOList2);
            consignmentDOList = consignmentDOList.stream().distinct().collect(Collectors.toList());
        }
        return BeanUtils.toBean(consignmentDOList, PurchaseConsignmentDTO.class);
    }

    // ==================== 子表（收货明细） ====================

    /**
     * 获得收货明细列表
     *
     * @param consignmentId 收货单ID
     * @return
     */
    @Override
    public List<ConsignmentDetailDO> getPurchaseConsignmentDetailListByConsignmentId(String consignmentId) {
        return consignmentDetailMapper.selectListByConsignmentId(consignmentId);
    }

    @Override
    public List<ConsignmentDetailDO> getConsignmentDetailListByProjectIds(Collection<String> ids) {
        LambdaQueryWrapperX<ConsignmentDetailDO> detailWrapperX = new LambdaQueryWrapperX<>();
        detailWrapperX.in(ConsignmentDetailDO::getProjectId,ids);
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(detailWrapperX);
        return consignmentDetailDOS;
    }

    @Override
    public List<ConsignmentDetailDO> getPurchaseDetailListByContractOrderIds(Collection<String> ids) {
        LambdaQueryWrapperX<ConsignmentDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(ConsignmentDetailDO::getOrderId,ids);
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(wrapperX);

        return consignmentDetailDOS;
    }

    @Override
    public List<PurchaseConsignmentDetailDTO> getPurchaseDetailListByShippingIds(Collection<String> ids) {
        LambdaQueryWrapperX<ConsignmentDetailDO> wrapperX = new LambdaQueryWrapperX<>();
        wrapperX.in(ConsignmentDetailDO::getShippingDetailId,ids);
        List<ConsignmentDetailDO> consignmentDetailDOS = consignmentDetailMapper.selectList(wrapperX);
        List<PurchaseConsignmentDetailDTO> list = BeanUtils.toBean(consignmentDetailDOS, PurchaseConsignmentDetailDTO.class);
        return list;
    }

    @Override
    public List<ConsignmentInfoDO> getPurchaseConsignmentInfoListByConsignmentId(String consignmentId) {
        return consignmentInfoMapper.selectListByConsignmentId(consignmentId);
    }

    /**
     * 提交采购审批
     *
     * @param id
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = PurchaseConsignment_CLUE_TYPE, subType = PurchaseConsignment_SUBMIT_SUB_TYPE, bizNo = "{{#id}}",
            success = PurchaseConsignment_SUBMIT_SUCCESS)
    public void submitPurchaseConsignment(String id, Long userId) {
        //校验采购单是否在审批
        ConsignmentDO consignmentDO = validatePurchaseConsignmentExists(id);
        if (!(ObjUtil.equals(consignmentDO.getStatus(), PpmAuditStatusEnum.DRAFT.getStatus()) || ObjUtil.equals(consignmentDO.getStatus(), PpmAuditStatusEnum.REJECT.getStatus()))) {
            throw exception(CONSIGNMENT_SUBMIT_FAIL_NOT_DRAFT);
        }

        //创建发货审批流程实例
        Map<String, Object> variables = new HashMap<>();
        String processInstanceId = bpmProcessInstanceApi.createProcessInstance(userId, new BpmProcessInstanceCreateReqDTO()
                .setProcessDefinitionKey(getProcessKey(consignmentDO.getConsignmentType())).setBusinessKey(String.valueOf(id)).setVariables(variables)).getCheckedData();

        //更新发货工作流编号
        consignmentMapper.updateById(new ConsignmentDO().setId(id).setProcessInstanceId(processInstanceId)
                .setStatus(PpmAuditStatusEnum.PROCESS.getStatus()).setConsignmentStatus(ConsignmentStatusEnum.PROCESS.getStatus()));

        // 4. 记录日志
        LogRecordContext.putVariable("consignmentNo", consignmentDO.getNo());
    }

    /**
     * 更新审批状态
     *
     * @param id     编号
     * @param status 审批结果
     */
    @Override
    public void updatePurchaseConsignmentStatus(String id, Integer status) {
        ConsignmentDO consignmentDO = validatePurchaseConsignmentExists(id);
        consignmentDO.setId(id);
        if (PpmAuditStatusEnum.APPROVE.getStatus().equals(status)) {
            consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.SINGING.getStatus()).setStatus(PpmAuditStatusEnum.APPROVE.getStatus());

        } else {
            consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.CANCEL.getStatus()).setStatus(PpmAuditStatusEnum.APPROVE.getStatus());
        }
        updateConsignmentStatus(consignmentDO);
    }

    /**
     * 确认收货
     *
     * @param id
     * @param consignmentStatus
     */
    @Override
    public void updateConsignmentStatus(String id, Integer consignmentStatus) {
        validatePurchaseConsignmentExists(id);
        consignmentMapper.updateById(new ConsignmentDO().setId(id).setConsignmentStatus(consignmentStatus));
    }

    @Override
    public Map<String, BigDecimal> getOrderMap(String contractId, List<String> detailIds, List<Integer> status) {

        //查询合同下  所有已发货的采购单(如果detailIds 不为空 则需要排除掉)
        List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.getInboundOderByContractId(contractId, detailIds, status);
        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = new HashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            detailDOS.forEach(purchaseConsignmentDetailDO -> {

                if (numberMap.get(purchaseConsignmentDetailDO.getOrderId()) == null) {
                    numberMap.put(purchaseConsignmentDetailDO.getOrderId(), purchaseConsignmentDetailDO.getConsignedAmount());
                } else {
                    numberMap.get(purchaseConsignmentDetailDO.getOrderId()).add(purchaseConsignmentDetailDO.getConsignedAmount());
                }
            });
        }

        return numberMap;
    }

    /**
     * 新增采购子表数据
     *
     * @param consignmentDO
     * @param list
     */
    private void createPurchaseConsignmentDetailList(ConsignmentDO consignmentDO, List<PurchaseConsignmentDetailSaveReqVO> list) {
        list.forEach(o -> o.setConsignmentId(consignmentDO.getId()));

        Map<String, List<PurchaseConsignmentDetailSaveReqVO>> map = new HashMap<>();
        for (PurchaseConsignmentDetailSaveReqVO detailSaveReqVO : list) {
            detailSaveReqVO.setConsignmentType(consignmentDO.getConsignmentType());
            detailSaveReqVO.setConsignmentStatus(consignmentDO.getConsignmentStatus());
            if (org.springframework.util.CollectionUtils.isEmpty(map.get(detailSaveReqVO.getMaterialConfigId()+"_"+detailSaveReqVO.getProjectOrderId()))) {
                map.put(detailSaveReqVO.getMaterialConfigId()+"_"+detailSaveReqVO.getProjectOrderId(), Lists.newArrayList(detailSaveReqVO));
            } else {
                map.get(detailSaveReqVO.getMaterialConfigId()+"_"+detailSaveReqVO.getProjectOrderId()).add(detailSaveReqVO);
            }
        }
        for (Map.Entry<String, List<PurchaseConsignmentDetailSaveReqVO>> entry : map.entrySet()) {


            List<PurchaseConsignmentDetailSaveReqVO> detailDOS = entry.getValue();
            BigDecimal number = new BigDecimal(0);
            for (PurchaseConsignmentDetailSaveReqVO detailDO : detailDOS) {
                number = number.add(detailDO.getConsignedAmount());
            }

            ConsignmentInfoDO infoDO = BeanUtils.toBean(detailDOS.get(0), ConsignmentInfoDO.class);
            infoDO.setConsignedAmount(number);


            consignmentInfoMapper.insert(infoDO);
            List<ConsignmentDetailDO> detailDOList =new ArrayList<>();
            if (consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.PURCHASE.getStatus()) ||consignmentDO.getConsignmentType().equals(ConsignmentTypeEnum.ORDER.getStatus()) ){
                for (int i =0 ;i<number.intValue();i++){
                    ConsignmentDetailDO detailDO =BeanUtils.toBean(detailDOS.get(0),ConsignmentDetailDO.class);
                    detailDO.setConsignedAmount(new BigDecimal(1));
                    detailDOList.add(detailDO);
                }

            }else {
                detailDOList.addAll(BeanUtils.toBean(detailDOS,ConsignmentDetailDO.class));
            }

            detailDOList.forEach(o -> o.setInfoId(infoDO.getId()));
            consignmentDetailMapper.insertBatch(detailDOList);

        }
    }

    /**
     * 更新采购子表数据
     *
     * @param consignmentDO
     * @param list
     */
    private void updatePurchaseConsignmentDetailList(ConsignmentDO consignmentDO, List<PurchaseConsignmentDetailSaveReqVO> list) {

        //删除子表数据
        deletePurchaseConsignmentDetail(consignmentDO.getId());

        //更新数据
        list.forEach(o -> o.setId(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createPurchaseConsignmentDetailList(consignmentDO, list);
    }


    /**
     * 删除子表数据(物理删除)
     */
    private void deletePurchaseConsignmentDetail(String consignmentId) {
        consignmentDetailMapper.deletePurchaseConsignmentDetail(consignmentId);
        consignmentInfoMapper.deletePurchaseConsignmentInfo(consignmentId);
    }

    /**
     * 删除子表数据
     *
     * @param consignmentId
     */
    private void deletePurchaseConsignmentDetailByConsignmentId(String consignmentId) {
        consignmentDetailMapper.deleteByConsignmentId(consignmentId);
        consignmentInfoMapper.deletePurchaseConsignmentInfo(consignmentId);
    }

    /**
     * 验证收货单订单信息
     *
     * @param vo
     */
    private void validatePurchaseConsignmentOrder(PurchaseConsignmentSaveReqVO vo) {


        if (!vo.getConsignmentType().equals(ConsignmentTypeEnum.PURCHASE.getStatus()) && !vo.getConsignmentType().equals(ConsignmentTypeEnum.OUT.getStatus()) ){
            return;
        }

        //查询合同信息
        CommonResult<List<ContractOrderRespDTO>> list = contractApi.getOrderList(vo.getContractId());

        Map<String, ContractOrderRespDTO> orderMap = CollectionUtils.convertMap(list.getCheckedData(), ContractOrderRespDTO::getId);

        List<String> detailIds = new ArrayList<>();
        if (!StringUtils.isEmpty(vo.getId())) {
            detailIds.addAll(convertList(vo.getPurchaseConsignmentDetails(), PurchaseConsignmentDetailSaveReqVO::getId));
        }

        //查询合同下所有未入库的发货单
        List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.getInboundOderByContractId(vo.getContractId(), detailIds, Lists.newArrayList(0, 1, 2, 3, 4));

        //计算每个订单已经发货的数量
        Map<String, BigDecimal> numberMap = new HashMap<>();

        if (!org.springframework.util.CollectionUtils.isEmpty(detailDOS)) {
            detailDOS.forEach(purchaseConsignmentDetailDO -> {

                if (numberMap.get(purchaseConsignmentDetailDO.getOrderId()) == null) {
                    numberMap.put(purchaseConsignmentDetailDO.getOrderId(), purchaseConsignmentDetailDO.getConsignedAmount());
                } else {
                    numberMap.get(purchaseConsignmentDetailDO.getOrderId()).add(purchaseConsignmentDetailDO.getConsignedAmount());
                }
            });
        }

        for (PurchaseConsignmentDetailSaveReqVO consignmentDetailDO : vo.getPurchaseConsignmentDetails()) {

            if (orderMap.get(consignmentDetailDO.getOrderId()) == null) {
                throw exception(CONSIGNMENT_ORDER_NOT_EXISTS);
            }

            //如果已有的收货单加上新建的收货单 订单发货数量大于合同的数量
            //则不允许创建
            //TODO  之后要加上退货单的信息
            BigDecimal outNumber = new BigDecimal(0);
            if (numberMap.get(consignmentDetailDO.getOrderId()) != null) {
                outNumber.add(numberMap.get(consignmentDetailDO.getOrderId()));
            }
            if (consignmentDetailDO.getConsignedAmount().add(outNumber).compareTo(orderMap.get(consignmentDetailDO.getOrderId()).getQuantity()) > 0) {
                throw exception(CONSIGNMENT_ORDER_EXCEED);
            }

        }
    }

    /**
     * 查询收货单
     *
     * @param id
     * @return
     */
    @Override
    public List<ConsignmentDO> getConsignmentByContract(String id) {
        return consignmentMapper.getConsignmentByContract(id);
    }


    @Override
    public ConsignmentDO queryConsignmentByNo(String consignmentNo) {
        return consignmentMapper.queryConsignmentByNo(consignmentNo);
    }

    @Override
    public void updatePurchaseConsignmentQms(@Valid PurchaseConsignmentSaveReqVO updateReqVO) {

        for (PurchaseConsignmentDetailSaveReqVO detailDO : updateReqVO.getPurchaseConsignmentDetails()) {
            InspectionSheetReqDTO dto = new InspectionSheetReqDTO();
            dto.setRecordNumber(updateReqVO.getNo());
            dto.setMaterialConfigId(detailDO.getMaterialConfigId());
            dto.setQuantity(detailDO.getSignedAmount().intValue());
            dto.setSchemeType(InspectionSchemeTypeEnum.PURCHASETYPE.getStatus());
            dto.setSchemeId(detailDO.getSchemeId());
            CommonResult<String> sheetResult = inspectionSheetApi.createInspectionSheet(dto);
        }

        ConsignmentDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentDO.class);
        updateObj.setConsignmentStatus(ConsignmentStatusEnum.ANALYSISING.getStatus());


        consignmentMapper.updateById(updateObj);
    }

    @Override
    public void checkSchemeSheetResult() {


        //获取所有检验中的入库单
        List<ConsignmentDO> consignmentDOS = consignmentMapper.selectList(ConsignmentDO::getConsignmentStatus, ConsignmentStatusEnum.ANALYSISING.getStatus());


        if (!org.springframework.util.CollectionUtils.isEmpty(consignmentDOS)) {

            List<OrderReqDTO> reqDTOS = CollectionUtils.convertList(consignmentDOS, purchaseConsignmentDO ->
            {
                OrderReqDTO orderReqDTO = new OrderReqDTO().setOrderNumber(purchaseConsignmentDO.getNo()).setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_IN);
                return orderReqDTO;
            });
            CommonResult<List<OrderReqDTO>> orderList = orderApi.orderList(reqDTOS);
            Map<String,List<OrderReqDTO>> listMap = new HashMap<>();

            for (OrderReqDTO dto :orderList.getCheckedData()){
                if (com.alibaba.nacos.common.utils.CollectionUtils.isEmpty(listMap.get(dto.getOrderNumber()))){
                    listMap.put(dto.getOrderNumber(),Lists.newArrayList(dto));
                }else {
                    listMap.get(dto.getOrderNumber()).add(dto);
                }

            }

            List<String> consignmentNo = CollectionUtils.convertList(consignmentDOS, ConsignmentDO::getNo);
            //查询对应的质检单
            CommonResult<List<InspectionSheetRespDTO>> result = inspectionSheetApi.getInspectionSheetListByRecordNumber(consignmentNo);

            Map<String, InspectionSheetRespDTO> map = new HashMap<>();
            for (InspectionSheetRespDTO dto : result.getCheckedData()) {
                map.put(dto.getRecordNumber() + "_" + dto.getSchemes().get(0).getMaterialConfigId(), dto);
            }

            //如果质检单完成
            List<OrderReqDTO> orderReqDTOList = new ArrayList<>();
            List<ConsignmentDO> finishList = new ArrayList<>();
            for (ConsignmentDO consignmentDO : consignmentDOS) {

                List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.queryConsignmentDetailIdByConsignmentId(consignmentDO.getId());

                Boolean finish = true;
                List<String> materialIds = new ArrayList<>();
                for (ConsignmentDetailDO detailDO : detailDOS) {

                   // ContractOrderDO contractOrderDO = contractOrderMapper.selectById(detailDO.getOrderId());

                    InspectionSheetRespDTO dto = map.get(consignmentDO.getNo() + "_" + detailDO.getMaterialConfigId());

                    if (dto != null) {
                        if (dto.getStatus().intValue() != 3) {//如果质检单状态不为3 则视为没完成
                            finish = false;
                        } else {
                            materialIds.add(detailDO.getMaterialConfigId());
                        }
                    }

                }

                if (finish) {
                    //如果质检单完成  则调用WMS  通知状态
                    if (!org.springframework.util.CollectionUtils.isEmpty(materialIds)) {

                        List<OrderReqDTO> dtos = listMap.get(consignmentDO.getNo());

                        for (OrderReqDTO dto :dtos){
                            dto.setOrderStatus(1);//待质检
                            dto.setChooseStockId(dto.getMaterialStockId());
                            orderReqDTOList.add(dto);
                        }
                    }
                    consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.INBOUND.getStatus());
                    finishList.add(consignmentDO);
                }


            }

            if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOList)) {
                //更新入库状态为待入库
                CommonResult<List<String>> commonResult = orderApi.orderUpdateStatus(orderReqDTOList);
                //更新收货单状态完成
                if (commonResult.isSuccess()) {
                    consignmentMapper.updateBatch(finishList);
                }

            }


        }


        //
    }

    @Override
    public void checkSchemeSheet() {
        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoMapper.selectListBySheet();
        for (ConsignmentInfoDO infoDO:consignmentInfoDOS){
            TaskDTO taskDTO = new TaskDTO();
            taskDTO.setRecordNumber(infoDO.getNo());
            taskDTO.setSheetName(infoDO.getName());
            taskDTO.setMaterialConfigId(infoDO.getMaterialConfigId());
            taskDTO.setSourceType(infoDO.getConsignmentType());
            taskDTO.setSchemeType(1);
            List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.getDetailsForQMS(infoDO.getId());
            List<TaskDTO.Detail> details = new ArrayList<>();
            for (ConsignmentDetailDO detailDO :detailDOS){
                TaskDTO.Detail detail = new TaskDTO.Detail();
                detail.setMaterialId(detailDO.getMaterialStockId());
                detail.setBarCode(detailDO.getBarCode());
                detail.setBatchNumber(detailDO.getBatchNumber());
                details.add(detail);
            }
            taskDTO.setBatchNumber(detailDOS.get(0).getBatchNumber());
            taskDTO.setDetails(details);
            CommonResult<String> result =  inspectionSheetTaskApi.createInspectionSheetTask(taskDTO);
            if (result.isSuccess()){
                infoDO.setSchemeResult(1);
            }
            consignmentInfoMapper.updateById(infoDO);
        }

    }

    @Override
    public void checkInBoundInfo() {
        //获取所有入库中的入库单
        List<ConsignmentDO> consignmentDOS = consignmentMapper.selectList(ConsignmentDO::getConsignmentStatus, ConsignmentStatusEnum.INBOUND.getStatus());
        if (!org.springframework.util.CollectionUtils.isEmpty(consignmentDOS)) {
            List<OrderReqDTO> list = cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList(consignmentDOS, purchaseConsignmentDO -> {
                OrderReqDTO dto = new OrderReqDTO();
                dto.setOrderNumber(purchaseConsignmentDO.getNo());
                dto.setOrderType(DictConstants.WMS_ORDER_TYPE_PURCHASE_IN);
                return dto;
            });
            //查询发货单对应的出库单信息
            CommonResult<List<OrderReqDTO>> result = orderApi.orderList(list);


            //筛选出出库完成的单子
            List<OrderReqDTO> orderReqDTOList = result.getCheckedData().stream().filter(orderReqDTO -> orderReqDTO.getOrderStatus().equals(DictConstants.WMS_ORDER_DETAIL_STATUS_4)).collect(Collectors.toList());

            if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOList)) {
                Map<String, List<OrderReqDTO>> orderMap = result.getCheckedData().stream().collect(Collectors.groupingBy(OrderReqDTO::getOrderNumber));


                for (ConsignmentDO consignmentDO : consignmentDOS) {

                    ContractDO contractDO = contractMapper.getContractById(consignmentDO.getContractId());
                    List<OrderReqDTO> orderReqDTOS = orderMap.get(consignmentDO.getNo());
                    Boolean finish = true;
                    List<OrderMaterialRelationUpdateDTO> relationSaveReqDTOS = new ArrayList<>();
                    List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.queryConsignmentDetailIdByConsignmentId(consignmentDO.getId());




                    if (!org.springframework.util.CollectionUtils.isEmpty(orderReqDTOS)) {

                        Map<String, List<OrderReqDTO>> orderReqDTOMap =new HashMap<>();

                        for (OrderReqDTO reqDTO : orderReqDTOS){
                            if (org.springframework.util.CollectionUtils.isEmpty(orderReqDTOMap.get(reqDTO.getMaterialConfigId()))){
                                orderReqDTOMap.put(reqDTO.getMaterialConfigId(),Lists.newArrayList(reqDTO));
                            }else {
                                orderReqDTOMap.get(reqDTO.getMaterialConfigId()).add(reqDTO);
                            }

                        }

                        for (ConsignmentDetailDO detailDO : detailDOS) {
                            List<OrderReqDTO> dtos = orderReqDTOMap.get(detailDO.getMaterialConfigId());
                            if (org.springframework.util.CollectionUtils.isEmpty(dtos)) {//找不到完成的出库信息  则视为没完成
                                finish = false;
                            }else if (dtos.size() !=detailDO.getSignedAmount().intValue()){
                                finish = false; //数量对应不上则视为没完成
                            }else {
                                //如果有订单ID说明是毛坯  则需要初始化
                                if (StringUtils.isNotBlank(detailDO.getProjectOrderId())){
                                    for (OrderReqDTO dto : dtos){
                                        OrderMaterialRelationUpdateDTO reqDTO = new OrderMaterialRelationUpdateDTO();
                                        reqDTO.setOrderId(detailDO.getProjectOrderId());
                                        reqDTO.setProjectId(detailDO.getProjectId());
                                        reqDTO.setPlanId(detailDO.getProjectPlanId());
                                        reqDTO.setPlanItemId(detailDO.getProjectPlanItemId());
                                        CommonResult<MaterialStockRespDTO> respDTOCommonResult = materialStockApi.getById(dto.getMaterialStockId());
                                        reqDTO.setMaterialCode(respDTOCommonResult.getCheckedData().getBarCode());
                                        reqDTO.setVariableCode(respDTOCommonResult.getCheckedData().getBarCode());
                                        relationSaveReqDTOS.add(reqDTO);
                                    }
                                }
                            }
                        }
                    } else {
                        finish = false;
                    }

                    if (finish){//更新状态为完成  并
                        //todo  调用PMS 更新二维码
                        Boolean res = true;
                        String msg = "";
                        if (!org.springframework.util.CollectionUtils.isEmpty(relationSaveReqDTOS)){
                            //区分普通采购 和毛坯物料采购


                            //如果是物料采购  则初始化  如果是外协 则需要更新
                            if (contractDO.getContractType().intValue()==1){
                                CommonResult<String> commonResult= pmsOrderMaterialRelationApi.orderMaterialInitBatch(relationSaveReqDTOS);
                                res = commonResult.isSuccess();
                                msg =commonResult.getMsg();
                            }else {
                                CommonResult<String> commonResult  =pmsOrderMaterialRelationApi.outSourceUpdate(relationSaveReqDTOS);
                                res = commonResult.isSuccess();
                                msg =commonResult.getMsg();
                            }

                        }
                        if (res){
                            consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());

                            consignmentMapper.updateById(consignmentDO);
                        }else {
                            logger.error("入库完成调用PMS失败==="+msg);
                        }


                    }

                }

            }


        }
    }

    @Override
    public void checkInBoundInfo1() throws InterruptedException {


        for (int i = 0;i<10;i++){
            GeneratorCodeReqDTO reqDTO = new GeneratorCodeReqDTO();
            reqDTO.setClassificationCode("PM_PO");
            reqDTO.setEncodingRuleType(1);
            encodingRuleApi.generatorCode(reqDTO);
        }

    }

    @Override
    public List<ConsignmentDO> getConsignmentDetailByIds(Collection<String> ids) {
        return consignmentMapper.selectBatchIds(ids);
    }

    @Override
    public void updateConsignmentStatus(ConsignmentDO consignmentDO) {


        consignmentMapper.updateById(consignmentDO);
        List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.selectList(ConsignmentDetailDO::getConsignmentId,consignmentDO.getId());
        detailDOS.forEach(shippingDetailDO -> {
            shippingDetailDO.setConsignmentStatus(consignmentDO.getConsignmentStatus());

        });
        List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoMapper.selectList(ConsignmentInfoDO::getConsignmentId,consignmentDO.getId());
        consignmentInfoDOS.forEach(shippingDetailDO -> {
            shippingDetailDO.setConsignmentStatus(consignmentDO.getConsignmentStatus());

        });
        consignmentDetailMapper.updateBatch(detailDOS);
        consignmentInfoMapper.updateBatch(consignmentInfoDOS);

    }

    @Override
    public List<ConsignmentDO> getConsignments(ConsignmentInfoQueryReqVO reqVO) {
        return consignmentMapper.getConsignments(reqVO);
    }


    private String getProcessKey(Integer consignmentType) {

        if (ConsignmentTypeEnum.PURCHASE.getStatus().equals(consignmentType)){
            return CONSIGNMENT_PROCESS_KEY;
        }else if (ConsignmentTypeEnum.OUT.getStatus().equals(consignmentType)){
            return CONSIGNMENT_PROCESS_KEY;
        }else if (ConsignmentTypeEnum.OUT_MATERIAL.getStatus().equals(consignmentType)){
            return CONTRACT_CONSIGNMENT_RETURN_PROCESS_KEY;
        }else if (ConsignmentTypeEnum.ORDER.getStatus().equals(consignmentType)){
            return SHIPPING_INSTORAGE_PROCESS_KEY;
        }else if (ConsignmentTypeEnum.SHIPPING_RETURN.getStatus().equals(consignmentType)){
            return RETURN_PROCESS_KEY;
        }

        return null;

    }

}
