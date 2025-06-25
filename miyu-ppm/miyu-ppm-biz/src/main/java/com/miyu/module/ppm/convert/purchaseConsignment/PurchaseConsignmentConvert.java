package com.miyu.module.ppm.convert.purchaseConsignment;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.company.dto.CompanyRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.WmsDTO;
import com.miyu.module.ppm.controller.admin.contract.vo.ContractOrderRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignment.vo.PurchaseConsignmentRespVO;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.controller.admin.warehousedetail.vo.WarehouseDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.companyproduct.CompanyProductDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.consignment.ConsignmentDO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.warehousedetail.WarehouseDetailDO;
import org.apache.commons.lang3.StringUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface PurchaseConsignmentConvert {

    PurchaseConsignmentConvert INSTANCE = Mappers.getMapper(PurchaseConsignmentConvert.class);

    default List<PurchaseConsignmentRespVO> convertList(List<ConsignmentDO> list, Map<Long, AdminUserRespDTO> userMap, Map<String, ContractDO> contractMap, Map<String, CompanyRespDTO> companyMap) {
        return CollectionUtils.convertList(list, purchaseConsignmentDO ->
        {
            PurchaseConsignmentRespVO purchaseConsignmentRespVO = BeanUtils.toBean(purchaseConsignmentDO, PurchaseConsignmentRespVO.class);
            if (StringUtils.isNotBlank(purchaseConsignmentDO.getConsignedBy()))
                MapUtils.findAndThen(userMap, Long.valueOf(purchaseConsignmentDO.getConsignedBy()), a -> purchaseConsignmentRespVO.setConsignedBy(a.getNickname()));
            ContractDO contractRespDTO  = contractMap.get(purchaseConsignmentDO.getContractId());
            if(contractRespDTO != null){
                purchaseConsignmentRespVO.setContractNo(contractRespDTO.getNumber()).setContractName(contractRespDTO.getName());
                MapUtils.findAndThen(companyMap, contractRespDTO.getParty(), a -> purchaseConsignmentRespVO.setCompanyName(a.getName()));
                purchaseConsignmentRespVO.setContractType(contractRespDTO.getContractType());
            }
            return purchaseConsignmentRespVO;
        });

    }

    default List<ContractOrderRespVO> convertOrderList(List<ContractOrderDO> list, Map<String, MaterialConfigRespDTO> map, Map<String, BigDecimal> numberMap) {
        return CollectionUtils.convertList(list, orderRespDTO ->
        {
            ContractOrderRespVO orderRespVO = BeanUtils.toBean(orderRespDTO, ContractOrderRespVO.class);

            MapUtils.findAndThen(map, orderRespDTO.getMaterialId(), a -> orderRespVO.setMaterialName(a.getMaterialName()).setMaterialCode(a.getMaterialTypeCode())
                    .setMaterialNumber(a.getMaterialNumber()).setMaterialType(a.getMaterialTypeId()).setMaterialUnit(a.getMaterialUnit())
                    .setMaterialSpecification(a.getMaterialSpecification()).setMaterialBrand(a.getMaterialBrand()).setMaterialConfigId(a.getId()));
            BigDecimal number = numberMap.get(orderRespDTO.getId());
            if (number==null){
                orderRespVO.setFinishQuantity(new BigDecimal(0));
            }else {
                orderRespVO.setFinishQuantity(number);
            }
            return orderRespVO;
        });

    }

    default WmsDTO convertMaterial(List<ConsignmentDetailDO> detailDOS, Map<String, ContractOrderRespDTO> orderMap, ConsignmentDO consignmentDO) {
        WmsDTO wmsDTO = new WmsDTO();
        wmsDTO.setConsignmentId(consignmentDO.getId());

        List<WmsDTO.WmsMaterialInfo> materialInfos = CollectionUtils.convertList(detailDOS, shippingDetailDO ->
        {
            WmsDTO.WmsMaterialInfo wmsMaterialInfo = new WmsDTO.WmsMaterialInfo();
            wmsMaterialInfo.setMaterialTypeId(orderMap.get(shippingDetailDO.getOrderId()).getMaterialId());
            wmsMaterialInfo.setNumber(shippingDetailDO.getConsignedAmount());
            return wmsMaterialInfo;
        });
        wmsDTO.setInfoList(materialInfos);
        return wmsDTO;
    }

    /**
     * 获取采购退货可选项
     * @param consignmentDO
     * @param detailDO
     * @param materialConfigRespDTO
     * @return
     */
    default PurchaseConsignmentDetailRespVO getConsignmentDetail(ConsignmentDO consignmentDO, ConsignmentDetailDO detailDO, List<MaterialConfigRespDTO> materialConfigRespDTO ) {
        PurchaseConsignmentDetailRespVO purchaseConsignmentDetailRespVO = BeanUtils.toBean(detailDO, PurchaseConsignmentDetailRespVO.class);
        return purchaseConsignmentDetailRespVO.setConsignmentId(consignmentDO.getId())
                .setNo(consignmentDO.getNo())
                .setConsignmentId(consignmentDO.getId())
                .setName(consignmentDO.getName());
    }

    /**
     * 一物一码数据添加免检信息
     * @param list
     * @param productMap
     * @return
     */
    default List<WarehouseDetailRespVO>  queryWarehouseCheck( List<WarehouseDetailDO> list ,Map<String , CompanyProductDO> productMap){
        return CollectionUtils.convertList(list,warehouseDetailDO ->{
            WarehouseDetailRespVO vo = BeanUtils.toBean(warehouseDetailDO,WarehouseDetailRespVO.class);
            MapUtils.findAndThen(productMap,vo.getMaterialConfigId(), a -> vo.setQualityCheck(a.getQualityCheck()));
            return vo;
        });
    }



}
