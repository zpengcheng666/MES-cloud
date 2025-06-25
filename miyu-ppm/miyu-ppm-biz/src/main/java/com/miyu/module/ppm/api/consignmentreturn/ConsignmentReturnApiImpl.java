package com.miyu.module.ppm.api.consignmentreturn;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.ppm.api.consignmentReturn.ConsignmentReturnApi;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ReturnMaterialDTO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnService;
import com.miyu.module.ppm.service.consignmentreturn.ConsignmentReturnServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController
@Validated
public class ConsignmentReturnApiImpl implements ConsignmentReturnApi {

    @Resource
    private ConsignmentReturnService consignmentReturnService;
    @Autowired
    private ConsignmentReturnServiceImpl consignmentReturnServiceImpl;

    /**
     * 更新审批状态
     * @param businessKey
     * @param status
     * @return
     */
    @Override
    public CommonResult<String> updateConsignmentReturnStatus(String businessKey, Integer status) {

        consignmentReturnService.updateConsignmentProcessInstanceStatus(businessKey,status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<List<ConsignmentReturnDTO>> getConsignmentReturnDetailByContractIds(Collection<String> ids) {
        return CommonResult.success(consignmentReturnService.getConsignmentReturnDetailByContractIds(ids));
    }

    @Override
    public CommonResult<List<ReturnMaterialDTO>> getReturnByCodes(Collection<String> barCodes) {

        List<ShippingDetailDO> shippingDOS =  consignmentReturnService.getReturnByBarCodes(barCodes);

        return CommonResult.success(BeanUtils.toBean(shippingDOS, ReturnMaterialDTO.class));
    }

}
