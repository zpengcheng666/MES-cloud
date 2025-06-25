package com.miyu.cloud.mcs.restServer.service.encoding;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.mcs.dto.resource.McsMaterialConfigDTO;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
import com.miyu.module.mcc.api.encodingrule.dto.DemoDTO;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.api.encodingrule.dto.UpdateCodeReqDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

@Service
@Validated
public class EncodingServiceImpl implements EncodingService {

    @Resource
    private EncodingRuleApi encodingRuleApi;

    @Override
    public McsMaterialConfigDTO getMaterialConfig(McsMaterialConfigDTO materialConfigDTO) {
        CommonResult<DemoDTO> commonResult = encodingRuleApi.getAndGeneratorMaterialConfig(BeanUtils.toBean(materialConfigDTO, DemoDTO.class));
        if (!commonResult.isSuccess()) throw new ServiceException(commonResult.getCode(), commonResult.getMsg());
        McsMaterialConfigDTO mcsMaterialConfigDTO = BeanUtils.toBean(commonResult.getData(), McsMaterialConfigDTO.class);
        return mcsMaterialConfigDTO;
    }

    @Override
    public String getDistributionCode() {
        return getCodeyType("MCS_D");
    }

    @Override
    public String getCodeyType(String typeCode) {
        GeneratorCodeReqDTO reqDTO = new GeneratorCodeReqDTO();
        reqDTO.setEncodingRuleType(1);
        reqDTO.setClassificationCode(typeCode);
        CommonResult<String> result;
        try {
            result = encodingRuleApi.generatorCode(reqDTO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        return result.getData();
    }

    @Override
    public void updateMaterialConfigCodeStatus(String materialConfigNumber) {
        try {
            encodingRuleApi.updateCodeStatus(new UpdateCodeReqDTO().setCode(materialConfigNumber));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
