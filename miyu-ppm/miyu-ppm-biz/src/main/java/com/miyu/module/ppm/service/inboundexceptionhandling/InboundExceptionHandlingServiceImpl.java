package com.miyu.module.ppm.service.inboundexceptionhandling;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.dal.mysql.consignmentinfo.ConsignmentInfoMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.mateiral.dto.ReceiveMaterialReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo.*;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.inboundexceptionhandling.InboundExceptionHandlingMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 入库异常处理 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class InboundExceptionHandlingServiceImpl implements InboundExceptionHandlingService {

    @Resource
    private InboundExceptionHandlingMapper inboundExceptionHandlingMapper;
    @Resource
    private ConsignmentInfoMapper consignmentInfoMapper;
    @Resource
    private ConsignmentMapper consignmentMapper;
    @Resource
    private ContractService contractService;
    @Resource
    private CompanyProductService companyProductService;
    @Resource
    private MaterialStockApi materialStockApi;

    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;

    @Override
    public String createInboundExceptionHandling(InboundExceptionHandlingSaveReqVO createReqVO) {
        // 插入
        InboundExceptionHandlingDO inboundExceptionHandling = BeanUtils.toBean(createReqVO, InboundExceptionHandlingDO.class);
        inboundExceptionHandlingMapper.insert(inboundExceptionHandling);
        // 返回
        return inboundExceptionHandling.getId();
    }

    @Override
    public void updateInboundExceptionHandling(InboundExceptionHandlingSaveReqVO updateReqVO) {
        // 校验存在
        validateInboundExceptionHandlingExists(updateReqVO.getId());
        // 更新
        InboundExceptionHandlingDO updateObj = BeanUtils.toBean(updateReqVO, InboundExceptionHandlingDO.class);
        inboundExceptionHandlingMapper.updateById(updateObj);
    }

    @Override
    public void deleteInboundExceptionHandling(String id) {
        // 校验存在
        validateInboundExceptionHandlingExists(id);
        // 删除
        inboundExceptionHandlingMapper.deleteById(id);
    }

    @Override
    public void handleInboundException(String id, Integer resultType) {
        InboundExceptionHandlingDO handlingDO = validateInboundExceptionHandlingExists(id);
        handlingDO.setStatus(1);
        handlingDO.setRusultType(resultType);
        ConsignmentInfoDO consignmentInfoDO = consignmentInfoMapper.selectById(handlingDO.getInfoId());
        List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.selectList(ConsignmentDetailDO::getInfoId, consignmentInfoDO.getId());
        ConsignmentDO consignmentDO = consignmentMapper.selectById(consignmentInfoDO.getConsignmentId());
        if (resultType.intValue() == 1) {//入库

            ContractDO contractDO = contractService.getContractById(consignmentDO.getContractId());
            //1
            //1.1验证是否需要质检
            Boolean needCheck = true;
            if (consignmentInfoDO.getConsignmentType().equals(ConsignmentTypeEnum.PURCHASE.getStatus())) {
                //采购收货 需要查看合同方对应的产品是否免检   其他的收货都需要质检
                List<CompanyProductDO> companyProductDOS = companyProductService.queryCompanyProductByParty(contractDO.getParty(), Lists.newArrayList(consignmentInfoDO.getMaterialConfigId()));

                CompanyProductDO productDO = companyProductDOS.get(0);
                if (productDO.getQualityCheck().intValue() == 1) {
                    needCheck = false;
                }
            }
            List<ReceiveMaterialReqDTO> receiveMaterialReqDTOList = new ArrayList<>();
            //调用WMS生码逻辑

            if (handlingDO.getExceptionType().intValue() == 1) {// 来货不足
                for (ConsignmentDetailDO detailDO : detailDOS) {
                    //待确认的生码
                    if (detailDO.getConsignmentStatus().equals(ConsignmentStatusEnum.CONFIRM.getStatus())) {
                        ReceiveMaterialReqDTO receiveMaterialReqDTO = new ReceiveMaterialReqDTO();
                        receiveMaterialReqDTO.setBarCode(detailDO.getBarCode());
                        //todo  具体参数参考
                        receiveMaterialReqDTO.setMaterialStatus(needCheck ? 1 : 2);
                        receiveMaterialReqDTO.setLocationId(detailDO.getLocationId());
                        receiveMaterialReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                        receiveMaterialReqDTOList.add(receiveMaterialReqDTO);
                    }

                }
            } else {//来货过多
                int value = handlingDO.getSignedAmount().subtract(handlingDO.getConsignedAmount()).intValue();

                List<ConsignmentDetailDO> newDetails = new ArrayList<>();
                for (int i = 0; i < value; i++) {
                    ConsignmentDetailDO detailDO = BeanUtils.toBean(detailDOS.get(0),ConsignmentDetailDO.class);
                    detailDO.setId(null);
                    //consignmentDetailMapper.insert(detailDO);
                    newDetails.add(detailDO);
                }
                detailDOS.addAll(newDetails);
                for (ConsignmentDetailDO detailDO : detailDOS) {
                    ReceiveMaterialReqDTO receiveMaterialReqDTO = new ReceiveMaterialReqDTO();
                    receiveMaterialReqDTO.setBarCode(detailDO.getBarCode());
                    //todo  具体参数参考
                    receiveMaterialReqDTO.setMaterialStatus(needCheck ? 1 : 2);
                    receiveMaterialReqDTO.setLocationId(detailDO.getLocationId());
                    receiveMaterialReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                    receiveMaterialReqDTOList.add(receiveMaterialReqDTO);

                }
            }

            CommonResult<List<MaterialStockRespDTO>> commonResult = materialStockApi.receiveMaterial(receiveMaterialReqDTOList);
            List<ConsignmentDetailDO> detailDOList = new ArrayList<>();
            if (commonResult.isSuccess()) {
                //更新码记录
                List<MaterialStockRespDTO> list = commonResult.getCheckedData();
                Map<String, MaterialStockRespDTO> map = CollectionUtils.convertMap(list, MaterialStockRespDTO::getBarCode);
                for (int i = 0; i < detailDOS.size(); i++) {
                    ConsignmentDetailDO detailDO = detailDOS.get(i);
                    //无码
                    if (ConsignmentTypeEnum.PURCHASE.getStatus().equals(detailDO.getConsignmentType()) || ConsignmentTypeEnum.ORDER.getStatus().equals(detailDO.getConsignmentType())) {

                        if (i<list.size()) {
                            MaterialStockRespDTO respDTO = list.get(i);
                            detailDO.setMaterialStockId(respDTO.getId());
                            detailDO.setBarCode(respDTO.getBarCode());
                            detailDO.setBatchNumber(respDTO.getBatchNumber());
                            detailDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
                        } else {
                            detailDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
                        }
                        detailDOList.add(detailDO);
                    } else {//有码
                        MaterialStockRespDTO respDTO = map.get(detailDO.getBarCode());
                        if (respDTO != null) {
                            detailDO.setMaterialStockId(respDTO.getId());
                            detailDO.setBarCode(respDTO.getBarCode());
                            detailDO.setBatchNumber(respDTO.getBatchNumber());
                            detailDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
                        } else {
                            detailDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
                        }
                        detailDOList.add(detailDO);

                    }
                }

            } else {
                throw exception(CONSIGNMENT_SIGN_ERROR);
            }


            // 2。更新状态
            consignmentInfoDO.setSchemeResult(needCheck ? 0 : 1);
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());

            consignmentInfoMapper.updateById(consignmentInfoDO);
            detailDOList.forEach(consignmentDetailDO -> {
                if (StringUtils.isEmpty(consignmentDetailDO.getId())){
                    consignmentDetailMapper.insert(consignmentDetailDO);
                }else {
                    consignmentDetailMapper.updateById(consignmentDetailDO);
                }
            });

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

        } else {// 退货
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.RETURN.getStatus());
            detailDOS.forEach(consignmentDetailDO -> {
                if(ConsignmentStatusEnum.CONFIRM.getStatus().equals(consignmentDetailDO.getConsignmentStatus())){
                    consignmentDetailDO.setConsignmentStatus(ConsignmentStatusEnum.RETURN.getStatus());
                }
            });
            consignmentInfoMapper.updateById(consignmentInfoDO);
            consignmentDetailMapper.updateBatch(detailDOS);


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

        }

        inboundExceptionHandlingMapper.updateById(handlingDO);

    }

    private InboundExceptionHandlingDO validateInboundExceptionHandlingExists(String id) {
        InboundExceptionHandlingDO inboundExceptionHandlingDO = inboundExceptionHandlingMapper.selectById(id);
        if (inboundExceptionHandlingDO == null) {
            throw exception(INBOUND_EXCEPTION_HANDLING_NOT_EXISTS);
        }
        return inboundExceptionHandlingDO;
    }

    @Override
    public InboundExceptionHandlingDO getInboundExceptionHandling(String id) {
        return inboundExceptionHandlingMapper.selectById(id);
    }

    @Override
    public PageResult<InboundExceptionHandlingDO> getInboundExceptionHandlingPage(InboundExceptionHandlingPageReqVO pageReqVO) {
        return inboundExceptionHandlingMapper.selectPage(pageReqVO);
    }

}