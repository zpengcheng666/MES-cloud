package com.miyu.module.qms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.qms.api.dto.UnqualifiedMaterialReqDTO;
import com.miyu.module.qms.api.dto.UnqualifiedMaterialRespDTO;
import com.miyu.module.qms.api.unqualifiedmaterial.UnqualifiedMaterialApi;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import com.miyu.module.qms.service.inspectionsheet.InspectionSheetService;
import com.miyu.module.qms.service.unqualifiedmaterial.UnqualifiedMaterialService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class UnqualifiedMaterialApiImpl implements UnqualifiedMaterialApi {

    @Resource
    private UnqualifiedMaterialService unqualifiedMaterialService;

    @Resource
    private InspectionSheetService InspectionSheetService;

    @Override
    public CommonResult<String> updateUnqualifiedMaterial(List<UnqualifiedMaterialReqDTO> reqDTOList) {
        unqualifiedMaterialService.updateUnqualifiedMaterialApproveResultBatch(reqDTOList);
        return null;
    }

    @Override
    public CommonResult<List<UnqualifiedMaterialRespDTO>> getUnqualifiedMaterialListByBarCodes(Collection<String> barCodes) {

        List<UnqualifiedMaterialDO> list = unqualifiedMaterialService.getUnqualifiedMaterialListByBarCodes(barCodes);

        return CommonResult.success(BeanUtils.toBean(list, UnqualifiedMaterialRespDTO.class));
    }

    /**
     * 更新不合格品审批状态
     * @param businessKey
     * @param status
     * @return
     */
    @Override
    public CommonResult<String> updateUnqualifiedAuditStatus(String businessKey, Integer status) {
        InspectionSheetService.updateUnqualifiedAuditStatus(businessKey, status);
        return null;
    }
}
