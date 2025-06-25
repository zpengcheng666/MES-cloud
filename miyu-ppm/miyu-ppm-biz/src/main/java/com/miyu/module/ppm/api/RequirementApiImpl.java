package com.miyu.module.ppm.api;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.ppm.api.purchaseRequirement.RequirementApi;
import com.miyu.module.ppm.api.purchaseRequirement.dto.PurchaseRequirementDTO;
import com.miyu.module.ppm.controller.admin.purchaserequirement.vo.PurchaseRequirementSaveReqVO;
import com.miyu.module.ppm.service.purchaserequirement.PurchaseRequirementService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;


@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class RequirementApiImpl implements RequirementApi {

    @Resource
    private PurchaseRequirementService requirementService;

    @Override
    public CommonResult<Boolean> createPurchaseRequirement(PurchaseRequirementDTO requirementDTO) {
        requirementService.createPurchaseRequirementPMS(BeanUtils.toBean(requirementDTO, PurchaseRequirementSaveReqVO.class));
        return CommonResult.success(true);
    }

    @Override
    public CommonResult<String> updateRequirementAuditStatus(String bussinessKey, Integer status) {
        requirementService.updateRequirementAuditStatus(bussinessKey, status);
        return null;
    }
}
