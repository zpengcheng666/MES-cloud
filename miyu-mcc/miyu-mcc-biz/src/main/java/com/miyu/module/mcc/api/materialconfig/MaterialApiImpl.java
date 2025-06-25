package com.miyu.module.mcc.api.materialconfig;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.mcc.controller.admin.materialconfig.vo.MaterialConfigSaveReqVO;
import com.miyu.module.mcc.service.materialconfig.MaterialConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class MaterialApiImpl implements MaterialMCCApi {

    @Resource
    private MaterialConfigService materialconfigService;

    @Override
    public CommonResult<String> createMaterialConfig(MaterialConfigReqDTO reqDTO) {
        return success(materialconfigService.createMaterialConfig(BeanUtils.toBean(reqDTO, MaterialConfigSaveReqVO.class)));
    }

    @Override
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigList(Collection<String> ids) {
        return success(BeanUtils.toBean(materialconfigService.getMaterialConfigListByIds(ids), MaterialConfigRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigListByTypeCode(MaterialConfigReqDTO reqDTO) {
        return success(BeanUtils.toBean(materialconfigService.getMaterialConfigListByTypeCode(reqDTO), MaterialConfigRespDTO.class));
    }

    @Override
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigListByCode(Collection<String> codes) {
        return success(BeanUtils.toBean(materialconfigService.getMaterialConfigListByCodes(codes), MaterialConfigRespDTO.class));

    }

    @Override
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigListByTypeId(String materialTypeId) {
        return success(BeanUtils.toBean(materialconfigService.getMaterialConfigListByTypeId(materialTypeId), MaterialConfigRespDTO.class));
    }

    @Override
    public CommonResult<String> updateAudit(String businessKey, Integer status) {
        materialconfigService.updateAudit(businessKey, status);
        return success("成功");
    }

}
