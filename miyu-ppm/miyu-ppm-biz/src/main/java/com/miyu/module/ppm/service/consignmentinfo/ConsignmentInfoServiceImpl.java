package com.miyu.module.ppm.service.consignmentinfo;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.google.common.collect.Lists;
import com.miyu.module.ppm.controller.admin.home.vo.ConsignmentCompanyNumberRespVO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.inboundexceptionhandling.InboundExceptionHandlingDO;
import com.miyu.module.ppm.dal.dataobject.shippinginfo.ShippingInfoDO;
import com.miyu.module.ppm.dal.mysql.consignment.ConsignmentMapper;
import com.miyu.module.ppm.dal.mysql.consignmentdetail.ConsignmentDetailMapper;
import com.miyu.module.ppm.dal.mysql.inboundexceptionhandling.InboundExceptionHandlingMapper;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentStatusEnum;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.service.companyproduct.CompanyProductService;
import com.miyu.module.ppm.service.contract.ContractService;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.mateiral.dto.ReceiveMaterialReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import com.miyu.module.ppm.controller.admin.consignmentinfo.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentinfo.ConsignmentInfoDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.ppm.dal.mysql.consignmentinfo.ConsignmentInfoMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.*;

/**
 * 收货产品 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class ConsignmentInfoServiceImpl implements ConsignmentInfoService {

    @Resource
    private ConsignmentInfoMapper consignmentInfoMapper;
    @Resource
    private ConsignmentDetailMapper consignmentDetailMapper;
    @Resource
    private ConsignmentMapper consignmentMapper;
    @Resource
    private ContractService contractService;
    @Resource
    private CompanyProductService companyProductService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private InboundExceptionHandlingMapper inboundExceptionHandlingMapper;

    @Override
    public String createConsignmentInfo(ConsignmentInfoSaveReqVO createReqVO) {
        // 插入
        ConsignmentInfoDO consignmentInfo = BeanUtils.toBean(createReqVO, ConsignmentInfoDO.class);
        consignmentInfoMapper.insert(consignmentInfo);
        // 返回
        return consignmentInfo.getId();
    }

    @Override
    public void updateConsignmentInfo(ConsignmentInfoSaveReqVO updateReqVO) {
        // 校验存在
        validateConsignmentInfoExists(updateReqVO.getId());
        // 更新
        ConsignmentInfoDO updateObj = BeanUtils.toBean(updateReqVO, ConsignmentInfoDO.class);
        consignmentInfoMapper.updateById(updateObj);
    }

    @Override
    public void signInfo(@Valid ConsignmentInfoSaveReqVO updateReqVO) {
        ConsignmentInfoDO consignmentInfoDO = consignmentInfoMapper.selectById(updateReqVO.getId());
        consignmentInfoDO.setSignedAmount(updateReqVO.getSignedAmount());
        consignmentInfoDO.setSignedBy(getLoginUserId().toString());
        consignmentInfoDO.setSignedTime(LocalDateTime.now());
        consignmentInfoDO.setLocationId(updateReqVO.getLocationId());
        List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.selectList(ConsignmentDetailDO::getInfoId, updateReqVO.getId());
        ContractDO contractDO = contractService.getContractById(consignmentInfoDO.getContractId());
        ConsignmentDO consignmentDO = consignmentMapper.selectById(consignmentInfoDO.getConsignmentId());
        if (consignmentInfoDO.getConsignedAmount().compareTo(updateReqVO.getSignedAmount()) == 0){
            //签收数量和收货数量一致
            //1.生码
            //1.1验证是否需要质检
            Boolean needCheck = true;
            if (consignmentInfoDO.getConsignmentType().equals(ConsignmentTypeEnum.PURCHASE.getStatus())){
                //采购收货 需要查看合同方对应的产品是否免检   其他的收货都需要质检

                List<CompanyProductDO> companyProductDOS = companyProductService.queryCompanyProductByParty(contractDO.getParty(), Lists.newArrayList(consignmentInfoDO.getMaterialConfigId()));

                CompanyProductDO productDO = companyProductDOS.get(0);
                if (productDO.getQualityCheck().intValue() == 1 ){
                    needCheck = false;
                }
            }
            List<ReceiveMaterialReqDTO> receiveMaterialReqDTOList = new ArrayList<>();
            //调用WMS生码逻辑
            for (ConsignmentDetailDO detailDO :detailDOS){
                ReceiveMaterialReqDTO receiveMaterialReqDTO = new ReceiveMaterialReqDTO();
                receiveMaterialReqDTO.setBarCode(detailDO.getBarCode());
                //todo  具体参数参考
                receiveMaterialReqDTO.setMaterialStatus(needCheck?1:2);
                receiveMaterialReqDTO.setLocationId(updateReqVO.getLocationId());
                receiveMaterialReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                receiveMaterialReqDTOList.add(receiveMaterialReqDTO);
            }
            CommonResult<List<MaterialStockRespDTO>> commonResult =  materialStockApi.receiveMaterial(receiveMaterialReqDTOList);

            List<ConsignmentDetailDO> detailDOList = new ArrayList<>();
            if (commonResult.isSuccess()){
                //更新码记录
                List<MaterialStockRespDTO> list = commonResult.getCheckedData();
                for (int i = 0; i<detailDOS.size();i++){
                    ConsignmentDetailDO detailDO = detailDOS.get(i);
                    MaterialStockRespDTO respDTO = list.get(i);
                    detailDO.setMaterialStockId(respDTO.getId());
                    detailDO.setBarCode(respDTO.getBarCode());
                    detailDO.setBatchNumber(respDTO.getBatchNumber());
                    detailDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
                    detailDO.setLocationId(updateReqVO.getLocationId());
                    detailDO.setSignedAmount(new BigDecimal(1));
                    detailDO.setSignedBy(getLoginUserId().toString());
                    detailDO.setSignedTime(LocalDateTime.now());
                    detailDOList.add(detailDO);
                }

            }else {
                throw exception(CONSIGNMENT_SIGN_ERROR);
            }
            // 2。更新状态
            consignmentInfoDO.setSchemeResult(needCheck?0:1);
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());

            consignmentInfoMapper.updateById(consignmentInfoDO);
            consignmentDetailMapper.updateBatch(detailDOList);
            //3.查看整单状态
            List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoMapper.selectListByConsignmentId(consignmentInfoDO.getConsignmentId());

            Boolean isFinish = true;
            for (ConsignmentInfoDO infoDO : consignmentInfoDOS){
                //如果完成 更新主单状态
                if (!infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.FINISH.getStatus())&&!infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.REJECT.getStatus())){
                    isFinish = false;
                }
            }
            if (isFinish){
                consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
                consignmentMapper.updateById(consignmentDO);
            }


        }else {
            //签收数量和收货数量不一致
            //初始化异常处理

            InboundExceptionHandlingDO inboundExceptionHandlingDO = BeanUtils.toBean(consignmentInfoDO,InboundExceptionHandlingDO.class);

            inboundExceptionHandlingDO.setExceptionType(consignmentInfoDO.getConsignedAmount().compareTo(updateReqVO.getSignedAmount()) >0?1:2);
            inboundExceptionHandlingDO.setId(null);
            inboundExceptionHandlingDO.setInfoId(consignmentInfoDO.getId());
            inboundExceptionHandlingDO.setNo(consignmentDO.getNo());
            inboundExceptionHandlingDO.setStatus(0);
            inboundExceptionHandlingDO.setCompanyId(contractDO ==null?consignmentDO.getCompanyId():contractDO.getParty());
            inboundExceptionHandlingDO.setProjectId(consignmentDO.getProjectId());
            inboundExceptionHandlingDO.setContractId(contractDO !=null?contractDO.getId():null);

            inboundExceptionHandlingMapper.insert(inboundExceptionHandlingDO);
            //状态更新到待确认
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.CONFIRM.getStatus());

            if (consignmentInfoDO.getConsignedAmount().compareTo(updateReqVO.getSignedAmount()) >0){
                //如果数量不足 则需要把后几个作废
                int value= (consignmentInfoDO.getConsignedAmount().subtract(updateReqVO.getSignedAmount())).intValue();
                for (int i = 0;i<detailDOS.size();i++){
                    if (i<detailDOS.size()-value){
                        detailDOS.set(i,detailDOS.get(i).setConsignmentStatus(ConsignmentStatusEnum.CONFIRM.getStatus()).setLocationId(updateReqVO.getLocationId()))
                        .setSignedAmount(new BigDecimal(1))
                        .setSignedBy(getLoginUserId().toString())
                        .setSignedTime(LocalDateTime.now());;
                    }else {
                        detailDOS.set(i,detailDOS.get(i).setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus()));
                    }
                }

            }else {
                detailDOS.forEach(consignmentDetailDO -> {
                    consignmentDetailDO.setConsignmentStatus(ConsignmentStatusEnum.CONFIRM.getStatus());
                    consignmentDetailDO.setSignedAmount(new BigDecimal(1));
                    consignmentDetailDO.setSignedBy(getLoginUserId().toString());
                    consignmentDetailDO.setSignedTime(LocalDateTime.now());
                });
            }


            consignmentInfoMapper.updateById(consignmentInfoDO);
            consignmentDetailMapper.updateBatch(detailDOS);
        }




    }

    @Override
    public void signMaterial(@Valid ConsignmentInfoSaveReqVO updateReqVO) {
        ConsignmentInfoDO consignmentInfoDO = consignmentInfoMapper.selectById(updateReqVO.getId());
        consignmentInfoDO.setSignedAmount(new BigDecimal(updateReqVO.getIds().size()));
        consignmentInfoDO.setSignedBy(getLoginUserId().toString());
        consignmentInfoDO.setSignedTime(LocalDateTime.now());
        consignmentInfoDO.setLocationId(updateReqVO.getLocationId());
        List<ConsignmentDetailDO> detailDOS = consignmentDetailMapper.selectList(ConsignmentDetailDO::getInfoId, updateReqVO.getId());
        ContractDO contractDO = contractService.getContractById(consignmentInfoDO.getContractId());
        ConsignmentDO consignmentDO = consignmentMapper.selectById(consignmentInfoDO.getConsignmentId());
        if (detailDOS.size() == updateReqVO.getIds().size()){

            List<ReceiveMaterialReqDTO> receiveMaterialReqDTOList = new ArrayList<>();
            //调用WMS生码逻辑
            for (ConsignmentDetailDO detailDO :detailDOS){
                ReceiveMaterialReqDTO receiveMaterialReqDTO = new ReceiveMaterialReqDTO();
                receiveMaterialReqDTO.setBarCode(detailDO.getBarCode());
                //todo  具体参数参考
                receiveMaterialReqDTO.setMaterialStatus(1);
                receiveMaterialReqDTO.setLocationId(updateReqVO.getLocationId());
                receiveMaterialReqDTO.setMaterialConfigId(detailDO.getMaterialConfigId());
                receiveMaterialReqDTOList.add(receiveMaterialReqDTO);
            }
            CommonResult<List<MaterialStockRespDTO>> commonResult =  materialStockApi.receiveMaterial(receiveMaterialReqDTOList);

            List<ConsignmentDetailDO> detailDOList = new ArrayList<>();
            if (commonResult.isSuccess()){
                //更新码记录
                List<MaterialStockRespDTO> list = commonResult.getCheckedData();
                for (int i = 0; i<detailDOS.size();i++){
                    ConsignmentDetailDO detailDO = detailDOS.get(i);
                    MaterialStockRespDTO respDTO = list.get(i);
                    detailDO.setMaterialStockId(respDTO.getId());
                    detailDO.setBarCode(respDTO.getBarCode());
                    detailDO.setBatchNumber(respDTO.getBatchNumber());
                    detailDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
                    detailDO.setLocationId(updateReqVO.getLocationId());
                    detailDO.setSignedAmount(new BigDecimal(1));
                    detailDO.setSignedBy(getLoginUserId().toString());
                    detailDO.setSignedTime(LocalDateTime.now());
                    detailDOList.add(detailDO);
                }

            }else {
                throw exception(CONSIGNMENT_SIGN_ERROR);
            }
            // 2。更新状态
            consignmentInfoDO.setSchemeResult(0);
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());

            consignmentInfoMapper.updateById(consignmentInfoDO);
            consignmentDetailMapper.updateBatch(detailDOList);
            //3.查看整单状态
            List<ConsignmentInfoDO> consignmentInfoDOS = consignmentInfoMapper.selectListByConsignmentId(consignmentInfoDO.getConsignmentId());

            Boolean isFinish = true;
            for (ConsignmentInfoDO infoDO : consignmentInfoDOS){
                //如果完成 更新主单状态
                if (!infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.FINISH.getStatus()) &&!infoDO.getConsignmentStatus().equals(ConsignmentStatusEnum.REJECT.getStatus())){
                    isFinish = false;
                }
            }
            if (isFinish){
                consignmentDO.setConsignmentStatus(ConsignmentStatusEnum.FINISH.getStatus());
                consignmentMapper.updateById(consignmentDO);
            }

        }else {

            //签收数量和收货数量不一致
            //初始化异常处理

            InboundExceptionHandlingDO inboundExceptionHandlingDO = BeanUtils.toBean(consignmentInfoDO,InboundExceptionHandlingDO.class);
            inboundExceptionHandlingDO.setExceptionType(1);
            inboundExceptionHandlingDO.setId(null);
            inboundExceptionHandlingDO.setInfoId(consignmentInfoDO.getId());
            inboundExceptionHandlingDO.setNo(consignmentDO.getNo());
            inboundExceptionHandlingDO.setStatus(0);
            inboundExceptionHandlingDO.setCompanyId(contractDO ==null?consignmentDO.getCompanyId():contractDO.getParty());
            inboundExceptionHandlingDO.setProjectId(consignmentDO.getProjectId());
            inboundExceptionHandlingDO.setContractId(contractDO !=null?contractDO.getId():null);

            inboundExceptionHandlingMapper.insert(inboundExceptionHandlingDO);
            //状态更新到待确认
            consignmentInfoDO.setConsignmentStatus(ConsignmentStatusEnum.CONFIRM.getStatus());

            detailDOS.forEach(consignmentDetailDO -> {
                if (updateReqVO.getIds().contains(consignmentDetailDO.getId())){
                    consignmentDetailDO.setConsignmentStatus(ConsignmentStatusEnum.CONFIRM.getStatus());
                    consignmentDetailDO.setLocationId(updateReqVO.getLocationId());
                    consignmentDetailDO.setSignedAmount(new BigDecimal(1));
                    consignmentDetailDO.setSignedBy(getLoginUserId().toString());
                    consignmentDetailDO.setSignedTime(LocalDateTime.now());
                }else {
                    consignmentDetailDO.setConsignmentStatus(ConsignmentStatusEnum.REJECT.getStatus());
                }
            });


            consignmentInfoMapper.updateById(consignmentInfoDO);
            consignmentDetailMapper.updateBatch(detailDOS);




        }
    }

    @Override
    public void deleteConsignmentInfo(String id) {
        // 校验存在
        validateConsignmentInfoExists(id);
        // 删除
        consignmentInfoMapper.deleteById(id);
    }

    private void validateConsignmentInfoExists(String id) {
        if (consignmentInfoMapper.selectById(id) == null) {
            throw exception(CONSIGNMENT_INFO_NOT_EXISTS);
        }
    }

    @Override
    public ConsignmentInfoDO getConsignmentInfo(String id) {
        return consignmentInfoMapper.selectById(id);
    }

    @Override
    public PageResult<ConsignmentInfoDO> getConsignmentInfoPage(ConsignmentInfoPageReqVO pageReqVO) {
        return consignmentInfoMapper.selectPage(pageReqVO);
    }

    @Override
    public List<ConsignmentInfoDO> getConsignmentInfoByContractId(String contractId,String consignmentId) {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eqIfPresent(ConsignmentInfoDO::getContractId,contractId)
                .neIfPresent(ConsignmentInfoDO::getConsignmentStatus,ConsignmentStatusEnum.REJECT.getStatus());
        if (StringUtils.isNotBlank(consignmentId)){
            wrapperX.ne(ConsignmentInfoDO::getConsignmentId,consignmentId);
        }
        return consignmentInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentInfoDO> getConsignmentInfoByConsignmentId(String consignmentId) {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.eqIfPresent(ConsignmentInfoDO::getConsignmentId,consignmentId)
                .neIfPresent(ConsignmentInfoDO::getConsignmentStatus,ConsignmentStatusEnum.REJECT.getStatus());
        return consignmentInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentInfoDO> getConsignmentInfos(ConsignmentInfoQueryReqVO reqVO) {
        return consignmentInfoMapper.getConsignmentInfos(reqVO);
    }

    @Override
    public List<ConsignmentCompanyNumberRespVO> getCompanyConsignmentAmount(LocalDateTime[] createTimeRange) {
        return consignmentInfoMapper.getCompanyConsignmentAmount(createTimeRange[0],createTimeRange[1]);
    }

    @Override
    public List<ConsignmentInfoDO> getConsignmentInfoByOrderIds(List<String> orderIds) {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inIfPresent(ConsignmentInfoDO::getOrderId,orderIds)
                .eqIfPresent(ConsignmentInfoDO::getConsignmentStatus,ConsignmentStatusEnum.FINISH.getStatus());
        return consignmentInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentInfoDO> getConsignmentInfoByShippingIds(List<String> shippingIds) {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inIfPresent(ConsignmentInfoDO::getShippingId, shippingIds)
                .ne(ConsignmentInfoDO::getConsignmentStatus, ConsignmentStatusEnum.CANCEL.getStatus());
        return consignmentInfoMapper.selectList(wrapperX);
    }

    @Override
    public List<ConsignmentInfoDO> getConsignmentInfoByContractIds(List<String> contractIds) {
        MPJLambdaWrapperX<ConsignmentInfoDO> wrapperX = new MPJLambdaWrapperX<>();
        wrapperX.inIfPresent(ConsignmentInfoDO::getContractId, contractIds)
                .eq(ConsignmentInfoDO::getConsignmentStatus, ConsignmentStatusEnum.FINISH.getStatus());
        return consignmentInfoMapper.selectList(wrapperX);
    }

}