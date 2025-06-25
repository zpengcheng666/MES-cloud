package com.miyu.module.ppm.convert.consignmentReturn;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.collection.MapUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.ppm.api.contract.dto.ContractOrderRespDTO;
import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.ConsignmentMaterialRespVO;
import com.miyu.module.ppm.controller.admin.consignmentreturn.vo.ConsignmentReturnRespVO;
import com.miyu.module.ppm.controller.admin.contractconsignment.vo.ContractConsignmentRespVO;
import com.miyu.module.ppm.dal.dataobject.company.CompanyDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturn.ConsignmentReturnDO;
import com.miyu.module.ppm.dal.dataobject.consignmentreturndetail.ConsignmentReturnDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractDO;
import com.miyu.module.ppm.dal.dataobject.contractconsignment.ContractConsignmentDO;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import org.apache.commons.lang3.StringUtils;
import org.checkerframework.checker.units.qual.C;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Mapper
public interface ConsignmentReturnConvert {

    ConsignmentReturnConvert INSTANCE = Mappers.getMapper(ConsignmentReturnConvert.class);



}
