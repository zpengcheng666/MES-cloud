package com.miyu.module.mcc.api.encodingrule;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miyu.module.mcc.api.encodingrule.dto.DemoDTO;
import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
import com.miyu.module.mcc.api.encodingrule.dto.UpdateCodeReqDTO;
import com.miyu.module.mcc.controller.admin.coderecord.vo.CodeRecordSaveReqVO;
import com.miyu.module.mcc.controller.admin.materialconfig.vo.MaterialConfigSaveReqVO;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import com.miyu.module.mcc.dal.dataobject.encodingrule.EncodingRuleDO;
import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import com.miyu.module.mcc.enums.MccConstants;
import com.miyu.module.mcc.service.coderecord.CodeRecordService;
import com.miyu.module.mcc.service.encodingrule.EncodingRuleService;
import com.miyu.module.mcc.service.encodingruledetail.EncodingRuleDetailService;
import com.miyu.module.mcc.service.materialconfig.MaterialConfigService;
import com.miyu.module.mcc.service.materialtype.MaterialTypeService;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.mcc.enums.ErrorCodeConstants.RULE_CHECK_ERROR;

@RestController
@Validated
public class EncodingRuleApiImpl implements EncodingRuleApi {
    @Resource
    private EncodingRuleService encodingRuleService;
    @Resource
    private CodeRecordService codeRecordService;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    private EncodingRuleDetailService encodingRuleDetailService;
    @Resource
    private MaterialTypeService materialTypeService;

    @Override
    public CommonResult<DemoDTO> getAndGeneratorMaterialConfig(DemoDTO dto) {

        List<MaterialConfigDO> mainList = materialConfigService.getMaterialConfigListByCode(dto.getPartNumber());
        List<MaterialConfigDO> configDOS = materialConfigService.getMaterialConfigListByCode(dto.getMaterialConfigNumber());

        DemoDTO demoDTO = new DemoDTO();
        demoDTO = BeanUtils.toBean(dto, DemoDTO.class);
        demoDTO.setPartTypeId(mainList.get(0).getId());
        if (!CollectionUtils.isEmpty(configDOS)) {
            demoDTO.setMaterialConfigId(configDOS.get(0).getId());
        } else {
            //插入字段

            MaterialConfigSaveReqVO newConfigDo = BeanUtils.toBean(mainList.get(0), MaterialConfigSaveReqVO.class);
            newConfigDo.setMaterialNumber(dto.getMaterialConfigNumber());
            newConfigDo.setMaterialSourceId(mainList.get(0).getId());
            newConfigDo.setId(null);
            //如果图号为成品 则生成半成品
            if (mainList.get(0).getMaterialTypeCode().equals("CP")){
                List<MaterialTypeDO> typeDOS = materialTypeService.getMaterialTypeByCode("BP");
                newConfigDo.setMaterialTypeId(typeDOS.get(0).getId());
                newConfigDo.setMaterialTypeCode(typeDOS.get(0).getCode());
            }
            String id = materialConfigService.createMaterialConfigNoSubmit(newConfigDo);

            demoDTO.setMaterialConfigId(id);
        }

        return CommonResult.success(demoDTO);
    }

    @Override
    public CommonResult<String> generatorCode(GeneratorCodeReqDTO dto) throws InterruptedException {

        EncodingRuleDO ruleDO = encodingRuleService.generatorCode1(dto);

        return CommonResult.success(ruleDO.getGeneratorCode());
    }

    @Override
    public CommonResult<String> generatorNewCodeByOldCode(GeneratorCodeReqDTO dto) throws InterruptedException, JsonProcessingException {

        String oldCode = dto.getOldBarCode();
        //获取源码的信息
        CodeRecordDO recordDO = codeRecordService.getCodeRecordByCode(oldCode);
        //获取源码信息
        ObjectMapper mapper = new ObjectMapper();
        GeneratorCodeReqDTO reqDTO= mapper.readValue(recordDO.getParams(),GeneratorCodeReqDTO.class);
        EncodingRuleDO ruleDO = encodingRuleService.generatorCode(reqDTO);

        return CommonResult.success(ruleDO.getGeneratorCode());
    }

    @Override
    public CommonResult<String> updateCodeStatus(UpdateCodeReqDTO dto) throws InterruptedException {
        CodeRecordDO recordDO = codeRecordService.getCodeRecordByCode(dto.getCode());

        recordDO.setStatus(2);
        codeRecordService.updateCodeRecord(BeanUtils.toBean(recordDO, CodeRecordSaveReqVO.class));
        return CommonResult.success(dto.getCode());
    }
}
