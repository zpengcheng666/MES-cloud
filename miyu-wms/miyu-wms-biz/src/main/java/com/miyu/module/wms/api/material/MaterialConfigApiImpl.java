package com.miyu.module.wms.api.material;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.dto.MCCMaterialConfigReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigReqDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigSaveReqDTO;
import com.miyu.module.wms.controller.admin.materialconfig.vo.MaterialConfigSaveReqVO;
import com.miyu.module.wms.convert.materialconfig.MaterialConfigConvert;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static com.miyu.module.wms.enums.ErrorCodeConstants.MATERIAL_TYPE_PARENT_NOT_EXITS;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class MaterialConfigApiImpl implements MaterialConfigApi {

    @Resource
    private MaterialConfigService materialconfigService;

    @Override
    public CommonResult<String> createMaterialConfig(MCCMaterialConfigReqDTO reqDTO) {
        return success(materialconfigService.createMaterialConfig(MaterialConfigConvert.INSTANCE.convertSave(reqDTO)));
    }

    @Override
    public CommonResult<Boolean> updateMaterialConfig(MCCMaterialConfigReqDTO reqDTO) {
        MaterialConfigDO mainConfig = materialconfigService.getMaterialConfig(reqDTO.getId());
        if(mainConfig == null){
            return success(createMaterialConfig(reqDTO).isSuccess());
        }
        MaterialConfigSaveReqVO saveReqVO = MaterialConfigConvert.INSTANCE.convertSave(reqDTO);
        saveReqVO.setId(mainConfig.getId());
        materialconfigService.updateMaterialConfig(saveReqVO);
        return success(true);
    }

    @Override
    public CommonResult<Boolean> otherUpdateMaterialConfig(MaterialConfigSaveReqDTO reqVO) {
        MaterialConfigSaveReqVO bean = BeanUtils.toBean(reqVO, MaterialConfigSaveReqVO.class);
        materialconfigService.updateMaterialConfig(bean);
        return success(true);
    }

    @Override
    public CommonResult<Boolean> deleteMaterialConfig(String id) {
        materialconfigService.deleteMaterialConfig(id);
        return success(true);
    }

    @Override
    public CommonResult<List<MaterialConfigRespDTO>> getMaterialConfigList(Collection<String> ids) {
        return success(BeanUtils.toBean(materialconfigService.getMaterialConfigListByIds(ids), MaterialConfigRespDTO.class));
    }

    @Override
    public CommonResult<MaterialConfigRespDTO> getMaterialConfigById(String id) {
        return CommonResult.success(BeanUtils.toBean(materialconfigService.getMaterialConfig(id), MaterialConfigRespDTO.class));

    }

    //    @Override
//    public CommonResult<Long> sendSingleMailToMember(MailSendSingleToUserReqDTO reqDTO) {
//        return success(mailSendService.sendSingleMailToMember(reqDTO.getMail(), reqDTO.getUserId(),
//                reqDTO.getTemplateCode(), reqDTO.getTemplateParams()));
//    }

}
