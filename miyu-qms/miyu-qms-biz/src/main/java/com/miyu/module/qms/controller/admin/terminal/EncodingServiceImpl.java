package com.miyu.module.qms.controller.admin.terminal;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.mcc.api.encodingrule.EncodingRuleApi;
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
    public String getDistributionCode() {
        GeneratorCodeReqDTO reqDTO = new GeneratorCodeReqDTO();
        reqDTO.setEncodingRuleType(1);
        reqDTO.setClassificationCode("QMS");
        CommonResult<String> result = null;
        try {
            result = encodingRuleApi.generatorCode(reqDTO);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        try {
            encodingRuleApi.updateCodeStatus(new UpdateCodeReqDTO().setCode(result.getData()));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return result.getData();
    }

}
